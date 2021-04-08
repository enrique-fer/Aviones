package modelo;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prueba {
	private Map<Month, String> dict;
	
	private final StringProperty nombre;
	private final ObjectProperty<LocalDate> fecha;
	private final IntegerProperty participantes;
	
	public Prueba() {
		this(null, null);
	}

	public Prueba(String nombre, LocalDate fecha) {
		this.nombre = new SimpleStringProperty(nombre);
		this.fecha = new SimpleObjectProperty<LocalDate>(fecha);
		this.participantes = new SimpleIntegerProperty(0);
		setDictionary();
	}
	
	private Map<Month, String> getDict() {
		return this.dict;
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
	
	public LocalDate getFecha() {
		return fecha.get();
	}

	public void setFecha(LocalDate fecha) {
		this.fecha.set(fecha);
	}
	
	public ObjectProperty<LocalDate> fechaProperty() {
		return fecha;
	}
	
	public int getParticipantes() {
		return participantes.get();
	}

	public void setParticipantes(int participantes) {
		this.participantes.set(participantes);
	}
	
	public IntegerProperty participantesProperty() {
		return participantes;
	}
	
	 private void setDictionary() {
		 this.dict = new HashMap<Month, String>();
		 dict.put(Month.JANUARY, "Enero");
		 dict.put(Month.FEBRUARY, "Febrero");
		 dict.put(Month.MARCH, "Marzo");
		 dict.put(Month.APRIL, "Abril");
		 dict.put(Month.MAY, "Mayo");
		 dict.put(Month.JUNE, "Junio");
		 dict.put(Month.JULY, "Julio");
		 dict.put(Month.SEPTEMBER, "Septiembre");
		 dict.put(Month.OCTOBER, "Octubre");
		 dict.put(Month.NOVEMBER, "Noviembre");
		 dict.put(Month.DECEMBER, "Diciembre");
	 }
	
	
	@Override 
	public String toString() {
		return this.getDict().get(this.getFecha().getMonth());
	}
}
