package za.co.blacklemon.logreader;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

public class Main implements ItemListener, LogReaderWindow, ActionListener {
    private LogFileReader reader;
    private final JFrame frame = new JFrame("Log Reader");
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private boolean autoScroll;
    private Process jbossProcess;
    private static final String JBOSS_ACTION = "jboss";

    public static void main(String ... args) {

        new Main().start();
    }

    private void start() {
        setupFrame();

        reader = new LogFileReader(this);
        new Thread(reader).start();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();

        JCheckBox checkBoxScroll = new JCheckBox("Auto-scroll", true);
        autoScroll = true;
        checkBoxScroll.addItemListener(this);
        topPanel.add(checkBoxScroll, BorderLayout.WEST);

        JButton jbossButton = new JButton("Start JBoss");
        jbossButton.addActionListener(this);
        jbossButton.setActionCommand(JBOSS_ACTION);
        topPanel.add(jbossButton, BorderLayout.WEST);

        frame.add(topPanel, BorderLayout.NORTH);

        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(1000, 600));
        textPane.setBackground(Color.BLACK);
        textPane.setForeground(Color.GREEN);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        textPane.setFont(font);

        scrollPane = new JScrollPane(textPane);
        scrollPane.setAutoscrolls(true);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();

        frame.setVisible(true);
    }

    /**
     * Handle
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {
        autoScroll = e.getStateChange() != ItemEvent.DESELECTED;
    }

    /**
     * Handle button click actions
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if (JBOSS_ACTION.equals(e.getActionCommand())) {
            try {
                jbossProcess = Runtime.getRuntime().exec("cmd /c \"D:\\Server\\wildfly-8.1.0.Final\\bin\\standalone.bat\"");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Write text to the output window in the specified style
     * @param s Style of text
     * @param str Text to write
     */
    private void text(SimpleAttributeSet s, String str) {
        try {
            textPane.getDocument().insertString(textPane.getDocument().getLength(), str, s);
            textPane.getDocument().insertString(textPane.getDocument().getLength(), "\n", s);
        }
        catch(Exception x) {}

        if(autoScroll)
            textPane.setCaretPosition(textPane.getDocument().getLength());
    }

    public void error(String error) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, Color.RED);
        StyleConstants.setBold(s, true);

        text(s, error);
    }

    public void warn(String warning) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, Color.YELLOW);

        text(s, warning);
    }

    public void severe(String severe) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, Color.RED);
        StyleConstants.setBold(s, true);

        text(s, severe);
    }

    public void info(String info) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, Color.GREEN);

        text(s, info);
    }

    public void fatal(String fatal) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setForeground(s, Color.RED);
        StyleConstants.setBold(s, true);

        text(s, fatal);
    }
}
