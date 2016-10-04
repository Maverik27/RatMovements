package ratMovements;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class RatMain {
	public static void main(String[] args) throws Exception {
		Runnable r = new Runnable() {
			@Override
			public void run() {

				// get the screen size as a java dimension
				JFrame f = new JFrame("RatMovements");
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
