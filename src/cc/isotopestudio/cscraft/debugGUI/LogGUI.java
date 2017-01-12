package cc.isotopestudio.cscraft.debugGUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Created by Mars on 5/30/2016.
 * Copyright ISOTOPE Studio
 */
public class LogGUI extends JFrame {
    private static final JTextArea text = new JTextArea();
    private static JFrame fm;

    public void run() {
        fm = new JFrame("CScraft Debug-Log");
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

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void addInfo(String info) {
        text.append("[" + format.format(new java.util.Date()) + "] "
                + info + "\n");
    }

}
