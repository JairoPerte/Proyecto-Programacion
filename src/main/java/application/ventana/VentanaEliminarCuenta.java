package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;

import application.App;
import application.cookies.CookieWriter;
import application.database.model.UsuarioDAO;
import application.panels.PaneDistribucion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaEliminarCuenta extends Stage {

	private int numFallos = 0;

	public VentanaEliminarCuenta(Connection con, Stage stage, int userLog) {
		GridPane gP = new GridPane();

		Label lblContrasena = new Label("Introduzca la contraseña: ");
		PasswordField pfContrasena = new PasswordField();

		Label lblConfirma = new Label("Confirme la contraseña: ");
		PasswordField pfConfirma = new PasswordField();

		Button btnEliminar = new Button("Eliminar");
		btnEliminar.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #ff0000;");

		btnEliminar.setOnAction(event -> {
			if (pfContrasena.getText().length() != 0 && pfConfirma.getText().length() != 0) {
				if (pfContrasena.getText().equals(pfConfirma.getText())) {
					if (UsuarioDAO.comprobarContrasena(con, userLog, pfContrasena.getText())) {

						if (alerta(this)) {

							UsuarioDAO.eliminarUsuario(con, userLog);
							CookieWriter.eliminarCookie(userLog);
							// Desconectado por defecto
							App.userLog = -1;
							// Lo cerramos
							// Mostramos la ventan de inicio de sesión de cookies
							// guardadas (por si quiere iniciar sesión con otra cuenta o
							// no iniciar sesión)
							stage.setWidth(1);
							stage.setHeight(1);
							stage.setResizable(false);
							this.setHeight(1);
							this.setWidth(1);
							new VentanaInicioSesion(con);
							this.close();
							stage.close();

							stage.setWidth(900);
							stage.setHeight(700);
							stage.setResizable(true);
							this.setHeight(150);
							this.setWidth(330);
							// Para que cargen de nuevo los cambios
							Scene escenaNueva = new Scene(new PaneDistribucion(App.userLog, stage, con), 900, 700);
							escenaNueva.getStylesheets()
									.add(getClass().getResource("/estilos/application.css").toExternalForm());
							stage.setScene(escenaNueva);
							stage.show();
						} else {
							this.close();
						}
					} else {
						numFallos++;
						if (numFallos > 3) {
							stage.close();
						}
						// throws ContrasenaIncorrecta
					}
				} else {
					// throws ContrasenasNoCoincidentes
				}
			} else {
				// throws CamposObligatorios
			}
		});

		btnEliminar.setOnMouseEntered(event -> {
			this.getScene().setCursor(Cursor.HAND);
		});

		btnEliminar.setOnMouseExited(event -> {
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		gP.getChildren().addAll(lblContrasena, pfContrasena, lblConfirma, pfConfirma, btnEliminar);

		GridPane.setConstraints(lblContrasena, 0, 0);
		GridPane.setConstraints(pfContrasena, 1, 0);
		GridPane.setConstraints(lblConfirma, 0, 1);
		GridPane.setConstraints(pfConfirma, 1, 1);
		GridPane.setConstraints(btnEliminar, 0, 2, 2, 1);
		GridPane.setHalignment(btnEliminar, HPos.CENTER);

		GridPane.setMargin(lblContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(lblConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(btnEliminar, new Insets(10, 5, 10, 5));

		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setTitle("Eliminar");
		try {
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\cruz-roja.png")));
		} catch (FileNotFoundException e) {
			// throws FaltaInterfaz
		}
		this.setScene(new Scene(gP, 330, 150));
		this.showAndWait();
	}

	private boolean alerta(Stage stage) {
		try {
			Alert alerta = new Alert(Alert.AlertType.WARNING);

			alerta.setTitle("Borrar cuenta");
			alerta.setContentText(
					"Multiple Leviathan class creatures detected. Are you certain whatever you're doing is worth it?");
			alerta.setHeaderText("Estas a un click de eliminar tu cuenta");
			alerta.initOwner(stage);

			ButtonType btnPulsado = alerta.showAndWait().get();
			if (btnPulsado == ButtonType.OK) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
