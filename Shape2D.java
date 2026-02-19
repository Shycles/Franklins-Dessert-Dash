/**
 * Shape2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */
import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape2D {
	
	public final static int RED = 0;
	public final static int GREEN = 1;
	public final static int BLUE = 2;
	public final static int BLACK = 3;
	public final static int GREY = 4;
	public final static int WHITE = 5;
	public final static int YELLOW = 6;
	public final static int CYAN = 7;
	public final static int MAGENTA = 8;
	public final static int BROWN = 9;
	
    // RGB color table
    public static final Color[] COLORS = {
        //         R     G    B
        new Color(255,   0,   0),  // Red     0
        new Color(  0, 255,   0),  // Green   1
        new Color(  0,   0, 255),  // Blue    2
        new Color(  0,   0,   0),  // Black   3
        new Color(230, 220, 220),  // Grey    4
        new Color(255, 255, 255),  // White   5
        new Color(255, 255,   0),  // Yellow  6
        new Color(  0, 255, 255),  // Cyan    7
        new Color(219, 166, 196),  // Magenta 8 
        new Color(165,  42,  42),  // Brown   9
        new Color(255,  38,  38),
        new Color(255, 168,  38),
        new Color(212, 255,  38),
        new Color( 82, 255,  38),
        new Color( 38, 255, 125),
        new Color( 38, 255, 255),
        new Color( 38, 125, 255),
        new Color( 82,  38, 255),
        new Color(212,  38, 255),
        new Color(255,  38, 168),
    }; 
    
    private int xPos; // xPos, location on screen for x
    private int yPos; // yPos, location on screen for y
    private int xVel = 1; // xVel, velocity in x direction
    private int yVel = 1; // yVel, velocity in y direction
    private Color fillColor; // the fill color for Draw
    private int fillColorIndex; // the index of the color
    private Color outlineColor; // the outline color for Draw
    private int outlineColorIndex = 0; // the outline color index
    private boolean fill = true; // fill flag on/true, off/false
    private boolean outline = false; // outline flag on/true, off/false
    
	// Transformation attributes/data members for scale(deform), rotate(spin), translate(move)
    private double sX = 1.0; // Scale X
    private double sY = 1.0; // Scale Y
    private double rotAngleZ; // Rotation about the Z axis


    /**
     * Constructor for Shape2D class
     * @param fillColorIndex, index of the shape's fill color
     * @param xPosition, x position
     * @param yPosition, y position 
     */
	public Shape2D(int fillColorIndex, int xPosition, int yPosition)
    {
        this.fillColorIndex = fillColorIndex;
        this.fillColor = COLORS[fillColorIndex];     
        this.xPos = xPosition;
        this.yPos = yPosition;
        
        this.outlineColor = COLORS[outlineColorIndex];     

    }

    /**
     * Moves the shape by an amount (xDelta, yDelta)
     *
     * Move - translates the shape by an amount (xDelta, yDelta)
     *
     * @param  xDelta - amount to translate along the x axis
     *         yDelta - amount to translate along the y axis
     * @return None
     */
    public void Move(int xDelta, int yDelta)
    {
        //move the shape
        this.xPos += xDelta;
        this.yPos += yDelta;
    }

	/**
	 * abstract draw method that will be implemented differently by each shape
	 * @param g
	 */
	public abstract void Draw(Graphics g);

	/**
	 * getter for rotAngleZ
	 * @return rotAngleZ
	 */
	public double GetZRotate() {
		return this.rotAngleZ;
	}
	/**
	 * setter for rotAngleZ
	 */
	public void SetZRotate(double rotAngleZ) {
		this.rotAngleZ = rotAngleZ;
	}
	/**
	 * setter for the sX and sY
	 */
	public void SetScale(double x, double y) {
		this.sX = x;
		this.sY = y;
	}
	/**
	 * getter for sX
	 * @return sX
	 */
	public double GetScaleX() {
		return this.sX;
	}
	/**
	 * getter for sY
	 * @return sY
	 */
	public double GetScaleY() {
		return this.sY;
	}
	/**
	 * setter for the xPos and yPos
	 */
	public void SetPos(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	/**
	 * getter for x position
	 * @return the xPos
	 */
	public int GetX() {
		return xPos;
	}

	/**
	 * setter for x position
	 * @param xPos the xPos to set
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * getter for y position
	 * @return the yPos
	 */
	public int GetY() {
		return yPos;
	}

	/**
	 * setter for y position
	 * @param yPos the yPos to set
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	/**
	 * getter for x velocity
	 * @return the xVel
	 */
	public int getxVel() {
		return xVel;
	}

	/**
	 * setter for x velocity
	 * @param xVel the xVel to set
	 */
	public void setxVel(int xVel) {
		this.xVel = xVel;
	}

	/**
	 * getter for y velocity
	 * @return the yVel
	 */
	public int getyVel() {
		return yVel;
	}

	/**
	 * setter for y velocity
	 * @param yVel the yVel to set
	 */
	public void setyVel(int yVel) {
		this.yVel = yVel;
	}

	/**
	 * getter for the fillColor
	 * @return the fillColor
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * setter for the fillColor
	 * @param fillColor the fillColor to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * getter for the fillColorIndex
	 * @return the fillColorIndex
	 */
	public int getFillColorIndex() {
		return fillColorIndex;
	}

	/**
	 * setter for the fillColorIndex
	 * will also call the setFillColor
	 * @param fillColorIndex the fillColorIndex to set
	 */
	public void setFillColorIndex(int fillColorIndex) {
		this.fillColorIndex = fillColorIndex;
		this.setFillColor(this.COLORS[fillColorIndex]);

	}

	/**
	 * getter for the outlineColor
	 * @return the outlineColor
	 */
	public Color getOutlineColor() {
		return outlineColor;
	}

	/**
	 * setter for the outlineColor
	 * @param outlineColor the outlineColor to set
	 */
	public void SetOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	/**
	 * getter for the outlineColorIndex
	 * @return the outlineColorIndex
	 */
	public int getOutlineColorIndex() {
		return outlineColorIndex;
	}

	/**
	 * setter for the outlineColorIndex
	 * will also call the setOutlineColor
	 * @param outlineColorIndex the outlineColorIndex to set
	 */
	public void SetOutlineColorIndex(int outlineColorIndex) {
		this.outlineColorIndex = outlineColorIndex;
		this.SetOutlineColor(this.COLORS[outlineColorIndex]);
	}

	/**
	 * getter for the fill
	 * @return the fill
	 */
	public boolean getFill() {
		return fill;
	}

	/**
	 * setter for the fill
	 * @param fill the fill to set
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}

	/**\
	 * getter for the outline
	 * @return the outline
	 */
	public boolean getOutline() {
		return outline;
	}

	/**
	 * setter for the outline
	 * @param outline the outline to set
	 */
	public void SetOutline(boolean outline) {
		this.outline = outline;
	}

	/**
	 * setter for the xVel and yVel
	 * @param x
	 * @param y
	 */
	public void SetSpeed(int x, int y) {
        this.xVel = x;
        this.yVel = y;
    }
	
	/**
	 * move the shape according to the velocity
	 */
	public void Animate() {
        xPos += xVel;
        yPos += yVel;
    }

}
