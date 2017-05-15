package sample;
/////////////////////////////////////////////////////////////////////////////////////
/**
 * Created by Deltamike276 on 4/29/2017.
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

    @Override
    public void encrypt()
    {
        //Converts the text key to binary
        String cipherKey =  convertToHex(getEncryptionKey());
        //Create empty output String
        String plainText = getCipherText();
        String out = "";

        System.out.println("DEBUG: Starting Vernam Cipher!");
        for(int i = 0,j = 0; i < plainText.length(); i++,j++) {

            /*XOR key with code*/
            long val1 = Character.digit(plainText.charAt(i),16);
            long val2 = Character.digit(cipherKey.charAt(j),16);
            long result = (val1^val2);
            out += Long.toHexString(result);

                /*Resets key*/
            if (j == cipherKey.length() - 1)
                j = 0;
        }

        setCipherText(out);
        System.out.println("DEBUG: Starting Vernam Cipher Finished Successfully!");
        finalizeCipher();
    }

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
    }
}