package ratMovements;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Ui {

    static final Logger log = Logger.getLogger(Ui.class.getName());

    public void generateUI() {
        log.info("Setting up JFrame informations...");

        // get the screen size as a java dimension
        JFrame f = new JFrame("Rat Movements 2.0");
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/images/RatIcon.png"));
        ImageIcon background = new ImageIcon(this.getClass().getResource("/resources/images/RatBackground1.png"));
        Image scaledBackground = getScaledImage(background.getImage(), 200, 200);
        background = new ImageIcon(scaledBackground);
        int height = (int) (background.getIconHeight() + (background.getIconHeight() * 0.2));
        int width = (int) (background.getIconWidth() + (background.getIconHeight() * 0.8));

        f.setPreferredSize(new Dimension(width, height));

        // set the jframe height and width
        f.setLayout(new FlowLayout());
        f.setBackground(Color.BLACK);
        f.setContentPane(new JLabel(background));
        f.setIconImage(icon.getImage());
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
