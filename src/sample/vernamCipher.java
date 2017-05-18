package sample;

import javafx.concurrent.Service;

/**
 * Main chiper class for Winding Cipher.
 *
 * Encrypts and Decrypts the data
 * and contains all methods necessary for both.
 * @author Schalk Pretoruis <pretorius.scw@gmail.com/>
 */
public class vernamCipher extends CryptoMain {

    /**
     * Default constructor for vernam cipher
     */
    vernamCipher()
    {
        super();
        setEncryptionType(encryptionType.vernamCipher);
    }

    /**
     * Overloaded constructor for winding cipher
     * for dealing with Files.
     * @param fileLocation Directory of the file.
     * @param key User crypto key.
     * @param encrypt Boolean stating that the cipher should encrypt or decrypt.
     */
    vernamCipher(String fileLocation, String key, boolean encrypt)
    {
        //Calls parent class
        super(fileLocation,key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.vernamCipher);
        setFile(true);
    }

    /**
     * Overloaded constructor for winding cipher
     * for dealing with Messages.
     * @param message User message entered into the text box.
     * @param key User crypto key.
     */
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
        // Converts the encryption key to HEX string.
        String cipherKey =  convertToHex(getEncryptionKey());
        // Get and set plaintext.
        String plainText = getCipherText();
        // Create empty output String.
        String out = "";
        Controller.progress = 70;
        System.out.println("DEBUG: Starting Vernam Cipher!");
        int incrementingVal = Math.round(plainText.length());

        // Start encryption.
        for(int i = 0,j = 0; i < plainText.length(); i++,j++) {
            Controller.progress = Math.round((i*100)/incrementingVal);
            // Create long value 1 and set equal to character at i in HEX values.
            long val1 = Character.digit(plainText.charAt(i),16);
            // Create long value 1 and set equal to character at j in HEX values.
            long val2 = Character.digit(cipherKey.charAt(j),16);
            // Create long and set it equal the XORed result from val1 and val2.
            // XOR values always positive, because HEX is always >= 0.
            long result = (val1^val2);
            // Set output equals to result at i.
            out += Long.toHexString(result);
            // Reset cipherKey to 0, allow repetition of key.
            if (j == cipherKey.length() - 1)
                j = 0;
        }

        Controller.progress = 80;
        // Set the ciphertext.
        setCipherText(out);
        System.out.println("DEBUG: Starting Vernam Cipher Finished Successfully!");
        // Save the data accordingly.
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
        // Since the encryption and decryption of the vernam cipher requires the same computations
        // it is unnecessary to write redundant code.
        encrypt();
    }
}