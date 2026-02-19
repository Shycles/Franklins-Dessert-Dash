/**
 * Polygon2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */
import java.awt.Color;
import java.awt.Graphics;

public class Polygon2D extends Shape2D {

    int [] xCoords;
    int [] yCoords;
    int [] txCoords;
    int [] tyCoords;
	
    /**
     * Constructor for Polygon2D Class
     * @param fillColorIndex, index for fill color
     * @param xPosition, x position
     * @param yPosition, y position
     * @param xCoords, x coordinate
     * @param yCoords, y coordinate
     */
	public Polygon2D(int fillColorIndex, int xPosition, int yPosition, int[] xCoords ,int[] yCoords) {
		super(fillColorIndex, xPosition, yPosition);
		// initialize the coordinates arrays 
		this.xCoords = new int[xCoords.length];
		this.yCoords = new int[yCoords.length];
		this.txCoords = new int[xCoords.length];
		this.tyCoords = new int[yCoords.length];
		
		for (int i = 0; i < xCoords.length; i++) {
			this.xCoords[i] = xCoords[i];
			this.txCoords[i] = xCoords[i] + xPosition;
		}

		for (int i = 0; i < yCoords.length; i++) {
			this.yCoords[i] = yCoords[i];
			this.tyCoords[i] = yCoords[i] + yPosition;
		}
		
		this.SetOutline(true);
		this.SetOutlineColorIndex(Shape2D.BLACK);
	}
	
	@Override
	/**
	 * Draws a polygon supporting both fill and outline color and transformation
	 */
	public void Draw(Graphics g) {
		Transform();
    	Color fillColor = this.getFillColor();
    	if(this.getFill()) {
            g.setColor(fillColor);
            g.fillPolygon(this.txCoords, this.tyCoords, this.xCoords.length);
    	}
    	if(this.getOutline()) {
            g.setColor(this.getOutlineColor());
            g.drawPolygon(this.txCoords, this.tyCoords, this.xCoords.length); 		
    	}
	}
	
	/**
	 * Supports scaling and rotation
	 */
	private void Transform() {
		double degs = super.GetZRotate();
		double rads = Math.toRadians(degs);
		double Sx = super.GetScaleX();
		double Sy = super.GetScaleY();
		for (int i = 0; i < xCoords.length; i++) {
			double x = Sx * this.xCoords[i];
			double y = Sy * this.yCoords[i];
			this.txCoords[i] = (int) (((x * Math.cos(rads) - y * Math.sin(rads)) + super.GetX()) + 0.5);
			this.tyCoords[i] = (int) (((x * Math.sin(rads) + y * Math.cos(rads)) + super.GetY()) + 0.5);
		}
	}

}
