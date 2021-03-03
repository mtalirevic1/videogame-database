package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.GameReview;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.DTO.VideoGame;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import ba.unsa.etf.rpr.projekat.UIControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;

public class GameViewController extends Controller {

    public Label gameName;
    public Label gameDeveloper;
    public Label gameGenre;
    public Label gameReleaseDate;
    public Label gameDescription;
    public Label scoreLabel, scoreNumberLabel;
    public ImageView gameImage;
    public Button reviewButton;
    public ImageView reviewButtonImageView = new ImageView();
    public ListView<GameReview> reviewListView;
    private VideoGame selectedGame;
    private UserAccount accountInUse;
    private DatabaseDAO dao;
    private GameReview accountReview = null;
    private ObservableList<GameReview> gameReviews = FXCollections.observableArrayList();
    private ResourceBundle bundle = ResourceBundle.getBundle("Language");

    public GameViewController(VideoGame videoGame, DatabaseDAO dao, UserAccount accountInUse) {
        this.selectedGame = videoGame;
        this.dao = dao;
        this.accountInUse = accountInUse;
    }

    public GameReview getAccountReview() {
        return accountReview;
    }

    public void setAccountReview(GameReview accountReview) {
        this.accountReview = accountReview;
    }

    @FXML
    public void initialize() {
        gameName.setText(selectedGame.getName());
        gameGenre.setText(selectedGame.getGenre());
        gameDeveloper.setText(selectedGame.getDeveloper().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        gameReleaseDate.setText(selectedGame.getReleaseDate().format(formatter));
        gameDescription.setText(selectedGame.getDescription());
        UIControl.loadImage(gameImage, selectedGame.getImageLink(), 250, 250);
        accountReview = dao.getReviewByUserGame(selectedGame, accountInUse);
        reviewButtonImageView.setFitHeight(16);
        reviewButtonImageView.setFitWidth(16);
        reviewButton.setGraphic(reviewButtonImageView);
        if (accountReview != null) {
            setButtonUpdate();
        } else {
            setButtonWrite();
        }
        Set<GameReview> gameReviewSet = dao.getReviewsByGameId(selectedGame.getId());
        gameReviews.addAll(gameReviewSet);
        reviewListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(GameReview item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    UIControl.loadImage(imageView, item.getAccount().getAvatarLink(), 50, 50);
                    setGraphic(imageView);
                    setText(bundle.getString("userColon") + " " + item.getAccount().getUsername() + " "
                            + bundle.getString("scoreColon") + " " + item.getScore() + "/10\n"
                            + bundle.getString("commentColon") + " " + item.getComment());
                    setFont(Font.font(14));
                }
            }
        });
        reviewListView.setItems(gameReviews);
        setAverageScore();
    }

    public void setButtonUpdate() {
        reviewButton.setText(bundle.getString("updateReview"));
        try {
            reviewButtonImageView.setImage(new Image(new FileInputStream("resources/img/update.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reviewButton.setOnMouseClicked(event -> {
            updateReview();
        });
    }

    public void setButtonWrite() {
        reviewButton.setText(bundle.getString("writeReview"));
        try {
            reviewButtonImageView.setImage(new Image(new FileInputStream("resources/img/write.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reviewButton.setOnMouseClicked(event -> {
            writeReview();
        });
    }

    public void setAverageScore() {
        if (gameReviews.size() == 0) {
            scoreLabel.setText(bundle.getString("avgScoreColon"));
            scoreNumberLabel.setText(bundle.getString("noScore"));
            return;
        }
        Double sum = 0.;
        for (GameReview gameReview : gameReviews) {
            sum += gameReview.getScore();
        }
        sum /= gameReviews.size();
        scoreLabel.setText(bundle.getString("avgScoreColon"));
        scoreNumberLabel.setText((Math.round(sum * 10) / 10.0) + "");
    }

    private void updateReview() {
        UIControl.openWindow(getClass(), new ReviewController(gameReviews, accountReview, dao, this), bundle, "updateReview.fxml", bundle.getString("updateReview"));
    }

    private void writeReview() {
        UIControl.openWindow(getClass(), new ReviewController(gameReviews, selectedGame, accountInUse, dao, this), bundle, "writeReview.fxml", bundle.getString("writeReview"));
    }
}
