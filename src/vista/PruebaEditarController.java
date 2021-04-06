package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Person;
import modelo.Prueba;

public class PruebaEditarController {
	@FXML
    private TextField nombreField;
    @FXML
    private DatePicker fechaField;
    
    private Stage dialogStage;
    private Prueba prueba;
    private boolean okClicked = false;
    
    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public boolean isOkClicked() {
        return okClicked;
    }

    public void setPrueba(Prueba prueba) {
        this.prueba = prueba;
    }
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            prueba.setNombre(nombreField.getText());
            prueba.setFecha(fechaField.getValue());
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

        if (nombreField.getText() == null || nombreField.getText().length() == 0) {
            errorMessage += "Nombre no valido!\n"; 
        }
        if (fechaField.getValue() == null || 
        		fechaField.getValue().toString().length() == 0) {
            errorMessage += "Fecha no valido!\n"; 
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
