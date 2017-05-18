package sample;

//Standard Imports.
import com.sun.deploy.util.ArrayUtil;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Main cipher class for Transposition Cipher.
 *
 * Encrypts and Decrypts the data
 * and contains all methods necessary for both.
 * @author Daniel Malan <13danielmalan@gmail.com></>
 */
public class TranspositionCipher extends CryptoMain {
    /**
     * Default constructor for Transposition Cipher.
     */
    TranspositionCipher() {
        super();
        setEncryptionType(encryptionType.transpositionCipher);
    }

    /**
     * Overloaded constructor for Transposition cipher
     * for dealing with Files.
     *
     * @param fileLocation Directory of the file.
     * @param key          User crypto key.
     * @param encrypt      Boolean stating that the cipher should encrypt or decrypt.
     */
    TranspositionCipher(String fileLocation, String key, boolean encrypt) {
        //Calls parent class
        super(fileLocation, key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.transpositionCipher);
        setFile(true);
    }

    /**
     * Overloaded constructor for Transposition cipher
     * for dealing with Messages.
     *
     * @param message User message entered into the text box.
     * @param key     User crypto key.
     */
    TranspositionCipher(String message, String key) {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.transpositionCipher);
        System.out.println("MSG " + convertToHex(message));
        System.out.println("Input " + getCipherText());
    }

    /**
     * Encryption - Transposition Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and encrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void encrypt() {
        //System.out.print("KEY FIX: " + tabular(getEncryptionKey()));
        System.out.println("DEBUG: Starting Transposition Cipher encryption!");
        String cipherKey = tabular(getEncryptionKey());
        String data = (getCipherText());

        Controller.progress = 30;

        //---Extreme Case---
        //Data length smaller than key
        if (data.length() < cipherKey.length())
            cipherKey = cipherKey.substring(0, data.length());

        Controller.progress = 40;

        System.out.println("DEBUG: KEY:" + cipherKey.toString());

        //Gets the length of variables
        int keyLength = cipherKey.length();
        int dataLength = data.length();

        Controller.progress = 50;

        //Establishes table size for perfect data fit
        int columns;
        for (columns = keyLength; columns < data.length(); columns--)
            if (dataLength % columns == 0)
                break;

        Controller.progress = 60;

        int[] keyValue;
        //---Extreme Case---
        /*Adjust Key According to mod*/
        if (columns < keyLength) {
            keyLength = columns;
            keyValue = getKeyValueRepresentation(cipherKey.substring(0, keyLength));
        } else
            keyValue = getKeyValueRepresentation(cipherKey);

        int rows = Math.abs(Math.round((dataLength / columns) + 0.5f)) - 1;

        Controller.progress = 70;
        String[][] table = new String[rows][columns];

        /*Reads data into the table*/
        for (int col = 0, cnt = 0; col < table.length; col++)
            for (int row = 0; row < table[0].length; row++, cnt++) {

                if (cnt < data.length())
                    table[col][row] = data.substring(cnt, cnt + 1);
                else
                    table[col][row] = getRandomVal();
            }

        Controller.progress = 80;

        //only used for debug
        //Prints key values//
        for (int i = 0; i < keyLength; i++)
            System.out.print(keyValue[i] + " ");

        System.out.println();
        System.out.println("--------------------");

        //Prints out the Table
        for (int col = 0; col < table.length; col++) {
            for (int row = 0; row < table[0].length; row++) {
                System.out.print(table[col][row] + " ");
            }
            System.out.println("");
        }


        Controller.progress = 90;

        String output = "";

        int[] tmpKeyValue = new int[keyLength];
        for (int i = 0; i < keyValue.length; i++)
            tmpKeyValue[i] = keyValue[i];

        Arrays.sort(tmpKeyValue);
        for (int i = 0; i < columns; i++) {
            int val = findValAtArrayIndex(keyValue, tmpKeyValue[i]);
            for (int j = 0; j < rows; j++)
                output += table[j][val];
        }

        setCipherText(output);
        finalizeCipher();
        System.out.println("DEBUG: Transposition Cipher encrypt Successfully!");
    }

    /**
     * Decryption - Transposition Cipher
     * <p>
     * Overrides the method of crypto main
     * Takes the message or files and decrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void decrypt() {
        System.out.println("DEBUG: Starting Transposition Cipher decryption!");

        //System.out.print("KEY FIX: " + tabular(getEncryptionKey()));
        String cipherKey = tabular(getEncryptionKey());
        String data = (getCipherText());

        Controller.progress = 30;

        //---Extreme Case---
        //Data length smaller than key
        if (data.length() < cipherKey.length())
            cipherKey = cipherKey.substring(0, data.length());

        Controller.progress = 40;

        //Gets the length of variables
        int keyLength = cipherKey.length();
        int dataLength = data.length();

        Controller.progress = 50;

        //Establishes table size for perfect data fit
        int columns;
        for (columns = keyLength; columns < data.length(); columns--)
            if (dataLength % columns == 0)
                break;

        Controller.progress = 60;

        int[] keyValue;
        //---Extreme Case---
        /*Adjust Key According to mod*/
        if (columns < keyLength) {
            keyLength = columns;
            keyValue = getKeyValueRepresentation(cipherKey.substring(0, keyLength));
        } else
            keyValue = getKeyValueRepresentation(cipherKey);

        int rows = Math.abs(Math.round((dataLength / columns) + 0.5f)) - 1;

        Controller.progress = 60;
        String[][] table = new String[rows][columns];

        int[] tmpKeyValue = new int[keyLength];
        for (int i = 0; i < keyValue.length; i++)
            tmpKeyValue[i] = keyValue[i];

        String input = "";
        Arrays.sort(tmpKeyValue);
        Collections.reverse(Arrays.asList(tmpKeyValue));

        System.out.println(data);

        for (int i = 0, cnt = 0; i < columns; i++) {
            int val = findValAtArrayIndex(keyValue, tmpKeyValue[i]);
            for (int j = 0; j < rows; j++, cnt++)
                table[j][val] = data.substring(cnt, cnt + 1);
        }

        Controller.progress = 80;

        //only used for debug
        //Prints key values//
        for (int i = 0; i < keyLength; i++)
            System.out.print(keyValue[i] + " ");

        System.out.println();
        System.out.println("--------------------");

        //Only used for debugging.
        //Prints out the Table

        for (int col = 0; col < table.length; col++) {
            for (int row = 0; row < table[0].length; row++) {
                System.out.print(table[col][row] + " ");
            }
            System.out.println("");
        }

        Controller.progress = 90;

        for (int col = 0; col < table.length; col++)
            for (int row = 0; row < table[0].length; row++)
                input += table[col][row];

        setCipherText(input);
        finalizeCipher();
        System.out.println("DEBUG: Transposition Cipher decrypt Successful!");
    }

    /**
     * Gets a integer values for each character in key
     * <p>
     * Uses ascii to turn character value into integer.
     *
     * @param key String user key.
     * @return Integer array containing values for key at each index.
     */
    private int[] getKeyValueRepresentation(String key) {
        List<Integer> history = new ArrayList<Integer>();
        int length = key.length();
        int[] keyVal = new int[length];
        for (int i = 0, inc = 0; i < length; ) {
            int val = key.charAt(i) - 48;
            if (!history.contains(val + inc)) {
                keyVal[i] = val + inc;
                history.add(val + inc);
                i++;
                inc = 0;
            }
            else inc++;
        }
        return keyVal;
    }

    /**
     * Gets a random hex value
     * @return returns a string of a single random hex character.
     */
    private String getRandomVal()
    {
        return String.format("%x",(int)(Math.random()*100)).substring(0,1);
    }

    /**
     * Finds Value in Array
     *
     * Finds a value inside an array and returns its
     * index at that position.
     * @param array integer key array
     * @param val value to find.
     * @return return the index at which that value is found or -1 if not found.
     */
    private int findValAtArrayIndex(int[] array, int val)
    {
        for(int i = 0; i < array.length; i++)
        {
            if(val == array[i])
                return i;
        }

        return -1;
    }

    private String tabular(String key)
    {
        String out = "";
        for(int i = 0; i < key.length(); i++)
        {
            out += ((int) key.charAt(i));
        }

        return out;
    }
}



