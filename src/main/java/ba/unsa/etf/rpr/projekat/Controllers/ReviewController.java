package ba.unsa.etf.rpr.projekat.Controllers;

import ba.unsa.etf.rpr.projekat.DTO.GameReview;
import ba.unsa.etf.rpr.projekat.DTO.UserAccount;
import ba.unsa.etf.rpr.projekat.DTO.VideoGame;
import ba.unsa.etf.rpr.projekat.Interfaces.DatabaseDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReviewController extends Controller {

    public Slider scoreSlider;
    public TextField commentField;
    private ObservableList<GameReview> gameReviews;
    private GameReview accountReview;
    private DatabaseDAO dao;
    private VideoGame videoGame;
    private UserAccount userAccount;
    private GameViewController gameViewController;

    public ReviewController(ObservableList<GameReview> gameReviews, GameReview accountReview, DatabaseDAO dao, GameViewController gameViewController) {
        this.gameReviews = gameReviews;
        this.accountReview = accountReview;
        this.dao = dao;
        this.gameViewController = gameViewController;
    }

    public ReviewController(ObservableList<GameReview> gameReviews, VideoGame videoGame, UserAccount userAccount, DatabaseDAO dao, GameViewController gameViewController) {
        this.gameReviews = gameReviews;
        this.videoGame = videoGame;
        this.userAccount = userAccount;
        this.dao = dao;
        this.gameViewController = gameViewController;
        this.accountReview = null;
    }

    @FXML
    public void initialize() {
        if (accountReview != null) {
            scoreSlider.adjustValue(accountReview.getScore());
            commentField.setText(accountReview.getComment());
        }
    }

    public void postClick(ActionEvent actionEvent) {
        double score = scoreSlider.getValue();
        GameReview review = new GameReview(videoGame, userAccount, (int) score, commentField.getText());
        dao.addGameReview(review);
        gameReviews.add(review);
        gameViewController.setButtonUpdate();
        gameViewController.setAccountReview(review);
        gameViewController.setAverageScore();
        close();
    }

    public void updateClick(ActionEvent actionEvent) {
        for (int i = 0; i < gameReviews.size(); i++) {
            if (gameReviews.get(i).equals(accountReview)) {
                GameReview gameReview = gameReviews.get(i);
                gameReviews.remove(i);
                gameReview.setComment(commentField.getText());
                double score = scoreSlider.getValue();
                gameReview.setScore((int) score);
                gameReviews.add(i, gameReview);
                dao.updateGameReview(gameReviews.get(i));
                gameViewController.setAccountReview(gameReviews.get(i));
                gameViewController.setAverageScore();
                break;
            }
            i++;
        }
        close();
    }

    public void deleteClick(ActionEvent actionEvent) {
        for (int i = 0; i < gameReviews.size(); i++) {
            if (accountReview.equals(gameReviews.get(i))) {
                dao.removeGameReview(gameReviews.get(i));
                gameReviews.remove(i);
                gameViewController.setButtonWrite();
                gameViewController.setAccountReview(null);
                gameViewController.setAverageScore();
                break;
            }
            i++;
        }
        close();

    }

    public void cancelClick(ActionEvent actionEvent) {
        close();
    }

    public void close() {
        Stage stage = (Stage) commentField.getScene().getWindow();
        stage.close();
    }
}
