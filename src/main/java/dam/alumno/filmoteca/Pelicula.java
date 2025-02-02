package dam.alumno.filmoteca;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pelicula {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final ListProperty<String> genre = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty director = new SimpleStringProperty();
    private final FloatProperty rating = new SimpleFloatProperty();
    private final StringProperty poster = new SimpleStringProperty();


    // Campo para deserialización
    private List<String> genreRaw;

    public Pelicula() {
    }

      public Pelicula(int id, String title, int year, List<String> genre,
                    String description, String director, float rating,
                    String poster) {
        this.id.set(id);
        this.title.set(title);
        this.year.set(year);
        this.genre.set((ObservableList<String>) genre);
        this.description.set(description);
        this.director.set(director);
        this.rating.set(rating);
        this.poster.set(poster);
    }


    @JsonProperty("genre")
    public void setGenreRaw(List<String> genreRaw) {
        this.genreRaw = genreRaw;
        this.genre.set(FXCollections.observableArrayList(genreRaw));
    }
    @JsonProperty("genre")
    public List<String> getGenreRaw() {
        return genre.get();
    }

    public ObservableList<String> getGenre() {
        return genre.get();}
    public ListProperty<String> genreProperty() {
        return genre;}
    public void setGenre(List<String> genre) {
        this.genre.set(FXCollections.observableArrayList(genre));
    }

    public String getDirector() {return director.get();}
    public StringProperty directorProperty() {return director;}
    public void setDirector(String director) {this.director.set(director);}

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }
    public StringProperty titleProperty() {
        return title;
    }
    public void setTitle(String title) {
        this.title.set(title);
    }

    public Integer getYear() {
        return year.get();
    }
    public IntegerProperty yearProperty() {
        return year;
    }
    public void setYear(int year) {
        this.year.set(year);
    }

    public String getDescription() {
        return description.get();
    }
    public StringProperty descriptionProperty() {
        return description;
    }
    public void setDescription(String description) {
        this.description.set(description);
    }

    public float getRating() {
        return rating.get();
    }
    public FloatProperty ratingProperty() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating.set( rating);
    }

    public String getPoster() {
        return poster.get();
    }
    public StringProperty posterProperty() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster.set(poster);
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", title=" + title +
                ", year=" + year +
                ", genre=" + genre +
                ", description=" + description +
                ", director=" + year +
                ", rating=" + rating +
                ", poster=" + poster +
                '}';
    }

}
