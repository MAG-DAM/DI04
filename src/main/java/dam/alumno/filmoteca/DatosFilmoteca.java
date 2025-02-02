package dam.alumno.filmoteca;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Clase singleton que gestiona la lista de películas
// Utiliza el patrón Singleton para asegurar que solo
// haya una instancia de la clase en la aplicación

public class DatosFilmoteca {
    // Instancia única de la clase
    private static DatosFilmoteca instancia = null;
    // Lista observable de películas
    private static ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();

    private void DatosFilmoteca () {
    }

     //Método estático que devuelve la instancia única de la clase
    //Si la instancia no existe, la crea

    public static DatosFilmoteca  getInstancia() {
        if (instancia == null) {
            instancia = new DatosFilmoteca();
        }
        return instancia;
    }

   //Metodo para obtener la lista observable de películas

    public static ObservableList<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    //Metodo para establecer nueva lista de películas

    public static void setListaPeliculas(ObservableList<Pelicula> listaPeliculas) {
        DatosFilmoteca.listaPeliculas = listaPeliculas;
    }
}
