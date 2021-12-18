package simu.view;

import java.text.ParseException;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import simu.MainApp;
import simu.framework.Moottori;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.OmaMoottori;
import simu.model.SimulaattorinParametrit;

/**
 * Ikkuna uuden simulaattorin luomista varten, jossa käyttäjä määrittelee
 * toivomansa parametrit
 * 
 * @author Sanna Kukkonen
 */
public class UusiSimuIkkunaOhjain {

	/**
	 * Ikkunan GridPane -näkymä, joka tarvitaan tässä ohjaimen oman FXML-tiedostosta
	 * huolimatta, jotta nopeusSliderin saa alustettua. Muut ikkunan elementit
	 * asetellaan SceneBuilderillä
	 */
	@FXML
	private GridPane gridPane;

	/**
	 * Tekstirivi, joka printtaa virheilmoituksen. Näkyvillä vasta, kun tarpeen
	 */
	@FXML
	private Label virheIlmoitus;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää simulaation kokonaisajan
	 */
	@FXML
	private TextField simulaatioAika;

	/**
	 * ComboBox, jossa käyttäjä määrittelee simulaattorin tehtaan koneiden
	 * luotettavuuden asteikolta 1-5 (tämä vaikuttaa ajon aikana rikkimenevien
	 * tuotteiden määrään)
	 */
	@FXML
	private ComboBox<String> luotettavuusCB;

	/**
	 * Spinner, johon käyttäjä syöttää ajon aikana erikoiskalibroitavien tuotteiden
	 * prosentuaalisen määrän (0-100)
	 */
	private Spinner<Double> erikoiskalibSpinner;

	/**
	 * Slider, eli liukuvalikko, jossa käyttäjä määrittelee käyttäjän määrittelemien
	 * koneiston palveluaikoijen kertoimen asteikoilta 0.5-2. Tällöin saa nopeasti
	 * muokattua palveluajat esimerkiksi kaksinkertaisiksi tai vastaavasti puolet
	 * hitaammiksi
	 */
	private Slider kerroinSlider;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää kasauspisteen keskimääräisen palveluajan
	 */
	@FXML
	private TextField kasausaikaTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää 1. testauspisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField test1aikaTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää 2. testauspisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField test2aikaTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää pakkauspisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField pakkausTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää laaduntarkastuspisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField laaduntarkastusTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää korjauspisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField korjausTF;

	/**
	 * Tekstikenttä, johon käyttäjä syöttää erikoiskalibrointipisteen keskimääräisen
	 * palveluajan
	 */
	@FXML
	private TextField erikoiskalibTF;

	/**
	 * Nappi, joka käynnistää simulaattorin. Käyttäjän syöttämät parametrit tästä
	 * näkymästä menevät SimulaattorinParametrit -singletonin kautta 'omille
	 * paikoilleen' ja simulaattori ajaa arvot niiden mukaan.
	 */
	@FXML
	private Button ajaNappi;

