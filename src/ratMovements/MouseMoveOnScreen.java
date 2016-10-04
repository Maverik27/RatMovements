package ratMovements;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MouseMoveOnScreen {

	public boolean start = true;
	long stopped; // time of service has been stopped
	long elapsedTime; // elapsed time
	long waitTimeToStart = 60000; // wait time we need to attend for restart
									// service

	// second passed between each mouse movement
	long mSec = 1000;
	long sec = TimeUnit.MILLISECONDS.toSeconds(mSec);

	Robot robot;
	JLabel label;

	MouseMoveOnScreen() throws AWTException {

		robot = new Robot();
		label = new JLabel();

		ActionListener al = new ActionListener() {

			Point lastPoint = MouseInfo.getPointerInfo().getLocation();

			@Override
			public void actionPerformed(ActionEvent e) {

				Point p = MouseInfo.getPointerInfo().getLocation();
				elapsedTime = 0;

				if (!p.equals(lastPoint)) {

					stopAutoMouse();
					stopped = System.currentTimeMillis();

				} else {

					try {

						elapsedTime = System.currentTimeMillis() - stopped;

						if (elapsedTime > waitTimeToStart) {

							setStartTrue();
							System.out.println(
									TimeUnit.MILLISECONDS.toSeconds(elapsedTime) + " second passed! Service restored!");

							while (isStart()) {

								robot.mouseMove(1, 1);
								Thread.sleep(mSec);
								
								// verifica se il mouse si sta muovendo fuori da
								// un preciso rettangolo di pixel
								p = MouseInfo.getPointerInfo().getLocation();

								if (!(p.x == 1 && (p.y == 1 || p.y == 2))) {
									break;
								}
								robot.mouseMove(1, 2);
								Thread.sleep(mSec);

							}
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				lastPoint = p;
			}
		};
		Timer timer = new Timer(30, al);
		timer.start();
	}

	public boolean isStart() {
		return start;
	}

	public void setStartTrue() {
		start = true;
	}

	public void stopAutoMouse() {
		start = false;
		System.out.println("Detected mouse movement! Service Stopped!");
	}

	public static void main(String[] args) throws Exception {
		Runnable r = new Runnable() {
			@Override
			public void run() {

				// get the screen size as a java dimension
				JFrame f = new JFrame("RatMovements");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				ImageIcon ratman = new ImageIcon(this.getClass().getResource("/ratMovements/ratman.jpg"));
				
				int height = ratman.getIconHeight() + 50;
				int width = ratman.getIconWidth() + 50;

				// set the jframe height and width
				f.setPreferredSize(new Dimension(width, height));
				f.setLayout(new FlowLayout());
				f.setContentPane(new JLabel(ratman));
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				f.pack();
				f.setLocationByPlatform(true);
				f.setVisible(true);
				
				try {
					MouseMoveOnScreen mmos = new MouseMoveOnScreen();
				} catch (AWTException ex) {
					ex.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}
}