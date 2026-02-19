/**
 * Sprite2D Class
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (v1.0 4-18-25)
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sprite2D extends Shape2D {

	private BufferedImage[] imageFrames;
	private int frame;
	boolean collide = false;
	boolean collected = false;

	// Parametric constructor
	/**
	 * Constructor for the Sprite2D Class
	 * @param xPos, x position
	 * @param yPos, y position
	 * @param imageFrames, image frame
	 */
	public Sprite2D(int xPos, int yPos, BufferedImage[] imageFrames) {
		super(0, xPos, yPos);
		this.imageFrames = new BufferedImage[imageFrames.length];
		for (int i = 0; i < imageFrames.length; i++) {
			this.imageFrames[i] = imageFrames[i];
		}
		frame = 0;
	}

	/**
	 * draws the current image/frame 
	 */
	@Override
	public void Draw(Graphics g) {
		// Draw the next Sprite frame if it wasn't collected already by Franklin
		if(!collected) {
			g.drawImage(imageFrames[frame], GetX(), GetY(), null);
		}
		frame++;
		if (frame == imageFrames.length) {
			frame = 0;
		}
	}
	
	/**
	 * get the width of the current image
	 * required for detecting collision
	 * @return the width of the current image
	 */
	public int GetWidth() {
		return imageFrames[frame].getWidth();
	}
	/**
	 * get the height of the current image
	 * required for detecting collision
	 * @return the height of the current image
	 */
	public int GetHeight() {
		return imageFrames[frame].getHeight();
	}
	/**
	 * set the collide state
	 * @param collide true if collision occurred
	 */
	public void SetCollide(boolean collide) {
		this.collide = collide;
	}
	/**
	 * return the collide state
	 * @return the collide state
	 */
	public boolean GetCollide() {
		return this.collide;
	}
	/**
	 * set the collected state
	 * @param collected true if the shape got collected
	 */
	public void SetCollected(boolean collected) {
		this.collected = collected;
	}
	/**
	 * return the collected state
	 * @return the collected state
	 */
	public boolean GetCollected() {
		return this.collected;
	}

}
