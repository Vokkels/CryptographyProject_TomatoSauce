package sample;

import java.io.Serializable;
import org.apache.commons.codec.binary.Hex;
import com.sun.javafx.scene.layout.region.Margins;
import com.sun.xml.internal.ws.commons.xmlutil.Converter;
import org.omg.IOP.Encoding;
import sun.nio.cs.US_ASCII;
import sun.text.normalizer.UTF16;

import java.io.*;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static sample.Controller.cm;
import static sample.Controller.progress;

class CryptoMain implements Serializable {

    private String fileLocation;
    private String message;
    private String cipherText;
    private String cipherBits;
    private encryptionType Type;
    private String encryptionKey;
    private boolean isFile;

    private boolean wasEncrypted;

    public enum encryptionType {transpositionCipher, vigenereCipher, vernamCipher, windingCipher}

    CryptoMain() {
        fileLocation = "";
        cipherText = null;
    }

    /**
     Called When a Cipher has a file location and
     wants to open a message
     Takes message location as a param
     * */
    CryptoMain(String _fileLocation, String _key, boolean _encrypt) {

        setFileLocation(_fileLocation);
        setEncryptionKey(_key);
        setWasEncrypted(_encrypt);
        setCipherText(null);
        setCipherBits(null);

        /*Opens the File*/
        OpenFile();
    }

    /**
    Called when a cipher has to work with a
    message and. Takes the message and key as
    a parameter
     */
    CryptoMain(String _message, String _key){

        setEncryptionKey(_key);
        if(Controller.useNormalASCII)
            setCipherText(_message);
        else
            setCipherText(convertToHex(_message));
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

        byte[] bytes = text.getBytes();
        return Hex.encodeHexString(bytes);
    }

    //Convert BYTE Array to HEX string
    public String bytesToHexString(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x",b&0xff));
        return sb.toString();
    }

    //Convert HEX to BYTE Array
    public byte[] convertHexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String convertByteAToString(byte[] bytes)
    {
        try {
            String decoded = new String(bytes, "ISO-8859-1");
            return decoded;
        }catch (Exception x) {System.out.println(x.getMessage());}
        return null;
    }

    public byte[] convertStringToByteA(String input)
    {
        try{
            byte[] encoded = input.getBytes("ISO-8859-1");
            return encoded;
        }
        catch (Exception x) {System.out.println(x.getMessage());}
        return null;
    }

    public String convertByteStringToASCII(String input)
    {
        try {

        }
        catch (Exception x){ System.out.println(x.getMessage());}
            return null;


    }

    public static String convertHexToPlain(String hexString)
    {
        try {

            char[] tmp = hexString.toCharArray();
            byte[] bytes = Hex.decodeHex(tmp);
            return new String(bytes, "ISO-8859-1");

        }
        catch (Exception x)
        {
            System.out.println(x.getMessage());
        }

        return null;

       /* if(hexString.length()%2 != 0){
            System.err.println("requires EVEN number of chars");
            return null;
        }
        StringBuilder sb = new StringBuilder();

        for( int i=0; i <  hexString.length() -1; i+=2 ){

        String output = hexString.substring(i, (i + 2));
          ger.parseInt(output, 16);
        sb.append((char)decimal);
    }
        return sb.toString();*/
    }

    /**
     Called at the end of a cypher to finalize
     the algorithm
    * */
    public void finalizeCipher()
    {
        if(getIsFile())
        {
            /*File Encryption Method*/
            SaveFile(getWasEncrypted());
        }
        else
        {
            /*Message Encryption Method*/
            System.out.println("DEBUG: FINALIZED: " + getCipherText());

            if(!Controller.useNormalASCII)
                setCipherText(convertHexToPlain(getCipherText()));

        }

        /** Update Progress Bar*/
        progress = 0;
    }

    public void encrypt()
    {
        System.out.print("Override Required!");
    }

    public void decrypt()
    {
        System.out.println("Override Required!");
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

    public void setFile(Boolean _isFile){this.isFile = _isFile;}

    public void setWasEncrypted(boolean wasEncrypted) {this.wasEncrypted = wasEncrypted;}

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

    public boolean getIsFile(){return this.isFile;}

    public boolean getWasEncrypted() {return wasEncrypted;}

}

class CryptoSelect_File
{
    CryptoSelect_File(CryptoMain.encryptionType type, String fileLocation, String key, boolean encrypt)
    {
        cm = null;
        switch (type)
        {
            case transpositionCipher:
                System.out.println("DEBUG: transpositionCipher Selected");
                cm = new TranspositionCipher(fileLocation, key, encrypt);
                break;

            case vigenereCipher:
                System.out.println("DEBUG: vigenereCipher Selected");
                cm = new vigenereCipher(fileLocation, key, encrypt);
                break;

            case vernamCipher:
                System.out.println("DEBUG: vernamCipher Selected");
                cm = new vernamCipher(fileLocation, key, encrypt);
                break;

            case windingCipher:
                System.out.println("DEBUG: windingCipher Selected");
                cm = new windingCipher(fileLocation, key, encrypt);
                break;

        }
        if(encrypt)
            cm.encrypt();
        else
            cm.decrypt();
    }
}

class CryptoSelect_Msg
{
    CryptoSelect_Msg(CryptoMain.encryptionType type, String Message, String key, boolean encrypt)
    {
        cm = null;
        switch (type)
        {
            case transpositionCipher:
                System.out.println("DEBUG: transpositionCipher Selected");
                cm = new TranspositionCipher(Message, key);
                break;

            case vigenereCipher:
                System.out.println("DEBUG: vigenereCipher Selected");
                cm = new vigenereCipher(Message, key);
                break;

            case vernamCipher:
                System.out.println("DEBUG: vernamCipher Selected");
                cm = new vernamCipher(Message, key);
                break;

            case windingCipher:
                System.out.println("DEBUG: windingCipher Selected");
                cm = new windingCipher(Message, key);
                break;
        }

        /** Sets the state of the cipher text */
        cm.setWasEncrypted(encrypt);
        System.out.println(cm.getWasEncrypted());

        if(encrypt)
            cm.encrypt();
        else
            cm.decrypt();
    }
}
