package bd;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Person;
import modelo.Prueba;

public class BBDD {
	private final String drive = "jdbc:mysql://localhost:3306/";
	private final String NOMBRE = "competicion";
	private final String TABLA_PILOTO = "CREATE TABLE pilotos"
			+ "(licencia INT(20), nombre varchar(50), "
			+ "apellidos varchar(100), PRIMARY KEY(licencia))";
	
	private final String TABLA_PRUEBA = "CREATE TABLE pruebas" //PK (mes, anio)
			+ "(nombre varchar(50), fecha DATE,"
			+ " participantes INT(3), PRIMARY KEY(nombre))";
	//TODO añadir por separado dia, mes, anio
	
	private final String TABLA_PILOTO_PRUEBA = "CREATE TABLE pilotos_pruebas"
			+ "(id_piloto INT(20), id_prueba varchar(50), PRIMARY KEY(id_piloto, id_prueba),"
			+ "CONSTRAINT piloto_FK FOREIGN KEY(id_piloto) REFERENCES pilotos(licencia),"
			+ "CONSTRAINT prueba_FK FOREIGN KEY(id_prueba) REFERENCES pruebas(nombre))";
	//TODO incluir pilotos en lista de espera, y nuevos PK
	//TODO incluir campos para para prueba de cada piloto
	
	
	private Connection db;

	public BBDD() {
		if(!conectar()) {
			crearDB();
			conectar();
			crearTablas();
		}
	}
	
	private boolean conectar() {
        try {
            db = DriverManager.getConnection(drive + this.NOMBRE, "root", "root");
            	
            return true;
        } catch (SQLException sqle) {
            
            return false;
        }
	}
	
