package dam.alumno.filmoteca;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javafx.scene.media.AudioClip;

public class PeliculaController{
    public Label textoTitulo;

    public TextField fieldTitle ;
    public TextField fieldYear;
    public TextField fieldGenre ;
    public TextArea fieldDescription;
    public TextField fieldDirector;
    public Text fieldVRat ;
    public Slider fieldRating;
    public TextField fieldURLPoster;
    public ImageView imagePoster;
    private DatosFilmoteca datosPelicula = DatosFilmoteca.getInstancia();
    private MainController mainController;
    private AudioClip sound = new AudioClip(getClass().getResource("/sounds/slideSound.mp3").toString());

    public void initialize() {

        //Añdimos un listerner en el textfield de busqueda
        //para buscar conforme escribimos en el
        fieldURLPoster.textProperty().addListener((observable, oldValue, newValue) -> {
            if (mainController.cargarImagen(imagePoster,newValue.trim())){
                fieldURLPoster.getStyleClass().removeAll("cuadroRojo");//Desactivamos cuadro rojo
            } else {
                fieldURLPoster.getStyleClass().add("cuadroRojo");//Activamos cuadro rojo
            }
        });

        //Aplicamos TextFormatter para restringir la entrada
        //del textField del año a un numero de 4 digitos como maximo
        UnaryOperator<TextFormatter.Change> filtro = change -> {
            String newText = change.getControlNewText();
            return (newText.matches("\\d{0,4}")) ? change : null; // Máximo 4 dígitos
        };
        fieldYear.setTextFormatter(new TextFormatter<>(filtro));

        // Aplicamos TextFormatter para impedir borrar los corchetes
        // en el campo de genero
        UnaryOperator<TextFormatter.Change> filtro2 = change -> {
            String newText = change.getControlNewText();
            if (!newText.startsWith("[") || !newText.endsWith("]")) {
                return null; // Evita borrar los corchetes
            }
            return change;
        };
        fieldGenre.setTextFormatter(new TextFormatter<>(filtro2));

        // Agregamos Listener para actualizar el valor del Text
        // y cargar sonido cuando el Slider cambie de valor
        fieldRating.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Mostramos valor con 1 decimal
            fieldVRat.setText(String.format("%.1f", newValue.doubleValue()));
            sound.play(); // Reproducir sonido

        });
    }

    //Metodo para impedir borrar los corchetes en
    //el textfield donde se definen los generos

    private String limpiarCorchetes(String texto) {
        if (texto.startsWith("[") && texto.endsWith("]")) {
            // Elimina el primer y último carácter
            return texto.substring(1, texto.length() - 1);
        }
        return texto;
    }

    //Asignamos el controlador principal a este controlador
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    //Metodo para presentar datos de una película en el formulario

    public void setPelicula (Pelicula pelicula) {
        if (pelicula != null) {
            // Cambiamos el título del formulario a "Editar" si existe perlicula
            textoTitulo.setText("Editar Pelicula");

            // Rellenamos los campos
            fieldTitle.setText(pelicula.getTitle());
            fieldYear.setText(pelicula.getYear().toString());
            fieldGenre.setText(String.valueOf(pelicula.getGenre()));
            fieldDescription.setText(pelicula.getDescription());
            fieldDirector.setText(pelicula.getDirector());
            fieldVRat.setText(String.valueOf(pelicula.getRating()));
            fieldRating.setValue(pelicula.getRating());
            fieldURLPoster.setText(pelicula.getPoster());
            // Cargamos la imagen en el imageview
            mainController.cargarImagen(imagePoster, pelicula.getPoster());

        } else {
            // Si no hay película, cambiamos el
            // título del formulario para agregar una nueva
            textoTitulo.setText("Añadir Pelicula");
        }
    }

    //Manejamos evento generado al presionar el botón ACEPTAR

    public void handlerAceptar(ActionEvent actionEvent) {
        Pelicula pelicula = mainController.getPeliculaSel();

        //el textfield del año debe tener al menos un digito
        if (fieldYear.getText().isEmpty()) {
            mainController.mostrarAlerta("Debes introducir al menos 1 numero en el campo Año");

        } else{
            // Si el formulario es para añadir una nueva película
            if (Objects.equals(textoTitulo.getText(), "Añadir Pelicula")) {
            pelicula = new Pelicula();
            pelicula.setId(generarNuevoId());// Generamos nuevo ID único para la película
            datosPelicula.getListaPeliculas().add(pelicula);// Agregamos la película a la lista
            }
            // Asignamos los valores de los campos al objeto Pelicula
            pelicula.setTitle(fieldTitle.getText());
            pelicula.setYear(Integer.parseInt(fieldYear.getText()));
            // Formateamos el género
            pelicula.setGenre(Collections.singletonList(limpiarCorchetes(fieldGenre.getText().toString())));
            pelicula.setDescription(fieldDescription.getText());
            pelicula.setDirector(fieldDirector.getText());
            // Redondeamos el rating a un decimal
            pelicula.setRating((float) (Math.round(fieldRating.getValue() * 10.0) / 10.0));
            pelicula.setPoster(fieldURLPoster.getText());
            cerrarVentana(actionEvent);
        }
    }

    //Manejamos evento generado al presionar el botón CANCELAR

    public void handlerCancelar(ActionEvent actionEvent) {
        cerrarVentana(actionEvent);
    }

    //Metodo para generar ID único para una nueva película

    private int generarNuevoId() {
        return datosPelicula.getListaPeliculas()
                .stream()
                .mapToInt(Pelicula::getId)
                .max()
                .orElse(0) + 1; //nuevo ID que es el mayor existente + 1
    }

    //Metodo para cerrar la ventana actual

    private void cerrarVentana(ActionEvent actionEvent) {
        // Obtenermos la ventana actual a partir del evento
        // y la cerramos
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


}
