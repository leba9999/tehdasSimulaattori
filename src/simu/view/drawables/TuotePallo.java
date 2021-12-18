package simu.view.drawables;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Luoka määrittelee GraphicsContextiin pallon piirtämisen. Idea on yksin kertaistaa piirtovaihetta
 * kutsumalla vain yhtä metodia kun halutaan piirtää pallo ja päivittää pallon piirto parametrejä (missä, koko ja väri).
 * 
 * @author Leevi Koskinen
 *
 */

public class TuotePallo {
	/**
	 * Tunniste minkä tuotteen pallo on. Eli on sama id kuin tuotteella jota pallo esittää
	 */
    private int id;
    /**
     * Pallon X koordinaatti kankaalla
     */
	private double x;
	/**
	 * Pallon Y koordinaatti kankaalla
	 */
	private double y;
	/**
	 * Pallon säde
	 */
	private double radius;
	/**
	 * Pallon väri
	 */
	private Color color;
	
	/**
	 * Konstruktori jossa määritellään kaikki arvot (x, y, radius) nollaksi ja väri mustaksi
	 */
	public TuotePallo(){
		this.x = 0;
		this.y = 0;
		this.radius = 0;
		this.color = Color.BLACK;
	}
	/**
	 * Hakee pallon X koordinaatin
	 * @return palauttaa pallon x koordinaatin
	 */
	public double getX() {
		return x;
	}

	/**
	 * Asettaa pallon X koordinaatin
	 * @param pallon x koordinaatti
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Hakee pallon Y koordinaatin
	 * @return palauttaa pallon y koordinaatin
	 */
	public double getY() {
		return y;
	}

	/**
	 * Asettaa pallon Y koordinaatin
	 * @param pallon y koordinaatti
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Hakee pallon säteen (radius)
	 * @return pallon säde
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Asettaa pallon säteen (radius)
	 * @param pallon säde
	 */

	public void setRadius(double radius) {
		this.radius = radius;
	}
	/**
	 * Asettaa pallon X ja Y koordinaatin yhtä aikaa, niin ei tarvitse kutsua erikseen getX ja getY metodeja 
	 * @param x pallon koordinaatti
	 * @param y pallon koordinaatti
	 */
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Hakee pallon värin
	 * @return color pallon väri
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * Asettaa pallon värin (color)
	 * @param color pallon väri
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Piirtää määritellyn pallon GraphicsContextiin
	 * @param g GraphicsContext joka piirtää pallon
	 */
	public void draw(GraphicsContext g) {
        g.setFill(color);
        g.fillOval(x, y, radius*2, radius*2);
	}
	
	/**
	 * Hakee pallon tunnisteen id:n
	 * @return id tunnisteen
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Asetetaan pallon tunniste
	 * @param id tunniste
	 */
	public void setId(int id) {
		this.id = id;
	}
}
