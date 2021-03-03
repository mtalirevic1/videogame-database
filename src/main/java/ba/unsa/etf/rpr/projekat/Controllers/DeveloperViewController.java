package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.Developer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DeveloperViewController extends Controller {

    public Label developerName;
    public Label developerDescription;
    public ImageView developerImage;
    private Developer selectedDeveloper;

    public DeveloperViewController(Developer d) {
        this.selectedDeveloper = d;
    }

    @FXML
    public void initialize() {
        developerName.setText(selectedDeveloper.getName());
        developerDescription.setText(selectedDeveloper.getDescription());
        new Thread(() -> {
            Image image = new Image(selectedDeveloper.getIconLink());
            Platform.runLater(() -> {
                developerImage.setImage(image);
                developerImage.setFitHeight(250);
                developerImage.setFitWidth(250);
            });
        }).start();
    }

    public void okClick(ActionEvent actionEvent) {

    }

    public void cancelClick(ActionEvent actionEvent) {

    }
}
