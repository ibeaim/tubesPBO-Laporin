package com.laporin.tubesjava_laporin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
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
    @FXML
    private CheckBox login_checkBox;

    @FXML
    private Button login_CreateAccount;

    @FXML
    private Button login_Login;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_passwordHidden;

    @FXML
    private TextField login_passwordText;

    @FXML
    private Button register_Login;

    @FXML
    private TextField register_Username;

    @FXML
    private CheckBox register_checkBox;

    @FXML
    private Button register_createAccount;

    @FXML
    private AnchorPane register_form;

    @FXML
    private PasswordField register_passwordHidden;

    @FXML
    private TextField register_passwordText;

    @FXML
    private AnchorPane sideBar;

    @FXML
    private AnchorPane sideBar1;

    @FXML
    private TextField login_username;
    private Stage stage;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    public Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect
                    = DriverManager.getConnection("jdbc:mysql://localhost/laporin","root","");
            return connect;
        }catch (Exception e){e.printStackTrace();}
        return null;
    }
    private double x = 0, y = 0;
    Encryptor encryptor = new Encryptor();

    byte[] encryptionKey = {65, 12, 12, 12, 12, 12, 12, 12, 12,
            12, 12, 12, 12, 12, 12, 12 };
    public void register(){
        String password = getPasswordRegister();
        alertMessage alert = new alertMessage();
        // CHECK IF WE HAVE EMPTY FIELDS
        if(register_Username.getText().isEmpty() || password.isEmpty()){
            alert.errorMessage("All fields are necessary to be filled");
        }
        else {
            // CHECK IF THE USERNAME ALREADY EXIST
            String checkUsername = "SELECT * FROM users WHERE username = '"
                    +register_Username.getText()+"'";
            connect = connectDB();
            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkUsername);
                if (result.next()){
                    alert.errorMessage(register_Username.getText()+" is already exist");
                }
                else{
                    String insertData = "INSERT INTO users "+"(username,password) "
                            +"VALUES(?,?)";
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, register_Username.getText());
                    prepare.setString(2, encryptor.encrypt(password,encryptionKey));
                    prepare.executeUpdate();
                    alert.successMessage("Register Success!");
                    registerClearFields();
                    register_form.setVisible(false);
                    login_form.setVisible(true);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void registerClearFields(){
        register_Username.setText("");
        register_passwordHidden.setText("");
        register_passwordText.setText("");
    }
    public void showPassword(){
        if(login_checkBox.isSelected()){
            login_passwordText.setText(login_passwordHidden.getText());
            login_passwordText.setVisible(true);
            login_passwordHidden.setVisible(false);
        } else {
            login_passwordHidden.setText(login_passwordText.getText());
            login_passwordHidden.setVisible(true);
            login_passwordText.setVisible(false);
        }
    }
    public void showPasswordRegister(){
        if(register_checkBox.isSelected()){
            register_passwordText.setText(register_passwordHidden.getText());
            register_passwordText.setVisible(true);
            register_passwordHidden.setVisible(false);
        } else {
            register_passwordHidden.setText(register_passwordText.getText());
            register_passwordHidden.setVisible(true);
            register_passwordText.setVisible(false);
        }
    }
    public void login(){
        String password = getPassword();
        alertMessage alert = new alertMessage();
        // CHECK IF SOME FIELDS EMPTY
        if (login_username.getText().isEmpty()||password.isEmpty()){
            alert.errorMessage("Incorrect Username/Password");
        } else {
            String selectData = "SELECT username, password FROM users WHERE "
                    + "username = ? and password = ?";
            connect = connectDB();
            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, encryptor.encrypt(password,encryptionKey));
                result = prepare.executeQuery();
                if (result.next()){
                    // PROCEED TO MAIN
                    alert.successMessage("Login Success!");
                    Parent root = FXMLLoader.load(getClass().getResource("home-view.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    login_Login.getScene().getWindow().hide();
                } else {
                    // Alert
                    alert.errorMessage("Incorrect Username/Password");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    @FXML
//    void loginHandler(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
//        String username = usernameText.getText();
//        String password = getPassword();
//        updateUsernamesAndPasswords();
//
//        String encryptedPassword = loginInfo.get(username);
//        if(password.equals(encryptor.decrypt(encryptedPassword,encryptionKey))){
//            System.out.println("successfully login!");
//        } else {
//            System.out.println("successfully login!");
//        }
//    }
//    private void updateUsernamesAndPasswords() throws IOException {
//        Scanner scanner = new Scanner(file);
//        loginInfo.clear();
//        loginInfo = new HashMap<>();
//        while (scanner.hasNext()){
//            String[] splitInfo = scanner.nextLine().split(",");
//            loginInfo.put(splitInfo[0],splitInfo[1]);
//        }
//    }
    private String getPassword(){
        if(login_passwordText.isVisible()){
            return login_passwordText.getText();
        } else {
            return login_passwordHidden.getText();
        }
    }
    private String getPasswordRegister(){
        if(register_passwordText.isVisible()){
            return register_passwordText.getText();
        } else {
            return register_passwordHidden.getText();
        }
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
    public void switchForm (ActionEvent event){
        if(event.getSource() == register_Login){
            register_form.setVisible(false);
            login_form.setVisible(true);
        }else if (event.getSource() == login_CreateAccount){
            register_form.setVisible(true);
            login_form.setVisible(false);
        }
    }
    @FXML
    void closeProgram(ActionEvent event){
        stage.close();
    }

}
