package simu.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Tallenetaan tiedostoon ja ladataan tiedostosta simulaattorin tapahtuma lista
 * @author Leevi Koskinen, Sanna Kukkonen, Janne Lähteenmäki
 *
 */

public class TiedostonOhjain implements ITiedostonOhjain {

	/**
	 * Sisältää versio numeron, jonka avulla tunnistetaan onko ladattu tiedosto yhteen sopiva 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Tallentaa simulaattorin tapahtumia sisältävän TapahtumaInfo Arraylistan tiedostoon 
	 */
	@Override
	public boolean tallenna(ArrayList<TapahtumaInfo> lista) {
		try (FileOutputStream tiedosto = new FileOutputStream("data/simulaationTapahtumat.juustokumina");
				ObjectOutputStream tuloste = new ObjectOutputStream(tiedosto);) {
			tuloste.writeObject(lista);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Lataa tiedostosta TapahtumaInfo Arraylistan, joka sisältää simulaattorin tapahtumia
	 */
	@Override
	public ArrayList<TapahtumaInfo> lataa() {
		try (FileInputStream tiedosto = new FileInputStream("data/simulaationTapahtumat.juustokumina");
				ObjectInputStream syote = new ObjectInputStream(tiedosto);) {
			return (ArrayList<TapahtumaInfo>) syote.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Tallentaa simulaattorin tapahtumia sisältävän TapahtumaInfo Arraylistan haluttuun tiedostoon/polkuun
	 */
	@Override
	public boolean tallenna(ArrayList<TapahtumaInfo> lista, String osoite) {
		try (FileOutputStream tiedosto = new FileOutputStream(osoite);
				ObjectOutputStream tuloste = new ObjectOutputStream(tiedosto);) {
			tuloste.writeObject(lista);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Lataa halutusta tiedostosta TapahtumaInfo Arraylistan, joka sisältää simulaattorin tapahtumia
	 */
	@Override
	public ArrayList<TapahtumaInfo> lataa(String osoite) {
		try (FileInputStream tiedosto = new FileInputStream(osoite);
				ObjectInputStream syote = new ObjectInputStream(tiedosto);) {
			return (ArrayList<TapahtumaInfo>) syote.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
