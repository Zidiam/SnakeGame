import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGamePanel extends JPanel{
	private final int WIDTH = 1020, HEIGHT = 516;
	private final int JUMP = 24;
	private int speed = 75;
	
	private final int IMAGE_SIZE = 24;
	
	private ImageIcon squareImg, appleImg, grassImg, mouthOpenDownImg, mouthOpenLeftImg, mouthOpenUpImg,
					   mouthOpenRightImg, snakeDownImg, snakeUpImg, snakeRightImg, snakeLeftImg, currentFaceImg,
					   foodDirectionImg, tailUpImg, tailDownImg, tailRightImg, tailLeftImg, currentTail, backgroundImg;
	
	private Timer timer;
	
	private int x, y, points, grassTime, grassCounter;
	
	private boolean left, right, up, down, gameOver, grassDown = false;
	
	private Random rand = new Random();
	
	private Point appleCord = new Point();
	private Point newCord = new Point();
	private Point grassCord = new Point();
	
	private ArrayList<Point> SnakeCords = new ArrayList<Point>();
	
	public SnakeGamePanel() {
		appleCord.setLocation(((rand.nextInt(WIDTH/24 - 2)+1)* 24), ((rand.nextInt(HEIGHT/24 - 2)+1)* 24));
		newCord.setLocation(240, 240);
		
		SnakeCords.add(newCord);
		grassCord.setLocation(((rand.nextInt(WIDTH/24 - 2)+1)* 24), ((rand.nextInt(HEIGHT/24 - 2)+1)* 24));
		
		addKeyListener(new DirectionListener());
		timer = new Timer(speed, new ReboundListener());
		
		ImageLoader();
		
		x = 240;
		y = 240;
		points = grassCounter = 0;
		grassTime = 30;
		
		currentFaceImg = snakeRightImg;
		foodDirectionImg = mouthOpenRightImg;
		
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		timer.start();
		
	}
	
	public void ImageLoader() {
		squareImg = new ImageIcon("src\\Images\\square.png");
		appleImg = new ImageIcon("src\\Images\\apple.png");
		grassImg = new ImageIcon("src\\Images\\grass.png");
		mouthOpenRightImg = new ImageIcon("src\\Images\\mouthOpenRight.png");
		mouthOpenLeftImg = new ImageIcon("src\\Images\\mouthOpenLeft.png");
		mouthOpenUpImg = new ImageIcon("src\\Images\\mouthOpenUp.png");
		mouthOpenDownImg = new ImageIcon("src\\Images\\mouthOpenDown.png");
		snakeDownImg = new ImageIcon("src\\Images\\snakeDown.png");
		snakeLeftImg = new ImageIcon("src\\Images\\snakeLeft.png");
		snakeRightImg = new ImageIcon("src\\Images\\snakeRight.png");
		snakeUpImg = new ImageIcon("src\\Images\\snakeUp.png");
		tailDownImg = new ImageIcon("src\\Images\\tailDown.png");
		tailUpImg = new ImageIcon("src\\Images\\tailUp.png");
		tailRightImg = new ImageIcon("src\\Images\\tailRight.png");
		tailLeftImg = new ImageIcon("src\\Images\\tailLeft.png");
		backgroundImg = new ImageIcon("src\\Images\\background.png");
	}
	
	public void paintComponent(Graphics page)
	{
		
		Graphics2D g2 = (Graphics2D)page;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
		super.paintComponent(page);
		backgroundImg.paintIcon(this, page, 0, 0);
		
		page.setColor(Color.BLACK);
		page.fillRect(0, 0, 24, HEIGHT + 24);
		page.fillRect(WIDTH-12, 0, 24, 24 + HEIGHT);
		page.fillRect(0, HEIGHT - 12, 24 + WIDTH, 24);
		page.fillRect(0, 0, WIDTH + 24, 24);
		
		page.setColor(Color.CYAN);
		page.drawString("Points: " + points, WIDTH/2, 15);
		page.setColor(Color.BLACK);
		
		int distance = (int) Math.sqrt(Math.pow(appleCord.x-SnakeCords.get(SnakeCords.size()-1).x, 2) + Math.pow(appleCord.y - SnakeCords.get(SnakeCords.size()-1).y, 2));
		int distance2 = (int) Math.sqrt(Math.pow(grassCord.x-SnakeCords.get(SnakeCords.size()-1).x, 2) + Math.pow(grassCord.y - SnakeCords.get(SnakeCords.size()-1).y, 2));
		
		if (SnakeCords.size() >= 2) {
			for(int i = 1; i < SnakeCords.size()-1; i++) {
				squareImg.paintIcon(this, page, SnakeCords.get(i).x, SnakeCords.get(i).y);
			}
		}
		if(distance < 100 || distance2 < 100)
			foodDirectionImg.paintIcon(this, page, SnakeCords.get(SnakeCords.size()-1).x, SnakeCords.get(SnakeCords.size()-1).y);
		else
			currentFaceImg.paintIcon(this, page, SnakeCords.get(SnakeCords.size()-1).x, SnakeCords.get(SnakeCords.size()-1).y);
		
		if (SnakeCords.size() >= 2 && (SnakeCords.get(0).x != SnakeCords.get(1).x || SnakeCords.get(0).y != SnakeCords.get(1).y)) {
			if(SnakeCords.get(0).x > SnakeCords.get(1).x)
				currentTail = tailRightImg;
			if(SnakeCords.get(0).x < SnakeCords.get(1).x)
				currentTail = tailLeftImg;
			if(SnakeCords.get(0).y > SnakeCords.get(1).y)
				currentTail= tailDownImg;
			if(SnakeCords.get(0).y < SnakeCords.get(1).y)
				currentTail = tailUpImg;
			currentTail.paintIcon(this, page, SnakeCords.get(0).x, SnakeCords.get(0).y);
		}
		
		appleImg.paintIcon(this, page, appleCord.x, appleCord.y);
		grassImg.paintIcon(this, page, grassCord.x, grassCord.y);
		
		if (gameOver == true) {
	        g2.drawString("GAME OVER ",WIDTH/2, HEIGHT/2); 
	        g2.drawString("Points: " + points,WIDTH/2, HEIGHT/2 + 24); 
		}
	}
	private class DirectionListener implements KeyListener{
		public void keyPressed(KeyEvent event) {
			switch(event.getKeyCode()) {
			case KeyEvent.VK_W:
				if (down == false) {
					currentFaceImg = snakeUpImg;
					foodDirectionImg = mouthOpenUpImg;
					up = true;
					left = false;
					right = false;
					down = false;
				}
				break;
			case KeyEvent.VK_A:
				if(right == false) {
					currentFaceImg = snakeLeftImg;
					foodDirectionImg = mouthOpenLeftImg;
					up = false;
					left = true;
					right = false;
					down = false;
				}
				break;
			case KeyEvent.VK_S:
				if(up == false) {
					currentFaceImg = snakeDownImg;
					foodDirectionImg = mouthOpenDownImg;
					up = false;
					left = false;
					right = false;
					down = true;
				}
				break;
			case KeyEvent.VK_D:
				if(left == false) {
					currentFaceImg = snakeRightImg;
					foodDirectionImg = mouthOpenRightImg;
					up = false;
					left = false;
					right = true;
					down = false;
				}
				break;
			}
			repaint();
		}
		public void keyTyped(KeyEvent event) {}
		public void keyReleased(KeyEvent event) {}
	}
	
	private class ReboundListener implements ActionListener
	{
		//--------------------------------------------------------------
		//  Updates the position of the image and possibly the direction
		//  of movement whenever the timer fires an action event.
		//--------------------------------------------------------------
		public void actionPerformed(ActionEvent event)
		{
			if (gameOver == false){
				counter();
				move();
				checkCollision();
				repaint();
			}
		}
	}
	
	private void checkCollision() {
		Point cord = SnakeCords.get(SnakeCords.size()-1);
		Point end = SnakeCords.get(0);
		if (cord.x >= appleCord.x - IMAGE_SIZE && cord.x <= appleCord.x+IMAGE_SIZE && cord.y <= appleCord.y + IMAGE_SIZE && cord.y >= appleCord.y - IMAGE_SIZE){
			for(int i = 0; i < 3; i++)
				SnakeCords.add(0, end);
			points += 3;
			if(points % 10 == 0) {
				speed += speed/10;
				timer = new Timer(speed, new ReboundListener());
			}
			appleCord.setLocation(((rand.nextInt(WIDTH/24 - 2)+1)* 24), ((rand.nextInt(HEIGHT/24 - 2)+1)* 24));
		}
		if (cord.x >= grassCord.x-IMAGE_SIZE && cord.x <= grassCord.x+IMAGE_SIZE && cord.y <= grassCord.y + IMAGE_SIZE && cord.y >= grassCord.y - IMAGE_SIZE){
			points += 10;
			if(SnakeCords.size() >= 3) {
				for(int i = 0; i < 3; i++)
					SnakeCords.remove(0);
			}
			grassCord.setLocation(((rand.nextInt(WIDTH/24 - 2)+1)* 24), ((rand.nextInt(HEIGHT/24 - 2)+1)* 24));	
		}
		for(int ind = 0; ind < SnakeCords.size()-1; ind ++) {
			if (cord.x == SnakeCords.get(ind).x && cord.y == SnakeCords.get(ind).y && SnakeCords.size() >= 7)
				gameOver = true;
		
		}
		
	}
	
	private void counter() {
		grassCounter ++;
		if(grassTime <= grassCounter) {
			grassCounter = 0;
			grassDown = !grassDown;
			if(grassDown == true)
				grassCord.setLocation(((rand.nextInt(WIDTH/24 - 2)+1)* 24), ((rand.nextInt(HEIGHT/24 - 2)+1)* 24));
			else
				grassCord.setLocation(-100, -100);
		}
		
		
	}
	
	private void move() {
		if (left == true){
			x -= JUMP;
			if (x < 24)
				gameOver = true;
			Point temp = new Point(x, y);
			SnakeCords.add(temp);
			SnakeCords.remove(0);
	}
		if (right == true){
			x += JUMP;
			if (x > WIDTH-24)
				gameOver = true;
			Point temp = new Point(x, y);
			SnakeCords.add(temp);
			SnakeCords.remove(0);
		}
		
		if (up == true){
			y -= JUMP;
			if (y < 24)
				gameOver = true;
			Point temp = new Point(x, y);
			SnakeCords.add(temp);
			SnakeCords.remove(0);
	}
		
		if (down == true){
			y += JUMP;
			if (y > HEIGHT-24)
				gameOver = true;
			Point temp = new Point(x, y);
			SnakeCords.add(temp);
			SnakeCords.remove(0);
		}
	}
}
	
	