	private void crearDB() {
		try {
			db = DriverManager.getConnection(drive, "root", "root");
			PreparedStatement ps = db.prepareStatement("CREATE DATABASE " + this.NOMBRE);
			ps.executeUpdate();			
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void crearTablas() {
		try {
			crearTablaPilotos();
			crearTablaPruebas();
			crearTablaPP();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void crearTablaPilotos() throws SQLException {		
		PreparedStatement ps = db.prepareStatement(TABLA_PILOTO);
		ps.executeUpdate();
		ps.close();		
	}
	
	private void crearTablaPruebas() throws SQLException {		
		PreparedStatement ps = db.prepareStatement(TABLA_PRUEBA);
		ps.executeUpdate();
		ps.close();
	}
	
	private void crearTablaPP() throws SQLException {
		PreparedStatement ps = db.prepareStatement(TABLA_PILOTO_PRUEBA);
		ps.executeUpdate();
		ps.close();
	}
	
	//SELECTS
	public ObservableList<Person> getPersonData() { //Incluir la tabla prueba_piloto
		ObservableList<Person> personData = FXCollections.observableArrayList();
		String query = "SELECT * FROM pilotos";
		
		try {
			Statement st = db.createStatement();
			ResultSet rs = st.executeQuery(query);
  
			while (rs.next()) {
				Person p = new Person();
				p.setLicencia(rs.getString("licencia"));
				p.setNombre(rs.getString("nombre"));
				p.setApellidos(rs.getString("apellidos"));
				getPruebaPersonData(p);

				personData.add(p);
			}
			st.close();
	    } catch (Exception e) {
	    	System.err.println("Got an exception! ");
	    	System.err.println(e.getMessage());
	    }
		
		return personData;
	}
	
	public ObservableList<Prueba> getPruebaData() { //Incluir la tabla prueba_piloto
		ObservableList<Prueba> pruebaData = FXCollections.observableArrayList();
		String query = "SELECT * FROM pruebas ORDER BY fecha";
		
		try {
			Statement st = db.createStatement();
			ResultSet rs = st.executeQuery(query);
  
			while (rs.next()) {
				Prueba p = new Prueba();
				p.setNombre(rs.getString("nombre"));
				p.setFecha(rs.getDate("fecha").toLocalDate());
				p.setParticipantes(rs.getInt("participantes"));

				pruebaData.add(p);
			}
			st.close();
	    } catch (Exception e) {
	    	System.err.println("Got an exception! ");
	    	System.err.println(e.getMessage());
	    }
		
		return pruebaData;
	}
	
	public Person getPerson(String licencia) {
		String query = "SELECT * FROM pilotos WHERE licencia = ?";
		Person p = new Person();
		
		try {
	    	  PreparedStatement preparedStmt = db.prepareStatement(query);
	    	  preparedStmt.setString (1, licencia);
	    	  
	    	  ResultSet rs = preparedStmt.executeQuery();
	    	  
	    	  while (rs.next()) {
					p.setNombre(rs.getString("nombre"));
					p.setLicencia(licencia);
					p.setApellidos(rs.getString("apellidos"));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	public void getPruebaPersonData(Person person) {		
		String query = "SELECT * FROM pruebas WHERE nombre IN ("
				+ "SELECT id_prueba FROM pilotos_pruebas WHERE id_piloto = ?)";
		
		try {
			PreparedStatement st = db.prepareStatement(query);
			st.setString(1, person.getLicencia());
			
			ResultSet rs = st.executeQuery();
  
			while (rs.next()) {
				Prueba p = new Prueba();
				p.setNombre(rs.getString("nombre"));
				p.setFecha(rs.getDate("fecha").toLocalDate());
				p.setParticipantes(rs.getInt("participantes"));

				person.addPrueba(p);
			}
			st.close();
	    } catch (Exception e) {
	    	System.err.println("Got an exception! ");
	    	System.err.println(e.getMessage());
	    }
	}
	
	
	
	//INSERTS
	public void insertPerson(Person person) {
	      String query = "INSERT INTO pilotos (licencia, nombre, apellidos)"
	        + " VALUES (?, ?, ?)";
	     
	      try {
	    	  PreparedStatement preparedStmt = db.prepareStatement(query);
	    	  preparedStmt.setString (1, person.getLicencia());
	    	  preparedStmt.setString (2, person.getNombre());
	    	  preparedStmt.setString (3, person.getApellidos());
	    	  
	    	  preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPrueba(Prueba prueba) {
	      String query = "INSERT INTO pruebas (nombre, fecha, participantes)"
	        + " VALUES (?, ?, ?)";
	     
	      try {
	    	  PreparedStatement preparedStmt = db.prepareStatement(query);
	    	  preparedStmt.setString (1, prueba.getNombre());
	    	  preparedStmt.setDate(2, Date.valueOf(prueba.getFecha()));
	    	  preparedStmt.setInt(3, prueba.getParticipantes());
	    	  
	    	  preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertPruebaPerson(Prueba prueba, Person person) {
	      String query = "INSERT INTO pilotos_pruebas (id_piloto, id_prueba)"
	        + " VALUES (?, ?)";
	     
	      try {
	    	  PreparedStatement preparedStmt = db.prepareStatement(query);
	    	  preparedStmt.setString (1, person.getLicencia());
	    	  preparedStmt.setString(2, prueba.getNombre());
	    	  
	    	  preparedStmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//UPDATES
	public void updatePerson(Person person) {
		String query = "UPDATE pilotos SET nombre = ?, apellidos = ? where licencia = ?";
	    PreparedStatement preparedStmt;
		try {
			preparedStmt = db.prepareStatement(query);
			preparedStmt.setString(1, person.getNombre());
			preparedStmt.setString(2, person.getApellidos());
			preparedStmt.setString(3, person.getLicencia());

			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	   
	public void updatePrueba(Prueba prueba) {
		String query = "UPDATE pruebas SET participantes = ? WHERE nombre = ?";
	    PreparedStatement preparedStmt;
		try {
			preparedStmt = db.prepareStatement(query);
			preparedStmt.setInt(1, prueba.getParticipantes());
			preparedStmt.setString(2, prueba.getNombre());

			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//DELETE
	public void deletePerson(Person person) {
		String query = "DELETE FROM pilotos where licencia = ?";
	    PreparedStatement preparedStmt;
	    
	    try {
			preparedStmt = db.prepareStatement(query);
			preparedStmt.setString(1, person.getLicencia());

			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//TODO deletePruebaPerson
}