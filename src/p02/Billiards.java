package p02;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
public class Billiards extends JFrame {

	public static int Width = 800;
	public static int Height = 600;

	private JButton b_start, b_stop;

	private Board board;
 
	private final int N_BALL = 7;
	private Ball[] balls;
	private Thread[] threads;
	
	public Billiards() {

		board = new Board();
		board.setForeground(new Color(0, 128, 0));
		board.setBackground(new Color(0, 128, 0));

		initBalls();

		b_start = new JButton("Empezar");
		b_start.addActionListener(new StartListener());
		b_stop = new JButton("Parar");
		b_stop.addActionListener(new StopListener());

		JPanel p_Botton = new JPanel();
		p_Botton.setLayout(new FlowLayout());
		p_Botton.add(b_start);
		p_Botton.add(b_stop);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(board, BorderLayout.CENTER);
		getContentPane().add(p_Botton, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Width, Height);
		setLocationRelativeTo(null);
		setTitle("Práctica programación concurrente objetos móviles independientes");
		setResizable(false);
		setVisible(true);
	}

	private void initBalls() {
		  balls = new Ball[N_BALL];
		  
		  for(int i = 0; i < balls.length; i++) {
		   balls[i] = new Ball();
		  }
		  
		  board.setBalls(balls); 
		 }

	/**
	 * Creates a Thread to simulate the movement of a ball
	 * @param b Ball whose movement needs to be simulated
	 * @return Created Thread
	 */
	private Thread createThread(Ball b) {
		Runnable runloop = new Runnable() {
			public void run() {
				try {
					// infinite loop: always running until stopped by user action
					for(;;) {
						// first, we move the ball
						b.move();
						// then, we redraw the board to show the ball's new position
						board.repaint();
						// finally, we sleep the thread for some time
						TimeUnit.MILLISECONDS.sleep(15);
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		};
		return new Thread(runloop);
	}
	
	private class StartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (threads == null) {
				// initialize the array and iterate over it
				threads = new Thread[N_BALL];
				for (int i = 0; i < threads.length; ++i) {
					// creating the threads and starting them
					threads[i] = createThread(balls[i]);
					threads[i].start();
				}
			}
		}
	}

	private class StopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (threads != null) {
				for (int i = 0; i < threads.length; i++) {
					threads[i].interrupt();
				}
				threads = null;
			}	
		}
	} 

	public static void main(String[] args) {
		new Billiards();
	}
}
