package sample;

import javafx.concurrent.Service;

/**
 * Main class for communication  between the interface and activities
 * @author Schalk Pretorius </pretorius.scw@gmail.com>
 */
public class vernamCipher extends CryptoMain {

    vernamCipher()
    {
        super();
        setEncryptionType(encryptionType.vernamCipher);
    }

    vernamCipher(String fileLocation, String key, boolean encrypt)
    {
        //Calls parent class
        super(fileLocation,key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.vernamCipher);
        setFile(true);
    }

    vernamCipher(String message, String key)
    {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.vernamCipher);
    }


    /**
     * Encryption - Vernam Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and decrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */

    @Override
    public void encrypt()
    {
        Controller.progress = 50;
        //Converts the text key to binary
        String cipherKey =  convertToHex(getEncryptionKey());
        //Create empty output String
        String plainText = getCipherText();
        String out = "";
        Controller.progress = 70;
        System.out.println("DEBUG: Starting Vernam Cipher!");

        int incrementingVal = Math.round(plainText.length());

        for(int i = 0,j = 0; i < plainText.length(); i++,j++) {

            Controller.progress = Math.round((i*100)/incrementingVal);

            /*XOR key with code*/
            long val1 = Character.digit(plainText.charAt(i),16);
            long val2 = Character.digit(cipherKey.charAt(j),16);
            long result = (val1^val2);
            out += Long.toHexString(result);

                /*Resets key*/
            if (j == cipherKey.length() - 1)
                j = 0;
        }

        Controller.progress = 80;
        setCipherText(out);
        System.out.println("DEBUG: Starting Vernam Cipher Finished Successfully!");
        finalizeCipher();
    }

    /**
     * Decryption - Vernam Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and decrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void decrypt()
    {
        //Converts the text key to binary
        String cipherKey = convertToHex(getEncryptionKey());
        //Create empty output String
        String encryptedText = getCipherText();
        String out = "";

        System.out.println("DEBUG: Starting Vernam Cipher!");
        for(int i = 0,j = 0; i < encryptedText.length(); i++,j++) {

            /*XOR key with code*/
            long val1 = Character.digit(encryptedText.charAt(i),16);
            long val2 = Character.digit(cipherKey.charAt(j),16);
            long result = (val1^val2);
            out += Long.toHexString(result);

            /*Resets key*/
            if (j == cipherKey.length() - 1)
                j = 0;
        }

        setCipherText(out);
        System.out.println("DEBUG: Starting Vernam Cipher Finished Successfully!");
        System.out.println("Decrypt: " + out);
        finalizeCipher();
        Controller.progress = 100;
    }
}