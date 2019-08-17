package za.co.ennui.solr.api.impl;

/**
 * This exists to wrap all exceptions - PMD.
 * 
 * @author John Williams
 *
 */
public class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ApplicationException(final String message, final Exception exception) {
    super(message, exception);
  }


}
