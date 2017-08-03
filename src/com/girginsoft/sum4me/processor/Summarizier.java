/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girginsoft.sum4me.processor;

import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

/**
 *
 * @author girginsoft
 */
public class Summarizier implements Runnable{
    protected JProgressBar progressBar;
    protected String absoluteInputPath;
    protected String absoluteOutputPath;
    public static final int LOW_LEVEL = 15000;
    public static final int MEDIUM_LEVEL = 25000;
    public static final int HIGH_LEVEL = 35000;
    public static final int VERY_HIGH_LEVEL = 45000;
    public Summarizier (JProgressBar progressBar, String absoluteInputPath, String absoluteOutputPath, int level ) {
        this.progressBar = progressBar;
        this.absoluteInputPath = absoluteInputPath;
        this.absoluteOutputPath = absoluteOutputPath;
        this.threshould = level;
    }
    public  void process() throws Exception, FrameGrabber.Exception, IOException {
        this.progressBar.setIndeterminate(true);
        System.out.println("Welcome to OpenCV " + Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture video = new VideoCapture();
        video.open(this.absoluteInputPath);
     
        if (!video.isOpened()) {
            System.out.println("Error");
        } else {
            Mat frame = new Mat();
            int i = 0;
            String filename = absoluteOutputPath + "/frame{0}.jpg";
            while (video.read(frame)) {
                  //  System.out.println("Frame Obtained");
                    //System.out.println("Captured Frame Width "+ frame.width() + " Height " + frame.height());
                    Highgui.imwrite(filename.replace("{0}", Integer.toString(i++)), frame);
                    System.out.println("OK " + i + ". frame");
             }
             //i = 2887;
             int j = i;
             ArrayList<Integer> removedItems = new ArrayList<Integer>();
             int referanceFrameIndex = 0;
             int totalRemoved = 0;
             ArrayList<Integer> removedFiles = new ArrayList<Integer>();
             while((i - j + 1) < i + 1) {
                        String current = filename.replace("{0}", Integer.toString(i - j));
                        String previous = filename.replace("{0}", Integer.toString(referanceFrameIndex));
                        Histogram currentFrame = Histogram.getHisrogram(current);
                        Histogram previousFrame = Histogram.getHisrogram(previous);
                        boolean willFrameRemoved = willFrameRemoved(currentFrame, previousFrame);
                        if (!willFrameRemoved) {
                            referanceFrameIndex = i - j - 1;
                      
                        } else {
                            System.out.println(current + " will be removed");
                            totalRemoved++;
                            removedFiles.add(i - j);
                        }
                        j --;
                        //System.out.println(current + " - " + moi.dump());
                 
             }
              FrameGrabber grabber = new OpenCVFrameGrabber(this.absoluteInputPath);
              FrameRecorder recorder = new OpenCVFrameRecorder(absoluteOutputPath + "/output.avi", 320, 240); 
              recorder.start(); 
              int counter = 0;
              this.progressBar.setIndeterminate(false);
              while(counter < i) {
                 boolean found = false;
                 for (Integer rem : removedFiles) {
                        if (counter == rem) {
                            found = true;
                        }
                  }
                 String currentFileName = filename.replace("{0}", Integer.toString(counter));
                  if (!found) {
                      System.out.println("Frame is being added to video" + currentFileName);
                      recorder.record(com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage(filename.replace("{0}", Integer.toString(counter))));
                  } else {
                      System.err.println(currentFileName + "  deleting this file");
                      new File(currentFileName).delete();
                  }
                 this.progressBar.setValue(counter * 100 / (i - removedFiles.size()));
                  counter++;
              }
             System.err.println(totalRemoved + " frame is removed from " + i);
             recorder.release();
             java.awt.Desktop.getDesktop().open(new File(absoluteOutputPath + "/output.avi"));
        }
        video.release();
    }
    private int threshould = 25000;
     private  boolean willFrameRemoved(Histogram currentFrame, Histogram previousFrame) {
          double total  = calculateBlueDiff(currentFrame, previousFrame) + calculateGreenDiff(currentFrame, previousFrame) + calculateRedDiff(currentFrame, previousFrame);
          return total < this.threshould;
    }
    private static double calculateRedDiff(Histogram currentFrame, Histogram previousFrame) {
          MatOfInt currentHistogram = new MatOfInt();
          currentHistogram.fromArray(currentFrame.getRedHistogram());
          MatOfInt previousHistogram = new MatOfInt();
          previousHistogram.fromArray(previousFrame.getRedHistogram());
         return calculateEucledianDistance( currentHistogram,  previousHistogram);
    }
    
    private static double calculateBlueDiff(Histogram currentFrame, Histogram previousFrame) {
          MatOfInt currentHistogram = new MatOfInt();
          currentHistogram.fromArray(currentFrame.getBlueHistogram());
          MatOfInt previousHistogram = new MatOfInt();
          previousHistogram.fromArray(previousFrame.getBlueHistogram());
         return calculateEucledianDistance( currentHistogram,  previousHistogram);
    }
      private static double calculateGreenDiff(Histogram currentFrame, Histogram previousFrame) {
          MatOfInt currentHistogram = new MatOfInt();
          currentHistogram.fromArray(currentFrame.getGreenHistogram());
          MatOfInt previousHistogram = new MatOfInt();
          previousHistogram.fromArray(previousFrame.getGreenHistogram());
         return calculateEucledianDistance( currentHistogram,  previousHistogram);
    }

    private static double calculateEucledianDistance(MatOfInt currentHistogram, MatOfInt previousHistogram) {
        MatOfInt result = new MatOfInt();
        Core.subtract(currentHistogram, previousHistogram, result);
        double sum = 0;
        for (Integer i : result.toList()) {
               sum += Math.abs(i);
        }
        return sum;
    }

    @Override
    public void run() {
        try {
            this.process();
        } catch (Exception ex) {
            Logger.getLogger(Summarizier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(Summarizier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Summarizier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
}
