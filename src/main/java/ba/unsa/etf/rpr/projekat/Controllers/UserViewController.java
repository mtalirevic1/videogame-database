package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAODB;
import ba.unsa.etf.rpr.projekat.DAO.DatabaseDAOXML;
import ba.unsa.etf.rpr.projekat.DTO.Developer;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.DTO.VideoGame;
import ba.unsa.etf.rpr.projekat.Exceptions.InvalidSearchTermException;
import ba.unsa.etf.rpr.projekat.Interfaces.DataControl;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.JasperReport;
import ba.unsa.etf.rpr.projekat.SearchType;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;


public class UserViewController extends Controller implements DataControl {

    private DatabaseDAO dao;
    private UserAccount accountInUse;
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public BorderPane mainBorder;
    public Menu usernameMenu;
    public TextField searchFieldVG, searchFieldDV;
    public ChoiceBox<SearchType> choiceBoxVG, choiceBoxDV;
    public TilePane tilePaneVG, tilePaneDV;
    public ObservableList<VideoGame> videoGames = FXCollections.observableArrayList();
    public ObservableList<Developer> developers = FXCollections.observableArrayList();
    public final ObservableList<SearchType> searchTypesVG =
            FXCollections.observableArrayList(
                    SearchType.All, SearchType.Name, SearchType.Genre, SearchType.Developer);
    public final ObservableList<SearchType> searchTypesDV =
            FXCollections.observableArrayList(
                    SearchType.All, SearchType.Name);

    public UserViewController(DatabaseDAO dao, UserAccount accountInUse) {
        this.dao = dao;
        this.accountInUse = accountInUse;
    }

    @FXML
    public void initialize() {
        choiceBoxVG.setItems(searchTypesVG);
        choiceBoxVG.setValue(SearchType.All);
        choiceBoxDV.setItems(searchTypesDV);
        choiceBoxDV.setValue(SearchType.All);
        usernameMenu.setText(accountInUse.getUsername());
        new Thread(() -> {
            Image image = null;
            try {
                image = new Image(accountInUse.getAvatarLink());
            } catch (IllegalArgumentException e) {
                try {
                    image = new Image(new FileInputStream("resources/img/controller.png"));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            Image finalImage = image;
            Platform.runLater(() -> {
                ImageView imageView = new ImageView(finalImage);
                imageView.setFitWidth(15);
                imageView.setFitHeight(15);
                usernameMenu.setGraphic(imageView);
            });
        }).start();

    }


    @Override
    public void switchDb(ActionEvent actionEvent) {
        if (dao != null) dao.close();
        clearUI();
        dao = new DatabaseDAODB();
    }

    @Override
    public void switchXml(ActionEvent actionEvent) {
        if (dao != null) dao.close();
        clearUI();
        dao = new DatabaseDAOXML();
    }

    @Override
    public void clearUI() {
        tilePaneVG.getChildren().clear();
        tilePaneDV.getChildren().clear();
    }

    public void clickAbout(ActionEvent actionEvent) {
        UIControl.openWindow(getClass(), null, bundle, "about.fxml");
    }

    public void clickLogout(ActionEvent actionEvent) {
        Stage stage = (Stage) mainBorder.getScene().getWindow();
        stage.close();
    }

    public void searchVG(ActionEvent actionEvent) {
        try {
            SearchType selectedType = choiceBoxVG.getValue();
            String search = searchFieldVG.getText();
            if (search.equals("") && selectedType != SearchType.All) {
                throw new InvalidSearchTermException(bundle.getString("searchException"));
            }
            videoGames.clear();
            tilePaneVG.getChildren().clear();
            if (selectedType.equals(SearchType.All)) {
                videoGames.setAll(dao.getVideoGames());
            } else if (selectedType.equals(SearchType.Name)) {
                videoGames.setAll(dao.getVideoGameByName(search));
            } else if (selectedType.equals(SearchType.Developer)) {
                videoGames.setAll(dao.getVideoGameByDeveloper(search));
            } else if (selectedType.equals(SearchType.Genre)) {
                videoGames.setAll(dao.getVideoGameByGenre(search));
            }
            for (VideoGame vg : videoGames) {
                Button button = new Button();
                button.setOnMouseClicked(event -> {
                    openGameView(vg);
                });
                new Thread(() -> {
                    Image image = null;
                    try {
                        image = new Image(vg.getImageLink());
                    } catch (IllegalArgumentException e) {
                        try {
                            image = new Image(new FileInputStream("resources/img/controller.png"));
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    Image finalImage = image;
                    Platform.runLater(() -> {
                        ImageView imageView = new ImageView(finalImage);
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
                        button.setGraphic(imageView);
                        tilePaneVG.getChildren().add(button);
                    });
                }).start();
            }
        } catch (InvalidSearchTermException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("exception"));
            alert.setHeaderText(e.getLocalizedMessage());
            alert.setContentText(bundle.getString("emptySearch"));
            alert.showAndWait();
        }
    }

    public void searchDV(ActionEvent actionEvent) {
        try {
            SearchType selectedType = choiceBoxDV.getValue();
            String search = searchFieldDV.getText();
            if (search.equals("") && selectedType != SearchType.All) {
                throw new InvalidSearchTermException(bundle.getString("searchException"));
            }
            developers.clear();
            tilePaneDV.getChildren().clear();
            if (selectedType.equals(SearchType.All)) {
                developers.setAll(dao.getDevelopers());
            } else if (selectedType.equals(SearchType.Name)) {
                developers.setAll(dao.getDeveloperByName(search));
            }
            for (Developer dv : developers) {
                Button button = new Button();
                button.setOnMouseClicked(event -> {
                    openDeveloperView(dv);
                });
                new Thread(() -> {
                    Image image = null;
                    try {
                        image = new Image(dv.getIconLink());
                    } catch (IllegalArgumentException e) {
                        try {
                            image = new Image(new FileInputStream("resources/img/controller.png"));
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                    Image finalImage = image;
                    Platform.runLater(() -> {
                        ImageView imageView = new ImageView(finalImage);
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
                        button.setGraphic(imageView);
                        tilePaneDV.getChildren().add(button);
                    });
                }).start();
            }
        } catch (InvalidSearchTermException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(bundle.getString("exception"));
            alert.setHeaderText(e.getLocalizedMessage());
            alert.setContentText(bundle.getString("emptySearch"));
            alert.showAndWait();
        }
    }

    public void openGameView(VideoGame videoGame) {
        UIControl.openWindow(getClass(), new GameViewController(videoGame, dao, accountInUse), ResourceBundle.getBundle("Language"), "gameDetails.fxml");
    }

    public void openDeveloperView(Developer dv) {
        UIControl.openWindow(getClass(), new DeveloperViewController(dv), ResourceBundle.getBundle("Language"), "developerDetails.fxml");
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
}
