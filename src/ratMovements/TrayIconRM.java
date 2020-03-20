package ratMovements;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

public class TrayIconRM {

    static final Logger log = Logger.getLogger(TrayIcon.class.getName());

    public void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            log.warn("SystemTray is not supported");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(TrayIconRM.createImage("/resources/images/RatIcon.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();
        trayIcon.setToolTip("Rat Movements 2.0");
        trayIcon.setImageAutoSize(true);

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem openItem = new MenuItem("Open Rat Movements 2.0");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(aboutItem);
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.warn("TrayIcon could not be added.");
            return;
        }

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Rat Movements 2.0 - Copyright MK 2020");
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Rat Movements has been closed!");
                tray.remove(trayIcon);
                System.exit(0);
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Ui.getInstance().getJ().setVisible(true);
                    Ui.getInstance().getJ().setExtendedState(JFrame.NORMAL);
                } catch (AWTException ex) {
                    ex.printStackTrace();
                }
            }
        });

        trayIcon.addMouseListener(new MouseListener() {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getClickCount() >= 2){
                    try {
                        Ui.getInstance().getJ().setVisible(true);
                        Ui.getInstance().getJ().setExtendedState(JFrame.NORMAL);
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
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
