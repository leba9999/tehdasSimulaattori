package simu.model;

/**
 * Singleton, johon tallennetaan & josta haetaan parametreja simulaattorin
 * pyöräyttämiseksi
 * 
 * @author Sanna Kukkonen
 */
public class SimulaattorinParametrit {

	/**
	 * Singletonin instanssi
	 */
	private static SimulaattorinParametrit INSTANCE = null;

	/**
	 * Koko simulaation ajoaika tunteina
	 */
	private double simulaatioaikaTunteina;

	/**
	 * Koko simulaation tehtaan koneiston luotettavuus asteikolla 1-5
	 */
	private int koneistonLuotettavuus;

	/**
	 * Koko simulaation ajon erikoiskalibroitavien määrä prosentteina
	 */
	private double erikoiskalibroitavienMäärä;

	/**
	 * Kasauspisteen keskimääläinen palveluaika
	 */
	private double kasauspisteenKMAika;

	/**
	 * 1. testauspisteen keskimääläinen palveluaika
	 */
	private double test1KMAika;

	/**
	 * 2. testauspisteen keskimääläinen palveluaika
	 */
	private double test2KMAika;

	/**
	 * Pakkauspisteen keskimääläinen palveluaika
	 */
	private double pakkauspisteenKMAika;

	/**
	 * Laaduntarkastuspisteen keskimääläinen palveluaika
	 */
	private double laaduntarkKMAika;

	/**
	 * Korjauspisteen keskimääläinen palveluaika
	 */
	private double korjauspisteenKMAika;

	/**
	 * Erikoiskalibrointipisteen keskimääläinen palveluaika
	 */
	private double erikoiskalibKMAika;

	/**
	 * Singletonin getInstance()-metodi
	 * 
	 * @return INSTANCE palauttaa Singletonin ainoan instanssin
	 */
	public static SimulaattorinParametrit getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SimulaattorinParametrit();

