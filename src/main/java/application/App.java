package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;

import application.database.utils.UtilsBD;
import application.exceptions.FaltaInterfaz;
import application.panels.PaneDistribucion;
import application.ventana.VentanaInicioSesion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	// Si el usuario está desconectaco (-1)
	public static int userLog = -1;

	@Override
	public void start(Stage stage) {
		try {
			// Conectamos a la Base de Datos
			Connection con = UtilsBD.conectarBD();

			// Buscamos las cookies de sesión(si hay alguna)
			new VentanaInicioSesion(con);

			BorderPane application = new PaneDistribucion(userLog, stage, con);

			Scene scene = new Scene(application, 900, 700);
			scene.getStylesheets().add(getClass().getResource("/estilos/application.css").toExternalForm());
			stage.setTitle("RickyRack");
			stage.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\RickyRack-logo-fondo.png")));
			stage.setScene(scene);
			stage.show();
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, stage);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}