package io.swagger.api;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.Api;
import io.swagger.api.factories.SearchProductsApiServiceFactory;
import io.swagger.model.Product;
import io.swagger.model.SearchKeywords;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Api
@Path("/search_products")


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen",
    date = "2019-08-08T11:22:47.637Z[GMT]")
public class SearchProductsApi {
  private final SearchProductsApiService delegate;

  public SearchProductsApi(@Context ServletConfig servletContext) {
    SearchProductsApiService delegate = null;

    if (servletContext != null) {
      String implClass = servletContext.getInitParameter("SearchProductsApi.implementation");
      if (implClass != null && !"".equals(implClass.trim())) {
        try {
          delegate = (SearchProductsApiService) Class.forName(implClass).newInstance();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    if (delegate == null) {
      delegate = SearchProductsApiServiceFactory.getSearchProductsApi();
    }

    this.delegate = delegate;
  }

  @POST

  @Consumes({"application/json"})
  @Produces({"application/json"})
  @Operation(summary = "Searches and retrieves products based on query keywords",
      description = "Search for products in the database by passing in keywords which describe the product.  Multiple keywords will result in 'AND' query.  Note we are using POST instead of GET to return products.",
      tags = {})
  @ApiResponses(value = {@ApiResponse(responseCode = "200",
      description = "Return an array of products found.  If no products are found return an empty array.",
      content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),

      @ApiResponse(responseCode = "400",
          description = "Invalid parameter was specified in SearchKeyword parameters. Such as no object, no keyword or invalid json object being sent."),

      @ApiResponse(responseCode = "500", description = "If server error occured in the backend")})
  public Response searchProducts(@Parameter(
      description = "pass single or multiple keywords as strings to find related products.") SearchKeywords body

      , @Context SecurityContext securityContext) throws NotFoundException {
    return delegate.searchProducts(body, securityContext);
  }
}
