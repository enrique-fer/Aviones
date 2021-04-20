package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Manga;

public class PersonMangaEditarController {

	 	@FXML
	    private TextField vueloField;
	    @FXML
	    private TextField aterrizajeField;
	    @FXML
	    private TextField alturaField;


	    private Stage dialogStage;
	    private Manga manga;
	    private boolean okClicked = false;
	    
	    @FXML
	    private void initialize() {
	    }

	    public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	    
	    public void setManga(Manga manga) {
	        this.manga = manga;

	        vueloField.setText(manga.getVuelo());
	        aterrizajeField.setText(String.valueOf(manga.getAterrizaje()));
	        alturaField.setText(String.valueOf(manga.getAltura()));
	    }

	    public boolean isOkClicked() {
	        return okClicked;
	    }

	    @FXML
	    private void handleOk() {
	        if (isInputValid()) {
	            manga.setVuelo(vueloField.getText());
	            manga.setAterrizaje(Integer.parseInt(aterrizajeField.getText()));
	            manga.setAltura(Integer.parseInt(alturaField.getText()));
	         
	            okClicked = true;
	            dialogStage.close();
	        }
	    }
	    
	    @FXML
	    private void handleCancel() {
	        dialogStage.close();
	    }
	    
	    private boolean isInputValid() {
	        String errorMessage = "";

	        if (vueloField.getText() == null || vueloField.getText().length() == 0) {
	            errorMessage += "Vuelo no valido!\n"; 
	        }
	        if (aterrizajeField.getText() == null || aterrizajeField.getText().length() == 0) {
	            errorMessage += "Aterrizaje no valido!\n"; 
	        }
	        if (alturaField.getText() == null || alturaField.getText().length() == 0) {
	            errorMessage += "Altura no valida!\n"; 
	        }

	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            // Show the error message.
	        	Alert a = new Alert(AlertType.WARNING);
			    a.setTitle("Campos invalidos");
			    a.setHeaderText("Porfavor corrige los campos erroneos");
			    a.setContentText(errorMessage);
			    a.showAndWait();
	            return false;
	        }
	    }
}
