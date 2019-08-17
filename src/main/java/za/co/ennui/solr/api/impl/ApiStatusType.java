package za.co.ennui.solr.api.impl;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

/**
 * To allow the modification of the @link(Response) reasonPhrase so that same is visible to the
 * client.
 * 
 * @author John Williams
 *
 */
public class ApiStatusType implements StatusType {

  private Family family;
  private int statusCode;
  private String reasonPhrase;

  /**
   * A {@link javax.ws.rs.core.StatusType StatusType} with an amenable reason code.
   * 
   * @param family An enumeration representing the class of status code
   * @param statusCode the HTTP status code
   * @param reasonPhrase to append to the {@link javax.ws.rs.core.StatusType StatusType}
   */
  public ApiStatusType(final Family family, final int statusCode, final String reasonPhrase) {
    super();

    this.family = family;
    this.statusCode = statusCode;
    this.reasonPhrase = reasonPhrase;
  }

  protected ApiStatusType(final Status status, final String reasonPhrase) {
    this(status.getFamily(), status.getStatusCode(), reasonPhrase);
  }

  @Override
  public Family getFamily() {
    return family;
  }

  @Override
  public String getReasonPhrase() {
    return reasonPhrase;
  }

  @Override
  public int getStatusCode() {
    return statusCode;
  }


}
