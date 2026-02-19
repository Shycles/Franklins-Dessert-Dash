/**
 * Circle2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */
import java.awt.*;
import java.awt.geom.*;
public class Circle2D extends Shape2D
{

    private int diameter;
    
    public Circle2D () {
    	super(0, 0, 2);
    	this.diameter = 40;
    }

    /**
     * Constructor for objects of class Circle2D
     */
    public Circle2D(int fillColorIndex, int xPosition, int yPosition, int diameter)
    {
    	super(fillColorIndex, xPosition, yPosition);
        this.diameter = diameter;
    }


    /**
     * public void Draw(Graphics g)
     * 
     * Render the circle for both filled and outlined according to the states
     *
     * @param  - Graphics g is the graphics context
     * @return - void
     */
    public void Draw(Graphics g)
    {
    	Color fillColor = this.getFillColor();
    	int xPos = this.GetX();
    	int yPos = this.GetY();
    	if(this.getFill()) {
            g.setColor(fillColor);
            g.fillOval(xPos, yPos, diameter, diameter);
    	}
    	if(this.getOutline()) {
            g.setColor(this.getOutlineColor());
            g.drawOval(xPos, yPos, diameter, diameter);    		
    	}
    }
}
