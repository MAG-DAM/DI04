package dam.alumno.filmoteca;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class MainController {
    @FXML
    private AnchorPane anchorPaneIV;
    @FXML
    private AnchorPane anchorPaneGP;
    @FXML
    private SplitPane divisionPane;
    @FXML
    private HBox hbox;

    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas;
    @FXML
    private TableView<Pelicula> tablaPeliculas;
    @FXML
    private TableColumn<Pelicula, Integer> columnaId;
    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;
    @FXML
    private TableColumn<Pelicula, Integer> columnaAño;
    @FXML
    private TableColumn<Pelicula, String> columnaGenero;
    @FXML
    private TableColumn<Pelicula, String> columnaDescripcion;
    @FXML
    private TableColumn<Pelicula, String> columnaDirector;
    @FXML
    private TableColumn<Pelicula, Float> columnaRating;
    @FXML
    private TableColumn<Pelicula, String> columnaPoster;
    @FXML
    private Text textoTitulo;
    @FXML
    private Text textoAño;
    @FXML
    private Text textoGenero;
    @FXML
    private TextArea textoDescripcion;
    @FXML
    private Text textoDirector;
    @FXML
    private Text VRat;
    @FXML
    private TextField textoURL;
    @FXML
    private Slider rating;
    @FXML
    private ImageView imageview;
    @FXML
    private TextField fieldBuscar;

    private AudioClip loadSound = new AudioClip(getClass().getResource("/sounds/loadImage.mp3").toString());

    public void initialize() {

        // Permitimos que el TextField de busqueda se expanda dentro del HBox
        hbox.setHgrow(fieldBuscar, Priority.ALWAYS);

        //Añdimos un listerner en el textfield de busqueda
        //para buscar a la misma que escribimos en él
        fieldBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (buscarPelicula(newValue.trim())){
                //Si la busqueda tiene exito desactivamos el cuadro rojo
                fieldBuscar.getStyleClass().removeAll("cuadroRojo");
            } else {
                //Si no encontramos una pelicula con el titulo activamos el cuadro rojo
                fieldBuscar.getStyleClass().add("cuadroRojo");
            }
        });

        // Listeners para actualizar tamaño de la imagen
        // cuando el AnchorPane cambia de tamaño
        anchorPaneIV.widthProperty().addListener((obs, oldVal, newVal) -> ajustarTamaño());
        anchorPaneIV.heightProperty().addListener((obs, oldVal, newVal) -> ajustarTamaño());

        // Forzamos a que el divisor del SplitPane
        // mantenga siempre un ratio 0.65
        Platform.runLater(() -> {
            divisionPane.getDividers().get(0).positionProperty().addListener((obs, oldVal, newVal) -> {
                divisionPane.setDividerPositions(0.65);
            });
        });

        // Obtenemos lista de películas
        listaPeliculas = datosFilmoteca.getListaPeliculas();
        configurarColumnas();// Configuramos las columnas de la tabla
        tablaPeliculas.setItems(listaPeliculas);

        // Listener para actualizar los detalles
        // de la película seleccionada en el tableView
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
               actualizarPelicula(newValue);
            }
        });
        //Seleccionamos el primer elemento
        // de la tabla, si  existen peliculas
        if (tablaPeliculas.getItems().size() > 0) {
            tablaPeliculas.getSelectionModel().select(0);
        }
    }

    // Metodo para ajustat el tamaño de la imagen
    // al tamaño del contenedor AnchorPane
    private void ajustarTamaño() {
       imageview.setFitWidth(anchorPaneIV.getWidth());
      imageview.setFitHeight(anchorPaneIV.getHeight());
    }

    // Metodo para Configurar los valores
    // de las columnas de la tabla de películas
    private void configurarColumnas() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnaAño.setCellValueFactory(new PropertyValueFactory<>("year"));
        columnaGenero.setCellValueFactory(new PropertyValueFactory<>("genre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnaDirector.setCellValueFactory(new PropertyValueFactory<>("director"));
        columnaRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        columnaPoster.setCellValueFactory(new PropertyValueFactory<>("poster"));
    }

    // Metodo para actualizar los detalles
    // de la película seleccionada
    private void actualizarPelicula(Pelicula pelicula) {
        textoTitulo.setText(pelicula.getTitle());
        textoAño.setText(pelicula.getYear().toString());
        textoGenero.setText(pelicula.getGenre().toString());
        textoDescripcion.setText(pelicula.getDescription());
        textoDirector.setText(pelicula.getDirector());
        rating.setValue(pelicula.getRating());
        VRat.setText(String.valueOf(pelicula.getRating()));
        textoURL.setText(pelicula.getPoster());
        // Cargamos imagen de la película
        cargarImagen(imageview, pelicula.getPoster());
        // Reproducimos sonido
        loadSound.play();

    }

    // Metodo para obtener película
    // seleccionada en la tabla
    public Pelicula getPeliculaSel() {
        return tablaPeliculas.getSelectionModel().getSelectedItem();
    }

    // Métodos para manejar los eventos
    // de los botones y elementos de la barra de menu


    @FXML
    private void handlerNueva(ActionEvent actionEvent) {
        abrirFormulario("Añadir Pelicula", null);
        tablaPeliculas.getSelectionModel().selectLast();

    }

    @FXML
    private void handlerMenuAñadir(ActionEvent actionEvent) {
        abrirFormulario("Añadir Pelicula", null);
        tablaPeliculas.getSelectionModel().selectLast();
    }

    @FXML
    private void handlerEditar(ActionEvent actionEvent) {
        editarPelicula();
    }

    @FXML
    private void handlerMenuEditar(ActionEvent actionEvent) {
        editarPelicula();
    }

    @FXML
    private void handlerEliminar(ActionEvent actionEvent) {
        eliminarPelicula();
    }

    @FXML
    private void handlerMenuEliminar(ActionEvent actionEvent) {
        eliminarPelicula();
    }

    @FXML
    private void handlerGuardar(ActionEvent actionEvent) {
        guardar();
    }

    @FXML
    private void handlerSalir(ActionEvent actionEvent) {
        if (mostrarConfirmacion("¿Seguro que quieres cerrar la aplicación?")) {
            System.exit(0);
        }
    }

    //Metodo para la lanzar la modificacion
    // de una pelicula

    private void editarPelicula(){
        Pelicula peliculaSeleccionada = getPeliculaSel();
        if (peliculaSeleccionada != null) {
            abrirFormulario("Editar Pelicula", peliculaSeleccionada);
            actualizarPelicula(peliculaSeleccionada);
        } else {
            mostrarAlerta("Debe seleccionar una pelicula");
        }
    }

    // Método para abrir el formulario de edición
    // o nueva creación de una película

    private void abrirFormulario(String titulo, Pelicula pelicula) {
        try {
            // Cargamos archivo FXML que define la interfaz del formulario
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("PeliculaView.fxml"));
            // Creamos nueva escena con el diseño cargado
            Scene escena = new Scene(loader.load(),860,530);
            // Obtenemos el controlador asociado a la vista cargada
            PeliculaController controlador = loader.getController();
            // Asignamos la instancia del controlador principal al formulario
            controlador.setMainController(this);
            // Pasamos la película seleccionada al controlador del formulario
            controlador.setPelicula(pelicula);
            // Crear nueva ventana (Stage) y asignamos titulo y escena
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(escena);

            // Configuramos como ventana modal
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);  // Deshabilitamos la opción de redimensionar
            stage.centerOnScreen();// Centramos la ventana en la pantalla

            stage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error al abrir el formulario: " + titulo);
            e.printStackTrace();
        }
    }

    // Metodo para eliminar la película seleccionada

    private void eliminarPelicula () {
        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();
        if (indice>=0) {
            if (mostrarConfirmacion("¿Estás seguro que quieres borrar esta película?")) {
                tablaPeliculas.getItems().remove(indice);
            }
        } else {
            mostrarAlerta("Debes seleccionar una pelicula");
        }
    }

    // Metodo para guardar la lista de películas en un archivo JSON

    public static void guardar(){
        ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getInstancia().getListaPeliculas();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("datos/peliculas.json"),listaPeliculas);
            mostrarAlerta("Datos guardados correctamente");
        }catch (IOException e) {
            mostrarAlerta("ERROR no se ha podido guardar los datos de la aplicación");
            e.printStackTrace();
        }
    }

    // Metodo para mostrar una alerta de confirmación

    public static boolean mostrarConfirmacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    // Método para mostrar una alerta de advertencia

    public static void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Metodo para buscar una película
    // por título en el tableview
    public boolean buscarPelicula(String tituloBuscado) {
        for (Pelicula pelicula : listaPeliculas) {
            if (pelicula.getTitle().equalsIgnoreCase(tituloBuscado)) {
                tablaPeliculas.getSelectionModel().select(pelicula); // Selecciona la fila
                tablaPeliculas.scrollTo(pelicula); // Desplaza hasta la película seleccionada
                return true; // Sale del método una vez encontrada
            }
        }
        return false;
    }

    // Metodo para cargar imagen en un
    // imageview a partir de la URL

    public boolean cargarImagen(ImageView imageview, String posterUrl) {
        Image imagenError = new Image(getClass().getResource("/images/imageError.png").toString()); // Imagen local de error
        if (esImagenValida(posterUrl)) {
            try {
                // Cargamos la imagen desde la URL
                Image image = new Image(posterUrl, true);
                imageview.setImage(image);// Establecemos la imagen en el ImageView
                return true;
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + posterUrl);
                e.printStackTrace();
                imageview.setImage(null); // Si hay un error limpiamos el ImageView
                return false;
            }
        } else {
            imageview.setImage(imagenError); // Muestra imagen de error si la URL no es válida
            return false;
        }

    }
    //Metodo para vetificar si la URL apunta a una imagen válida

    private boolean esImagenValida(String urlStr) {
        if (urlStr == null || urlStr.isEmpty()) {
            return false;
        } else {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD"); // Solo obtenemos headers, sin descargar la imagen
                connection.setConnectTimeout(3000); // Tiempo máximo de espera
                connection.setReadTimeout(3000);
                connection.connect();

                String contentType = connection.getContentType();
                return contentType != null && contentType.startsWith("image"); // Verificamos si es una imagen
            } catch (Exception e) {
                return false;
            }
        }
    }


}