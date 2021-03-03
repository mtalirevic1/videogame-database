package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.Developer;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DevFormController extends Controller {
    private Developer developer;
    private ObservableList<Developer> developers;
    private DatabaseDAO dao;

    public TextField nameField, imageField;
    public TextArea descriptionField;

    public DevFormController(Developer developer, ObservableList<Developer> developers, DatabaseDAO dao) {
        this.developer = developer;
        this.developers = developers;
        this.dao = dao;
    }

    @FXML
    public void initialize() {
        if (developer != null) {
            nameField.setText(developer.getName());
            imageField.setText(developer.getIconLink());
            descriptionField.setText(developer.getDescription());
        }
    }

    public void okClick(ActionEvent actionEvent) {
        Boolean b1 = isNameValid();
        if (!b1) {
            nameField.getStyleClass().removeAll("fieldCorrect");
            nameField.getStyleClass().add("fieldIncorrect");
        } else {
            nameField.getStyleClass().removeAll("fieldIncorrect");
            nameField.getStyleClass().add("fieldCorrect");
            if (developer != null) {
                developer.setName(nameField.getText());
                developer.setDescription(descriptionField.getText());
                developer.setIconLink(imageField.getText());
                dao.updateDeveloper(developer);
            } else {
                Developer newDev = new Developer(-1, nameField.getText(), descriptionField.getText(), imageField.getText());
                dao.addDeveloper(newDev);
            }
            developers.clear();
            developers.setAll(dao.getDevelopers());
            close();
        }
    }

    public void cancelClick(ActionEvent actionEvent) {
        close();
    }

    private Boolean isNameValid() {
        return !nameField.getText().isEmpty();
    }

    private void close() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
