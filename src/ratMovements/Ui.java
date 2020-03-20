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
    JFrame j;

    // static variable single_instance of type MouseMoveOnScreen
    private static Ui single_instance = null;

    // static method to create instance of Singleton class
    public static Ui getInstance() throws AWTException {
        if (single_instance == null)
            single_instance = new Ui();

        return single_instance;
    }

    public Ui() {
        generateUI();
    }

    public void generateUI() {
        log.info("Setting up JFrame informations...");
        this.j = new JFrame("Rat Movements 2.0");
        //Create JFrame and get icons and background
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/images/RatIcon.png"));
        ImageIcon background = new ImageIcon(this.getClass().getResource("/resources/images/RatBackground1.png"));

        // Config JFrame settings
        Image scaledBackground = getScaledImage(background.getImage(), 200, 200);
        background = new ImageIcon(scaledBackground);
        int height = (int) (background.getIconHeight() + (background.getIconHeight() * 0.3));
        int width = (int) (background.getIconWidth() + (background.getIconWidth() * 0.9));
        this.j.setPreferredSize(new Dimension(width, height));

        this.j.setLayout(new FlowLayout());
        this.j.setBackground(Color.BLACK);
        this.j.setContentPane(new JLabel(background));
        this.j.setIconImage(icon.getImage());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Utilities");
        JMenu menuWaitTime = new JMenu("Set Wait Time");
        JCheckBoxMenuItem _1Minute = new JCheckBoxMenuItem("1 Minute");
        _1Minute.setState(true);
        JCheckBoxMenuItem _5Minutes = new JCheckBoxMenuItem("5 Minutes");
        JCheckBoxMenuItem _10Minutes = new JCheckBoxMenuItem("10 Minutes");
        menuWaitTime.add(_1Minute);
        menuWaitTime.add(_5Minutes);
        menuWaitTime.add(_10Minutes);
        menu.add(menuWaitTime);
        JMenuItem openLogFileMenuItem = new JMenuItem("Open Log File");
        JMenuItem minimizeOnTry = new JMenuItem("Minimize on Tray");
        menu.add(openLogFileMenuItem);
        menuBar.add(menu);
        this.j.setJMenuBar(menuBar);

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

        this.j.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                j.setExtendedState(JFrame.ICONIFIED);
            }
        });

        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.j.pack();
        this.j.setLocationByPlatform(true);
        this.j.setVisible(true);

        this.j.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("Rat Movements is closing window...");
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

    public JFrame getJ() {
        return j;
    }

    public void setJ(JFrame j) {
        this.j = j;
    }
}
