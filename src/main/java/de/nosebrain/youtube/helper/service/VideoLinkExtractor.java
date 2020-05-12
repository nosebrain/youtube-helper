package de.nosebrain.youtube.helper.service;

import de.nosebrain.youtube.helper.model.Video;

public interface VideoLinkExtractor {
  Video getVideoLink(final String id);
}
