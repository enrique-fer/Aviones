package vista;

import controlador.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class RootController {
 	@FXML
    private MenuItem pruebas;
    @FXML
    private MenuItem pilotos;
    
    private MainApp mainApp;
    
    public RootController() {}
    
    public void setMainApp(MainApp mainApp) {
    	this.mainApp = mainApp;
    }
    
    @FXML
    private void personScene() {
    	this.mainApp.showPersonOverview();
    }
    
    @FXML
    private void pruebaScene() {
    	this.mainApp.showPruebaOverview();
    }
}
