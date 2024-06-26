package application.database.utils;

//Con un import podemos utilizar todas las clases de paquete indicado
//El * implica todas las clases dentro de java.sql
import java.sql.Connection;
import java.sql.DriverManager;

public class UtilsBD {

	/**
	 * Conecta a la Base de datos
	 * 
	 * @return devuelve la conexión correcta de la base de datos
	 *         o null si no
	 */
	public static Connection conectarBD() {

		try {
			// Definimos el driver de la BD a la que nos conectamos
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Creamos una conexión activa con BD
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/rickyrack", "root", "");

			// Si no ha saltado la excepcion devolvemos la conexion
			return con;

			// Capturamos cualquier excepción
		} catch (Exception e) {
			// throws ConexionFallida
			return null;
		}

	}

}
