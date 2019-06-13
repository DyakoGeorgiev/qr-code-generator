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
  private String fileName;
  private String qrCodeName;
  private Path pathToCreateFile;

  public MapToFileQRImage (String fileName, String qrCodeName, Path pathToCreateFile) {
    this.fileName = fileName;
    this.qrCodeName = qrCodeName;
    this.pathToCreateFile = pathToCreateFile;
  }

  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    mapQRCodeToFile(fileName,qrCodeName);
    File file = Paths.get(pathToCreateFile.toString() + File.separator + ".qr_code.txt").toFile();

    saveMappingToFile(file);
  }

  private void mapQRCodeToFile(String fileName, String qrCodeName){
    mappings = new HashMap<>();
    mappings.put(fileName,qrCodeName);
  }



  private void saveMappingToFile(File fileName) throws IOException {
    Iterator it = mappings.entrySet().iterator();
    try(PrintWriter out = new PrintWriter(
      new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream(fileName,true), StandardCharsets.UTF_8)))) {
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry) it.next();
        out.println(pair.getKey() + ":" + pair.getValue());
        it.remove();
      }
    }
    //set hidden attribute
    Files.setAttribute(fileName.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
  }
}
