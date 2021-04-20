package vista;

import java.util.Collections;

import controlador.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import modelo.Clasificacion;
import modelo.Manga;
import modelo.Person;
import modelo.Prueba;

public class ClasificacionOverviewController {
	
	@FXML
	private ChoiceBox<Integer> years;
	@FXML
	private ChoiceBox<Prueba> months;
	@FXML
	private Button results;
	@FXML
	private TableView<Clasificacion> puestosTable;
	@FXML 
	private TableColumn<Clasificacion, Number> puestoColumn;
	@FXML 
	private TableColumn<Clasificacion, String> licenciaColumn;
	@FXML 
	private TableColumn<Clasificacion, Number> puntuacionColumn;
	
	private MainApp mainApp;
	private ObservableList<Clasificacion> podium;
	
	public ClasificacionOverviewController() {
		this.podium = FXCollections.observableArrayList();
	}
	
	@FXML
	private void initialize() {
		puestoColumn.setCellValueFactory(cellData -> cellData.getValue().posicionProperty());
		licenciaColumn.setCellValueFactory(cellData -> cellData.getValue().getPerson().licenciaProperty());
		puntuacionColumn.setCellValueFactory(cellData -> cellData.getValue().puntuacionProperty());
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;		
		
		setYears();
	}

	private void setYears() {
		results.setVisible(false);
		years.setItems(this.mainApp.getBBDD().getPruebaYears());
		
		showPruebasAnio(null);

		years.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPruebasAnio(newValue));
	}
	
	private void showPruebasAnio(Integer anio) {
		if (anio != null) {
			results.setVisible(false);
			months.setItems(mainApp.getBBDD().getPruebaData(anio));

			showPruebaDetails(null);

			months.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> showPruebaDetails(newValue));
		}
	}
	
	private void showPruebaDetails(Prueba prueba) {
        if (prueba != null) {
        	puestosTable.getItems().clear();
        	results.setVisible(true);
        } 
	}
	
	@FXML
	private void showResults() {
		podium = FXCollections.observableArrayList();
		Prueba selP = months.getSelectionModel().getSelectedItem();
		ObservableList<Manga> mangas = this.mainApp.getBBDD().getPruebaMangasData(selP);
		Clasificacion c = null;
		
		if (selP != null) {
			if (selP.getParticipantes() < 20) {
				Alert a = new Alert(AlertType.WARNING);
				a.setTitle("Participantes");
				a.setHeaderText("Numero de participantes");
				a.setContentText("No hay registrados los suficientes participantes");
				a.showAndWait();
			} else {
				if (selP.getParticipantes() == 20 && mangas.size() == 0) {
					Alert a = new Alert(AlertType.WARNING);
					a.setTitle("Clasificacion");
					a.setHeaderText("Puntuaje");
					a.setContentText("Todavia no se ha realizado la prueba, por lo que\n"
							+ "no se pueden visualizar los puntos obtenidos");
					a.showAndWait();
				} else {
					 ObservableList<Person> persons = this.mainApp.getBBDD().getPruebaPersonData(selP);
					 Manga m = null;
					 for(Person p : persons) {
						 c = new Clasificacion(p);
						 for (int i = 1; i <= 6; i++) {
							 for (int j = 1; j <= 2; j++) {
								 m = new Manga(i, j);
								 m = this.mainApp.getBBDD().getMangaPersonPrueba(p, selP, m);
								 c.setPuntuacion(c.getPuntuacion() + m.getPuntuacion());
							 }
						 }
						 
						 this.podium.add(c);
					 }
					 
					 Collections.sort(podium);
					 
					 for(int i = 0; i < podium.size(); i++) {
						 podium.get(i).setPosicion(i + 1);
					 }
					 
					 puestosTable.setItems(podium);
				}
			}
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Seleccion");
			a.setHeaderText("No has seleccionado ninguna prueba");
			a.setContentText("Profavor selecciona una prueba de las disponibles");
			a.showAndWait();
		}
	}
}
