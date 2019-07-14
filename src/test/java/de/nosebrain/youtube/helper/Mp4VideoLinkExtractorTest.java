package de.nosebrain.youtube.helper;

import static de.nosebrain.util.ValidationUtils.present;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.nosebrain.youtube.helper.model.Video;
import de.nosebrain.youtube.helper.model.VideoLink;
import de.nosebrain.youtube.helper.model.VideoQuality;

@RunWith(Parameterized.class)
public class Mp4VideoLinkExtractorTest {

  private static final Mp4VideoLinkExtractor EXTRACTOR = new Mp4VideoLinkExtractor();
  
  @Parameters
  public static Collection<Object[]> data() {
      return Arrays.asList(new Object[][] {
               { "lp-EO5I60KA" }, { "fHAOWLhrxhQ" }
         });
  }
  
  private final String id;
  
  public Mp4VideoLinkExtractorTest(final String id) {
    super();
    this.id = id;
  }

  @Test
  public void testVideo1() {
    final Video video = EXTRACTOR.getVideoLink(this.id);
    assertNotNull(video);
    
    for (final VideoQuality quality : VideoQuality.values()) {
      final VideoLink videoLink = video.getLinks().get(quality);
      if (!present(videoLink)) {
        continue;
      }
      
      assertThat(videoLink.getUrl(), not(isEmptyString()));
    }
  }
}
