package p02;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Ball {
	private String Ball = "Ball.png"; 

	private double x,y,dx,dy;
	private double v,fi;
	private Image image;
	private final int IMG_TAM_X,IMG_TAM_Y;

	public Ball() {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(Ball));
		image = ii.getImage();
		
		IMG_TAM_X = 32;
		IMG_TAM_Y = 32;
		
		x = Billiards.Width/4-16;
		y = Billiards.Height/2-16;
		v = 5;
		fi =  Math.random() * Math.PI * 2;
		assertPositionIsValid();
	}

	public synchronized void move() {
		v = v*Math.exp(-v/1000);
		dx = v*Math.cos(fi);
		dy = v*Math.sin(fi);
		if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
			dx = 0;
			dy = 0;
		}
		x += dx;   
		y += dy;
		
		reflect();
		
		assertPositionIsValid();
	}

	private void reflect() {
		if (Math.abs(x + IMG_TAM_X - Board.RIGHTBOARD) <  Math.abs(dx)) {
			fi = Math.PI - fi;
		}
		if (Math.abs(y + IMG_TAM_Y - Board.BOTTOMBOARD) <  Math.abs(dy)) {
			fi = - fi;
		}
		if (Math.abs(x - Board.LEFTBOARD) <  Math.abs(dx)) {
			fi = Math.PI - fi;
		}
		if (Math.abs(y - Board.TOPBOARD) <  Math.abs(dy)) {
			fi = - fi;
		}
	}

	/**
	 * Checks if the ball position is within the board limits
	 */
	private void assertPositionIsValid() {
		assert x > Board.LEFTBOARD && x < Board.RIGHTBOARD &&
		y > Board.TOPBOARD && y < Board.BOTTOMBOARD :
		"Invalid position: ball is outside the board";	
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public double getFi() {
		return fi;
	}

	public double getdr() {
		return Math.sqrt(dx*dx+dy*dy);
	}

	public synchronized void setX(double x) {
		this.x = x;
		assertPositionIsValid();
	}

	public synchronized void setY(double y) {
		this.y = y;
		assertPositionIsValid();
	}

	public Image getImage() {
		return image;
	}	
}