package ratMovements;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ui {

    static final Logger log = Logger.getLogger(Ui.class.getName());

    public void generateUI() {
        log.info("Setting up JFrame informations...");

        //Create JFrame and get icons and background
        JFrame f = new JFrame("Rat Movements 2.0");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/images/RatIcon.png"));
        ImageIcon background = new ImageIcon(this.getClass().getResource("/resources/images/RatBackground1.png"));

        // Config JFrame settings
        Image scaledBackground = getScaledImage(background.getImage(), 200, 200);
        background = new ImageIcon(scaledBackground);
        int height = (int) (background.getIconHeight() + (background.getIconHeight() * 0.3));
        int width = (int) (background.getIconWidth() + (background.getIconWidth() * 0.9));
        f.setPreferredSize(new Dimension(width, height));

        f.setLayout(new FlowLayout());
        f.setBackground(Color.BLACK);
        f.setContentPane(new JLabel(background));
        f.setIconImage(icon.getImage());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Utilities");
        JMenu menuWaitTime = new JMenu("Set Wait Time");
        JMenuItem _1Minute = new JMenuItem("1 Minute");
        JMenuItem _5Minutes = new JMenuItem("5 Minutes");
        JMenuItem _10Minutes = new JMenuItem("10 Minutes");
        menuWaitTime.add(_1Minute);
        menuWaitTime.add(_5Minutes);
        menuWaitTime.add(_10Minutes);
        menu.add(menuWaitTime);
        JMenuItem openLogFileMenuItem = new JMenuItem("Open Log File");

        _1Minute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MouseMoveOnScreen.getInstance().setWaitTimeToStart(new Long(60000));
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

        menu.add(openLogFileMenuItem);
        menuBar.add(menu);
        f.setJMenuBar(menuBar);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationByPlatform(true);
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("Rat Movements is closing...");
            }
        });

        log.info("JFrame configurations completed");
    }

    private Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
}
