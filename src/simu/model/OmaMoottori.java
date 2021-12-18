package simu.model;

import java.util.EnumMap;

import eduni.distributions.Normal;
import eduni.distributions.Uniform;
import simu.MainApp;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;
/**
 * OmaMoottori toteuttaa Moottorissa määritellyt abstraktti metodit. Luokka toimii simulaation moottorina
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */
public class OmaMoottori extends Moottori implements Runnable {

	private Saapumisprosessi saapumisprosessi;
	private int valmistuneetTuotteet;

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
	public OmaMoottori() {

		SimulaattorinParametrit s = SimulaattorinParametrit.getInstance();
		
		palvelupisteet = new EnumMap<>(PalvelupisteTyyppi.class);
		palvelupisteet.put(PalvelupisteTyyppi.KASAUSPISTE,  new Palvelupiste(new Uniform(s.getKasauspisteenKMAika() - (s.getKasauspisteenKMAika()/2), s.getKasauspisteenKMAika() + (s.getKasauspisteenKMAika()/2)), tapahtumalista, TapahtumanTyyppi.KPDEP, PalvelupisteTyyppi.KASAUSPISTE));
		palvelupisteet.put(PalvelupisteTyyppi.TESTAUSPISTE1, new Palvelupiste(new Uniform(s.getTest1KMAika() - (s.getTest1KMAika()/2), s.getTest1KMAika() + (s.getTest1KMAika()/2)), tapahtumalista, TapahtumanTyyppi.TP1DEP, PalvelupisteTyyppi.TESTAUSPISTE1));
		palvelupisteet.put(PalvelupisteTyyppi.TESTAUSPISTE2,  new Palvelupiste(new Uniform(s.getTest2KMAika() - (s.getTest2KMAika()/2), s.getTest2KMAika() + (s.getTest2KMAika()/2)), tapahtumalista, TapahtumanTyyppi.TP2DEP, PalvelupisteTyyppi.TESTAUSPISTE2));
		palvelupisteet.put(PalvelupisteTyyppi.ERIKOISKALIBROINTI,  new Palvelupiste(new Normal(s.getErikoiskalibKMAika(), s.getErikoiskalibKMAika()/3), tapahtumalista, TapahtumanTyyppi.EPDEP, PalvelupisteTyyppi.ERIKOISKALIBROINTI));
		palvelupisteet.put(PalvelupisteTyyppi.PAKKAUSPISTE,  new Palvelupiste(new Uniform(s.getPakkauspisteenKMAika() - (s.getPakkauspisteenKMAika()/2), s.getPakkauspisteenKMAika() + (s.getPakkauspisteenKMAika()/2)), tapahtumalista, TapahtumanTyyppi.PPDEP, PalvelupisteTyyppi.PAKKAUSPISTE));
		palvelupisteet.put(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE,  new Palvelupiste(new Uniform(s.getLaaduntarkKMAika() - (s.getLaaduntarkKMAika()/2), s.getLaaduntarkKMAika() + (s.getLaaduntarkKMAika()/2)), tapahtumalista, TapahtumanTyyppi.LPDEP, PalvelupisteTyyppi.LAADUNTARKASTUSPISTE));
		palvelupisteet.put(PalvelupisteTyyppi.KORJAUSPISTE,  new Palvelupiste(new Normal(s.getKorjauspisteenKMAika(), s.getKorjauspisteenKMAika()/3), tapahtumalista, TapahtumanTyyppi.KJPDEP, PalvelupisteTyyppi.KORJAUSPISTE));

		saapumisprosessi = new Saapumisprosessi(new Normal(15, 5), tapahtumalista, TapahtumanTyyppi.ARR1);
	}

