package sample;

import java.io.BufferedReader;
import java.io.IOException;

public class vigenereCipher extends CryptoMain
{

    vigenereCipher()
    {
        super();
        setEncryptionType(encryptionType.vigenereCipher);
    }

    vigenereCipher(String fileLocation, String key)
    {
        //Calls parent class
        super();
        //set the file location
        this.setFileLocation(fileLocation);
        //Sets encryption type
        setEncryptionType(encryptionType.vigenereCipher);

        //set the encryption key
        setEncryptionKey(key);
    }

    public char temp;

    @Override
    public void encrypt()
    {

    }

    @Override
    public void decrypt()
    {

    }
}
