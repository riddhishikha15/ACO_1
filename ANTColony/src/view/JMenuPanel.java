package view;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import object.Playground;

public class JMenuPanel extends JPanel{

    private Playground p;

    public JMenuPanel(Playground p) {
        this.p = p;
        this.initMenu(p);
    }

    private void initMenu(Playground p) {

                setPreferredSize(new Dimension(400, 36));

                setLayout(new FlowLayout());

                JMenuBar jBar = new JMenuBar();

                JMenu jFile = new JMenu("Playground");

                JMenuItem jNew = new JMenuItem("New");
                jNew.addActionListener(new buttonListener(p));

                JMenuItem jLoad = new JMenuItem("Load");
                jLoad.addActionListener(new buttonListener(p));

                JToggleButton jStart = new JToggleButton("Start");
                jStart.addItemListener(new buttonStart(p));

                JMenuItem jResetTrace = new JMenuItem("ResetTrace");
                jResetTrace.addActionListener(new buttonListener(p));

                JMenuItem jResetPlayground = new JMenuItem("ResetPlayground");
                jResetPlayground.addActionListener(new buttonListener(p));

                JMenuItem jResetAnt = new JMenuItem("ResetAnt");
                jResetAnt.addActionListener(new buttonListener(p));

                JMenuItem jSave = new JMenuItem("Save");
                jSave.addActionListener(new buttonListener(p));

                JMenuItem jQuit = new JMenuItem("Exit");
                jQuit.addActionListener(new buttonListener(p));


                jFile.add(jNew);
                jFile.add(jLoad);
                jFile.add(jResetAnt);
                jFile.add(jResetTrace);
                jFile.add(jResetPlayground);
                jFile.add(jSave);
                jFile.add(jQuit);

                jBar.add(jFile);

                this.add(jBar);
                this.add(jStart);
    }
}