package vista;

import controlador.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Manga;
import modelo.Person;
import modelo.Prueba;

public class PersonMangaController {
	
	 	@FXML
	    private TableView<Person> personTable;
	    @FXML
	    private TableColumn<Person, String> nombreColumn;
	    @FXML
	    private TableColumn<Person, String> licenciaColumn;
	    @FXML
	    private Label vueloLabel;
	    @FXML
	    private Label aterrizajeLabel;
	    @FXML
	    private Label alturaLabel;
	    @FXML
	    private Label puntuacionLabel; 
	    
	    private MainApp mainApp;
	    private Stage dialogStage;
	    private Manga manga;
	    private Prueba prueba;
	    
	    public PersonMangaController() {}
	    
	    public void setDialogStage(Stage dialogStage) {
			this.dialogStage = dialogStage;
			
		}
	    
	    public void setManga(Manga manga) {
	    	this.manga = manga;
	    }
	    
	    public void setPrueba(Prueba prueba) {
	    	this.prueba = prueba;
	    }
	    
	    public void setMainApp(MainApp mainApp) {
	    	this.mainApp = mainApp;
	    	
	    	personTable.setItems(this.mainApp.getBBDD()
	    			.getPersonMangaGrupoData(this.prueba, this.manga));
	    }
	    
	    @FXML
	    private void initialize() {
	    	 nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
	         licenciaColumn.setCellValueFactory(cellData -> cellData.getValue().licenciaProperty());
	         
	         showPersonDetails(null);

	         personTable.getSelectionModel().selectedItemProperty().addListener(
	                 (observable, oldValue, newValue) -> showPersonDetails(newValue));
	    }
	    
	    private void showPersonDetails(Person person) {
	        if (person != null) {	 
	        	this.manga = this.mainApp.getBBDD().getMangaPersonPrueba(person, this.prueba, this.manga);
	        	vueloLabel.setText(manga.getVuelo());
	        	aterrizajeLabel.setText(String.valueOf(manga.getAterrizaje()));
	        	alturaLabel.setText(String.valueOf(manga.getAltura()));
	        	puntuacionLabel.setText(String.valueOf(manga.getPuntuacion()));
	        } else {
	        	vueloLabel.setText("");
	        	aterrizajeLabel.setText("");
	        	alturaLabel.setText("");
	        	puntuacionLabel.setText("");
	           
	        }
	    }
	    
	    @FXML
	    private void handlePutInfo() {
	    	Person selP = personTable.getSelectionModel().getSelectedItem();
	    	if (selP != null) {
	    		boolean okClicked = this.mainApp.showMangaEditDialog(this.manga);
	    		
	    		if (okClicked) {
	    			this.mainApp.getBBDD().updateMangaPersonPrueba(selP, this.prueba, this.manga);
	    			showPersonDetails(selP);
	    		}
	    	} else {
	    		Alert a = new Alert(AlertType.WARNING);
			    a.setTitle("Seleccion");
			    a.setHeaderText("Pilotos");
			    a.setContentText("Por favor selecciona un piloto");
			    a.showAndWait();
	    	}
	    }
}
