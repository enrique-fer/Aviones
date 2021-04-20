package modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Clasificacion implements Comparable<Clasificacion> {
	
	private Person person;
	private final DoubleProperty puntuacion;
	private final IntegerProperty posicion;

	public Clasificacion(Person person) {
		this.setPerson(person);
		this.puntuacion = new SimpleDoubleProperty(0);
		this.posicion = new SimpleIntegerProperty(0);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public double getPuntuacion() {
		return puntuacion.get();
	}

	public void setPuntuacion(double puntuacion) {
		this.puntuacion.set(puntuacion);
	}
	
	public DoubleProperty puntuacionProperty() {
		return puntuacion;
	}
	
	public int getPosicion() {
		return posicion.get();
	}

	public void setPosicion(int posicion) {
		this.posicion.set(posicion);
	}
	
	public IntegerProperty posicionProperty() {
		return posicion;
	}

	@Override
	public int compareTo(Clasificacion o) {
		 return -Double.compare(getPuntuacion(), o.getPuntuacion());
	}
}
