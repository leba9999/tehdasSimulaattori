package simu.view;

import java.util.ArrayList;
import java.util.EnumMap;

import javafx.fxml.FXML;
import javafx.scene.canvas.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simu.framework.Kello;
import simu.model.PalvelupisteTyyppi;
import simu.model.TapahtumaInfo;
import simu.view.drawables.TuotePallo;
import simu.view.drawables.Palvelupisteet;

/**
 * Ohjaa ja huolehtii kankaalle animaation piirtämisestä ja toistamisesta.
 * Animaatio perustetaan simulaation tapahtuma listasta aika järjestyksessä
 * 
 * @author Leevi Koskinen, Janne Lähteenmäki
 *
 */

public class AnimaatioOhjain implements IAnimaatioOhjain {
	/**
	 * Kangas olio johon piirretään animaatiota
	 */
	@FXML
	private Canvas canvas;
	/**
	 * paneeli johon asetetaan aina joka piirto vaiheessa uusi kangas
	 */
	@FXML
	private BorderPane pane;
	/**
	 * Sisältää piirrettävien palvelupisteiden oliot
	 */
	private EnumMap<PalvelupisteTyyppi, Palvelupisteet> palveluP;
	/**
	 * Sisältää piirettävät tuote ympyrät animaatiossa
	 */
	private ArrayList<TuotePallo> tuotePallurat;
	/**
	 * Simulaattorissa simuloitu tapahtumalista, josta koko animaatio perustetaan
	 */
	private ArrayList<TapahtumaInfo> tapahtumat;
	/**
	 * Tapahtuma listan tapahtuma animaatio askel
	 */
	private int seuraavaTapahtuma = 0;
	/**
	 * toisto:lla määritellään toistetaanko animaatiota. false = ei toisteta, true = animaatiota toistetaan
	 */
	private boolean toisto;
	/**
	 * Animaation toisto nopeus, eli tarkemmin päivitys nopeus. Kuinka usein animaatiota päivitetään (ms)
	 */
	private int nopeus = 500;
	/**
	 * Mihin suuntaan animaatio kulkee. false = "taaksepäin", true = "eteenpäin"
	 */
	private boolean suunta;
	
