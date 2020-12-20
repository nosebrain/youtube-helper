package de.nosebrain.youtube.helper;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.model.YoutubeVideo;
import com.github.kiulian.downloader.model.formats.VideoFormat;
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

  @Override
  public Video getVideoLink(final String id) {
    try {
      final YoutubeDownloader downloader = new YoutubeDownloader();
      final YoutubeVideo videoInfo = downloader.getVideo(id);

      final Video video = new Video();

      for (final VideoFormat videoFormat : videoInfo.videoFormats()) {
        final String mimeType = videoFormat.mimeType();
        if (mimeType.contains("mp4") && mimeType.contains("avc1")) {
          final VideoQuality videoQuality = convertVideoFormat(videoFormat.videoQuality());
          if (videoQuality == null) {
            continue;
          }
          final VideoLink videoLink = new VideoLink();
          videoLink.setUrl(videoFormat.url());
          video.getLinks().put(videoQuality, videoLink);
        }
      }

      video.setTitle(videoInfo.details().title());
      return video;
    } catch (final YoutubeException e) {
      log.error("error while extracting video info", e);
    }
    return null;
  }

  private static VideoQuality convertVideoFormat(com.github.kiulian.downloader.model.quality.VideoQuality videoQuality) {
    switch (videoQuality) {
      case hd720: return VideoQuality.HD720;
      case hd1080: return VideoQuality.HD1080;
      case large: return VideoQuality.LARGE;
      case tiny: return VideoQuality.TINY;
      case medium: return VideoQuality.MEDIUM;
      case small: return VideoQuality.SMALL;
    }

    return null;
  }
}
