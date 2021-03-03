package ba.unsa.etf.rpr.projekat;

import ba.unsa.etf.rpr.projekat.Controllers.LoginController;
import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ResourceBundle;


public class Main extends Application {

    private DatabaseDAO dao;

    @Override
    public void start(Stage primaryStage) throws Exception {
        dao = new DatabaseDAODB();
        UIControl.openWindow(getClass(), new LoginController(dao), ResourceBundle.getBundle("Language"), "login.fxml", false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
