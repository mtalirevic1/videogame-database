package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.DTO.*;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseDAODB implements DatabaseDAO {

    private Connection conn;

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    private PreparedStatement getVideoGamesQuery, addVideoGameQuery, removeVideoGameQuery, updateVideoGameQuery,
            getVideoGameByIdQuery, getVideoGameByNameQuery, getVideoGameByDeveloperQuery, getVideoGameByGenreQuery,
            getDevelopersQuery, addDeveloperQuery, removeDeveloperQuery, updateDeveloperQuery, getDeveloperByIdQuery,
            getUsersQuery, addUserQuery, removeUserQuery, updateUserQuery, getUserByIdQuery,
            getAdminsQuery, addAdminQuery, removeAdminQuery, updateAdminQuery, getAdminByIdQuery,
            getNewVideoGameIdQuery, getNewAdminIdQuery, getNewUserIdQuery, getNewDeveloperIdQuery, getDeveloperByNameQuery,
            getReviewsByGameIdQuery, addGameReviewQuery, removeGameReviewQuery, updateGameReviewQuery, getReviewByUserGameQuery,
            removeReviewsByGameQuery, removeReviewsByUserQuery;

    public DatabaseDAODB() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:resources/db/database.db");
            Scanner input;
            try {
                input = new Scanner(new FileInputStream("resources/db/database.sql"));
                String sqlQuery = "";
                while (input.hasNext()) {
                    sqlQuery += input.nextLine();
                    if (sqlQuery.length() > 1 && sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                        try {
                            Statement stmt = conn.createStatement();
                            stmt.execute(sqlQuery);
                            sqlQuery = "";
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                }
                input.close();
            } catch (FileNotFoundException e) {
                System.out.println("SQL file does not exist");
            }

            getVideoGamesQuery = conn.prepareStatement("SELECT * FROM video_game");
            getDevelopersQuery = conn.prepareStatement("SELECT * FROM developer");
            getUsersQuery = conn.prepareStatement("SELECT * FROM user_account");
            getAdminsQuery = conn.prepareStatement("SELECT * FROM admin_account");
            getReviewsByGameIdQuery = conn.prepareStatement("SELECT * FROM game_review WHERE game_id=?");

            getVideoGameByIdQuery = conn.prepareStatement("SELECT * FROM video_game WHERE id=?");
            getDeveloperByIdQuery = conn.prepareStatement("SELECT * FROM developer WHERE id=?");
            getUserByIdQuery = conn.prepareStatement("SELECT * FROM user_account WHERE id=?");
            getAdminByIdQuery = conn.prepareStatement("SELECT * FROM admin_account WHERE id=?");

            getNewVideoGameIdQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM video_game");
            getNewDeveloperIdQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM developer");
            getNewUserIdQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM user_account");
            getNewAdminIdQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM admin_account");
            getNewAdminIdQuery = conn.prepareStatement("SELECT MAX(id)+1 FROM admin_account");

            addVideoGameQuery = conn.prepareStatement("INSERT INTO video_game VALUES(?,?,?,?,?,?,?)");
            addDeveloperQuery = conn.prepareStatement("INSERT INTO developer VALUES(?,?,?,?)");
            addUserQuery = conn.prepareStatement("INSERT INTO user_account VALUES(?,?,?,?)");
            addAdminQuery = conn.prepareStatement("INSERT INTO admin_account VALUES(?,?,?)");
            addGameReviewQuery = conn.prepareStatement("INSERT INTO game_review VALUES(?,?,?,?)");

            removeVideoGameQuery = conn.prepareStatement("DELETE FROM video_game WHERE id=?");
            removeDeveloperQuery = conn.prepareStatement("DELETE FROM developer WHERE id=?");
            removeUserQuery = conn.prepareStatement("DELETE FROM user_account WHERE id=?");
            removeAdminQuery = conn.prepareStatement("DELETE FROM admin_account WHERE id=?");
            removeGameReviewQuery = conn.prepareStatement("DELETE FROM game_review WHERE game_id=? AND user_id=?");
            removeReviewsByGameQuery = conn.prepareStatement("DELETE FROM game_review WHERE game_id=?");
            removeReviewsByUserQuery = conn.prepareStatement("DELETE FROM game_review WHERE user_id=?");

            updateVideoGameQuery = conn.prepareStatement("UPDATE video_game SET name=?, genre=?, dev_id=?, description=?, release_date=?, image_link=? WHERE id=?");
            updateDeveloperQuery = conn.prepareStatement("UPDATE developer SET name=?, description=?, icon=? WHERE id=?");
            updateUserQuery = conn.prepareStatement("UPDATE user_account SET username=?, password=?, avatar=? WHERE id=?");
            updateAdminQuery = conn.prepareStatement("UPDATE admin_account SET username=?, password=? WHERE id=?");
            updateGameReviewQuery = conn.prepareStatement("UPDATE game_review SET score=?, comment=? WHERE game_id=? AND user_id=?");

            getVideoGameByDeveloperQuery = conn.prepareStatement("SELECT * FROM video_game, developer WHERE video_game.id=developer.id AND developer.name LIKE ?");
            getVideoGameByNameQuery = conn.prepareStatement("SELECT * FROM video_game WHERE name LIKE ?");
            getVideoGameByGenreQuery = conn.prepareStatement("SELECT * FROM video_game WHERE genre LIKE ?");
            getDeveloperByNameQuery = conn.prepareStatement("SELECT * FROM developer WHERE name LIKE ?");
            getReviewByUserGameQuery = conn.prepareStatement("SELECT * FROM game_review WHERE game_id=? AND user_id=?");

        } catch (SQLException e) {
            System.out.println("Failed to prepare statement");
        }
    }

    @Override
    public ObservableList<VideoGame> getVideoGames() {
        ObservableList<VideoGame> videoGames = FXCollections.observableArrayList();
        try {
            ResultSet rs = getVideoGamesQuery.executeQuery();
            while (rs.next()) {
                VideoGame videoGame = new VideoGame(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getDeveloperById(rs.getInt(4)),
                        rs.getString(5),
                        rs.getDate(6).toLocalDate(),
                        rs.getString(7));
                videoGames.add(videoGame);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return videoGames;
    }

    @Override
    public void addVideoGame(VideoGame videoGame) {
        try {
            if (getDeveloperById(videoGame.getDeveloper().getId()) == null) {
                addDeveloper(videoGame.getDeveloper());
            }
            ResultSet rs = getNewVideoGameIdQuery.executeQuery();
            int id = 0;
            if (rs.next()) id = rs.getInt(1);
            videoGame.setId(id);
            addVideoGameQuery.setInt(1, videoGame.getId());
            addVideoGameQuery.setString(2, videoGame.getName());
            addVideoGameQuery.setString(3, videoGame.getGenre());
            addVideoGameQuery.setInt(4, videoGame.getDeveloper().getId());
            addVideoGameQuery.setString(5, videoGame.getDescription());
            addVideoGameQuery.setDate(6, Date.valueOf(videoGame.getReleaseDate()));
            addVideoGameQuery.setString(7, videoGame.getImageLink());
            addVideoGameQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }

    }

    @Override
    public void removeVideoGame(VideoGame videoGame) {
        try {
            removeVideoGameQuery.setInt(1, videoGame.getId());
            removeVideoGameQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void updateVideoGame(VideoGame videoGame) {
        try {
            updateVideoGameQuery.setInt(7, videoGame.getId());
            updateVideoGameQuery.setString(1, videoGame.getName());
            updateVideoGameQuery.setString(2, videoGame.getGenre());
            updateVideoGameQuery.setInt(3, videoGame.getDeveloper().getId());
            updateVideoGameQuery.setString(4, videoGame.getDescription());
            updateVideoGameQuery.setDate(5, Date.valueOf(videoGame.getReleaseDate()));
            updateVideoGameQuery.setString(6, videoGame.getImageLink());
            updateVideoGameQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public VideoGame getVideoGameById(int id) {
        VideoGame videoGame = null;
        try {
            getVideoGameByIdQuery.setInt(1, id);
            ResultSet rs = getVideoGameByIdQuery.executeQuery();
            while (rs.next()) {
                videoGame = new VideoGame(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getDeveloperById(rs.getInt(4)),
                        rs.getString(5),
                        rs.getDate(6).toLocalDate(),
                        rs.getString(7));
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return videoGame;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByName(String name) {
        ObservableList<VideoGame> videoGames = FXCollections.observableArrayList();
        try {
            getVideoGameByNameQuery.setString(1, name);
            ResultSet rs = getVideoGameByNameQuery.executeQuery();
            while (rs.next()) {
                VideoGame videoGame = new VideoGame(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getDeveloperById(rs.getInt(4)),
                        rs.getString(5),
                        rs.getDate(6).toLocalDate(),
                        rs.getString(7));
                videoGames.add(videoGame);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return videoGames;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByGenre(String genre) {
        ObservableList<VideoGame> videoGames = FXCollections.observableArrayList();
        try {
            getVideoGameByGenreQuery.setString(1, genre);
            ResultSet rs = getVideoGameByGenreQuery.executeQuery();
            while (rs.next()) {
                VideoGame videoGame = new VideoGame(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getDeveloperById(rs.getInt(4)),
                        rs.getString(5),
                        rs.getDate(6).toLocalDate(),
                        rs.getString(7));
                videoGames.add(videoGame);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return videoGames;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByDeveloper(String developer) {
        ObservableList<VideoGame> videoGames = FXCollections.observableArrayList();
        try {
            getVideoGameByDeveloperQuery.setString(1, developer);
            ResultSet rs = getVideoGameByDeveloperQuery.executeQuery();
            while (rs.next()) {
                VideoGame videoGame = new VideoGame(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        getDeveloperById(rs.getInt(4)),
                        rs.getString(5),
                        rs.getDate(6).toLocalDate(),
                        rs.getString(7));
                videoGames.add(videoGame);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return videoGames;
    }

    @Override
    public ObservableList<Developer> getDevelopers() {
        ObservableList<Developer> developers = FXCollections.observableArrayList();
        try {
            ResultSet rs = getDevelopersQuery.executeQuery();
            while (rs.next()) {
                Developer developer = new Developer(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
                developers.add(developer);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return developers;
    }

    @Override
    public void addDeveloper(Developer developer) {
        try {
            ResultSet rs = getNewDeveloperIdQuery.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            developer.setId(id);
            addDeveloperQuery.setInt(1, developer.getId());
            addDeveloperQuery.setString(2, developer.getName());
            addDeveloperQuery.setString(3, developer.getDescription());
            addDeveloperQuery.setString(4, developer.getIconLink());
            addDeveloperQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeDeveloper(Developer developer) {
        try {
            removeDeveloperQuery.setInt(1, developer.getId());
            removeDeveloperQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void updateDeveloper(Developer developer) {
        try {
            updateDeveloperQuery.setInt(4, developer.getId());
            updateDeveloperQuery.setString(1, developer.getName());
            updateDeveloperQuery.setString(2, developer.getDescription());
            updateDeveloperQuery.setString(3, developer.getIconLink());
            updateDeveloperQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public Developer getDeveloperById(int id) {
        Developer developer = null;
        try {
            getDeveloperByIdQuery.setInt(1, id);
            ResultSet rs = getDeveloperByIdQuery.executeQuery();
            while (rs.next()) {
                developer = new Developer(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return developer;
    }

    @Override
    public ObservableList<Developer> getDeveloperByName(String name) {
        ObservableList<Developer> developers = FXCollections.observableArrayList();
        try {
            getDeveloperByNameQuery.setString(1, name);
            ResultSet rs = getDeveloperByNameQuery.executeQuery();
            while (rs.next()) {
                Developer developer = new Developer(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
                developers.add(developer);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return developers;
    }

    @Override
    public ObservableList<UserAccount> getUsers() {
        ObservableList<UserAccount> users = FXCollections.observableArrayList();
        try {
            ResultSet rs = getUsersQuery.executeQuery();
            while (rs.next()) {
                UserAccount userAccount = new UserAccount(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
                users.add(userAccount);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return users;
    }

    @Override
    public void addUser(UserAccount userAccount) {
        try {
            ResultSet rs = getNewUserIdQuery.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            userAccount.setId(id);
            addUserQuery.setInt(1, userAccount.getId());
            addUserQuery.setString(2, userAccount.getUsername());
            addUserQuery.setString(3, userAccount.getPassword());
            addUserQuery.setString(4, userAccount.getAvatarLink());
            addUserQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeUser(UserAccount userAccount) {
        try {
            removeUserQuery.setInt(1, userAccount.getId());
            removeUserQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void updateUser(UserAccount userAccount) {
        try {
            updateUserQuery.setInt(4, userAccount.getId());
            updateUserQuery.setString(1, userAccount.getUsername());
            updateUserQuery.setString(2, userAccount.getPassword());
            updateUserQuery.setString(3, userAccount.getAvatarLink());
            updateUserQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public UserAccount getUserById(int id) {
        UserAccount userAccount = null;
        try {
            getUserByIdQuery.setInt(1, id);
            ResultSet rs = getUserByIdQuery.executeQuery();
            while (rs.next()) {
                userAccount = new UserAccount(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return userAccount;
    }

    @Override
    public ObservableList<AdminAccount> getAdmins() {
        ObservableList<AdminAccount> admins = FXCollections.observableArrayList();
        try {
            ResultSet rs = getAdminsQuery.executeQuery();
            while (rs.next()) {
                AdminAccount adminAccount = new AdminAccount(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
                admins.add(adminAccount);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return admins;
    }

    @Override
    public void addAdmin(AdminAccount adminAccount) {
        try {
            ResultSet rs = getNewAdminIdQuery.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            adminAccount.setId(id);
            addAdminQuery.setInt(1, adminAccount.getId());
            addAdminQuery.setString(2, adminAccount.getUsername());
            addAdminQuery.setString(3, adminAccount.getPassword());
            addAdminQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeAdmin(AdminAccount adminAccount) {
        try {
            removeAdminQuery.setInt(1, adminAccount.getId());
            removeAdminQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void updateAdmin(AdminAccount adminAccount) {
        try {
            updateAdminQuery.setInt(3, adminAccount.getId());
            updateAdminQuery.setString(1, adminAccount.getUsername());
            updateAdminQuery.setString(2, adminAccount.getPassword());
            updateAdminQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public AdminAccount getAdminById(int id) {
        AdminAccount adminAccount = null;
        try {
            getAdminByIdQuery.setInt(1, id);
            ResultSet rs = getAdminByIdQuery.executeQuery();
            while (rs.next()) {
                adminAccount = new AdminAccount(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return adminAccount;
    }

    @Override
    public Set<GameReview> getReviewsByGameId(int id) {
        Set<GameReview> gameReviews = new TreeSet<>();
        try {
            getReviewsByGameIdQuery.setInt(1, id);
            ResultSet rs = getReviewsByGameIdQuery.executeQuery();
            while (rs.next()) {
                VideoGame videoGame = getVideoGameById(rs.getInt(1));
                UserAccount userAccount = getUserById(rs.getInt(2));
                GameReview gameReview = new GameReview(videoGame,
                        userAccount,
                        rs.getInt(3),
                        rs.getString(4));
                gameReviews.add(gameReview);
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return gameReviews;
    }

    @Override
    public GameReview getReviewByUserGame(VideoGame videoGame, UserAccount userAccount) {
        GameReview gameReview = null;
        try {
            getReviewByUserGameQuery.setInt(1, videoGame.getId());
            getReviewByUserGameQuery.setInt(2, userAccount.getId());
            ResultSet rs = getReviewByUserGameQuery.executeQuery();
            while (rs.next()) {
                gameReview = new GameReview(
                        getVideoGameById(rs.getInt(1)),
                        getUserById(rs.getInt(2)),
                        rs.getInt(3),
                        rs.getString(4)
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
        return gameReview;
    }

    @Override
    public void addGameReview(GameReview gameReview) {
        try {
            addGameReviewQuery.setInt(1, gameReview.getVideoGame().getId());
            addGameReviewQuery.setInt(2, gameReview.getAccount().getId());
            addGameReviewQuery.setInt(3, gameReview.getScore());
            addGameReviewQuery.setString(4, gameReview.getComment());
            addGameReviewQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeGameReview(GameReview gameReview) {
        try {
            removeGameReviewQuery.setInt(1, gameReview.getVideoGame().getId());
            removeGameReviewQuery.setInt(2, gameReview.getAccount().getId());
            removeGameReviewQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void updateGameReview(GameReview gameReview) {
        try {
            updateGameReviewQuery.setInt(3, gameReview.getVideoGame().getId());
            updateGameReviewQuery.setInt(4, gameReview.getAccount().getId());
            updateGameReviewQuery.setInt(1, gameReview.getScore());
            updateGameReviewQuery.setString(2, gameReview.getComment());
            updateGameReviewQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeReviewsByGame(VideoGame videoGame) {
        try {
            removeGameReviewQuery.setInt(1, videoGame.getId());
            removeGameReviewQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }

    @Override
    public void removeReviewsByUser(UserAccount userAccount) {
        try {
            removeGameReviewQuery.setInt(1, userAccount.getId());
            removeGameReviewQuery.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while executing query");
        }
    }


    @Override
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
