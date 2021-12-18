package simu.view;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import simu.MainApp;
import simu.model.TapahtumaInfo;

/**
 * Luokkaa ohjaa SimulaattorinIkkuna.fxml ja ydistää fxml toiminnalisuuden ohjelman kanssa. Luokassa myös
 * luodaan AnimaatioOhjain, joka ohjaa SimulaattorinIkkuna:ssa olevaa canvasta.
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 */

public class SimulaattorinIkkunaOhjain {

	/**
	 * FXML tiedostosta haetun BorderPane olio, johon asetetaan AnimaatioOhjaimessa kangas
	 */
	@FXML
	private BorderPane pane;
	/**
	 * FXML tiedostosta haettu nappi, jolla ohjataan animaation toistoa päälle ja pois
	 */
	@FXML
	private Button toistoNappi;
	/**
	 *  FXML tiedostosta haettu Slideri, jolla ohjataan animaation tapahtumia ja näytetään missä tapahtumassa ollaan menossa
	 */
	@FXML
	private Slider tapahtumaSlider;
	/**
	 *  FXML tiedostosta haettu VBox joka sisältäää Sliderin ja ohjausnappeja sisältävän VBoxin. Tästä VBoxista poistetaan Slideri
	 *  animaation alustuksessa ja lisätään takaisin oikeilla arvoilla
	 */
	@FXML
	private VBox vboxAnOhjain;
	/**
	 *  FXML tiedostosta haettu VBox joka sisältää kaikki animaation ohjaus napit
	 */
	@FXML
	private VBox vboxNapit;
	
	/**
	 * IAnimaatioOhjain rajapinta olio jonka metodeja käytetään.
	 */
	private IAnimaatioOhjain animaatioOhjain;
	/**
	 * Tietorakenne tapahtumista koko simulaatiosta jota hyödynnetään AnimaatioOhjaimessa
	 */
	private ArrayList<TapahtumaInfo> tapahtumat;

