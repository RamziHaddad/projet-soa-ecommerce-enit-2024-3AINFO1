package com.ecommerce.cart;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class CartResourceTest {

    @Test
void testAddToCart() {
    String jsonPayload = "{ \"productId\": 1, \"quantity\": 2 }";

    given()
      .contentType("application/json")
      .body(jsonPayload)
      .when().post("/cart")
      .then()
         .statusCode(200)
         .body("message", is("Item added to cart"));
}

@Test
void testGetCart() {
    given()
      .when().get("/cart")
      .then()
         .statusCode(200)
         .body("size()", is(1));
}

@Test
void testGetCartItemById() {
    given()
      .when().get("/cart/1")
      .then()
         .statusCode(200)
         .body("productId", is(1));
}

@Test
void testRemoveFromCart() {
    given()
      .when().delete("/cart/1")
      .then()
         .statusCode(200)
         .body("message", is("Item removed from cart"));
}
