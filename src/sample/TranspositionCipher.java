package sample;

import com.sun.deploy.util.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;

public class TranspositionCipher extends CryptoMain
{
    TranspositionCipher()
    {
        super();
        setEncryptionType(encryptionType.vernamCipher);
    }

    TranspositionCipher(String fileLocation, String key, boolean encrypt)
    {
        //Calls parent class
        super(fileLocation,key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.vigenereCipher);
        setFile(true);
    }

    TranspositionCipher(String message, String key)
    {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.vernamCipher);
    }

    @Override
    public void encrypt()
    {
        System.out.println("DEBUG: Starting Transposition Cipher encryption!");
        String cipherKey =  getEncryptionKey();
        String data = getCipherText();

        int keyLength = cipherKey.length();
        int dataLength = data.length();

        /*Establishes table size for perfect data fit*/
        int columns ;
        for(columns = keyLength; columns < data.length(); columns--)
            if(dataLength % columns == 0)
                break;

        int[] keyValue;
        /*Adjust Key According to mod*/
        if(columns < keyLength) {
            keyLength = columns;
            keyValue = getKeyValueRepresentation(cipherKey.substring(0, keyLength));
        }
        else
            keyValue = getKeyValueRepresentation(cipherKey);

        System.out.println("MOD: " + columns);
        System.out.println("LEN: " + data.length());

        int rows = Math.abs(Math.round((dataLength/columns) + 0.5f)) - 1;

        String[][] table = new String[rows][columns];

        /*Reads data into the table*/
        for(int col = 0, cnt = 0; col < table.length; col++)
            for(int row = 0; row < table[0].length; row++, cnt++) {

                if(cnt < data.length())
                    table[col][row] = data.substring(cnt, cnt + 1);
                else
                    table[col][row] = getRandomVal();
            }

         /*Prints key values*/
         for(int i = 0; i < keyLength; i++)
             System.out.print(keyValue[i] + " ");

        System.out.println(" ");

        /*Prints out the Table*/
        for(int col = 0; col < table.length; col++) {
            for (int row = 0; row < table[0].length; row++) {
                System.out.print(table[col][row] + " ");
            }
            System.out.println("");
        }

        String output = "";

        int[] tmpKeyValue = new int[keyLength];
        for(int i = 0; i < keyValue.length; i++)
            tmpKeyValue[i] = keyValue[i];

        Arrays.sort(tmpKeyValue);
        for(int i = 0; i < columns; i++)
        {
            int val = findValAtArrayIndex(keyValue, tmpKeyValue[i]);
            System.out.println("COL: " + val);
            for(int j = 0; j < rows; j++)
                output += table[j][val];
        }

        System.out.println("Output Length: " + output.length());
        setCipherText(output);
        finalizeCipher();
        System.out.println("DEBUG: Transposition Cipher encrypt Successfully!");
    }


    @Override
    public void decrypt() {
        System.out.println("DEBUG: Starting Transposition Cipher decryption!");

        String cipherKey =  getEncryptionKey();
        String data = getCipherText();

        int keyLength = cipherKey.length();
        int dataLength = data.length();
        System.out.println("Length " + dataLength);
        /*Establishes table size for perfect data fit*/
        int columns ;
        for(columns = keyLength; columns < data.length(); columns--)
            if(dataLength % columns == 0)
                break;

        int[] keyValue;
        /*Adjust Key According to mod*/
        if(columns < keyLength) {
            keyLength = columns;
            keyValue = getKeyValueRepresentation(cipherKey.substring(0, keyLength));
        }
        else
            keyValue = getKeyValueRepresentation(cipherKey);

        System.out.println("MOD: " + columns);
        System.out.println("LEN: " + data.length());

        int rows = Math.abs(Math.round((dataLength/columns) + 0.5f)) - 1;

        String[][] table = new String[rows][columns];

        int[] tmpKeyValue = new int[keyLength];
        for(int i = 0; i < keyValue.length; i++)
            tmpKeyValue[i] = keyValue[i];

        String input = "";
        Arrays.sort(tmpKeyValue);
        Collections.reverse(Arrays.asList(tmpKeyValue));
        for(int i = 0, cnt = 0; i < columns; i++, cnt++)
        {
            int val = findValAtArrayIndex(keyValue, tmpKeyValue[i]);
            System.out.println("COL: " + val);
            for(int j = 0; j < rows; j++)
                table[j][val] = data.substring(cnt, cnt++);
        }

          /*Prints out the Table*/
        for(int col = 0; col < table.length; col++) {
            for (int row = 0; row < table[0].length; row++) {
                System.out.print(table[col][row] + " ");
            }
            System.out.println("");
        }

        //setCipherText(output);
        //finalizeCipher();

        System.out.println("DEBUG: Transposition Cipher decrypt Successful!");
    }

    private int[] getKeyValueRepresentation(String key)
    {
        int length = key.length();
        int[] keyVal = new int[length];
        for(int i = 0; i < length; i++)
            keyVal[i] = key.charAt(i) - 48;

        return keyVal;
    }

    private String getRandomVal()
    {
        return String.format("%x",(int)(Math.random()*100)).substring(0,1);
    }

    private int findValAtArrayIndex(int[] array, int val)
    {
        for(int i = 0; i < array.length; i++)
        {
            if(val == array[i])
                return i;
        }

        return -1;
    }
}



