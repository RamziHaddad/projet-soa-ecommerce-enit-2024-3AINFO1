package com.example;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.response.UpdateResponse;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.enterprise.context.ApplicationScoped;

@Path("/solr")
@ApplicationScoped
public class SolrIndexerService {

    private static final String SOLR_URL = "http://localhost:8983/solr/products-index";
    private final SolrClient solrClient = new HttpSolrClient.Builder(SOLR_URL).build();

    @POST
    @Path("/index")
    public Response indexProduct(Product product) {
        try {
            // Crée un document Solr avec les champs que vous souhaitez indexer
            org.apache.solr.client.solrj.beans.SolrInputDocument document = new org.apache.solr.client.solrj.beans.SolrInputDocument();
            document.addField("id", product.getIdProduit());
            document.addField("Description", product.getDescription());

            // Indexe le document dans Solr
            UpdateResponse response = solrClient.add(document);
            solrClient.commit();

            return Response.ok("Document indexed successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error indexing document: " + e.getMessage()).build();
        }
    }

    // Classe interne représentant le produit
    public static class Product {
        @Field
        private String id;

        @Field
        private String Description;

        // Getters et setters
        public String getIdProduit() {
            return id;
        }

        public void setIdProduit(String idProduit) {
            this.id = idProduit;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            this.Description = description;
        }
    }
}
