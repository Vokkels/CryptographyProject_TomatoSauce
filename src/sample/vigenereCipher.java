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
     * Overloaded constructor for Vigenere cipher
     * for dealing with Messages.
     * @param message User message entered into the text box.
     * @param key User crypto key.
     */
    vigenereCipher(String message, String key) {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.vigenereCipher);
    }

    public char temp;

    /**
     * Encryption - Vigenere Cipher
     * <p>
     * Overrides the method of crypto main
     * Takes the message or files and encrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void encrypt() {

        Controller.progress = 50;
        char[] keyChars = getEncryptionKey().toCharArray();
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        Controller.progress = 70;
        for (int i = 0; i < bytes.length; i++) {
            int keyNR = keyChars[i % keyChars.length] - 32;
            int c = bytes[i] & 255;
            if ((c >= 32) && (c <= 127)) {
                int x = c - 32;
                x = (x - keyNR + 96) % 96;
                bytes[i] = (byte) (x + 32);
            }
        }
        Controller.progress = 80;
        String out = bytesToHexString(bytes);
        setCipherText(out);
        finalizeCipher();
    }


    /**
     * Decryption - Winding Cipher
     * <p>
     * Overrides the method of crypto main
     * Takes the message or files and decrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void decrypt() {

        Controller.progress = 50;
        char[] keyChars = getEncryptionKey().toCharArray();
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        Controller.progress = 70;
        for (int i = 0; i < bytes.length; i++) {
            int keyNR = keyChars[i % keyChars.length] - 32;
            int c = bytes[i] & 255;
            if ((c >= 32) && (c <= 127)) {
                int x = c - 32;
                x = (x + keyNR) % 96;
                bytes[i] = (byte) (x + 32);
            }
        }
        Controller.progress = 80;
        String out = bytesToHexString(bytes);
        setCipherText(out);
        finalizeCipher();
    }
}