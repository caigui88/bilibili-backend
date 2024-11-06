package com.bilibili.chat.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateVO {
    // Getter and Setter for flag
    private boolean flag;
    // Getter and Setter for code
    private int code;
    // Getter and Setter for desc
    private String desc;
    // Getter and Setter for count
    private Integer count;
    // Getter and Setter for data
    private Data data;

    public static class Data {
        private String sid;
        private String coverImgSrc;
        private String title;
        private String subTitle;
        private String Outline;

        // Getter and Setter for sid
        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        // Getter and Setter for coverImgSrc
        public String getCoverImgSrc() {
            return coverImgSrc;
        }

        public void setCoverImgSrc(String coverImgSrc) {
            this.coverImgSrc = coverImgSrc;
        }

        // Getter and Setter for title
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        // Getter and Setter for subTitle
        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getOutline() {
            return Outline;
        }

        public void setOutline(String outline) {
            this.Outline = outline;
        }
    }
}
