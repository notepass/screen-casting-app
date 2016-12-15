package de.asw.apps.screencast.util;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * This class contains utility methods for working with images
 */
public class ImageUtil {
  public static enum OutputFormat {
    JPG("jpg"), PNG("png"), BMP("bmp");

    private String format;

    private OutputFormat(String format) {
      this.format = format;
    }

    public String getFormat() {
      return format;
    }
  }

  /**
   * Converts an buffered image into a byte[]
   * @param in BufferedImage to convert
   * @param outFormat The format to use for the image
   * @return
   */
  public static byte[] bufferedImageToByte(BufferedImage in, OutputFormat outFormat) {
    return bufferedImageToByte(in, outFormat, 0.3f);
  }

  /**
   * Converts an buffered image into a byte[]
   * @param in BufferedImage to convert
   * @param outFormat The format to use for the image
   * @param compressionLevel Level for compression. 0 is the lowest compression, 1 the highest. Only works with JPG output type
   * @return
   */
  public static byte[] bufferedImageToByte(BufferedImage in, OutputFormat outFormat, float compressionLevel) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(in.getHeight()*in.getWidth());
    try {
      if (outFormat.equals(OutputFormat.JPG)) {
        compressionLevel = 1-compressionLevel;
        JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
        jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(compressionLevel);

        ImageWriter writer = ImageIO.getImageWritersByFormatName(outFormat.getFormat()).next();
        writer.setOutput(new MemoryCacheImageOutputStream(baos));
        writer.write(null, new IIOImage(in, null, null), jpegParams);
      } else {
        ImageIO.write(in, outFormat.getFormat(), baos);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return baos.toByteArray();
  }

  /**
   * Converts an HEX color to an RGB color
   * @param hex Hex color (e.g. #FF00CC or FF00CC)
   * @return
   */
  public static int[] hexToRgb(String hex) {
    if (hex.startsWith("#")) {
      hex = hex.substring(1, hex.length());
    }
    int[] rgb = new int[3];
    rgb[0] = Integer.valueOf( hex.substring( 0, 2 ), 16 );
    rgb[1] = Integer.valueOf( hex.substring( 2, 4 ), 16 );
    rgb[2] = Integer.valueOf( hex.substring( 4, 6 ), 16 );

    return rgb;
  }
}
