package simu.framework;

import java.util.ArrayList;
import java.util.EnumMap;

import simu.model.Palvelupiste;
import simu.model.PalvelupisteTyyppi;
import simu.model.TapahtumaInfo;
import simu.model.TapahtumanTyyppi;
import simu.model.Tuote;

/**
 * Moottori ajaa simulaatiota.
 * 
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */

public abstract class Moottori {
	
	private double simulointiaika = 0;
	
	private Kello kello;
	
	/**
	 * Simulaattorin kaikki tapahtumat kerättynä yhteen listaan
	 */
	protected ArrayList<TapahtumaInfo> simulaationTapahtumat;
	
	protected Tapahtumalista tapahtumalista;
	/**
	 * EnumMappi jossa on kaiki palvelupisteet
	 */
	protected EnumMap<PalvelupisteTyyppi, Palvelupiste> palvelupisteet;
	

	public Moottori(){

		kello = Kello.getInstance(); // Otetaan kello muuttujaan yksinkertaistamaan koodia

		tapahtumalista = new Tapahtumalista();
		simulaationTapahtumat = new ArrayList<TapahtumaInfo>();
		
		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa 
		
		
	}

	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}
	
	
	public void aja(){
		alustukset(); // luodaan mm. ensimmäinen tapahtuma
		while (simuloidaan()){
			
			Trace.out(Trace.Level.INFO, "\nA-vaihe: kello on " + nykyaika());
			kello.setAika(nykyaika());
			
			Trace.out(Trace.Level.INFO, "\nB-vaihe:" );
			suoritaBTapahtumat();
			
			Trace.out(Trace.Level.INFO, "\nC-vaihe:" );
			yritaCTapahtumat();

		}
		tulokset();
		
	}
	
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}
	/**
	 * yritaCTapahtumat yrittää toteuttaa C tapahtumia ja jos tapahtuu tapahtuma tallennetaan se tallennaTapahtuma metodilla joka on
	 * toteutettu omaMoottorissa
	 */
	private void yritaCTapahtumat(){
		for (PalvelupisteTyyppi p: palvelupisteet.keySet()){
			if (!palvelupisteet.get(p).onVarattu() && palvelupisteet.get(p).onJonossa()){
				Tuote t = palvelupisteet.get(p).aloitaPalvelu();
				switch(palvelupisteet.get(p).getPisteenTyyppi()) {
				case KASAUSPISTE:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KPPA, Kello.getInstance().getAika(), t));
					break;
				case TESTAUSPISTE1:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP1PA, Kello.getInstance().getAika(), t));
					break;
				case TESTAUSPISTE2:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.TP2PA, Kello.getInstance().getAika(), t));
					break;
				case ERIKOISKALIBROINTI:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.EPPA, Kello.getInstance().getAika(), t));
					break;
				case PAKKAUSPISTE:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.PPPA, Kello.getInstance().getAika(), t));
					break;
				case LAADUNTARKASTUSPISTE:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.LPPA, Kello.getInstance().getAika(), t));
					break;
				case KORJAUSPISTE:
					tallennaTapahtuma(new TapahtumaInfo(TapahtumanTyyppi.KJPA, Kello.getInstance().getAika(), t));
					break;
					
				}
			}
		}
	}

	
	private double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	
	private boolean simuloidaan(){
		return kello.getAika() < simulointiaika;
	}
	
			

	protected abstract void alustukset(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract void suoritaTapahtuma(Tapahtuma t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void tulokset(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	/**
	 * Tallenna tapahtuma simulaationTapahtumat listaan
	 * @param t tapahtuma joka tapahtuu
	 */
	protected abstract void tallennaTapahtuma(TapahtumaInfo t); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
}