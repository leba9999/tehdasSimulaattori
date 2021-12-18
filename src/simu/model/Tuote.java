package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Trace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * Luokka esittää simulaattorissa tuotetta, joka kulkee tuotantolinjaa pitkin.
 * Tuote luokka täytyy olla Serializable ja Cloneable koska luokka halutaan pystyä
 * tallentamaan tiedostoon ja kopioimaan animaatio listaa varten, jotta saadaan tuotteen
 * tilanne tallennettua halutulla hetkellä
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */
public class Tuote implements Serializable, Cloneable {

	/**
	 * Sisältää versio numeron, jonka avulla tunnistetaan onko tiedostosta ladattu
	 * olio yhteensopiva.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Tuotteiden seuraava tunniste
	 */
	private static int ID = 1;

	/**
	 * Tuotteiden yhteen laskettu aika simulaatiossa
	 */
	private static long sum = 0;
	/**
	 * Tuotteen saapumisaika simulaatioon
	 */
	private double saapumisaika;
	/**
	 * Tuotteen poistumisaika simulaatiosta
	 */
	private double poistumisaika;
	/**
	 * Tuotteen saapumisaika palvelupisteelle. Muutujan arvo tallennetaan EnumMapiin
	 * tuotteenLapimenoajat, eli kyseessä on temp muuttuja
	 */
	private double palvelupisteelleSaapumisaika;
	/**
	 * Tuotteen poistumisaika palvelupisteeltä. Muutujan arvo tallennetaan
	 * EnumMapiin tuotteenLapimenoajat, eli kyseessä on temp muuttuja
	 */
	private double palvelupisteellePoistumisaika;
	/**
	 * <pre>
	 * tuotteenLapimenoajat on enumMap, jossa on tiedot tuotteen läpimenoajoista
	 * palvelupistekohtaisesti. Eli enumMapin avaimena toimii palvelupisteen tyyppi
	 * ja Arraylistassa on tuotteen kokema läpimeno aika palvelupisteessä Esimerkki
	 * tietorakenteesta:
	 * 
	 * Tyyppi Arraylista 
	 * |-------------|----------------| 
	 * |Avain:       |Arvot listassa: |
	 * |KASAUSPISTE  |25.1            | 
	 * |KORJAUSPISTE |15.2, 14.5      | 
	 * |TESTAUSPISTE1|35.4, 43.2, 31.3| 
	 * |-------------|----------------|
	 * </pre>
	 */
	private EnumMap<PalvelupisteTyyppi, ArrayList<Double>> tuotteenLapimenoajat;
	/**
	 * Tuotteen uniikki tunniste
	 */
	private int id;
	/**
	 * Kuinka monta kertaa tuote on mennyt rikki simulaatiossa
	 */
	private int rikkoutumisKerrat;
	/**
	 * Onko tuote erikoinen joka tarvitsee käydä erikoiskalibroinnissa
	 */
	private boolean onkoErikoinen = false;
	/**
	 * Onko tuote rikkinäinen. Jos on niin täytyy tuote lähetää korjauspisteelle
	 */
	private boolean onkoRikki = false;

	/**
	 * Tuotteen konstruktori, jossa asetetaan tuotteelle erikoiskalibroitavien
	 * jakauma generaattori, tuotteen saapumisaika simulaatioon, tuotteen uniikki
	 * tunniste(id) ja alustetaan tuotteen kokema läpimeno aika EnumMap lista.
	 * 
	 * @param generator asetetaan haluttu jakauma generaattori
	 */
	public Tuote(ContinuousGenerator generator) {
		id = ID++;
		saapumisaika = Kello.getInstance().getAika();
		tuotteenLapimenoajat = new EnumMap<>(PalvelupisteTyyppi.class);

		if (generator.sample() < SimulaattorinParametrit.getInstance().getErikoiskalibroitavienMäärä()) {
			onkoErikoinen = true;
			SimulaattorinSuureet.getInstance().addErikonen();
		}

		Trace.out(Trace.Level.INFO, "Uusi tuote nro " + id + " saapui klo " + saapumisaika);
	}
	/**
	 * Metodilla kopioidaan tuote olio
	 * @return palauttaa kloonin/kopion tuotteesta
	 * @throws CloneNotSupportedException poikkeuksen jos kloonausta/kopiontia ei tueta
	 */
	public Object clone() throws CloneNotSupportedException
	{
		Tuote t = (Tuote)super.clone();
		return t;
	}

	/**
	 * Antaa tuotteen kokemat läpimenoajat halutusta palvelupisteestä tyypin avulla.
	 * 
	 * @param tyyppi mistä palvelupisteestä halutaan ajat
	 * @return Palauttaa Arraylistan tuotteen kokemista läpimenoajoista
	 */
	public ArrayList<Double> getPalvelupisteenLapimenoAjat(PalvelupisteTyyppi tyyppi) {
		return tuotteenLapimenoajat.get(tyyppi);
	}

	/**
	 * Antaa tuotteen kokema läpimenoajat yhteensä yhdessä palvelupisteessä
	 * 
	 * @param tyyppi mistä palvelupisteestä halutaan summattu läpimenoaika
	 * @return Palauttaa tuotteen kokema summattu läpimenoaika
	 */
	public double getPalvelupisteenLapimenoAjatYhteensa(PalvelupisteTyyppi tyyppi) {
		double summa = 0;
		if (tuotteenLapimenoajat.containsKey(tyyppi)) {
			for (int i = 0; i < tuotteenLapimenoajat.get(tyyppi).size(); i++) {
				summa += tuotteenLapimenoajat.get(tyyppi).get(i);
			}
		}
		return summa;
	}

