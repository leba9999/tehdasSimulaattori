package simu.view;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.fxml.FXML;
import simu.MainApp;

/**
 * RootLayout.fxml ohjaa RootLayotOhjaimen kautta ohjelmaa
 *   
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteennäki
 *
 */

public class RootLayoutOhjain {
	/**
	 * referenssi MainApp luokkaan jonka kautta voidaan kutsua metodeja MainApistä
	 */
    private MainApp mainApp;
    /**
     * Asetetaan pääohjelman luokka RootLayoutOhjaimelle niin voidaan kutsua MainAppistä metodeja
     * @param mainApp luokka
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla näytetään suureIkkuna 
     */
    @FXML
    private void naytaJanne() {
	      mainApp.naytaSuureetIkkuna();
	}
    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla näytetään UusiSimuIkkuna 
     */
    @FXML
    private void naytaUusi() {
	      mainApp.showUusiSimuIkkuna();
	}
    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla näytetään tietokannan toiminnot
     */
    @FXML
    private void naytaTietokanta() {
		mainApp.naytaTietokantaIkkuna();
	}
    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin joka hoitaa tiedoston avaamisen 
     */
    @FXML
    private void avaaNappi() {
        mainApp.handleOpen();
    }

    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla tallennetaan tiedostoon
     */
    @FXML
    private void tallennaTiedostoonNappi() {
    	mainApp.tallennaTiedostoon();
	}
    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla näytetään YhdistaKaantaanIkkuna
     */
    @FXML
    private void yhdistaTietokantaan() {
    	mainApp.naytaYhdistaKantaanIkkuna();
	}

    /**
     * Metodi jota kutsutaan FXML tiedostosta. Kutsuu MainAppista metodin jolla tallennetaan tietokantaan
     */
    @FXML
    private void tallennaTietokantaan() {
    	mainApp.tallennaTietokantaan();
    }

    /**
     * Metodilla avataan Help sivu hyperlinkin avulla internet selaimeen
     */
    @FXML
    private void avaaHelpSivu() {
    	try {
    	    Desktop.getDesktop().browse(new URL("https://users.metropolia.fi/~sannku/FactorySimulatorAboutSite/helpsivu/").toURI());
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} catch (URISyntaxException e) {
    	    e.printStackTrace();
    	}
    }
    /**
     * Metodilla näytetään simulaation loppu raportti kutsumalla MainAppista metodia joka näyttää suure Ikkunan ja siirtää animaation viimeiseen tapahtumaan
     */
    @FXML
    private void naytaLoppuRapsa() {
    	mainApp.avaaLoppuRapsa();
    }

    /**
     * Metodi jota kutsutaan FXML tiedostosta. Lopettaa ohjelman
     */
    @FXML
    private void lopetaOhjelma() {
    	System.exit(0);
	}

}
