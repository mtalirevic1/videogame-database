package ba.unsa.etf.rpr.projekat.DTO;

import java.util.Objects;

public class UserAccount extends Account {

    private String avatarLink;

    public UserAccount(int id, String username, String password, String avatarLink) {
        super(id, username, password);
        this.avatarLink = avatarLink;
    }

    public UserAccount() {
        super();
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAvatarLink());
    }
}
