package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.App;
import application.cookies.Hash;
import application.database.model.UsuarioDAO;
import application.panels.PaneDistribucion;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaIniciarSesion extends Stage {

	private int numFallos = 0;

	public VentanaIniciarSesion(Stage stage, Connection con) {
		try {
			GridPane inicioSesion = new GridPane();
			Label lblUsuario = new Label("Introduzca el usuario:");
			TextField usuario = new TextField();
			Label lblContrasena = new Label("Introduzca la contraseña:");
			PasswordField contrasena = new PasswordField();
			CheckBox guardarInicioSesion = new CheckBox("Guardar mi inicio de sesión");
			Button iniciar = new Button("Iniciar Sesión");
			inicioSesion.getChildren().addAll(lblUsuario, usuario, lblContrasena, contrasena, guardarInicioSesion,
					iniciar);

			iniciar.setOnMouseClicked(event -> {
				String nombre = usuario.getText();
				String password = contrasena.getText();
				if (usuario.getText().length() == 0 || password.length() == 0) {
					// throws CampoObligatorios
				} else {
					try {
						ResultSet rs = UsuarioDAO.getUsuario(con, nombre);
						if (rs.next()) {
							if (UsuarioDAO.comprobarContrasena(con, nombre, password)) {
								int id = rs.getInt("idusuario");
								if (guardarInicioSesion.isSelected()) {
									Hash.crearHash(con, id);
								}
								App.userLog = id;
								this.close();
								stage.close();
								stage.setScene(new Scene(new PaneDistribucion(App.userLog, stage, con), 900, 700));
								stage.show();
							} else {
								this.numFallos++;
								if (numFallos == 3) {
									stage.close();
								}
								// throws ContrasenaErronea(numFallos)
							}
						} else {
							// throws UsuarioNoExistene
						}
					} catch (SQLException e) {
					}
				}
			});

			GridPane.setConstraints(lblUsuario, 0, 0);
			GridPane.setConstraints(usuario, 1, 0);
			GridPane.setConstraints(lblContrasena, 0, 1);
			GridPane.setConstraints(contrasena, 1, 1);
			GridPane.setConstraints(guardarInicioSesion, 0, 2, 2, 1);
			GridPane.setConstraints(iniciar, 0, 3, 2, 1);

			GridPane.setMargin(lblUsuario, new Insets(5, 10, 5, 10));
			GridPane.setMargin(usuario, new Insets(5, 10, 5, 10));
			GridPane.setMargin(lblContrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(contrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(guardarInicioSesion, new Insets(5, 10, 5, 10));
			GridPane.setMargin(iniciar, new Insets(5, 10, 5, 10));

			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\default-user-icon.png")));
			Scene escena = new Scene(inicioSesion, 330, 140);
			this.setTitle("Iniciar Sesión");
			this.initOwner(stage);
			this.initModality(Modality.WINDOW_MODAL);
			this.setScene(escena);

			// Mostramos
			this.showAndWait();
		} catch (FileNotFoundException e) {
			// throws FaltaInterfaz
		}
	}
}
