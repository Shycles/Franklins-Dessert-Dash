/**
 * Rectangle2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */
import java.awt.*;
import java.awt.geom.*;
public class Rectangle2D extends Shape2D
{

    private int width;
    private int height;
    
    /**
     * Constructor for objects of Rectangle2D Circle
     */
    public Rectangle2D(int fillColorIndex, int xPosition, int yPosition, int width, int height)
    {
    	super(fillColorIndex, xPosition, yPosition);
        this.width = width;
        this.height = height;
    }


    /**
     * public void Draw(Graphics g)
     * 
     * Render the rectangle for both filled and outlined according to the states
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
            g.fillRect(xPos, yPos, width, height);
    	}
    	if(this.getOutline()) {
            g.setColor(this.getOutlineColor());
            g.drawRect(xPos, yPos, width, height); 		
    	}
    }
}
