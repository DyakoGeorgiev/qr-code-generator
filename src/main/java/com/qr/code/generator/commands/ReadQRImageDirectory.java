package com.qr.code.generator.commands;


import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.qr.code.generator.utils.DeleteDirectory;

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
  private final File sourceDir;
  private final File targetDir;

  private Map<String, String> hashMap;

  public ReadQRImageDirectory (final File sourceDir, final File targetDir) {
    this.sourceDir = sourceDir;
    this.targetDir = targetDir;
  }

  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    if (!sourceDir.isDirectory()) {
      throw new NoSuchFileException(EXCEPTION_MESSAGE);
    }

    //create target directory
    if(targetDir.isDirectory()){
      DeleteDirectory.execute(targetDir);
    }
    targetDir.mkdirs();

    //get all files in the directory
    final File[] contents = sourceDir.listFiles();

    if (contents != null) {
      for (final File file : contents) {

        final File newFile = new File(targetDir.getAbsolutePath() + File.separator + file.getName());

        if (file.isDirectory()) {
          new ReadQRImageDirectory(file, newFile).execute();
        }

        if(isEquals(file)) {
          hashMap = getHashMapFromFile(new File(file.getAbsolutePath()));
        }

        final Iterator it = getIterator();
        while(it.hasNext()){
          final Map.Entry pair = (Map.Entry) it.next();

          final DecodeQRImage decodeQRImage = new DecodeQRImage(getQrCodeImage(sourceDir, pair));
          decodeQRImage.execute();

          final String textFromQRImage = decodeQRImage.getText();

          printToFile(targetDir, pair, textFromQRImage);

          it.remove();
        }
      }
    }
  }

  private Iterator<Map.Entry<String, String>> getIterator () {
    return hashMap.entrySet().iterator();
  }


  /**
  * Print the text from the file to the target directory
   *
   * @param targetDir - directory where the file will be created
   * @param pair - given pair of oldFileNam : newFileName, it gets the oldFileName and makes a file in the target
   *             with that name
   * @param text - print the text into the file
   *
   * @throws FileNotFoundException - if no directory exists
  * */
  private void printToFile (File targetDir, Map.Entry pair, String text) throws FileNotFoundException {
    try (PrintWriter out = new PrintWriter(targetDir + File.separator + pair.getKey())) {
      out.println(text);
    }
  }

  /**
   * Method to return the QR Code file, given source directory
   *
   * @param sourceDir - directory where to look for the file
   * @param pair - oldFileName : newFileName pair. The method gets the value from the pair to extract the QR Code image
   *
   * */

  private File getQrCodeImage (File sourceDir, Map.Entry pair) {
    return new File( sourceDir.getAbsolutePath() + File.separator + pair.getValue());
  }

  private boolean isEquals (File file) {
    return file.getName().equals(QR_CODE_FILE_MAPPING_NAME);
  }

   /**
   * Given file that contains printed key:value pairs that contains oldFileName : newFileName,
    * the method extracts the information into hashmap.
   *
    * @param file - file containing the key:value pairs
   * */
  private Map<String,String> getHashMapFromFile (File file){
    final String delimiter = ":";
    final HashMap<String, String> map = new HashMap<>();
    try(BufferedReader in = new BufferedReader(new FileReader(file))){
      String line;
      while ((line = in.readLine()) != null) {
        final String[] parts = line.split(delimiter);
        map.put(parts[0], parts[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }
}
