package ratMovements;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MouseMoveOnScreen {

    final Logger log = Logger.getLogger(MouseMoveOnScreen.class.getName());

    private boolean start = false;
    private long stopped = System.currentTimeMillis(); // time of service has been stopped
    private long elapsedTime; // elapsed time
    private long waitTimeToStart = 60000; // wait time to attend to restart service
    //private long waitTimeToStart = 5000; // wait time to attend to restart service TEST MODE ONLY!!
    private long mSec = 1000;// second passed between each mouse movement
    private int miniWaits = 10; // number of smaller waits within a bigger wait
    private Robot robot;

    public MouseMoveOnScreen() throws AWTException {

        log.info("Creating new Robot to move mouse automatically...");
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
                            int inc = (MouseInfo.getPointerInfo().getLocation().y > 0 ? -1 : 1);

                            while (isStart()) {
                                p.y += inc;
                                robot.mouseMove(p.x, p.y);

                                if (splitWait(p)) break;

                                p.y -= inc;
                                robot.mouseMove(p.x, p.y);

                                if (splitWait(p))
                                    break;
                            }
                        }
                    } catch (InterruptedException e1) {
                        log.error("InterruptedException! -->" + Arrays.toString(e1.getStackTrace()));
                    }
                }
                lastPoint = p;
            }
        };
        Timer timer = new Timer(30, al);
        timer.start();
    }

    private boolean splitWait(Point p) throws InterruptedException {
        for (int i = 0; i < miniWaits; i++) {
            Thread.sleep(mSec / miniWaits);

            // is pointer inside a specific pixel rectangle?
            if (isMouseMoved(MouseInfo.getPointerInfo().getLocation(), p)) {
                return true;
            }
        }

        return false;
    }

    private boolean isMouseMoved(Point p, Point pp) {
        if (p.x == pp.x && p.y == pp.y) {
            return false;
        } else {
            log.info("Mouse Moved! Old Position --> " + p.getLocation() + " New Position --> " + MouseInfo.getPointerInfo().getLocation());
            return true;
        }
    }

    private boolean isStart() {
        return start;
    }

    private void setStartTrue() {
        start = true;
        log.info(TimeUnit.MILLISECONDS.toSeconds(elapsedTime) + " second passed! Service Restarted!");
        log.info("Start variable setted to True -> " + start);
    }

    private void stopAutoMouse() {
        start = false;
        log.info("Detected mouse movement! Service Stopped!");
        log.info("Start variable setted to False -> " + start);
    }
}