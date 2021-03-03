package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DTO.Account;
import ba.unsa.etf.rpr.projekat.DTO.AdminAccount;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.LanguageType;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;


public class LoginController extends Controller {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button exitButton, loginButton, registerButton;
    public ChoiceBox<LanguageType> languageChoiceBox;
    public final ObservableList<LanguageType> languages = FXCollections.observableArrayList(LanguageType.EN, LanguageType.BS);
    public Label errorLabel, quoteBox, authorLabel, quoteLabel, languageLabel, usernameLabel, passwordLabel;
    private DatabaseDAO dao = null;
    private ArrayList<Account> accounts = null;
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");
    private Stage stage = null;
    Locale locale = null;

    public LoginController(DatabaseDAO dao) {
        this.dao = dao;
    }

    @FXML
    public void initialize() {
        if (dao != null) dao.close();
        dao = new DatabaseDAODB();
        accounts = new ArrayList<Account>();
        accounts.addAll(dao.getAdmins());
        accounts.addAll(dao.getUsers());
        languageChoiceBox.setItems(languages);
        languageChoiceBox.getSelectionModel().select(LanguageType.EN);
        loadQuote();
    }

    public void loginClick(ActionEvent actionEvent) {
        errorLabel.setText("");
        String un = usernameField.getText();
        String pass = passwordField.getText();
        Boolean exists = false;
        if (!un.equals("") && !pass.equals("")) {
            for (int i = 0; i < accounts.size(); i++) {
                Account currentAccount = accounts.get(i);
                if (un.equals(currentAccount.getUsername())) {
                    exists = true;
                    if (currentAccount.getPassword().equals(pass)) {
                        openMain(currentAccount);
                        Stage stage = (Stage) exitButton.getScene().getWindow();
                        stage.close();
                    } else {
                        errorLabel.setText(bundle.getString("incorrectPassword"));
                    }
                    break;
                }
            }
            if (!exists) {
                errorLabel.setText(bundle.getString("incorrectUsername"));
            }
        } else {
            errorLabel.setText(bundle.getString("userPassField"));
        }
    }

    public void registerClick(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), new RegistrationController(dao, accounts), ResourceBundle.getBundle("Language"), "register.fxml");
    }

    public void exitClick(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void setLabels() {
        languageLabel.setText(bundle.getString("language"));
        usernameLabel.setText(bundle.getString("username"));
        passwordLabel.setText(bundle.getString("password"));
        exitButton.setText(bundle.getString("exit"));
        loginButton.setText(bundle.getString("login"));
        registerButton.setText(bundle.getString("register"));
        quoteLabel.setText(bundle.getString("quote"));
        try {
            stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle(bundle.getString("vgDatabase"));
        } catch (NullPointerException e) {

        }
    }

    public void switchLanguage(ActionEvent actionEvent) {
        LanguageType language = languageChoiceBox.getValue();
        if (language == LanguageType.BS) {
            locale = new Locale("bs_BA");
        } else if (language == LanguageType.EN) {
            locale = new Locale("en_US", "US");
        }
        bundle = ResourceBundle.getBundle("Language", locale);
        setLabels();
    }

    public void openMain(Account account) {
        if (account instanceof AdminAccount) {
            AdminAccount adminAccount = (AdminAccount) account;
            UIControl.openWindow(getClass(), new AdminViewController(dao, adminAccount), ResourceBundle.getBundle("Language"), "adminView.fxml");
        } else {
            UserAccount userAccount = (UserAccount) account;
            UIControl.openWindow(getClass(), new UserViewController(dao, userAccount), ResourceBundle.getBundle("Language"), "userView.fxml");
        }
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void loadQuote() {
        new Thread(() -> {
            try {
                URL url = new URL("https://type.fit/api/quotes");
                BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                StringBuilder json = new StringBuilder();
                String line = null;
                while ((line = input.readLine()) != null) {
                    json.append(line);
                }
                JSONArray jsonArray = new JSONArray(json.toString());
                int index = getRandomNumberInRange(0, jsonArray.length() - 1);
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                Platform.runLater(() -> {
                    String text = jsonObject.getString("text");
                    Object author = jsonObject.get("author");
                    quoteBox.setText(text);
                    if (author instanceof String) {
                        authorLabel.setText((String) author);
                    } else {
                        authorLabel.setText(bundle.getString("anonAuthor"));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
