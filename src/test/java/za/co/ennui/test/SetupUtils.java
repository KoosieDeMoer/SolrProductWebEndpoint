package za.co.ennui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import io.swagger.model.Product;
import io.swagger.model.SearchKeywords;

import za.co.ennui.solr.api.impl.ApplicationException;

public class SetupUtils {


  private static SimpleDateFormat SDF =
      new SimpleDateFormat("EEE MMM dd MM:HH:ss ZZZ yyyy", Locale.ROOT);

  /**
   * Does all the shared assertions.
   * 
   * @param products as returned from the API
   */
  public static void assertProductsCorrect(final Product[] products) {

    assertEquals(products[0].getId(), "SP2514N");
    assertEquals(products[0].getManufacturerName(), "Samsung Electronics Co. Ltd.");
    assertEquals(products[0].getUrl(), "https://samsung.com/SP2514N");
    try {
      assertEquals(products[0].getLastModifiedAt(),
          SetupUtils.SDF.parse("Mon Feb 13 17:26:37 CAT 2006"));
    } catch (ParseException e) {
      throw new ApplicationException("Can't actually get here", e);
    }
    assertEquals(products[0].getScore(), new Float(0.7826174f));

    assertEquals(products[1].getId(), "F8V7067-APL-KIT");
    assertEquals(products[1].getManufacturerName(), "Belkin");
    assertEquals(products[1].getUrl(), "https://belkin.com/F8V7067-APL-KIT");
    try {
      assertEquals(products[1].getLastModifiedAt(),
          SetupUtils.SDF.parse("Mon Aug 01 18:30:25 CAT 2005"));
    } catch (ParseException e) {
      throw new ApplicationException("Can't actually get here", e);
    }
    assertEquals(products[1].getScore(), new Float(0.7163829f));

  }

  /**
   * Build a valid search.
   * 
   * @return a valid search
   */
  public static SearchKeywords buildValidSearch() {
    SearchKeywords body = new SearchKeywords();
    List<String> query = new ArrayList<>();
    query.add("electronics");
    query.add("music");
    body.setQuery(query);
    body.setStart(1L);
    body.setRows(10L);
    return body;
  }

  /**
   * Build a set of responses that the mocks will provide.
   * 
   * @return a list of docs for the mock to use
   */
  public static SolrDocumentList buildDocListResponses() {
    SolrDocumentList solrDocumentList = new SolrDocumentList();
    solrDocumentList.setMaxScore(1.3563559f);
    solrDocumentList.setStart(1);
    solrDocumentList.setNumFound(2);



    // first doc
    Map<String, Object> fields = new ConcurrentHashMap<>();
    fields.put("id", "SP2514N");
    fields.put("name", "Samsung SpinPoint P120 SP2514N - hard drive - 250 GB - ATA-133");
    fields.put("manu", "Samsung Electronics Co. Ltd.");
    fields.put("manu_id_s", "samsung");
    try {
      fields.put("manufacturedate_dt", SDF.parse("Mon Feb 13 17:26:37 CAT 2006"));
    } catch (ParseException e) {
      fail("Could not parse date from string literal: " + e.getMessage());
    }
    fields.put("score", 0.7826174f);

    SolrDocument doc = new SolrDocument(fields);

    List<SolrDocument> docs = new ArrayList<>();
    docs.add(doc);

    // second doc
    fields = new HashMap<>();
    fields.put("id", "F8V7067-APL-KIT");
    fields.put("name", "Belkin Mobile Power Cord for iPod w/ Dock");
    fields.put("manu", "Belkin");
    fields.put("manu_id_s", "belkin");
    try {
      fields.put("manufacturedate_dt", SDF.parse("Mon Aug 01 18:30:25 CAT 2005"));
    } catch (ParseException e) {
      fail("Could not parse date from string literal: " + e.getMessage());
    }
    fields.put("score", 0.7163829f);

    doc = new SolrDocument(fields);
    docs.add(doc);

    solrDocumentList.addAll(docs);
    return solrDocumentList;
  }


}
