package sample;

import static sample.Controller.cm;

/**
 * This class creates the new instance (child) of crypto main, and
 * handles the process in dealing with Messages.
 * @author Daniel Malan <13danielmalan@gmail.com></>
 */

public class CryptoSelect_Msg {

    /**
     * Calls the constructor of the the receptive cipher class.
     * @param type Algorithm type (Uses enumeration).
     * @param Message User message entered into the text box.
     * @param key User crypto key.
     * @param encrypt Boolean instructing the cipher should encrypt or decrypt.
     */
    CryptoSelect_Msg(encryptionType type, String Message, String key, boolean encrypt)
    {
        cm = null;
        switch (type)
        {
            case transpositionCipher:
                System.out.println("DEBUG: transpositionCipher Selected");
                cm = new TranspositionCipher(Message, key);
                break;

            case vigenereCipher:
                System.out.println("DEBUG: vigenereCipher Selected");
                cm = new vigenereCipher(Message, key);
                break;

            case vernamCipher:
                Message = CryptoMain.convertByteAToString(CryptoMain.convertHexStringToByteArray(CryptoMain.convertToHex(Message)));
                System.out.println("DEBUG: vernamCipher Selected");
                cm = new vernamCipher(Message, key);
                break;

            case windingCipher:
                Message = CryptoMain.convertByteAToString(CryptoMain.convertHexStringToByteArray(CryptoMain.convertToHex(Message)));
                System.out.println("DEBUG: windingCipher Selected");
                cm = new windingCipher(Message, key);
                break;
        }

        /** Sets the state of the cipher text */
        cm.setWasEncrypted(encrypt);
        System.out.println(cm.getWasEncrypted());

        if(encrypt)
            cm.encrypt();
        else
            cm.decrypt();
    }
}
