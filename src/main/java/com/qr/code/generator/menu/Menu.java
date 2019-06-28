package com.qr.code.generator.menu;

import com.qr.code.generator.QRCodeReaderWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

  private static final String LINE = "-----------------------";
  private final QRCodeReaderWriter  qrCodeReaderWriter;

  public Menu(QRCodeReaderWriter qrCodeReaderWriter){
    this.qrCodeReaderWriter = qrCodeReaderWriter;
  }

  private int getUserChoiceOperation() {

    final Scanner in = new Scanner(System.in);
    int choice;
    System.out.println();
    System.out.println("The program works only if the files in the directories are .txt format!");
    System.out.println("The files should not have too much text, because the program will crash due to file content limitations ");
    System.out.println("What operation do you want to execute?");
    System.out.println("Type the operation number in the command LINE.");
    System.out.println(LINE);
    System.out.println("1 - Write QRCodes to target directory preserving source directory hierarchy");
    System.out.println("2 - Read QRCode directory and restore the original .txt files.");
    System.out.println("Type 0 to exit the program.");
    choice = in.nextInt();
    return choice;
  }

  public void executeMenu() {
    final Scanner in = new Scanner(System.in);
    while (true) {
      switch (getUserChoiceOperation()) {
        case 0:
          System.out.println("Program exit!");
          return;
        case 1:
          System.out.println("Type the source directory (Example: F:\\TextFiles) : ");
          final String sourcePath = in.nextLine();
          final File sourceDir = new File(sourcePath);
          System.out.println("Type the depth that you want to go. (Example: 5) : ");
          final int depth = in.nextInt();
          in.nextLine();
          System.out.println("Type the target directory (Example: F:\\TextFilesQRCodes) : ");
          final String targetPath = in.nextLine();
          final File targetDir = new File(targetPath);
          try {
            qrCodeReaderWriter.write(sourceDir,depth,targetDir);
          } catch (IOException | WriterException | NotFoundException e) {
            e.printStackTrace();
          }
          System.out.println("Done!");
          break;
        case 2:
          System.out.println("Type the source directory (Example: F:\\TextFilesQRCodes) : ");
          final String source = in.nextLine();
          final File sourceDirectory = new File(source);
          System.out.println("Type the target directory (Example: F:\\TextFiles) : ");
          final String target = in.nextLine();
          final File targetDirectory = new File(target);
          try {
            qrCodeReaderWriter.read(sourceDirectory,targetDirectory);
          } catch (IOException | WriterException | NotFoundException e) {
            e.printStackTrace();
          }
          System.out.println("Done!");
          break;
        default:
          System.out.println("Type the command number between 1 or 2 and 0 if you want to stop execution.");
          break;
      }
    }
  }
}
