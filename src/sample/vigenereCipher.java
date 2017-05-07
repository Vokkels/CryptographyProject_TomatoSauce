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

        //set the encryption key
        setEncryptionKey(key);

        //Tutorial of process to follow
        //Opens the file with the correct file location
        OpenFile();
        //returns teh cipherText <Here modifications can be done with it>
        getCipherText();
        //after modified data save it with
        //setCipherText("Modified data goes here");
        //Gets the users crypto KEY
        getEncryptionKey();
        //Saves file, if isFileEncrypted is true it will be saved with the .tomato extension
        SaveFile(true);
    }

    @Override
    public void encrypt()
    {

    }

    @Override
    public void decrypt()
    {

    }
}
