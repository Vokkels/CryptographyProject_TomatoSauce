package sample;

import java.lang.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Main cipher class for Winding Cipher.
 *
 * Encrypts and Decrypts the data
 * and contains all methods necessary for both.
 * @author Daniel Malan <13danielmalan@gmail.com></>
 */
public class windingCipher extends CryptoMain {

    /**
     * Default constructor for winding cipher
     */
    windingCipher()
    {
        super();
        setEncryptionType(encryptionType.windingCipher);
    }

    /**
     * Overloaded constructor for winding cipher
     * for dealing with Files.
     * @param fileLocation Directory of the file.
     * @param key User crypto key.
     * @param encrypt Boolean stating that the cipher should encrypt or decrypt.
     */
    windingCipher(String fileLocation, String key, boolean encrypt)
    {
        //Calls parent class
        super(fileLocation,key, encrypt);
        //Sets encryption type
        setEncryptionType(encryptionType.windingCipher);
        setFile(true);
    }

    /**
     * Overloaded constructor for winding cipher
     * for dealing with Messages.
     * @param message User message entered into the text box.
     * @param key User crypto key.
     */
    windingCipher(String message, String key)
    {
        //Calls parent class
        super(message, key);
        setFile(false);
        setEncryptionType(encryptionType.windingCipher);
    }

    /**
     * Encryption - Winding Cipher
     *
     * Overrides the method of crypto main
     * Takes the message or files and encrypts
     * them. Output is saved in hexadecimal to
     * the instance variable, (cipherText) of the super class.
     */
    @Override
    public void encrypt()
    {
        /*Create key block*/
        String cipherKeyTmp = convertToHex(getEncryptionKey());
        String[][] cipherKey = getCipherKey(cipherKeyTmp);
        double tmp  = Math.round((getCipherText().length()/(128*128))+0.5);
        int blockAmount =  (int) tmp;

        System.out.println("Contains: " + blockAmount + " blocks");

        //Adjust Key
        int windLength = getEncryptionKey().length();
        if(windLength >= blockAmount)
            windLength = blockAmount - 1;

        Controller.progress = 30;
        String cipherBlocks[][][] = getCipherBlocks(blockAmount,getCipherText());

        String[][][] windedMatrix = new String[blockAmount][128][128];

        System.arraycopy(windMatrix(cipherBlocks, 3), 0,windedMatrix,0,blockAmount);
        Controller.progress = 35;
        cipherBlocks = XOR(windedMatrix, cipherKey,true);
        Controller.progress = 40;
        String output = "";

        Controller.progress = 70;
        for(int count = 0; count < blockAmount; count++) {
            System.out.println("BlockFirst: " + cipherBlocks[count][0][0]);
            for(int i = 0; i < 128; i++) {
                for(int j = 0; j < 128; j++)
                {
                    if(cipherBlocks[count][i][j] != null)
                        output += cipherBlocks[count][i][j];
                    else
                        break;
                }
            }
        }
        Controller.progress = 80;

        /*
        for(int count = 0; count < blockAmount; count++) {
            String[][] windedMatrix = windMatrix(cipherBlocks[count], 30);
            for(int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++) {
                    if(windedMatrix[i][j] != null)
                    output += windedMatrix[i][j];
                    else
                        break;
                }
            }
        }*/

        setCipherText(output);
        //System.out.println(output);
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

        String cipherKeyTmp = convertToHex(getEncryptionKey());
        String[][] cipherKey = getCipherKey(cipherKeyTmp);
        double tmp  = Math.round((getCipherText().length()/(128*128))+0.5);
        int blockAmount =  (int) tmp;

        System.out.println("Contains: " + blockAmount + " blocks");

        //Adjust Key
        int windLength = getEncryptionKey().length();
        if(windLength >= blockAmount)
            windLength = blockAmount - 1;

        Controller.progress = 30;
        String output = "";
        String cipherBlocks[][][] = new String[blockAmount][128][128];
        String[][][] windedMatrix = getCipherBlocks(blockAmount, getCipherText());


        System.arraycopy(windMatrix(windedMatrix,-4),0,cipherBlocks,0,blockAmount);

        //String reverse = new StringBuffer(getCipherText()).reverse().toString();

        System.arraycopy(XOR(cipherBlocks, cipherKey, false),0,windedMatrix,0,blockAmount);

       // System.arraycopy(getCipherBlocks(blockAmount, getCipherText()),0,cipherBlocks,0,blockAmount);

        Controller.progress = 35;

        // = windMatrix(cipherBlocks,-4);

        Controller.progress = 70;

        //String[][][] tmp =cipherBlocks

        for(int count = 0; count < blockAmount; count++) {
            System.out.println("BlockFirst: " + windedMatrix[count][0][0]);
            for(int i = 0; i < 128; i++) {
                for(int j = 0; j < 128; j++)
                {
                    if(windedMatrix[count][i][j] != null)
                        output += windedMatrix[count][i][j];
                    else
                        break;
                }
            }
        }
        Controller.progress = 80;
        //System.out.println(output);
        //System.out.println(convertHexToPlain(output));
        setCipherText(output);
        finalizeCipher();
    }

