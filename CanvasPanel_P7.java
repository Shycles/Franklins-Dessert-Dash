/**
 * 2D CanvasPanel
 * 
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version v1.0 4/18/25
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

// For Sprites 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CanvasPanel_P7 extends JPanel {
	// canvas data
    private final static int X_CORNER = 5;
    private final static int Y_CORNER = 5;
    private final static int CANVAS_WIDTH = 920;
    private final static int CANVAS_HEIGHT = 620;

    // Indices of dynamic shapes
    private final static int FRANKLIN_SPRITE = 0;
    private final static int CARROT_SPRITE = 1;
    private final static int STRAWBERRY_SPRITE = 2;
    private final static int UPPER_STRAWBERRY_SPRITE = 3;
    private final static int ONION_SPRITE = 4;
    private final static int UPPER_ONION_SPRITE = 5;
    private final static int CAKE_SPRITE_START = 6;

    private List<Shape2D>   shapesList;	// list of shapes used
    
    private boolean action;		// start or pause the game
    private boolean jumpUp;		// Franklin jumping
    private boolean fallDown;	// Franklin falling down
    private int frameNumber;	// frame number
    private int score = 0;		// score - how many collectibles were collected vs how onion points deducted

    // audio players
    private audioPlayer collectSound = new audioPlayer("collectionSound.wav");
    private audioPlayer failSound = new audioPlayer("fail.wav");
    private audioPlayer oughSound = new audioPlayer("ough1.wav");

    private Timer renderLoop = null;	// the render loop, we need to have a reference to it 
    private long gameTime = 0;			// how long the game was played
    private long currentTime = 0;		// used to calculate the gameTime
    private final int MOVE_LEFT_SPEED = -3;
	
    // enum for game state
	enum GameState {
	    START_SCREEN,
	    PLAYING,
	    END_SCREEN
	}
	
	private GameState gameState;	// holds the current game state
	
	/**
	 * CanvasPanel_P7 default constructor
	 */
    public CanvasPanel_P7()
    {
    	this.init();

        // Callback from keyboard events
        this.setFocusable(true);
        this.addKeyListener(new myActionListener());
        System.out.println("keyboard event registered");

        // Create a render loop
        // Create a Swing Timer that will tick 30 times a second
        // At each tick the ActionListener that was registered via the lambda expression will be invoked
        renderLoop = new Timer(30, (ActionEvent ev) -> {frameNumber++; Simulate(); repaint();}); // lambda expression for ActionListener implements actionPerformed
        renderLoop.start();
    }
    
    /**
     * will initialize the shapes, score, etc.
     * will be called when we first trigger the game or when we reset and start over.
     */
    private void init()
    {   
        shapesList = new ArrayList<>();
        action = false;		// the game is not active
        gameState = GameState.START_SCREEN;		// starting from the start screen
        gameTime = 0;		// reset the game time		
        currentTime = 0;	// reset the current time
        
        // generate and add the Franklin Sprite
        this.generateFranklin();

        // generate and add the  carrot sprite
        this.addSingleFrameSprite("carrotPixel.png", 500, 449);

        // generate and add the strawberry sprite
        this.addSingleFrameSprite("pixelStrawberry.png", 20, 449);	// strawberry on cake
        this.addSingleFrameSprite("pixelStrawberry.png", 200, 200);	// flying strawberry

        
        // generate and add the  Onion sprite
        this.addSingleFrameSprite("onion.png", 400, 449);	// onion on cake
        this.addSingleFrameSprite("onion.png", 10, 300);	// flying onion

        
        // generate and add the 15 pieces of cake,
        // with 75 pixels delta on the x axis to simulate a continuous cake
        int cakePosY = 509;
        for (int i = 0; i < 15; i++) {
            this.addSingleFrameSprite("cakeLarge.png", i*75, cakePosY);
		}
	}
    
    /**
     * generating the Franklin sprite (multi frame sprite) and adding it to the shapes list
     */
    private void generateFranklin() {
        BufferedImage[] Franklin_Sprites = new BufferedImage[12];
        try {
        	for (int i = 0; i < Franklin_Sprites.length; i++) {
        		int j = i/3 + 1;	// adding each frame 3 times for better animation
        		String fileName = "rabbitsz_"+j+".png";
                Franklin_Sprites[i] = ImageIO.read(new File(fileName));
			}
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        shapesList.add(new Sprite2D(100, 399, Franklin_Sprites));
    }
    
    /**
     * generating a single frame sprite and adding it to the shapes list
     * @param fileName - the file name of the single image
     * @param xPos - the X position for the sprite
     * @param yPos - the Y position for the sprite
     */
    private void addSingleFrameSprite(String fileName, int xPos, int yPos) {
        BufferedImage[] sprite = new BufferedImage[1];
        try {
        	sprite[0] = ImageIO.read(new File(fileName));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        shapesList.add(new Sprite2D(xPos, yPos, sprite));
    }

    /**
     * Simulate, animate all the shapes
     */
    private void Simulate()
    {
    	// if the game is active, animate all the shapes
        if (action)
        {
        	this.SimulateFranklin();
        	this.moveSprite(CARROT_SPRITE, MOVE_LEFT_SPEED);
        	this.moveSprite(STRAWBERRY_SPRITE, MOVE_LEFT_SPEED);
        	this.moveSprite(UPPER_STRAWBERRY_SPRITE, MOVE_LEFT_SPEED-3);
        	this.moveSprite(ONION_SPRITE, MOVE_LEFT_SPEED);
        	this.moveSprite(UPPER_ONION_SPRITE, MOVE_LEFT_SPEED-5);
        	this.SimulateCake();
        }
    }

    /**
     * special function to handle Franklin's movement and intersections
     */
	private void SimulateFranklin() {
		Shape2D shape = shapesList.get(FRANKLIN_SPRITE);
		if (jumpUp) {
			// if Franklin is currently jumping
			shape.SetSpeed(0, -10);		// reduce the Y by 10 to make Franklin go up
			shape.Animate();			// change the velocity
			if (shape.GetY() < 100) {
				// if Franklin reached the upper limit, switch to falling
				jumpUp = false;
				fallDown = true;
			}
		}
		if (fallDown) {
			// if Franklin is currently falling
			if (shape.GetY() < 399) {
				// if didn't reach the cake yet, keep falling
				shape.SetSpeed(0, 10);	// increase the Y by 10 to make Franklin go down
				shape.Animate();		// change the velocity
			} else {
				// Franklin reached the cake, stop falling
				fallDown = false;
				shape.SetSpeed(0, 0);
			}
		}
			
		// check and handle collision between Franklin and the carrot 
		boolean collide = this.checkIfSpritesCollide(FRANKLIN_SPRITE, CARROT_SPRITE);
		if(collide) {	// if Franklin hit a carrot, it's game over
			// play fail sound
			Thread collectSoundThread = new Thread(failSound);
        	collectSoundThread.start();
        	action = false;		// the game is no longer active
        	gameState = GameState.END_SCREEN;	// switch to End Screen
        	
        	// calculate the game time
            long currentTimeMillis = System.currentTimeMillis();
            gameTime += currentTimeMillis - currentTime;
		}
		
		// detect and handle collision with the strawberries
		int[] collectibles = {STRAWBERRY_SPRITE, UPPER_STRAWBERRY_SPRITE};
		this.handleCollectibles(collectibles, true);

		// detect and handle collision with the onions
		int[] obsticles = {ONION_SPRITE, UPPER_ONION_SPRITE};
		this.handleCollectibles(obsticles, false);
	}

	/**
	 * Genric method to check and handle collision between Franklin and the given list of shapes
	 * @param collectibles - the list of shapes we want to check
	 * @param goodCollection - is that a good collection that increases the score or a bad one which will reduce the score
	 */
	private void handleCollectibles(int[] collectibles, boolean goodCollection) {
		int multiplier = goodCollection ? 1 : -1;
		for (int i = 0; i < collectibles.length; i++) {
			boolean collide = this.checkIfSpritesCollide(FRANKLIN_SPRITE, collectibles[i]);
			Sprite2D shape = (Sprite2D)shapesList.get(collectibles[i]);
			if(collide) {
				if(!shape.GetCollide()) {
					shape.SetCollide(true);
					shape.SetCollected(true);
				    audioPlayer sound = collectSound;

					if(multiplier < 0) {
					    sound = oughSound;
					}
					Thread soundThread = new Thread(sound);
					soundThread.start();
		        	score = score + 1*multiplier;
				}
			} else {
				shape.SetCollide(false);
			}
			
			if(shape.GetX() < -50) {
				shape.SetCollected(false);
			}
		}
	}
	
	/**
	 * Check if two shapes had collide
	 * @param shapeA - the index of the first shape
	 * @param shapeB - the index of the second shape
	 * @return
	 */
	private boolean checkIfSpritesCollide(int shapeA, int shapeB) {
		Sprite2D spriteA = (Sprite2D)shapesList.get(shapeA); // cast from Shape2D to Sprite2D
		int spriteAX = spriteA.GetX();
		int spriteAY = spriteA.GetY();
		int spriteAW = spriteA.GetWidth();
		int spriteAH = spriteA.GetHeight();
		
		Sprite2D spriteB = (Sprite2D)shapesList.get(shapeB); // cast from Shape2D to Sprite2D
		int spriteBX = spriteB.GetX();
		int spriteBY = spriteB.GetY();
		int spriteBW = spriteB.GetWidth();
		int spriteBH = spriteB.GetHeight();
		
		// check for collision 
		boolean c1 = intervalIntersect(spriteBX, spriteBX + spriteBW, spriteAX, spriteAX + spriteAW);
		boolean c2 = intervalIntersect(spriteBY, spriteBY + spriteBH, spriteAY, spriteAY + spriteAH);
		boolean collide = c1 && c2;
		return collide;
	}
	
	/**
	 * Generic method to move shapes left 
	 * @param shapeIndex - the index of the shape from the shapesList that we want to move
	 */
	private void moveSprite(int shapeIndex, int xDelta) {
		Shape2D shape = shapesList.get(shapeIndex);

		int leftEdge = -75;
		int rightEdge = 920;
		shape.Move(xDelta, 0);	// move the shape based on the xDelta value
		int cx = shape.GetX();
		if (cx < leftEdge) {
			// if the shape exited from the left, bring it back from the right
			// and randomly change the X location
			int y = shape.GetY();
			Random random = new Random();
			int randomNumber = random.nextInt(500);
			shape.SetPos(rightEdge + randomNumber, y);
		}
	}

	/**
	 * simulate the cake, special handling as we have multiple pieces of cake
	 */
	private void SimulateCake() {
		int leftEdge = -75;
		int rightEdge = 920;
		// move the cake
		for (int i = 0; i < 15; i++) {
			Shape2D shape = shapesList.get(CAKE_SPRITE_START + i);
			shape.Move(MOVE_LEFT_SPEED, 0); // move carrot in x direction
			int cx = shape.GetX();
			if (cx < leftEdge) {
				int y = shape.GetY();
				shape.SetPos(rightEdge, y);
			}
		}
	}

	/**
	 * check for intersection
	 * spriteBX, spriteBX + spriteBW, spriteAX, spriteAX + spriteAW
	 * @param a - shape1 X/Y pos
	 * @param b - shape1 X/Y pos + its width/height
	 * @param c - shape2 X/Y pos
	 * @param d - shape2 X/Y pos + its width/height
	 * @return true if intersection detected
	 */
	boolean intervalIntersect(int a, int b, int c, int d) {
		boolean intersect = true;
		if ((a > d) || (c > b)) {
			intersect = false;
		}
		return intersect;
	}

	/**
	 * Paint the component
	 * This method is called by renderloop
	 */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set canvas background to grey
        g.setColor(Shape2D.COLORS[Shape2D.GREY]);
        g.fillRect(X_CORNER, Y_CORNER, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // handle the game states: open screen, game play and end screen
        switch (gameState) {
			case START_SCREEN:
	        	// Display instructions
				
				// set a grey background for the instructions 
	            Rectangle2D backRect = new Rectangle2D (Shape2D.MAGENTA, 290, 40, 550, 260);   // Green rectangle, shape 1
	            backRect.Draw(g);
	            // set the color/font and print the instructions
	            g.setColor(Color.white);
	            g.setFont(new Font("Consolas", Font.PLAIN, 20));
	            g.drawString("Welcome to Franklin's Dessert Dash!", 300, 70);
	            g.drawString("Avoid hitting the carrots or the game will end.", 300, 100);
	            g.drawString("To score, you need to collect strawberries.", 300, 130);
	            g.drawString("If you hit an onion, you will lose a point!", 300, 160);
	            g.drawString("To start playing press 'A'", 300, 190);
	            g.drawString("To pause the game press 'S'", 300, 220);
	            g.drawString("To jump press the UP arrow", 300, 250);
	            g.drawString("To go down press the DOWN arrow", 300, 280);
	
				break;
			case PLAYING:
				// set the color/font and display the score
	            g.setColor(Color.white);   
	            g.setFont(new Font("Consolas", Font.PLAIN, 30));
	            g.drawString("Score: " + Integer.toString(score), 500, 70);
	
				break;
			case END_SCREEN:
	            Rectangle2D endBackRect = new Rectangle2D (Shape2D.MAGENTA, 490, 50, 420, 130);   // Green rectangle, shape 1
	            endBackRect.Draw(g);
				// set the color/font and display end game info (score, duration, etc.)
	            g.setColor(Color.white);   
	            g.setFont(new Font("Consolas", Font.PLAIN, 20));
	            g.drawString("Game Over!", 500, 70);
	            g.drawString("Your Score is: " + Integer.toString(score), 500, 100);
	            g.drawString("You survived for " + Long.toString(gameTime/1000) + " seconds", 500, 130);
	            g.drawString("To start the game again, press 'R'.", 500, 160);
				
				break;
	
			default:
				break;
		}

        // Draw all the shapes in the shapes list (will be displayed in all screens)
        for (Shape2D shape : shapesList)
        {
        	if(shape != null) {
        		shape.Draw(g);
        	}
        }
        
        // Draw the frame (using 4 rectangles)
        new Rectangle2D(Shape2D.MAGENTA, 0, 0, 10, 900).Draw(g);
        new Rectangle2D(Shape2D.MAGENTA, 920, 0, 10, 900).Draw(g);
        new Rectangle2D(Shape2D.MAGENTA, 0, 0, 1200, 10).Draw(g);
      	new Rectangle2D(Shape2D.MAGENTA, 0, 620, 1200, 10).Draw(g);
    }
    
    /**
     * Inner class to handle the key events
     */
    public class myActionListener extends KeyAdapter 
    {
    	/**
    	 * handles the key pressed events
    	 */
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                	// handle Up Arrow pressed
                    jumpUp = true;
                    fallDown = false;
                    break;
                case KeyEvent.VK_DOWN:
                	// handle Down Arrow pressed
                    jumpUp = false;
                    fallDown = true;
                    break;
                case KeyEvent.VK_A:
                	// handle 'A' pressed, let the game start
                	gameState = GameState.PLAYING;
                    action = true;
                    currentTime = System.currentTimeMillis();	// keep the current time
                    break;
                case KeyEvent.VK_S:
                	// handle 'S' pressed, pause the game
                    action = false;
                    long currentTimeMillis = System.currentTimeMillis();	// get the current time
                    gameTime += currentTimeMillis - currentTime;	// calculate the game time (we might have multiple start/pause events)
                    break;
                case KeyEvent.VK_R:
                	// handle 'R' pressed, restart the game
                	init();	// init() will reset everything and will take us back to the the Start Screen
                    break;
                default:
                    System.out.println("invalid key");
            }
        }
    }
    
    public static int getCanvasWidth()
    {
        return CANVAS_WIDTH;
    }

    public static int getCanvasHeight()
    {
        return CANVAS_HEIGHT;
    }

    public static int getCanvasXBorder()
    {
        return X_CORNER;
    }

    public static int getCanvasYBorder()
    {
        return Y_CORNER;
    }

}
