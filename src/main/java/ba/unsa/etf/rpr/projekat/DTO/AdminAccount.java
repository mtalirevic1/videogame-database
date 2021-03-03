package ba.unsa.etf.rpr.projekat.DTO;

public class AdminAccount extends Account {
    public AdminAccount(int id, String username, String password) {
        super(id, username, password);
    }

    public AdminAccount() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminAccount)) return false;
        AdminAccount account = (AdminAccount) o;
        return getUsername().equals(account.getUsername());
    }
}
