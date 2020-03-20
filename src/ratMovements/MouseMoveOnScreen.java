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
    private long mSec = 10;// second passed between each mouse movement
    private int miniWaits = 10; // number of smaller waits within a bigger wait
    private Robot robot;

    // static variable single_instance of type MouseMoveOnScreen
    private static MouseMoveOnScreen single_instance = null;

    // static method to create instance of Singleton class
    public static MouseMoveOnScreen getInstance() throws AWTException {
        if (single_instance == null)
            single_instance = new MouseMoveOnScreen();

        return single_instance;
    }


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
                    //Se il mouse è fermo nello stesso punto verifico da quanto tempo è fermo
                    elapsedTime = System.currentTimeMillis() - stopped;

                    //Se è fermo da più tempo di quello impostato per far partire l'automazione, faccio ripartire
                    if (elapsedTime > waitTimeToStart) {

                        setStartTrue();
                        int inc = (MouseInfo.getPointerInfo().getLocation().y > 0 ? -1 : 1);

                        while (isStart()) {
                            try {
                                p.y += inc;
                                robot.mouseMove(p.x, p.y);

                                if (splitWait(p)) break;

                                p.y -= inc;
                                robot.mouseMove(p.x, p.y);

                                if (splitWait(p)) break;

                            } catch (InterruptedException ex) {
                               log.error(ex.getStackTrace());
                            }
                        }
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
            log.info("Mouse Moved! Old Position --> [" + p.x + "-" + p.y + "] New Position --> [" + pp.x + "-" + pp.y + "]");
            return true;
        }
    }

    private boolean isStart() {
        return start;
    }

    private void setStartTrue() {
        start = true;
        log.info(TimeUnit.MILLISECONDS.toSeconds(elapsedTime) + " seconds passed! Service Restarted!");
        log.info("Start variable setted to True -> " + start);
    }

    private void stopAutoMouse() {
        start = false;
        log.info("Detected mouse movement! Service Stopped!");
        log.info("Start variable setted to False -> " + start);
    }

    public long getWaitTimeToStart() {
        return waitTimeToStart;
    }

    public void setWaitTimeToStart(long waitTimeToStart) {
        this.waitTimeToStart = waitTimeToStart;
        log.info("New wait time to start setted: " + TimeUnit.MILLISECONDS.toMinutes(waitTimeToStart) + " Minute(s)");
    }
}