    /**
     * Refactor the data into data blocks.
     *
     * Takes the block size and then calls the
     * super class and gets the cipher bits and converts
     * it into blocks of 128x128 and the adds it into an array
     * thus creating a three dimensional array.
     * @param blockAmount takes the amount of blocks to create as parameter.
     * @return Returns a multidimensional array of strings.
     */
    private String[][][] getCipherBlocks(int blockAmount, String data)
    {
         /*Create Cipher Blocks*/
        String[][][] cipherBlocks =  new String[blockAmount][128][128];

        System.out.println("DEBUG: Building Plain Text Blocks!");
        int hexCounter = 0;
        for(int blockCount = 0; blockCount < blockAmount; blockCount++) {
            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++, hexCounter++) {

                    if(hexCounter < getCipherText().length())
                        cipherBlocks[blockCount][i][j] = data.substring(hexCounter, hexCounter + 1);
                    else
                        break;
                }
            }
        }

        return  cipherBlocks;
    }

    /**
     * Converts the key into a 128x128 data block
     *
     * The key will repeat if it's shorter than 16384,
     * until the entire block is filled.
     * @param cipherKeyTmp A String containing the user key.
     * @return a multidimensional block of strings of 128x128.
     */
    private String[][] getCipherKey(String cipherKeyTmp)
    {
        System.out.println("DEBUG: Building Key Block!");
        String[][] cipherKey = new String[128][128];
        for(int i = 0; i < 128; i++) {
            for (int j = 0, k = 0; j < 128; j++, k++)
            {
                cipherKey[i][j] = cipherKeyTmp.substring(k, k + 1);
                if (k == cipherKeyTmp.length() - 1)
                    k = 0;
            }
        }

        return  cipherKey;
    }

    /**
     * XOR character for character values.
     *
     * Takes the plaintext third dimensional string array
     * and XOR char for char returning the XORed blocks.
     * @param plainText Third dimensional array of plaintext data blocks.
     * @param key The user key in the form of a 128x128 data block.
     * @param forward The direction in which the XOR process is moving.
     * @return An XORed third dimensional array of string.
     */
    private String[][][] XOR(String[][][] plainText, String[][] key, boolean forward) {

        System.out.println("DEBUG: Starting XOR!");
        int blockAmount = plainText.length;
        String[][][] cipherText = new String[blockAmount][128][128];
        System.out.println("DEBUG: XOR Successful!");

        if(forward) {
            cipherText[0] = XORBlock(key, plainText[0]);
            for (int blockCounter = 1; blockCounter < blockAmount; blockCounter++) {
                cipherText[blockCounter] = XORBlock(cipherText[blockCounter - 1], plainText[blockCounter]);
            }
        }
        else
        {
            for (int blockCounter = blockAmount - 1; blockCounter > 1; blockCounter--) {
                cipherText[blockCounter] = XORBlock(plainText[blockCounter - 1], plainText[blockCounter]);
            }

            cipherText[0] = XORBlock(plainText[0], key);
        }

        System.out.println("DEBUG: XOR Successful!");
        return cipherText;
    }

    /**
     * Expansion of XOR
     *
     * Get called by XOR method, takes
     * two data blocks and XORs them.
     * @param Block1 multidimensional string array block 1.
     * @param Block2 multidimensional string array block 2.
     * @return Multidimensional string array of result block after XOR.
     */
    private String[][] XORBlock(String[][] Block1, String[][] Block2) {
        String[][] outBlock = new String[128][128];
        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {

                if (Block1[i][j] != null && Block2[i][j] != null) {
                    long val1 = Character.digit(Block1[i][j].toCharArray()[0], 16);
                    long val2 = Character.digit(Block2[i][j].toCharArray()[0], 16);
                    long result = (val1 ^ val2);
                    outBlock[i][j] = Long.toHexString(result);
                }
                else
                    outBlock[i][j] = null;
            }
        }
        return outBlock;
    }

    /**
     * Rotates the data block by x amount of degrees.
     *
     *Takes the three dimensional data block and rotates the data
     * then returns the result as a three dimensional array of strings.
     * @param matrix Third dimensional data block.
     * @param degrees The amount of rotations to be done on data blocks.
     * @return Result of rotated data blocks.
     */
    private String[][][] windMatrix(String[][][] matrix, int degrees)
    {
        List<String[][]> list = new ArrayList<String[][]>();
        int i = 0;
        for(i = 0; i < matrix.length; i++)
            list.add(matrix[i]);

        java.util.Collections.rotate(list.subList(0,i), degrees);
        return matrix;
    }
}
