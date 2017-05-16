package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * This class handles all the multithreading related responsibilities
 * of the progress bar and the threaded ciphers
 *
 * @author Daniel Malan <13danielmalan@gmail.com></>
 */

/**
 * Creates a new Service that handles
 * the multithreaded progress bar.
 */
public class CipherMultithreading {
    final static Service thread = new Service<Integer>() {
        @Override
        public Task createTask() {
            return new Task<Integer>() {
                @Override
                public Integer call() throws InterruptedException {
                    int i = 0;

                    while (i < 100) {
                        i = Math.round((int) Controller.progress);
                        updateProgress(i, 100);
                    }
                    return i;
                }
            };
        }
    };

    /**
     * Class for handling the cipher threads.
     */
    public static class threadedAlgo implements Runnable {

        private encryptionType type;
        private String fileName;
        private String message;
        private String key;
        private boolean encrypt;
        private boolean isFile;

        /**
         * Creates a new thread for a cipher with data from a file.
         * @param _type Algorithm type (Uses enumeration).
         * @param _fileName Directory of the file.
         * @param _key User crypto key.
         * @param encrypt Boolean stating that the cipher should encrypt or decrypt.
         */
        public void threadedAlgo_File(encryptionType _type, String _fileName, String _key, boolean encrypt) {
            this.type = _type;
            this.fileName = _fileName;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = true;
        }

        /**
         * Creates a new thread for a cipher with data from the message box.
         * @param _type Algorithm type (Uses enumeration).
         * @param _message User message entered into the text box.
         * @param _key  User crypto key.
         * @param encrypt Boolean instructing the cipher should encrypt or decrypt.
         */
        public void threadedAlgo_MSG(encryptionType _type, String _message, String _key, boolean encrypt) {
            this.type = _type;
            this.fileName = _message;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = false;
        }

        /**
         * Called when the new thread is started.
         */
        @Override
        public void run() {

            if (isFile)
                new CryptoSelect_File(type, fileName, key, encrypt);
            else
                new CryptoSelect_Msg(type, message, key, encrypt);
        }
    }
}
