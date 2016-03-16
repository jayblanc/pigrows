package fr.jayblanc.pigrows.model;

public class Device {

    private String key;
    private String name;
    private String description;
    private long registration;
    private long lastActivity;
    private PictureParams masterPictureConfig;
    private PictureParams slavePictureConfig;
    private boolean configChanged = false;

    public Device(String key, String name, String description) {
        super();
        this.key = key;
        this.name = name;
        this.description = description;
        this.registration = System.currentTimeMillis();
        this.lastActivity = this.registration;
        masterPictureConfig = new PictureParams();
        slavePictureConfig = new PictureParams();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRegistration() {
        return registration;
    }

    public void setRegistration(long registration) {
        this.registration = registration;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public boolean isConfigChanged() {
        return configChanged;
    }

    public void setConfigChanged(boolean configChanged) {
        this.configChanged = configChanged;
    }

    public PictureParams getMasterPictureConfig() {
        return masterPictureConfig;
    }

    public void setMasterPictureConfig(PictureParams masterPictureConfig) {
        this.masterPictureConfig = masterPictureConfig;
    }

    public PictureParams getSlavePictureConfig() {
        return slavePictureConfig;
    }

    public void setSlavePictureConfig(PictureParams slavePictureConfig) {
        this.slavePictureConfig = slavePictureConfig;
    }

}
