package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.input.DragEvent;
import java.io.File;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class Controller {

    private String location;

    public String getFileLocation()
    {
        return location;
    }

    public void encryptFile(ActionEvent actionEvent)
    {
        new CryptoSelect(radioButtons(), getFileLocation(), getKey(), true);
    }

    public void decryptFile(ActionEvent actionEvent)
    {
        new CryptoSelect(radioButtons(), getFileLocation(), getKey(), false);
    }

    @FXML
    private RadioButton transposition;
    @FXML
    private RadioButton vigenere;
    @FXML
    private RadioButton vernam;
    @FXML
    private RadioButton winding;

    public CryptoMain.encryptionType radioButtons()
    {
        if (transposition.isSelected())
            return CryptoMain.encryptionType.transpositionCipher;
        if (vigenere.isSelected())
            return CryptoMain.encryptionType.vigenereCipher;
        if (vernam.isSelected())
            return CryptoMain.encryptionType.vernamCipher;
        if (winding.isSelected())
            return CryptoMain.encryptionType.windingCipher;
        return null;
    }

    public void dropping(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            for (File file:db.getFiles()) {
                location = file.getAbsolutePath();
            }
        }
        event.setDropCompleted(success);
        event.consume();
        System.out.println(location);
    }

    public void draggingOver(DragEvent event)
    {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
    }

    @FXML
    private PasswordField inputKey;

    public String getKey()
    {
        System.out.println(inputKey.getText());
        return inputKey.toString();
    }
}
