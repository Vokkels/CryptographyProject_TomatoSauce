package sample;

import java.util.Arrays;
import java.util.Random;

public class TranspositionCipher extends CryptoMain
{
    TranspositionCipher()
    {
        super();
        setEncryptionType(encryptionType.transpositionCipher);
    }

    TranspositionCipher(String fileLocation, String key) {
        //Calls parent class
        super();
        //set the file location
        this.setFileLocation(fileLocation);
        //Sets encryption type
        setEncryptionType(encryptionType.transpositionCipher);

        //set the encryption key
        setEncryptionKey(key);
    }

    public void keysort()
    {
        char[] key = getEncryptionKey().toUpperCase().toCharArray();
        int[] pos = new int[getEncryptionKey().length()];




        for (int i = 0; i < getEncryptionKey().length(); i++)











        for(char c : key)
        {
            int temp = (int)c;
            int temp_integer = 64; //for upper case
            if(temp<=90 & temp>=65)
                System.out.print(temp-temp_integer);
        }
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



