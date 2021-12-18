package simu;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import simu.model.PalvelupisteTyyppi;
import simu.model.SimulaattorinParametrit;
import simu.model.SimulaattorinSuureet;
import simu.framework.Moottori;
import simu.model.AsetuksienOhjain;
import simu.model.ISuureetDAO;
import simu.model.OmaMoottori;
import simu.model.SuureetDAO;
import simu.model.TapahtumaInfo;
import simu.model.TiedostonOhjain;
import simu.view.RootLayoutOhjain;
import simu.view.SimulaattorinIkkunaOhjain;
import simu.view.SuureetOhjain;
import simu.view.TietokantaIkkunaOhjain;
import simu.view.UusiSimuIkkunaOhjain;

/**
 * Pääluokka josta ohjataan simulaattoria ja siihen liittyviä ikkunoita 
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */
public class MainApp extends Application {
	/**
	 * Pääikkunan näyttämö
	 */
	private Stage primaryStage;
	/**
	 * Pääikkunan pohjapiirustukset (Menu valikko)
	 */
	private BorderPane rootLayout;
	/**
	 * Simulaattorin tapahtumat
	 */
	private ArrayList<TapahtumaInfo> tapahtumat;
	/**
	 * TiedostonOhjain jolla ladataan simulaattorin tapahtumat
	 */
	private TiedostonOhjain tiedostonOhjain = new TiedostonOhjain();
	/**
	 * Pääikkunan ohjain
	 */
	private SimulaattorinIkkunaOhjain simulaattoriIkunanOhjain;
	/**
	 * TietokantaIkkunanOhjain joka ohjaa tietokannan ikkunaa
	 */
	private TietokantaIkkunaOhjain tietokantaController;
	/**
	 * SuureOhjain ohjaa suure ikkunaa
	 */
	private SuureetOhjain suureOhjain;
	/**
	 * suureIkkunan näyttämö
	 */
	private Stage suureStage;
	/**
	 * latausIkkunan näyttämö
	 */
	private Stage latausStage;
	/**
	 * uusiSimuIkkunan näyttämö
	 */
	private Stage uusiSimuIkkunaStage;
	/**
	 * tietokannan näyttämö
	 */
	private Stage tietokantaStage;
	/**
	 * YhdistaKantaan näyttämö
	 */
	private Stage kantaStage;
	/**
	 * Onko tapahtumat ladattu toisessa threadissa
	 */
	private boolean tapahtumatLadattu = false;
	/**
	 * Käynnistä JavaFX ikkunat
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		this.primaryStage.getIcons().add(new Image("file:img/logo.png"));
		
		this.primaryStage.setTitle("Ryhmä 8");
		// Luodaan animaatioTimer jotta voidaan tutkia onko threadi ladannut meille uudet tapahtumat
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(tapahtumatLadattu) {
					animaationAlustus();
					tapahtumatLadattu = false;
					latausStage.close();
				}
			}
		}.start();
		initRootLayout();
		showSimulaattoriIkkuna();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					Platform.exit();
					//System.exit(0);
				}
			});
			// Set the person into the controller.
			RootLayoutOhjain controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Luodaan tai näytetään SimulaattoriIkkuna, jos on jo valmiiksi luotu
	 */
	public void showSimulaattoriIkkuna() {
		try {
			// Load SimulaattoriIkkuna
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SimulaattorinIkkuna.fxml"));
			AnchorPane simuIkkuna = (AnchorPane) loader.load();

			// Set simuIkkuna into the center of root layout.
			rootLayout.setCenter(simuIkkuna);
			simulaattoriIkunanOhjain = loader.getController();
			simulaattoriIkunanOhjain.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Luodaan tai näytetään latausIkkuna, jos on jo valmiiksi luotu
	 */
	public void naytaLatausIkkuna() {
		if (latausStage == null) {
			try {
				// Load the fxml file and create a new stage for the popup dialog.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/latausIkkuna.fxml"));
				AnchorPane page = (AnchorPane) loader.load();

				// Create the dialog Stage.
				latausStage = new Stage();

				latausStage.getIcons().add(new Image("file:img/logo.png"));
				latausStage.setTitle("Loading...");
				latausStage.initModality(Modality.WINDOW_MODAL);
				latausStage.initOwner(primaryStage);
				Scene scene = new Scene(page);
				latausStage.setScene(scene);
				latausStage.setResizable(false);
				latausStage.initStyle(StageStyle.UTILITY);
				latausStage.setOnCloseRequest(event -> {
	                event.consume();
	            });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		latausStage.requestFocus();
		latausStage.show();
	}
	/**
	 * Luodaan tai näytetään SuureetIkkuna, jos on jo valmiiksi luotu
	 */
	public void naytaSuureetIkkuna() {
		if (suureStage == null) {
			try {
				// Load the fxml file and create a new stage for the popup dialog.
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/SuureetIkkuna.fxml"));
				AnchorPane page = (AnchorPane) loader.load();

				// Create the dialog Stage.
				suureStage = new Stage();

				suureStage.getIcons().add(new Image("file:img/logo.png"));
				suureStage.setTitle("Quantity");
				suureStage.initModality(Modality.WINDOW_MODAL);
				// dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(page);
				suureStage.setScene(scene);
				suureOhjain = loader.getController();
				suureOhjain.setMainApp(this);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		suureStage.requestFocus();
		suureStage.show();
	}

	/**
	 * Luodaan tai näytetään UusiSimuIkkuna, jos on jo valmiiksi luotu
	 */
	public void showUusiSimuIkkuna() {
		if (uusiSimuIkkunaStage == null) {
			try {
				// Load SimulaattoriIkkuna
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/UusiSimuIkkuna.fxml"));
				AnchorPane uusiSimuIkkuna = (AnchorPane) loader.load();

				// Create the dialog Stage.
				uusiSimuIkkunaStage = new Stage();
				uusiSimuIkkunaStage.getIcons().add(new Image("file:img/logo.png"));
				uusiSimuIkkunaStage.setTitle("New Simulation");
				uusiSimuIkkunaStage.initModality(Modality.WINDOW_MODAL);
				// dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(uusiSimuIkkuna);
				uusiSimuIkkunaStage.setScene(scene);
				uusiSimuIkkunaStage.setResizable(false);

				UusiSimuIkkunaOhjain controller = loader.getController();
				controller.setMainApp(this);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		uusiSimuIkkunaStage.requestFocus();
		uusiSimuIkkunaStage.show();
	}

	/**
	 * Luodaan tai näytetään TietokantaIkkuna, jos on jo valmiiksi luotu
	 */
	public void naytaTietokantaIkkuna() {
		if (tietokantaStage == null) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/TietokantaIkkuna.fxml"));
				AnchorPane tietokantaIkkuna = (AnchorPane) loader.load();

				tietokantaStage = new Stage();

				tietokantaStage.getIcons().add(new Image("file:img/logo.png"));
				tietokantaStage.setTitle("Database Actions");
				tietokantaStage.initModality(Modality.WINDOW_MODAL);

				Scene scene = new Scene(tietokantaIkkuna);
				tietokantaStage.setScene(scene);

				tietokantaController = loader.getController();
				tietokantaController.setMainApp(this);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		tietokantaController.initialize();
		tietokantaStage.requestFocus();
		tietokantaStage.show();
	}

	/**
	 * Luodaan tai näytetään YhdistaKantaanIkkuna, jos on jo valmiiksi luotu
	 */
	public void naytaYhdistaKantaanIkkuna() {
		if (kantaStage == null) {
			try {
				// Load SimulaattoriIkkuna
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("view/YhdistaKantaanIkkuna.fxml"));
				AnchorPane kantaIkkuna = (AnchorPane) loader.load();

				// Create the dialog Stage.
				kantaStage = new Stage();
				kantaStage.getIcons().add(new Image("file:img/logo.png"));
				kantaStage.setTitle("Connect to database");
				kantaStage.initModality(Modality.WINDOW_MODAL);
				// dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(kantaIkkuna);
				kantaStage.setScene(scene);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		kantaStage.requestFocus();
		kantaStage.show();
	}
	/**
	 * Alusta animaatio simulaattoriIkkunaan
	 */
	public void animaationAlustus() {
		simulaattoriIkunanOhjain.setTapahtumat(tapahtumat);
		simulaattoriIkunanOhjain.alustaAnimaatio();
		paivitaSuureIkkuna();
	}

	/**
	 * Päivitä SuureetIkkuna
	 */
	public void paivitaSuureIkkuna() {
		if(suureStage != null) {
			if (suureStage.isShowing()) {
				suureOhjain.paivataSuureet();
			}
		}
	}
	/**
	 * Luo uusi simulaatio SimulaattorinParametrit singletonin mukaan ja käynnistää threadin
	 */
	public void uusiSimulaatio() {
		naytaLatausIkkuna();
		Moottori m = new OmaMoottori();
		m.setSimulointiaika(SimulaattorinParametrit.getInstance().getSimulaatioaikaMinuutteina());
		System.out.println("saatana"); 
		((OmaMoottori)m).setMainApp(this);
		Thread thread1 = new Thread((OmaMoottori)m);
		thread1.start();
	}
	/**
	 * Lataa uusimman simulaation tiedostosta ja ilmoittaa MainApp animationTimerille että tapahtumat on ladattu
	 */
	public void lataaUusiSimulaatio() {
		tapahtumat = tiedostonOhjain.lataa();
		tapahtumatLadattu = true;
	}
	/**
	 * Lataa vanha simulaatio tiedosto polusta ja nimellä
	 * @param file polku missä on ladattava tiedosto
	 */
	private void lataaVanhaSimulaatio(File file) {
		tapahtumat = tiedostonOhjain.lataa(file.getPath());
		tapahtumatLadattu = true;
	}
	/**
	 * Avaa loppu raportin suureet ikkunaan
	 */
	public void avaaLoppuRapsa() {
		if(tapahtumat != null) {
			if(tapahtumat.size() > 0) {
				this.paivitaSuureet(tapahtumat.size()-1);
				this.paivitaSuureIkkuna();
				this.naytaSuureetIkkuna();
			}
		}
	}
	/**
	 * Tallentaa simulaation tiedostoon
	 * @param file polku minne tallennetaan tiedosto nimellä
	 */
	private void tallennaTiedostoon(File file) {
		setFilePath(file);
		if(!tiedostonOhjain.tallenna(tapahtumat, file.getPath())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Saving");
			alert.setContentText("Saving unsuccesfull. Please make sure the path is right");
			alert.showAndWait();
		}
	}

	
	/**
	 * Palauttaa viimeisimmän tiedosto polun minne tallennettiin tai mistä ladattiin tiedosto.
	 * 
	 * @return File joka ladattiin/tallennettiin viimeksi
	 */
	private File getFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Asettaa tiedosto polun joka avataan seuraavan kerran kun yritetään tallentaa tai lataa tiedostoa
	 * 
	 * @param File joka ladattiin/tallennettiin
	 */
	private void setFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			primaryStage.setTitle("Ryhmä 8 - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			primaryStage.setTitle("Ryhmä 8");
		}
	}

	/**
	 * Avaa FileChooser:in jolla käyttäjä voi valita mistä avaa tiedoston
	 */
	@FXML
	public void handleOpen() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BIN files (*.juustokumina)",
				"*.juustokumina");
		fileChooser.getExtensionFilters().add(extFilter);
		if (getFilePath() != null) {
			String myDir = getFilePath().getParent();
			fileChooser.setInitialDirectory(new File(myDir));
		}

		// Show open file dialog
		File file = fileChooser.showOpenDialog(primaryStage);

		if (file != null) {
			naytaLatausIkkuna();
			setFilePath(file);
			new Thread(() -> {
				lataaVanhaSimulaatio(file);
			}).start();
		}
	}

	/**
	 * Avaa FileChooser:in jolla käyttäjä voi valita minne tallentaa tiedoston
	 */
	@FXML
	public void tallennaTiedostoon() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BIN files (*.juustokumina)",
				"*.juustokumina");
		fileChooser.getExtensionFilters().add(extFilter);
		if (getFilePath() != null) {
			String myDir = getFilePath().getParent();
			fileChooser.setInitialDirectory(new File(myDir));
		}
		// Show save file dialog
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".juustokumina")) {
				file = new File(file.getPath() + ".juustokumina");
			}
			tallennaTiedostoon(file);
		}
		
	}
	/**
	 * Päivitä SimulaattorinSuureet singletoniin halutun tapahtuman suureet
	 * @param tapahtuma:n indeksi jonka suureet halutaan lataa
	 */
	public void paivitaSuureet(int tapahtuma) {
		if (tapahtumat != null && !tapahtumat.isEmpty()) {
			SimulaattorinSuureet s = SimulaattorinSuureet.getInstance();
			TapahtumaInfo ti = tapahtumat.get(tapahtuma);
			for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
				// System.out.println("Tapahtuma: " + animaatioOhjain.getSeuraavatapahtuma() + "
				// CNT: " +
				// tapahtumat.get(animaatioOhjain.getSeuraavatapahtuma()).getSuureet().get(pt).get(SimulaattorinSuureet.getInstance().CNT));
				s.updateCNT(pt, ti.getSuureet().get(pt).get(s.CNT));
				s.updateTIME(pt, ti.getSuureet().get(pt).get(s.TIME));
				s.updateQ_TIME(pt, ti.getSuureet().get(pt).get(s.Q_TIME));
				s.updateAVG_TIME(pt, ti.getSuureet().get(pt).get(s.AVG_TIME));
				s.updateW_TIME(pt, ti.getSuureet().get(pt).get(s.W_TIME));
				s.updatePERF(pt, ti.getSuureet().get(pt).get(s.PERF));
				s.updateUTIL(pt, ti.getSuureet().get(pt).get(s.UTIL));
			}
			s.setLapiKeskimaarainenAika(ti.getLapimenneidenKeskiaika());
			s.setSimulaationErikoiset(ti.getErikoiset());
			s.setSimulaationSuoritusteho(ti.getSimulaationSuoritusteho());
			s.setSimulaatiossaRikkoutuneet(ti.getSimulaatiossaRikkoutuneet());
			s.setSimulaatiossaValmistuneet(ti.getSimulaatiossaValmistuneet());
			s.setSimuloinninKokonaisAika(ti.getSimuloinninKokonaisaika());
		}
	}
	
