/**
 * StarPoly2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */

public class StarPoly2D extends Polygon2D {
	private static int[] xCoords = { 0, 2, 10, 2, 0, -2, -10, -2};
    private static int[] yCoords = { -10, -2, 0, 2, 10, 2, 0, -2};
    
    /**
     * Constructor for the StarPoly2D Class
     * @param fillColorIndex, index for fill color
     * @param xPosition, x position
     * @param yPosition, y position
     */
	public StarPoly2D(int fillColorIndex, int xPosition, int yPosition) {
		super(fillColorIndex, xPosition, yPosition, xCoords, yCoords);
	}
}
