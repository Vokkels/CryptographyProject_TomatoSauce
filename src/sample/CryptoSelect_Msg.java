package sample;

import static sample.Controller.cm;

/**
 * Created by Deltamike76 on 5/16/2017.
 */
public class CryptoSelect_Msg {

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
                System.out.println("DEBUG: vernamCipher Selected");
                cm = new vernamCipher(Message, key);
                break;

            case windingCipher:
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
