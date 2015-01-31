package de.nosebrain.youtube.helper;

import static de.nosebrain.util.ValidationUtils.present;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.nosebrain.util.web.UrlUtils;
import de.nosebrain.youtube.helper.model.Video;
import de.nosebrain.youtube.helper.model.VideoLink;
import de.nosebrain.youtube.helper.model.VideoQuality;
import de.nosebrain.youtube.helper.service.VideoLinkExtractor;

@Component
public class Mp4VideoLinkExtractor implements VideoLinkExtractor {
  private static final Logger log = LoggerFactory.getLogger(Mp4VideoLinkExtractor.class);
  
  private static final Pattern VAR_EXTRACTOR_PATTERN = Pattern.compile("\"url_encoded_fmt_stream_map\":\"([^\"]*)\"");
  
  
  @Override
  public Video getVideoLink(final String id) {
    try {
      final Document site = Jsoup.connect("https://www.youtube.com/watch?v=" + id).get();
      log.debug(site.html());
      final Elements scripts = site.select("script");
      for (final Element script : scripts) {
          final String value = script.toString();
          final Matcher matcher = VAR_EXTRACTOR_PATTERN.matcher(value);
          if (matcher.find()) {
            final String mapsString = matcher.group(1);
            final String[] splittedMaps = mapsString.split(",");
            final Video video = new Video();
            for (final String map : splittedMaps) {
              final String toParse = map.replaceAll("\\\\u0026", "\n");
              final StringReader reader = new StringReader(toParse);
              final Properties propertyMap = new Properties();
              propertyMap.load(reader);
              final String type = propertyMap.getProperty("type");
              final String quality = propertyMap.getProperty("quality");
              if (type.startsWith("video%2Fmp4")) {
                final VideoQuality videoQuality = VideoQuality.valueOf(quality.toUpperCase());
                final VideoLink videoLink = new VideoLink();
                final String link = UrlUtils.decodeUrlString(propertyMap.getProperty("url"));
                
                if (present(link) && link.contains("signature=")) {
                  videoLink.setUrlContainsSignature(true);
                } else {
                  String signature = propertyMap.getProperty("sig");
                  if (!present(signature)) {
                    signature = propertyMap.getProperty("s");
                  }
                  videoLink.setSignature(signature);
                }
                
                videoLink.setUrl(link);
                video.getLinks().put(videoQuality, videoLink);
                final Elements titleSelects = site.select("meta[name=title]");
                if (present(titleSelects)) {
                  final String title = titleSelects.get(0).attr("content");
                  video.setTitle(title);
                }
              }
            }
            this.fixSignatures(video);
            return video;
          }
      }
    } catch (final IOException e) {
      log.error("error while getting web page for " + id, e);
    }
    
    return null;
  }


  private void fixSignatures(final Video video) {
    int count = 0;
    int sigCount = 0;
    String signature = null;
    for (final VideoQuality quality : VideoQuality.values()) {
      final VideoLink videoLink = video.getLinks().get(quality);
      if (present(videoLink)) {
        count++;
        final String currentSignature = videoLink.getSignature();
        if (present(currentSignature)) {
          sigCount++;
          signature = currentSignature;
        }
      }
    }
    
    if ((count != sigCount) && (sigCount == 1)) {
      for (final VideoQuality quality : VideoQuality.values()) {
        final VideoLink videoLink = video.getLinks().get(quality);
        if (present(videoLink)) {
          videoLink.setSignature(signature);
        }
      }
    }
  }

}
