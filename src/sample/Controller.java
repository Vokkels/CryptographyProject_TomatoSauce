package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.DragEvent;
import java.io.File;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class Controller {

    public static CryptoMain cm;
    private String location = "";
    private boolean isFile;


    public String getFileLocation()
    {
        return location;
    }

    public void encryptFile(ActionEvent actionEvent)
    {
       runCipher(true);
    }

    public void decryptFile(ActionEvent actionEvent)
    {
        runCipher(false);
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
        return inputKey.getText();
    }

    public String getMessageTyped()
    {
        return inputTxt.getText();
    }

    public void setMessage(String message)
    {
        inputTxt.setText(message);
    }

    @FXML
    private ToggleButton tglType;

    @FXML
    private ToggleButton tglFile;

    @FXML
    private TextField inputTxt;

    public void TypeMSG(MouseEvent mouseEvent)
    {
        inputTxt.promptTextProperty().setValue("                      Type a message.");
        inputTxt.editableProperty().setValue(true);
        inputTxt.setAlignment(Pos.CENTER_LEFT);
        isFile = false;
        setMessage(inputTxt.getText());
    }

    public void SelFILE(MouseEvent mouseEvent)
    {
        inputTxt.promptTextProperty().setValue("                    Drag and Drop File.");
        inputTxt.editableProperty().setValue(false);
        inputTxt.textProperty().setValue("");
        inputTxt.setAlignment(Pos.CENTER_LEFT);
        isFile = true;
    }

    public void TypeFunct(MouseEvent mouseEvent)
    {
        if (tglType.isSelected())
            inputTxt.setAlignment(Pos.TOP_LEFT);
        else if (tglFile.isSelected())
            inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void runCipher(boolean encrypt)
    {
        if(tglFile.isSelected())
        {
            new CryptoSelect_File(radioButtons(), getFileLocation(), getKey(),encrypt);
        }
        else if(tglType.isSelected())
        {
            new CryptoSelect_Msg(radioButtons(), getMessageTyped(), getKey(),encrypt);
            setMessage(cm.getCipherText());
        }
        else System.out.println("No Input Type Selected!");
    }

    @FXML
    private ToggleButton tglType;

    @FXML
    private ToggleButton tglFile;

    @FXML
    private TextField inputTxt;

    public void TypeMSG(MouseEvent mouseEvent)
    {
        inputTxt.promptTextProperty().setValue("                      Type a message.");
        inputTxt.editableProperty().setValue(true);
        inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void SelFILE(MouseEvent mouseEvent)
    {
        inputTxt.promptTextProperty().setValue("                    Drag and Drop File.");
        inputTxt.editableProperty().setValue(false);
        inputTxt.textProperty().setValue("");
        inputTxt.setAlignment(Pos.CENTER_LEFT);
    }

    public void TypeFunct(MouseEvent mouseEvent)
    {
        if (tglType.isSelected())
            inputTxt.setAlignment(Pos.TOP_LEFT);
        else if (tglFile.isSelected())
            inputTxt.setAlignment(Pos.CENTER_LEFT);

    }
}
