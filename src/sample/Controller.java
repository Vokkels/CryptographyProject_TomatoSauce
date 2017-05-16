package sample;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class Controller {

    public static CryptoMain cm;
    private String location = "";
    private boolean isFile;
    public static int progress;
    private boolean threadStarted;
    public static boolean useNormalASCII;

    @FXML
    private ToggleButton tglType;
    @FXML
    private ToggleButton tglFile;
    @FXML
    private TextField inputTxt;
    @FXML
    private RadioButton transposition;
    @FXML
    private RadioButton vigenere;
    @FXML
    private RadioButton vernam;
    @FXML
    private RadioButton winding;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private PasswordField inputKey;

    public Controller() {
        /** Start the Progressbar Thread*/
        progress = 0;
        //progressBar.progressProperty().setValue(0);
        // progressBar.progressProperty().unbind();
        // progressBar.progressProperty().bind(thread.progressProperty());
        //thread.start();
    }

    public String getFileLocation() {
        return location;
    }

    public void encryptFile(ActionEvent actionEvent) {
        runCipher(true);}

    public void decryptFile(ActionEvent actionEvent) {
        runCipher(false);
    }

    public encryptionType radioButtons() {
        if (transposition.isSelected()) {
            useNormalASCII = true;
            return encryptionType.transpositionCipher;
        }
        if (vigenere.isSelected()) {
            useNormalASCII = false;
            return encryptionType.vigenereCipher;
        }
        if (vernam.isSelected()) {
            useNormalASCII = false;
            return encryptionType.vernamCipher;
        }
        if (winding.isSelected()) {
            useNormalASCII = false;
            return encryptionType.windingCipher;
        }
        return null;
    }

    public void dropping(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file : db.getFiles()) {
                location = file.getAbsolutePath();
            }
        }
        event.setDropCompleted(success);
        event.consume();
        System.out.println(location);
    }

    public void draggingOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    public String getKey() {
        System.out.println(inputKey.getText());
        return inputKey.getText();
    }

    public String getMessageTyped() {
        return inputTxt.getText();
    }

    public void setMessage(String message) {
        inputTxt.setText(message);
    }

    public void runCipher(boolean encrypt) {

        progress = 0;

        //thread.cancel();
        try {
            if (!threadStarted) {
                progressBar.progressProperty().bind(thread.progressProperty());
                thread.start();
                threadStarted = true;
            }
        } catch (Exception x) {
        }

        threadedAlgo cipherThread = new threadedAlgo();
        ExecutorService executor = Executors.newFixedThreadPool(30);

        // thread.setExecutor(executor);

        // thread.start();

        if (tglFile.isSelected()) {
            cipherThread.threadedAlgo_File(radioButtons(), getFileLocation(), getKey(), encrypt);
            executor.execute(cipherThread);

            //new CryptoSelect_File(radioButtons(), getFileLocation(), getKey(), encrypt);
        } else if (tglType.isSelected()) {
            System.out.println("MSG: " + CryptoMain.convertToHex(getMessageTyped()));

            //cipherThread.threadedAlgo_MSG(radioButtons(), getMessageTyped(), getKey(), encrypt);
            //executor.execute(cipherThread);
            new CryptoSelect_Msg(radioButtons(), getMessageTyped().trim(), getKey(), encrypt);
            setMessage(cm.getCipherText());
        } else System.out.println("No Input Type Selected!");

    }


    public void TypeMSG(MouseEvent mouseEvent) {
        inputTxt.promptTextProperty().setValue("                      Type a message.");
        inputTxt.editableProperty().setValue(true);
        inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void SelFILE(MouseEvent mouseEvent) {
        inputTxt.promptTextProperty().setValue("                    Drag and Drop File.");
        inputTxt.editableProperty().setValue(false);
        inputTxt.textProperty().setValue("");
        inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void TypeFunct(MouseEvent mouseEvent) {
        if (tglType.isSelected())
            inputTxt.setAlignment(Pos.TOP_LEFT);
        else if (tglFile.isSelected())
            inputTxt.setAlignment(Pos.CENTER_LEFT);

    }

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

        public void threadedAlgo_File(encryptionType _type, String _fileName, String _key, boolean encrypt)
        {
            this.type = _type;
            this.fileName = _fileName;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = true;
        }

        public void threadedAlgo_MSG(encryptionType _type, String _message, String _key, boolean encrypt)
        {
            this.type = _type;
            this.fileName = _message;
            this.key = _key;
            this.encrypt = encrypt;
            isFile = false;
        }

        @Override
        public void run() {

            if(isFile)
                new CryptoSelect_File(type, fileName, key, encrypt);
            else
                new CryptoSelect_Msg(type, message, key, encrypt);
        }
    }
}