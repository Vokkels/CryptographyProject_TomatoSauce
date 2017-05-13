package sample;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Deltamike76 on 4/29/2017.
 */
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

        System.out.println("LOOK AT ME THIS IS MY KEY: " + key);

        //set the encryption key
        setEncryptionKey(key);
    }

    @Override
    public void encrypt()
    {
        //repeat key to fill plaintext
        OpenFile();
        System.out.println(getEncryptionKey());
        String plainText = getCipherText();
        String key = getFilledKey(getEncryptionKey(),plainText.length());
        System.out.println(key);

    }

    @Override
    public void decrypt()
    {

    }

    private String getFilledKey(String key, int length)
    {
        String out = "";
        for(int i = 0, j = 0; i < length; i++)
        {
            if(j < key.length()) {
                out += key.charAt(j);
                j++;
            }
            else{
                j = 0;
                out += key.charAt(j);
            }
        }
        return out;
    }
}
