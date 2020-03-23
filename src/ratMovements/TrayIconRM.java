package ratMovements;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TrayIconRM {

    static final Logger log = Logger.getLogger(TrayIcon.class.getName());
    private TrayIcon trayIcon;
    private TrayIcon trayIconPaused;
    private SystemTray tray;

    public void createAndShowGUI() throws AWTException {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            log.warn("SystemTray is not supported");
            return;
        }

        trayIcon = new TrayIcon(TrayIconRM.createImage("/resources/images/RatIcon.png", "tray icon"));
        trayIcon.setToolTip("Rat Movements 2.0");
        trayIcon.setImageAutoSize(true);

        trayIconPaused = new TrayIcon(TrayIconRM.createImage("/resources/images/RatIconPaused.png", "tray icon paused"));
        trayIconPaused.setToolTip("Service Paused");
        trayIconPaused.setImageAutoSize(true);

        tray = SystemTray.getSystemTray();

        // Create a popup menu components
        JPopupMenu popup = generateMenu();

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.warn("TrayIcon could not be added.");
            return;
        }

        MouseListener mouseListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.setLocation(e.getX() - 120, e.getY() - 130);
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        trayIcon.addMouseListener(mouseListener);
        trayIconPaused.addMouseListener(mouseListener);
    }

    private JPopupMenu generateMenu() {

        JPopupMenu menu = new JPopupMenu();
        JMenu menuWaitTime = new JMenu("Set Wait Time");
        JCheckBoxMenuItem _1Minute = new JCheckBoxMenuItem("1 Minute");
        JCheckBoxMenuItem _5Minutes = new JCheckBoxMenuItem("5 Minutes");
        JCheckBoxMenuItem _10Minutes = new JCheckBoxMenuItem("10 Minutes");
        JCheckBoxMenuItem pauseItem = new JCheckBoxMenuItem("Pause Service");
        JMenuItem openLogFileMenuItem = new JMenuItem("Open Log File");
        JMenuItem exitItem = new JMenuItem("Exit");

        menuWaitTime.add(_1Minute);
        menuWaitTime.add(_5Minutes);
        menuWaitTime.add(_10Minutes);
        _1Minute.setState(true);

        menu.add(menuWaitTime);
        menu.add(pauseItem);
        menu.add(openLogFileMenuItem);
        menu.addSeparator();
        menu.add(exitItem);

        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    if (pauseItem.isSelected()) {
                        pauseItem.setState(true);
                        MouseMoveOnScreen.getInstance().setServiceStarted(false);
                        tray.remove(trayIcon);
                        tray.add(trayIconPaused);
                        log.info("Service stopped by User -> " + MouseMoveOnScreen.getInstance().isServiceStarted());
                    } else {
                        pauseItem.setState(false);
                        MouseMoveOnScreen.getInstance().setServiceStarted(true);
                        tray.add(trayIcon);
                        tray.remove(trayIconPaused);
                        log.info("Service resumed by User -> " + MouseMoveOnScreen.getInstance().isServiceStarted());
                    }
                } catch (AWTException ex) {
                    log.error("Error stopping/resuming service by user input! -> " + ex.getStackTrace());
                }
            }
        });

        _1Minute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MouseMoveOnScreen.getInstance().setWaitTimeToStart(new Long(60000));
                    _1Minute.setState(true);
                    _5Minutes.setState(false);
                    _10Minutes.setState(false);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });

        _5Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MouseMoveOnScreen.getInstance().setWaitTimeToStart(new Long(350000));
                    _1Minute.setState(false);
                    _5Minutes.setState(true);
                    _10Minutes.setState(false);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });

        _10Minutes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MouseMoveOnScreen.getInstance().setWaitTimeToStart(new Long(650000));
                    _1Minute.setState(false);
                    _5Minutes.setState(false);
                    _10Minutes.setState(true);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });

        openLogFileMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String filePath = System.getProperty("user.home") + "/RatMovements/Logs/RatMovements.log";
                File f = new File(filePath);
                try {
                    log.info("Opening log file...");
                    Desktop.getDesktop().open(f);
                } catch (IOException ex) {
                    log.error("ERROR opening log file -> " + ex.getStackTrace());
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Rat Movements has been closed!");
                System.exit(0);
            }
        });

        return menu;
    }

    //Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = TrayIcon.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
