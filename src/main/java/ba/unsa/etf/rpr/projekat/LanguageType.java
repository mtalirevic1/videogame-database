package ba.unsa.etf.rpr.projekat;

import java.util.ResourceBundle;

public enum LanguageType {
    EN,
    BS;

    @Override
    public String toString() {
        ResourceBundle b = ResourceBundle.getBundle("Language");
        switch (this) {
            case EN:
                return b.getString("english");
            case BS:
                return b.getString("bosnian");
            default:
                throw new IllegalArgumentException();
        }
    }
}
