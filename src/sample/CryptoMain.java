package sample;

import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

class CryptoMain implements Serializable {

    private String fileLocation;
    private String cipherText;
    private String cipherBits;
    private encryptionType Type;
    private String encryptionKey;

    public enum encryptionType {transpositionCipher, vigenereCipher, vernamCipher, windingCipher}

    ;

    CryptoMain() {
        fileLocation = "";
        cipherText = null;
    }

    CryptoMain(String fileLocation) {
        this.fileLocation = fileLocation;
        cipherText = null;
    }

    /*Returns the Object of the opened File*/
    public void OpenFile() {
        try {
            System.out.println("DEBUG: Trying to open file");

            final int bufferSize = 512;
            final byte[] buffer = new byte[bufferSize];
            final StringBuilder sb = new StringBuilder();

            FileInputStream FIS = new FileInputStream(fileLocation);
            int bytesRead;

            System.out.println("DEBUG: File opened successfully!");
            System.out.println("DEBUG: Trying to convert data to HEX");
            while ((bytesRead = FIS.read(buffer)) > 0) {
                for (int i = 0; i < bytesRead; i++)
                    sb.append(String.format("%02X", buffer[i]));
            }
            System.out.println("DEBUG: Byte data convection successful!");
            FIS.close();
            setCipherText(sb.toString());
            //cipherBytes = DatatypeConverter.parseHexBinary(sb.toString());
            //setCipherBits(convertToBits(sb.toString()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*Saves the data to a file at the specified location.*/
    public void SaveFile(boolean isFileEncrypted) {
        System.out.println("DEBUG: Trying to convert HEX Data to bytes");
        byte[] data = DatatypeConverter.parseHexBinary(getCipherText());

        System.out.println("DEBUG: HEX data convection successful!");

        try {
            System.out.println("DEBUG: Trying to write to file");
            String outputFile = "";

            if (isFileEncrypted)
                outputFile = getFileLocation() + ".tomato";
            else if (getFileLocation().contains(".tomato")) {
                outputFile = getFileLocation().replace(".tomato", "");
            } else
                outputFile = getFileLocation();

            RandomAccessFile RAF = new RandomAccessFile(outputFile, "rw");
            RAF.write(data);
            RAF.close();

            System.out.println("DEBUG: File written successfully!");
        } catch (Exception x) {
            System.out.println(x.getStackTrace());
        }
    }

    public static String convertToBits(String text) {
        System.out.println("DEBUG: Convection to Bits Initialized!");
        String output = "";
        char[] tmp = text.toCharArray();
        for (int i = 0; i < tmp.length; i++) {
            output += Integer.toBinaryString(tmp[i]);
        }
        System.out.println("DEBUG: Convection to Bits Finished Successfully!");
        return output;
    }

    public static String convertToHex(String text) {
        return String.format("%040x", new BigInteger(1, text.getBytes(StandardCharsets.UTF_16)));
    }

    public void encrypt()
    {
        System.out.print("Override Needed!");
    }

    public void decrypt()
    {
        System.out.println("Override Needed!");
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void setEncryptionKey(String key){
        this.encryptionKey = key;
    }

    public void setCipherText(String EncryptedOutput) {
        this.cipherText = EncryptedOutput;
    }

    public void setEncryptionType(encryptionType _encryptionType){
        this.Type = _encryptionType;
    }

    public void setCipherBits(String _newCipherBits){this.cipherBits = _newCipherBits;}

    public String getFileLocation() {
        return this.fileLocation;
    }

    public String getEncryptionKey() {
        return this.encryptionKey;
    }

    public String getCipherText(){return this.cipherText;}

    public String getCipherBits(){return  this.cipherBits;}

    public encryptionType getEncryptionType()
    {
        return this.Type;
    }
}

class CryptoSelect
{
    CryptoSelect(CryptoMain.encryptionType type, String fileLocation, String key, boolean encrypt)
    {

        CryptoMain crypto = null;
        switch (type)
        {
            case transpositionCipher:
                System.out.println("DEBUG: transpositionCipher Selected");
                crypto = new TranspositionCipher(fileLocation, key);
                break;

            case vigenereCipher:
                System.out.println("DEBUG: vigenereCipher Selected");
                crypto = new vigenereCipher(fileLocation, key);
                break;

            case vernamCipher:
                System.out.println("DEBUG: vernamCipher Selected");
                crypto = new vernamCipher(fileLocation, key);
                break;

            case windingCipher:
                System.out.println("DEBUG: windingCipher Selected");
                crypto = new windingCipher(fileLocation, key);
                break;

        }
        if(encrypt)
            crypto.encrypt();
        else
            crypto.decrypt();
    }
}
