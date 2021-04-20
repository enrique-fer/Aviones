package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.Manga;
import modelo.Person;
import modelo.Prueba;

public class BBDD {
	private final String drive = "jdbc:mysql://localhost:3306/";
	private final String NOMBRE = "competicion";
	private final String TABLA_PILOTO = "CREATE TABLE pilotos "
			+ "(licencia INT(20), nombre varchar(50), "
			+ "apellidos varchar(100), PRIMARY KEY(licencia))";

	private final String TABLA_PRUEBA = "CREATE TABLE pruebas "
			+ "(nombre varchar(50), dia INT(3), mes INT(3), anio INT(5),"
			+ " participantes INT(3), lista INT(4), PRIMARY KEY(mes, anio))";

	private final String TABLA_PILOTO_PRUEBA = "CREATE TABLE pilotos_pruebas"
			+ "(id_piloto INT(20), id_prueba_mes INT(3), id_prueba_anio INT(3), "
			+ "espera BOOLEAN, orden INT(4), "
			+ "PRIMARY KEY(id_piloto, id_prueba_mes, id_prueba_anio), "
			+ "CONSTRAINT piloto_FK FOREIGN KEY(id_piloto) REFERENCES pilotos(licencia),"
			+ "CONSTRAINT prueba_FK FOREIGN KEY(id_prueba_mes, id_prueba_anio) " 
			+ "REFERENCES pruebas(mes, anio))";

	private final String TABLA_MANGA = "CREATE TABLE mangas "
			+ "(numero INT(2), grupo INT(2), id_prueba_mes INT(3), "
			+ "id_prueba_anio INT(5), id_piloto INT(20), puntuacion DOUBLE, "
			+ "vuelo VARCHAR(10), aterrizaje INT(3), altura INT(5), "
			+ "PRIMARY KEY(numero, id_piloto, id_prueba_mes, id_prueba_anio),"
			+ "CONSTRAINT m_piloto_FK FOREIGN KEY(id_piloto) REFERENCES pilotos(licencia),"
			+ "CONSTRAINT m_prueba_FK FOREIGN KEY(id_prueba_mes, id_prueba_anio) " 
			+ "REFERENCES pruebas(mes, anio))";
	
	private Connection db;

	public BBDD() {
		if (!conectar()) {
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
			crearTablaManga();
			
			insertPerson(new Person("Enrique", "Fernandez", "1"));
			insertPerson(new Person("Cesar", "Carras", "2"));
			insertPerson(new Person("Marcos", "Heras", "3"));
			insertPerson(new Person("Christian", "Amigo", "4"));
			insertPerson(new Person("Martin", "Sanchez", "5"));
			insertPerson(new Person("Dario", "Morales", "6"));
			insertPerson(new Person("Raquel", "Garcia", "7"));
			insertPerson(new Person("Sakina", "Alboudani", "8"));
			insertPerson(new Person("Silvia", "Muñoz", "9"));
			insertPerson(new Person("Carmen", "Lopez", "10"));
			insertPerson(new Person("Ana", "Santiago", "11"));
			insertPerson(new Person("Toñi", "Aprieto", "12"));
			insertPerson(new Person("Isac", "Torres", "13"));
			insertPerson(new Person("Mario", "Jorrin", "14"));
			insertPerson(new Person("Lara", "Ramos", "15"));
			insertPerson(new Person("Blanca", "Macoss", "16"));
			insertPerson(new Person("Cribin", "Garcia", "17"));
			insertPerson(new Person("Alex", "Garcia", "18"));
			insertPerson(new Person("Raoul", "Vazquez", "19"));
			insertPerson(new Person("Martina", "Alvarez", "20"));
			insertPerson(new Person("Andrea", "Alvarez", "21"));
			insertPerson(new Person("Maria", "Gonzalez", "22"));
			insertPerson(new Person("Felix", "Rodriguez", "23"));
			insertPerson(new Person("Conchi", "Uco", "24"));
			insertPerson(new Person("Pepe", "Botella", "25"));
			
			insertPrueba(new Prueba("GrandT", LocalDate.of(2021, 6, 15)));
			insertPrueba(new Prueba("Prix", LocalDate.of(2021, 9, 8)));
			insertPrueba(new Prueba("Canyon", LocalDate.of(2021, 11, 22)));
			insertPrueba(new Prueba("Express", LocalDate.of(2022, 2, 11)));
			insertPrueba(new Prueba("Alturas", LocalDate.of(2022, 5, 27)));
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
	
	private void crearTablaManga() throws SQLException {
		PreparedStatement ps = db.prepareStatement(TABLA_MANGA);
		ps.executeUpdate();
		ps.close();
	}
	
	// SELECTS
	//Datos de personas
	public ObservableList<Person> getPersonData() {
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
				p.setPruebas(getPruebaPersonData(p.getLicencia()));

				personData.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return personData;
	}
	
	public ObservableList<Person> getPruebaPersonData(Prueba prueba) {
		ObservableList<Person> inscritosData = FXCollections.observableArrayList();
		String query = "SELECT * FROM pilotos WHERE licencia in ("
				+ "SELECT id_piloto FROM pilotos_pruebas WHERE id_prueba_mes = ? "
				+ "AND id_prueba_anio = ? AND espera = false);";

		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, prueba.getFecha().getMonthValue());
			ps.setInt(2, prueba.getFecha().getYear());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Person p = new Person();
				p.setLicencia(rs.getString("licencia"));
				p.setNombre(rs.getString("nombre"));
				p.setApellidos(rs.getString("apellidos"));
				
				inscritosData.add(p);
			}
			ps.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return inscritosData;
	}
	
