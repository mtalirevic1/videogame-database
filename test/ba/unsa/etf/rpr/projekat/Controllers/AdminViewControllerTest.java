package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DTO.AdminAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class AdminViewControllerTest {
    DatabaseDAO dao;
    AdminAccount adminAccount;

    @Start
    public void start(Stage primaryStage) throws Exception {
        dao = new DatabaseDAODB();
        adminAccount = new AdminAccount(1, "admin", "admin");
        UIControl.openWindow(getClass(), new AdminViewController(dao, adminAccount), ResourceBundle.getBundle("Language"), "adminView.fxml");
    }

    //One large test because separating into multiple tests fails when run together
    @Test
    void addUpdateDeleteAdmin(FxRobot robot) {
        robot.clickOn("#adminTab");
        robot.clickOn("#addAdminButton");
        robot.clickOn("#usernameField").write("admin2");
        robot.clickOn("#passwordField").write("Pa$$w0rd");
        robot.clickOn("#okButton");
        Boolean adminExists = false;
        ListView listView = robot.lookup("#adminListView").queryAs(ListView.class);
        ObservableList<AdminAccount> admins = listView.getItems();
        for (AdminAccount admin : admins) {
            if (admin.getUsername().equals("admin2")) {
                adminExists = true;
                break;
            }
        }
        assertTrue(adminExists);

        robot.clickOn("admin2");
        robot.clickOn("#updateAdminButton");
        robot.clickOn("#usernameField").write("345");
        robot.clickOn("Ok");
        adminExists = false;
        admins = listView.getItems();
        for (AdminAccount admin : admins) {
            if (admin.getUsername().equals("admin2345")) {
                adminExists = true;
                break;
            }
        }
        assertTrue(adminExists);

        robot.clickOn("admin2345");
        robot.clickOn("#deleteAdminButton");
        robot.clickOn("OK");
        adminExists = false;
        admins = listView.getItems();
        for (AdminAccount admin : admins) {
            if (admin.getUsername().equals("admin2345")) {
                adminExists = true;
                break;
            }
        }
        assertFalse(adminExists);
    }
}