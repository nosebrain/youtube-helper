package de.nosebrain.youtube.helper.model;

public class VideoLink {
  private String url;
  private String signature;
  private boolean urlContainsSignature;
  
  /**
   * @return the url
   */
  public String getUrl() {
    return this.url;
  }

  /**
   * @param url the url to set
   */
  public void setUrl(final String url) {
    this.url = url;
  }

  /**
   * @return the signature
   */
  public String getSignature() {
    return this.signature;
  }
  
  /**
   * @param signature the signature to set
   */
  public void setSignature(final String signature) {
    this.signature = signature;
  }

  /**
   * @return the urlContainsSignature
   */
  public boolean isUrlContainsSignature() {
    return this.urlContainsSignature;
  }

  /**
   * @param urlContainsSignature the urlContainsSignature to set
   */
  public void setUrlContainsSignature(final boolean urlContainsSignature) {
    this.urlContainsSignature = urlContainsSignature;
  }
}
