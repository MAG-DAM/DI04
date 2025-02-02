package dam.alumno.filmoteca;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.io.IOException;
import java.util.List;

public class MainApp extends Application {

    // Método que se ejecuta al iniciar la aplicación
    @Override
    public void start(Stage stage) throws IOException {
        // Cargamos el archivo FXML que define la interfaz de usuario
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainView.fxml"));
        // Creamos escena con el archivo FXML cargado y establecemos el tamaño de la ventana
        Scene scene = new Scene(fxmlLoader.load(), 1000, 550);
        stage.setTitle("APLICACION DE FILMOTECA");
        // Establecemos tamaño mínimo de la ventana
        stage.setMinWidth(1000);
        stage.setMinHeight(550);
        // Asignamos la escena al stage y la mostramos
        stage.setScene(scene);
        stage.show();
    }
    //Lanzamos la aplicación
    public static void main(String[] args) {
        launch();
    }

    public void init() {
        // Obtenemos instancia única de Datos, según patrón Singleton
        DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
        // Creamos objeto ObjectMapper para trabajar con archivo JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Leemos los datos del archivo "peliculas.json"
            // y lo convertirmos en una lista de objetos tipo "Pelicula"
            List<Pelicula> lista = objectMapper.readValue(new File("datos/peliculas.json"),
                    new TypeReference<List<Pelicula>>() {});
            // Establecemos la lista de películas en el objeto DatosFilmoteca
            datosFilmoteca.getListaPeliculas().setAll(lista);

        } catch (IOException e){
            // Si ocurre un error al cargar el archivo JSON
            // mostramos mensaje y finalizamos
            System.out.println("ERROR al cargar los datos. La aplicación no puede iniciarse");
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {

    }


}



