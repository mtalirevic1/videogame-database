package ba.unsa.etf.rpr.projekat.DTO;

import java.time.LocalDate;
import java.util.Objects;

public class VideoGame {

    private Integer id;
    private Developer developer;
    private String genre;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String imageLink;

    public VideoGame(Integer id, String name, String genre, Developer developer, String description, LocalDate releaseDate, String imageLink) {
        this.id = id;
        this.genre = genre;
        this.developer = developer;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.imageLink = imageLink;
    }

    public VideoGame() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoGame)) return false;
        VideoGame videoGame = (VideoGame) o;
        return getName().equals(videoGame.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDeveloper(), getGenre(), getName(), getDescription(), getReleaseDate(), getImageLink());
    }
}
