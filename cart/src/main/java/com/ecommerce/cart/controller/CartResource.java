package com.ecommerce.cart.controller;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;

@Path("/cart")
public class CartResource {

    // Add to Cart
    @POST
@Path("/add")
@Produces(MediaType.APPLICATION_JSON)
public Response addToCart(CartDTO cartDTO) {
   

    // Map CartDTO to CartItem
    CartItem item = new CartItem();
    item.setUserId(cartDTO.getUserId());
    item.setProductId(cartDTO.getProductId());
    item.setQuantity(cartDTO.getQuantity());

    // Find the cart by userId
    Cart cart = Cart.find("userId", item.getUserId()).firstResult();

    if (cart == null) {
        // No cart found, create a new one
        cart = new Cart();
        cart.userId = item.getUserId();
        cart.items = new ArrayList<>(); // Ensure the list is initialized
        cart.totalPrice = 0.0; // Initialize total price
    }

    // Add the item to the cart
    cart.items.add(item);
    cart.totalPrice += item.getQuantity() * 10.0; // Example price calculation

    // Persist the cart (handles both new and updated carts)
    cart.persist();
    
    return Response.ok(cart).build();
}



    // Get All Items in Cart for a specific user
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart() {
        Cart cart = Cart.find("userId", 1L).firstResult();  // Change to dynamic userId if needed
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Cart not found\"}").build();
        }
        return Response.ok(cart).build();
    }

    // Get Specific Cart Item by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItem(@PathParam("id") Long id) {
        CartItem cartItem = CartItem.findById(id); // Find CartItem by ID
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Cart item not found\"}").build();
        }
        return Response.ok(cartItem).build();
    }

    // Remove Item from Cart by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromCart(@PathParam("id") Long id) {
        boolean isDeleted = CartItem.deleteById(id);  // Delete CartItem by ID
        if (!isDeleted) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"message\": \"Cart item not found\"}").build();
        }
        return Response.ok("{\"message\": \"Item removed from cart\"}").build();
    }
}
