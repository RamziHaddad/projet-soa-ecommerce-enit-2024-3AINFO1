package tn.soa_ecommerce.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.service.OrderService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final Mapper<Order, OrderDTO> orderMapper;

    public OrderController(OrderService orderService, Mapper<Order, OrderDTO> orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    // Create Order API
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO request) {
        try {
            Order order = orderMapper.mapFrom(request);
            OrderDTO createdOrder = orderService.createOrder(order);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            System.out.println("error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
        try {
            Optional<Order> orderOptional = orderService.getOrderById(id);
            if (orderOptional.isPresent()) {
                OrderDTO orderDTO = orderMapper.mapTo(orderOptional.get());
                return new ResponseEntity<>(orderDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   /* @PutMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID id) {
        try {
            boolean isCancelled = orderService.cancelOrder(id);
            if (isCancelled) {
                return new ResponseEntity<>("Order canceled successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Order not found or cannot be canceled", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/
}