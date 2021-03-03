package ba.unsa.etf.rpr.projekat;

import java.util.ResourceBundle;

public enum SearchType {
    All,
    Name,
    Genre,
    Developer;

    @Override
    public String toString() {
        ResourceBundle b = ResourceBundle.getBundle("Language");
        switch (this) {
            case All:
                return b.getString("all");
            case Name:
                return b.getString("name");
            case Genre:
                return b.getString("genre");
            case Developer:
                return b.getString("developer");
            default:
                throw new IllegalArgumentException();
        }
    }
}
