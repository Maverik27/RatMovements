package ratMovements;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MouseMoveOnScreen {

	private boolean start;
	private long stopped; // time of service has been stopped
	private long elapsedTime; // elapsed time
	private long waitTimeToStart = 60000; // wait time to attend to restart
											// service
	private long mSec = 1000;// second passed between each mouse movement
	private Robot robot;

	public MouseMoveOnScreen() throws AWTException {

		robot = new Robot();
		ActionListener al = new ActionListener() {

			Point lastPoint = MouseInfo.getPointerInfo().getLocation();

			@Override
			public void actionPerformed(ActionEvent e) {

				Point p = MouseInfo.getPointerInfo().getLocation();
				elapsedTime = 0;

				if (!p.equals(lastPoint)) {

					if (isStart()) {
						stopAutoMouse();
					}
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

								// is pointer inside a specific pixel rectangle?
								if (isMouseMoved(MouseInfo.getPointerInfo().getLocation())) {
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

	private boolean isMouseMoved(Point p) {
		if (!(p.x == 1 && (p.y == 1 || p.y == 2))) {
			return true;
		}
		return false;
	}

	private void setStartFalse() {
		start = false;
	}

	private boolean isStart() {
		return start;
	}

	private void setStartTrue() {
		start = true;
	}

	private void stopAutoMouse() {
		start = false;
		System.out.println("Detected mouse movement! Service Stopped!");
	}
}