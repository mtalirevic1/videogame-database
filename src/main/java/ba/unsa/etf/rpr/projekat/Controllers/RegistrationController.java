package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.Account;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RegistrationController extends Controller {

    public TextField usernameField, avatarField;
    public PasswordField passwordField;

    private DatabaseDAO dao;
    private ArrayList<Account> accounts;

    public RegistrationController(DatabaseDAO dao, ArrayList<Account> accounts) {
        this.dao = dao;
        this.accounts = accounts;
    }

    @FXML
    public void initialize() {

    }

    public void registerClick(ActionEvent actionEvent) {
        Boolean b1 = usernameValid(), b2 = passwordValid();
        if (!b1) {
            usernameField.getStyleClass().removeAll("fieldCorrect");
            usernameField.getStyleClass().add("fieldIncorrect");
        } else {
            usernameField.getStyleClass().removeAll("fieldIncorrect");
            usernameField.getStyleClass().add("fieldCorrect");
        }
        if (!b2) {
            passwordField.getStyleClass().removeAll("fieldCorrect");
            passwordField.getStyleClass().add("fieldIncorrect");
        } else {
            passwordField.getStyleClass().removeAll("fieldIncorrect");
            passwordField.getStyleClass().add("fieldCorrect");
        }
        if (b1 && b2) {
            UserAccount ua = new UserAccount(-1, usernameField.getText(), passwordField.getText(), avatarField.getText());
            dao.addUser(ua);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            accounts.add(ua);
            stage.close();
        }
    }

    //username length at least 4 characters, no whitespace allowed
    private Boolean usernameValid() {
        return usernameField.getText().matches("^(?=\\S+$).{4,}$");
    }

    //length at least 8 characters ,at least 1 digit, 1 lower case,
    // 1 upper case, 1 special character, no whitespace allowed
    private Boolean passwordValid() {
        return passwordField.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }
}
