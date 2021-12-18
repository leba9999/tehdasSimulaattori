package simu.view;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import simu.model.SuureetDAO;
import simu.model.AsetuksienOhjain;

/**
 * Luokkaa ohjataan FXML tiedostosta, joka määrittää mihin tietokantaan halutaan yhdistää.
 * Luokassa siis määritellään mihin tietokantaan DAO yhdistää
 * 
 * @author Leevi Koskinen
 *
 */

public class YhdistaKantaanOhjain {
	/**
	 * FXML tekstikenttä syötettävää ip osoitetta varten
	 */
    @FXML
    private TextField ipOsoite;
	/**
	 * FXML tekstikenttä syötettävää tietokannan nimeä varten
	 */
    @FXML
    private TextField dbNimi;
	/**
	 * FXML tekstikenttä syötettävää tietokannan käyttäjän nimeä varten
	 */
    @FXML
    private TextField kayttaja;
	/**
	 * FXML tekstikenttä syötettävää tietokannan käyttäjän salasanaa varten
	 */
    @FXML
    private PasswordField salasana;
	/**
	 * FXML tiedostoton referenssi ok nappiin
	 */
    @FXML
    private Button okNappi;
   /**
    * Asetuksien ohjain lataa tiedostosta teksti laatikoihin tiedot
    */
    private AsetuksienOhjain asetusOhjain;
    
    /**
     * FXML kutsuu initialize metodia ikkunan luonnin yhteydessä ja asetetaan kaikki valmiiksi
     */
    @FXML
    private void initialize() {
    	HashMap<String, String> asetukset = new HashMap<String, String>();
    	asetusOhjain = new AsetuksienOhjain();
        asetukset = asetusOhjain.lataa();
    	if(asetukset != null) {
        	ipOsoite.setText(asetukset.get("ipOsoite"));
        	dbNimi.setText(asetukset.get("dbNimi"));
        	kayttaja.setText(asetukset.get("kayttaja"));
        	salasana.setText(asetukset.get("salasana"));
    	} else {
        	ipOsoite.setText(SuureetDAO.getIPOSOITE());
        	dbNimi.setText(SuureetDAO.getDATABASE());
        	kayttaja.setText(SuureetDAO.getKAYTTAJA());
        	salasana.setText(SuureetDAO.getSALASANA());
    	}
    }
    /**
     * FXML kustuu kun painetaan OK nappia
     */
    @FXML
    private void okNapinPainallus() {
    	HashMap<String, String> asetukset = new HashMap<String, String>();
    	asetukset.put("ipOsoite", ipOsoite.getText());
    	asetukset.put("dbNimi", dbNimi.getText());
    	asetukset.put("kayttaja", kayttaja.getText());
    	asetukset.put("salasana", salasana.getText());
    	asetusOhjain = new AsetuksienOhjain();
    	asetusOhjain.tallenna(asetukset);
    	SuureetDAO.setIPOSOITE(ipOsoite.getText());
    	SuureetDAO.setDATABASE(dbNimi.getText());
    	SuureetDAO.setKAYTTAJA(kayttaja.getText());
    	SuureetDAO.setSALASANA(salasana.getText());
		Stage stage = (Stage) okNappi.getScene().getWindow();
		stage.close();
	}
}
