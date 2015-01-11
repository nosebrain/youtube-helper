package de.nosebrain.youtube.helper.model;

import java.util.HashMap;
import java.util.Map;

public class Video {
  
  private Map<VideoQuality, VideoLink> links = new HashMap<VideoQuality, VideoLink>();
  private String title;

  /**
   * @return the links
   */
  public Map<VideoQuality, VideoLink> getLinks() {
    return this.links;
  }

  /**
   * @param links the links to set
   */
  public void setLinks(final Map<VideoQuality, VideoLink> links) {
    this.links = links;
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
}
