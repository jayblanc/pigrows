package fr.jayblanc.pigrows.model;

public class Folder {

    private String name;
    private String lastModification;
    private long nbFiles;
    private long size;

    public Folder() {
    }

    public Folder(String name, String lastModification, long nbFiles, long size) {
        super();
        this.name = name;
        this.lastModification = lastModification;
        this.nbFiles = nbFiles;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModification() {
        return lastModification;
    }

    public void setLastModification(String lastModification) {
        this.lastModification = lastModification;
    }

    public long getNbFiles() {
        return nbFiles;
    }

    public void setNbFiles(long nbFiles) {
        this.nbFiles = nbFiles;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
