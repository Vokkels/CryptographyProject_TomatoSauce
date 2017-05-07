package sample;

import java.lang.*;

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
        cipherBlocks = XOR(cipherBlocks, cipherKey);
        String output = "";

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
        cipherBlocks = XOR(cipherBlocks, cipherKey);
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

    private String[][][] XOR(String[][][] plainText, String[][] key) {

        System.out.println("DEBUG: Starting XOR!");
        int blockAmount = plainText.length;
        String[][][] cipherText = new String[blockAmount][128][128];
        for (int blockCounter = 0; blockCounter < blockAmount; blockCounter++) {
            for (int i = 0; i < 128; i++) {
                for (int j = 0; j < 128; j++) {
                    if (plainText[blockCounter][i][j] != null) {

                        /*occurs for first block*/
                        long val1;
                        long val2;

                        if (blockCounter < 1)
                            val1 = Character.digit(key[i][j].toCharArray()[0], 16);
                        else
                            val1 = Character.digit(plainText[blockCounter - 1][i][j].toCharArray()[0], 16);

                        val2 = Character.digit(plainText[blockCounter][i][j].toCharArray()[0], 16);
                        long result = (val1 ^ val2);
                        cipherText[blockCounter][i][j] = Long.toHexString(result);
                    } else
                        break;
                }
            }
        }

        System.out.println("DEBUG: XOR Successful!");
        return cipherText;
    }

    private String[][] windMatrix(String[][] matrix, int degrees)
    {
        System.out.println("Winding Process Started!");
        int n = 128;
        //int A[][] = new int[n][n];
        //int B[][] = new int[n][n];

        String A[][] = new String[128][128];
        String B[][] = matrix;

        String[][] turnedMatrix = new String[128][128];
        int  c1=0, c2=n-1, r1=0, r2=n-1;
        int count=1;

        String output = "";
        for(int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                if (matrix[i][j] != null)
                    output += matrix[i][j];
            }
        }

        int k = 1;
        while(count<=n*n)
        {

            for(int i=c1;i<=c2;i++)
            {
                A[r1][i] = Integer.toString(k++);//output.substring(count-1,count);
                count++;
            }

            for(int j=r1+1;j<=r2;j++)
            {
                A[j][c2]=Integer.toString(k++);//output.substring(count-1,count);
                count++;
            }

            for(int i=c2-1;i>=c1;i--)
            {
                A[r2][i]= Integer.toString(k++);//output.substring(count-1,count);
                count++;
            }

            for(int j=r2-1;j>=r1+1;j--)
            {
                A[j][c1]= Integer.toString(k++);//output.substring(count-1,count);
                count++;
            }

            c1++;
            c2--;
            r1++;
            r2--;
        }


        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                System.out.print(A[i][j]+ "\t");
            }
            System.out.println();
        }

        return  A;
    }
}
