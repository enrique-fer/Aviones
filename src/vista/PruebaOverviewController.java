package vista;

import controlador.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import modelo.Person;
import modelo.Prueba;
import util.DateUtil;

public class PruebaOverviewController {	
	
	@FXML
	private ChoiceBox<Prueba> months;
	
	@FXML
	private Label nombreLabel;
	@FXML
	private Label fechaLabel;
	@FXML
	private Label participantesLabel;
	
	private MainApp mainApp;
	
	public PruebaOverviewController() {}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		months.setItems(mainApp.getBBDD().getPruebaData());
	}
	
	@FXML
	private void initialize() {
		showPruebaDetails(null);
		 
		months.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showPruebaDetails(newValue));
	}
	 
	private void showPruebaDetails(Prueba prueba) {
	        if (prueba != null) {
	            nombreLabel.setText(prueba.getNombre());
	            fechaLabel.setText(DateUtil.format(prueba.getFecha()));
	            participantesLabel.setText(Integer.toString(prueba.getParticipantes()));
	
	            //TODO ver pilotos inscritos
	        } else {
	            nombreLabel.setText("");
	            fechaLabel.setText("");
	            participantesLabel.setText("0");
	           
	        }
	}
	 
	@FXML
	private void signUpPrueba() {
		boolean exist = false;
		Prueba selectedPrueba = months.getSelectionModel().getSelectedItem();
		//TODO comprobar numero de participantes
		Person tempPerson = new Person();
		
		if (selectedPrueba != null) {
			if (selectedPrueba.getParticipantes() <= 20) {
				boolean okClicked = this.mainApp.showLicenciaDialog(tempPerson);
				
				if (okClicked) {
					//TODO comprobar si ya se ha inscrito
					 ObservableList<Person> personas = this.mainApp.getBBDD().getPersonData();
					 
					 for (Person person : personas) {
						 if (person.getLicencia().contentEquals(tempPerson.getLicencia())) {
							 exist = true;
							 this.mainApp.getBBDD().insertPruebaPerson(selectedPrueba, person);
							 selectedPrueba.setParticipantes(selectedPrueba.getParticipantes() + 1);
							 this.mainApp.getBBDD().updatePrueba(selectedPrueba);
							 break;
						 }
					 }
					 
					 if (!exist) {
						 Alert a = new Alert(AlertType.WARNING);
				 	        a.setTitle("Prueba");
				 	        a.setHeaderText("Numero de licencia");
				 	        a.setContentText("No existen coincidencias con la licencia introducida");
				 	        a.showAndWait();
					 }
				}
				months.setItems(mainApp.getBBDD().getPruebaData());
				months.getSelectionModel().select(selectedPrueba);
			} else {
				Alert a = new Alert(AlertType.WARNING);
	 	        a.setTitle("Prueba");
	 	        a.setHeaderText("Plazas disponibles");
	 	        a.setContentText("No quedan plazas para la prueba seleccionada");
	 	        a.showAndWait();
			}
		} else {
			Alert a = new Alert(AlertType.WARNING);
 	        a.setTitle("Seleccion");
 	        a.setHeaderText("No has seleccionado ninguna prueba");
 	        a.setContentText("Porfavor selecciona una prueba de las disponibles");
 	        a.showAndWait();
		}
	}
	
	@FXML
	private void handleNewPrueba() {
		 Prueba tempPrueba = new Prueba();
	        boolean okClicked = mainApp.showPruebaEditDialog(tempPrueba);
	        if (okClicked) {
	            mainApp.getBBDD().insertPrueba(tempPrueba);
	            months.setItems(mainApp.getBBDD().getPruebaData());
	        }
	}
}
