package com.qr.code.generator.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.qr.code.generator.utils.DeleteDirectory;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class WriteQRImageDirectory implements QRImage {

  private final File sourceDir;
  private final int depth;
  private final File targetDir;

  public WriteQRImageDirectory (final File sourceDir, final int depth,final File targetDir) {
    this.sourceDir = sourceDir;
    this.depth = depth;
    this.targetDir = targetDir;
  }

  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    if (sourceDir.isDirectory()){
      moveQRDirectory(sourceDir, targetDir, depth);
    } else {
      ensureParentFolder(targetDir);
      copyFile(sourceDir, targetDir);
    }
  }

  private void moveQRDirectory (final File sourceDir, final File targetDir, final int depth) throws IOException, WriterException, NotFoundException {
    if(depth == 0){ return; }

    if(targetDir.isDirectory()){
      DeleteDirectory.execute(targetDir);
    }
    targetDir.mkdirs();

    final File[] contents = sourceDir.listFiles();

    if (contents != null) {
      for (final File file : contents) {
        final File newFile = new File(targetDir.getAbsolutePath() + File.separator + file.getName());
        if (file.isDirectory()){
          moveQRDirectory(file, newFile, depth-1);
        } else {
          //Generate QRCode and save it with new random name in the target directory
          final File qrFilePath = getQrFilePath(targetDir);
          if(file.getName().endsWith(".txt") && Files.size(file.toPath()) < 1500){
            final String content = getContent(file);

            final QRImage createQRImage = new CreateQRImage(qrFilePath,content);
            createQRImage.execute();

            final QRImage mapQRImage = new MapToFileQRImage(file.getName(),qrFilePath.getName(),targetDir.toPath());
            mapQRImage.execute();
          }
        }
      }
    }
  }

  /**
   * Method to get the path and the random generated name of the new QR Code file.
   *
   * @param targetDir - target directory where to print the QR Code file
   *
   * */

  private File getQrFilePath (final File targetDir) {
    return new File(targetDir.getAbsolutePath() + File.separator + RandomStringUtils.randomAlphabetic(10)+".png");
  }

  /**
   * Method to return the content of the file.
   *
   * @param file  - file which content will be returned
   * @throws IOException - if problem occurs during extracting the content of the file
   * */

  private String getContent (final File file) throws IOException {
    return Files.readString(Paths.get(file.getAbsolutePath()), Charset.forName("ISO-8859-1"));
  }

  /**
   * Method that gets the directories from the two file parameters.
   * And then copies file(directory) from one place to another.
   *
   * @param source - The file which will be copied
   * @param dest  - the new destination of the file
   * @throws IOException - if if problem occurs during copy
   * */

  private void copyFile(final File source,final File dest) throws IOException {
    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  private void ensureParentFolder(final File file) {
    final File parent = file.getParentFile();
    if (parent != null && !parent.exists()){
      parent.mkdirs();
    }
  }
}
