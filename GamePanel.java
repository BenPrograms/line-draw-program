package ben.mellogame.source;
import javax.imageio.ImageIO;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt. *;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage. *;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener{
	//Fields
	public static int WIDTH;
	public static int HEIGHT;
	
	private long redTime;
	private long redDelay;
	private boolean redTrue = false;
	
	private Dimension screenSize;
	
	private Thread thread;
	private boolean running;
	private int FPS = 30;
	private double averageFPS;
	
	private BufferedImage image;
	private Graphics2D g;
	
	private boolean line;
	
	private static ArrayList<Dot> dots;
	
	private Random r;
	private Random r2;
	private Random r3;
	
	private Random ran;
	private Random ran2;
	private Random ran3;
	
	private int ranColor;
	private int ranColor2;
	private int ranColor3;
	
	private int randColor;
	private int randColor2;
	private int randColor3;

	private Color lines;
	private Color lines2;
	private Color background = new Color(0, 0, 0);

	private long time;
	private long timeDelay;
	
	private boolean backDraw = false;
	
	private int fade;
	
	private boolean fader;
	
	//Constructor
	public GamePanel(){
		super();
		r = new Random();
		r2 = new Random();
		r3 = new Random();
		
		ran = new Random();
		ran2 = new Random();
		ran3 = new Random();
		
		randColor = ran.nextInt((200 - 75) + 1) + 75;
		randColor2 = ran2.nextInt((200 - 75) + 1) + 75;
		randColor3 = ran3.nextInt((200 - 75) + 1) + 75;
		
		ranColor2 = r2.nextInt((200 - 75) + 1) + 75;
		ranColor3 = r3.nextInt((200 - 75) + 1) + 75;
		ranColor = r.nextInt((200 - 75) + 1) + 75;
		
		fade = 0;
		fader = true;
		
		backDraw = false;
		
		lines = new Color(randColor, randColor2, randColor3, 1);
		lines2 = new Color(ranColor, ranColor2, ranColor3, 1);
		
		dots = new ArrayList<Dot>();
		line = true;
		redTime = 0;
		redDelay = 20;
		redTrue = false;
		
		time = 0;
		timeDelay = 450;
	
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		HEIGHT = screenSize.height;
		WIDTH = screenSize.width;
		
		initDots();
	}
	public void initDots(){
		//initializes dots
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
		
		GamePanel.dots.add(new Dot());
		GamePanel.dots.add(new Dot());
	}
	public void addNotify(){
		super.addNotify();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	public static final void makeScreenshot() {
	    BufferedImage bufferedImage = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT,
	            BufferedImage.TYPE_INT_ARGB);

	    try {
	        // Create temp file.
	        File temp = File.createTempFile("screenshot", ".png");

	        // Use the ImageIO API to write the bufferedImage to a temporary file
	        ImageIO.write(bufferedImage, "png", temp);

	        // Delete temp file when program exits.
	        temp.deleteOnExit();
	    } catch (IOException ioe) {
	        System.out.println("did not send to computer");
	    } // catch
	} // makeScreenshot method
	public void run(){
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		long startTime;
		long URDTimeMillis;
		long waitTime;
		int frameCount = 0;
		int maxFrameCount = 30;
		
		long targetTime = 1000 / FPS;
		long totalTime = 0;
		
		//GameLoop
		while(running){
			startTime = System.nanoTime();
			
			gameUpdate();
			gameRender();
			gameDraw();
			
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;
			try{
				if(waitTime >= 0){
					Thread.sleep(waitTime);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if(frameCount == maxFrameCount){
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}
	}
	
	public void gameUpdate(){
		for(int i = 0; i < dots.size(); i++){
			dots.get(i).update();
		}
	}
	
	public void gameRender(){
		time++;
		fader = true;
		if(backDraw == false){
			g.setColor(background);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			backDraw = true;
		}
		if(time >= timeDelay && time <= timeDelay + timeDelay / 4){
			if(fade <= 255 && fader == true){
				fade++;
				g.setColor(new Color(0, 0, 0, fade));
				g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
				fader = false;
			}
		}
		if(time >= timeDelay + timeDelay / 4){
			fade = 0;
			g.setColor(background);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			
			randColor = ran.nextInt((200 - 75) + 1) + 75;
			randColor2 = ran2.nextInt((200 - 75) + 1) + 75;
			randColor3 = ran3.nextInt((200 - 75) + 1) + 75;
			
			ranColor2 = r2.nextInt((200 - 75) + 1) + 75;;
			ranColor3 = r3.nextInt((200 - 75) + 1) + 75;;
			ranColor = r.nextInt((200 - 75) + 1) + 75;;
			
			lines2 = new Color(ranColor, ranColor2, ranColor3, 1);
			
			time = 0;
		}
		for(int i = 0; i < dots.size(); i++){
			dots.get(i).draw(g);
		}	
			g.setStroke(new BasicStroke(1));
		for(int i = 0; i < dots.size(); i++){
			if(line == true){
				g.setColor(lines2);
				g.drawLine(dots.get(0).getX() + 5, dots.get(0).getY() + 5, dots.get(1).getX() + 5, dots.get(1).getY() + 5);
				g.drawLine(dots.get(1).getX() + 5, dots.get(1).getY() + 5, dots.get(2).getX() + 5, dots.get(2).getY() + 5);	
				g.drawLine(dots.get(2).getX() + 5, dots.get(2).getY() + 5, dots.get(3).getX() + 5, dots.get(3).getY() + 5);
				g.drawLine(dots.get(3).getX() + 5, dots.get(3).getY() + 5, dots.get(4).getX() + 5, dots.get(4).getY() + 5);
				g.drawLine(dots.get(4).getX() + 5, dots.get(4).getY() + 5, dots.get(5).getX() + 5, dots.get(5).getY() + 5);
				
				g.setColor(lines);
				g.drawLine(dots.get(3).getX() + 5, dots.get(3).getY() + 5, dots.get(6).getX() + 5, dots.get(6).getY() + 5);
				g.drawLine(dots.get(7).getX() + 5, dots.get(7).getY() + 5, dots.get(4).getX() + 5, dots.get(4).getY() + 5);
				g.drawLine(dots.get(5).getX() + 5, dots.get(5).getY() + 5, dots.get(6).getX() + 5, dots.get(6).getY() + 5);
				g.drawLine(dots.get(6).getX() + 5, dots.get(6).getY() + 5, dots.get(0).getX() + 5, dots.get(0).getY() + 5);
				
				g.setColor(new Color(255, 255, 0, 1));
				g.drawLine(dots.get(8).getX() + 5, dots.get(5).getY() + 5, dots.get(6).getX() + 5, dots.get(6).getY() + 5);
				g.drawLine(dots.get(9).getX() + 5, dots.get(6).getY() + 5, dots.get(1).getX() + 5, dots.get(1).getY() + 5);
			}
		}
	}
	
	public void gameDraw(){
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	public void keyPressed(KeyEvent key){
		int keyCode = key.getKeyCode();
		if(keyCode != KeyEvent.VK_ENTER){
			System.exit(0);
		}
	}
	
	public void keyReleased(KeyEvent key){
		int keyCode = key.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER){
			time = timeDelay;
		}
	}

	public void keyTyped(KeyEvent key) {
		
	}
}
