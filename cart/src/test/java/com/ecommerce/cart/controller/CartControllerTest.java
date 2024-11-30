package com.ecommerce.cart.controller;

import com.ecommerce.cart.dto.CartDTO;
import com.ecommerce.cart.service.CartService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CartControllerTest {

    @Inject
    CartService cartService;
    @Test
    public void testAddItemToCart() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(1L); // Ensure this ID exists or is valid
        cartDTO.setProductId("prod123");
        cartDTO.setQuantity(2);
    
        given()
            .contentType("application/json")
            .body(cartDTO)
        .when()
            .post("/cart/add") // Ensure this matches your resource path
        .then()
            .statusCode(200) // Expect HTTP 200 response
            .body("userId", is(1)) // Update checks based on response structure
            .body("totalPrice", is(20.0f)); // Adjust as needed
    }
    

}
