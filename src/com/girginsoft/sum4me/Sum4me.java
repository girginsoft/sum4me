/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girginsoft.sum4me;

import com.girginsoft.sum4me.processor.Summarizier;
import com.girginsoft.sum4me.view.Application;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameRecorder.Exception;



/**
 *
 * @author girginsoft
 */
public class Sum4me{

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception, FrameGrabber.Exception {
        
//        Summarizier.process();
        new Application();
    }

}