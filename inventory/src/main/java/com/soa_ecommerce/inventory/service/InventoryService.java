package com.soa_ecommerce.inventory.service;


import com.soa_ecommerce.inventory.domain.Product;
import com.soa_ecommerce.inventory.domain.Reservation;
import com.soa_ecommerce.inventory.domain.ReservationDetails;
import com.soa_ecommerce.inventory.dto.InventoryRequest;
import com.soa_ecommerce.inventory.exception.InsufficientQuantityException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyCancelledException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyExistsException;
import com.soa_ecommerce.inventory.exception.ReservationAlreadyReleasedException;
import com.soa_ecommerce.inventory.repository.ProductRepository;
import com.soa_ecommerce.inventory.repository.ReservationDetailsRepository;
import com.soa_ecommerce.inventory.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service

public class InventoryService {

    private final ProductRepository productRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailsRepository reservationDetailsRepository;

    public InventoryService(ProductRepository productRepository, ReservationRepository reservationRepository, ReservationDetailsRepository reservationDetailsRepository) {
        this.productRepository = productRepository;
        this.reservationRepository = reservationRepository;
        this.reservationDetailsRepository = reservationDetailsRepository;
    }

    //ajout d'un produit dans le stock
    @Transactional(rollbackFor = Exception.class)
    public void receiveProduct(UUID productId, Integer quantity){

        productRepository.findById(productId).ifPresentOrElse(
               product ->  {
                   product.setTotalQuantity(product.getTotalQuantity()+quantity);
                   productRepository.save(product);
               },
                ()->{
                    Product newProduct = Product.builder()
                            .productId(productId)
                            .totalQuantity(quantity)
                            .build();
                    productRepository.save(newProduct);
                }
        );

    }
    public boolean isExists(UUID productId) {
        return productRepository.existsById(productId);
    }

    //sortie d'une commande
    @Transactional(rollbackFor = Exception.class)
    public void releaseOrder(UUID orderId){
        Reservation reservation=reservationRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("Reservation "+ orderId +" not found"));
        if(reservation.isReleased()){
            throw new ReservationAlreadyReleasedException("Reservation already released");
        }
        reservationDetailsRepository.deleteAll(reservation.getReservationsDetails());
        reservation.setReleased(true);
        reservationRepository.save(reservation);
    }



    //Reservation d'un produit
    @Transactional(rollbackFor = Exception.class)
    public void reserveProduct(InventoryRequest request) {
        if(reservationRepository.findByOrderId(request.orderId()).isPresent()){
            throw new ReservationAlreadyExistsException("Reservation already exists");
        }
        Reservation reservation=Reservation.builder()
                .orderId(request.orderId())
                .cancelled(false)
                .released(false)
                .build();
        request.products().forEach(orderItem -> {
            Product product = productRepository.findById(orderItem.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product "+ orderItem.productId() +" not found"));

            if (product.getTotalQuantity() < orderItem.quantity()) {
                throw new InsufficientQuantityException(
                        "Insufficient quantity for product " + orderItem.productId());
            }

            product.setTotalQuantity(product.getTotalQuantity() - orderItem.quantity());
            ReservationDetails reservationDetails=ReservationDetails.builder()
                    .product(product)
                    .reservation(reservation)
                    .quantityReserved(orderItem.quantity())
                    .build();
            reservation.getReservationsDetails().add(reservationDetails);
            reservationDetailsRepository.save(reservationDetails);
            productRepository.save(product);


        });
        reservationRepository.save(reservation);
    }

    //annulation de la commande
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(UUID orderId) {
        Reservation reservation=reservationRepository.findByOrderId(orderId).orElseThrow(() -> new EntityNotFoundException("Reservation "+ orderId+" not found"));
        if(reservation.isCancelled()){
            throw new ReservationAlreadyCancelledException("Reservation already cancelled");
        }
        reservation.getReservationsDetails().forEach(
                reservationDetail-> {
                    Product product = productRepository.findById(reservationDetail.getProduct().getProductId())
                            .orElseThrow(() -> new EntityNotFoundException("Product "+ reservationDetail.getProduct().getProductId()+" not found"));
                    product.setTotalQuantity(product.getTotalQuantity()+reservationDetail.getQuantityReserved());
                    productRepository.save(product);
                    reservationDetailsRepository.delete(reservationDetail);
                    }

        );
        reservation.setCancelled(true);
        reservationRepository.save(reservation);
    }




}
