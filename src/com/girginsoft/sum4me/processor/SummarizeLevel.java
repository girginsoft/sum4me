/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.girginsoft.sum4me.processor;
/**
 *
 * @author girginsoft
 */
public enum SummarizeLevel {
        LOW("Low Summarized"), MEDIUM("Medium Summarized"), HIGH("High Summarized");
        private String sl = null;

        private SummarizeLevel(String level) {
            this.sl = level;
        }
        public String getSummarizeLevel() {
            return sl;
        }
}
