package vista;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Person;


public class PersonEditarController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
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

        nombreField.setText(person.getNombre());
        apellidosField.setText(person.getApellidos());
        licenciaField.setText(person.getLicencia());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

   
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            person.setNombre(nombreField.getText());
            person.setApellidos(apellidosField.getText());
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

        if (nombreField.getText() == null || nombreField.getText().length() == 0) {
            errorMessage += "Nombre no valido!\n"; 
        }
        if (apellidosField.getText() == null || apellidosField.getText().length() == 0) {
            errorMessage += "Apellido no valido!\n"; 
        }
        if (licenciaField.getText() == null || licenciaField.getText().length() == 0) {
            errorMessage += "Licencia no valida!\n"; 
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