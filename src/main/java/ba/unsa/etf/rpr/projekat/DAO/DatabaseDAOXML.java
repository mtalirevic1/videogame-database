package ba.unsa.etf.rpr.projekat.DAO;

import ba.unsa.etf.rpr.projekat.DTO.*;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseDAOXML implements DatabaseDAO {

    private ArrayList<VideoGame> videoGames = new ArrayList<>();
    private ArrayList<Developer> developers = new ArrayList<>();
    private ArrayList<UserAccount> userAccounts = new ArrayList<>();
    private ArrayList<AdminAccount> adminAccounts = new ArrayList<>();
    private ArrayList<GameReview> gameReviews = new ArrayList<>();

    public DatabaseDAOXML() {
        read();
    }

    private void read() {
        videoGames.clear();
        developers.clear();
        userAccounts.clear();
        adminAccounts.clear();
        gameReviews.clear();
        try {
            XMLDecoder decoder = new XMLDecoder(new FileInputStream("resources/xml/videoGames.xml"));
            videoGames = (ArrayList<VideoGame>) decoder.readObject();
            decoder.close();

            decoder = new XMLDecoder(new FileInputStream("resources/xml/developers.xml"));
            developers = (ArrayList<Developer>) decoder.readObject();
            decoder.close();

            decoder = new XMLDecoder(new FileInputStream("resources/xml/userAccounts.xml"));
            userAccounts = (ArrayList<UserAccount>) decoder.readObject();
            decoder.close();

            decoder = new XMLDecoder(new FileInputStream("resources/xml/adminAccounts.xml"));
            adminAccounts = (ArrayList<AdminAccount>) decoder.readObject();
            decoder.close();

            decoder = new XMLDecoder(new FileInputStream("resources/xml/gameReviews.xml"));
            gameReviews = (ArrayList<GameReview>) decoder.readObject();
            decoder.close();

        } catch (FileNotFoundException e) {
            System.out.println("Unable to read file");
        }
    }

    private void write() {
        try {
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream("resources/xml/videoGames.xml"));
            encoder.setPersistenceDelegate(LocalDate.class,
                    new PersistenceDelegate() {
                        @Override
                        protected Expression instantiate(Object localDate, Encoder encdr) {
                            return new Expression(localDate,
                                    LocalDate.class,
                                    "parse",
                                    new Object[]{localDate.toString()});
                        }
                    });
            encoder.writeObject(videoGames);
            encoder.close();
            encoder = new XMLEncoder(new FileOutputStream("resources/xml/developers.xml"));
            encoder.writeObject(developers);
            encoder.close();
            encoder = new XMLEncoder(new FileOutputStream("resources/xml/userAccounts.xml"));
            encoder.writeObject(userAccounts);
            encoder.close();
            encoder = new XMLEncoder(new FileOutputStream("resources/xml/adminAccounts.xml"));
            encoder.writeObject(adminAccounts);
            encoder.close();
            encoder = new XMLEncoder(new FileOutputStream("resources/xml/gameReviews.xml"));
            encoder.writeObject(gameReviews);
            encoder.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write file");
        }
    }

    @Override
    public ObservableList<VideoGame> getVideoGames() {
        return FXCollections.observableArrayList(videoGames);
    }

    @Override
    public void addVideoGame(VideoGame videoGame) {
        if (getDeveloperById(videoGame.getDeveloper().getId()) == null) {
            addDeveloper(videoGame.getDeveloper());
        }
        int maxId = 0;
        for (VideoGame v : videoGames)
            if (v.getId() > maxId) maxId = v.getId();
        videoGame.setId(maxId + 1);
        videoGames.add(videoGame);
        write();
    }

    @Override
    public void removeVideoGame(VideoGame videoGame) {
        for (int i = 0; i < videoGames.size(); i++)
            if (videoGames.get(i).getId().equals(videoGame.getId())) {
                videoGames.remove(i);
                break;
            }
        write();
    }

    @Override
    public void updateVideoGame(VideoGame videoGame) {
        for (int i = 0; i < videoGames.size(); i++)
            if (videoGames.get(i).getId().equals(videoGame.getId())) {
                videoGames.set(i, videoGame);
                break;
            }
        write();
    }

    @Override
    public VideoGame getVideoGameById(int id) {
        VideoGame videoGame = null;
        for (VideoGame v : videoGames) {
            if (v.getId().equals(id)) {
                videoGame = v;
            }
        }
        return videoGame;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByName(String name) {
        ObservableList<VideoGame> videoGames = getVideoGames();
        int size = videoGames.size();
        for (int i = 0; i < size; i++) {
            if (videoGames.get(i).getName().contains(name)) {
                videoGames.remove(i);
                i--;
                size--;
            }
        }
        return videoGames;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByGenre(String genre) {
        ObservableList<VideoGame> videoGames = getVideoGames();
        int size = videoGames.size();
        for (int i = 0; i < size; i++) {
            if (videoGames.get(i).getGenre().contains(genre)) {
                videoGames.remove(i);
                i--;
                size--;
            }
        }
        return videoGames;
    }

    @Override
    public ObservableList<VideoGame> getVideoGameByDeveloper(String developer) {
        ObservableList<VideoGame> videoGames = getVideoGames();
        int size = videoGames.size();
        for (int i = 0; i < size; i++) {
            if (videoGames.get(i).getDeveloper().getName().contains(developer)) {
                videoGames.remove(i);
                i--;
                size--;
            }
        }
        return videoGames;
    }

    @Override
    public ObservableList<Developer> getDevelopers() {
        return FXCollections.observableArrayList(developers);
    }

    @Override
    public void addDeveloper(Developer developer) {
        int maxId = 0;
        for (Developer d : developers)
            if (d.getId() > maxId) maxId = d.getId();
        developer.setId(maxId + 1);
        developers.add(developer);
        write();
    }

    @Override
    public void removeDeveloper(Developer developer) {
        for (int i = 0; i < developers.size(); i++)
            if (developers.get(i).getId().equals(developer.getId())) {
                developers.remove(i);
                break;
            }
        write();
    }

    @Override
    public void updateDeveloper(Developer developer) {
        for (int i = 0; i < developers.size(); i++)
            if (developers.get(i).getId().equals(developer.getId())) {
                developers.set(i, developer);
                break;
            }
        write();
    }

    @Override
    public Developer getDeveloperById(int id) {
        Developer developer = null;
        for (Developer d : developers) {
            if (d.getId().equals(id)) {
                developer = d;
                break;
            }
        }
        return developer;
    }

    @Override
    public ObservableList<Developer> getDeveloperByName(String name) {
        ObservableList<Developer> developers = getDevelopers();
        int size = developers.size();
        for (int i = 0; i < size; i++) {
            if (developers.get(i).getName().contains(name)) {
                developers.remove(i);
                i--;
                size--;
            }
        }
        return developers;
    }

    @Override
    public ObservableList<UserAccount> getUsers() {
        return FXCollections.observableArrayList(userAccounts);
    }

    @Override
    public void addUser(UserAccount userAccount) {
        int maxId = 0;
        for (UserAccount u : userAccounts)
            if (u.getId() > maxId) maxId = u.getId();
        userAccount.setId(maxId + 1);
        userAccounts.add(userAccount);
        write();
    }

    @Override
    public void removeUser(UserAccount userAccount) {
        for (int i = 0; i < userAccounts.size(); i++)
            if (userAccounts.get(i).getId().equals(userAccount.getId())) {
                userAccounts.remove(i);
                break;
            }
        write();
    }

    @Override
    public void updateUser(UserAccount userAccount) {
        for (int i = 0; i < userAccounts.size(); i++)
            if (userAccounts.get(i).getId().equals(userAccount.getId())) {
                userAccounts.set(i, userAccount);
                break;
            }
        write();
    }

    @Override
    public UserAccount getUserById(int id) {
        UserAccount userAccount = null;
        for (UserAccount ua : userAccounts) {
            if (ua.getId().equals(id)) {
                userAccount = ua;
            }
        }
        return userAccount;
    }

    @Override
    public ObservableList<AdminAccount> getAdmins() {
        return FXCollections.observableArrayList(adminAccounts);
    }

    @Override
    public void addAdmin(AdminAccount adminAccount) {
        int maxId = 0;
        for (AdminAccount a : adminAccounts)
            if (a.getId() > maxId) maxId = a.getId();
        adminAccount.setId(maxId + 1);
        adminAccounts.add(adminAccount);
        write();
    }

    @Override
    public void removeAdmin(AdminAccount adminAccount) {
        for (int i = 0; i < adminAccounts.size(); i++)
            if (adminAccounts.get(i).getId().equals(adminAccount.getId())) {
                adminAccounts.remove(i);
                break;
            }
        write();
    }

    @Override
    public void updateAdmin(AdminAccount adminAccount) {
        for (int i = 0; i < adminAccounts.size(); i++)
            if (adminAccounts.get(i).getId().equals(adminAccount.getId())) {
                adminAccounts.set(i, adminAccount);
                break;
            }
        write();
    }

    @Override
    public AdminAccount getAdminById(int id) {
        AdminAccount adminAccount = null;
        for (AdminAccount ua : adminAccounts) {
            if (ua.getId().equals(id)) {
                adminAccount = ua;
            }
        }
        return adminAccount;
    }

    @Override
    public Set<GameReview> getReviewsByGameId(int id) {
        Set<GameReview> gr = new TreeSet<>();
        gr.addAll(gameReviews);
        return gr;
    }

    @Override
    public GameReview getReviewByUserGame(VideoGame videoGame, UserAccount userAccount) {
        GameReview gr = null;
        int vgId1 = videoGame.getId(), uaId1 = userAccount.getId();
        for (GameReview gameReview : gameReviews) {
            int vgId2 = gameReview.getVideoGame().getId(), uaId2 = gameReview.getAccount().getId();
            if (vgId1 == vgId2 && uaId1 == uaId2) {
                gr = gameReview;
                break;
            }
        }
        return gr;
    }

    @Override
    public void addGameReview(GameReview gameReview) {
        gameReviews.add(gameReview);
        write();
    }

    @Override
    public void removeGameReview(GameReview gameReview) {
        for (int i = 0; i < gameReviews.size(); i++) {
            int vgId1 = gameReview.getVideoGame().getId(), vgId2 = gameReviews.get(i).getVideoGame().getId();
            int uId1 = gameReview.getAccount().getId(), uId2 = gameReviews.get(i).getAccount().getId();
            if (vgId1 == vgId2 && uId1 == uId2) {
                gameReviews.remove(i);
                break;
            }
        }
        write();
    }

    @Override
    public void updateGameReview(GameReview gameReview) {
        for (int i = 0; i < gameReviews.size(); i++) {
            int vgId1 = gameReview.getVideoGame().getId(), vgId2 = gameReviews.get(i).getVideoGame().getId();
            int uId1 = gameReview.getAccount().getId(), uId2 = gameReviews.get(i).getAccount().getId();
            if (vgId1 == vgId2 && uId1 == uId2) {
                gameReviews.set(i, gameReview);
                break;
            }
        }
        write();
    }

    @Override
    public void removeReviewsByGame(VideoGame videoGame) {
        for (int i = 0; i < gameReviews.size(); i++) {
            int vgId1 = videoGame.getId(), vgId2 = gameReviews.get(i).getVideoGame().getId();
            if (vgId1 == vgId2) {
                gameReviews.remove(i);
                break;
            }
        }
        write();
    }

    @Override
    public void removeReviewsByUser(UserAccount userAccount) {
        for (int i = 0; i < gameReviews.size(); i++) {
            int uId1 = userAccount.getId(), uId2 = gameReviews.get(i).getAccount().getId();
            if (uId1 == uId2) {
                gameReviews.remove(i);
                break;
            }
        }
        write();
    }

    @Override
    public void close() {

    }
}
