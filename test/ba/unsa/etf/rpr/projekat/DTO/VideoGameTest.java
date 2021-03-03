package ba.unsa.etf.rpr.projekat.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VideoGameTest {
    VideoGame videoGame;
    Developer developer;

    @BeforeEach
    public void start() {
        developer = new Developer(1, "dev", "desc", "link");
        videoGame = new VideoGame(2, "game", "genre", developer, "desc", LocalDate.now(), "link");
    }

    @Test
    void getId() {
        assertEquals((Integer) 2, videoGame.getId());
    }

    @Test
    void setId() {
        videoGame.setId(23);
        assertEquals((Integer) 23, videoGame.getId());
    }

    @Test
    void getDeveloper() {
        assertEquals("dev", videoGame.getDeveloper().getName());
    }

    @Test
    void setDeveloper() {
        Developer dev = new Developer(3, "dev2", "desc", "link");
        videoGame.setDeveloper(dev);
        assertEquals("dev2", videoGame.getDeveloper().getName());
    }

    @Test
    void getGenre() {
        assertEquals("genre", videoGame.getGenre());
    }

    @Test
    void setGenre() {
        videoGame.setGenre("zanr");
        assertEquals("zanr", videoGame.getGenre());
    }

    @Test
    void getName() {
        assertEquals("game", videoGame.getName());
    }

    @Test
    void setName() {
        videoGame.setName("ime");
        assertEquals("ime", videoGame.getName());
    }

    @Test
    void getDescription() {
        assertEquals("desc", videoGame.getDescription());
    }

    @Test
    void setDescription() {
        videoGame.setDescription("opis");
        assertEquals("opis", videoGame.getDescription());
    }

    @Test
    void setReleaseDate() {
        videoGame.setReleaseDate(LocalDate.now());
        assertEquals(LocalDate.now(), videoGame.getReleaseDate());
    }

    @Test
    void getImageLink() {
        assertEquals("link", videoGame.getImageLink());
    }

    @Test
    void setImageLink() {
        videoGame.setImageLink("image");
        assertEquals("image", videoGame.getImageLink());
    }

    @Test
    void testToString() {
        assertEquals("game", videoGame.toString());
    }
}