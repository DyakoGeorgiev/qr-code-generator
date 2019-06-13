package com.qr.code.generator.commands;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRImage {
  void execute() throws IOException, NotFoundException, WriterException;
}
