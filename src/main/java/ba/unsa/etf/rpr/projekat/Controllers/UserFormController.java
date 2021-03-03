package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserFormController extends Controller {

    private UserAccount userAccount;
    private DatabaseDAO dao;
    private ObservableList<UserAccount> userAccounts;

    public TextField usernameField, passwordField, imageField;

    public UserFormController(UserAccount userAccount, DatabaseDAO dao, ObservableList<UserAccount> userAccounts) {
        this.userAccount = userAccount;
        this.dao = dao;
        this.userAccounts = userAccounts;
    }

    @FXML
    public void initialize() {
        if (userAccount != null) {
            usernameField.setText(userAccount.getUsername());
            passwordField.setText(userAccount.getPassword());
            imageField.setText(userAccount.getAvatarLink());
        }

    }

    public void okClick(ActionEvent actionEvent) {
        Boolean b1 = isUsernameValid(), b2 = isPasswordValid();
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
            if (userAccount != null) {
                userAccount.setUsername(usernameField.getText());
                userAccount.setPassword(passwordField.getText());
                userAccount.setAvatarLink(imageField.getText());
                dao.updateUser(userAccount);
            } else {
                UserAccount newAccount = new UserAccount(-1, usernameField.getText(), passwordField.getText(), imageField.getText());
                dao.addUser(newAccount);
            }
            userAccounts.clear();
            userAccounts.setAll(dao.getUsers());
            close();
        }
    }

    public void cancelClick(ActionEvent actionEvent) {
        close();
    }

    private Boolean isUsernameValid() {
        return usernameField.getText().matches("^(?=\\S+$).{4,}$");
    }

    private Boolean isPasswordValid() {
        return passwordField.getText().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    }

    private void close() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