	public ObservableList<Person> getPruebaPersonDataEspera(Prueba prueba) {
		ObservableList<Person> data = FXCollections.observableArrayList();
		
		String query = "SELECT p.licencia, p.nombre, p.apellidos "
				+ "FROM pilotos AS p INNER JOIN pilotos_pruebas AS pp "
				+ "ON p.licencia = pp.id_piloto "
				+ "WHERE pp.id_prueba_mes = ? AND pp.id_prueba_anio = ? "
				+ "AND pp.espera = true ORDER BY pp.orden";
		
		PreparedStatement ps;
		
		try {
			ps = db.prepareStatement(query);
			ps.setInt(1, prueba.getFecha().getMonthValue());
			ps.setInt(2, prueba.getFecha().getYear());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Person p = new Person();
				p.setLicencia(rs.getString("licencia"));
				p.setNombre(rs.getString("nombre"));
				p.setApellidos(rs.getString("apellidos"));
				
				data.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	//Datos de pruebas
	public ObservableList<Prueba> getPruebaData(int anio) {
		ObservableList<Prueba> pruebaData = FXCollections.observableArrayList();
		String query = "SELECT * FROM pruebas WHERE anio = ? ORDER BY mes";

		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, anio);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Prueba p = new Prueba();
				p.setNombre(rs.getString("nombre"));
				int dia = rs.getInt("dia");
				int mes = rs.getInt("mes");
				anio = rs.getInt("anio");
				p.setFecha(LocalDate.of(anio, mes, dia));
				p.setParticipantes(rs.getInt("participantes"));
				p.setLista(rs.getInt("lista"));

				pruebaData.add(p);
			}
			ps.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return pruebaData;
	}

	public ObservableList<Prueba> getPruebaPersonData(String licencia) {
		ObservableList<Prueba> data = FXCollections.observableArrayList();

		String query = "SELECT * FROM pruebas WHERE (mes,anio) IN ("
				+ "SELECT id_prueba_mes, id_prueba_anio FROM pilotos_pruebas "
				+ "WHERE id_piloto = ?)";

		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, licencia);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Prueba p = new Prueba();
				p.setNombre(rs.getString("nombre"));
				int dia = rs.getInt("dia");
				int mes = rs.getInt("mes");
				int anio = rs.getInt("anio");
				p.setFecha(LocalDate.of(anio, mes, dia));
				p.setParticipantes(rs.getInt("participantes"));

