package simu.view;

import java.util.ArrayList;

import simu.model.TapahtumaInfo;

/**
 *
 * @author Leevi Koskinen
 *
 */
public interface IAnimaatioOhjain {
	/**
	 * Alustetaan tapahtumat, eli asetetaan halutut tapahtumat ja animaatio luodaan niiden perusteella
	 * @param tapahtumat jotka halutaan antaa animaatiolle
	 */
	public void alustaTapahtumat(ArrayList<TapahtumaInfo> tapahtumat);

	/**
	 * Päivitä animaatio seuraavien tapahtumien mukaiseksi, eli asetetaan tuote palloja palvelupisteille ja reitille
	 */
	public void paivita();
	/**
	 * Piirrä kankaalle animaatio kuva
	 */
	public void piirra();
	/**
	 * Aseta animaation toisto päälle
	 */
	public void toista();
	/**
	 * Pysäytä animaation toistaminen
	 */
	public void pysayta();

	/**
	 * Vähennetään animaation toisto nopeutta
	 */
	public void vahennaNopeutta();
	/**
	 * Lisätään animaation toisto nopeutta
	 */
	public void lisaaNopeutta();
	/**
	 * Hypätään seuraavaan animaatio askeleeseen/kuvaan
	 */
	public void seuraavaAskel();
	/**
	 * Hypätään edelliseen animaatio askeleeseen/kuvaan
	 */
	public void edellinenAskel();

	/**
	 * Metodilla saadaan selville onko animaation toistaminen käynnissä
	 * 
	 * @return Palauttaa animaation toiston
	 */
	public boolean getToisto();
	/**
	 * Metodilla saadaan selville animaation toisto nopeus
	 * 
	 * @return Palauttaa animaation nopeuden
	 */
	public double getNopeus();
	/**
	 * Haetaan animaation toisto suunta, false = "taaksepäin", true = "eteenpäin"
	 * @return suunta johon animaatio kulkee
	 */
	public boolean getSuunta();
	/**
	 * Asetetaan animaation toisto suunta, false = "taaksepäin", true = "eteenpäin"
	 * @param suunta johon animaatio kulkee
	 */
	public void setSuunta(boolean suunta);
	/**
	 * Haetaan seuraavan tapahtuman indeksi numero eli mikä tapahtuma pitäisi seuraavaksi tapahtua
	 * @return seuraavaTapahtuma:n indeksi numero
	 */
	public int getSeuraavatapahtuma();
	/**
	 * Asetetaan seuraavatapahtuma
	 * @param seuraava tapahtuman indeksi
	 */
	public void setSeuraavatapahtuma(int seuraava);
}
