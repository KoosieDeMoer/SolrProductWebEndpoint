package za.co.ennui.test;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.rules.ExpectedException;

import za.co.ennui.Main;

public class StartupTest {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  @Test
  public void goldenTest() {
    String[] args = new String[6];
    // 8080 http://localhost:8983/solr techproducts
    // org.apache.solr.client.solrj.impl.HttpSolrClient 10000 60000
    args[0] = "8081";
    args[1] = "http://localhost:8983/solr";
    args[2] = "techproducts";
    args[3] = "org.apache.solr.client.solrj.impl.HttpSolrClient";
    args[4] = "10000";
    args[5] = "60000";

    // can't leave the jetty server running to leave a timebomb
    Main.timeBomb(100L);

    exceptionRule.expect(IllegalStateException.class);

    Main.main(args);

    assertEquals("For PMD only", args.length, 2);

  }

  @Test
  public void threeArgsTest() {
    String[] args = new String[3];
    // 8080 http://localhost:8983/solr techproducts
    // org.apache.solr.client.solrj.impl.HttpSolrClient 10000 60000
    args[0] = "8082";
    args[1] = "http://localhost:8983/solr";
    args[2] = "techproducts";

    // can't leave the jetty server running to leave a timebomb
    Main.timeBomb(100L);

    exceptionRule.expect(IllegalStateException.class);

    Main.main(args);

    assertEquals("For PMD only", args.length, 3);
  }

  @Test
  public void tooFewArgsTest() {
    String[] args = new String[2];
    args[0] = "8083";
    args[1] = "http://localhost:8983/solr";
    exit.expectSystemExitWithStatus(0);
    Main.main(args);
    assertEquals("For PMD only", args.length, 2);
  }

}