	/**
	 * Simulaattorin sovellus (javafx:n MainApp)
	 */
	private MainApp mainApp;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti kasauspisteen keskimääräinen
	 * palveluaika, jota käyttäjä voi muuttaa kerroinSliderin määrittämän kertoimen
	 * arvosta huolimatta milloin vain. Muuttujan avulla tämän keskimääräisen
	 * palveluajan tekstikenttä näyttää sulavasti kerroinSliderin aiheuttamat
	 * muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka näkyy
	 * tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenKasausaika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti 1. testauspisteen keskimääräinen
	 * palveluaika, jota käyttäjä voi muuttaa kerroinSliderin määrittämän kertoimen
	 * arvosta huolimatta milloin vain. Muuttujan avulla tämän keskimääräisen
	 * palveluajan tekstikenttä näyttää sulavasti kerroinSliderin aiheuttamat
	 * muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka näkyy
	 * tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenTest1aika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti 2. testauspisteen keskimääräinen
	 * palveluaika, jota käyttäjä voi muuttaa kerroinSliderin määrittämän kertoimen
	 * arvosta huolimatta milloin vain. Muuttujan avulla tämän keskimääräisen
	 * palveluajan tekstikenttä näyttää sulavasti kerroinSliderin aiheuttamat
	 * muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka näkyy
	 * tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenTest2aika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti pakkauspisteen keskimääräinen
	 * palveluaika, jota käyttäjä voi muuttaa kerroinSliderin määrittämän kertoimen
	 * arvosta huolimatta milloin vain. Muuttujan avulla tämän keskimääräisen
	 * palveluajan tekstikenttä näyttää sulavasti kerroinSliderin aiheuttamat
	 * muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka näkyy
	 * tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenPakkausaika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti laaduntarastuspisteen
	 * keskimääräinen palveluaika, jota käyttäjä voi muuttaa kerroinSliderin
	 * määrittämän kertoimen arvosta huolimatta milloin vain. Muuttujan avulla tämän
	 * keskimääräisen palveluajan tekstikenttä näyttää sulavasti kerroinSliderin
	 * aiheuttamat muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka
	 * näkyy tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenLaaduntarkastusaika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti korjauspisteen keskimääräinen
	 * palveluaika, jota käyttäjä voi muuttaa kerroinSliderin määrittämän kertoimen
	 * arvosta huolimatta milloin vain. Muuttujan avulla tämän keskimääräisen
	 * palveluajan tekstikenttä näyttää sulavasti kerroinSliderin aiheuttamat
	 * muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka näkyy
	 * tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenKorjausaika;

	/**
	 * Double-muuttuja, jota tarvitaan kerroinSliderin visuaaliseen toimintaan.
	 * Tähän muuttujaan tallennetaan väliaikaisesti erikoiskalibrointipisteen
	 * keskimääräinen palveluaika, jota käyttäjä voi muuttaa kerroinSliderin
	 * määrittämän kertoimen arvosta huolimatta milloin vain. Muuttujan avulla tämän
	 * keskimääräisen palveluajan tekstikenttä näyttää sulavasti kerroinSliderin
	 * aiheuttamat muutokset käyttäjälle. Lopulliseksi arvoksi koituu se arvo, joka
	 * näkyy tekstikentällä aja-nappia painaessa.
	 */
	private double valiaikainenErikoiskalibaika;

	/**
	 * Metodi asettaa ikkunalle polun javafx:n pääsovellukselle (MainApp)
	 * 
	 * @param mainApp JavaFX:n MainApp (pääsovellus)
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Alustaa tämän ohjainluokan ja hakee oletusarvot SimulaattorinParametrit
	 * -singletonista. Metodia kutsutaan automaattisesti heti, kun fxml-tiedosto on
	 * ladattu.
	 */
	@FXML
	private void initialize() {
		
		virheIlmoitus.setVisible(false);
		virheIlmoitus.setTextFill(Color.RED);

		luotettavuusCB.getItems().add("1 - very unreliable");
		luotettavuusCB.getItems().add("2");
		luotettavuusCB.getItems().add("3 - normal");
		luotettavuusCB.getItems().add("4");
		luotettavuusCB.getItems().add("5 - very reliable");
		luotettavuusCB.getSelectionModel().selectLast(); // oletusvalintana maksimi

		DoubleSpinnerValueFactory valueFactory = new DoubleSpinnerValueFactory(0, 100);
		erikoiskalibSpinner = new Spinner<Double>(0, 100, 20.0);
		erikoiskalibSpinner.setValueFactory(valueFactory);
		valueFactory.setAmountToStepBy(0.1);
		erikoiskalibSpinner.setEditable(true);
		erikoiskalibSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> {
			spinnerinMetodi(observableValue, oldValue, newValue);
		});

		kerroinSlider = new Slider(0.5, 2, 1); // oletusvalinta 1
		kerroinSlider.setMajorTickUnit(0.5);
		kerroinSlider.setMinorTickCount(4);
		kerroinSlider.setShowTickMarks(true);
		kerroinSlider.setShowTickLabels(true);
		kerroinSlider.setSnapToTicks(true);

