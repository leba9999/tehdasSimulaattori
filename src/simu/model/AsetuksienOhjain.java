package simu.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;


/**
 * Tallenetaan tiedostoon ja ladataan tiedostosta simulaattorin asetukset (pääasiassa tietokannan tiedot)
 * @author Leevi Koskinen
 *
 */
public class AsetuksienOhjain {

	/**
	 * tallentaa tiedostoon HashMapin, joka sisältää simulaattorin asetukset.
	 * HashMapin avaimena toimii String ja arvona Integers
	 */
	public boolean tallenna(HashMap<String, String> lista) {
		try (FileOutputStream tiedosto = new FileOutputStream("data/simulaattorinAsetukset.bin");
				ObjectOutputStream tuloste = new ObjectOutputStream(tiedosto);) {
			tuloste.writeObject(lista);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Lataa tiedostosta HashMapin, joka sisältää simulaattorin asetukset.
	 * HashMapin avaimena toimii String ja arvona Integer
	 */
	public HashMap<String, String> lataa() {
		try (FileInputStream tiedosto = new FileInputStream("data/simulaattorinAsetukset.bin");
				ObjectInputStream syote = new ObjectInputStream(tiedosto);) {
			return (HashMap<String, String>) syote.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
