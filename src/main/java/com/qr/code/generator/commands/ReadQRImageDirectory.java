package com.qr.code.generator.commands;


import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ReadQRImageDirectory implements QRImage {

  private static final String QR_CODE_FILE_MAPPING_NAME = ".qr_code.txt";
  private static final String EXCEPTION_MESSAGE = "Please provide existing directory to monitor.";
  private File sourceDir;
  private File targetDir;

  private HashMap<String, String> hashMap;

  public ReadQRImageDirectory (File sourceDir, File targetDir) {
    this.sourceDir = sourceDir;
    this.targetDir = targetDir;
  }



  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    if (!sourceDir.isDirectory()) {
      throw new NoSuchFileException(EXCEPTION_MESSAGE);
    }

    //create target directory
    targetDir.mkdirs();

    //get all files in the directory
    File[] contents = sourceDir.listFiles();

    if (contents != null) {
      for (File file : contents) {

        File newFile = new File(targetDir.getAbsolutePath() + File.separator + file.getName());

        if (file.isDirectory()) {
          new ReadQRImageDirectory(file, newFile).execute();
        }

        if(isEquals(file)) {
          hashMap = getHashMapFromFile(new File(file.getAbsolutePath()));
        }

        Iterator it = getIterator();
        while(it.hasNext()){
          Map.Entry pair = (Map.Entry) it.next();

          QRImage decodeQRImage = new DecodeQRImage(getQrCodeImage(sourceDir, pair));
          decodeQRImage.execute();

          String textFromQRImage = ((DecodeQRImage) decodeQRImage).getText();

          printToFile(targetDir, pair, textFromQRImage);

          it.remove();
        }
      }
    }
  }

  private Iterator<Map.Entry<String, String>> getIterator () {
    return hashMap.entrySet().iterator();
  }

  private void printToFile (File targetDir, Map.Entry pair, String text) throws FileNotFoundException {
    try (PrintWriter out = new PrintWriter(targetDir + File.separator + pair.getKey())) {
      out.println(text);
    }
  }

  private File getQrCodeImage (File sourceDir, Map.Entry pair) {
    return new File( sourceDir.getAbsolutePath() + File.separator + pair.getValue());
  }

  private boolean isEquals (File file) {
    return file.getName().equals(QR_CODE_FILE_MAPPING_NAME);
  }

  private HashMap<String,String> getHashMapFromFile (File file){
    String delimiter = ":";
    HashMap<String, String> map = new HashMap<>();
    try(BufferedReader in = new BufferedReader(new FileReader(file))){
      String line;
      while ((line = in.readLine()) != null) {
        String[] parts = line.split(delimiter);
        map.put(parts[0], parts[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }

}
