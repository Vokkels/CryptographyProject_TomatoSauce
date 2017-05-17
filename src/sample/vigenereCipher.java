package sample;

//http://www.eqianli.tech/questions/4527927/encrypt-byte-array-using-vigenere-cipher-in-java
//http://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
//http://javarevisited.blogspot.co.za/2013/03/convert-and-print-byte-array-to-hex-string-java-example-tutorial.html

public class vigenereCipher extends CryptoMain
{
    vigenereCipher()
    {
        super();
        setEncryptionType(encryptionType.vernamCipher);
    }

    vigenereCipher(String fileLocation, String key, boolean encrypt)
    {
        //Calls parent class
        super(fileLocation,key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.vigenereCipher);
        setFile(true);
    }

    vigenereCipher(String message, String key)
    {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.vigenereCipher);
    }

    public char temp;

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
        char[] keyChars = getEncryptionKey().toCharArray();
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        byte[] temp = bytes;
        for (int i = 0; i < temp.length; i++) {
            int keyNR = keyChars[i % keyChars.length] - 32;
            int c = bytes[i] & 255;
            if ((c >= 32) && (c <= 127)) {
                int x = c - 32;
                x = (x + keyNR) % 96;
                bytes[i] = (byte) (x + 32);
            }
        }
        String out = bytesToHexString(bytes);
        System.out.println(out);
        setCipherText(out);
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
        char[] keyChars = getEncryptionKey().toCharArray();
        byte[] bytes = convertHexStringToByteArray(getCipherText());
        byte[] temp = bytes;
        for (int i = 0; i < temp.length; i++) {
            int keyNR = keyChars[i % keyChars.length] - 32;
            int c = bytes[i] & 255;
            if ((c >= 32) && (c <= 127)) {
                int x = c - 32;
                x = (x - keyNR + 96) % 96;
                bytes[i] = (byte) (x + 32);
            }
        }
        String out = bytesToHexString(bytes);
        System.out.println(out);
        setCipherText(out);
        finalizeCipher();
    }
}