		return INSTANCE;
	}

	/**
	 * Singletonin konstruktori. Mikäli käyttäjä ei syötä mitään parametreja,
	 * simulaatio pyörähtää oletusarvojen perusteella.
	 */
	private SimulaattorinParametrit() {
		setOletusarvot();
	}

	/**
	 * Asettaa paramterit ohjelmoijien päättämiksi oletusarvoiksi
	 */
	public void setOletusarvot() {
		this.simulaatioaikaTunteina = 10;
		this.erikoiskalibroitavienMäärä = 20;
		this.koneistonLuotettavuus = 10;

		this.kasauspisteenKMAika = 37.5;
		this.test1KMAika = 35;
		this.test2KMAika = 35;
		this.pakkauspisteenKMAika = 6.5;
		this.laaduntarkKMAika = 6.5;
		this.korjauspisteenKMAika = 6.5;
		this.erikoiskalibKMAika = 35;
	}

	/**
	 * Simulaattorille asetetaan käyttäjän syöttämä simulaatioaika tunteina
	 * 
	 * @param käyttäjänSyöttämäSimulaatioaika Simulaation ajoaika tunteina
	 *                                        (käyttäjän syöttämä)
	 */
	public void setSimulaatioAikaTunteina(double käyttäjänSyöttämäSimulaatioaika) {
		this.simulaatioaikaTunteina = käyttäjänSyöttämäSimulaatioaika;
	}

	/**
	 * Palauttaa simulaattorin ajoajan tunteina
	 * 
	 * @return Simulaation ajoaika tunteina
	 */
	public double getSimulaatioaikaTunteina() {
		return simulaatioaikaTunteina;
	}

	/**
	 * Palauttaa simulaattorin ajoajan minuutteina (Kello-singletonin käyttöön)
	 * 
	 * @return Simulaation ajoaika minuutteina
	 */
	public double getSimulaatioaikaMinuutteina() {
		return simulaatioaikaTunteina * 60;
	}

	/**
	 * Simulaattorille asetetaan käyttäjän valitsema koneiston luotettavuus
	 * asteikolta 1-5
	 * 
	 * @param käyttäjänValitsemaLuotettavuus käyttäjän valitseman arvon indeksi
	 *                                       ComboBoxissa
	 */
	public void setKoneistonLuotettavuus(int käyttäjänValitsemaLuotettavuus) {
		this.koneistonLuotettavuus = käyttäjänValitsemaLuotettavuus + 1;
	}

	/**
	 * Palauttaa simulaattorin koneiden luotettavuuden arvon (1-5)
	 * 
	 * @return Palauttaa simulaattorin koneiden luotettavuuden arvon (1-5)
	 */
	public int getKoneistonLuotettavuus() {
		return koneistonLuotettavuus;
	}

	/**
	 * Simulaattorille asetetaan käyttäjän määrittelemä erikoiskalibroitavien
	 * tuotteiden prosentuaalisen määrän
	 * 
	 * @param käyttäjänSyöttämäErikoiskalibrointienMäärä Erikoiskalibroitavien
	 *                                                   tuotteiden prosentuaalinen
	 *                                                   määrä
	 */
	public void setErikoiskalibroitavienMäärä(double käyttäjänSyöttämäErikoiskalibrointienMäärä) {
		this.erikoiskalibroitavienMäärä = käyttäjänSyöttämäErikoiskalibrointienMäärä;
	}

	/**
	 * Palauttaa erikoiskalibroitavien tuotteiden prosentuaalisen määrän
	 * 
	 * @return Erikoiskalibroitavien tuotteiden prosentuaalinen määrä
	 */
	public double getErikoiskalibroitavienMäärä() {
		return erikoiskalibroitavienMäärä;
	}

	/**
	 * Asettaa simulaattorin tehtaan kasauspisteen keskimääräisen palveluajan
	 * 
	 * @param kasauspisteenKMAika Käyttäjän määrittelemä kasauspisteen
	 *                            keskimääräinen palveluaika
	 */
	public void setKasauspisteenKMAika(double kasauspisteenKMAika) {
		this.kasauspisteenKMAika = kasauspisteenKMAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan kasauspisteen keskimääräisen palveluajan
	 * 
	 * @return Kasauspisteen keskimääräinen palveluaika
	 */
	public double getKasauspisteenKMAika() {
		return kasauspisteenKMAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan 1. testauspisteen keskimääräisen palveluajan
	 * 
	 * @return 1. testauspisteen keskimääräinen palveluaika
	 */
	public double getTest1KMAika() {
		return test1KMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan 1. testauspisteen keskimääräisen palveluajan
	 * 
	 * @param test1kmAika Käyttäjän määrittelemä 1. testauspisteen keskimääräinen
	 *                    palveluaika
	 */
	public void setTest1KMAika(double test1kmAika) {
		test1KMAika = test1kmAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan 2. testauspisteen keskimääräisen palveluajan
	 * 
	 * @return 2. testauspisteen keskimääräinen palveluaika
	 */
	public double getTest2KMAika() {
		return test2KMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan 2. testauspisteen keskimääräisen palveluajan
	 * 
	 * @param test2kmAika Käyttäjän määrittelemä 2. testauspisteen keskimääräinen
	 *                    palveluaika
	 */
	public void setTest2KMAika(double test2kmAika) {
		test2KMAika = test2kmAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan pakkauspisteen keskimääräisen palveluajan
	 * 
	 * @return pakkauspisteen keskimääräinen palveluaika
	 */
	public double getPakkauspisteenKMAika() {
		return pakkauspisteenKMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan pakkauspisteen keskimääräisen palveluajan
	 * 
	 * @param pakkauspisteenKMAika Käyttäjän määrittelemä pakkauspisteen
	 *                             keskimääräinen palveluaika
	 */
	public void setPakkauspisteenKMAika(double pakkauspisteenKMAika) {
		this.pakkauspisteenKMAika = pakkauspisteenKMAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan laaduntarkastuspisteen keskimääräisen
	 * palveluajan
	 * 
	 * @return laaduntarkastuspisteen keskimääräinen palveluaika
	 */
	public double getLaaduntarkKMAika() {
		return laaduntarkKMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan laaduntarkastuspisteen keskimääräisen
	 * palveluajan
	 * 
	 * @param laaduntarkKMAika Käyttäjän määrittelemä laaduntarkastuspisteen
	 *                         keskimääräinen palveluaika
	 */
	public void setLaaduntarkKMAika(double laaduntarkKMAika) {
		this.laaduntarkKMAika = laaduntarkKMAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan korjauspisteen keskimääräisen palveluajan
	 * 
	 * @return korjauspisteen keskimääräinen palveluaika
	 */
	public double getKorjauspisteenKMAika() {
		return korjauspisteenKMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan korjauspisteen keskimääräisen palveluajan
	 * 
	 * @param korjauspisteenKMAika Käyttäjän määrittelemä korjauspisteen
	 *                             keskimääräinen palveluaika
	 */
	public void setKorjauspisteenKMAika(double korjauspisteenKMAika) {
		this.korjauspisteenKMAika = korjauspisteenKMAika;
	}

	/**
	 * Palauttaa simulaattorin tehtaan erikoiskalibrointipisteen keskimääräisen
	 * palveluajan
	 * 
	 * @return erikoiskalibrointipisteen keskimääräinen palveluaika
	 */
	public double getErikoiskalibKMAika() {
		return erikoiskalibKMAika;
	}

	/**
	 * Asettaa simulaattorin tehtaan erikoiskalibrointipisteen keskimääräisen
	 * palveluajan
	 * 
	 * @param erikoiskalibKMAika Käyttäjän määrittelemä erikoiskalibrointipisteen
	 *                           keskimääräinen palveluaika
	 */
	public void setErikoiskalibKMAika(double erikoiskalibKMAika) {
		this.erikoiskalibKMAika = erikoiskalibKMAika;
	}

}
