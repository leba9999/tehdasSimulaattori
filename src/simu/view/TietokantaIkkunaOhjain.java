package simu.view;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import simu.MainApp;
import simu.model.AsetuksienOhjain;
import simu.model.ISuureetDAO;
import simu.model.SuureetDAO;

/**
 * Ohjainluokka joka hallitsee tietokantaikkunan toimintaa
 * @author Janne Lähteenmäki
 *
 */

public class TietokantaIkkunaOhjain {

	/**
	 * Luodaan uusi SuureetDAO olio metodien käyttöönsaamiseksi
	 */
	private ISuureetDAO sDao = new SuureetDAO();

	/**
	 * Käyttöliittymä
	 */
	private MainApp mainApp;

	/**
	 * ListView, johon lisätään simulaatiot tietokannasta
	 */
	@FXML
	private ListView<String> simulaatiotList = new ListView<String>();

	/**
	 * Namiska simulaation avaamiselle tietokannasta
	 */
	@FXML
	private Button avaaBtn;

	/**
	 * Namiska simulaation poistamiselle tietokannasta
	 */
	@FXML
	private Button poistaBtn;

	/**
	 * Namiska tietokantaikkunan sulkemiselle
	 */
	@FXML
	private Button suljeBtn;

	/**
	 * Käyttöliittymän setteri
	 * 
	 * @param mainApp Käyttöliittymä
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Tietokantaikkunan alustamismetodi simulaatiolistalla
	 */
	@FXML
	public void initialize() {
		String teksti = "Simulaatio";
		
		HashMap<String, String> asetukset = new HashMap<String, String>();
		AsetuksienOhjain asetusOhjain = new AsetuksienOhjain();
        asetukset = asetusOhjain.lataa();
    	if(asetukset != null) {
        	SuureetDAO.setIPOSOITE(asetukset.get("ipOsoite"));
        	SuureetDAO.setDATABASE(asetukset.get("dbNimi"));
        	SuureetDAO.setKAYTTAJA(asetukset.get("kayttaja"));
        	SuureetDAO.setSALASANA(asetukset.get("salasana"));
    	}
    	sDao = new SuureetDAO();
		Integer[] ajot = sDao.getMontakoAjoa();
		ObservableList<String> montakoAjoa = FXCollections.observableArrayList();
		for (int i = 0; i < ajot.length; i++) {
			montakoAjoa.add(teksti + " " + ajot[i].toString());
		}
		simulaatiotList.setItems(montakoAjoa);
		
		if(simulaatiotList.getItems().isEmpty()) {
			avaaBtn.setDisable(true);
			poistaBtn.setDisable(true);
		} else {
			avaaBtn.setDisable(false);
			poistaBtn.setDisable(false);
		}
	}

	/**
	 * Metodi tietokantaikkunan sulkemiselle Annetaan suljeBtn:ille
	 */
	@FXML
	private void suljeIkkuna() {
		Stage stage = (Stage) suljeBtn.getScene().getWindow();
		stage.close();
	}

	/**
	 * Metodi simulaation poistamiselle tietokannasta ja listanäkymästä 
	 * Hakee listasta valitun simulaation ja tekee poiston 
	 * Annetaan poistaBtn:ille
	 */
	@FXML
	private void poistaSimulaatio() {
		int haettuAjo = Integer
				.parseInt(simulaatiotList.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", ""));
		simulaatiotList.getItems().remove(simulaatiotList.getSelectionModel().getSelectedIndex());
		
		sDao.poistaPpSuureita(haettuAjo);
		sDao.poistaSimSuureita(haettuAjo);
		
		if(simulaatiotList.getItems().isEmpty()) {
			avaaBtn.setDisable(true);
			poistaBtn.setDisable(true);
		}
	}

	/**
	 * Metodi simulaation datan avaamiselle suureetikkunaan
	 *  Hakee listasta valitun simulaation ja sen datan 
	 *  Avaa datan suureetikkunassa
	 */
	@FXML
	private void avaaSimulaatio() {

		int haettuAjo = Integer
				.parseInt(simulaatiotList.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", ""));
		sDao.haeSimSuureet(haettuAjo);
		sDao.haePpSuureet(haettuAjo);

		mainApp.naytaSuureetIkkuna();
	}

}
