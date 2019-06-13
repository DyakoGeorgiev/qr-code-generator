package com.qr.code.generator;


import com.qr.code.generator.menu.Menu;

public class Main {
  public static void main (String[] args) {
    QRCodeReaderWriter qrCodeReaderWriter = new QRCodeReaderWriterImpl();
    Menu menu = new Menu(qrCodeReaderWriter);
    menu.executeMenu();
  }
}
