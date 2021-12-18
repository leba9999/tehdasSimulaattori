package simu.model;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * Singleton, johon tallennetaan simulaattorin tuottamia suureita. Tällöin
 * tiedot on myös helpompi viedä tietokantaan, kun ne ovat kasassa yhdessä
 * oliossa
 * 
 * @author Sanna Kukkonen
 *
 */

public class SimulaattorinSuureet {

	/**
	 * Singleotonin instanssi
	 */
	private static SimulaattorinSuureet INSTANCE = null;

	/**
	 * Palvelupistekohtaiset suureet tallennetaan EnumMappiin, jossa avaimena
	 * palvelupisteen tyyppi (PalvelupisteTyyppi-enum) & arvona HashMap, jossa
	 * suureiden arvot suureiden nimen takana (String, Double). Tällöin suureet ovat
	 * järjestyksessä palvelupisteensä mukaan
	 */
	private EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> ppSuureet;

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle läpimenneiden tuotteiden
	 * määrälle (count -> CNT)
	 */
	public final String CNT = "CNT";

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle kokonaispalveluajalle (time
	 * -> TIME)
	 */
	public final String TIME = "TIME";

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle keskimääräiselle tuotteiden
	 * palveluajalle (average time -> AVG_TIME)
	 */
	public final String AVG_TIME = "AVG_TIME"; // keskimaarainenPalveluAika()

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle keskimääräiselle tuotteiden
	 * jonotusajalle
	 */
	public final String Q_TIME = "Q_TIME";

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle keskimääräiselle tuotteiden
	 * läpimenoajalle
	 */
	public final String W_TIME = "W_TIME";

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle suoritusteholle
	 * (performance -> PERF)
	 */
	public final String PERF = "PERF";

	/**
	 * Merkkijonovakio - lyhenne palvelupistekohtaiselle käyttöasteelle (utility ->
	 * UTIL)
	 */
	public final String UTIL = "UTIL";

	/**
	 * Muuttuja simuloinnin kokonaisajalle
	 */
	private double simuloinninKokonaisaika;

	/**
	 * Muuttuja simulaation suoritusteholle
	 */
	private double simulaationSuoritusteho;

	/**
	 * Muuttuja simulaatiossa valmistuneiden tuotteiden määrälle
	 */
	private int simulaatiossaValmistuneet;

	/**
	 * Muuttuja simulaatiossa rikkimenneiden tuotteiden määrälle
	 */
	private int simulaatiossaRikkoutuneet;

	/**
	 * Muuttuja simulaatiossa ilmenneiden erikoiskalibrointia tarvitsevien
	 * tuotteiden määrälle
	 */
	private int erikoiset;

	/**
	 * Muuttuja koko tuotantolinjaston läpimmenneiden tuotteiden keskimääräinen
	 * läpimenoaika
	 */
	private double lapimenneidenKeskiaika;

	/**
	 * Singletonin getInstance()-metodi
	 * 
	 * @return INSTANCE palauttaa Singletonin ainoan instanssin
	 */
	public static SimulaattorinSuureet getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SimulaattorinSuureet();

