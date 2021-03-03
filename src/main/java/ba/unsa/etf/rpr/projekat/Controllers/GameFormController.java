package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.Developer;
import ba.unsa.etf.rpr.projekat.DTO.VideoGame;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GameFormController extends Controller {

    private VideoGame videoGame;
    private ObservableList<VideoGame> videoGames;
    private DatabaseDAO dao;

    public TextField nameField, imageField, genreField;
    public TextArea descriptionField;
    public ChoiceBox<Developer> developerChoiceBox;
    public DatePicker releaseDatePicker;

    public GameFormController(VideoGame videoGame, ObservableList<VideoGame> videoGames, DatabaseDAO dao) {
        this.videoGame = videoGame;
        this.videoGames = videoGames;
        this.dao = dao;
    }

    @FXML
    public void initialize() {
        developerChoiceBox.setItems(dao.getDevelopers());
        releaseDatePicker.getEditor().setDisable(true);
        if (videoGame != null) {
            nameField.setText(videoGame.getName());
            genreField.setText(videoGame.getGenre());
            descriptionField.setText(videoGame.getDescription());
            imageField.setText(videoGame.getImageLink());
            developerChoiceBox.setValue(videoGame.getDeveloper());
            releaseDatePicker.setValue(videoGame.getReleaseDate());
        }
    }

    public void okClick(ActionEvent actionEvent) {
        Boolean b1 = isNameValid(), b2 = isGenreValid(), b3 = isDeveloperValid(), b4 = isDateValid();
        if (!b1) {
            nameField.getStyleClass().removeAll("fieldCorrect");
            nameField.getStyleClass().add("fieldIncorrect");
        } else {
            nameField.getStyleClass().removeAll("fieldIncorrect");
            nameField.getStyleClass().add("fieldCorrect");
        }
        if (!b2) {
            genreField.getStyleClass().removeAll("fieldCorrect");
            genreField.getStyleClass().add("fieldIncorrect");
        } else {
            genreField.getStyleClass().removeAll("fieldIncorrect");
            genreField.getStyleClass().add("fieldCorrect");
        }
        if (!b3) {
            developerChoiceBox.getStyleClass().removeAll("fieldCorrect");
            developerChoiceBox.getStyleClass().add("fieldIncorrect");
        } else {
            developerChoiceBox.getStyleClass().removeAll("fieldIncorrect");
            developerChoiceBox.getStyleClass().add("fieldCorrect");
        }
        if (!b4) {
            releaseDatePicker.getStyleClass().removeAll("fieldCorrect");
            releaseDatePicker.getStyleClass().add("fieldIncorrect");
        } else {
            releaseDatePicker.getStyleClass().removeAll("fieldIncorrect");
            releaseDatePicker.getStyleClass().add("fieldCorrect");
        }
        if (b1 && b2 && b3 && b4) {
            if (videoGame != null) {
                videoGame.setName(nameField.getText());
                videoGame.setDescription(descriptionField.getText());
                videoGame.setImageLink(imageField.getText());
                videoGame.setGenre(genreField.getText());
                videoGame.setDeveloper(developerChoiceBox.getValue());
                videoGame.setReleaseDate(releaseDatePicker.getValue());
                dao.updateVideoGame(videoGame);

            } else {
                VideoGame vg = new VideoGame(-1,
                        nameField.getText(),
                        genreField.getText(),
                        developerChoiceBox.getValue(),
                        descriptionField.getText(),
                        releaseDatePicker.getValue(),
                        imageField.getText());
                dao.addVideoGame(vg);
            }
            videoGames.clear();
            videoGames.setAll(dao.getVideoGames());
            close();
        }
    }

    public void cancelClick(ActionEvent actionEvent) {
        close();
    }

    private Boolean isNameValid() {
        return !nameField.getText().isEmpty();
    }

    private Boolean isGenreValid() {
        return !nameField.getText().isEmpty();
    }

    private Boolean isDeveloperValid() {
        return developerChoiceBox.getValue() != null;
    }

    private Boolean isDateValid() {
        return !releaseDatePicker.getEditor().getText().isEmpty();
    }

    private void close() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