	@Override
	protected void alustukset() {
		Kello.getInstance().setAika(0);
		SimulaattorinSuureet.getInstance().alustaSuureet();
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Tapahtuma t) { // B-vaiheen tapahtumat

		Tuote a;
		switch (t.getTyyppi()) {

		case ARR1:
			a = new Tuote(new Uniform(1, 100));
			palvelupisteet.get(PalvelupisteTyyppi.KASAUSPISTE).lisaaJonoon(a);
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KPARR, Kello.getInstance().getAika(), a));
			saapumisprosessi.generoiSeuraava();
			break;
		case KPDEP:
			// Pitää jotenkin päättää kunpaan testauspisteeseen siiretään.
			a = palvelupisteet.get(PalvelupisteTyyppi.KASAUSPISTE).otaJonosta();
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KPDEP, Kello.getInstance().getAika(), a));
			// if-elsessä lajitellaan tuotteet testauspisteisiinsä
			if(palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE1).compareTo(palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE2)) > 0) {
				palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE2).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.TESTAUSPISTE2));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP2ARR, Kello.getInstance().getAika(), a));
			} else {
				palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE1).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.TESTAUSPISTE1));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP1ARR, Kello.getInstance().getAika(), a));
			}
			break;
		case TP1DEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE1).otaJonosta();
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP1DEP, Kello.getInstance().getAika(), a));
			if (a.getOnkoRikki()) {
				palvelupisteet.get(PalvelupisteTyyppi.KORJAUSPISTE).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.KORJAUSPISTE));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KJARR, Kello.getInstance().getAika(), a));
			} else if(a.viedaakoErikoiskalibrointiin()) {
				System.err.println("ERIKOISKALIPROINTIIN! tuote : " +a.getId());
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.ERIKOISKALIBROINTI));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.EPARR, Kello.getInstance().getAika(), a));
				palvelupisteet.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).lisaaJonoon(a);
			} else {
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.PAKKAUSPISTE));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.PPARR, Kello.getInstance().getAika(), a));
				palvelupisteet.get(PalvelupisteTyyppi.PAKKAUSPISTE).lisaaJonoon(a);
			}
			break;
		case TP2DEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE2).otaJonosta();
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP2DEP, Kello.getInstance().getAika(), a));
			if (a.getOnkoRikki()) {
				palvelupisteet.get(PalvelupisteTyyppi.KORJAUSPISTE).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.KORJAUSPISTE));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KJARR, Kello.getInstance().getAika(), a));
			} else if(a.viedaakoErikoiskalibrointiin()) {
				System.err.println("ERIKOISKALIPROINTIIN! tuote : " +a.getId());
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.ERIKOISKALIBROINTI));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.EPARR, Kello.getInstance().getAika(), a));
				palvelupisteet.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).lisaaJonoon(a);
			} else {
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.PAKKAUSPISTE));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.PPARR, Kello.getInstance().getAika(), a));
				palvelupisteet.get(PalvelupisteTyyppi.PAKKAUSPISTE).lisaaJonoon(a);
			}
			break;
		case EPDEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.ERIKOISKALIBROINTI).otaJonosta();
			palvelupisteet.get(PalvelupisteTyyppi.PAKKAUSPISTE).lisaaJonoon(a);
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.EPDEP, Kello.getInstance().getAika(), a));
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a));
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.PPARR, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.PAKKAUSPISTE));
			break;
		case PPDEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.PAKKAUSPISTE).otaJonosta();
			palvelupisteet.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).lisaaJonoon(a);
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.PPDEP, Kello.getInstance().getAika(), a));
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.LAADUNTARKASTUSPISTE));
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.LPARR, Kello.getInstance().getAika(), a));
			break;
		case KJPDEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.KORJAUSPISTE).otaJonosta();
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KJPDEP, Kello.getInstance().getAika(), a));
			a.setOnkoRikki(false);
			if(palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE1).compareTo(palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE2)) > 0) {
				palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE2).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.TESTAUSPISTE2));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP2ARR, Kello.getInstance().getAika(), a));
			} else {
				palvelupisteet.get(PalvelupisteTyyppi.TESTAUSPISTE1).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.TESTAUSPISTE1));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP1ARR, Kello.getInstance().getAika(), a));
			}
			break;
		case LPDEP:
			a = palvelupisteet.get(PalvelupisteTyyppi.LAADUNTARKASTUSPISTE).otaJonosta();
			tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.LPDEP, Kello.getInstance().getAika(), a));
			if(a.getOnkoRikki()) {
				palvelupisteet.get(PalvelupisteTyyppi.KORJAUSPISTE).lisaaJonoon(a);
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.AN, Kello.getInstance().getAika(), a, PalvelupisteTyyppi.KORJAUSPISTE));
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KJARR, Kello.getInstance().getAika(), a));
			} else {
				valmistuneetTuotteet++;
				a.setPoistumisaika(Kello.getInstance().getAika());
				a.raportti();
				tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.EX, Kello.getInstance().getAika(), a));
			}
			break;
		default:
			System.out.println("roskiin");
			break;
		}
	}
	/**
	 * Näyttää simulaation tulokset, tallentaa suureet SimulaattorinSuureet singletoniin ja väliaikaiseen tiedostoon
	 */
	@Override
	protected void tulokset() {
		System.out.println("Simulointi päättyi kello " + Kello.getInstance().getAika());
		System.out.println("Tulokset ... puuttuvat vielä");
		System.out.println("Jononpituus: " + palvelupisteet.get(PalvelupisteTyyppi.KASAUSPISTE).getKeskimaarainenJononpituus());
		System.out.println("Valmistuneiden tuotteiden määrä: " + valmistuneetTuotteet);
		
		TiedostonOhjain ohjain = new TiedostonOhjain();
		ohjain.tallenna(simulaationTapahtumat);
		
		SimulaattorinSuureet.getInstance().setSimuloinninKokonaisAika(Kello.getInstance().getAika());
		SimulaattorinSuureet.getInstance().setSimulaatiossaValmistuneet(valmistuneetTuotteet);
		SimulaattorinSuureet.getInstance().setSimulaatiossaRikkoutuneet(Palvelupiste.rikkiMenneetTuotteet());
		SimulaattorinSuureet.getInstance().setSimulaationSuoritusteho(valmistuneetTuotteet / Kello.getInstance().getAika());

		mainApp.lataaUusiSimulaatio();
	}
	
	/**
	 * Tallenna simulaattorin tapahtuma simulaationTapahtuma listaan
	 * @param t tapahtuma joka tallennetaan
	 */
	@Override
	protected void tallennaTapahtuma(TapahtumaInfo t) {

		SimulaattorinSuureet.getInstance().setSimuloinninKokonaisAika(Kello.getInstance().getAika());
		SimulaattorinSuureet.getInstance().setSimulaatiossaValmistuneet(valmistuneetTuotteet);
		SimulaattorinSuureet.getInstance().setSimulaatiossaRikkoutuneet(Palvelupiste.rikkiMenneetTuotteet());
		SimulaattorinSuureet.getInstance().setSimulaationSuoritusteho(valmistuneetTuotteet / Kello.getInstance().getAika());
		
		for(PalvelupisteTyyppi pt : PalvelupisteTyyppi.values()) {
            palvelupisteet.get(pt).suureetTalteen();
        }
		
		SimulaattorinSuureet s = SimulaattorinSuureet.getInstance();
		
		t.setSimulaatiossaValmistuneet(s.getSimulaatiossaValmistuneet());
		t.setSimuloinninKokonaisaika(s.getSimuloinninKokonaisaika());
		t.setLapimenneidenKeskiaika(s.getLapiKeskimaarainenAika());
		t.setSimulaationSuoritusteho(s.getSimulaationSuoritusteho());
		t.setSimulaatiossaRikkoutuneet(s.getSimulaatiossaRikkoutuneet());
		t.setErikoiset(s.getErikoiset());
		
		simulaationTapahtumat.add(t);
		
	}

	@Override
	public void run() {
		super.aja();
	}
}