		return INSTANCE;
	}

	/**
	 * Singletonin konstruktori, jossa kutsutaan alustaSuureet() -metodia
	 */
	private SimulaattorinSuureet() {
		alustaSuureet();
	}

	/**
	 * Suureet alustava metodi. Tämä myös tyhjentää Singletonin edellisiltä
	 * suureilta
	 */
	public void alustaSuureet() {
		ppSuureet = new EnumMap<>(PalvelupisteTyyppi.class);

		// alustetaan EnumMap & sinne HashMapit
		ppSuureet.put(PalvelupisteTyyppi.KASAUSPISTE, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.TESTAUSPISTE1, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.TESTAUSPISTE2, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.ERIKOISKALIBROINTI, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.PAKKAUSPISTE, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE, uusiHashMap());
		ppSuureet.put(PalvelupisteTyyppi.KORJAUSPISTE, uusiHashMap());

		setSimuloinninKokonaisAika(0);
		setSimulaationSuoritusteho(0);
		setSimulaatiossaValmistuneet(0);
		setSimulaatiossaRikkoutuneet(0);
		setSimulaationErikoiset(0);
		setLapiKeskimaarainenAika(0);
	}

	/**
	 * Metodi luo sekä palauttaa hashmapin, jossa avaimena toimivat
	 * palvelupistekohtaisten suureiden lyhenteet (Singletonin omat
	 * vakiomerkkijonot) & arvoiksi alustavasti 0.0 . Möhemmin arvoiksi tallennetaan
	 * palvelupistekohtaiset suureet
	 * 
	 * @return HashMap, jossa avaimena palvelupistekohtaisen suureen lyhenne
	 *         (String) ja arvoina paikka suureille (double)
	 */
	private HashMap<String, Double> uusiHashMap() {

		HashMap<String, Double> palautettava = new HashMap<String, Double>();

		palautettava.put(CNT, 0.0);
		palautettava.put(TIME, 0.0);
		palautettava.put(AVG_TIME, 0.0);
		palautettava.put(Q_TIME, 0.0);
		palautettava.put(W_TIME, 0.0);
		palautettava.put(PERF, 0.0);
		palautettava.put(UTIL, 0.0);

		return palautettava;
	}

	/**
	 * Päivittää palvelupistekohtaisen läpimenneiden tuotteiden määrän EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona CNT-avaimen pariksi
	 */
	public void updateCNT(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(CNT, d);
	}

	/**
	 * Päivittää palvelupistekohtaisen kokonaispalveluajan EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona TIME-avaimen pariksi
	 */
	public void updateTIME(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(TIME, d);
	}

	/**
	 * Päivittää palvelupistekohtaisen keskimääräisen tuotteiden palveluajan
	 * EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona AVG_TIME-avaimen pariksi
	 */
	public void updateAVG_TIME(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(AVG_TIME, d);
	}

	/**
	 * Päivittää palvelupistekohtaisen keskimääräisen tuotteiden jonotusajan
	 * EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona Q_TIME-avaimen pariksi
	 */
	public void updateQ_TIME(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(Q_TIME, d);
	}

	/**
	 * Päivittää palvelupistekohtaisen keskimääräisen tuotteiden läpimenoajan
	 * EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona W_TIME-avaimen pariksi
	 */
	public void updateW_TIME(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(W_TIME, d);

	}

	/**
	 * Päivittää palvelupistekohtaisen käyttöasteen EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona UTIL-avaimen pariksi
	 */
	public void updateUTIL(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(UTIL, d);
	}

	/**
	 * Päivittää palvelupistekohtaisen suoritustehon EnumMappiin
	 * 
	 * @param t Palvelupisteen tyyppi (enum), minkä avulla ppSuureet-enummapista
	 *          haetaan halutun palvelupisteen suure-hashmap
	 * @param d suure (double), joka tallennetaan arvona PERF-avaimen pariksi
	 */
	public void updatePERF(PalvelupisteTyyppi t, double d) {

		ppSuureet.get(t).put(PERF, d);
	}

	/**
	 * Päivittää simuloinnin kokonaisajan talteen
	 * 
	 * @param d Päivitetty simuloinnin kokonaisaika
	 */
	public void setSimuloinninKokonaisAika(double d) {

		this.simuloinninKokonaisaika = d;
	}

	/**
	 * Palauttaa simuloinnin kokonaisajan
	 * 
	 * @return Simuloinnin kokonaisaika
	 */
	public double getSimuloinninKokonaisaika() {

		return this.simuloinninKokonaisaika;
	}

	/**
	 * Päivittää simulaation suoritustehon talteen
	 * 
	 * @param d Päivitetty simulaation suoritusteho
	 */
	public void setSimulaationSuoritusteho(double d) {

		this.simulaationSuoritusteho = d;
	}

	/**
	 * Palauttaa simulaation suoritustehon
	 * 
	 * @return Simulaation suoritusteho
	 */
	public double getSimulaationSuoritusteho() {

		return this.simulaationSuoritusteho;
	}

	/**
	 * Päivittää simulaatiossa valmistuneiden tuotteiden kokonaismäärän talteen
	 * 
	 * @param d Simulaatiossa valmistuneiden tuotteiden kokonaismäärä
	 */
	public void setSimulaatiossaValmistuneet(int d) {

		this.simulaatiossaValmistuneet = d;
	}

	/**
	 * Palauttaa simulaatiossa valmistuneiden tuotteiden kokonaismäärän
	 * 
	 * @return Simulaatiossa valmistuneiden tuotteiden kokonaismäärä
	 */
	public int getSimulaatiossaValmistuneet() {

		return this.simulaatiossaValmistuneet;
	}

	/**
	 * Päivittää simulaatiossa rikkoutuneiden tuotteiden kokonaismäärän talteen
	 * 
	 * @param d Simulaatiossa rikkoutuneiden tuotteiden kokonaismäärä
	 */
	public void setSimulaatiossaRikkoutuneet(int d) {

		this.simulaatiossaRikkoutuneet = d;
	}

	/**
	 * Palauttaa simulaatiossa rikkoutuneiden tuotteiden kokonaismäärän
	 * 
	 * @return Simulaatiossa rikkoutuneiden tuotteiden kokonaismäärä
	 */
	public int getSimulaatiossaRikkoutuneet() {

		return this.simulaatiossaRikkoutuneet;
	}

	/**
	 * Lisää eikoiset-muuttujaan +1. Metodia kutsutaan, kun uusi erikoinen tuote on
	 * luotu
	 */
	public void addErikonen() {

		this.erikoiset++;
	}

	/**
	 * Asettaa erikoistuotteiden kokonaismäärän, eli erikoiskalibrointia vaativien
	 * tuotteiden määrän
	 * 
	 * @param d Erikoistuotteiden kokonaismäärä
	 */
	public void setSimulaationErikoiset(int d) {

		this.erikoiset = d;
	}

	/**
	 * Palauttaa erikoiskalibrointia vaativien tuotteiden kokonaismäärän
	 * 
	 * @return Erikoiskalibrointia vaativien tuotteiden kokonaismäärä
	 */
	public int getErikoiset() {

		return this.erikoiset;
	}

	/**
	 * Päivittää läpimenneiden tuotteiden keskimääräisen läpimenoajan talteen
	 * 
	 * @param d Läpimenneiden tuotteiden keskimääräinen läpimenoaika
	 */
	public void setLapiKeskimaarainenAika(double keskiaika) {

		this.lapimenneidenKeskiaika = keskiaika;
	}

	/**
	 * Palauttaa läpimenneiden tuotteiden keskimääräisen läpimenoajan
	 * 
	 * @return Läpimenneiden tuotteiden keskimääräinen läpimenoaika
	 */
	public double getLapiKeskimaarainenAika() {

		return this.lapimenneidenKeskiaika;
	}

	/**
	 * Palauttaa EnumMapin, jossa avaimina palvelupisteiden enumit, joiden arvoina
	 * HashMapit, joissa kasassa palvelupistekohtaiset suureet
	 * 
	 * @return EnumMap (palvelupisteet & palvelupistekohtaiset suureet)
	 */
	public EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> getSuureetEnumMap() {

		return this.ppSuureet;
	}
}
