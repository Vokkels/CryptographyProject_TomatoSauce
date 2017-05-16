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

/**
 * Main class for communication  between the interface and activities
 * @author Schalk Pretorius </pretorius.scw@gmail.com>
 */
public class Controller {

    public static CryptoMain cm;
    private String location = "";
    private boolean isFile;
    public static int progress;
    private boolean threadStarted;
    public static boolean useNormalASCII;

    /**
     * The @FXML annotation an FMXLLoader to inject values in an FXML file reference.
     */
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
        progress = 0;
    }

    public String getFileLocation() {
        return location;
    }

    /**
     *
     * @param actionEvent
     */
    public void encryptFile(ActionEvent actionEvent)
    {
        if (tglType.isSelected() && inputTxt.getText() == null)
            ErrorMessage("TypeSomething", null);
        else if (tglFile.isSelected() && location == "")
            ErrorMessage("DropSomething", null);
        else if (!tglFile.isSelected() && !tglType.isSelected())
            ErrorMessage("SelectSomething", null);
        else if (inputKey.getText() == null || inputKey.getText().length() < 8)
            ErrorMessage("TypePassKey", null);
        else
            runCipher(true);

    }

    /**
     *
     * @param actionEvent
     */
    public void decryptFile(ActionEvent actionEvent)
    {
        if (tglType.isSelected() && inputTxt.getText() == null)
            ErrorMessage("TypeSomething", "decrpyt");
        else if (tglFile.isSelected() && location == "")
            ErrorMessage("DropSomething", "decrpyt");
        else if (!tglFile.isSelected() && !tglType.isSelected())
            ErrorMessage("SelectSomething", "decrpyt");
        else if (inputKey.getText() == null || inputKey.getText().length() < 8)
            ErrorMessage("TypePassKey", "decrpyt");
        else
            runCipher(false);
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param event
     */
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

    /**
     *
     * @param event
     */
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

    /**
     *
     * @param encrypt
     */
    public void runCipher(boolean encrypt) {

        progress = 0;

        try {
            if (!threadStarted) {
                progressBar.progressProperty().bind(CipherMultithreading.thread.progressProperty());
                CipherMultithreading.thread.start();
                threadStarted = true;
            }
        } catch (Exception x) {
            System.out.println(x.getMessage());
        }

        CipherMultithreading.threadedAlgo cipherThread = new CipherMultithreading.threadedAlgo();
        ExecutorService executor = Executors.newFixedThreadPool(30);

        if (tglFile.isSelected()) {
            cipherThread.threadedAlgo_File(radioButtons(), getFileLocation(), getKey(), encrypt);
            executor.execute(cipherThread);

        } else if (tglType.isSelected()) {

            new CryptoSelect_Msg(radioButtons(), getMessageTyped().trim(), getKey(), encrypt);
            setMessage(cm.getCipherText());
        } else System.out.println("No Input Type Selected!");

    }

    public void TypeMSG(MouseEvent mouseEvent) {
        inputTxt.setText(null);
        inputKey.setText(null);
        inputTxt.promptTextProperty().setValue("                      Type a message.");
        inputTxt.editableProperty().setValue(true);
        inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void SelFILE(MouseEvent mouseEvent) {
        inputKey.setText(null);
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

    public void ErrorMessage(String whatMessage, String decrypt)
    {
        if (whatMessage == "TypeSomething") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            if (decrypt == null)
                alert.setContentText("There is no message to encrypt. \nPlease type something in the designated area.");
            else
                alert.setContentText("There is no message to decrypt. \nPlease type something in the designated area.");
            alert.showAndWait();
        }

        if (whatMessage == "DropSomething")
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            if (decrypt == null)
                alert.setContentText("There is currently no file selected to encrypt. \nPlease drag and drop a file into the designated area.");
            else
                alert.setContentText("There is currently no file selected to decrypt. \nPlease drag and drop a file into the designated area.");
            alert.showAndWait();
        }

        if (whatMessage == "TypePassKey") {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please type a passkey longer than 8 characters to continue.");
            alert.showAndWait();
        }

        if (whatMessage == "SelectSomething")
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            if (decrypt == null)
                alert.setContentText("There is currently no option selected to perform any encryption on.");
            else
                alert.setContentText("There is currently no option selected to perform any decryption on.");
            alert.showAndWait();
        }
    }
}