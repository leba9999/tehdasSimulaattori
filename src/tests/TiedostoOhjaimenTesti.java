package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import eduni.distributions.Uniform;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.ITiedostonOhjain;
import simu.model.TapahtumaInfo;
import simu.model.TapahtumanTyyppi;
import simu.model.TiedostonOhjain;
import simu.model.Tuote;

class TiedostoOhjaimenTesti {

	private static ITiedostonOhjain tiedostonOhjain;
	private static ArrayList<TapahtumaInfo> tapahtumat;
	@BeforeAll
	public static void alkuToimet() {
		Trace.setTraceLevel(Level.INFO);
		tiedostonOhjain = new TiedostonOhjain();
		Tuote t = new Tuote(new Uniform(1, 100));
		tapahtumat = new ArrayList<TapahtumaInfo>();
		tapahtumat.add(new TapahtumaInfo(TapahtumanTyyppi.KPARR, 100, t));
		tapahtumat.add(new TapahtumaInfo(TapahtumanTyyppi.KPPA, 100, t));
		tapahtumat.add(new TapahtumaInfo(TapahtumanTyyppi.KPDEP, 130, t));
	}
	@Test
	@Order(1)
	void tallennaDefault() {
		assertTrue(tiedostonOhjain.tallenna(tapahtumat), "Tallentaminen ei onnistunut, tarkista tiedosto polku!");
	}
	@Test
	@Order(2)
	void lataaDefault() {
		ArrayList<TapahtumaInfo> ladatutTapahtumat;
		ladatutTapahtumat = tiedostonOhjain.lataa();
		assertNotEquals(null, ladatutTapahtumat, "Tapahtumien lataus ei onnistunut, tarkista tiedosto polku!");
		
		assertEquals(tapahtumat.size(), ladatutTapahtumat.size(), "tapahtumat listan ja ladattujen tapahtumien koko ei täsmää");
		if(tapahtumat.size() != ladatutTapahtumat.size()) {
			return;
		}
		for(int i = 0; i < tapahtumat.size(); i++) {
			assertEquals(tapahtumat.get(i).getAika(), ladatutTapahtumat.get(i).getAika(),"Ladatun tiedoston tapahtuma aika ei vastannut oletettua aikaa");
			assertEquals(tapahtumat.get(i).getTyyppi(), ladatutTapahtumat.get(i).getTyyppi(),"Ladatun tiedoston tapahtuman tyyppi väärä");
			assertEquals(tapahtumat.get(i).getTuote().getId(), ladatutTapahtumat.get(i).getTuote().getId(),"Ladatun tiedoston tuotteen id ei ole sama");
			assertEquals(tapahtumat.get(i).getTuote().getRikkoutumisKerrat(), ladatutTapahtumat.get(i).getTuote().getRikkoutumisKerrat(),"Ladatun tuotteen rikkoutumis kerrat ei ole sama");
		}
	}
	@Test
	@Order(3)
	void tallennaNimella() {
		assertTrue(tiedostonOhjain.tallenna(tapahtumat, "data/Test.juustokumina"), "Tallentaminen ei onnistunut, tarkista tiedosto polku!");
	}
	@Test
	@Order(4)
	void lataaNimella() {
		ArrayList<TapahtumaInfo> ladatutTapahtumat;
		ladatutTapahtumat = tiedostonOhjain.lataa("data/Test.juustokumina");
		assertNotEquals(null, ladatutTapahtumat, "Tapahtumien lataus ei onnistunut, tarkista tiedosto polku!");
		
		assertEquals(tapahtumat.size(), ladatutTapahtumat.size(), "tapahtumat listan ja ladattujen tapahtumien koko ei täsmää");
		if(tapahtumat.size() != ladatutTapahtumat.size()) {
			return;
		}
		for(int i = 0; i < tapahtumat.size(); i++) {
			assertEquals(tapahtumat.get(i).getAika(), ladatutTapahtumat.get(i).getAika(),"Ladatun tiedoston tapahtuma aika ei vastannut oletettua aikaa");
			assertEquals(tapahtumat.get(i).getTyyppi(), ladatutTapahtumat.get(i).getTyyppi(),"Ladatun tiedoston tapahtuman tyyppi väärä");
			assertEquals(tapahtumat.get(i).getTuote().getId(), ladatutTapahtumat.get(i).getTuote().getId(),"Ladatun tiedoston tuotteen id ei ole sama");
			assertEquals(tapahtumat.get(i).getTuote().getRikkoutumisKerrat(), ladatutTapahtumat.get(i).getTuote().getRikkoutumisKerrat(),"Ladatun tuotteen rikkoutumis kerrat ei ole sama");
		}
	}

}
