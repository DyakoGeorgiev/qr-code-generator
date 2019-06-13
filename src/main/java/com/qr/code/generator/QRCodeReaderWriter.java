package com.qr.code.generator;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.IOException;

public interface QRCodeReaderWriter {



  void write(File sourceDir, int depth, File targetDir) throws IOException, WriterException, NotFoundException;


  void read(File sourceDir, File targetDir) throws IOException, NotFoundException, WriterException;
}