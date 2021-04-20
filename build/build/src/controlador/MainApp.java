package controlador;

import java.io.IOException;
import bd.BBDD;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Manga;
import modelo.Person;
import modelo.Prueba;
import vista.ClasificacionOverviewController;
import vista.MangaOverviewController;
import vista.PersonEditarController;
import vista.PersonMangaController;
import vista.PersonMangaEditarController;
import vista.PersonOverviewController;
import vista.PersonsPruebaController;
import vista.PruebaEditarController;
import vista.PruebaLicenciaController;
import vista.PruebaOverviewController;
import vista.RootController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private BBDD bd = new BBDD();

    public MainApp() {}
	
	public BBDD getBBDD() {
		return this.bd;
	}
	
	//Main overviews
	public void showPersonOverview() {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/VistaPersona.fxml"));
	        AnchorPane personOverview = (AnchorPane) loader.load();

	        rootLayout.setCenter(personOverview);

	        PersonOverviewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void showPruebaOverview() {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/VistaPrueba.fxml"));
	        AnchorPane mangaOverview = (AnchorPane) loader.load();

	        rootLayout.setCenter(mangaOverview);

	        PruebaOverviewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showMangaOverview() {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/VistaManga.fxml"));
	        AnchorPane mangaOverview = (AnchorPane) loader.load();

	        rootLayout.setCenter(mangaOverview);

	        MangaOverviewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void showClasificacionesOverview() {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/VistaClasificacion.fxml"));
	        AnchorPane clasOverview = (AnchorPane) loader.load();

	        rootLayout.setCenter(clasOverview);

	        ClasificacionOverviewController controller = loader.getController();
	        controller.setMainApp(this);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	//Extra panel overviews
	public boolean showPersonEditDialog(Person person) {
	    try {
	        
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/DialogoEditarPersona.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	       
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Editar Piloto");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	       
	        PersonEditarController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPerson(person);

	        
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean showPruebaEditDialog(Prueba prueba) {
	    try {
	        
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/DialogoEditarPrueba.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	       
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Crear prueba");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	       
	        PruebaEditarController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPrueba(prueba);

	        
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean showLicenciaDialog(Person person) {
		  try {
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(MainApp.class.getResource("../vista/DialogoLicencia.fxml"));
		        AnchorPane page = (AnchorPane) loader.load();

		       
		        Stage dialogStage = new Stage();
		        dialogStage.setTitle("Inscripcion");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        dialogStage.initOwner(primaryStage);
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);

		       
		        PruebaLicenciaController controller = loader.getController();
		        controller.setDialogStage(dialogStage);
		        controller.setPerson(person);

		        
		        dialogStage.showAndWait();

		        return controller.isOkClicked();
		    } catch (IOException e) {
		        e.printStackTrace();
		        return false;
		    }
	}
	
	public void showPersonsPruebaDialog(Prueba prueba) {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/DialogoPersonsPrueba.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Ver Pilotos Inscritos");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        PersonsPruebaController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPrueba(prueba);
	        controller.setMainApp(this);
	        dialogStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
    
	public void showPersonMangaDialog(Prueba prueba, Manga manga) {
		try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/DialogoManga.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Grupo " + manga.getGrupo());
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        PersonMangaController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPrueba(prueba);
	        controller.setManga(manga);
	        controller.setMainApp(this);
	        dialogStage.showAndWait();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean showMangaEditDialog(Manga manga) {
	    try {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("../vista/DialogoEditarManga.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	       
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edita los resultados");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	       
	        PersonMangaEditarController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setManga(manga);

	        
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	//Initial state
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MainApp");

        initRootLayout();
    }
    
  
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("../vista/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            RootController controller = loader.getController();
	        controller.setMainApp(this);
            
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	public Stage getPrimaryStage() {
		return primaryStage;
	}

    public static void main(String[] args) {
        launch(args);
    }
}
