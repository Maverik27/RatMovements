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
    private JPopupMenu popup;

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
        popup = generateMenu();

        try {
            tray.add(trayIcon);
            trayIcon.displayMessage("Rat Movements 2.0", "Application is Running on System Tray!", TrayIcon.MessageType.NONE);
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
                    popup.setLocation(e.getX() - 200, e.getY() - 130);
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
        JCheckBoxMenuItem runOnStartupItem = new JCheckBoxMenuItem("Start automatically on startup");
        runOnStartupItem.setState(isFileExistInStartUpFolder());
        JMenuItem openLogFileMenuItem = new JMenuItem("Open Log File");
        JMenuItem closeMenuItem = new JMenuItem("Close Menu");
        JMenuItem exitItem = new JMenuItem("Exit");

        menuWaitTime.add(_1Minute);
        menuWaitTime.add(_5Minutes);
        menuWaitTime.add(_10Minutes);
        _1Minute.setState(true);

        menu.add(menuWaitTime);
        menu.add(pauseItem);
        menu.add(runOnStartupItem);
        menu.add(openLogFileMenuItem);
        menu.add(closeMenuItem);
        menu.addSeparator();
        menu.add(exitItem);

        runOnStartupItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //String startupAllUserFolder = "\"C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp\\\"";
                String filePath = System.getProperty("user.home");
                String startupUserFolder = "\"" + filePath + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\\"";

                String shortCut = "\"C:\\Program Files (x86)\\Rat Movements 2.0\\ShortCut Rat Movements 2.0.vbs\"";
                String delFile = "\"" + filePath + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\ShortCut Rat Movements 2.0.vbs\"";
                Runtime r = Runtime.getRuntime();

                try {
                    if (runOnStartupItem.isSelected()) {
                        runOnStartupItem.setState(true);

                        String xcopy = "cmd /c xcopy /Y " + shortCut + " " + startupUserFolder;
                        log.debug("XCOPY: -> " + xcopy);
                        r.exec(xcopy);
                        log.info("Rat Movements 2.0 added to startup folder by User");
                    } else {
                        runOnStartupItem.setState(false);

                        String del = "cmd /c del " + delFile;
                        log.debug("DEL: -> " + del);
                        r.exec(del);
                        log.info("Rat Movements 2.0 removed from startup folder by User");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        closeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.setVisible(false);
            }
        });

        pauseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (pauseItem.isSelected()) {
                        MouseMoveOnScreen.getInstance().setServiceStarted(false);
                        tray.remove(trayIcon);
                        tray.add(trayIconPaused);
                        trayIconPaused.displayMessage("Rat Movements 2.0", "Service Paused!", TrayIcon.MessageType.NONE);
                        log.info("Service stopped by User");
                    } else {
                        MouseMoveOnScreen.getInstance().setServiceStarted(true);
                        tray.remove(trayIconPaused);
                        tray.add(trayIcon);
                        trayIcon.displayMessage("Rat Movements 2.0", "Service Resumed!", TrayIcon.MessageType.NONE);
                        log.info("Service resumed by User");
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

                //String filePath = System.getProperty("user.home") + "/RatMovements/Logs/RatMovements.log";
                String filePathInstaller = "C:/Program Files (x86)/Rat Movements 2.0/Logs/RatMovements.log";

                //File f = new File(filePath);
                File f = new File(filePathInstaller);

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

    private boolean isFileExistInStartUpFolder() {

        String filePath = System.getProperty("user.home");
        String startupFilePath = filePath + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\ShortCut Rat Movements 2.0.vbs";
        log.info("File Path on startup folder -> " + startupFilePath);

        File startupFile = new File(startupFilePath);

        if (startupFile.exists()) {
            log.info("File already Exist on startup folder");
            return true;
        } else {
            log.info("File does not Exist on startup folder");
            return false;
        }
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
