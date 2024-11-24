package com.ecommerce.cart;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cart")
public class CartResource {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToCart(CartItem item) {
        // Add item to the cart logic
        return Response.ok().entity("{\"message\": \"Item added to cart\"}").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart() {
        // Return items in the cart
        return Response.ok().entity("[{\"productId\": 1, \"quantity\": 2}]").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCartItem(@PathParam("id") int id) {
        // Return a specific cart item by ID
        return Response.ok().entity("{\"productId\": 1, \"quantity\": 2}").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFromCart(@PathParam("id") int id) {
        // Logic to remove an item from the cart
        return Response.ok().entity("{\"message\": \"Item removed from cart\"}").build();
    }
}
