package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class LoginControllerTest {
    DatabaseDAO dao;

    @Start
    public void start(Stage primaryStage) throws Exception {
        dao = new DatabaseDAODB();
        UIControl.openWindow(getClass(), new LoginController(dao), ResourceBundle.getBundle("Language"), "login.fxml", false);
    }

    @Test
    void loginScreenTest(FxRobot robot) {
        robot.clickOn("#usernameField").write("invalidUser");
        robot.clickOn("#loginButton");
        Label errorLabel = robot.lookup("#errorLabel").queryAs(Label.class);
        assertEquals("Username or password field empty", errorLabel.getText());

        robot.clickOn("#languageButton");
        Button languageButton = robot.lookup("#languageButton").queryAs(Button.class);
        assertEquals("Engleski", languageButton.getText());

        robot.clickOn("#registerButton");
        Label avatarField = robot.lookup("#errorLabel").queryAs(Label.class);
        assertEquals("", avatarField.getText());
    }
}