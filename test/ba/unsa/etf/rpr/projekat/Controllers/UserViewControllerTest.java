package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(ApplicationExtension.class)
class UserViewControllerTest {
    DatabaseDAO dao;
    UserAccount userAccount;
    Stage theStage;

    @Start
    public void start(Stage primaryStage) throws Exception {
        dao = new DatabaseDAODB();
        userAccount = new UserAccount(1, "ksaracevic", "pass", "link");
        theStage = primaryStage;
        UIControl.openWindow(getClass(), new UserViewController(dao, userAccount), ResourceBundle.getBundle("Language"), "userView.fxml");
    }

    @Test
    void userViewTest(FxRobot robot) {
        robot.clickOn("#menuHelp");
        robot.clickOn("#menuAbout");
        Label label = robot.lookup("#authorLabel").queryAs(Label.class);
        assertEquals("Author: Kenan Saracevic", label.getText());

        robot.clickOn("#usernameMenu");
        robot.clickOn("#logoutOption");
        assertFalse(theStage.isShowing());
    }

}