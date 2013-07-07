package de.nosebrain.youtube.helper;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import de.nosebrain.youtube.helper.model.Video;

public class MediumQualityMp4VideoLinkExtractorTest {

  private static final MediumQualityMp4VideoLinkExtractor EXTRACTOR = new MediumQualityMp4VideoLinkExtractor();
  
  @Test
  public void testVideo1() {
    final Video videoLink = EXTRACTOR.getVideoLink("Mcv75hETSjk");
    assertEquals("", videoLink.getUrl());
  }
}
