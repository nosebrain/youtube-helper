package de.nosebrain.youtube.helper.controller;

import static de.nosebrain.util.ValidationUtils.present;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.nosebrain.youtube.helper.model.Video;
import de.nosebrain.youtube.helper.service.VideoLinkExtractor;

@Controller
public class MainController {

  @Autowired
  private VideoLinkExtractor extractor;
  
  @RequestMapping("/index.mp4")
  public String getVideo(@RequestParam("id") final String id) {
    final Video videoLink = this.extractor.getVideoLink(id);
    if (present(videoLink)) {
      return "redirect:" + videoLink.getUrl() + "&signature=" + videoLink.getSignature() + "&title=" + videoLink.getTitle();
    }
    
    return null;
  }
}
