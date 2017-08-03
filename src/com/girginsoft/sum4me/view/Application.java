/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girginsoft.sum4me.view;


import com.apple.laf.AquaOptionPaneUI;
import com.girginsoft.sum4me.processor.Summarizier;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author girginsoft
 */
public class Application extends JFrame implements ActionListener {
    private static int FRAME_WIDTH = 500;
    private static int FRAME_HEIGHT = 300;
    protected JButton selectedFile = null;
    protected JLabel selectedFileName = null;
    protected JFileChooser chooser = null;
    protected JButton convertButton = null;
    protected JProgressBar progressBar = null;
    protected Summarizier summarizier = null;
    protected JLabel outputFilePath = null;
    protected JFileChooser outputChooser = null;
    protected JComboBox threshouldLevel = null;
    
    public Application() {
        setTitle("Video Summarization Application");
        setSize(Application.FRAME_WIDTH, Application.FRAME_HEIGHT);
       this.getContentPane().setLayout(new GridLayout(6, 3, 2, 1));
        setDefaultCloseOperation(Application.EXIT_ON_CLOSE);
        createBrowseFileButton();
        createBrowseFile();
        createThreshouldLevelBox();
        createSelectedFileLabel("");
        createConvertButton();
        createOutputBrowseFile();
        createOutputFileLabel("");
        createProgressBar();
        setVisible(true);
        
    }
    private void createThreshouldLevelBox() {
        this.threshouldLevel = new JComboBox();
        threshouldLevel.addItem("Low Summarized");
        
        threshouldLevel.addItem("Medium Summarized");
        threshouldLevel.addItem("High Summarized");
        threshouldLevel.addItem("Very High Summarized");
        this.add(threshouldLevel);
    }
    private void createBrowseFile() {
        this.chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter ("wmv or avi", "wmv", "avi");
        chooser.setFileFilter(filter);
   }
   private void createOutputBrowseFile() {
        this.outputChooser = new JFileChooser();
        outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

   }
    
    private void createBrowseFileButton() {
        this.selectedFile = new JButton();
        this.selectedFile.setText("Choose File");
        this.selectedFile.addActionListener(this);
        this.selectedFile.setActionCommand("open_menu");
        this.selectedFile.setSize(100, 200);
        this.selectedFile.setVisible(true);
        this.getContentPane().add(this.selectedFile);
    }
    
    private void createSelectedFileLabel(String text) {
      this.selectedFileName = new JLabel();
      this.selectedFileName.setText(text);
      this.selectedFileName.setVisible(true);
      this.getContentPane().add(this.selectedFileName);
   }
   private void createOutputFileLabel(String text) {
      this.outputFilePath = new JLabel();
      this.outputFilePath.setText(text);
      this.outputFilePath.setVisible(true);
      this.getContentPane().add(this.outputFilePath);
   }
   private void createProgressBar() {
       this.progressBar = new JProgressBar(0, 100);
       this.progressBar.setValue(0);
       this.progressBar.setStringPainted(true);
       this.progressBar.setVisible(false);
       this.progressBar.setIndeterminate(true);
       this.getContentPane().add(this.progressBar);
   }
    private void createConvertButton() {
        this.convertButton = new JButton();
        this.convertButton.setText("Summarize");
        this.convertButton.addActionListener(this);
        this.convertButton.setActionCommand("summarize");
        this.convertButton.setSize(100, 200);
        this.convertButton.setVisible(false);
        this.getContentPane().add(this.convertButton);
    
    }
    private void showInputDialog() {
         int returnVal = chooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                if (this.selectedFileName != null) {
                    this.selectedFileName.setText(chooser.getSelectedFile().getName());
                    if (this.convertButton != null) {
                        this.convertButton.setVisible(true);
                    }
                    
                }
                
        }
    }
    private void showOutPutDialog() {
       int returnVal = outputChooser.showOpenDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                if (this.outputFilePath != null) {
                    this.outputFilePath.setText(outputChooser.getSelectedFile().getAbsolutePath());
                        this.progressBar.setVisible(true);
                        int level = Summarizier.MEDIUM_LEVEL;
                        if (threshouldLevel.getSelectedIndex() == 0) {
                          level = Summarizier.LOW_LEVEL;
                        } else if (threshouldLevel.getSelectedIndex() == 2) {
                            level = Summarizier.HIGH_LEVEL;
                        } else if (threshouldLevel.getSelectedIndex() == 3) {
                            level = Summarizier.VERY_HIGH_LEVEL;
                        }
                        new Thread(new Summarizier(this.progressBar, this.chooser.getSelectedFile().getAbsolutePath(), this.outputChooser.getSelectedFile().getAbsolutePath(), level)).start();
                       
                }
                
        }
    }
    
    
     @Override
    public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("open_menu")) {
                this.showInputDialog();
            } else if (ae.getActionCommand().equals("summarize")) {
                this.showOutPutDialog();
                System.out.println("converting");
            }
     }

}