	/**
	 * Antaa kuinka monta kertaa tuote on mennyt rikki
	 * 
	 * @return Palauttaa tuotteen rikkoutimiskerrat
	 */
	public int getRikkoutumisKerrat() {
		return rikkoutumisKerrat;
	}

	/**
	 * Asettaa tuotteelle rikkoutimiskerrat, eli kuinka monta kertaa tuote on mennyt
	 * rikki
	 * 
	 * @param rikkoutumisKerrat asettaa tuotteen rikkimeno kerrat
	 */
	public void setRikkoutumisKerrat(int rikkoutumisKerrat) {
		this.rikkoutumisKerrat = rikkoutumisKerrat;
	}

	/**
	 * Asettaa tuotteelle palvelupisteelle saapumisajan
	 * 
	 * @param palvelupisteelleSaapumisaika mihin aikaan tuote saapuu
	 *                                     palvelupisteelle
	 */
	public void setPalvelupisteelleSaapumisaika(double palvelupisteelleSaapumisaika) {
		this.palvelupisteelleSaapumisaika = palvelupisteelleSaapumisaika;
	}

	/**
	 * Asettaa tuotteelle poistumisajan palvelupisteeltä ja lisää enumMappiin
	 * kyseiselle palvelupisteelle tuotteen kokeman läpimeno ajan
	 * 
	 * @param palvelupisteellePoistumisaika milloin tuote poistui/poistuu
	 *                                      palvelupisteeltä
	 * @param tyyppi                        miltä palvelupisteeltä tuote
	 *                                      poistuu/poistui
	 */
	public void setPalvelupisteellePoistumisaika(double palvelupisteellePoistumisaika, PalvelupisteTyyppi tyyppi) {
		this.palvelupisteellePoistumisaika = palvelupisteellePoistumisaika;
		if (!tuotteenLapimenoajat.containsKey(tyyppi)) {
			ArrayList<Double> ar = new ArrayList<>();
			ar.add(palvelupisteellePoistumisaika - palvelupisteelleSaapumisaika);
			tuotteenLapimenoajat.put(tyyppi, ar);
		} else {
			ArrayList<Double> ar = tuotteenLapimenoajat.get(tyyppi);
			ar.add(palvelupisteellePoistumisaika - palvelupisteelleSaapumisaika);
			tuotteenLapimenoajat.put(tyyppi, ar);
		}
	}

	/**
	 * Antaa tuotteen poistumisajan simulaatiosta
	 * 
	 * @return Paluttaa tuotteen poitumisajan
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * Asettaa tuotteelle simulaatiosta poistumisajan
	 * 
	 * @param poistumisaika milloin tuote poistui
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	/**
	 * Antaa tuotteen simulaatioon saapumisenajan
	 * 
	 * @return Palauttaa ajan milloin tuote saapui simulaatioon
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}

	/**
	 * Asettaa tuotteelle saapumisajan simulaatioon
	 * 
	 * @param saapumisaika milloin tuote saapui simulaatioon
	 */
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}

	/**
	 * Antaa tuotteen uniikin tunnisteen.
	 * 
	 * @return Palauttaa tuotteen id:n
	 */
	public int getId() {
		return id;
	}

	/**
	 * Onko tuote erikoiskalibroitava, jos on niin viedään
	 * erikoiskalibrointipisteelle
	 * 
	 * @return Palauttaa tiedon onko tuote erikoinen
	 */
	public boolean viedaakoErikoiskalibrointiin() {
		return onkoErikoinen;
	}

	/**
	 * Antaa tiedon onko tuote rikki
	 * 
	 * @return Palauttaa tiedon onko tuote rikki
	 */
	public boolean getOnkoRikki() {
		return onkoRikki;
	}

	/**
	 * Asettaa tuotteen rikkinäiseksi
	 * 
	 * @param onkoRikki onko tuote rikki
	 */
	public void setOnkoRikki(boolean onkoRikki) {
		this.onkoRikki = onkoRikki;
	}

	/**
	 * Tuotteen loppu raportti kun poistuu simulaatiosta
	 */
	public void raportti() {
		Trace.out(Trace.Level.INFO, "\nTuote " + id + " valmis! ");
		Trace.out(Trace.Level.INFO, "Tuote " + id + " saapui: " + saapumisaika);
		Trace.out(Trace.Level.INFO, "Tuote " + id + " poistui: " + poistumisaika);
		Trace.out(Trace.Level.INFO, "Tuote " + id + " viipyi: " + (poistumisaika - saapumisaika));
		Trace.out(Trace.Level.INFO, "Tuote " + id + " Kasauspisteen läpimeno aika summa: "
				+ (getPalvelupisteenLapimenoAjatYhteensa(PalvelupisteTyyppi.TESTAUSPISTE1)));
		// TODO: Voi poistaa.
		if (tuotteenLapimenoajat.containsKey(PalvelupisteTyyppi.TESTAUSPISTE1)) {
			for (int i = 0; i < tuotteenLapimenoajat.get(PalvelupisteTyyppi.TESTAUSPISTE1).size(); i++) {
				Trace.out(Trace.Level.INFO, "Tuote " + id + " Kasauspisteen läpimeno: "
						+ (tuotteenLapimenoajat.get(PalvelupisteTyyppi.TESTAUSPISTE1).get(i)));
			}
		}
		sum += (poistumisaika - saapumisaika);
		double keskiarvo = sum / id;
		System.out.println("Tuotteiden läpimenoaikojen keskiarvo tähän asti " + keskiarvo);
		SimulaattorinSuureet.getInstance().setLapiKeskimaarainenAika(keskiarvo);
	}

}
