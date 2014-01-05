package de.nosebrain.youtube.helper;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.nosebrain.youtube.helper.model.Video;

public class MediumQualityMp4VideoLinkExtractorTest {

  private static final MediumQualityMp4VideoLinkExtractor EXTRACTOR = new MediumQualityMp4VideoLinkExtractor();
  
  @Test
  public void testVideo1() {
    final Video videoLink = EXTRACTOR.getVideoLink("mtaIpkjF6Ss");
    assertNotNull(videoLink);
    assertThat(videoLink.getUrl(), not(isEmptyString()));
  }
}
