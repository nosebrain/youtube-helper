package de.nosebrain.youtube.helper.controller;

import static de.nosebrain.util.ValidationUtils.present;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.nosebrain.youtube.helper.model.Video;
import de.nosebrain.youtube.helper.model.VideoLink;
import de.nosebrain.youtube.helper.model.VideoQuality;
import de.nosebrain.youtube.helper.service.VideoLinkExtractor;

@Controller
public class MainController {

  @Autowired
  private VideoLinkExtractor extractor;
  
  @RequestMapping("/index.mp4")
  public String getVideo(@RequestParam("id") final String id, @RequestParam(value = "quality", required = false) VideoQuality quality) throws UnsupportedEncodingException {
    if (quality == null) {
      quality = VideoQuality.MEDIUM;
    }
    final Video video = this.extractor.getVideoLink(id);
    if (present(video)) {
      final VideoLink link = video.getLinks().get(quality);
      String redirectUrl = link.getUrl();
      final String signature = link.getSignature();
      if (!link.isUrlContainsSignature() && present(signature)) {
        redirectUrl += "&signature=" + signature;
      }
      return "redirect:" + redirectUrl + "&title=" + URLEncoder.encode(video.getTitle(), "UTF-8");
    }
    
    return null;
  }
}
