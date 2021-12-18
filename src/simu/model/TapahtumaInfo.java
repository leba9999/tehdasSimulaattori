package simu.model;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;


/**
 * Sisältää simulaattorissa tapahtuneen tapahtuman kaikki tiedot ja kyseisen tuotteen, johon tapahtuma koskee.
 * Luokka myös pitää sisällään tiedon kaikista suureista. 
 * Luokkaa käytetään hyödyksi animaatiossa, jossa tullaan tarvitsemaan tietyn hetken tietoja. 
 * 
 * Luokka täytyy olla Serializable ja Comparable, jotta voidaan tallentaa luokka tiedostoon helposti ja
 * vertailla tapahtuma aikoja listassa. 
 *
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 */

public class TapahtumaInfo implements Serializable, Comparable<TapahtumaInfo> {
	
	/**
	 * Sisältää versio numeron, jonka avulla tunnistetaan onko tiedostosta ladattu olio yhteensopiva
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Sisältää mitä tuotetta tapahtuma koskee
	 */
	private Tuote tuote;
	 /**
	  * EnumMapissä säilytetään simulaattorin palvelupisteiden suureet, joita sitten käytetään suureIkunassa
	  */
	private EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> suureet;
	/**
	 * Tarvitaan tietää minne tuote on seuraavaksi menossa animaatiota varten, jotta saadaan päätettyä mihin kohtaan pallon pitää hypätä
	 */
	private PalvelupisteTyyppi pTyyppi;

	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää simuloinnin kokonais ajan
	 */
	private double simuloinninKokonaisaika;

	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää simuloinnin suoritus tehon
	 */
	private double simulaationSuoritusteho;

	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää simuloinnissa valmistuneiden tuotteiden määrän
	 */
	private int simulaatiossaValmistuneet;
	
	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää simuloinnissa rikkoutuneiden tuotteiden määrän
	 */
	private int simulaatiossaRikkoutuneet;

	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää simuloinnissa erikoikalibrointia tarvitsevien tuotteiden määrän
	 */
	private int erikoiset;

	/**
	 * Singletonista SimulaattorinSuureet talteen otettu muuttuja, joka sisältää koko tuotantolinjaston läpimmenneiden tuotteiden keskimääräisen
	 * läpimenoajan
	 */
	private double lapimenneidenKeskiaika;
	
	/**
	 * Tapahtuman tyyppi, jolla saadaan selville minkälainen tapahtuma on kyseessä
	 */
	private TapahtumanTyyppi tyyppi;
	
	/**
	 * Tapahtuman kellon aika. Milloin tapahtuma tapahtui simulaation aikana
	 */
	private double aika;
	
	/**
	 * TapahtumaInfon konstruktori, jossa asetetaan tapahtumalle tyyppi, aika ja mitä tuotetta tapahtuma koskee
	 * @param tyyppi minkälainen tapahtuma on kyseessä 
	 * @param aika milloin tapahtuma tapahtui simulaatiossa
	 * @param tuote mikä tuote aiheutti tapahtuman
	 */
	public TapahtumaInfo(TapahtumanTyyppi tyyppi, double aika, Tuote tuote) {
		try {
			this.tuote = (Tuote)tuote.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		this.tyyppi = tyyppi;
		this.aika = aika;
		this.suureet = new EnumMap<>(PalvelupisteTyyppi.class);
		for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
			HashMap<String, Double> temp = new HashMap<String, Double>();
				for (String s : SimulaattorinSuureet.getInstance().getSuureetEnumMap().get(pt).keySet()) {
					temp.put(s, SimulaattorinSuureet.getInstance().getSuureetEnumMap().get(pt).get(s));
				}
				this.suureet.put(pt, temp);
		}
	}

