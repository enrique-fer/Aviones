package modelo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Person {

	private final StringProperty nombre;
	private final StringProperty apellidos;
	private final StringProperty licencia;
	private ObservableList<Prueba> pruebas;

	public Person() {
		this(null, null, null);
	}
	
	public Person(String nombre, String apellidos, String licencia) {
		this.nombre = new SimpleStringProperty(nombre);
		this.apellidos = new SimpleStringProperty(apellidos);
		this.licencia = new SimpleStringProperty(licencia);
		this.pruebas = FXCollections.observableArrayList();
	}
	
	public String getNombre() {
		return nombre.get();
	}

	public void setNombre(String nombre) {
		this.nombre.set(nombre);
	}
	
	public StringProperty nombreProperty() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos.get();
	}

	public void setApellidos(String apellidos) {
		this.apellidos.set(apellidos);
	}
	
	public StringProperty apellidosProperty() {
		return apellidos;
	}

	public String getLicencia() {
		return licencia.get();
	}

	public void setLicencia(String licencia) {
		this.licencia.set(licencia);
	}
	
	public StringProperty licenciaProperty() {
		return licencia;
	}

	public ObservableList<Prueba> getPruebas() {
		return pruebas;
	}

	public void setPruebas(ObservableList<Prueba> pruebas) {
		this.pruebas = pruebas;
	}
	
	public void addPrueba(Prueba prueba) {
		this.pruebas.add(prueba);
	}
}