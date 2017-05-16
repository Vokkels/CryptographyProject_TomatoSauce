package sample;

import static sample.Controller.cm;

/**
 * This class creates the new instance (child) of crypto main, and
 * handles the process in dealing with files.
 * @author Daniel Malan <13danielmalan@gmail.com></>
 */
public class CryptoSelect_File {

    /**
     * Calls the constructor of the the receptive cipher class.
     * @param type Algorithm type (Uses enumeration).
     * @param fileLocation Directory of the file.
     * @param key User crypto key.
     * @param encrypt Boolean stating that the cipher should encrypt or decrypt.
     */
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
