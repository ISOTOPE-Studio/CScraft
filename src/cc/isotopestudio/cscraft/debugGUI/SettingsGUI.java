package cc.isotopestudio.cscraft.debugGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mars on 5/30/2016.
 * Copyright ISOTOPE Studio
 */
public class SettingsGUI extends JFrame {
    public static final JTextArea text = new JTextArea();
    private static JFrame fm;

    public void run() {
        fm = new JFrame("CScraft Debug-setting");
        JScrollPane fp = new JScrollPane();
        Container con = fm.getContentPane();
        fp.setViewportView(text);
        con.add(fp);
        text.setEditable(false);
        fm.setSize(500, 400);
    }

    public static void on(boolean b) {
        fm.setVisible(b);
    }

}
