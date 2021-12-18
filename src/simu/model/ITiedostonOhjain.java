package simu.model;

import java.util.ArrayList;

/**
 * Tiedosto ohjaimen rajapinta jolla halutaan määritellä muutama pakollinen metodi tiedostonOhjaimille
 * @author Leevi Koskinen
 */

public interface ITiedostonOhjain {
	/**
	 * Tallenna oletettuun (default) polkuun ja nimellä joka on valmiiksi määritetty
	 * @param lista mitä tapahtumia halutaan tallentaa tiedostoon
	 * @return onnistuiko tiedostoon tallentaminen
	 */
	public boolean tallenna( ArrayList<TapahtumaInfo> lista);
	/**
	 * Tallenna haluttuun polkuun ja nimellä joka on määritetty osoitteessa
	 * @param lista mitä tapahtumia halutaan tallentaa tiedostoon
	 * @param osoite minne ja millä nimellä halutaan tallentaa
	 * @return onnistuiko tiedostoon tallentaminen
	 */
	public boolean tallenna( ArrayList<TapahtumaInfo> lista, String osoite);
	/**
	 * Lataa oletetusta (default) tiedostosta tapahtumaInfo listan 
	 * @return palauttaa tapahtumaInfo listan tai jos ei onnistunut lataus niin null
	 */
	public ArrayList<TapahtumaInfo> lataa();
	/**
	 * Lataa halutusta tiedostosta tapahtumaInfo listan 
	 * @param osoite mistä ja millä nimellä löytdetään tiedosto
	 * @return palauttaa tapahtumaInfo listan tai jos ei onnistunut lataus niin null
	 */
	public ArrayList<TapahtumaInfo> lataa(String osoite);
}
