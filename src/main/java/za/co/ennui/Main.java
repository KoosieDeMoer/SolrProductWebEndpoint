package za.co.ennui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.api.SearchProductsApi;
import io.swagger.api.SearchProductsApiService;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

public class Main {

  private static final int MINIMUM_ARGS = 3;
  private static final int MIN_ARGS_PLUS_CLIENT_CLASS = MINIMUM_ARGS + 1;
  private static final int MIN_ARGS_PLUS_CONNECTION_TIMEOUT = MIN_ARGS_PLUS_CLIENT_CLASS + 1;

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);
  private static int exposedPortNo;
  private static Server server;

  /**
   * Starts a Jetty server.
   * 
   * @param args see @link(usage)
   * @throws Exception when it can't start
   */
  public static void main(final String[] args) {

    usage(args);

    // Build the Swagger Bean.
    buildSwagger();

    // Holds handlers
    final HandlerList handlers = new HandlerList();

    // Handler for Entity Browser and Swagger
    handlers.addHandler(buildContext());

    server = new Server(exposedPortNo);
    server.setHandler(handlers);

    try {
      server.start();
      server.join();
    } catch (InterruptedException e) {
      LOG.error("Problems starting server", e);
    } catch (Exception e) {
      LOG.error("Problems starting server", e);
    } finally {
      server.destroy();
    }
  }

  /**
   * Allows unit testing to spin up and kill servers.
   * 
   * @param interval how many ms to wait before the bomb goes off
   */
  public static void timeBomb(final long interval) {
    new Thread() {
      @Override
      public void run() {
        try {
          Thread.sleep(interval);
          server.stop();
        } catch (Exception ex) {
          LOG.error("Problems stopping server", ex);
        }
      }
    }.start();
  }

  private static void buildSwagger() {
    // This configures Swagger
    final BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("1.0.0");
    beanConfig.setResourcePackage(SearchProductsApi.class.getPackage().getName());
    beanConfig.setScan(true);
    beanConfig.setBasePath("/");
    beanConfig.setDescription(
        "Search Products API to demonstrate Swagger with Jersey2 in an embedded Jetty instance.");
    beanConfig.setTitle("Entity Browser");
  }

  private static ContextHandler buildContext() {
    final ResourceConfig resourceConfig = new ResourceConfig();
    // Replace EntityBrowser with your resource class
    // io.swagger.jaxrs.listing loads up Swagger resources
    resourceConfig.packages(SearchProductsApiService.class.getPackage().getName(),
        ApiListingResource.class.getPackage().getName());
    final ServletContainer servletContainer = new ServletContainer(resourceConfig);
    final ServletHolder searchProductsApi = new ServletHolder(servletContainer);
    final ServletContextHandler entityBrowserContext =
        new ServletContextHandler(ServletContextHandler.SESSIONS);
    entityBrowserContext.setContextPath("/");
    entityBrowserContext.addServlet(searchProductsApi, "/*");

    return entityBrowserContext;
  }

  private static void usage(final String... args) {
    if (args.length < MINIMUM_ARGS) {
      LOG.error(
          "Usage requires command line parameters MY_EXPOSED_PORT_NO SOLR_URL COLLECTION <SOLRJ_CLIENT_CLASS> <SOLRJ_CONNECTION_TIMEOUT> <SOLRJ_CLIENT_SOCKET_TIMEOUT>, eg 8080 http://localhost:8983/solr/techproducts org.apache.solr.client.solrj.impl.HttpSolrClient 10000 60000");
      System.exit(0);
    } else {
      exposedPortNo = Integer.parseInt(args[0]);
      SearchProductsApiService.solrUrl = args[1];
      SearchProductsApiService.collection = args[2];
      if (args.length > MINIMUM_ARGS) {
        SearchProductsApiService.solrClientClass = args[3];
      }
      if (args.length > MIN_ARGS_PLUS_CLIENT_CLASS) {
        SearchProductsApiService.connectionTimeout = Integer.parseInt(args[4]);
      }
      if (args.length > MIN_ARGS_PLUS_CONNECTION_TIMEOUT) {
        SearchProductsApiService.socketTimeout = Integer.parseInt(args[5]);
      }
    }
  }

}
