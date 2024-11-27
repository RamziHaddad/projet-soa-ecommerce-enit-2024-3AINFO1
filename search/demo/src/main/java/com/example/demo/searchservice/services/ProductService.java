package com.example.demo.searchservice.services;

import com.example.demo.searchservice.models.Product;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    @Autowired
    private SolrClient solrClient;

    public void addProduct(String coreName, Product product) {
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", product.getId());
            document.addField("description", product.getDescription());
            System.out.println("Adding product: " + product.getId() + " to Solr core: " + coreName);

            solrClient.add(coreName, document);  // Ajouter le document à Solr
            solrClient.commit(coreName);  // Valider les changements dans Solr

            System.out.println("Product indexed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error indexing product: " + e.getMessage());
        }
    }


    // Méthode pour récupérer tous les produits (exemple)
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try {
            // Ici, vous pourriez récupérer des produits depuis Solr, une base de données ou un autre système
            // Pour l'exemple, nous ajoutons des produits fictifs
            products.add(new Product("1", "Product 1 Description"));
            products.add(new Product("2", "Product 2 Description"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving products: " + e.getMessage());
        }
        return products;
    }
}
