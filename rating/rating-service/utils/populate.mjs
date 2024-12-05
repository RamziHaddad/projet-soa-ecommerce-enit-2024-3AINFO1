import pg from 'pg';
import fetch from "node-fetch";
import "dotenv/config";

const { Client } = pg;

const client = new Client({
  user: process.env.DB_USER || "postgres",
  host: process.env.DB_HOST || "db_rating",
  database: process.env.DB_NAME || "db_rating",
  password: process.env.DB_PASSWORD || "azerty",
  port: process.env.DB_PORT || 5432,
});

async function populateProducts() {
  try {
    console.log("Connecting to the database...");
    await client.connect();

    console.log("Fetching products data...");
    const response = await fetch("https://api.escuelajs.co/api/v1/products");
    const products = await response.json();

    console.log("Populating database with products...");
    for (const product of products) {
      const flattenedImages = product.images.flatMap(img => Array.isArray(img) ? img : [img]);
      const query = `
        INSERT INTO products (id, title, price, description, category_id, images) 
        VALUES ($1, $2, $3, $4, $5, $6)
        ON CONFLICT (id) DO NOTHING;
      `;
      //const imagesJson = JSON.stringify(product.images);
      

      await client.query(query, [
        product.id,
        product.title,
        product.price,
        product.description,
        product.category?.id || null,
        flattenedImages,
      ]);
    }

    console.log("Products populated successfully!");
  } catch (error) {
    console.error("Error populating products:", error);
  } finally {
    await client.end();
  }
}

populateProducts();