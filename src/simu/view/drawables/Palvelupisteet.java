package simu.view.drawables;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Luoka määrittelee GraphicsContextiin palvelupisteen piirtämisen. Idea on yksin kertaistaa piirtovaihetta
 * kutsumalla vain yhtä metodia kun halutaan piirtää palvelupiste ja päivittää palvelupisteen piirto parametrejä (missä, koko ja väri).
 * 
 * @author Leevi Koskinen
 *
 */
public class Palvelupisteet {
	/**
	 * Jononpituus jolla lasketaan pallojen paikat palvelupisteen jonossa. 
	 */
	private int jononPituus;
	/**
	 * Palvelupisteen X koordinaatti kankaalla
	 */
	private double x;
	/**
	 * Palvelupisteen Y koordinaatti kankaalla
	 */
	private double y;
	/**
	 * Palvelupisteen säde
	 */
	private double radius;
	/**
	 * Palvelupisteen väri
	 */
	private Color color;
	/**
	 * Palvelupisteen nimi joka piiretään palvelupisteen ylä puolelle
	 */
	private String name;
	/**
	 * Tuotepallot jotka ovat jonossa palvelupisteelle
	 */
	private ArrayList<TuotePallo> pallot;
	/**
	 * Palvelupisteellä palveltava tuotepallo
	 */
	private TuotePallo palveltavaPallo;
	
	/**
	 * Konstruktori jossa asetetaan kaikki nollaan ja väri valkoiseksi
	 */
	public Palvelupisteet() {
		this.x = 0;
		this.y = 0;
		this.radius = 15;
		this.color = Color.WHITE;
		this.name = "";
		this.pallot = new ArrayList<>();
	}
	/**
	 * Hakee palvelupisteen X koordinaatin
	 * @return palauttaa palvelupisteen x koordinaatin
	 */
	public double getX() {
		return x;
	}

	/**
	 * Asettaa palvelupisteen X koordinaatin
	 * @param palvelupisteen x koordinaatti
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Hakee palvelupisteen Y koordinaatin
	 * @return palauttaa palvelupisteen y koordinaatin
	 */
	public double getY() {
		return y;
	}

	/**
	 * Asettaa palvelupisteen Y koordinaatin
	 * @param palvelupisteen y koordinaatti
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Hakee palvelupisteen värin
	 * @return color palvelupisteen väri
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * Asettaa palvelupisteen värin (color)
	 * @param color palvelupisteen väri
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Lisää tuotepallo palvelupisteen jonoon
	 * @param pallo joka lisätään jonoon
	 */
	public void lisaaPalloJonoon(TuotePallo pallo) {
		if(pallot.size() % 2 == 0) {
			pallo.setX((x - pallo.getRadius()*2) - (pallo.getRadius()*2 * jononPituus));
			pallo.setY(y);
		} else {
			pallo.setX((x - pallo.getRadius()*2)  - (pallo.getRadius()*2 * jononPituus));
			pallo.setY((y + radius)+pallo.getRadius());
			jononPituus++;
		}
		pallot.add(pallo);
	}
	/**
	 * Poistaa pallon jonosta
	 * @return TuotePallon joka poistettiin jonosta
	 */
	public TuotePallo poistaPalloJonosta() {
		TuotePallo pallo = null;
		try {
			pallo = pallot.remove(0);
			jononPituus = 0;
			for(int i = 0; i < pallot.size(); i++) {
				if(i % 2 == 0) {
					pallot.get(i).setX((x - pallot.get(i).getRadius()*2) - (pallot.get(i).getRadius()*2 * jononPituus));
					pallot.get(i).setY(y);
				} else {
					pallot.get(i).setX((x - pallot.get(i).getRadius()*2)  - (pallot.get(i).getRadius()*2 * jononPituus));
					pallot.get(i).setY((y + radius)+pallot.get(i).getRadius());
					jononPituus++;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pallo;
	}
	/**
	 * Palvelupiste "palvelee" palloa. Eli siirtää pallon keskelle palvelupistettä
	 * @param pallo joka siirretään palveltavaksi
	 */
	public void palvelePalloa(TuotePallo pallo) {
		palveltavaPallo = pallo;
		palveltavaPallo.setX(x + radius - pallo.getRadius());
		palveltavaPallo.setY(y + radius - pallo.getRadius());
	}
	/**
	 * Poistaa tuotepallon palvelupisteeltä
	 * @return TuotePallo joka poistettiin palvelupisteeltä
	 */
	public TuotePallo poistaPalvelustaPallo() {
		TuotePallo p = palveltavaPallo;
		palveltavaPallo = null;
		return p;
	}
	/**
	 * Hakee palvelupisteen nimen
	 * @return name palvelupisteen nimi
	 */
	public String getName() {
		return name;
	}
	/**
	 * Asettaa palvelupisteen nimen
	 * @param name palvelupisteen nimi
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Hakee palvelupisteen säteen (radius)
	 * @return palvelupisteen säde
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Asettaa palvelupisteen säteen (radius)
	 * @param palvelupisteen säde
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Piirtää määritellyn palvelupisteen GraphicsContextiin
	 * @param g GraphicsContext joka piirtää palvelupisteen
	 */
	public void draw(GraphicsContext g) {

		g.setFill(Color.BLACK);
		//g.setFont(new Font(10));
		g.fillText(name, (x - name.length()), y - 2);
        
        g.setFill(color);
        g.fillOval(x, y, radius * 2, radius * 2);
        g.strokeOval(x, y, radius * 2, radius * 2);

	}
}
