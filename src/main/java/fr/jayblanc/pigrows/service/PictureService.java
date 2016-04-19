package fr.jayblanc.pigrows.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import fr.jayblanc.pigrows.model.Folder;
import fr.jayblanc.pigrows.model.Picture;

public interface PictureService {
    
    String store(String key, String filename, InputStream content) throws IOException, IllegalArgumentException, NoSuchAlgorithmException;
    
    List<Folder> listFolders(String key) throws IOException;
    
    List<Picture> listPictures(String key, String folder, int offset, int limit) throws IOException;
    
    long count(String key, String folder) throws IOException;
    
    File download(String key, String filename) throws IllegalArgumentException;
    
    void exportFolder(String key, String folder, OutputStream os) throws IOException;
    
    void exportAll(String key, OutputStream os) throws IOException;
    
    void purgeFolder(String key, String folder);
    
    void purgeAll(String key) throws IOException;

}
