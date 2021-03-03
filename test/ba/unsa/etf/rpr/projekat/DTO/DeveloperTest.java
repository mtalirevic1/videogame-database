package ba.unsa.etf.rpr.projekat.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeveloperTest {
    Developer developer;

    @BeforeEach
    public void start() {
        developer = new Developer(1, "Dev", "Desc", "icon");
    }

    @Test
    void getId() {
        assertEquals((Integer) 1, developer.getId());
    }

    @Test
    void setId() {
        developer.setId(2);
        assertEquals((Integer) 2, developer.getId());
    }

    @Test
    void getName() {
        assertEquals("Dev", developer.getName());
    }

    @Test
    void setName() {
        developer.setName("name");
        assertEquals("name", developer.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Desc", developer.getDescription());
    }

    @Test
    void setDescription() {
        developer.setDescription("opis");
        assertEquals("opis", developer.getDescription());
    }

    @Test
    void getIconLink() {
        assertEquals("icon", developer.getIconLink());
    }

    @Test
    void setIconLink() {
        developer.setIconLink("link");
        assertEquals("link", developer.getIconLink());
    }
}