				data.add(p);
			}
			ps.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return data;
	}
	
	
	
	public ObservableList<Integer> getPruebaYears() {
		ObservableList<Integer> years = FXCollections.observableArrayList();
		String query = "SELECT DISTINCT anio FROM pruebas";

		try {
			Statement st = db.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				years.add(rs.getInt("anio"));
			}
			st.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		
		return years;
	}
	
	public ObservableList<Prueba> getPersonPruebaData(Person person, int anio) {
		ObservableList<Prueba> data = FXCollections.observableArrayList();

		String query = "SELECT * FROM pruebas WHERE (mes,anio) IN ("
				+ "SELECT id_prueba_mes, id_prueba_anio FROM pilotos_pruebas "
				+ "WHERE id_piloto = ? AND id_prueba_anio = ? AND espera = false)";

		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setInt(2, anio);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Prueba p = new Prueba();
				p.setNombre(rs.getString("nombre"));
				int dia = rs.getInt("dia");
				int mes = rs.getInt("mes");
				p.setFecha(LocalDate.of(anio, mes, dia));
				p.setParticipantes(rs.getInt("participantes"));

				data.add(p);
			}
			ps.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return data;
	}
	
	//Datos de mangas
	public ObservableList<Manga> getPruebaMangasData(Prueba prueba) {
		ObservableList<Manga> data = FXCollections.observableArrayList();
		
		String query = "SELECT DISTINCT numero FROM mangas WHERE id_prueba_mes = ? "
				+ "AND id_prueba_anio = ?";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, prueba.getFecha().getMonthValue());
			ps.setInt(2, prueba.getFecha().getYear());
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int numero = rs.getInt("numero");
				
				Manga m = new Manga(numero, 0);
				
				data.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}

	public ObservableList<Person> getPersonMangaGrupoData(Prueba prueba, Manga manga) {
		ObservableList<Person> data = FXCollections.observableArrayList();
		
		String query = "SELECT * FROM pilotos WHERE licencia IN ("
				+ "SELECT id_piloto FROM mangas WHERE id_prueba_mes = ? "
				+ "AND id_prueba_anio = ? AND numero = ? AND grupo = ?)";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setInt(1, prueba.getFecha().getMonthValue());
			ps.setInt(2, prueba.getFecha().getYear());
			ps.setInt(3, manga.getNumero());
			ps.setInt(4, manga.getGrupo());
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				Person p = new Person();
				p.setLicencia(rs.getString("licencia"));
				p.setNombre(rs.getString("nombre"));
				p.setApellidos(rs.getString("apellidos"));
				
				data.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return data;
	}
	
	public Manga getMangaPersonPrueba(Person person, Prueba prueba, Manga manga) {
		Manga m = new Manga(manga.getNumero(), manga.getGrupo());
		
		String query = "SELECT * FROM mangas WHERE id_piloto = ? AND id_prueba_mes = ? "
				+ "AND id_prueba_anio = ? AND numero = ? AND grupo = ?";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setInt(2, prueba.getFecha().getMonthValue());
			ps.setInt(3, prueba.getFecha().getYear());
			ps.setInt(4, manga.getNumero());
			ps.setInt(5, manga.getGrupo());
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String v = rs.getString("vuelo");
				if (v == null) {
					m.setVuelo("0'");
				} else {
					m.setVuelo(v);
				}
				
				m.setAterrizaje(rs.getInt("aterrizaje"));
				m.setAltura(rs.getInt("altura"));
				m.setPuntuacion(rs.getDouble("puntuacion"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return m;
	}
	
	//Datos de clasificaciones
	public ObservableList<Person> getPruebaResults(Prueba prueba) {
		ObservableList<Person> data = FXCollections.observableArrayList();
		
		return data;
	}
	
	// INSERTS
	public void insertPerson(Person person) {
		String query = "INSERT INTO pilotos (licencia, nombre, apellidos)" + " VALUES (?, ?, ?)";

		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setString(2, person.getNombre());
			ps.setString(3, person.getApellidos());

			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertPrueba(Prueba prueba) throws SQLException {
		String query = "INSERT INTO pruebas (nombre, dia, mes, anio, participantes, lista) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement ps = db.prepareStatement(query);
		ps.setString(1, prueba.getNombre());
		ps.setInt(2, prueba.getFecha().getDayOfMonth());
		ps.setInt(3, prueba.getFecha().getMonthValue());
		ps.setInt(4, prueba.getFecha().getYear());
		ps.setInt(5, prueba.getParticipantes());
		ps.setInt(6, prueba.getLista());

		ps.executeUpdate();
	}

	public void insertPruebaPerson(Prueba prueba, Person person) {
		String query = "INSERT INTO pilotos_pruebas (id_piloto, id_prueba_mes,"
				+ " id_prueba_anio, espera, orden)"
				+ " VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setInt(2, prueba.getFecha().getMonthValue());
			ps.setInt(3, prueba.getFecha().getYear());
			ps.setBoolean(4, false);
			ps.setInt(5, prueba.getLista());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertarPruebaPersonEspera(Prueba prueba, Person person) {
		String query = "INSERT INTO pilotos_pruebas (id_piloto, id_prueba_mes,"
				+ " id_prueba_anio, espera, orden)"
				+ " VALUES (?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setInt(2, prueba.getFecha().getMonthValue());
			ps.setInt(3, prueba.getFecha().getYear());
			ps.setBoolean(4, true);
			ps.setInt(5, prueba.getLista());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertarPersonMangaPrueba(Prueba prueba, Person person) {
		String query = "INSERT INTO mangas (numero, id_piloto,  id_prueba_mes, "
				+ "id_prueba_anio) VALUES (?, ?, ?, ?)";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(2, person.getLicencia());
			ps.setInt(3, prueba.getFecha().getMonthValue());
			ps.setInt(4, prueba.getFecha().getYear());
			
			for (int i = 1; i <= 6; i++) {
				ps.setInt(1, i);
				
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// UPDATE
	public void updatePerson(Person person) {
		String query = "UPDATE pilotos SET nombre = ?, apellidos = ? WHERE licencia = ?";
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query);
			ps.setString(1, person.getNombre());
			ps.setString(2, person.getApellidos());
			ps.setString(3, person.getLicencia());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePrueba(Prueba prueba) {
		String query = "UPDATE pruebas SET participantes = ?, lista = ? WHERE mes = ? AND anio = ?";
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query);
			ps.setInt(1, prueba.getParticipantes());
			ps.setInt(2, prueba.getLista());
			ps.setInt(3, prueba.getFecha().getMonthValue());
			ps.setInt(4, prueba.getFecha().getYear());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePersonPrueba(Person person, Prueba prueba, int pos) {
		String query = "UPDATE pilotos_pruebas SET espera = ?, orden = ? "
				+ "WHERE id_piloto = ? AND id_prueba_mes = ? AND "
				+ "id_prueba_anio = ?";
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query);
			
			if (prueba.getParticipantes() < 20) {
				ps.setBoolean(1, false);
				ps.setInt(2, 0);
				prueba.setParticipantes(prueba.getParticipantes() + 1);
				prueba.setLista(prueba.getLista() -1);
			} else {
				ps.setBoolean(1, true);
				ps.setInt(2, pos);
				pos++;
			}
			
			ps.setString(3, person.getLicencia());			
			ps.setInt(4, prueba.getFecha().getMonthValue());
			ps.setInt(5, prueba.getFecha().getYear());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePersonMangaPrueba(Person person, Prueba prueba, int manga, int grupo) {
		String query = "UPDATE mangas SET grupo = ?, vuelo = ?,  aterrizaje = ?, "
				+ "altura = ?, puntuacion = ? "
				+ "WHERE id_piloto = ? AND id_prueba_mes = ? AND "
				+ "id_prueba_anio = ? AND numero = ?";
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query);
			
			ps.setInt(1, grupo);	
			ps.setString(2, "0'");
			ps.setInt(3, -1);
			ps.setInt(4, 0);
			ps.setInt(5, 0);
			ps.setString(6, person.getLicencia());
			ps.setInt(7, prueba.getFecha().getMonthValue());
			ps.setInt(8, prueba.getFecha().getYear());
			ps.setInt(9, manga);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateMangaPersonPrueba(Person person, Prueba prueba, Manga manga){
		String query = "UPDATE mangas SET vuelo = ?, aterrizaje = ?, altura = ?, "
				+ "puntuacion = ? WHERE id_piloto = ? AND id_prueba_mes = ? "
				+ "AND id_prueba_anio = ? AND numero = ? AND grupo = ?";
		
		try {
			PreparedStatement ps = db.prepareStatement(query);
			
			ps.setString(1, manga.getVuelo());
			ps.setInt(2, manga.getAterrizaje());
			ps.setInt(3, manga.getAltura());
			ps.setDouble(4, manga.getPuntuacion());
			ps.setString(5, person.getLicencia());
			ps.setInt(6, prueba.getFecha().getMonthValue());
			ps.setInt(7, prueba.getFecha().getYear());
			ps.setInt(8, manga.getNumero());
			ps.setInt(9, manga.getGrupo());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// DELETE
	public void deletePerson(Person person) {
		String query1 = "DELETE FROM pilotos_pruebas WHERE id_piloto = ?";
		String query = "DELETE FROM pilotos WHERE licencia = ?";
		
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query1);
			ps.setString(1, person.getLicencia());
			ps.executeUpdate();
			
			ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(Prueba prueba : person.getPruebas()) {
			prueba.setParticipantes(prueba.getParticipantes() - 1);
			updatePrueba(prueba);
		}
	}
	
	public void deletePersonPrueba(Person person, Prueba prueba) {
		String query = "DELETE FROM pilotos_pruebas WHERE "
				+ "id_piloto= ? AND id_prueba_mes = ? "
				+ "AND id_prueba_anio = ?";
		
		PreparedStatement ps;
		try {
			ps = db.prepareStatement(query);
			ps.setString(1, person.getLicencia());
			ps.setInt(2, prueba.getFecha().getMonthValue());
			ps.setInt(3, prueba.getFecha().getYear());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}