package ba.unsa.etf.rpr.projekat.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameReviewTest {
    VideoGame videoGame;
    UserAccount userAccount;
    Developer developer;
    GameReview gameReview;

    @BeforeEach
    public void start() {
        developer = new Developer(1, "dev", "desc", "link");
        videoGame = new VideoGame(2, "game", "genre", developer, "desc", LocalDate.now(), "link");
        userAccount = new UserAccount(3, "user", "password", "link");
        gameReview = new GameReview(videoGame, userAccount, 5, "comm");
    }

    @Test
    void getVideoGame() {
        assertEquals((Integer) 2, gameReview.getVideoGame().getId());
    }

    @Test
    void setVideoGame() {
        Developer dev = new Developer(1, "dev", "desc", "link");
        VideoGame vg = new VideoGame(6, "testGame", "genre", dev, "desc", LocalDate.now(), "link");
        gameReview.setVideoGame(vg);
        assertEquals("testGame", gameReview.getVideoGame().getName());
    }

    @Test
    void getAccount() {
        assertEquals((Integer) 3, gameReview.getAccount().getId());
    }

    @Test
    void setAccount() {
        UserAccount acc = new UserAccount(6, "testUser", "password", "link");
        gameReview.setAccount(acc);
        assertEquals("testUser", gameReview.getAccount().getUsername());
    }

    @Test
    void getScore() {
        assertEquals((Integer) 5, gameReview.getScore());
    }

    @Test
    void setScore() {
        gameReview.setScore(3);
        assertEquals((Integer) 3, gameReview.getScore());
    }

    @Test
    void getComment() {
        assertEquals("comm", gameReview.getComment());
    }

    @Test
    void setComment() {
        gameReview.setComment("kom");
        assertEquals("kom", gameReview.getComment());
    }

}