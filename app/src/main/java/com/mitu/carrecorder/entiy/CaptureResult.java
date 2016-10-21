package com.mitu.carrecorder.entiy;

/**
 * 说明：
 * 2016/6/30 0030
 */
public class CaptureResult extends Command {

    private String photoName;
    private String photoPath;
    private String freePicNum;

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getFreePicNum() {
        return freePicNum;
    }

    public void setFreePicNum(String freePicNum) {
        this.freePicNum = freePicNum;
    }
}
