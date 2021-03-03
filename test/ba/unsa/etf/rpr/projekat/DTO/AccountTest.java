package ba.unsa.etf.rpr.projekat.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    AdminAccount adminAccount;
    UserAccount userAccount;

    @BeforeEach
    public void start() {
        adminAccount = new AdminAccount(1, "admin", "password");
        userAccount = new UserAccount(2, "user", "pass", "img");
    }

    @Test
    void getId() {
        assertEquals((Integer) 1, adminAccount.getId());
        assertEquals((Integer) 2, userAccount.getId());
    }

    @Test
    void setId() {
        adminAccount.setId(40);
        userAccount.setId(50);
        assertEquals((Integer) 40, adminAccount.getId());
        assertEquals((Integer) 50, userAccount.getId());
    }

    @Test
    void getUsername() {
        assertEquals("admin", adminAccount.getUsername());
        assertEquals("user", userAccount.getUsername());
    }

    @Test
    void setUsername() {
        adminAccount.setUsername("sef");
        userAccount.setUsername("korisnik");
        assertEquals("sef", adminAccount.getUsername());
        assertEquals("korisnik", userAccount.getUsername());
    }

    @Test
    void getPassword() {
        assertEquals("password", adminAccount.getPassword());
        assertEquals("pass", userAccount.getPassword());
    }

    @Test
    void setPassword() {
        adminAccount.setPassword("lozinka");
        userAccount.setPassword("sifra");
        assertEquals("lozinka", adminAccount.getPassword());
        assertEquals("sifra", userAccount.getPassword());
    }

    @Test
    void testToString() {
        assertEquals("admin", adminAccount.toString());
        assertEquals("user", userAccount.toString());
    }
}