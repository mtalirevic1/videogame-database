package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAOXML;
import ba.unsa.etf.rpr.projekat.DTO.AdminAccount;
import ba.unsa.etf.rpr.projekat.DTO.Developer;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.DTO.VideoGame;
import ba.unsa.etf.rpr.projekat.Interfaces.DataControl;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.JasperReport;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminViewController extends Controller implements DataControl {

    private DatabaseDAO dao;
    private AdminAccount accountInUse;
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public BorderPane mainBorder;
    public Menu usernameMenu;
    public ListView<VideoGame> VGListView;
    public ListView<Developer> DVListView;
    public ListView<UserAccount> userListView;
    public ListView<AdminAccount> adminListView;
    private ObservableList<VideoGame> videoGames;
    private ObservableList<Developer> developers;
    private ObservableList<AdminAccount> adminAccounts;
    private ObservableList<UserAccount> userAccounts;

    public AdminViewController(DatabaseDAO dao, AdminAccount accountInUse) {
        this.dao = dao;
        this.accountInUse = accountInUse;
        videoGames = dao.getVideoGames();
        developers = dao.getDevelopers();
        adminAccounts = dao.getAdmins();
        userAccounts = dao.getUsers();
    }

    @FXML
    public void initialize() {
        usernameMenu.setText(accountInUse.getUsername());
        ImageView imageView = new ImageView();
        try {
            imageView.setImage(new Image(new FileInputStream("resources/img/admin.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        usernameMenu.setGraphic(imageView);
        configureLists();
        VGListView.setItems(videoGames);
        DVListView.setItems(developers);
        adminListView.setItems(adminAccounts);
        userListView.setItems(userAccounts);
    }

    private void getAllData() {
        clearUI();
        videoGames.setAll(dao.getVideoGames());
        developers.setAll(dao.getDevelopers());
        adminAccounts.setAll(dao.getAdmins());
        userAccounts.setAll(dao.getUsers());
    }

    @Override
    public void switchDb(ActionEvent actionEvent) {
        if (dao != null) dao.close();
        clearUI();
        dao = new DatabaseDAODB();
        getAllData();
    }

    @Override
    public void switchXml(ActionEvent actionEvent) {
        if (dao != null) dao.close();
        clearUI();
        dao = new DatabaseDAOXML();
        getAllData();
    }

    @Override
    public void clearUI() {
        videoGames.clear();
        developers.clear();
        adminAccounts.clear();
        userAccounts.clear();
    }

    public void clickAbout(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), null, bundle, "about.fxml");
    }

    public void clickLogout(ActionEvent actionEvent) {
        Stage stage = (Stage) mainBorder.getScene().getWindow();
        stage.close();
    }

    public void showReport(ActionEvent actionEvent) {
        if (dao instanceof DatabaseDAODB) {
            DatabaseDAODB dbDao = (DatabaseDAODB) dao;
            try {
                new JasperReport().showReport(dbDao.getConn());
            } catch (JRException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void addVideoGame(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), new GameFormController(null, videoGames, dao), bundle, "videoGameForm.fxml", false);
    }

    public void updateVideoGame(ActionEvent actionEvent) {
        VideoGame selectedGame = VGListView.getSelectionModel().getSelectedItem();
        if (selectedGame != null) {
            UIControl.openWindow(getClass(), new GameFormController(selectedGame, videoGames, dao), bundle, "videoGameForm.fxml", false);
        }
    }

    public void deleteVideoGame(ActionEvent actionEvent) {
        VideoGame videoGame = VGListView.getSelectionModel().getSelectedItem();
        if (videoGame == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("deleteVG"));
        alert.setHeaderText(bundle.getString("deletingVG") + " " + videoGame.getName());
        alert.setContentText(bundle.getString("confirmDeleteVG") + " " + videoGame.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.removeReviewsByGame(videoGame);
            dao.removeVideoGame(videoGame);
            videoGames.remove(videoGame);
        }
    }

    public void addDeveloper(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), new DevFormController(null, developers, dao), bundle, "developerForm.fxml", false);
    }

    public void updateDeveloper(ActionEvent actionEvent) {
        Developer selectedDev = DVListView.getSelectionModel().getSelectedItem();
        if (selectedDev != null) {
            UIControl.openWindow(getClass(), new DevFormController(selectedDev, developers, dao), bundle, "developerForm.fxml", false);
        }
    }

    public void deleteDeveloper(ActionEvent actionEvent) {
        Developer developer = DVListView.getSelectionModel().getSelectedItem();
        if (developer == null) return;
        ObservableList<VideoGame> res = dao.getVideoGameByDeveloper(developer.getName());
        if (!res.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("deleteDV"));
            alert.setHeaderText(bundle.getString("deletingDV") + " " + developer.getName());
            alert.setContentText(bundle.getString("confirmDeleteDV") + " " + developer.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                dao.removeDeveloper(developer);
                developers.remove(developer);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(bundle.getString("cannotDeleteDV"));
            alert.setContentText(bundle.getString("DevVGExists"));
            alert.show();
        }
    }

    public void addUser(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), new UserFormController(null, dao, userAccounts), bundle, "userForm.fxml", false);
    }

    public void updateUser(ActionEvent actionEvent) {
        UserAccount selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UIControl.openWindow(getClass(), new UserFormController(selectedUser, dao, userAccounts), bundle, "userForm.fxml", false);
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        UserAccount userAccount = userListView.getSelectionModel().getSelectedItem();
        if (userAccount == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(bundle.getString("deleteUser"));
        alert.setHeaderText(bundle.getString("deletingUser") + " " + userAccount.getUsername());
        alert.setContentText(bundle.getString("confirmDeleteUser") + " " + userAccount.getUsername() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.removeReviewsByUser(userAccount);
            dao.removeUser(userAccount);
            userAccounts.remove(userAccount);
        }
    }

    public void addAdmin(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), new AdminFormController(null, dao, adminAccounts), bundle, "adminForm.fxml", false);
    }

    public void updateAdmin(ActionEvent actionEvent) {
        AdminAccount selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
        if (selectedAdmin != null) {
            if (selectedAdmin.equals(accountInUse)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(bundle.getString("error"));
                alert.setHeaderText(bundle.getString("cannotUpdateAdmin"));
                alert.setContentText(bundle.getString("loggedAdmin"));
                alert.show();
            } else {
                UIControl.openWindow(getClass(), new AdminFormController(selectedAdmin, dao, adminAccounts), bundle, "adminForm.fxml", false);
            }
        }
    }

    public void deleteAdmin(ActionEvent actionEvent) {
        AdminAccount selectedAdmin = adminListView.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) return;
        if (selectedAdmin.equals(accountInUse)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("error"));
            alert.setHeaderText(bundle.getString("cannotDeleteAdmin"));
            alert.setContentText(bundle.getString("loggedAdmin"));
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(bundle.getString("deleteAdmin"));
            alert.setHeaderText(bundle.getString("deletingAdmin") + " " + selectedAdmin.getUsername());
            alert.setContentText(bundle.getString("confirmDeleteAdmin") + " " + selectedAdmin.getUsername() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                dao.removeAdmin(selectedAdmin);
                adminAccounts.remove(selectedAdmin);
            }
        }
    }

    private void configureLists() {
        VGListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(VideoGame item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    UIControl.loadImage(imageView, item.getImageLink(), 50, 50);
                    setGraphic(imageView);
                    setText(item.getName());
                    setFont(Font.font(14));
                }
            }
        });

        DVListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(Developer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    UIControl.loadImage(imageView, item.getIconLink(), 50, 50);
                    setGraphic(imageView);
                    setText(item.getName());
                    setFont(Font.font(14));
                }
            }
        });

        userListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(UserAccount item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    UIControl.loadImage(imageView, item.getAvatarLink(), 50, 50);
                    setGraphic(imageView);
                    setText(item.getUsername());
                    setFont(Font.font(14));
                }
            }
        });

        adminListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(AdminAccount item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    try {
                        imageView.setImage(new Image(new FileInputStream("resources/img/admin.png")));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                    setText(item.getUsername());
                    setFont(Font.font(14));
                }
            }
        });
    }
}
