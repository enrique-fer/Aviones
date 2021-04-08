package vista;

import java.time.Duration;
import java.time.LocalDate;
import controlador.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modelo.Person;
import modelo.Prueba;

public class PersonsPruebaController {
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> nombreColumn;
	@FXML
	private TableColumn<Person, String> licenciaColumn;
	@FXML
	private TableColumn<Person, String> apellidosColumn;

	private MainApp mainApp;

	private Stage dialogStage;

	private Prueba prueba;

	public PersonsPruebaController() {
	}

	@FXML
	private void initialize() {
		nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
		licenciaColumn.setCellValueFactory(cellData -> cellData.getValue().licenciaProperty());
		apellidosColumn.setCellValueFactory(cellData -> cellData.getValue().apellidosProperty());
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		personTable.setItems(this.mainApp.getBBDD().getInscritosData(prueba));
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setPrueba(Prueba prueba) {
		this.prueba = prueba;
	}
	
	@FXML
	private void signUpPrueba() {
		boolean exist = false, reg = false;
		Person tempPerson = new Person();

		if (this.prueba != null) {
			LocalDate today = LocalDate.now();
			if (Duration.between(today, prueba.getFecha()).toDays() > 2) {
				if (this.prueba.getParticipantes() <= 20) {
					boolean okClicked = this.mainApp.showLicenciaDialog(tempPerson);

					if (okClicked) {
						ObservableList<Person> personas = this.mainApp.getBBDD().getPersonData();

						for (Person person : personas) {
							if (person.getLicencia().contentEquals(tempPerson.getLicencia())) {
								exist = true;
								ObservableList<Prueba> pruebas = FXCollections.observableArrayList();
								pruebas = tempPerson.getPruebas();
								for (Prueba prueba : pruebas) {
									if ((prueba.getFecha().getMonthValue() == this.prueba.getFecha().getMonthValue())
											&& (prueba.getFecha().getYear() == this.prueba.getFecha().getYear())) {
										reg = true;
										break;
									}
								}
								if (reg) {
									Alert a = new Alert(AlertType.WARNING);
									a.setTitle("Inscripcion");
									a.setHeaderText("Registro duplicado");
									a.setContentText("Ya esta registrado en esta prueba");
									a.showAndWait();
								} else {
									this.mainApp.getBBDD().insertPruebaPerson(this.prueba, person);
									this.prueba.setParticipantes(this.prueba.getParticipantes() + 1);
									this.mainApp.getBBDD().updatePrueba(this.prueba);
									personTable.setItems(this.mainApp.getBBDD().getInscritosData(this.prueba));
								}
								break;
							}
						}

						if (!exist) {
							Alert a = new Alert(AlertType.WARNING);
							a.setTitle("Piloto");
							a.setHeaderText("Numero de licencia");
							a.setContentText("No existe la licencia indicada");
							a.showAndWait();
						}
					}
				} else {
					Alert a = new Alert(AlertType.WARNING);
					a.setTitle("Participantes");
					a.setHeaderText("Limite de partcipantes");
					a.setContentText("Se ha completado el cupo de participantes");
					a.showAndWait();
				}
			} else {
				Alert a = new Alert(AlertType.WARNING);
				a.setTitle("Plazo");
				a.setHeaderText("Fuera de plazo");
				a.setContentText("El plazo de inscripcion para esta prueba ha finalizado");
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
	public void signOutPrueba() {
		Person tempPerson = personTable.getSelectionModel().getSelectedItem();
		
		if(tempPerson != null) {
			LocalDate today = LocalDate.now();
			if (Duration.between(today, prueba.getFecha()).toDays() > 0) {
				this.mainApp.getBBDD().deletePersonPrueba(tempPerson, this.prueba);
				this.prueba.setParticipantes(this.prueba.getParticipantes() - 1);
				this.mainApp.getBBDD().updatePrueba(prueba);
				personTable.setItems(this.mainApp.getBBDD().getInscritosData(this.prueba));
			}
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Seleccion");
			a.setHeaderText("No has seleccionado ningun piloto");
			a.setContentText("Porfavor selecciona uno de los pilotos inscritos");
			a.showAndWait();
		}
	}
}
