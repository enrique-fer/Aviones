package vista;

import java.time.LocalDate;
import controlador.MainApp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import modelo.Person;
import modelo.Prueba;
import static java.time.temporal.ChronoUnit.DAYS;

public class PersonsPruebaController {
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> nombreColumn;
	@FXML
	private TableColumn<Person, String> licenciaColumn;
	@FXML
	private TableColumn<Person, String> apellidosColumn;
	
	@FXML
	private Button signUp;
	@FXML
	private Button signOut;
	@FXML
	private Button comp;
	@FXML
	private Button list;

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

		showCompetidores();
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

		if (this.prueba != null) {  //Ha elegido prueba
			LocalDate today = LocalDate.now();
			if (DAYS.between(today, prueba.getFecha()) > 2) { //Fecha limite de inscripcion
				if (this.prueba.getParticipantes() < 20) { //Plazas libres
					boolean okClicked = this.mainApp.showLicenciaDialog(tempPerson);

					if (okClicked) {
						ObservableList<Person> personas = this.mainApp.getBBDD().getPersonData();

						for (Person person : personas) {
							if (person.getLicencia().contentEquals(tempPerson.getLicencia())) { //Existe la licencia
								exist = true;
								ObservableList<Prueba> pruebas = this.mainApp.getBBDD().getPruebaPersonData(tempPerson.getLicencia());
								for (Prueba prueba : pruebas) {
									if ((prueba.getFecha().getMonthValue() == this.prueba.getFecha().getMonthValue())
											&& (prueba.getFecha().getYear() == this.prueba.getFecha().getYear())) { //Ya esta inscrito
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
									personTable.setItems(this.mainApp.getBBDD().getPruebaPersonData(this.prueba));
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
					a.setContentText("Se ha completado el cupo de participantes\nSe te añadira a la lista de espera");
					a.showAndWait();
					
					boolean okClicked = this.mainApp.showLicenciaDialog(tempPerson);
					if (okClicked) {
						this.prueba.setLista(this.prueba.getLista() + 1);
						this.mainApp.getBBDD().insertarPruebaPersonEspera(prueba, tempPerson);
						this.mainApp.getBBDD().updatePrueba(prueba);
						a = new Alert(AlertType.WARNING);
						a.setTitle("Participantes");
						a.setHeaderText("Lista de espera");
						a.setContentText(String.format("Se encuentra en la posicion %s", this.prueba.getLista()));
						a.showAndWait();
					}
					
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
			if (DAYS.between(today, prueba.getFecha()) > 0) {
				this.mainApp.getBBDD().deletePersonPrueba(tempPerson, this.prueba);
				this.prueba.setParticipantes(this.prueba.getParticipantes() - 1);
				this.mainApp.getBBDD().updatePrueba(prueba);
				
				if (this.prueba.getLista() > 0) {
					int pos = 1;
					ObservableList<Person> persons = this.mainApp.getBBDD()
							.getPruebaPersonDataEspera(this.prueba);
					
					for (Person person : persons) {
						this.mainApp.getBBDD().updatePersonPrueba(person, this.prueba, pos);
					}
				}
				
				this.mainApp.getBBDD().updatePrueba(this.prueba);
				personTable.setItems(this.mainApp.getBBDD().getPruebaPersonData(this.prueba));
			}
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setTitle("Seleccion");
			a.setHeaderText("No has seleccionado ningun piloto");
			a.setContentText("Porfavor selecciona uno de los pilotos inscritos");
			a.showAndWait();
		}
	}
	
	@FXML
	public void showCompetidores() {
		personTable.setItems(this.mainApp.getBBDD().getPruebaPersonData(prueba));
		comp.setVisible(false);
		signUp.setVisible(true);
		signOut.setVisible(true);
		list.setVisible(true);
	}
	
	@FXML
	public void showListaEspera() {
		personTable.setItems(this.mainApp.getBBDD().getPruebaPersonDataEspera(prueba));
		comp.setVisible(true);
		signUp.setVisible(false);
		signOut.setVisible(false);
		list.setVisible(false);
	}
}
