package com.example.demo.searchservice.services;

import com.example.demo.searchservice.models.Product;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Product> getAllProductsFromSolr(String coreName) {
        List<Product> products = new ArrayList<>();
        try {
            SolrQuery query = new SolrQuery("*:*");  // Requête pour obtenir tous les produits
            QueryResponse response = solrClient.query(coreName, query);
            SolrDocumentList docs = response.getResults();

            for (SolrDocument doc : docs) {
                String id = (String) doc.getFieldValue("id");
                List<String> descriptions = (List<String>) doc.getFieldValue("description");
                products.add(new Product(id, descriptions));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving products from Solr: " + e.getMessage());
        }
        return products;
    }

    public List<Product> findSimilarProducts(String coreName, String productId) {
        try {
            // Configuration des paramètres pour "More Like This"
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("q", "id:" + productId); // Produit de référence
            params.set("mlt", true); // Activer "More Like This"
            params.set("mlt.fl", "description"); // Champ utilisé pour la similarité
            params.set("mlt.mindf", 0); // Fréquence minimale d'un mot dans les documents
            params.set("mlt.mintf", 0); // Fréquence minimale d'un mot dans le champ
            params.set("mlt.count", 10); // Nombre maximum de résultats
            params.set("rows", 10); // Nombre maximum de résultats similaires

            QueryRequest request = new QueryRequest(params);
            QueryResponse response = request.process(solrClient, coreName);

            // Debug : Afficher la réponse brute
            System.out.println("Réponse brute : " + response);

            // Récupération des documents similaires depuis moreLikeThis
            NamedList<SolrDocumentList> moreLikeThis = (NamedList<SolrDocumentList>) response.getResponse().findRecursive("moreLikeThis");
            if (moreLikeThis == null) {
                System.out.println("Aucun produit similaire trouvé pour l'ID " + productId);
                return Collections.emptyList();
            }

            // Accéder aux produits similaires pour l'ID donné
            SolrDocumentList similarDocs = moreLikeThis.get(productId);
            if (similarDocs == null || similarDocs.isEmpty()) {
                System.out.println("Aucun produit similaire trouvé pour l'ID " + productId);
                return Collections.emptyList();
            }

            // Convertir les résultats en objets Product
            List<Product> similarProducts = new ArrayList<>();
            for (SolrDocument doc : similarDocs) {
                Product product = new Product();
                product.setId((String) doc.getFieldValue("id"));
                product.setDescription((List<String>) doc.getFieldValue("description"));
                similarProducts.add(product);
            }

            System.out.println("Produits similaires retournés : " + similarProducts);
            return similarProducts;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }
    // Méthode pour rechercher un produit par texte libre
    public List<Product> searchByText(String core, String queryText) throws Exception {
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.set("qt", "select");
            solrQuery.set("q", "description:\"" + queryText + "\"");
            solrQuery.setRows(10);
            solrQuery.set("fl", "id,description");

            System.out.println("Query sent to Solr: " + solrQuery.toString());

            QueryResponse response = solrClient.query(core, solrQuery);
            SolrDocumentList documents = response.getResults();

            System.out.println("Number of documents returned: " + documents.size());

            // Conversion des résultats en objets Product
            return documents.stream().map(doc -> {
                String id = (String) doc.getFieldValue("id");

                // Gérer le champ description comme une liste
                Object descriptionField = doc.getFieldValue("description");
                List<String> description = null;
                if (descriptionField instanceof List) {
                    description = (List<String>) descriptionField;
                } else if (descriptionField instanceof String) {
                    description = List.of((String) descriptionField);
                }

                return new Product(id, description);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error during Solr query: " + e.getMessage());
            throw new Exception("Error retrieving products by text search from Solr: " + e.getMessage(), e);
        }
    }










}

