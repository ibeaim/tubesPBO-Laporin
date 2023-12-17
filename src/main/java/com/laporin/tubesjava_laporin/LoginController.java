package com.laporin.tubesjava_laporin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class LoginController implements Initializable {
    private double x = 0, y = 0;
    @FXML
    public TextField usernameText;
    @FXML
    private AnchorPane sideBar;
    private Stage stage;
    @FXML
    private PasswordField passwordHidden;
    @FXML
    public TextField passwordText;
    @FXML
    private CheckBox checkBox;
    @FXML
    public TextField errorField;
    @FXML
    public TextField successRegist;

    File file = new File("data.txt");

    HashMap<String, String> loginInfo = new HashMap<>();

    Encryptor encryptor = new Encryptor();

    byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };
    @FXML
    void changeVisibility(ActionEvent event){
        if (checkBox.isSelected()) {
            passwordText.setText(passwordHidden.getText());
            passwordText.setVisible(true);
            passwordHidden.setVisible(false);
            return;
        }
        passwordHidden.setText(passwordText.getText());
        passwordHidden.setVisible(true);
        passwordText.setVisible(false);
    }

    @FXML
    void loginHandler(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String username = usernameText.getText();
        String password = getPassword();
        updateUsernamesAndPasswords();

        String encryptedPassword = loginInfo.get(username);
        if(password.equals(encryptor.decrypt(encryptedPassword,encryptionKey))){
            System.out.println("successfully login!");
            errorField.setVisible(false);
            successRegist.setVisible(false);
        } else {
            errorField.setVisible(true);
            successRegist.setVisible(false);
        }
    }

    @FXML
    void createAccount(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        writeToFile();
        System.out.println("successfully register!");
        successRegist.setVisible(true);
    }

    private String getPassword(){
        if(passwordText.isVisible()){
            return passwordText.getText();
        } else {
            return passwordHidden.getText();
        }
    }

    private void updateUsernamesAndPasswords() throws IOException {
        Scanner scanner = new Scanner(file);
        loginInfo.clear();
        loginInfo = new HashMap<>();
        while (scanner.hasNext()){
            String[] splitInfo = scanner.nextLine().split(",");
            loginInfo.put(splitInfo[0],splitInfo[1]);
        }
    }

    private void writeToFile() throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        String username = usernameText.getText();
        String password = getPassword();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));

        writer.write(username + "," + encryptor.encrypt(password,encryptionKey) + "\n");
        writer.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        sideBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });

    }
    @FXML
    void closeProgram(ActionEvent event){
        stage.close();
    }

}
