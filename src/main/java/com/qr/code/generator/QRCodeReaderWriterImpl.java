package com.qr.code.generator;

import com.qr.code.generator.commands.QRImage;
import com.qr.code.generator.commands.ReadQRImageDirectory;
import com.qr.code.generator.commands.WriteQRImageDirectory;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;


public class QRCodeReaderWriterImpl implements QRCodeReaderWriter{

  private  QRImage readQRImageDirectory;
  private  QRImage writeQRImageDirectory;

  public QRCodeReaderWriterImpl(){}


  /**
   * Performs recursive search, and generate the QR Code for every file. Also, move the
   * generated QR Code inside targetDir, preserving the directory hierarchy.
   *
   * @param sourceDir - source directory of the files that will have qr code images
   * @param targetDir - target directory where the files will be saved preserving the source directory hierarchy
   *
   * @throws IOException - if the operation fails
   * @throws WriterException - if the ZXing library can't create qr code for file
   */


  @Override
  public void write (final File sourceDir, final int depth, final File targetDir) throws IOException, WriterException, NotFoundException {
    if (!sourceDir.isDirectory()){
      throw new NoSuchFileException("Please provide path to existing directory.");
    }
    if (depth < 0) {
      throw new IllegalArgumentException("The depth should be positive number.");
    }
    writeQRImageDirectory = new WriteQRImageDirectory(sourceDir, depth, targetDir);
    writeQRImageDirectory.execute();
  }


  /**
   * Reads QR code (denoted by sourceDir) and saves the resulted file inside the targetDir, again
   * preserving the directory hierarchy.
   *
   * @param sourceDir - source directory of the qr code files
   * @param targetDir - target directory where the files will be decoded preserving the source directory hierarchy
   *
   * @throws IOException - if the operation fails
   * @throws NotFoundException - if there isn't any directory with that name
   */


  @Override
  public void read (final File sourceDir, final File targetDir) throws IOException, NotFoundException, WriterException {
    if(!sourceDir.isDirectory()){
      throw new NoSuchFileException("Please provide path to existing directory.");
    }
    readQRImageDirectory = new ReadQRImageDirectory(sourceDir, targetDir);
    readQRImageDirectory.execute();
  }

}