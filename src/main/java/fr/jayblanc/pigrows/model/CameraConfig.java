package fr.jayblanc.pigrows.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class CameraConfig {

    @Min(0) @Max(100)
    private int quality = 95;
    @Min(-100) @Max(100)
    private int sharpness = 0;
    @Min(-100) @Max(100)
    private int contrast = 0;
    @Min(0) @Max(100)
    private int brightness = 50;
    @Min(-100) @Max(100)
    private int saturation = 0;
    @Min(-10) @Max(10)
    private int ev = 0;
    @Min(100) @Max(800)
    private int iso = 200;
    private Exposure exposure = Exposure.AUTO;
    private WhiteBalance whiteBalance = WhiteBalance.AUTO;

    public CameraConfig() {
        super();
    }

    public CameraConfig(int quality, int sharpness, int contrast, int brightness, int saturation, int iso, Exposure exposure, WhiteBalance wb) {
        super();
        this.quality = quality;
        this.sharpness = sharpness;
        this.contrast = contrast;
        this.brightness = brightness;
        this.saturation = saturation;
        this.iso = iso;
        this.exposure = exposure;
        this.whiteBalance = wb;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getSharpness() {
        return sharpness;
    }

    public void setSharpness(int sharpness) {
        this.sharpness = sharpness;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getIso() {
        return iso;
    }

    public void setIso(int iso) {
        this.iso = iso;
    }

    public int getEv() {
        return ev;
    }

    public void setEv(int ev) {
        this.ev = ev;
    }

    public Exposure getExposure() {
        return exposure;
    }

    public void setExposure(Exposure exposure) {
        this.exposure = exposure;
    }

    public WhiteBalance getWb() {
        return whiteBalance;
    }

    public void setWb(WhiteBalance wb) {
        this.whiteBalance = wb;
    }
    
    public enum Exposure {
        AUTO, NIGHT, BACKLIGHT, SPORTS, SNOW, BEACH, VERYLONG, FIXEDFPS, ANTISHAKE, FIREWORKS
    }
    
    public enum WhiteBalance {
        OFF, AUTO, SUN, CLOUD, SHADE, TUNGSTEN, FLUORESCENT, INCANDESCENT, FLASH, HORIZON
    }

}
