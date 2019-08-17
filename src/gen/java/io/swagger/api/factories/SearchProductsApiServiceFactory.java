package io.swagger.api.factories;

import io.swagger.api.SearchProductsApiService;
import za.co.ennui.solr.api.impl.SearchProductsApiServiceImpl;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen",
    date = "2019-08-08T11:22:47.637Z[GMT]")
public class SearchProductsApiServiceFactory {
  private final static SearchProductsApiService service = new SearchProductsApiServiceImpl();

  public static SearchProductsApiService getSearchProductsApi() {
    return service;
  }
}
