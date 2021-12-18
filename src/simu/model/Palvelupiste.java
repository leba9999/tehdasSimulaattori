package simu.model;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Uniform;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;

/**
 * Luokka esittää simulaattorissa palvelupisteitä, joihin saapuu tuotteita
 * palveltaviksi.
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 */

public class Palvelupiste implements Comparable<Palvelupiste> {
	/**
	 * jono muuttuja sisältää tuotteita, jotka ovat jonossa palvelupisteelle.
	 * Jonosta poistuu tuote aina palvelun päätyttyä
	 */
	private LinkedList<Tuote> jono = new LinkedList<Tuote>(); // Tietorakennetoteutus
	/**
	 * rikkiMenneetTuotteet muuttuja sisältää kuinka monta tuotetta on mennyt rikki
	 * palvelupisteellä
	 */
	private static int rikkiMenneetTuotteet = 0;

	/**
	 * generator muuttuja on sattunnaisluku generaattori joka tuottaa luvun
	 * valitusta jakaumasta
	 */
	private ContinuousGenerator generator;
	/**
	 * tapahtumalistaan generoidaan uusi palvelun päättymistapahtuma
	 */
	private Tapahtumalista tapahtumalista;
	/**
	 * tapahtumalistaan generoitavan tapahtuman tyyppi
	 */
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi;
	/**
	 * pisteenTyyppi:llä tunnistetaan minkälainen palvelupiste on kyseessä
	 */
	private PalvelupisteTyyppi pisteenTyyppi;
	/**
	 * tuotteidenMaara on suure jolla saadaan selville kuinka monta tuotetta on
	 * mennyt palvelupisteen läpi
	 */
	private int tuotteidenMaara;

	/**
	 * palveluidenYhteisaika on suure josta saadaan selville kuinka kauan on
	 * palveltu eri tuotteita yhteensä
	 */
	private double palveluidenYhteisaika;
	/**
	 * palvelupisteenOleskeluaika on suure josta saadaan selville kuinka kauan
	 * yhteensä tuotteet ovat olleet palvelupisteellä. (jonotus + palveluaika)
	 */
	private double palvelupisteenOleskeluaika;
	/**
	 * varattu muuttuja kertoo onko palvelupiste vapaa palvelemaan tuotetta
	 */
	private boolean varattu = false;