	/**
	 * Tallenna tietokantaan loppuraportti. Lataa ruudulle allertin joka kertoo onnistuiko tallentaminen
	 */
	public void tallennaTietokantaan() {

		HashMap<String, String> asetukset = new HashMap<String, String>();
		AsetuksienOhjain asetusOhjain = new AsetuksienOhjain();
        asetukset = asetusOhjain.lataa();
    	if(asetukset != null) {
        	SuureetDAO.setIPOSOITE(asetukset.get("ipOsoite"));
        	SuureetDAO.setDATABASE(asetukset.get("dbNimi"));
        	SuureetDAO.setKAYTTAJA(asetukset.get("kayttaja"));
        	SuureetDAO.setSALASANA(asetukset.get("salasana"));
    	}
		ISuureetDAO sDao = new SuureetDAO();
		paivitaSuureet(tapahtumat.size()-1);
		if(sDao.tallennaPpSuureet() && sDao.tallennaSimSuureet()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Saving");
			alert.setContentText("Saving succesfull.");
			alert.showAndWait();
		} else if (sDao.tallennaPpSuureet() && !(sDao.tallennaSimSuureet())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Saving");
			alert.setContentText("Saving unsuccesfull. Please make sure the table simulaattorinsuureet is right.");
			alert.showAndWait();
		} else if (sDao.tallennaSimSuureet() && !(sDao.tallennaPpSuureet())) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Saving");
			alert.setContentText("Saving unsuccesfull. Please make sure the table tallennetutSuureet is right.");
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Saving");
			alert.setContentText("Saving unsuccesfull. Please make sure the tables are right in the database.");
			alert.showAndWait();
		}
	}
	/**
	 * Pääohjelman sisäänkäynti
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
