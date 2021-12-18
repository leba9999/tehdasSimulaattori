package simu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
/**
 * Data Access Object luokka, joka hoitaa datankäsittelyn tietokannan kanssa
 * 
 * @author Janne Lähteenmäki, Leevi Koskinen
 */

public class SuureetDAO implements ISuureetDAO {
	/**
	 * Tietokannan valitsemiseen tarvittavat tiedot
	 * Ip-osoite , tietokannan nimi, käyttäjä ja salasana 
	 */
	private static String IPOSOITE = "localhost";
	private static String DATABASE = "tehdasdb";
	private static String KAYTTAJA = "olso";
	private static String SALASANA = "olso";
	
	/**
	 * Ajokertojenlisäys muuttuja, käytetään kun ajokertoja halutaan lisätä
	 */
	private final int AJOKERTOJENLISAYS = 1;
	
	/**
	 * Yhteys muuttuja tietokantaan
	 */
	private Connection myCon;
	
	/**
	 * Lista kaikista palvelupisteiden suureista ja nimistä
	 */
	private EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> ppSuureet = SimulaattorinSuureet.getInstance()
			.getSuureetEnumMap();

	/**
	 * Konstruktori, jossa luonnin yhteydessä muodostetaan yhteys haluttuun tietokantaan
	 */
	public SuureetDAO() {
		try {
			myCon = DriverManager.getConnection("jdbc:mariadb://"+IPOSOITE+"/"+DATABASE+"?user="+KAYTTAJA+"&password="+SALASANA);
		} catch (SQLException e) {
			printSQLExceptions("SuureetDAO()", e);
			System.exit(-1);
		}
	}

	/**
	 * Metodi virhetilanteiden selville saamiseksi
	 * @param methodinNimi Metodin nimi, missä virhe muodostuu
	 * @param e Poikkeus joka muodostui
	 */
	private void printSQLExceptions(String methodinNimi, SQLException e) {
		System.err.println("Metodi: " + methodinNimi);
		do {
			System.err.println("Viesti: " + e.getMessage());
			System.err.println("Virhekoodi: " + e.getErrorCode());
			System.err.println("SQL-tilakoodi: " + e.getSQLState());
		} while (e.getNextException() != null);
	}
	
