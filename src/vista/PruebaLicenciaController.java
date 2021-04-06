package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Person;

public class PruebaLicenciaController {
	
 	@FXML
    private TextField licenciaField;
 	
 	 private Stage dialogStage;
 	 private Person person;
 	 private boolean okClicked = false;
 	 
 	 @FXML
     private void initialize() {
     }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setPerson(Person person) {
        this.person = person;
        
        licenciaField.setText(person.getLicencia());
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	person.setNombre("");
        	person.setApellidos("");
            person.setLicencia(licenciaField.getText());
         
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

        if (licenciaField.getText() == null || licenciaField.getText().length() == 0) {
            errorMessage += "Licencia no valida!\n"; 
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
        	Alert a = new Alert(AlertType.WARNING);
        	a.setTitle("Campo invalido");
		    a.setHeaderText("Porfavor corrige el campo");
		    a.setContentText(errorMessage);
		    a.showAndWait();
            return false;
        }
    }
}
