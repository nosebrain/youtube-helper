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
import de.nosebrain.youtube.helper.service.VideoLinkExtractor;

@Component
public class MediumQualityMp4VideoLinkExtractor implements VideoLinkExtractor {
  private static final Logger log = LoggerFactory.getLogger(MediumQualityMp4VideoLinkExtractor.class);
  
  private static final Pattern VAR_EXTRACTOR_PATTERN = Pattern.compile("\"url_encoded_fmt_stream_map\": \"([^\"]*)\"");
  
  
  public Video getVideoLink(final String id) {
    try {
      final Document site = Jsoup.connect("http://www.youtube.com/watch?v=" + id).get();
      final Elements scripts = site.select("script");
      if (scripts.size() >= 5) {
        final Element script = scripts.get(4);
        final String value = script.toString();
        final Matcher matcher = VAR_EXTRACTOR_PATTERN.matcher(value);
        if (matcher.find()) {
          final String mapsString = matcher.group(1);
          final String[] splittedMaps = mapsString.split(",");
          for (final String map : splittedMaps) {
            final String toParse = map.replaceAll("\\\\u0026", "\n");
            final StringReader reader = new StringReader(toParse);
            final Properties propertyMap = new Properties();
            propertyMap.load(reader);
            final String type = propertyMap.getProperty("type");
            final String quality = propertyMap.getProperty("quality");
            if (type.startsWith("video%2Fmp4") && "medium".equals(quality)) {
              final Video video = new Video();
              video.setSignature(propertyMap.getProperty("sig"));
              video.setUrl(UrlUtils.decodeUrlString(propertyMap.getProperty("url")));
              final Elements titleSelects = site.select("meta[name=title]");
              if (present(titleSelects)) {
                final String title = titleSelects.get(0).attr("content");
                video.setTitle(title);
              }
              return video;
            }
          }
        }
      }
    } catch (final IOException e) {
      log.error("error while getting web page for " + id, e);
    }
    return null;
  }

}
