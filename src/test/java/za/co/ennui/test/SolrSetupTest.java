package za.co.ennui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.swagger.api.NotFoundException;
import io.swagger.api.SearchProductsApiService;

import za.co.ennui.solr.api.impl.CatchAllExceptionMapper;
import za.co.ennui.solr.api.impl.SearchProductsApiServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SolrSetupTest {

  private static final String COLLECTION_NAME = "techproducts";

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Mock
  private Throwable throwable;

  @Test
  public void testGoldenSetup() throws NotFoundException {

    SearchProductsApiService.solrClientClass = "org.apache.solr.client.solrj.impl.HttpSolrClient";
    SearchProductsApiService.solrUrl = "http://localhost:8983/solr";
    SearchProductsApiService.collection = COLLECTION_NAME;

    SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNotNull("An instance of SearchProductsApiServiceImpl should have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testCloudSolrClientSetup() throws NotFoundException {

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Problems creating instance of SolrClient");

    SearchProductsApiService.solrClientClass = "org.apache.solr.client.solrj.impl.CloudSolrClient";
    SearchProductsApiService.solrUrl = "http://localhost:8983/solr";
    SearchProductsApiService.collection = COLLECTION_NAME;

    SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNotNull("An instance of SearchProductsApiServiceImpl should have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testLbHttpSolrClientSetup() throws NotFoundException {

    SearchProductsApiService.solrClientClass = "org.apache.solr.client.solrj.impl.LBHttpSolrClient";
    SearchProductsApiService.solrUrl = "http://localhost:8983/solr";
    SearchProductsApiService.collection = COLLECTION_NAME;

    SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNotNull("An instance of SearchProductsApiServiceImpl should have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testBadSolrUrlSetup() throws NotFoundException {

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Problems creating instance of SolrClient");

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Problems creating instance of SolrClient");
    SearchProductsApiService.solrUrl = null;
    SearchProductsApiService.collection = COLLECTION_NAME;

    SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNull("An instance of SearchProductsApiServiceImpl should not have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testBadSolrClientClassSetup() throws NotFoundException {

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Problems creating instance of SolrClient");

    SearchProductsApiService.solrClientClass = "org.apache.solr.client.solrj.impl.Http2SolrClient";

    final SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNull("An instance of SearchProductsApiServiceImpl should not have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testVeryBadSolrClientClassSetup() throws NotFoundException {

    exceptionRule.expect(RuntimeException.class);
    exceptionRule.expectMessage("Problems creating instance of SolrClient");

    SearchProductsApiService.solrClientClass = "Bad";

    final SearchProductsApiServiceImpl searchProductsApi = new SearchProductsApiServiceImpl();

    assertNull("An instance of SearchProductsApiServiceImpl should not have been instantiated",
        searchProductsApi);

  }

  @Test
  public void testCatchAllExceptionHandler() throws NotFoundException {

    final CatchAllExceptionMapper catchAllToJsonExceptionMapper = new CatchAllExceptionMapper();

    when(throwable.getLocalizedMessage()).thenReturn("Big trouble here!");

    final Response response = catchAllToJsonExceptionMapper.toResponse(throwable);

    assertEquals("Big trouble here!", response.getStatus(), 500);



  }

  @Test
  public void testCatchAllExceptionHandlerExceptions() throws NotFoundException {

    final CatchAllExceptionMapper catchAllToJsonExceptionMapper = new CatchAllExceptionMapper();

    when(throwable.getLocalizedMessage()).thenThrow(new RuntimeException());

    final Response response = catchAllToJsonExceptionMapper.toResponse(throwable);

    assertEquals("Exception while handling exception", response.getStatus(), 500);


  }


}
