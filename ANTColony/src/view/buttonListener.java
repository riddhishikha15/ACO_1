
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import object.MyFilter;
import object.Playground;
import object.SerializerNodes;

public class buttonListener implements ActionListener {

    private Playground p;

    public buttonListener(Playground p) {
        super();
        this.p = p;
    }


    public void actionPerformed(ActionEvent e) {
        if("New".equals(e.getActionCommand())){
            p.initPlayground();
                p.initTrace();
            p.initAnt();
            p.repaint();
        }
        else if("Load".equals(e.getActionCommand())){
            this.loadAFile(p);
        }
        else if("ResetTrace".equals(e.getActionCommand())){
            p.resetTrace();
            p.repaint();
        }
        else if ("ResetAnt".equals(e.getActionCommand())){
            p.initAnt();
            p.repaint();
        }
        else if ("ResetPlayground".equals(e.getActionCommand())){
            p.resetPlayground();
            p.repaint();
        }
        else if("Save".equals(e.getActionCommand())){
            SerializerNodes sNodes = new SerializerNodes(p);
            sNodes.saveInFile();
        }
        else if("Quit".equals(e.getActionCommand())){
            //p.exit();

        }

    }

    private void loadAFile(Playground p){
        SerializerNodes serializerNodes = new SerializerNodes(p);
                JFileChooser jFile = new JFileChooser();

                // Only .map files
                MyFilter monFiltre = new MyFilter( new String[]{"map"}, "Map files  *.map");
                jFile.addChoosableFileFilter(monFiltre);
                jFile.setCurrentDirectory(new File("/"));
                int retour = jFile.showOpenDialog(jFile);
                if (retour == JFileChooser.APPROVE_OPTION){
                        jFile.getSelectedFile().getName();
                        jFile.getSelectedFile().getAbsolutePath();

                        try {
                                serializerNodes.loadFromFile(jFile.getSelectedFile().getPath());
                        } catch (IOException e) { e.printStackTrace();
                        } catch (ClassNotFoundException e) {}
                }
    }


}