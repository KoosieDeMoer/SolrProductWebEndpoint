package io.swagger.api;

import io.swagger.model.SearchKeywords;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen",
    date = "2019-08-08T11:22:47.637Z[GMT]")
public abstract class SearchProductsApiService extends Object {

  /**
   * options for solrClientClass are: org.apache.solr.client.solrj.impl.HttpSolrClient
   * org.apache.solr.client.solrj.impl.LBHttpSolrClient
   * org.apache.solr.client.solrj.impl.CloudSolrClient
   */
  public static String solrClientClass = "org.apache.solr.client.solrj.impl.HttpSolrClient";
  public static int socketTimeout = 60_000;
  public static int connectionTimeout = 10_000;
  public static String solrUrl;
  public static String collection;

  /**
   * Searches for products on the solr instance that match the query in body.
   * 
   * @param body search quest, start and rows
   * @param securityContext
   * @return
   * @throws NotFoundException
   */
  public abstract Response searchProducts(SearchKeywords body, SecurityContext securityContext)
      throws NotFoundException;

}
