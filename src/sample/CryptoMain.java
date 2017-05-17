package sample;

//import statements
import java.io.Serializable;
import org.apache.commons.codec.binary.Hex;
import java.io.*;
import javax.xml.bind.DatatypeConverter;
import static sample.Controller.progress;

/**
 * Main parent class for handling repetitive tasks
 * between crypto ciphers. Also handel all
 * backbone functionality.
 * @author Daniel Malan <13danielmalan@gmail.com></>
 **/
class CryptoMain implements Serializable {

    private String fileLocation;
    private String message;
    private String cipherText;
    private String cipherBits;
    private encryptionType Type;
    private String encryptionKey;
    private boolean isFile;
    private boolean wasEncrypted;

    /**
     * Default Constructor
     * Takes no parameters
     */
    CryptoMain() {
        fileLocation = "";
        cipherText = null;
    }

    /**
     Called When a Cipher has a file location and
     wants to open a message.
     *
     * Opens the file, converts byte array data
     * to hexadecimal and sets all instance variables
     *
     * @param _fileLocation "File location of data"
     * @param _key "User crypto key"
     * @param _encrypt "boolean specifying if the data is being encrypted or decrypted "
     * */
    CryptoMain(String _fileLocation, String _key, boolean _encrypt) {

        setFileLocation(_fileLocation);
        setEncryptionKey(_key);
        setWasEncrypted(_encrypt);
        setCipherText(null);
        setCipherBits(null);
        Controller.progress = 10;
        /*Opens the File*/
        OpenFile();
        Controller.progress = 20;
    }

    /**
    Called when a cipher has to work with a
    message and. Takes the message and key as
    a parameter
     @param _message Message entered into the text box
     @param _key User Crypto key
     */
    CryptoMain(String _message, String _key){

        setEncryptionKey(_key);
        if(Controller.useNormalASCII)
            setCipherText(_message);
        else
            setCipherText(convertToHex(_message));
    }

    /**Opens the file
     *
     * Uses instance variables set through the overloaded
     * constructor of the crypto main class
     */
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

    /**
     * Saves the data to a file at the specified location.
     *
     * Does the conversion from hexadecimal back into bytes
     * and outputs data back to a new file
     * @param isFileEncrypted if true, add the .tomato extension, else removes it.
     * */
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

    /**
     * Converts a text String into a string of bytes.
     * @param text The text to convert.
     * @return returns a string of bytes.
     * */
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

    /**
     * Converts a strings into a hexadecimal string.
     * @param text the text to convert.
     * @return returns a string of hexadecimal encoded values.
     * */
    public static String convertToHex(String text) {

        try {
            byte[] bytes = text.getBytes("ASCII");
            return Hex.encodeHexString(bytes);
        }
        catch (Exception x) {
            System.out.println(x.getMessage());
        }

        return null;
    }

    /**
     *  Convert BYTE Array dta to a HEX string
     *  @param bytes bytes to convert.
     *  @return returns a string of hexadecimal values.
     */
    public String bytesToHexString(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x",b&0xff));
        return sb.toString();
    }

    //Convert HEX to BYTE Array
    /**
     * Converts a Hexadecimal String into a array of bytes.
     * @param s String of hexadecimal data to convert.
     * @return byte array of the converted data.
     * */
    public byte[] convertHexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Converts a byte Array to a string of bytes
     * according to the ISO-8859-1 character set.
     * @param bytes bytes to convert.
     * @return string of string of bytes.
     */
    public String convertByteAToString(byte[] bytes)
    {
        try {
            String decoded = new String(bytes, "ASCII");
            return decoded;
        }catch (Exception x) {System.out.println(x.getMessage());}
        return null;
    }

    /**
     * Converts a string of bytes back into a byte array.
     * @param input String of byte data.
     * @return byteArray of converted string.
     */
    public byte[] convertStringToByteA(String input)
    {
        try{
            byte[] encoded = input.getBytes("ISO-8859-1");
            return encoded;
        }
        catch (Exception x) {System.out.println(x.getMessage());}
        return null;
    }

    /**
     * Converts a Hexadecimal string back into plain text
     * according to the ISO-8859-1 character set.
     * @param hexString String of Hex data,
     * @return String of plain text.
     */
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
    }

    /**
     Called at the end of a cypher to finalize
     the algorithm.

     Converts data accordingly.
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
        Controller.progress = 0;
    }

    /**
     * Overridable method for allowing inheritance.
     */
    public void encrypt()
    {
        System.out.print("Override Required!");
    }

    /**
     * Overridable method for allowing inheritance.
     */
    public void decrypt()
    {
        System.out.println("Override Required!");
    }

    /**
     * Sets the file location.
     * @param fileLocation file location
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    /**
     * Sets the Encryption Key
     * @param key the encryption key,
     */
    public void setEncryptionKey(String key){
        this.encryptionKey = key;
    }

    /**
     * Sets the Cipher text
     * @param EncryptedOutput the new Cipher Text.
     */
    public void setCipherText(String EncryptedOutput) {
        this.cipherText = EncryptedOutput;
    }

    /**
     * Sets the Encryption type
     * @param _encryptionType the new Encryption Type.
     */
    public void setEncryptionType(encryptionType _encryptionType){
        this.Type = _encryptionType;
    }

    /**
     * Sets teh Cipher Bits
     * @param _newCipherBits The new Cipher bits.
     */
    public void setCipherBits(String _newCipherBits){this.cipherBits = _newCipherBits;}

    /**
     * Set if a file is used
     * @param _isFile Boolean value, true if file is used.
     */
    public void setFile(Boolean _isFile){this.isFile = _isFile;}

    /**
     * Sets boolean value if data was already encrypted.
     * @param wasEncrypted
     */
    public void setWasEncrypted(boolean wasEncrypted) {this.wasEncrypted = wasEncrypted;}

    /**
     * Gets the file location.
     * @return the directory of the file.
     */
    public String getFileLocation() {
        return this.fileLocation;
    }

    /**
     * Gets the encrypted key.
     * @return the encrypted key.
     */
    public String getEncryptionKey() {
        return this.encryptionKey;
    }

    /**
     * Gets The Cipher text
     * @return returns the cipher text. Can contain both Hexadecimal or ASCII data.
     */
    public String getCipherText(){return this.cipherText;}

    /**
     * Gets the Cipher Bits.
     * @return returns the bit string.
     */
    public String getCipherBits(){return  this.cipherBits;}

    /**
     * Gets the encryption Type.
     * Is a enumerator in Crypto Main.
     * @return returns the encryption type.
     */
    public encryptionType getEncryptionType()
    {
        return this.Type;
    }

    /**
     * Gets a boolean value if its  a file or not.
     * @return the boolean value.
     */
    public boolean getIsFile(){return this.isFile;}

    /**
     * Gets a Boolean value dentoting if the data was encrypted or not.
     * @return the boolean value.
     */
    public boolean getWasEncrypted() {return wasEncrypted;}

}