	/**
	 * TapahtumaInfon konstruktori, jossa asetetaan tapahtumalle tyyppi, aika, mitä tuotetta tapahtuma koskee ja minne tuote on seuraavaksi matkalla
	 * @param tyyppi minkälainen tapahtuma on kyseessä 
	 * @param aika milloin tapahtuma tapahtui simulaatiossa
	 * @param tuote mikä tuote aiheutti tapahtuman
	 * @param pTyyppi minkä tyyppiseen palvelupisteeseen tuote on matkalla seuraavaksi
	 */
	public TapahtumaInfo(TapahtumanTyyppi tyyppi, double aika, Tuote tuote, PalvelupisteTyyppi pTyyppi) {
		this.pTyyppi = pTyyppi;
		try {
			this.tuote = (Tuote)tuote.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		this.tyyppi = tyyppi;
		this.aika = aika;
		this.suureet = new EnumMap<>(PalvelupisteTyyppi.class);
		for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
			HashMap<String, Double> temp = new HashMap<String, Double>();
				for (String s : SimulaattorinSuureet.getInstance().getSuureetEnumMap().get(pt).keySet()) {
					temp.put(s, SimulaattorinSuureet.getInstance().getSuureetEnumMap().get(pt).get(s));
				}
				this.suureet.put(pt, temp);
		}
	}
	/**
	 * Metodilla asetetaan mihin palvelupisteeseen tuote on seuraavaksi menossa
	 * @param pTyyppi minkä tyyppiseen palvelupisteeseen tuote on seuraavaksi menossa
	 */
	public void setTuotteenPalvelupiste(PalvelupisteTyyppi pTyyppi) {
		this.pTyyppi = pTyyppi;
	}
	/**
	 * Metodilla selvitetään mihin palvelupisteeseen tuote on seuraavaksi menossa
	 * @return pTyyppi Palvelupisteen tyyppi mihin tuote on menossa
	 */
	public PalvelupisteTyyppi getTuotteenPalvelupiste() {
		return pTyyppi;
	}

	/**
	 * Palauttaa EnumMapin, jossa avaimina palvelupisteiden enumit, joiden arvoina
	 * HashMapit, joissa kasassa palvelupistekohtaiset suureet
	 * 
	 * @return EnumMap (palvelupisteet & palvelupistekohtaiset suureet)
	 */
	public EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> getSuureet() {
		return suureet;
	}
	/**
	 * Antaa tuotteen, jota tapahtuma koskee
	 * @return Palauttaa tuotteen
	 */
	public Tuote getTuote() {
		return tuote;
	}
	/**
	 * Asettaa tapahtuman tyypin, jotta tiedetään minkälainen tapahtuma tapahtui
	 * @param tyyppi minkälainen tapahtuma
	 */
	public void setTyyppi(TapahtumanTyyppi tyyppi) {
		this.tyyppi = tyyppi;
	}
	/**
	 * Antaa tapahtuman tyypin, jotta voidaan tutkia minkälainen tapahtuma tapahtui
	 * @return Palauttaa tapahtuman tyypin
	 */
	public TapahtumanTyyppi getTyyppi() {
		return tyyppi;
	}
	/**
	 * Asettaa tapahtuman simulaation kellon ajan
	 * @param aika tapahtuman kellon aika
	 */
	public void setAika(double aika) {
		this.aika = aika;
	}
	/**
	 * Antaa tapahtuman kellon ajan
	 * @return Palauttaa tapahtuman ajan
	 */
	public double getAika() {
		return aika;
	}
	/**
	 * Vertaa tapahtuman kellon aikoja, jotta voidaan listassa saada tapahtumat aika järjestykseen
	 * @return Palauttaa tiedon onko tapahtuman aika vähemmän, sama tai enemmän kuin toisen tapahtuman
	 */
	@Override
	public int compareTo(TapahtumaInfo arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}
	/**
	 * Päivittää simuloinnin kokonaisajan talteen
	 * 
	 * @param d Päivitetty simuloinnin kokonaisaika
	 */
	public void setSimuloinninKokonaisaika(double d) {

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
	 * Asettaa erikoistuotteiden kokonaismäärän, eli erikoiskalibrointia vaativien
	 * tuotteiden määrän
	 * 
	 * @param d Erikoistuotteiden kokonaismäärä
	 */
	public void setErikoiset(int d) {

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
	public double getLapimenneidenKeskiaika() {

		return this.lapimenneidenKeskiaika;
	}

	/**
	 * Asettaa läpimenneiden tuotteiden keskimääräisen läpimenoajan
	 * 
	 * @param Läpimenneiden tuotteiden keskimääräinen läpimenoaika
	 */
	public void setLapimenneidenKeskiaika(double lapimenneidenKeskiaika) {
		this.lapimenneidenKeskiaika = lapimenneidenKeskiaika;
	}
}