	/**
	 * Konstruktori jossa asetetaan paneeli ja alustetaan muuttujat alku arvoihin
	 * @param pane mihin kangas aseteaan
	 */
	public AnimaatioOhjain(BorderPane pane) {
		this.pane = pane;
		tuotePallurat = new ArrayList<>();
		suunta = true;
		pane.getChildren().clear();
		canvas = new Canvas(pane.getWidth(), pane.getHeight());
		pane.setCenter(canvas);
	}
	/**
	 * Konstruktori jossa ei alusteta muuta kuin pallojen (tuotePallurat) lista
	 */
	public AnimaatioOhjain() {
		tuotePallurat = new ArrayList<>();
	}
	/**
	 * Alustetaan tapahtumat, eli asetetaan halutut tapahtumat ja animaatio luodaan niiden perusteella
	 * @param tapahtumat jotka halutaan antaa animaatiolle
	 */
	@Override
	public void alustaTapahtumat(ArrayList<TapahtumaInfo> tapahtumat) {
		this.tapahtumat = tapahtumat;
		this.luoPalvelupisteet();
	}
	/**
	 * Luodaan piirrettävät palvelupisteet ja asetetaan ne haluttuihin koordinatteihin kankaalle.
	 */
	private void luoPalvelupisteet() {
		palveluP = new EnumMap<>(PalvelupisteTyyppi.class);
		for (PalvelupisteTyyppi tyyppi : PalvelupisteTyyppi.values()) {
			palveluP.put(tyyppi, new Palvelupisteet());
		}
		palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).setName("Kasauspiste");
		palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).setX(75);
		palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).setY((canvas.getHeight() / 10) + 100);

		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).setName("Testauspiste 1");
		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).setX(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getX() + 100);
		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).setY(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY() - 50);

		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).setName("Testauspiste 2");
		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).setX(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getX() + 100);
		palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).setY(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY() + 50);

		palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).setName("Erikoiskalibrointi");
		palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI)
				.setX(palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).getX() + 100);
		palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI)
				.setY(palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).getY() - 25);

		palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).setName("Pakkauspiste");
		palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE)
				.setX(palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).getX() + 100);
		palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).setY(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY());

		palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).setName("Laaduntarkastuspiste");
		palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE)
				.setX(palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).getX() + 100);
		palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE)
				.setY(palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).getY());

		palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).setName("Korjauspiste");
		palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).setX(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getX());
		palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).setY(palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY() + 100);

	}

	/**
	 * Päivitä animaatio seuraavien tapahtumien mukaiseksi, eli asetetaan tuote palloja palvelupisteille ja reitille
	 */
	@Override
	public void paivita() {
		if (tapahtumat != null) {
			if (seuraavaTapahtuma >= tapahtumat.size()) {
				seuraavaTapahtuma = 0;
			} else if (seuraavaTapahtuma < 0) {
				seuraavaTapahtuma = tapahtumat.size() - 1;
			}
			tuotePallurat.clear();
			luoPalvelupisteet();
			for (int i = 0; i < seuraavaTapahtuma; i++) {
				TuotePallo p = new TuotePallo();
				if (!tuotePallurat.isEmpty()) {
					for (int j = 0; j < tuotePallurat.size(); j++) {
						if (tuotePallurat.get(j).getId() == tapahtumat.get(i).getTuote().getId()) {
							p = tuotePallurat.get(j);
							break;
						}
					}
				}
				p.setColor(Color.GREEN);
				if (tapahtumat.get(i).getTuote().viedaakoErikoiskalibrointiin()) {
					p.setColor(Color.CYAN);
				}
				if (tapahtumat.get(i).getTuote().getOnkoRikki()) {
					p.setColor(Color.RED);
				}
				switch (tapahtumat.get(i).getTyyppi()) {
				case KPARR:
					p.setRadius(5);
					p.setId(tapahtumat.get(i).getTuote().getId());
					palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).lisaaPalloJonoon(p);
					tuotePallurat.add(p);
					break;
				case KPPA:
					palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).palvelePalloa(p);
					break;
				case KPDEP:
					palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).poistaPalvelustaPallo();
					p.setPosition(palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).getX() - 50 - p.getRadius(),
							palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY()
									+ palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getRadius() - p.getRadius());
					break;
				case TP1ARR:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).lisaaPalloJonoon(p);
					break;
				case TP1PA:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).palvelePalloa(p);
					break;
				case TP1DEP:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1).poistaPalvelustaPallo();
					p.setPosition(palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).getX() - 50 - p.getRadius(),
							palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY()
									+ palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getRadius() - p.getRadius());
					break;
				case TP2ARR:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).lisaaPalloJonoon(p);
					break;
				case TP2PA:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).palvelePalloa(p);
					break;
				case TP2DEP:
					palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2).poistaPalvelustaPallo();
					p.setPosition(palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).getX() - 50 - p.getRadius(),
							palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getY()
									+ palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getRadius() - p.getRadius());
					break;
				case EPARR:
					palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).lisaaPalloJonoon(p);
					break;
				case EPPA:
					palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).palvelePalloa(p);
					break;
				case EPDEP:
					palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).poistaPalvelustaPallo();
					break;
				case PPARR:
					palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).lisaaPalloJonoon(p);
					break;
				case PPPA:
					palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).palvelePalloa(p);
					break;
				case PPDEP:
					palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE).poistaPalvelustaPallo();
					break;
				case LPARR:
					palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).lisaaPalloJonoon(p);
					break;
				case LPPA:
					palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).palvelePalloa(p);
					break;
				case LPDEP:
					palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).poistaPalvelustaPallo();
					p.setPosition(palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).getX() + 40 - p.getRadius(),
							palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).getY() + palveluP.get(PalvelupisteTyyppi.KASAUSPISTE).getRadius() - p.getRadius());
					break;
				case KJARR:
					palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).lisaaPalloJonoon(p);
					break;
				case KJPA:
					palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).poistaPalloJonosta();
					palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).palvelePalloa(p);
					break;
				case KJPDEP:
					palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE).poistaPalvelustaPallo();
					break;
				case EX:
					tuotePallurat.remove(p);
					break;
				case AN:
					if (tapahtumat.get(i).getTuotteenPalvelupiste() != null) {
						Palvelupisteet kp = palveluP.get(PalvelupisteTyyppi.KASAUSPISTE);
						Palvelupisteet tp1 = palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1);
						Palvelupisteet tp2 = palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2);
						Palvelupisteet ep = palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI);
						Palvelupisteet pp = palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE);
						Palvelupisteet lp = palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE);
						Palvelupisteet kjp = palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE);
						switch (tapahtumat.get(i).getTuotteenPalvelupiste()) {
						case TESTAUSPISTE1:
							p.setPosition(tp1.getX() - 50 - p.getRadius(),
									tp1.getY() + tp1.getRadius() - p.getRadius());
							break;
						case TESTAUSPISTE2:
							p.setPosition(tp1.getX() - 50 - p.getRadius(),
									tp2.getY() + tp2.getRadius() - p.getRadius());
							break;
						case ERIKOISKALIBROINTI:
							p.setPosition(ep.getX() - 25 - p.getRadius(), ep.getY() + ep.getRadius() - p.getRadius());
							break;
						case PAKKAUSPISTE:
							p.setPosition(ep.getX() + ep.getRadius() * 2 + 25 - p.getRadius(),
									pp.getY() + pp.getRadius() - p.getRadius());
							break;
						case LAADUNTARKASTUSPISTE:
							p.setPosition(lp.getX() - 25 - p.getRadius(), pp.getY() + pp.getRadius() - p.getRadius());
							break;
						case KORJAUSPISTE:
							p.setPosition(ep.getX() - 50 - p.getRadius(), kjp.getY() + 50 - p.getRadius());
							break;

						}
					}
					break;
				}
			}
			Kello.getInstance().setAika(tapahtumat.get(seuraavaTapahtuma).getAika());
		}
	}
	/**
	 * Piirretään kankaalle viivat joita voi seurailla
	 */
	private void viivat() {

		GraphicsContext g = canvas.getGraphicsContext2D();
		Palvelupisteet kp = palveluP.get(PalvelupisteTyyppi.KASAUSPISTE);
		Palvelupisteet tp1 = palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE1);
		Palvelupisteet tp2 = palveluP.get(PalvelupisteTyyppi.TESTAUSPISTE2);
		Palvelupisteet ep = palveluP.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI);
		Palvelupisteet pp = palveluP.get(PalvelupisteTyyppi.PAKKAUSPISTE);
		Palvelupisteet lp = palveluP.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE);
		Palvelupisteet kjp = palveluP.get(PalvelupisteTyyppi.KORJAUSPISTE);

		g.setLineWidth(1.0);
		g.moveTo(kp.getX() - 50, kp.getY() + kp.getRadius());
		g.lineTo(tp1.getX() - 50, kp.getY() + kp.getRadius());

		g.lineTo(tp1.getX() - 50, tp1.getY() + tp1.getRadius());
		g.lineTo(ep.getX() - 50, tp1.getY() + tp1.getRadius());

		g.moveTo(tp1.getX() - 50, tp1.getY() + tp1.getRadius());
		g.lineTo(tp2.getX() - 50, tp2.getY() + tp2.getRadius());

		g.lineTo(tp2.getX() - 50, tp2.getY() + tp2.getRadius());
		g.lineTo(ep.getX() - 50, tp2.getY() + tp2.getRadius());
		g.lineTo(ep.getX() - 50, tp1.getY() + tp1.getRadius());

		g.moveTo(ep.getX() - 50, tp2.getY() + tp2.getRadius());
		g.lineTo(ep.getX() - 50, kjp.getY() + 50);

		g.moveTo(ep.getX() - 50, kp.getY() + kp.getRadius());
		g.lineTo(lp.getX() + 50, lp.getY() + lp.getRadius());
		g.moveTo(lp.getX() + 40, kp.getY() + kp.getRadius());
		g.lineTo(lp.getX() + 40, kjp.getY() + 50);
		g.lineTo(kp.getX() - 50, kjp.getY() + 50);
		g.lineTo(kp.getX() - 50, kjp.getY() + kjp.getRadius());
		g.lineTo(tp1.getX() - 50, kjp.getY() + kjp.getRadius());
		g.lineTo(tp1.getX() - 50, tp2.getY() + tp2.getRadius());

		// Erikoiskalibroinnin viivat:
		g.moveTo(ep.getX() - 25, kp.getY() + kp.getRadius());
		g.lineTo(ep.getX() - 25, ep.getY() + ep.getRadius());
		g.lineTo(ep.getX() + ep.getRadius() * 2 + 25, ep.getY() + ep.getRadius());
		g.lineTo(ep.getX() + ep.getRadius() * 2 + 25, kp.getY() + kp.getRadius());

		// Nuolien kärjet
		// Kasauspisteen
		g.moveTo(tp1.getX() - 65, kp.getY() + kp.getRadius() - 5);
		g.lineTo(tp1.getX() - 60, kp.getY() + kp.getRadius());
		g.lineTo(tp1.getX() - 65, kp.getY() + kp.getRadius() + 5);
		// Testauspiste1
		g.moveTo(tp1.getX() - 25, tp1.getY() + tp1.getRadius() - 5);
		g.lineTo(tp1.getX() - 20, tp1.getY() + tp1.getRadius());
		g.lineTo(tp1.getX() - 25, tp1.getY() + tp1.getRadius() + 5);
		// Testauspiste2
		g.moveTo(tp2.getX() - 25, tp2.getY() + tp2.getRadius() - 5);
		g.lineTo(tp2.getX() - 20, tp2.getY() + tp2.getRadius());
		g.lineTo(tp2.getX() - 25, tp2.getY() + tp2.getRadius() + 5);
		// Testauspiste1 & 2 jälkeen välissä
		g.moveTo(ep.getX() - 40, kp.getY() + kp.getRadius() - 5);
		g.lineTo(ep.getX() - 35, kp.getY() + kp.getRadius());
		g.lineTo(ep.getX() - 40, kp.getY() + kp.getRadius() + 5);
		// Eikoiskalibroinnin Ylös
		g.moveTo(ep.getX() - 30, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius() + 5);
		g.lineTo(ep.getX() - 25, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius());
		g.lineTo(ep.getX() - 20, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius() + 5);
		// alas
		g.moveTo(ep.getX() + ep.getRadius() * 2 + 30, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius() - 5);
		g.lineTo(ep.getX() + ep.getRadius() * 2 + 25, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius());
		g.lineTo(ep.getX() + ep.getRadius() * 2 + 20, ep.getY() + ((kp.getY() - ep.getY()) / 2) + kp.getRadius() - 5);
		// Pakkauspisteen
		g.moveTo(pp.getX() - 30, kp.getY() + kp.getRadius() - 5);
		g.lineTo(pp.getX() - 25, kp.getY() + kp.getRadius());
		g.lineTo(pp.getX() - 30, kp.getY() + kp.getRadius() + 5);
		// Laaduntarkastuksen
		g.moveTo(lp.getX() - 30, lp.getY() + lp.getRadius() - 5);
		g.lineTo(lp.getX() - 25, lp.getY() + lp.getRadius());
		g.lineTo(lp.getX() - 30, lp.getY() + lp.getRadius() + 5);
		// Laaduntarkastuksesta
		g.moveTo(lp.getX() + 35, lp.getY() + lp.getRadius() - 5);
		g.lineTo(lp.getX() + 40, lp.getY() + lp.getRadius());
		g.lineTo(lp.getX() + 35, lp.getY() + lp.getRadius() + 5);
		// Laaduntarkastuksesta korjauspisteelle alin
		g.moveTo(lp.getX() - 20, kjp.getY() + 50 - 5);
		g.lineTo(lp.getX() - 25, kjp.getY() + 50);
		g.lineTo(lp.getX() - 20, kjp.getY() + 50 + 5);
		// ylin
		g.moveTo(lp.getX() + 40 - 5, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2) - 5);
		g.lineTo(lp.getX() + 40, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2));
		g.lineTo(lp.getX() + 40 + 5, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2) - 5);
		// korjauspisteelle
		g.moveTo(kjp.getX() + kjp.getRadius() + 5, kjp.getY() + 50 - 5);
		g.lineTo(kjp.getX() + kjp.getRadius(), kjp.getY() + 50);
		g.lineTo(kjp.getX() + kjp.getRadius() + 5, kjp.getY() + 50 + 5);
		// Testauksista korjauspisteelle
		g.moveTo(ep.getX() - 50 - 5, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2) - 5);
		g.lineTo(ep.getX() - 50, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2));
		g.lineTo(ep.getX() - 50 + 5, kjp.getY() + 50 - (((kjp.getY() + 50) - (tp2.getY() + tp2.getRadius())) / 2) - 5);
		g.stroke();
	}

	/**
	 * Piirrä kankaalle animaatio kuva
	 */
	@Override
	public void piirra() {
		pane.getChildren().remove(canvas);
		canvas = new Canvas(canvas.getWidth(), canvas.getHeight());
		pane.setCenter(canvas);
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		this.viivat();
		// Color c = Color.web("#00aaff",1.0);// blue as a hex web value, explicit alpha
		// g.setFill(c);
		// g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (PalvelupisteTyyppi tyyppi : PalvelupisteTyyppi.values()) {
			palveluP.get(tyyppi).draw(g);
		}
		for (TuotePallo p : tuotePallurat) {
			p.draw(g);
		}

		Text theText = new Text("Kello: " + Kello.getInstance().getAika());
		double height = theText.getBoundsInLocal().getHeight();
		g.setFill(Color.BLACK);
		// g.setFont(new Font(10));
		g.fillText("Kello: " + Kello.getInstance().getAika(), 0, 0 + height);

	}

	/**
	 * Aseta animaation toisto päälle
	 */
	@Override
	public void toista() {
		toisto = true;
	}

	/**
	 * Pysäytä animaation toistaminen
	 */
	@Override
	public void pysayta() {
		toisto = false;
	}

	/**
	 * Vähennetään animaation toisto nopeutta
	 */
	@Override
	public void vahennaNopeutta() {
		nopeus += 100;
	}

	/**
	 * Lisätään animaation toisto nopeutta
	 */
	@Override
	public void lisaaNopeutta() {
		nopeus -= 100;
	}

	/**
	 * Hypätään seuraavaan animaatio askeleeseen/kuvaan
	 */
	@Override
	public void seuraavaAskel() {
		seuraavaTapahtuma++;

	}

	/**
	 * Hypätään edelliseen animaatio askeleeseen/kuvaan
	 */
	@Override
	public void edellinenAskel() {
		seuraavaTapahtuma--;
	}

	/**
	 * Metodilla saadaan selville onko animaation toistaminen käynnissä
	 * 
	 * @return Palauttaa animaation toiston
	 */
	@Override
	public boolean getToisto() {
		return toisto;
	}

	/**
	 * Metodilla saadaan selville animaation toisto nopeus
	 * 
	 * @return Palauttaa animaation nopeuden
	 */
	@Override
	public double getNopeus() {
		return nopeus;
	}
	/**
	 * Haetaan animaation toisto suunta, false = "taaksepäin", true = "eteenpäin"
	 * @return suunta johon animaatio kulkee
	 */
	@Override
	public boolean getSuunta() {
		return suunta;
	}
	/**
	 * Asetetaan animaation toisto suunta, false = "taaksepäin", true = "eteenpäin"
	 * @param suunta johon animaatio kulkee
	 */
	@Override
	public void setSuunta(boolean suunta) {
		this.suunta = suunta;
	}
	/**
	 * Haetaan seuraavan tapahtuman indeksi numero eli mikä tapahtuma pitäisi seuraavaksi tapahtua
	 * @return seuraavaTapahtuma:n indeksi numero
	 */
	@Override
	public int getSeuraavatapahtuma() {
		return seuraavaTapahtuma;
	}
	/**
	 * Asetetaan seuraavatapahtuma
	 * @param seuraava tapahtuman indeksi
	 */
	@Override
	public void setSeuraavatapahtuma(int seuraava) {
		seuraavaTapahtuma = seuraava;
	}

}
