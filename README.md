Personal Project **qr-code-generator**.  Made by Dyako Georgiev Dyakov.

# Resources
[QRGen Library](https://github.com/kenglxn/QRGen)
This library is easy to use and has broad set of methods. That's why i used it instead of ZXing Google library.

## Creating QR Code images

Given existing directory **(sourceDir)** and depth of search **(depth)**, the program generates QR code for every file. It stores the information inside another directory **(targetDir)**, preserving the directory hierarchy, but randomly changing the name of the file (resulted QR Code). The mapping between, the original file and the respective QR Code is stored in every directory in a hidden file called, **qr_codes**.

## Example


    QRCodeReaderWriter qrCodeReaderWriter = new QRCodeReaderWriterImpl();
    qrCodeReaderWriter.write(sourceDir,depth,targetDir);


## Reading QR Code Images

Given existing directory **(sourceDir)** , the program reads QR code files and stores the information inside them in directory **(targetDir)**, preserving the directory hierarchy.  The program gets the original file name of the .txt file from  the mapping between the original file and the respective QR Code from the stored in every directory hidden file called, **qr_codes**.

## Example

    QRCodeReaderWriter qrCodeReaderWriter = new QRCodeReaderWriterImpl();
    qrCodeReaderWriter.read(sourceDirectory,targetDirectory);

