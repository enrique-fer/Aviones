package vista;

import controlador.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import modelo.Manga;
import modelo.Person;
import modelo.Prueba;

public class MangaOverviewController {

	@FXML
	private ChoiceBox<Integer> years;
	@FXML
	private ChoiceBox<Prueba> months;
	@FXML
	private TableView<Manga> mangasTable;
	@FXML 
	private TableColumn<Manga, Number> numeroColumn;
	@FXML
	private Button groups;
	@FXML
	private Button gOne;
	@FXML
	private Button gTwo;
	
	private MainApp mainApp;
	
	public MangaOverviewController() {}
	
	@FXML
	private void initialize() {
		numeroColumn.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
	}
	
	public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        setYears();
	}
	
	private void setYears() {
		years.setItems(this.mainApp.getBBDD().getPruebaYears());
		groups.setVisible(false);
		gOne.setVisible(false);
		gTwo.setVisible(false);		
		
		showPruebasAnio(null);

		years.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPruebasAnio(newValue));
	}
	
	private void showPruebasAnio(Integer anio) {
		if (anio != null) {
			mangasTable.getItems().clear();
			months.setItems(mainApp.getBBDD().getPruebaData(anio));

			showPruebaDetails(null);

			months.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> showPruebaDetails(newValue));
		}
	}
	
	private void showPruebaDetails(Prueba prueba) {
        if (prueba != null) {
        	ObservableList<Manga> mangas = this.mainApp.getBBDD().getPruebaMangasData(prueba);
        	
        	if (prueba.getParticipantes() < 20) {
        		Alert a = new Alert(AlertType.WARNING);
				a.setTitle("Mangas");
				a.setHeaderText("Participantes");
				a.setContentText("No hay participantes suficientes para mostrar\n"
						+ "las mangas");
				a.showAndWait();
        	} else {
        		if (prueba.getParticipantes() == 20 && mangas.size() == 0) {
	        		groups.setVisible(true);
	        		Alert a = new Alert(AlertType.WARNING);
					a.setTitle("Mangas");
					a.setHeaderText("Grupos");
					a.setContentText("Genera los grupos antes de mostrar las mangas");
					a.showAndWait();
	        	} else {
	        		groups.setVisible(false);
	        		mangasTable.setItems(mangas);
	            	
	            	showMangasDetails(null);
	            	
	            	mangasTable.getSelectionModel().selectedItemProperty().addListener(
	            			(observable, oldValue, newValue) -> showMangasDetails(newValue)); 
	        	}
        	}        	 	
        } 
	}

	private void showMangasDetails(Manga manga) {
		if (manga != null) {
			gOne.setVisible(true);
			gTwo.setVisible(true);
		}
	}
	
	@FXML 
	private void genGroups() {
		int one = 0, two = 0;
		Prueba selprueba = months.getSelectionModel().getSelectedItem();
		ObservableList<Person> pilotos = this.mainApp.getBBDD().getPruebaPersonData(selprueba);
		
		for (Person p : pilotos) {
			this.mainApp.getBBDD().insertarPersonMangaPrueba(selprueba, p);
		}
		
		for (int i = 1; i <= 6; i++) {
			one = 0;
			two = 0;
			for (Person p : pilotos) {
				if (Math.random() < 0.5) {
					if (two < 10) {
						this.mainApp.getBBDD().updatePersonMangaPrueba(p, selprueba, i, 2);
						two++;
					} else {
						this.mainApp.getBBDD().updatePersonMangaPrueba(p, selprueba, i, 1);
						one++;
					}
				} else {
					if (one < 10) {
						this.mainApp.getBBDD().updatePersonMangaPrueba(p, selprueba, i, 1);
						one++;
					} else {
						this.mainApp.getBBDD().updatePersonMangaPrueba(p, selprueba, i, 2);
						two++;
					}
				}
			}
		}
		
		mangasTable.setItems(this.mainApp.getBBDD().getPruebaMangasData(selprueba));
		groups.setVisible(false);
	}
	@FXML
	private void showGroupOne() {
		Prueba selP = months.getSelectionModel().getSelectedItem();
		Manga selM = mangasTable.getSelectionModel().getSelectedItem();
		selM.setGrupo(1);
		this.mainApp.showPersonMangaDialog(selP, selM);
	}
	
	@FXML
	private void showGroupTwo() {
		Prueba selP = months.getSelectionModel().getSelectedItem();
		Manga selM = mangasTable.getSelectionModel().getSelectedItem();
		selM.setGrupo(2);
		this.mainApp.showPersonMangaDialog(selP, selM);
	}
}