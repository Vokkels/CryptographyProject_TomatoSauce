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

    @Override
    public void encrypt()
    {
        OpenFile();

        int[] keyPosition = rearrangeKey(getEncryptionKey());
        String cipherTxt = getCipherText();
        System.out.println(cipherTxt);
        int keyLength = keyPosition.length;
        int cipherTxtLength = cipherTxt.length();

        System.out.println("DEBUG: Starting Transposition Cipher encryption!");





        int row = (int) Math.ceil((double) cipherTxtLength / keyLength);

        char[][] table = new char[row][keyLength];
        int k = 0;
        for (int r = 0; r < row; r++)
            for (int c = 0; c < keyLength; c++)
            {
                if (cipherTxtLength == k)
                {
                    table[r][c] = RandomAlpha();
                    k--;
                }
                else
                    table[r][c] = cipherTxt.charAt(k);
                k++;
            }

        String encryptedCipherTxt = "";
        for (int x = 0; x < keyLength; x++) {
            for (int y = 0; y < keyLength; y++) {
                if (x == keyPosition[y]) {
                    for (int a = 0; a < row; a++) {
                        encryptedCipherTxt += table[a][y];
                    }
                }
            }
        }

        setCipherText(encryptedCipherTxt);
        System.out.println(encryptedCipherTxt);
        System.out.println("DEBUG: Transposition Cipher encrypt Successfully!");
        SaveFile(true);
    }

    @Override
    public void decrypt()
    {
        OpenFile();

        int[] keyPosition = rearrangeKey(getEncryptionKey());
        String cipherTxt = getCipherText();
        int keyLength = keyPosition.length;
        int cipherTxtLength = cipherTxt.length();

        System.out.println("DEBUG: Starting Transposition Cipher decryption!");
        System.out.println(cipherTxt);
        int row = (int) Math.ceil((double) cipherTxtLength / keyLength);

        String regex = "(?<=\\G.{" + row + "})";
        String[] get = cipherTxt.split(regex);

        String[][] table = new String[row][keyLength];
        for (int x = 0; x < keyLength; x++) {
            for (int y = 0; y < keyLength; y++) {
                if (keyPosition[x] == y) {
                    for (int z = 0; z < row; z++) {
                        table[z][y] = get[keyPosition[y]].substring(z, z + 1);
                    }
                }
            }
        }

        String decryptedCipherTxt = "";
        for (int x = 0; x < row; x++)
            for (int y = 0; y < keyLength; y++)
                decryptedCipherTxt += table[x][y];

        setCipherText(decryptedCipherTxt);
        System.out.println(decryptedCipherTxt);
        System.out.println("DEBUG: Transposition Cipher decrypt Successfully!");
        SaveFile(false);
    }

    public char RandomAlpha()
    {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'a');
    }

    public int[] rearrangeKey(String key)
    {
        String[] keys = key.split("");
        Arrays.sort(keys);
        int[] num = new int[key.length()];
        for (int x = 0; x < keys.length; x++)
            for (int y = 0; y < key.length(); y++)
                if (keys[x].equals(key.charAt(y) + ""))
                {
                    num[y] = x;
                    break;
                }
        return num;
    }
}