	/**
	 * Aika edellisestä animaation päivitys kerrasta. Pitää olla luokka muuttujana koska käytetään JavaFX:n AnimationTimer:ia, silloin se näkyy myös sille
	 * 
	 */
	private long aika;
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
	 * Metodi joka kutsutaan aina ikkunan luonnin yhteydessä ja luodaan uudet "tyhjät" animaatioOhjain ja tapahtumat lista, jottei
	 * ohjelma heitä erroreita jos painelee nappeja
	 */
	@FXML
	public void initialize() {
		animaatioOhjain = new AnimaatioOhjain();
		tapahtumat = new ArrayList<TapahtumaInfo>();
	}
	/**
	 * Alustetaan animaatio kuntoon, luomalla uusi animaatioOhjain ja AnimationTimer
	 */
	public void alustaAnimaatio() {
		animaatioOhjain = new AnimaatioOhjain(pane);
		if (tapahtumat == null || tapahtumat.size() <= 0) {
			return;
		}
		animaatioOhjain.alustaTapahtumat(tapahtumat);

		this.paivitaSuureet();
		vboxAnOhjain.getChildren().remove(tapahtumaSlider);
		tapahtumaSlider = new Slider(0, tapahtumat.size() - 1, 0); // oletusvalinta 0

		tapahtumaSlider.setShowTickMarks(true);
		tapahtumaSlider.setShowTickLabels(true);
		tapahtumaSlider.setOnMouseDragged(event -> {
			sliderinVetoValmis();
		});
		tapahtumaSlider.setOnMouseReleased(event -> {
			sliderinVetoValmis();
		});
		vboxAnOhjain.getChildren().add(tapahtumaSlider);

		animaatioOhjain.paivita();
		animaatioOhjain.piirra();
		// JavaFX:n animaationTimer joka kutsuu handle metodia joka framen päivityksen aikana.
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (animaatioOhjain.getToisto()) {
					double elapsedTime = now - aika;
					double timer = (double) elapsedTime / 1000000;
					if (timer >= animaatioOhjain.getNopeus()) {
						if (animaatioOhjain.getSuunta()) {
							animaatioOhjain.seuraavaAskel();
						} else {
							animaatioOhjain.edellinenAskel();
						}
						animaatioOhjain.paivita();
						tapahtumaSlider.setValue(animaatioOhjain.getSeuraavatapahtuma());
						paivitaSuureet();
						mainApp.paivitaSuureIkkuna();
						aika = now;
					}
				}
				animaatioOhjain.piirra();
			}
		}.start();
	}
	/**
	 * FXML tiedostosta kutsutaan toistoNappia, joka asettaa animaation toiston päälle tai pois riippuen kumpi se on.
	 */
	@FXML
	private void toistoNappi() {
		if (animaatioOhjain.getToisto()) {
			animaatioOhjain.pysayta();
			toistoNappi.setText("Play");
		} else {
			animaatioOhjain.toista();
			toistoNappi.setText("Stop");
		}
	}
	/**
	 * FXML tiedostosta kutsutaan nopeammaksiNappia, joka kutsuu IAnimaatioOhjaimen lisaaNopeutta metodia 
	 */
	@FXML
	private void nopeammaksiNappi() {
		animaatioOhjain.lisaaNopeutta();
	}

	/**
	 * FXML tiedostosta kutsutaan hitaammaksiNappi, joka kutsuu IAnimaatioOhjaimen vahennaNopeutta metodia 
	 */
	@FXML
	private void hitaammaksiNappi() {
		animaatioOhjain.vahennaNopeutta();
	}

	/**
	 * FXML tiedostosta kutsutaan askelEteenpain, joka pysäyttää animaation ja siirtää animaatiota yhden askeleen (tapahtuman) verran eteenpäin.
	 */
	@FXML
	private void askelEteenpain() {
		boolean oikeaSuunta = animaatioOhjain.getSuunta();
		animaatioOhjain.pysayta();
		toistoNappi.setText("Play");
		animaatioOhjain.seuraavaAskel();
		animaatioOhjain.paivita();
		tapahtumaSlider.setValue(animaatioOhjain.getSeuraavatapahtuma());
		this.paivitaSuureet();
		animaatioOhjain.setSuunta(oikeaSuunta);
	}

	/**
	 * FXML tiedostosta kutsutaan askelTaaksepain, joka pysäyttää animaation ja siirtää animaatiota yhden askeleen (tapahtuman) verran taaksepäin.
	 */
	@FXML
	private void askelTaaksepain() {
		boolean oikeaSuunta = animaatioOhjain.getSuunta();
		animaatioOhjain.pysayta();
		toistoNappi.setText("Play");
		animaatioOhjain.edellinenAskel();
		animaatioOhjain.paivita();
		tapahtumaSlider.setValue(animaatioOhjain.getSeuraavatapahtuma());
		this.paivitaSuureet();
		animaatioOhjain.setSuunta(oikeaSuunta);
	}

	/**
	 * FXML tiedostosta kutsutaan suunanVaihtoa, joka vaihtaa animaation toiston suuntaa eteen- tai taaksepäin riippuen kumpi se on.
	 */
	@FXML
	private void suunanVaihto() {
		if (animaatioOhjain.getSuunta()) {
			animaatioOhjain.setSuunta(false);
		} else {
			animaatioOhjain.setSuunta(true);
		}
	}

	/**
	 * FXML tiedostosta kutsutaan sliderinVetoValmis, joka vaihtaa animaation tapahtumaa sliderin arvon mukaan
	 */
	@FXML
	private void sliderinVetoValmis() {
		animaatioOhjain.pysayta();
		toistoNappi.setText("Play");
		animaatioOhjain.setSeuraavatapahtuma((int) tapahtumaSlider.getValue());
		animaatioOhjain.paivita();
		this.paivitaSuureet();
	}

	/**
	 * Päivitetään SuureIkkuna kutsumalla MainAppin paivitaSuureet ja paivitaSuureIkkuna metodeja
	 */
	private void paivitaSuureet() {
		if (tapahtumat != null && !tapahtumat.isEmpty()) {
			mainApp.paivitaSuureet(animaatioOhjain.getSeuraavatapahtuma());
			mainApp.paivitaSuureIkkuna();
		}
	}
	/**
	 * Asetetaan luokalle tapahtumat lista
	 * @param tapahtumat lista simulaation tapahtumista
	 */
	public void setTapahtumat(ArrayList<TapahtumaInfo> tapahtumat) {
		this.tapahtumat = tapahtumat;
	}
	/**
	 * FXML tiedostosta kutsutaan uusiSimulaatioNappi, joka kutsuu MainAppista showUusiSimuIkkuna metodia, jolla voidaan ajaa uusi simulaatio
	 */
	@FXML
	private void uusiSimulaatioNappi() {
		mainApp.showUusiSimuIkkuna();
	}
	/**
	 * FXML tiedostosta kutsutaan lataaSimulaatioNappi, joka kutsuu MainAppista handleOpen metodia, jolla voidaan ladata vanha simulaatio
	 */
	@FXML
	private void lataaSimulaatioNappi() {
		mainApp.handleOpen();
	}
}
