package simu.model;


/**
 * Rajapinta tietokannan käsittelyn metodeja varten
 * @author Janne Lähteenmäki
 */
public interface ISuureetDAO {
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract boolean tallennaPpSuureet();
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract boolean tallennaSimSuureet(); 
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract void haePpSuureet(int ajoNumero);
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract void haeSimSuureet(int ajoNumero);
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract void poistaPpSuureita(int ajoNumero);
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract void poistaSimSuureita(int ajoNumero);
	/**
	 * Abstrakti metodiotsake jonka rajapintaa käyttävät luokat täydentäventävät omiin tarpeisiinsa 
	 */
	public abstract Integer[] getMontakoAjoa();
}
