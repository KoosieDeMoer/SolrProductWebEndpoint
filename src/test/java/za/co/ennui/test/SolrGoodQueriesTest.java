package za.co.ennui.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.swagger.api.NotFoundException;
import io.swagger.api.SearchProductsApiService;
import io.swagger.model.Product;
import io.swagger.model.SearchKeywords;

import za.co.ennui.solr.api.impl.SearchProductsApiServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class SolrGoodQueriesTest {


  @Mock
  private SolrClient solrClientMock;

  @Mock
  private QueryResponse solrQueryResponseMock;

  static {
    SearchProductsApiService.solrUrl = "http://localhost:8983/solr";
    SearchProductsApiService.collection = "techproducts";
  }

  @Mock
  private SecurityContext securityContext;


  @Test
  public void testGoldenQuery() throws NotFoundException, ParseException {

    SearchKeywords body = SetupUtils.buildValidSearch();

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(body, securityContext);
    Product[] products = (Product[]) response.getEntity();

    assertEquals("Expecting 2 products to be returned", products.length, 2);
    SetupUtils.assertProductsCorrect(products);

  }

  @Test
  public void testNoStartOrRowsQuery() throws NotFoundException, ParseException {

    SearchKeywords body = SetupUtils.buildValidSearch();
    body.setStart(null);
    body.setRows(null);

    SearchProductsApiServiceImpl searchProductsApi =
        new SearchProductsApiServiceImpl(solrClientMock);
    Response response = searchProductsApi.searchProducts(body, securityContext);
    Product[] products = (Product[]) response.getEntity();

    assertEquals("Expecting 2 products to be returned", products.length, 2);
    SetupUtils.assertProductsCorrect(products);

  }



  @Before
  public void setupMocks() throws SolrServerException, IOException {

    SolrDocumentList solrDocumentList = SetupUtils.buildDocListResponses();

    when(solrClientMock.query(eq("non_exist"), any(MapSolrParams.class)))
        .thenThrow(new SolrServerException("Mocked exception"));

    when(solrClientMock.query(eq(SearchProductsApiService.collection), any(MapSolrParams.class)))
        .thenReturn(solrQueryResponseMock);
    when(solrQueryResponseMock.getResults()).thenReturn(solrDocumentList);

  }

}
