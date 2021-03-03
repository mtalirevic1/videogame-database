package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.AdminAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminFormController extends Controller {
    private AdminAccount adminAccount;
    private DatabaseDAO dao;
    private ObservableList<AdminAccount> adminAccounts;

    public TextField usernameField, passwordField;

    public AdminFormController(AdminAccount adminAccount, DatabaseDAO dao, ObservableList<AdminAccount> adminAccounts) {
        this.adminAccount = adminAccount;
        this.dao = dao;
        this.adminAccounts = adminAccounts;
    }

    @FXML
    public void initialize() {
        if (adminAccount != null) {
            usernameField.setText(adminAccount.getUsername());
            passwordField.setText(adminAccount.getPassword());
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
            if (adminAccount != null) {
                adminAccount.setUsername(usernameField.getText());
                adminAccount.setPassword(passwordField.getText());
                dao.updateAdmin(adminAccount);
            } else {
                AdminAccount newAccount = new AdminAccount(-1, usernameField.getText(), passwordField.getText());
                dao.addAdmin(newAccount);
            }
            adminAccounts.clear();
            adminAccounts.setAll(dao.getAdmins());
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
