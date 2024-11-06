package com.bilibili.chat.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProgressVO {
    // Getters and setters for code
    private int code;
    // Getters and setters for desc
    private String desc;
    // Getters and setters for data
    private Data data;

    public static class Data {
        private int process;
        private String pptId;
        private String pptUrl;

        // Getters and setters for process
        public int getProcess() {
            return process;
        }

        public void setProcess(int process) {
            this.process = process;
        }

        // Getters and setters for pptId
        public String getPptId() {
            return pptId;
        }

        public void setPptId(String pptId) {
            this.pptId = pptId;
        }

        // Getters and setters for pptUrl
        public String getPptUrl() {
            return pptUrl;
        }

        public void setPptUrl(String pptUrl) {
            this.pptUrl = pptUrl;
        }
    }
}
