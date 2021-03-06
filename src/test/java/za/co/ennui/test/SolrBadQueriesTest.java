package za.co.ennui.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.params.MapSolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.swagger.api.NotFoundException;
import io.swagger.api.SearchProductsApiService;
import io.swagger.model.SearchKeywords;

import za.co.ennui.solr.api.impl.SearchProductsApiServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SolrBadQueriesTest {


  private static final String NO_KEYWORDS_SPECIFIED = "No keywords specified";
  private static final String REASON_PHRASE_MESSAGE =
      "The reason phrase should be " + NO_KEYWORDS_SPECIFIED;

  @Mock
  private SolrClient solrClientMock;

  static {
    SearchProductsApiService.solrUrl = "http://localhost:8983/solr";
    SearchProductsApiService.collection = "techproducts";
  }

  @Mock
  private SecurityContext securityContext;


  @Test
  public void testNullBodyQuery() throws NotFoundException {

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(null, securityContext);

    assertEquals("An HTTP-400 response should be returned for a null body query", 400,
        response.getStatus());
    assertEquals(REASON_PHRASE_MESSAGE, response.getStatusInfo().getReasonPhrase(),
        NO_KEYWORDS_SPECIFIED);

  }


  @Test
  public void testEmptyBodyQuery() throws NotFoundException {

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(new SearchKeywords(), securityContext);

    assertEquals("An HTTP-400 response should be returned for an empty body query", 400,
        response.getStatus());
    assertEquals(REASON_PHRASE_MESSAGE, response.getStatusInfo().getReasonPhrase(),
        NO_KEYWORDS_SPECIFIED);

  }

  @Test
  public void testNullQueriesQuery() throws NotFoundException {

    SearchKeywords body = new SearchKeywords();

    body.query(null);

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(body, securityContext);

    assertEquals("An HTTP-400 response should be returned for a null queries query", 400,
        response.getStatus());
    assertEquals(REASON_PHRASE_MESSAGE, response.getStatusInfo().getReasonPhrase(),
        NO_KEYWORDS_SPECIFIED);

  }

  @Test
  public void testZeroQueriesQuery() throws NotFoundException {

    SearchKeywords body = new SearchKeywords();

    body.query(new ArrayList<String>());

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(body, securityContext);

    assertEquals("An HTTP-400 response should be returned for a zero keywords query", 400,
        response.getStatus());
    assertEquals(REASON_PHRASE_MESSAGE, response.getStatusInfo().getReasonPhrase(),
        NO_KEYWORDS_SPECIFIED);

  }

  @Test
  public void testBadSolrSystem()
      throws NotFoundException, ParseException, SolrServerException, IOException {

    when(solrClientMock.query(eq("non_exist"), any(MapSolrParams.class)))
        .thenThrow(new SolrServerException("Mocked exception"));

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    SearchProductsApiService.collection = "non_exist";

    SearchKeywords body = SetupUtils.buildValidSearch();

    Response response = searchProductsApi.searchProducts(body, securityContext);

    assertEquals("Should return an HTTP 500 for a non-supported Solr client class",
        response.getStatus(), 500);
    assertEquals(
        "Should return an HTTP 500: The SOLR service returned an exception: Mocked exception",
        response.getStatusInfo().getReasonPhrase(),
        "The SOLR service returned an exception: Mocked exception");


  }


}
