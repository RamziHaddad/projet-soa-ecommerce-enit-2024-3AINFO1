package com.example.demo.searchservice.services;

import com.example.demo.searchservice.models.Product;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private SolrClient solrClient;

    // Méthode pour indexer un produit dans Solr
    public void addProduct(String core, Product product) throws Exception {
        solrClient.addBean(core, product);
        solrClient.commit(core);
    }

    // Méthode pour récupérer tous les produits
    public List<Product> getAllProducts() throws Exception {
        try {
            SolrQuery query = new SolrQuery("*:*");
            QueryResponse response = solrClient.query("products-index", query);
            SolrDocumentList documents = response.getResults();

            System.out.println("Number of products found: " + documents.size());

            // Conversion des documents en objets Product
            return documents.stream().map(doc -> {
                String id = (String) doc.getFieldValue("id");

                // Gérer le champ "description" (liste ou chaîne unique)
                Object descriptionField = doc.getFieldValue("description");
                List<String> description = null;
                if (descriptionField instanceof List) {
                    description = (List<String>) descriptionField;
                } else if (descriptionField instanceof String) {
                    description = List.of((String) descriptionField); // Convertir une chaîne en liste
                }

                return new Product(id, description);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error retrieving products from Solr: " + e.getMessage(), e);
        }
    }

}
