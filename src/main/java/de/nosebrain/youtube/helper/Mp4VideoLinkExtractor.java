package de.nosebrain.youtube.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.nosebrain.util.web.UrlUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.nosebrain.youtube.helper.model.Video;
import de.nosebrain.youtube.helper.model.VideoLink;
import de.nosebrain.youtube.helper.model.VideoQuality;
import de.nosebrain.youtube.helper.service.VideoLinkExtractor;

@Component
public class Mp4VideoLinkExtractor implements VideoLinkExtractor {
  private static final Logger log = LoggerFactory.getLogger(Mp4VideoLinkExtractor.class);
  
  private static final Pattern VAR_EXTRACTOR_PATTERN = Pattern.compile("ytplayer\\.config = (.+);ytplayer\\.load");
  
  
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
            final JSONObject obj = new JSONObject(mapsString);

            final String info = obj.getJSONObject("args").getString("player_response");
            final JSONObject jsonInfo = new JSONObject(info.replace("\\u0026", "&"));

            final JSONArray formats = jsonInfo.getJSONObject("streamingData").getJSONArray("formats");
            final Video video = new Video();

            for (int i = 0 ; i < formats.length(); i++) {
              final JSONObject jsonFormat = formats.getJSONObject(i);
              final String mimeType = jsonFormat.getString("mimeType");
              final String quality = jsonFormat.getString("quality");
              if (mimeType.startsWith("video/mp4")) {
                final VideoQuality videoQuality = VideoQuality.valueOf(quality.toUpperCase());
                final VideoLink videoLink = new VideoLink();
                final String cipher = jsonFormat.getString("cipher");
                final String[] split = cipher.split("&");
                String url = null;
                String sig = null;
                for (String possibleUrl : split) {
                  if (possibleUrl.startsWith("url")) {
                    url = UrlUtils.decodeUrlString(possibleUrl.replace("url=", ""));
                  }
                  if (possibleUrl.startsWith("s=")) {
                    sig = UrlUtils.decodeUrlString(possibleUrl.substring(2));
                  }
                }

                videoLink.setUrl(url + "&sig=" + UrlUtils.encodeUrlString(sign(sig)));

                video.getLinks().put(videoQuality, videoLink);
              }
            }

            video.setTitle(jsonInfo.getJSONObject("videoDetails").getString("title"));
            return video;
          }
      }
    } catch (final IOException e) {
      log.error("error while getting web page for " + id, e);
    }
    
    return null;
  }

  private static String sign(String signature) {
    String[] chars = signature.split("");
    // Js.MR(a,8);
    swap(chars, 8);
    // Js.kF(a,37);
    reverse(chars, 0, chars.length - 1);
    // Js.MR(a,57);
    swap(chars, 57);
    // Js.MR(a,44);
    swap(chars, 44);
    // Js.yT(a,1);
    chars = splice(chars, 1);
    // Js.MR(a,53);
    swap(chars, 53);
    // Js.kF(a,30)
    reverse(chars, 0, chars.length - 1);

    return String.join("", chars);
  }

  private static String[] splice(String[] array, int till) {
    return Arrays.copyOfRange(array, till, array.length);
    // a.splice(0,b)
  }

  private static void reverse(String[] array, int start, int end) {
    String temp;

    while (start < end) {
      temp = array[start];
      array[start] = array[end];
      array[end] = temp;
      start++;
      end--;
    }
  }

  // mr(a, b)
  private static void swap(String[] array, int position) {
    String save = array[0];
    array[0] = array[position % array.length];
    array[position % array.length]= save;
  }
}
