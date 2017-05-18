package sample;

/**
 * Main cipher class for Winding Cipher.
 *
 * Encrypts and Decrypts the data
 * and contains all methods necessary for both.
 * @author Schalk Pretoruis
 */


public class vigenereCipher extends CryptoMain {

    /**
     * Default constructor for winding cipher
     */
    vigenereCipher() {
        super();
        setEncryptionType(encryptionType.vernamCipher);
    }

    /**
     * Overloaded constructor for Vigenere cipher
     * for dealing with Files.
     * @param fileLocation Directory of the file.
     * @param key User crypto key.
     * @param encrypt Boolean stating that the cipher should encrypt or decrypt.
     */
    vigenereCipher(String fileLocation, String key, boolean encrypt) {
        //Calls parent class
        super(fileLocation, key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.vigenereCipher);
        setFile(true);
    }

    /**
     * Overloaded constructor for winding cipher
     * for dealing with Messages.
     * @param message User message entered into the text box.
     * @param key User crypto key.
     */
    vigenereCipher(String message, String key)
    {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.vigenereCipher);
    }

    /**
     * Encryption - Vigenere Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and encrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void encrypt()
    {
        Controller.progress = 40;
        // Set the encryption key and change it to a character array.
        char[] keyToChar = getEncryptionKey().toCharArray();
        // Set byte Array by converting the plaintext into bytes.
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        Controller.progress = 60;
        // Start encryption process
        for (int i = 0; i < bytes.length; i++) {
            // Expand the key.
            int keyExpanded = keyToChar[i % keyToChar.length] - 32;
            // Convert bytes to ASCII.
            int c = bytes[i] & 255;
            // Test for syntax and uppercase & lowercase Alphabet characters.
            if ((c >= 32) && (c <= 127)) {
                // set temp equal to the current plaintext in ASCII.
                // and adjust for ASCII to capital letters (ERROR CONTROL).
                int x = c - 32;
                // Execution of the cipher (MOD 96, ERROR CONTROL) as stated above.
                // ASCII 32 to 96 includes all syntax and Uppercase Alphabet characters.
                x = (x + keyExpanded) % 96;
                // Conver cipher to bytes.
                // Set ciphertext in plaintexts position in byte array.
                bytes[i] = (byte) (x + 32);
            }
        }
        Controller.progress = 80;
        //Convert output back to HEX string.
        String out = bytesToHexString(bytes);
        //System.out.println(out);
        //Set the ciphertext.
        setCipherText(out);
        //Save the data accordingly.
        finalizeCipher();
    }

    /**
     * Decryption - Winding Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and decrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void decrypt()
    {
        Controller.progress = 40;
        // Set the decryption key and change it to a character array.
        char[] keyChars = getEncryptionKey().toCharArray();
        // Set byte Array by converting the plaintext into bytes.
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        Controller.progress = 60;
        // Start decryption process
        for (int i = 0; i < bytes.length; i++) {
            // Expand the key.
            int keyNR = keyChars[i % keyChars.length] - 32;
            // Convert bytes to ASCII.
            int c = bytes[i] & 255;
            // Test for syntax and uppercase & lowercase Alphabet characters.
            if ((c >= 32) && (c <= 127)) {
                // set temp equal to the current plaintext in ASCII.
                // and adjust for ASCII to capital letters (ERROR CONTROL).
                int x = c - 32;
                // Execution of the cipher (MOD 96, ERROR CONTROL) as stated above.
                // ASCII 32 to 96 includes all syntax and Uppercase Alphabet characters.
                x = (x - keyNR + 96) % 96;
                // Conver cipher to bytes.
                // Set ciphertext in plaintexts position in byte array.
                bytes[i] = (byte) (x + 32);
            }
        }
        Controller.progress = 80;
        //Convert output back to HEX string.
        String out = bytesToHexString(bytes);
        //System.out.println(out);
        //Set the ciphertext.
        setCipherText(out);
        //Save the data accordingly.
        finalizeCipher();
    }
}