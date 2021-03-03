package ba.unsa.etf.rpr.projekat.DTO;

import java.util.Objects;

public abstract class Account {
    private Integer id;
    private String username;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getUsername().equals(account.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword());
    }

    public Account() {
        super();
    }

    public Account(int id, String username, String password) {
        this.id = id;
       /* if(!username.matches("^(?=\\S+$).{4,}$")){
            throw new IllegalArgumentException("Illegal username");
        }
        if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
            throw new IllegalArgumentException("Illegal password");
        }*/
        this.username = username;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
