package za.co.ennui.solr.api.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.api.SearchProductsApiService;
import io.swagger.model.Product;
import io.swagger.model.SearchKeywords;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaJerseyServerCodegen",
    date = "2019-08-08T11:22:47.637Z[GMT]")
public class SearchProductsApiServiceImpl extends SearchProductsApiService {

  private static final Logger LOG = LoggerFactory.getLogger(SearchProductsApiServiceImpl.class);

  private final SolrClient solrClient;

  public SearchProductsApiServiceImpl() {
    super();
    this.solrClient = buildSolrClient();
  }

  /**
   * This constructor only exists for mock testing.
   * 
   * @param solrClient a mocked SolrClient
   */
  public SearchProductsApiServiceImpl(final SolrClient solrClient) {
    super();
    this.solrClient = solrClient;
  }

  @Override
  public Response searchProducts(final SearchKeywords body, final SecurityContext securityContext) {

    Map<String, String> queryParamMap = new ConcurrentHashMap<>();

    // the score field is not returned by default
    queryParamMap.put("fl", "*,score");

    // transform the keywords into the q parameter
    StringBuffer qqParameterSB = new StringBuffer();
    if (body != null && body.getQuery() != null && body.getQuery().size() > 0) {
      String separator = "";
      for (String keyWord : body.getQuery()) {
        qqParameterSB.append(separator);
        qqParameterSB.append(keyWord);
        separator = " ";
      }
    } else {
      return CatchAllExceptionMapper.buildFailureResponse(Status.BAD_REQUEST,
          "No keywords specified");
    }
    queryParamMap.put("q", qqParameterSB.toString());

    // start parameter
    if (body.getStart() != null) {
      queryParamMap.put("start", body.getStart().toString());
    }

    // rows parameter
    if (body.getRows() != null) {
      queryParamMap.put("rows", body.getRows().toString());
    }

    MapSolrParams queryParams = new MapSolrParams(queryParamMap);

    try {
      QueryResponse response = solrClient.query(collection, queryParams);
      SolrDocumentList documents = response.getResults();

      int numberOfProducts = documents.size();
      Product[] products = new Product[numberOfProducts];
      for (int i = 0; i < numberOfProducts; i++) {
        Product product = new Product();
        SolrDocument solrDocument = documents.get(i);
        product.setId((String) solrDocument.getFieldValue("id"));
        product.setUrl("https://" + solrDocument.getFieldValue("manu_id_s") + ".com/"
            + solrDocument.getFieldValue("id")); // TODO URL
        product.setScore((float) solrDocument.getFieldValue("score"));
        product.setManufacturerName((String) solrDocument.getFieldValue("manu"));

        // TODO last modified date
        product.setLastModifiedAt((Date) solrDocument.getFieldValue("manufacturedate_dt"));
        products[i] = product;
      }

      return Response.ok().entity(products).build();
    } catch (SolrServerException | IOException e) {
      LOG.error("The SOLR service return an exception", e);
      return CatchAllExceptionMapper.buildFailureResponse(Status.INTERNAL_SERVER_ERROR,
          "The SOLR service returned an exception: " + e.getMessage());
    }

  }

  /**
   * Builds the SolrClient using the startup arguments.
   * 
   */
  private SolrClient buildSolrClient() {

    try {
      Class<?> clazz = Class.forName(solrClientClass + "$Builder");

      // This constructor is deprecated but it allows the url to be
      // passed more easily
      Constructor<?> ctor = clazz.getConstructor();
      Object object = ctor.newInstance();
      Class<?> builderClazz = object.getClass();

      if (solrClientClass.equals("org.apache.solr.client.solrj.impl.CloudSolrClient")) {
        object = builderClazz.getMethod("withSolrUrl", String.class).invoke(object, solrUrl);
      } else if (solrClientClass.equals("org.apache.solr.client.solrj.impl.HttpSolrClient")
          || solrClientClass.equals("org.apache.solr.client.solrj.impl.LBHttpSolrClient")) {
        object = builderClazz.getMethod("withBaseSolrUrl", String.class).invoke(object, solrUrl);
      } else {
        throw new IllegalArgumentException(
            "No SolrClient matches provided value: " + solrClientClass);
      }

      object = builderClazz.getMethod("withConnectionTimeout", int.class).invoke(object,
          connectionTimeout);

      object = builderClazz.getMethod("withSocketTimeout", int.class).invoke(object, socketTimeout);

      Method buildMethod = builderClazz.getMethod("build");
      object = buildMethod.invoke(object);

      return (SolrClient) object;

    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException
        | InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException exception) {
      LOG.error("Problems creating instance of SolrClient", exception);
      throw new ApplicationException("Problems creating instance of SolrClient", exception);
    }

  }

}
