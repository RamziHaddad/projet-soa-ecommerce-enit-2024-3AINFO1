package com.soa_ecommerce.inventory.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soa_ecommerce.inventory.domain.Product;
import com.soa_ecommerce.inventory.domain.Reservation;
import com.soa_ecommerce.inventory.dto.InventoryRequest;
import com.soa_ecommerce.inventory.exception.InsufficientQuantityException;
import com.soa_ecommerce.inventory.kafka.producer.MessageProducer;
import com.soa_ecommerce.inventory.repository.ProductRepository;
import com.soa_ecommerce.inventory.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class InventoryService {
    private static final String INVENTORY_RESERVE_TOPIC="inventory-reserve-request";
    private static final String INVENTORY_RESERVE_RESULT_TOPIC = "inventory-reserve-result";
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;
    private final MessageProducer messageProducer;

    public InventoryService(ObjectMapper objectMapper, ProductRepository productRepository, ReservationRepository reservationRepository, MessageProducer messageProducer) {
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
        this.reservationRepository = reservationRepository;
        this.messageProducer = messageProducer;
    }


    //ajout d'un produit dans le stock
    public void receiveProduct(UUID productId, Integer quantity){

        productRepository.findById(productId).ifPresentOrElse(
               product ->  {
                   product.setTotalQuantity(product.getTotalQuantity()+quantity);
                   productRepository.save(product);
               },
                ()->{
                    Product newInventory = Product.builder()
                            .productId(productId)
                            .totalQuantity(quantity)

                            .build();
                    productRepository.save(newInventory);
                }
        );

    }


    public boolean isExists(UUID productId) {
        return productRepository.existsById(productId);
    }

    //Reservation d'un produit
    @Transactional
    @KafkaListener(topics = INVENTORY_RESERVE_TOPIC, groupId = "order-group")
    public void reserveProduct(String message){
        Map<String, Object> reservationResult = new HashMap<>();

        try {
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);

            UUID orderId = UUID.fromString((String) messageMap.get("orderId"));
            List<Map<String, Object>> items = (List<Map<String, Object>>) messageMap.get("Items");


            if (reservationRepository.findById(orderId).isPresent()) {
                reservationResult.put("orderId", orderId);
                reservationResult.put("success", true);
                reservationResult.put("message", "Order reservation already exists.");
                messageProducer.sendMessage(INVENTORY_RESERVE_RESULT_TOPIC, reservationResult);
                return;
            }

            for (Map<String, Object> item : items) {
                UUID productId = UUID.fromString((String) item.get("productID"));
                Integer quantity = (Integer) item.get("quantity");

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNotFoundException("Product " + productId + " not found"));

                if (product.getTotalQuantity() < quantity) {
                    throw new InsufficientQuantityException("Insufficient quantity for product " + productId);
                }

                product.setTotalQuantity(product.getTotalQuantity() - quantity);
                productRepository.save(product);

                reservationRepository.save(
                        Reservation.builder()
                                .orderId(orderId)
                                .product(product)
                                .quantityReserved(quantity)
                                .build()
                );
            }

            reservationResult.put("orderId", orderId);
            reservationResult.put("success", true);
            reservationResult.put("message", "Order reservation done successfully!");
            messageProducer.sendMessage(INVENTORY_RESERVE_RESULT_TOPIC, reservationResult);

        }catch (JsonProcessingException ex) {
            // Handle JSON parsing errors
            reservationResult.put("orderId", null);
            reservationResult.put("success", false);
            reservationResult.put("message", "Failed to parse message: " + ex.getMessage());
            messageProducer.sendMessage(INVENTORY_RESERVE_RESULT_TOPIC, reservationResult);
            throw new RuntimeException("Error parsing JSON message", ex);
        } catch (EntityNotFoundException | InsufficientQuantityException ex) {
            // Handle specific exceptions
            reservationResult.put("orderId", null);
            reservationResult.put("success", false);
            reservationResult.put("message", ex.getMessage());
            messageProducer.sendMessage(INVENTORY_RESERVE_RESULT_TOPIC, reservationResult);
            throw ex; // Re-throw to trigger Kafka retries and transactional rollback

        } catch (Exception ex) {
            // Handle unexpected exceptions
            reservationResult.put("orderId", null);
            reservationResult.put("success", false);
            reservationResult.put("message", "Unexpected error occurred: " + ex.getMessage());
            messageProducer.sendMessage(INVENTORY_RESERVE_RESULT_TOPIC, reservationResult);
            throw ex; // Re-throw to ensure rollback and Kafka retries
        }

    }


    //sortie d'une commande
    @Transactional
    public void releaseOrder(List<InventoryRequest> request){
        /*
        request.forEach(inventoryRequest -> {
            Product product = productRepository.findById(inventoryRequest.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product "+ inventoryRequest.productId() +" not found"));

            if (product.getReservedQuantity() <= inventoryRequest.quantity()) {
                throw new InsufficientQuantityException("Cannot release this product "+ inventoryRequest.productId()+",Not enough quantity, only "+inventory.getReservedQuantity());
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() - inventoryRequest.quantity());
            productRepository.save(inventory);
        });

         */
    }




    //annulation de la commande
    @Transactional
    public void cancelOrder(List<InventoryRequest> requests) {
        /*
        requests.forEach(request -> {
            // Récupérer l'inventaire pour le produit
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product " + request.productId() + " not found"));

            // Vérifier si la quantité réservée est suffisante pour l'annulation
            if (product.getReservedQuantity() < request.quantity()) {
                throw new InsufficientQuantityException(
                        "Cannot release this product " + request.productId() + ": insufficient reserved quantity");
            }

            // Mettre à jour les quantités
            product.setTotalQuantity(product.getTotalQuantity() + request.quantity());
            product.setReservedQuantity(product.getReservedQuantity() - request.quantity());

            // Sauvegarder les changements dans la base de données
            productRepository.save(product);
        });
        */

    }


}
