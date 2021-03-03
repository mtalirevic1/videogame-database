package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DTO.Account;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class RegistrationControllerTest {
    DatabaseDAO dao;
    ArrayList<Account> accounts;

    public static boolean containsStyle(TextField field, String style) {
        for (String s : field.getStyleClass())
            if (s.equals(style)) return true;
        return false;
    }

    @Start
    public void start(Stage primaryStage) throws Exception {
        dao = new DatabaseDAODB();
        accounts = new ArrayList<Account>();
        accounts.addAll(dao.getAdmins());
        accounts.addAll(dao.getUsers());
        UIControl.openWindow(getClass(), new RegistrationController(dao, accounts), ResourceBundle.getBundle("Language"), "register.fxml");
    }

    @Test
    void registrationTest(FxRobot robot) {
        robot.clickOn("#usernameField").write("A");
        robot.clickOn("#registerButton");
        TextField usernameField = robot.lookup("#usernameField").queryAs(TextField.class);
        assertTrue(containsStyle(usernameField, "fieldIncorrect"));

        robot.clickOn("#passwordField").write("B");
        robot.clickOn("#registerButton");
        usernameField = robot.lookup("#passwordField").queryAs(TextField.class);
        assertTrue(containsStyle(usernameField, "fieldIncorrect"));

        robot.clickOn("#passwordField").write("Pa$$w0rd");
        robot.clickOn("#registerButton");
        usernameField = robot.lookup("#passwordField").queryAs(TextField.class);
        assertTrue(containsStyle(usernameField, "fieldCorrect"));
    }

}