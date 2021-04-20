package vista;

import controlador.MainApp;
import javafx.fxml.FXML;

public class RootController {
    
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
    
    @FXML
    private void mangasScene() {
    	this.mainApp.showMangaOverview();
    }
    
    @FXML
    private void clasificacionScene() {
    	this.mainApp.showClasificacionesOverview();
    }
}
