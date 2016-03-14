package fr.jayblanc.pigrows.model;

public class DeviceConfig {

    private int quality = 90;
    private int sharpness = 0;
    private int contrast = 0;
    private int brightness = 0;
    private int saturation = 0;
    private int ISO = 200;
    private int exposure = 0;
    private WhiteBalance whiteBalance = WhiteBalance.AUTO;

    public DeviceConfig() {
        super();
    }

    public DeviceConfig(int quality, int sharpness, int contrast, int brightness, int saturation, int iSO, int exposure, WhiteBalance wb) {
        super();
        this.quality = quality;
        this.sharpness = sharpness;
        this.contrast = contrast;
        this.brightness = brightness;
        this.saturation = saturation;
        ISO = iSO;
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

    public int getISO() {
        return ISO;
    }

    public void setISO(int iSO) {
        ISO = iSO;
    }

    public int getExposure() {
        return exposure;
    }

    public void setExposure(int exposure) {
        this.exposure = exposure;
    }

    public WhiteBalance isAwb() {
        return whiteBalance;
    }

    public void setAwb(WhiteBalance awb) {
        this.whiteBalance = awb;
    }
    
    public enum WhiteBalance {
        OFF, AUTO, SUN, CLOUD, SHADE, TUNGSTEN, FLUORESCENT, INCANDESCENT, FLASH, HORIZON
    }

}