	/**
	 * Palvelupisteen konstruktorilla luodaan palvelupiste
	 * 
	 * @param generator      asetetaan jakaumageneraattorin palvelupisteelle, josta
	 *                       haetaan jakaumaan perustuvat palveluajat
	 * @param tapahtumalista simulaattorin tapahtumalista johon luodaan palvelun
	 *                       päätymisen tapahtumia
	 * @param tyyppi         minkälaisia palvelun päätymisen tapahtumia luodaan
	 *                       tapahtumalistaan
	 * @param pTyyppi        palvelupisteen tyyppi josta saadaan selville minkä
	 *                       tyyppinen palvelupiste on kyseessä
	 */
	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi,
			PalvelupisteTyyppi pTyyppi) {
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.pisteenTyyppi = pTyyppi;
		rikkiMenneetTuotteet = 0;
	}

	/**
	 * lisätään tuotteita palvelupisteen jonoon
	 * 
	 * @param a Tuote joka lisätään palvelupisteen jonoon
	 */
	public void lisaaJonoon(Tuote a) { // Jonon 1. asiakas aina palvelussa
		a.setPalvelupisteelleSaapumisaika(Kello.getInstance().getAika());
		jono.add(a);
	}

	/**
	 * Poistaa tuotteen palvelupisteen jonosta ja asettaa palvelupisteen varatusta
	 * vapaaksi
	 * 
	 * @return Palauttaa mikä tuote poistettiin palvelupisteeltä
	 */
	public Tuote otaJonosta() { // Poistetaan palvelussa ollut
		varattu = false;
		Tuote t = jono.peek();
		// Asetetaan tuotteelle palvelun päätymisaika kyseiselle palvelupisteelle
		t.setPalvelupisteellePoistumisaika(Kello.getInstance().getAika(), this.pisteenTyyppi);
		// Lisätään tuotteen oleskelu aika palvelupisteessä palvelupisteen kokonais
		// oleskelu aikaan
		palvelupisteenOleskeluaika += t.getPalvelupisteenLapimenoAjat(pisteenTyyppi)
				.get(t.getPalvelupisteenLapimenoAjat(pisteenTyyppi).size() - 1);
		return jono.poll();
	}

	/**
	 * Aloittaa palvelemaan jonon ensimmäistä tuotetta
	 * 
	 * @return Palauttaa palveltavana olleen tuotteen
	 */
	public Tuote aloitaPalvelu() { // Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		Tuote t = jono.peek();
		double palveluaika = generator.sample();

		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu tuotteelle " + t.getId());

		varattu = true;
		palveluidenYhteisaika += palveluaika;
		tuotteidenMaara++;
		// Jos palvelupisteen tyyppi vastaa testauspisteiden tai lopputarkastuksen
		// tyyppiä voi laitteesta löytyä vikoja.
		// ilmoittamaan laitteelle että on viallinen/rikki
		if (pisteenTyyppi == PalvelupisteTyyppi.TESTAUSPISTE1 || pisteenTyyppi == PalvelupisteTyyppi.TESTAUSPISTE2
				|| pisteenTyyppi == PalvelupisteTyyppi.LAADUNTARKASTUSPISTE) {
			ContinuousGenerator generator = new Uniform(1, 100); // onko tää oikeessa kohtaa?
			if (generator.sample() < SimulaattorinParametrit.getInstance().getKoneistonLuotettavuus()) {
				if (t.getRikkoutumisKerrat() < 1) {
					rikkiMenneetTuotteet++;
				}
				t.setRikkoutumisKerrat(t.getRikkoutumisKerrat() + 1);
				t.setOnkoRikki(true);
				System.err.println("Tuote : " + t.getId() + " on RIKKI!!!!");
			}
		}
		// Luo tapahtumalistaan uuden palvelun päätymisen tyyppisen tapahtuman
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi, Kello.getInstance().getAika() + palveluaika));
		return t;
	}

	/**
	 * Saadaan selville palvelupisteessä rikkimenneiden tuotteiden määrä
	 * 
	 * @return palauttaa rikkimenneiden tuotteiden määrän
	 */
	public static int rikkiMenneetTuotteet() {
		return rikkiMenneetTuotteet;
	}

	/**
	 * Palauttaa läpimenneiden tuotteiden suureen
	 * 
	 * @return Palauttaa palvelupisteestä läpimenneiden tuotteiden määrän
	 */
	public int getLapimenneet() {
		return tuotteidenMaara;
	}

	/**
	 * Kertoo palvelupistessä käytetyn palvelun kokonaisajan eli aktiivi ajan (B)
	 * 
	 * @return Palauttaa tuotteiden yhteisen palvelu ajan
	 */
	public double getKokonaisPalveluAika() {
		return palveluidenYhteisaika;
	}

	/**
	 * Antaa palvelupisteen keskimääräisen jononpituuden suureen, jota voidaan
	 * tutkia
	 * 
	 * @return Palauttaa keskimääräisen jonon pituuden palvelupisteessä
	 */
	public double getKeskimaarainenJononpituus() {
		if (palvelupisteenOleskeluaika != 0 && Kello.getInstance().getAika() != 0) {
			return (palvelupisteenOleskeluaika / Kello.getInstance().getAika());
		}
		return 0;
	}

	/**
	 * Antaa palvelupisteen keskimääräisen palveluajan suureen
	 * 
	 * @return Palauttaa keskimääräisen palveluajan
	 */
	public double getKeskimaarainenPalveluAika() {
		if (palveluidenYhteisaika != 0 && tuotteidenMaara != 0) {
			return palveluidenYhteisaika / tuotteidenMaara;
		}
		return 0;
	}

	/**
	 * Antaa palvelupisteen keskimääräisen tuotteiden läpimenoajan suureen. Eli
	 * kuinka kauan tuotteet viettivät palvelupisteessä aikaa keskimääräisesti
	 * 
	 * @return Palauttaa keskimääräisen läpimeno ajan
	 */
	public double getKeskimaarainenLapimenoAika() {
		if (palveluidenYhteisaika != 0 && tuotteidenMaara != 0) {
			return palveluidenYhteisaika / tuotteidenMaara;
		}
		return 0;
	}

	/**
	 * Antaa palvelupisteen suoritustehon suureen, josta saadaan selville
	 * palvelupisteen tehokkuus
	 * 
	 * @return tuotteidenMaara / palveluidenYhteisaika
	 */
	public double getSuoritusTeho() {
		if (tuotteidenMaara != 0 && palveluidenYhteisaika != 0) {
			return tuotteidenMaara / palveluidenYhteisaika;
		}
		return 0;
	}

	/**
	 * Antaa palvelupisteen käyttöasteen suureen, josta saadaan selville kuinka
	 * suuressa käytössä palvelupiste on
	 * 
	 * @return Palauttaa käyttöasteen
	 */
	public double getKayttoaste() {
		if (palveluidenYhteisaika != 0 && Kello.getInstance().getAika() != 0) {
			return palveluidenYhteisaika / Kello.getInstance().getAika();
		}

		return 0;
	}

	/**
	 * Kertoo onko palvelupiste palvelemassa tuotetta, jottei yritetä palvella
	 * toista tuotetta ennenkuin palvelu on päättynyt
	 * 
	 * @return Palauttaa tiedon siitä onko palvelupiste käytettävissä
	 */
	public boolean onVarattu() {
		return varattu;
	}

	/**
	 * Kertoo onko palvelupisteen jonossa yhtään tuotetta
	 * 
	 * @return Palauttaa tiedon onko jonossa tuoteita/tuotetta
	 */
	public boolean onJonossa() {
		return jono.size() != 0;
	}

	/**
	 * Kertoo palvelupisteen tyypin, jotta saadaan tietää onko mikä palvelupiste
	 * kyseessä
	 * 
	 * @return Palauttaa palvelupisteen tyypin
	 */
	public PalvelupisteTyyppi getPisteenTyyppi() {
		return pisteenTyyppi;
	}

	/**
	 * Tallentaa palvelupistekohtaiset suureet SimulaattorinSuureet -singletoniin
	 * talteen
	 */
	public void suureetTalteen() {
		SimulaattorinSuureet.getInstance().updateCNT(this.pisteenTyyppi, (double) tuotteidenMaara);
		SimulaattorinSuureet.getInstance().updateTIME(this.pisteenTyyppi, (double) getKokonaisPalveluAika());
		SimulaattorinSuureet.getInstance().updateQ_TIME(this.pisteenTyyppi, (double) getKeskimaarainenJononpituus());
		SimulaattorinSuureet.getInstance().updateAVG_TIME(this.pisteenTyyppi, (double) getKeskimaarainenPalveluAika());
		SimulaattorinSuureet.getInstance().updateW_TIME(this.pisteenTyyppi, (double) getKeskimaarainenLapimenoAika());
		SimulaattorinSuureet.getInstance().updatePERF(this.pisteenTyyppi, (double) getSuoritusTeho());
		SimulaattorinSuureet.getInstance().updateUTIL(this.pisteenTyyppi, (double) getKayttoaste());
	}

	/**
	 * Comparable rajapinnan compareTo metodin toteutus, jolla verrataan
	 * palvelupisteiden jonon pituuksia. Tämän avulla päätetään mihin
	 * palvelupisteeseen tuote kannattaa laittaa jonoon
	 */
	@Override
	public int compareTo(Palvelupiste o) {
		return this.jono.size() - o.jono.size();
	}
}
