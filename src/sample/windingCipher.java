package sample;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deltamike276 on 4/29/2017.
 */
public class windingCipher extends CryptoMain {

    windingCipher()
    {
        super();
        setEncryptionType(encryptionType.windingCipher);
    }

    windingCipher(String fileLocation, String key)
    {
        //Calls parent class
        super();
        //set the file location
        this.setFileLocation(fileLocation);
        //Sets encryption type
        setEncryptionType(encryptionType.windingCipher);

        //set the encryption key
        setEncryptionKey(key);
    }

    @Override
    public void encrypt()
    {
        OpenFile();

        /*Create key block*/
        String cipherKeyTmp = convertToHex(getEncryptionKey());
        String[][] cipherKey = getCipherKey(cipherKeyTmp);
        double tmp  = Math.round((getCipherText().length()/(128*128))+0.5);
        int blockAmount =  (int) tmp;
        String cipherBlocks[][][] = getCipherBlocks(blockAmount);
        cipherBlocks = XOR(cipherBlocks, cipherKey,true);
        String output = "";

       // String[][][] tmp1 = XOR(cipherBlocks, cipherKey, true);
      //  String[][][] tmp2 = XOR(tmp1, cipherKey, false);

        //cipherBlocks = windMatrix(cipherBlocks,getEncryptionKey().length());

        for(int count = 0; count < blockAmount; count++) {
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
        System.out.println(output);
        SaveFile(true);
}

    @Override
    public void decrypt()
    {
        OpenFile();
        String cipherKeyTmp = convertToHex(getEncryptionKey());
        String[][] cipherKey = getCipherKey(cipherKeyTmp);
        double tmp  = Math.round((getCipherText().length()/(128*128))+0.5);
        int blockAmount =  (int) tmp;

        String output = "";
        String cipherBlocks[][][] = getCipherBlocks(blockAmount);

        //cipherBlocks = windMatrix(cipherBlocks,-getEncryptionKey().length());

        cipherBlocks = XOR(cipherBlocks, cipherKey, false);
        for(int count = 0; count < blockAmount; count++) {
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

        setCipherText(output);
        SaveFile(false);
        /*
        for(int count = 0; count < blockAmount; count++) {
            cipherBlocks[count] = windMatrix(cipherBlocks[count], -30);
        }

        String[][][] xor = XOR(cipherBlocks, cipherKey);
        String output = "";
        for(int count = 0; count < blockAmount; count++) {
            String[][] windedMatrix = cipherBlocks[count];
            for(int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++) {
                    if(windedMatrix[i][j] != null)
                        output += windedMatrix[i][j];
                    else
                        break;
                }
            }
        }

        setCipherText(output);
        System.out.println(output);
        SaveFile(false);*/
    }

    private String[][][] getCipherBlocks(int blockAmount)
    {
         /*Create Cipher Blocks*/
        String[][][] cipherBlocks =  new String[blockAmount][128][128];

        System.out.println("DEBUG: Building Plain Text Blocks!");
        int hexCounter = 0;
        for(int blockCount = 0; blockCount < blockAmount; blockCount++) {
            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++, hexCounter++) {

                    if(hexCounter < getCipherText().length())
                        cipherBlocks[blockCount][i][j] = getCipherText().substring(hexCounter, hexCounter + 1);
                    else
                        break;
                }
            }
        }

        return  cipherBlocks;
    }

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

    private String[][][] windMatrix(String[][][] matrix, int degrees)
    {
        List<String[][]> list = new ArrayList<String[][]>();
        for(int i = 0; i < matrix.length; i++)
            list.add(matrix[i]);

        java.util.Collections.rotate(list, degrees);

        for(int i = 0; i < matrix.length; i++)
            matrix[i] = list.get(i);

        return matrix;
    }
}
