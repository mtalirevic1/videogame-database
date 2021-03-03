package ba.unsa.etf.rpr.projekat.Interfaces;

import ba.unsa.etf.rpr.projekat.DTO.*;
import javafx.collections.ObservableList;

import java.util.Set;

public interface DatabaseDAO {
    ObservableList<VideoGame> getVideoGames();

    void addVideoGame(VideoGame videoGame);

    void removeVideoGame(VideoGame videoGame);

    void updateVideoGame(VideoGame videoGame);

    VideoGame getVideoGameById(int id);

    ObservableList<VideoGame> getVideoGameByName(String name);

    ObservableList<VideoGame> getVideoGameByGenre(String genre);

    ObservableList<VideoGame> getVideoGameByDeveloper(String developer);

    ObservableList<Developer> getDevelopers();

    void addDeveloper(Developer developer);

    void removeDeveloper(Developer developer);

    void updateDeveloper(Developer developer);

    Developer getDeveloperById(int id);

    ObservableList<Developer> getDeveloperByName(String name);

    ObservableList<UserAccount> getUsers();

    void addUser(UserAccount userAccount);

    void removeUser(UserAccount userAccount);

    void updateUser(UserAccount userAccount);

    UserAccount getUserById(int id);

    ObservableList<AdminAccount> getAdmins();

    void addAdmin(AdminAccount adminAccount);

    void removeAdmin(AdminAccount adminAccount);

    void updateAdmin(AdminAccount adminAccount);

    AdminAccount getAdminById(int id);

    Set<GameReview> getReviewsByGameId(int id);

    GameReview getReviewByUserGame(VideoGame videoGame, UserAccount userAccount);

    void addGameReview(GameReview gameReview);

    void removeGameReview(GameReview gameReview);

    void updateGameReview(GameReview gameReview);

    void removeReviewsByGame(VideoGame videoGame);

    void removeReviewsByUser(UserAccount userAccount);

    void close();
}
