package sample;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Created by Deltamike76 on 5/16/2017.
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

    public static class threadedAlgo implements Runnable {

        private encryptionType type;
        private String fileName;
        private String message;
        private String key;
        private boolean encrypt;
        private boolean isFile;

        public void threadedAlgo_File(encryptionType _type, String _fileName, String _key, boolean encrypt) {
            this.type = _type;
            this.fileName = _fileName;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = true;
        }

        public void threadedAlgo_MSG(encryptionType _type, String _message, String _key, boolean encrypt) {
            this.type = _type;
            this.fileName = _message;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = false;
        }

        @Override
        public void run() {

            if (isFile)
                new CryptoSelect_File(type, fileName, key, encrypt);
            else
                new CryptoSelect_Msg(type, message, key, encrypt);
        }
    }
}
