package com.qr.code.generator.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class WriteQRImageDirectory implements QRImage {

  private File sourceDir;
  private int depth;
  private File targetDir;

  public WriteQRImageDirectory (File sourceDir, int depth, File targetDir) {
    this.sourceDir = sourceDir;
    this.depth = depth;
    this.targetDir = targetDir;
  }

  @Override
  public void execute () throws IOException, NotFoundException, WriterException {
    if (sourceDir.isDirectory())
      moveQRDirectory(sourceDir, targetDir, depth);
    else {
      ensureParentFolder(targetDir);
      copyFile(sourceDir, targetDir);
    }
  }

  public void moveQRDirectory (File sourceDir, File targetDir, int depth) throws IOException, WriterException, NotFoundException {
    if(depth == 0){ return; }

    targetDir.mkdirs();
    File[] contents = sourceDir.listFiles();

    if (contents != null) {
      for (File file : contents) {
        File newFile = new File(targetDir.getAbsolutePath() + File.separator + file.getName());
        if (file.isDirectory())
          moveQRDirectory(file, newFile, depth-1);
        else{
          //Generate QRCode and save it with new random name in the target directory
          File qrFilePath = getQrFilePath(targetDir);
          String content = getContent(file);

          QRImage createQRImage = new CreateQRImage(qrFilePath,content);
          createQRImage.execute();

          QRImage mapQRImage = new MapToFileQRImage(file.getName(),qrFilePath.getName(),targetDir.toPath());
          mapQRImage.execute();
        }
      }
    }
  }

  private File getQrFilePath (File targetDir) {
    return new File(targetDir.getAbsolutePath() + File.separator + RandomStringUtils.randomAlphabetic(10)+".png");
  }

  private String getContent (File file) throws IOException {
    return Files.readString(Paths.get(file.getAbsolutePath()), Charset.forName("ISO-8859-1"));
  }

  public void copyFile(File source, File dest) throws IOException {
    Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  public void ensureParentFolder(File file) {
    File parent = file.getParentFile();
    if (parent != null && !parent.exists())
      parent.mkdirs();
  }
}
