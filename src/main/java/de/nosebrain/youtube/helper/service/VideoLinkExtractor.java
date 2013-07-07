package de.nosebrain.youtube.helper.service;

import de.nosebrain.youtube.helper.model.Video;

public interface VideoLinkExtractor {
  public Video getVideoLink(final String id);
}