	/**
	 * Metodi palvelupisteiden suureiden ja ajokertojen tietokantaan tallentamiselle
	 * Lisää tietokannan sarakkeisiin listasta haetut tiedot yksitellen
	 * Tehty booleaniksi testejä varten
	 */
	public boolean tallennaPpSuureet() {

		int ajoKerrat = monennesAjoPp() + AJOKERTOJENLISAYS;

		for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
			String query = "INSERT INTO tallennetutsuureet (cnt, time, avg_time, q_time, w_time, perf, util, service_point, save_number)"
					+ "VALUES(?,?,?,?,?,?,?,?,?)";
			try (PreparedStatement stmt = myCon.prepareStatement(query)) {
				stmt.setDouble(1, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().CNT));
				stmt.setDouble(2, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().TIME));
				stmt.setDouble(3, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().AVG_TIME));
				stmt.setDouble(4, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().Q_TIME));
				stmt.setDouble(5, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().W_TIME));
				stmt.setDouble(6, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().PERF));
				stmt.setDouble(7, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().UTIL));
				stmt.setString(8, pt.toString());
				stmt.setInt(9, ajoKerrat);
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				printSQLExceptions("tallennaPpSuureet()", e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metodi simulaattorin suureiden ja ajokertojen tietokantaan tallentamiselle
	 * Lisää tietokannan sarakkeisiin SimulaattorinSuureet singletonista saadut tiedot yksitellen
	 * Tehty Booleaniksi testejä varten
	 */
	public boolean tallennaSimSuureet() {
		
		int ajoKerrat = monennesAjoSim() + AJOKERTOJENLISAYS;
		
		try(PreparedStatement stmt = myCon.prepareStatement("INSERT INTO simulaattorinsuureet (sim_time, sim_perf, save_number, fin_prod, bro_prod, spec_prod, prod_pas)" +
		"VALUES(?,?,?,?,?,?,?)")) {
			stmt.setDouble(1, SimulaattorinSuureet.getInstance().getSimuloinninKokonaisaika());
			stmt.setDouble(2, SimulaattorinSuureet.getInstance().getSimulaationSuoritusteho());
			stmt.setInt(3, ajoKerrat);
			stmt.setInt(4, SimulaattorinSuureet.getInstance().getSimulaatiossaValmistuneet());
			stmt.setInt(5, SimulaattorinSuureet.getInstance().getSimulaatiossaRikkoutuneet());
			stmt.setInt(6, SimulaattorinSuureet.getInstance().getErikoiset());
			stmt.setDouble(7, SimulaattorinSuureet.getInstance().getLapiKeskimaarainenAika());


			stmt.executeUpdate();
		} catch (SQLException e) {
			printSQLExceptions("tallennaSimSuureet()", e);
			return false;
		}
		return true;
	}

	/**
	 * Metodi joka hakee suurimman ajokerran tietokannasta
	 * @return Suurin ajokerta tietokannassa
	 */
	public int monennesAjoPp() {
		int monennes = 0;

		try (PreparedStatement stmt = myCon.prepareStatement("SELECT * FROM tallennetutsuureet")) {
			ResultSet myRs = stmt.executeQuery();
			while (myRs.next()) {
				int monas = myRs.getInt("save_number");
				if (monennes < monas) {
					monennes = monas;
				}
			}

		} catch (SQLException e) {
			printSQLExceptions("monennesAjoPp()", e);
		}
		return monennes;

	}
	
	public int monennesAjoSim() {
		int monennes = 0;

		try (PreparedStatement stmt = myCon.prepareStatement("SELECT * FROM simulaattorinsuureet")) {
			ResultSet myRs = stmt.executeQuery();
			while (myRs.next()) {
				int monas = myRs.getInt("save_number");
				if (monennes < monas) {
					monennes = monas;
				}
			}

		} catch (SQLException e) {
			printSQLExceptions("monennesAjoSim()", e);
		}
		return monennes;

	}
	
	/**
	 * Metodi ajokertojen listaukselle
	 * Listaa ajokertoja tietokannasta ArrayListiin ja muuntaa ArrayListin lopussa normaaliksi Arrayksi
	 * @return Integer taulukko, joka sisältää kaikki ajokerrat suuruusjärjestyksessä
	 */
	public	Integer[] getMontakoAjoa() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		try(PreparedStatement stmt = myCon.prepareStatement("SELECT * FROM simulaattorinsuureet")) {
			ResultSet myRs = stmt.executeQuery();
			while(myRs.next()) {
				list.add(myRs.getInt("save_number"));
			}
		} catch (SQLException e) {
			printSQLExceptions("montakoAjoa()", e);
		}
		
		Integer[] palautettavaTaulukko = new Integer[list.size()];
		return (Integer[]) list.toArray(palautettavaTaulukko);
	}
	
	/**
	 * Metodi palvelupisteiden suureiden hakemiselle tietokannasta
	 * Hakee syötetyn ajokerran mukaan tietyn simulaatioajon palvelpisteiden suureet ja päivittää ne singletoniin
	 * @param ajoNumero Syötetty ajokerta, minkä tiedot halutaan hakea
	 */
	public void haePpSuureet(int ajoNumero) {
		
		try(PreparedStatement stmt = myCon.prepareStatement("SELECT * FROM tallennetutsuureet WHERE save_number=?")) {
			stmt.setInt(1, ajoNumero);
			ResultSet myRs = stmt.executeQuery();
		
			while(myRs.next()) {
				SimulaattorinSuureet.getInstance().updateCNT(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("cnt"));
				SimulaattorinSuureet.getInstance().updateTIME(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("time"));
				SimulaattorinSuureet.getInstance().updateAVG_TIME(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("avg_time"));
				SimulaattorinSuureet.getInstance().updateQ_TIME(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("q_time"));
				SimulaattorinSuureet.getInstance().updateW_TIME(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("w_time"));
				SimulaattorinSuureet.getInstance().updatePERF(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("perf"));
				SimulaattorinSuureet.getInstance().updateUTIL(PalvelupisteTyyppi.valueOf(myRs.getString("service_point")), myRs.getDouble("util"));
				
			}
		} catch (SQLException e) {
			printSQLExceptions("haePpSuureet()", e);
		}

	}
	
	/**
	 * Metodi simulaattorin suureiden hakemiselle tietokannasta
	 * Hakee tietokannasta ajokerran mukaan tietyn simulaatioajon koko simulaattorin suureet ja asettaa ne singletoniin
	 * @param ajoNumero Syötetty ajokerta, minkä tiedot halutaan hakea 
	 */
	public void haeSimSuureet(int ajoNumero) {
		
		try(PreparedStatement stmt = myCon.prepareStatement("SELECT * FROM simulaattorinsuureet WHERE save_number=?")) {
			stmt.setInt(1, ajoNumero);
			ResultSet myRs = stmt.executeQuery();
			
			while(myRs.next()) {
				SimulaattorinSuureet.getInstance().setSimuloinninKokonaisAika((myRs.getDouble("sim_time")));
				SimulaattorinSuureet.getInstance().setSimulaationSuoritusteho((myRs.getDouble("sim_perf")));
				SimulaattorinSuureet.getInstance().setSimulaatiossaValmistuneet((myRs.getInt("fin_prod")));
				SimulaattorinSuureet.getInstance().setSimulaatiossaRikkoutuneet((myRs.getInt("bro_prod")));
				SimulaattorinSuureet.getInstance().setSimulaationErikoiset((myRs.getInt("spec_prod")));
				SimulaattorinSuureet.getInstance().setLapiKeskimaarainenAika((myRs.getDouble("prod_pas")));
				
			}
						
		} catch (SQLException e) {
			printSQLExceptions("haeSimSuureet()", e);
		}
	}
	
	/**
	 * Metodi palvelpisteiden suureiden poistamiseen tietokannasta
	 * Poistaa tietokannasta ajokerran mukaan kaikki ajokertaan liittyvän palvelupisteiden datan
	 * @param ajoNumero Syötetty ajokerta, minkä tiedot halutaan poistaa
	 */
	public void poistaPpSuureita(int ajoNumero) {
		
		try(PreparedStatement stmt = myCon.prepareStatement("DELETE FROM tallennetutsuureet WHERE save_number=?")) {
			stmt.setInt(1, ajoNumero);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			printSQLExceptions("poistaPpSuureita()", e);
		}
	}
	
	/**
	 * Metodi simuloinnin suureiden poistamiseen tietokannasta
	 * Poistaa tietokannasta ajokerran mukaan kaikki ajokertaan liittyvän simulaation datan
	 * @param ajoNumero Syötetty ajokerta, minkä tiedot halutaan poistaa
	 */
	public void poistaSimSuureita(int ajoNumero) {
		
		try(PreparedStatement stmt = myCon.prepareStatement("DELETE FROM simulaattorinsuureet WHERE save_number=?")) {
			stmt.setInt(1, ajoNumero);
			stmt.executeUpdate();
			
		} catch(SQLException e) {
			printSQLExceptions("poistaSimSuureita()", e);
		}
	}

	/**
	 * Ip-osoitteen getteri
	 * @return Tietokannan ip-osoitteen mihin yhdteys muodostetaan
	 */
	public static String getIPOSOITE() {
		return IPOSOITE;
	}
	
	/**
	 * Ip-osoitteen setteri
	 * @param yHTEYSOSOITE Syötteenä saatu tietokannan ip-osoite
	 */
	public static void setIPOSOITE(String yHTEYSOSOITE) {
		IPOSOITE = yHTEYSOSOITE;
	}
	
	/**
	 * Käyttäjän getteri
	 * @return Tietokannan käyttäjän nimi
	 */
	public static String getKAYTTAJA() {
		return KAYTTAJA;
	}
	
	/**
	 * Käyttäjän setteri
	 * @param kAYTTAJA Syötteenä saatu tietokannan käyttäjän nimi
	 */
	public static void setKAYTTAJA(String kAYTTAJA) {
		KAYTTAJA = kAYTTAJA;
	}
	
	/**
	 * Salasanan getteri
	 * @return Tietokannan salasana
	 */
	public static String getSALASANA() {
		return SALASANA;
	}
	
	/**
	 * Salasanan setteri
	 * @param sALASANA Syötteenä saatu tietokannan salasana
	 */
	public static void setSALASANA(String sALASANA) {
		SALASANA = sALASANA;
	}
	
	/**
	 * Tietokannan nimen getteri
	 * @return Tietokannan nimi
	 */
	public static String getDATABASE() {
		return DATABASE;
	}
	
	/**
	 * Tietokannan nimen setteri
	 * @param dATABASE Syötteenä saatu databasen nimi
	 */
	public static void setDATABASE(String dATABASE) {
		DATABASE = dATABASE;
	}
}
