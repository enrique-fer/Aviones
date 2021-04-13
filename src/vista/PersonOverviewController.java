package vista;

import controlador.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import modelo.Person;
import modelo.Prueba;
import util.DateUtil;

public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> nombreColumn;
    @FXML
    private TableColumn<Person, String> licenciaColumn;
    @FXML
    private ChoiceBox<Prueba> pruebas;
    @FXML
    private ChoiceBox<Integer> years;

    @FXML
    private Label nombreLabel;
    @FXML
    private Label apellidosLabel;
    @FXML
    private Label licenciaLabel;
    @FXML
    private Label nPruebaLabel; 
    @FXML
    private Label fPruebaLabel;

    // Reference to the main application.
    private MainApp mainApp;

    
    public PersonOverviewController() {
    }

   
    @FXML
    private void initialize() {
    	// Initialize the person table with the two columns.
        nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        licenciaColumn.setCellValueFactory(cellData -> cellData.getValue().licenciaProperty());
        
        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

   
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getBBDD().getPersonData());
    }
   
    private void showPersonDetails(Person person) {
        if (person != null) {
        	pruebas.getSelectionModel().clearSelection();
        	
            nombreLabel.setText(person.getNombre());
            apellidosLabel.setText(person.getApellidos());
            licenciaLabel.setText(person.getLicencia());
            
            years.setItems(this.mainApp.getBBDD().getPruebaYears());
            
            showPruebasAnio(null, null);

    		years.getSelectionModel().selectedItemProperty()
    				.addListener((observable, oldValue, newValue) -> showPruebasAnio(person, newValue));
        } else {
            nombreLabel.setText("");
            apellidosLabel.setText("");
            licenciaLabel.setText("");
           
        }
    }
    
    private void showPruebasAnio(Person person, Integer anio) {
		if (person != null && anio != null) {
			pruebas.setItems(mainApp.getBBDD().getPersonPruebaData(person, anio));

			showPruebaDetails(null);

			pruebas.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> showPruebaDetails(newValue));
		}
	}
    
    private void showPruebaDetails(Prueba prueba) {
        if (prueba != null) {
        	nPruebaLabel.setText(prueba.getNombre());
        	fPruebaLabel.setText(DateUtil.format(prueba.getFecha()));

        } else {
            nPruebaLabel.setText("");
            fPruebaLabel.setText("");           
        }
    }
    
    
    @FXML
    private void handleDeletePerson() {
    	 Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
    	    if (selectedPerson != null) {
    	        this.mainApp.getBBDD().deletePerson(selectedPerson);
    	        personTable.setItems(mainApp.getBBDD().getPersonData());
    	    } else {
    	        // Nothing selected.
    	        Alert a = new Alert(AlertType.WARNING);
    	        a.setTitle("Seleccion");
    	        a.setHeaderText("No has seleccionado ninguna persona");
    	        a.setContentText("Profavor selecciona una persona de la tabla");
    	        a.showAndWait();
    	    }
    }
    
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getBBDD().insertPerson(tempPerson);
            personTable.setItems(mainApp.getBBDD().getPersonData());
        }
    }

    
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
            	this.mainApp.getBBDD().updatePerson(selectedPerson);
                showPersonDetails(selectedPerson);
            }

        } else {
            // Nothing selected.
        	Alert a = new Alert(AlertType.WARNING);
 	        a.setTitle("Seleccion");
 	        a.setHeaderText("No has seleccionado ninguna persona");
 	        a.setContentText("Profavor selecciona una persona de la tabla");
 	        a.showAndWait();
        }
    }
}