package vista;

import java.sql.SQLException;
import controlador.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import modelo.Prueba;
import util.DateUtil;

public class PruebaOverviewController {	
	
	@FXML
	private ChoiceBox<Prueba> months;
	@FXML
	private ChoiceBox<Integer> years;
	
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
		
		years.setItems(this.mainApp.getBBDD().getPruebaYears());
	}
	
	@FXML
	private void initialize() {
		showPruebasAnio(null);

		years.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPruebasAnio(newValue));

	}
	
	private void showPruebasAnio(Integer anio) {
		if (anio != null) {
			months.setItems(mainApp.getBBDD().getPruebaData(anio));

			showPruebaDetails(null);

			months.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> showPruebaDetails(newValue));
		}
	}

	 
	private void showPruebaDetails(Prueba prueba) {
	        if (prueba != null) {
	            nombreLabel.setText(prueba.getNombre());
	            fechaLabel.setText(DateUtil.format(prueba.getFecha()));
	            participantesLabel.setText(Integer.toString(prueba.getParticipantes()));
	        } else {
	            nombreLabel.setText("");
	            fechaLabel.setText("");
	            participantesLabel.setText("0");
	           
	        }
	}
	 
	@FXML
	private void showPersonsPrueba() {
		Prueba selectedPrueba = months.getSelectionModel().getSelectedItem();
		int year = years.getSelectionModel().getSelectedIndex();
		int month = months.getSelectionModel().getSelectedIndex();
		
		if(selectedPrueba != null) {
			this.mainApp.showPersonsPruebaDialog(selectedPrueba);
			
			years.getSelectionModel().select(year);
			months.setItems(mainApp.getBBDD().getPruebaData(selectedPrueba.getFecha().getYear()));
			months.getSelectionModel().select(month);
		}else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Seleccion");
			a.setHeaderText("No has seleccionado ninguna prueba");
			a.setContentText("Profavor selecciona una prueba de las disponibles");
			a.showAndWait();
		}
	}
	
	@FXML
	private void handleNewPrueba() {
		 Prueba tempPrueba = new Prueba();
	        boolean okClicked = mainApp.showPruebaEditDialog(tempPrueba);
	        if (okClicked) {
	            try {
	            	mainApp.getBBDD().insertPrueba(tempPrueba);
					years.setItems(this.mainApp.getBBDD().getPruebaYears());
				} catch (SQLException e) {
					Alert a = new Alert(AlertType.WARNING);
					a.setTitle("Nueva prueba");
					a.setHeaderText("Coincidencia en la base de datos");
					a.setContentText("Ya existe una prueba en la fecha seleccionada");
					a.showAndWait();
				}
	           
	        }
	}
}
