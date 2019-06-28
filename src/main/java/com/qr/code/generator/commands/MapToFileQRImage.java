package com.qr.code.generator.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapToFileQRImage implements QRImage {

  private Map<String,String> mappings;
  private final String fileName;
  private final String qrCodeName;
  private final Path pathToCreateFile;

  public MapToFileQRImage (final String fileName, final String qrCodeName, final Path pathToCreateFile) {
    this.fileName = fileName;
    this.qrCodeName = qrCodeName;
    this.pathToCreateFile = pathToCreateFile;
  }

  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    mapQRCodeToFile(fileName,qrCodeName);
    final File file = Paths.get(pathToCreateFile.toString() + File.separator + ".qr_code.txt").toFile();

    saveMappingToFile(file);
  }

  private void mapQRCodeToFile(final String fileName, String qrCodeName){
    mappings = new HashMap<>();
    mappings.put(fileName,qrCodeName);
  }


  private void saveMappingToFile(final File fileName) throws IOException {
    final Iterator it = mappings.entrySet().iterator();
    try(PrintWriter out = new PrintWriter(
      new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(fileName,true), StandardCharsets.UTF_8)))) {
      while (it.hasNext()) {
        final Map.Entry pair = (Map.Entry) it.next();
        out.println(pair.getKey() + ":" + pair.getValue());
        it.remove();
      }
    }
    //set hidden attribute
    Files.setAttribute(fileName.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
  }
}
