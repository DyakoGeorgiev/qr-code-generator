package com.qr.code.generator.commands;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateQRImage implements QRImage {

  private File imageFile;
  private String dataToBeEncrypted;

  public CreateQRImage (File imageFile, String dataToBeEncrypted) {
    this.imageFile = imageFile;
    this.dataToBeEncrypted = dataToBeEncrypted;
  }

  @Override
  public void execute () {
    ByteArrayOutputStream out = QRCode.from(dataToBeEncrypted)
      .to(ImageType.PNG).stream();

    try(FileOutputStream fout = new FileOutputStream(imageFile)){
      fout.write(out.toByteArray());
      fout.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
