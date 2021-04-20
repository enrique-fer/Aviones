package modelo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.TimeUtil;

public class Manga {
	
	private final IntegerProperty numero;
	private final IntegerProperty grupo;
	private final DoubleProperty puntuacion;
	private final StringProperty vuelo;
	private final IntegerProperty aterrizaje;
	private final IntegerProperty altura;	
	
	public Manga() {
		this(-1, -1);
	}
	
	public Manga(int numero, int grupo) {
		this.numero = new SimpleIntegerProperty(numero);
		this.grupo = new SimpleIntegerProperty(grupo);
		this.puntuacion = new SimpleDoubleProperty(0);
		this.vuelo = new SimpleStringProperty("0'");
		this.aterrizaje = new SimpleIntegerProperty(11);
		this.altura = new SimpleIntegerProperty(0);
	}

	public int getNumero() {
		return numero.get();
	}

	public void setNumero(int numero) {
		this.numero.set(numero);
	}
	
	public IntegerProperty numeroProperty() {
		return numero;
	}
	
	public int getGrupo() {
		return grupo.get();
	}

	public void setGrupo(int grupo) {
		this.grupo.set(grupo);
	}
	
	public IntegerProperty grupoProperty() {
		return grupo;
	}
	
	public double getPuntuacion() {
		setPuntuacion(calcularManga());
		return puntuacion.get();
	}

	public void setPuntuacion(double puntuacion) {
		this.puntuacion.set(puntuacion);
	}
	
	public DoubleProperty puntuacionProperty() {
		return puntuacion;
	}
	
	public String getVuelo() {
		return vuelo.get();
	}
	
	public void setVuelo(String vuelo) {
		this.vuelo.set(vuelo);
	}
	
	public StringProperty vueloProperty() {
		return vuelo;
	}
	
	public int getAterrizaje() {
		return aterrizaje.get();
	}

	public void setAterrizaje(int aterrizaje) {
		this.aterrizaje.set(aterrizaje);
	}
	
	public IntegerProperty aterrizajeProperty() {
		return aterrizaje;
	}
	
	public int getAltura() {
		return altura.get();
	}

	public void setAltura(int altura) {
		this.altura.set(altura);
	}
	
	public IntegerProperty alturaProperty() {
		return altura;
	}
	
	
	private double calcularManga() {
		double punt = 0;
		
		punt = TimeUtil.minToSec(getVuelo()) + calcAterrizaje() - calcAltura();
		
		return punt;
	}
	
	private int calcAterrizaje() {
		int puntos = 0;
		switch(getAterrizaje()) {
			case 0: { 
				puntos = 50;
				break;
			}
			case 1: {
				puntos = 45;
				break;
			}
			case 2: {
				puntos = 40;
				break;
			}
			case 3: {
				puntos = 35;
				break;
			}
			case 4: {
				puntos = 35;
				break;
			}
			case 5: {
				puntos = 30;
				break;
			}
			case 6: {
				puntos = 25;
				break;
			}
			case 7: {
				puntos = 20;
				break;
			}
			case 8: {
				puntos = 15;
				break;
			}
			case 9: {
				puntos = 10;
				break;
			}
			case 10: {
				puntos = 5;
				break;
			}
			}
		return puntos;
	}
	
	private double calcAltura() {
		double puntos = 0;
		if (getAltura() > 200) {
			puntos = (200 * 0.5) + (getAltura() - 200) * 3;
		} else {
			puntos = getAltura() * 0.5;
		}
		return puntos;
	}
}
