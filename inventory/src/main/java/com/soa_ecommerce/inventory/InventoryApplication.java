package com.soa_ecommerce.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}
	/*@Bean
	public CommandLineRunner initializeInventory(InventoryRepository inventoryRepository) {
		return args -> {
			inventoryRepository.save(
					Inventory.builder()
							.productId(UUID.randomUUID())
							.totalQuantity(100)
							.reservedQuantity(10)
							.build()
			);

			inventoryRepository.save(
					Inventory.builder()
							.productId(UUID.randomUUID())
							.totalQuantity(200)
							.reservedQuantity(20)
							.build()
			);
		};
	}*/

}
