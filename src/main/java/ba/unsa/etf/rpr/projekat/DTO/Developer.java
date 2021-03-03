package ba.unsa.etf.rpr.projekat.DTO;

import java.util.Objects;

public class Developer {
    private Integer id;
    private String name;
    private String description;
    private String iconLink;

    public Developer(Integer id, String name, String description, String iconLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconLink = iconLink;
    }

    public Developer() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Developer)) return false;
        Developer developer = (Developer) o;
        return getName().equals(developer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getIconLink());
    }
}
