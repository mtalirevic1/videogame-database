package ba.unsa.etf.rpr.projekat.Interfaces;

import javafx.event.ActionEvent;

public interface DataControl {
    void switchDb(ActionEvent actionEvent);

    void switchXml(ActionEvent actionEvent);

    void clearUI();
}
