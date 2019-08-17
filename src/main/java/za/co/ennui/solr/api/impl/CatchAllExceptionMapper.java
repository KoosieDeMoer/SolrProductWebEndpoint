package za.co.ennui.solr.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts all exceptions into @link(ApiStatusType) to allow passing of a custom reasonPhrase.
 * 
 * @author John Williams
 *
 */
@Provider
public class CatchAllExceptionMapper extends Throwable implements ExceptionMapper<Throwable> {
  private static final Logger LOG = LoggerFactory.getLogger(CatchAllExceptionMapper.class);
  private static final long serialVersionUID = 1L;


  @Override
  public Response toResponse(final Throwable throwable) {
    Response retVal;
    try {
      retVal = buildFailureResponse(Status.INTERNAL_SERVER_ERROR, throwable.getLocalizedMessage());
    } catch (Exception e) {
      // this is top level and we must get something back to the client
      LOG.error("Failed to build error response with exception " + e.getClass() + ": ", e);
      retVal =
          buildFailureResponse(Status.INTERNAL_SERVER_ERROR, "Exception while handling exception");
    }
    return retVal;
  }

  /**
   * Builds a @link(Response) that packs in an @link(ApiStatusType)to allow passing of a custom
   * reasonPhrase.
   * 
   * @param internalServerError the error thrown internally
   * @param reasonPhrase to add to the status to help the client
   * @return
   */
  public static Response buildFailureResponse(final Status internalServerError,
      final String reasonPhrase) {
    return Response.status(new ApiStatusType(internalServerError, reasonPhrase)).build();
  }


}
