package com.qr.code.generator.commands;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DecodeQRImage implements QRImage {

  private final File qrCodeImage;
  private String text;

  public DecodeQRImage (final File qrCodeImage) {
    this.qrCodeImage = qrCodeImage;
  }

  @Override
  public void execute () throws IOException, NotFoundException {
    final BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
    final LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
    final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

    final Result result = new MultiFormatReader().decode(bitmap);
    text = result.getText();
  }

  public String getText(){
    if(text==null){
      throw new IllegalArgumentException("The text has no characters");
    }
    return text;
  }


}
