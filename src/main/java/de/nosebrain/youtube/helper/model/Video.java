package de.nosebrain.youtube.helper.model;

public class Video {
  
  private String url;
  private String title;
  private String signature;
  
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
   * @return the title
   */
  public String getTitle() {
    return this.title;
  }
  
  /**
   * @param title the title to set
   */
  public void setTitle(final String title) {
    this.title = title;
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
}
