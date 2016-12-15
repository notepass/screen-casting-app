package de.asw.apps.screencast;

import de.asw.apps.screencast.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
class ScreenFetcher {

  private final ScreenGrabber screenGrabber;

  private final AtomicReference<byte[]> currentImage = new AtomicReference<>();

  @Value("${screencast.mouse.draw}") private boolean drawCourser;
  @Value("${screencast.mouse.width}") private int courserWidth;
  @Value("${screencast.mouse.height}") private int courserHeight;
  @Value("${screencast.mouse.path}") private File courserPath;
  @Value("${screencast.mouse.borderColor}") private String courserBorderColorString;
  @Value("${screencast.mouse.fillColor}") private String courserFillColorString;
  @Value("#{${screencast.output.imageCompression}/100.0}") private float compressionLevel;
  private Color courserBorderColor;
  private Color courserFillColor;

  @PostConstruct
  public void prepare() {
    int[] rgbBorder = ImageUtil.hexToRgb(courserBorderColorString);
    int[] rgbFill = ImageUtil.hexToRgb(courserFillColorString);
    courserBorderColor = new Color(rgbBorder[0], rgbBorder[1], rgbBorder[2]);
    courserFillColor = new Color(rgbFill[0], rgbFill[1], rgbFill[2]);
  }

  @Scheduled(fixedDelayString = "#{${screencast.output.refreshIntervalMillis:-1}}")
  void updateImage() {
    BufferedImage bi = screenGrabber.grab();
    if (drawCourser) {
      Point mouse = MouseInfo.getPointerInfo().getLocation();
      Graphics g = bi.getGraphics();
      g.setColor(courserBorderColor);
      g.drawOval(mouse.x, mouse.y, courserWidth, courserHeight);
      g.setColor(courserFillColor);
      g.fillOval(mouse.x, mouse.y, courserWidth - 1, courserHeight - 1);
      g.dispose();
    }
    currentImage.set(ImageUtil.bufferedImageToByte(bi, ImageUtil.OutputFormat.JPG, compressionLevel));
  }

  byte[] getCurrentImage() {
    return currentImage.get();
  }
}
