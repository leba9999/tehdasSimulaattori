package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumMap;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import simu.model.PalvelupisteTyyppi;
import simu.model.SimulaattorinSuureet;
import simu.model.SuureetDAO;

// @author Janne Lähteenmäki

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SuureetDAOTest {

	private EnumMap<PalvelupisteTyyppi, HashMap<String, Double>> ppSuureet = SimulaattorinSuureet.getInstance()
			.getSuureetEnumMap();
	private static SuureetDAO sDao = new SuureetDAO();
	private static double arvo1 = 1.0;
	private static double arvo2 = 2.0;
	private static double arvo3 = 3.0;
	private static double arvo4 = 4.0;
	private static double arvo5 = 5.0;
	private static double arvo6 = 6.0;
	private static double arvo7 = 7.0;
	private static int arvo8 = 3;
	private static int arvo9 = 3;
	private static int arvo10 = 3;
	
	// Ennen mitään muuta, asetetaan singletoniin valitut arvot
	@BeforeAll
	public static void alkuToimet() {
		for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
			SimulaattorinSuureet.getInstance().updateCNT(pt, arvo1);
			SimulaattorinSuureet.getInstance().updateTIME(pt, arvo2);
			SimulaattorinSuureet.getInstance().updateAVG_TIME(pt, arvo3);
			SimulaattorinSuureet.getInstance().updateQ_TIME(pt, arvo4);
			SimulaattorinSuureet.getInstance().updateW_TIME(pt, arvo5);
			SimulaattorinSuureet.getInstance().updatePERF(pt, arvo6);
			SimulaattorinSuureet.getInstance().updateUTIL(pt, arvo7);
			SimulaattorinSuureet.getInstance().setLapiKeskimaarainenAika(arvo1);
			SimulaattorinSuureet.getInstance().setSimulaationErikoiset(arvo8);
			SimulaattorinSuureet.getInstance().setSimulaationSuoritusteho(arvo2);
			SimulaattorinSuureet.getInstance().setSimulaatiossaRikkoutuneet(arvo9);
			SimulaattorinSuureet.getInstance().setSimulaatiossaValmistuneet(arvo10);
			SimulaattorinSuureet.getInstance().setSimuloinninKokonaisAika(arvo3);
		}
	}
	
	// Lopuksi poistetaan tietokantaan testien ajaksi lisätty data
	@AfterAll
	public static void loppuToimet() {
		sDao.poistaPpSuureita(sDao.monennesAjoPp());
		sDao.poistaSimSuureita(sDao.monennesAjoSim());
	}
	
	// Testataan meneekö palvelupisteiden suureiden tallennus virheittä läpi
	@Test
	@Order(1)
	public void tallennaPpSuureet() {
		assertTrue(sDao.tallennaPpSuureet(), "Tallennus ei onnistunut");
	}
	
	// Testataan meneekö palvelupisteiden suureiden tallennus virheittä läpi
	@Test
	@Order(2)
	public void tallennaSimSuureet() {
		assertTrue(sDao.tallennaSimSuureet(), "Tallennus ei onnistunut");
	}
	
	// Alussa alustetaan singletonin kaikki arvot 0
	// Testataan täsmääkö haetut suureiden arvot aijemin tallennettuihin suureisiin
	@Test
	@Order(3)
	public void haePpSuureet() {
		SimulaattorinSuureet.getInstance().alustaSuureet();
		sDao.haePpSuureet(sDao.monennesAjoPp());

		for (PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
			assertEquals(arvo1, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().CNT), pt + " CNT arvo väärin");
			assertEquals(arvo2, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().TIME),
					pt + "TIME arvo väärin");
			assertEquals(arvo3, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().AVG_TIME),
					pt + "AVG_TIME arvo väärin");
			assertEquals(arvo4, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().Q_TIME),
					pt + "Q_TIME arvo väärin");
			assertEquals(arvo5, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().W_TIME),
					pt + "W_TIME arvo väärin");
			assertEquals(arvo6, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().PERF),
					pt + "PERF arvo väärin");
			assertEquals(arvo7, ppSuureet.get(pt).get(SimulaattorinSuureet.getInstance().UTIL),
					pt + "UTIL arvo väärin");
		}
	}
	
	// Alussa alustetaan singletonin kaikki arvot 0
	// Testataan täsmääkö haetut suureiden arvot aijemin tallennettuihin suureisiin
	@Test
	@Order(4)
	public void haeSimSureet() {
		SimulaattorinSuureet.getInstance().alustaSuureet();
		sDao.haeSimSuureet(sDao.monennesAjoSim());

		assertEquals(arvo1, SimulaattorinSuureet.getInstance().getLapiKeskimaarainenAika(), "Arvo väärin");
		assertEquals(arvo8, SimulaattorinSuureet.getInstance().getErikoiset(), "Arvo väärin");
		assertEquals(arvo2, SimulaattorinSuureet.getInstance().getSimulaationSuoritusteho(), "Arvo väärin");
		assertEquals(arvo9, SimulaattorinSuureet.getInstance().getSimulaatiossaRikkoutuneet(), "Arvo väärin");
		assertEquals(arvo10, SimulaattorinSuureet.getInstance().getSimulaatiossaValmistuneet(), "Arvo väärin");
		assertEquals(arvo3, SimulaattorinSuureet.getInstance().getSimuloinninKokonaisaika(), "Arvo väärin");

	}
}
