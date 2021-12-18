package simu.view;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import simu.MainApp;
import simu.model.PalvelupisteTyyppi;
import simu.model.SimulaattorinSuureet;

/**
 * SuureetOhjain luokka päivittää suureIkkunaan singletonista datan oikeisiin taulukkoihin ja niiden sarakkeisiin
 * 
 * @author Leevi Koskinen, Janne Lähteenmäki
 *
 * http://tutorials.jenkov.com/javafx/tableview.html
 */

public class SuureetOhjain {
	/**
	 * FXML Taulukko johon laitetaan palvelupisteiden suureet
	 */
	@FXML
	private TableView<Map> suureetTaulukko;

	/**
	 * FXML Taulukko johon laitetaan simulaation suureet
	 */
	@FXML
	private TableView<Map> simulaatioTaulukko;
	
	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> suureetKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> kasausKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> testaus1Kolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> testaus2Kolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> pakkausKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> laaduntarkastusKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> korjausKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> erikoiskalibrointiKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> valmistuneetKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> aikaKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> rikkoutuneetKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> spesiaalitKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> tuoteLapimenoKolumni;

	/**
	 * FXML tiedoston sarake
	 */
	@FXML
	private TableColumn<Map, String> suorituskykyKolumni;

	/**
	 * MainApp luokkaan referenssi, jonka kautta kutsutaan MainAppista 
	 */
	private MainApp mainApp;

	/**
	 * EnumMappi jossa on kaikki pavelupisteiden suureet
	 */
	private EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> suureet;
	/**
	 * JavaFX ObservableList:a johon laitetaan suureiden arvot
	 */
	private ObservableList<Map<String, Object>> items;
	/**
	 * JavaFX ObservableList:a johon laitetaan suureiden arvot
	 */
	private ObservableList<Map<String, Object>> items2;
	
	/**
	 * FXML kutsuu ikkunan luonti vaiheessa initialize, jossa pävitetään suureet taulukkoihin 
	 */
	@FXML
	private void initialize() {
		this.paivataSuureet();
	}

	/**
	 * Metodi jolla päivitetään suureet taulukkoon. Suureet haetaan SimulaattorinSuureet singletonista
	 */
	public void paivataSuureet() {
		suureet = SimulaattorinSuureet.getInstance().getSuureetEnumMap();
		suureetTaulukko.getItems().clear();
		simulaatioTaulukko.getItems().clear();
		
		items = FXCollections.<Map<String, Object>>observableArrayList();
		suureetKolumni.setCellValueFactory(new MapValueFactory<>(suureetKolumni.getText()));
		kasausKolumni.setCellValueFactory(new MapValueFactory<>(kasausKolumni.getText()));
		testaus1Kolumni.setCellValueFactory(new MapValueFactory<>(testaus1Kolumni.getText()));
		testaus2Kolumni.setCellValueFactory(new MapValueFactory<>(testaus2Kolumni.getText()));
		pakkausKolumni.setCellValueFactory(new MapValueFactory<>(pakkausKolumni.getText()));
		laaduntarkastusKolumni.setCellValueFactory(new MapValueFactory<>(laaduntarkastusKolumni.getText()));
		korjausKolumni.setCellValueFactory(new MapValueFactory<>(korjausKolumni.getText()));
		erikoiskalibrointiKolumni.setCellValueFactory(new MapValueFactory<>(erikoiskalibrointiKolumni.getText()));
		for (String s : suureet.get(PalvelupisteTyyppi.KASAUSPISTE).keySet()) {
			Map<String, Object> item1 = new HashMap<>();
			item1.put(suureetKolumni.getText(), s);
			item1.put(kasausKolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.KASAUSPISTE).get(s));
			item1.put(testaus1Kolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.TESTAUSPISTE1).get(s));
			item1.put(testaus2Kolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.TESTAUSPISTE2).get(s));
			item1.put(pakkausKolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.PAKKAUSPISTE).get(s));
			item1.put(laaduntarkastusKolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).get(s));
			item1.put(korjausKolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.KORJAUSPISTE).get(s));
			item1.put(erikoiskalibrointiKolumni.getText(), "" + suureet.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).get(s));
			items.add(item1);
		}
		suureetTaulukko.getItems().addAll(items);
		items2 = FXCollections.<Map<String, Object>>observableArrayList();
		aikaKolumni.setCellValueFactory(new MapValueFactory<>(aikaKolumni.getText()));
		valmistuneetKolumni.setCellValueFactory(new MapValueFactory<>(valmistuneetKolumni.getText()));
		rikkoutuneetKolumni.setCellValueFactory(new MapValueFactory<>(rikkoutuneetKolumni.getText()));
		spesiaalitKolumni.setCellValueFactory(new MapValueFactory<>(spesiaalitKolumni.getText()));
		tuoteLapimenoKolumni.setCellValueFactory(new MapValueFactory<>(tuoteLapimenoKolumni.getText()));
		suorituskykyKolumni.setCellValueFactory(new MapValueFactory<>(suorituskykyKolumni.getText()));

		Map<String, Object> item1 = new HashMap<>();
		item1.put(aikaKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getSimuloinninKokonaisaika());
		item1.put(valmistuneetKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getSimulaatiossaValmistuneet());
		item1.put(rikkoutuneetKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getSimulaatiossaRikkoutuneet());
		item1.put(spesiaalitKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getErikoiset());
		item1.put(tuoteLapimenoKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getLapiKeskimaarainenAika());
		item1.put(suorituskykyKolumni.getText(), "" + SimulaattorinSuureet.getInstance().getSimulaationSuoritusteho());
		
		items2.add(item1);
		simulaatioTaulukko.getItems().addAll(items2);
	}
	/**
	 * Asetetaan referenssi MainApp luokkaan
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
