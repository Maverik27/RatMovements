package ratMovements;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class RatMain {

    static final Logger log = Logger.getLogger(RatMain.class.getName());

    public static void main(String[] args) {

        File log4jfile = new File("src/resources/log4j.properties");
        PropertyConfigurator.configure(log4jfile.getAbsolutePath());
        PropertyConfigurator.configure(log4jfile.getPath());

        Runnable r = RatMain::run;
        SwingUtilities.invokeLater(r);
    }

    private static void run() {
        log.info("Rat Movements started!");
        Ui ui = new Ui();
        ui.generateUI();

        try {
            //MouseMoveOnScreen mmos = new MouseMoveOnScreen();
            MouseMoveOnScreen.getInstance();
        } catch (AWTException ex) {
            log.error("AWTException! -->" + Arrays.toString(ex.getStackTrace()));
        }
    }
}