		kasausaikaTF.setOnKeyTyped(e -> {
			try {
				valiaikainenKasausaika = Double.parseDouble(kasausaikaTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		test1aikaTF.setOnKeyTyped(e -> {
			try {
				valiaikainenTest1aika = Double.parseDouble(test1aikaTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		test2aikaTF.setOnKeyTyped(e -> {
			try {
				valiaikainenTest2aika = Double.parseDouble(test2aikaTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		pakkausTF.setOnKeyTyped(e -> {
			try {
				valiaikainenPakkausaika = Double.parseDouble(pakkausTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		laaduntarkastusTF.setOnKeyTyped(e -> {
			try {
				valiaikainenLaaduntarkastusaika = Double.parseDouble(laaduntarkastusTF.getText())
						/ kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		korjausTF.setOnKeyTyped(e -> {
			try {
				valiaikainenKorjausaika = Double.parseDouble(korjausTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		erikoiskalibTF.setOnKeyTyped(e -> {
			try {
				valiaikainenErikoiskalibaika = Double.parseDouble(erikoiskalibTF.getText()) / kerroinSlider.getValue();
			} catch (NumberFormatException x) {

			}
		});

		kerroinSlider.setOnMouseClicked(event -> {
			asetaKerroin();
		});

		gridPane.add(kerroinSlider, 1, 4);
		gridPane.add(erikoiskalibSpinner, 1, 2);

		defaultNapinPainallus();
	}

	/**
	 * Haetaan & siirretään käyttöliittymästä poimitut parametrit talteen
	 * SimulaattorinParametrit -singletoniin. Jos käyttäjä jättää jonkin parametrin
	 * syöttämättä, niin ajo ajetaan parametrin oletusarvolla, joka on määritetty
	 * SimulaattorinParametrit -singletonissa
	 * 
	 * @return true, jos käyttäjän syöttämät arvot kelpaavat simulaattorin
	 *         käynnistämiseen
	 */
	public boolean saaAjaa() {
		SimulaattorinParametrit s = SimulaattorinParametrit.getInstance();
		try {
			if (simulaatioAika.getText() != null || simulaatioAika.getText().length() != 0)
				s.setSimulaatioAikaTunteina(Double.parseDouble(simulaatioAika.getText()));

			switch (luotettavuusCB.getSelectionModel().getSelectedIndex()) {
			case 0:
				s.setKoneistonLuotettavuus(90);
				break;
			case 1:
				s.setKoneistonLuotettavuus(70);
				break;
			case 2:
				s.setKoneistonLuotettavuus(50);
				break;
			case 3:
				s.setKoneistonLuotettavuus(30);
				break;
			case 4:
				s.setKoneistonLuotettavuus(10);
				break;
			}

			if (kasausaikaTF.getText() != null || kasausaikaTF.getText().length() != 0)
				s.setKasauspisteenKMAika(Double.parseDouble(kasausaikaTF.getText()));

			if (test1aikaTF.getText() != null || test1aikaTF.getText().length() != 0)
				s.setTest1KMAika(Double.parseDouble(test1aikaTF.getText()));

			if (test2aikaTF.getText() != null || test2aikaTF.getText().length() != 0)
				s.setTest2KMAika(Double.parseDouble(test2aikaTF.getText()));

			if (pakkausTF.getText() != null || pakkausTF.getText().length() != 0)
				s.setPakkauspisteenKMAika(Double.parseDouble(pakkausTF.getText()));

			if (laaduntarkastusTF.getText() != null || laaduntarkastusTF.getText().length() != 0)
				s.setLaaduntarkKMAika(Double.parseDouble(laaduntarkastusTF.getText()));

			if (korjausTF.getText() != null || korjausTF.getText().length() != 0)
				s.setKorjauspisteenKMAika(Double.parseDouble(korjausTF.getText()));

			if (erikoiskalibTF.getText() != null || erikoiskalibTF.getText().length() != 0)
				s.setErikoiskalibKMAika(Double.parseDouble(erikoiskalibTF.getText()));

			s.setErikoiskalibroitavienMäärä(erikoiskalibSpinner.getValue());

			return true;

		} catch (Exception e) {
			virheIlmoitus.setText("Please make sure the parameters are in the right format (digits)");
			virheIlmoitus.setVisible(true);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Aja-napin kutsuma metodi, kun sitä on painettu. Käynnistää simulaattorin
	 */
	@FXML
	private void ajaNapinPainallus() {
		if (saaAjaa() == true) {
			Trace.setTraceLevel(Level.INFO);
			Stage stage = (Stage) ajaNappi.getScene().getWindow();
			stage.close();
			mainApp.uusiSimulaatio();
		}
	}

	/**
	 * Palauttaa parametrit takaisin oletusarvoihin ja nämä myös käyttäjän
	 * nähtäviksi. Defalut-napin lisäksi initialize() -metodi kutsuu tätä metodia
	 */
	@FXML
	private void defaultNapinPainallus() {
		SimulaattorinParametrit s = SimulaattorinParametrit.getInstance();

		s.setOletusarvot();
		simulaatioAika.setText("" + s.getSimulaatioaikaTunteina());
		luotettavuusCB.getSelectionModel().selectLast();

		erikoiskalibSpinner.getValueFactory().setValue(s.getErikoiskalibroitavienMäärä());

		kerroinSlider.setValue(1);

		kasausaikaTF.setText("" + s.getKasauspisteenKMAika());
		test1aikaTF.setText("" + s.getTest1KMAika());
		test2aikaTF.setText("" + s.getTest2KMAika());
		pakkausTF.setText("" + s.getPakkauspisteenKMAika());
		laaduntarkastusTF.setText("" + s.getLaaduntarkKMAika());
		korjausTF.setText("" + s.getKorjauspisteenKMAika());
		erikoiskalibTF.setText("" + s.getErikoiskalibKMAika());

		valiaikainenKasausaika = s.getKasauspisteenKMAika();
		valiaikainenTest1aika = s.getTest1KMAika();
		valiaikainenTest2aika = s.getTest2KMAika();
		valiaikainenPakkausaika = s.getPakkauspisteenKMAika();
		valiaikainenLaaduntarkastusaika = s.getLaaduntarkKMAika();
		valiaikainenKorjausaika = s.getKorjauspisteenKMAika();
		valiaikainenErikoiskalibaika = s.getErikoiskalibKMAika();
	}

	/**
	 * Asettaa kerroinSliderin määrittämän kertoimen aiheuttamat muutokset
	 * palvelupisteiden keskimääräisiin palveluaikoihin käyttäjän
	 * nähtäviksi/käytettäviksi.
	 */
	public void asetaKerroin() {

		kasausaikaTF.setText("" + valiaikainenKasausaika * kerroinSlider.getValue());
		test1aikaTF.setText("" + valiaikainenTest1aika * kerroinSlider.getValue());
		test2aikaTF.setText("" + valiaikainenTest2aika * kerroinSlider.getValue());
		pakkausTF.setText("" + valiaikainenPakkausaika * kerroinSlider.getValue());
		laaduntarkastusTF.setText("" + valiaikainenLaaduntarkastusaika * kerroinSlider.getValue());
		korjausTF.setText("" + valiaikainenKorjausaika * kerroinSlider.getValue());
		erikoiskalibTF.setText("" + valiaikainenErikoiskalibaika * kerroinSlider.getValue());

	}

	/**
	 * Metodi, jonka erikoiskalibSpinneri kutsuu käsitelläkseen käyttäjän
	 * mahdollisesti syöttämiä arvoja
	 * 
	 * @param observableValue tarkasteltava spinnerin arvo
	 * @param oldValue        spinnerin edellinen (vanha) arvo
	 * @param newValue        spinnerin uusi (/käyttäjän syöttämä) arvo
	 */
	private void spinnerinMetodi(ObservableValue<?> observableValue, Number oldValue, Number newValue) {
		if (newValue == null)
			erikoiskalibSpinner.getValueFactory().setValue((double) oldValue);
	}
}
