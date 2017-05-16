package sample;

import static sample.Controller.cm;

/**
 * Created by Deltamike76 on 5/16/2017.
 */
public class CryptoSelect_File {

    CryptoSelect_File(encryptionType type, String fileLocation, String key, boolean encrypt)
    {
        cm = null;
        switch (type)
        {
            case transpositionCipher:
                System.out.println("DEBUG: transpositionCipher Selected");
                cm = new TranspositionCipher(fileLocation, key, encrypt);
                break;

            case vigenereCipher:
                System.out.println("DEBUG: vigenereCipher Selected");
                cm = new vigenereCipher(fileLocation, key, encrypt);
                break;

            case vernamCipher:
                System.out.println("DEBUG: vernamCipher Selected");
                cm = new vernamCipher(fileLocation, key, encrypt);
                break;

            case windingCipher:
                System.out.println("DEBUG: windingCipher Selected");
                cm = new windingCipher(fileLocation, key, encrypt);
                break;

        }
        if(encrypt)
            cm.encrypt();
        else
            cm.decrypt();
    }
}
