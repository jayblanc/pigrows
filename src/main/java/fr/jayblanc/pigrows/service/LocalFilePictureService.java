package fr.jayblanc.pigrows.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.jayblanc.pigrows.PiGrowsConfig;
import fr.jayblanc.pigrows.model.Folder;
import fr.jayblanc.pigrows.model.Picture;

public class LocalFilePictureService implements PictureService {

    private static final Logger LOGGER = Logger.getLogger(PiGrowsConfig.class.getName());
    private static final String FILENAME_PATTERN = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2})\\:(\\d{2})\\:(\\d{2}).*";
    private Path store;

    private LocalFilePictureService() {
        store = Paths.get(PiGrowsConfig.getInstance().getHomePath().toString(), "pictures");
        LOGGER.log(Level.INFO, "Local File Picture Service store located at: " + store);
    }

    private static class LocalFilePictureServiceHolder {
        private final static LocalFilePictureService instance = new LocalFilePictureService();
    }

    public static LocalFilePictureService getInstance() {
        return LocalFilePictureServiceHolder.instance;
    }

    @Override
    public void store(String key, String filename, InputStream content) throws IOException, IllegalArgumentException {
        LOGGER.log(Level.INFO, "storing new picture with name : " + filename);
        if (filename.matches(FILENAME_PATTERN)) {
            Path file = Paths.get(store.toString(), key, filename.substring(0, 7), filename);
            if (!Files.exists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            try {
                Files.copy(content, file, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                content.close();
            }
        } else {
            throw new IllegalArgumentException("filename does not match pattern YYYY-MM-ddTHH:mm:ss.master.jpg");
        }
    }

    @Override
    public long count(String key, String folder) throws IOException {
        LOGGER.log(Level.INFO, "counting pictures for key/folder: " + key + "/" + folder);
        Path path = Paths.get(store.toString(), key, folder);
        try ( Stream<Path> stream = Files.list(path) ) {
            return stream.count();
        }
    }

    @Override
    public List<Folder> listFolders(String key) throws IOException {
        LOGGER.log(Level.INFO, "listing folders for key: " + key);
        Path path = Paths.get(store.toString(), key);
        try ( Stream<Path> stream = Files.list(path) ) {
            return stream.map(this::pathToFolder).collect(Collectors.toList());
        }
    }

    @Override
    public List<Picture> listPictures(String key, String folder, int offset, int limit) throws IOException {
        LOGGER.log(Level.INFO, "listing pictures for key/folder: " + key + "/" + folder);
        Path path = Paths.get(store.toString(), key, folder);
        try ( Stream<Path> stream = Files.list(path) ) {
            return stream.sorted((p1, p2) -> compareNames(p1, p2)).skip(offset).limit(limit).map(this::pathToPicture).collect(Collectors.toList());
        }
    }

    @Override
    public File download(String key, String filename) {
        LOGGER.log(Level.INFO, "downloading picture with name: " + filename);
        if (filename.matches(FILENAME_PATTERN)) {
            Path path = Paths.get(store.toString(), key, filename.substring(0, 7), filename);
            return path.toFile();
        } else {
            throw new IllegalArgumentException("filename does not match pattern");
        }
    }

    @Override
    public void exportFolder(String key, String folder, OutputStream out) throws IOException {
        LOGGER.log(Level.INFO, "export picture folder: " + key + "/" + folder);
        Path pp = Paths.get(store.toString(), key, folder);
        ZipOutputStream zs = new ZipOutputStream(out);
        try {
            pack(pp, zs);
        } finally {
            zs.close();
        }
    }
    
    @Override
    public void exportAll(String key, OutputStream out) throws IOException {
        LOGGER.log(Level.INFO, "export all pictures: " + key);
        Path pp = Paths.get(store.toString(), key);
        ZipOutputStream zs = new ZipOutputStream(out);
        try {
            Files.walk(pp).filter(path -> Files.isDirectory(path)).forEach(path -> pack(pp, zs));
        } finally {
            zs.close();
        }
    }

    @Override
    public void purgeFolder(String key, String folder) {
        LOGGER.log(Level.INFO, "purge picture folder: " + key + "/" + folder);
        Path pp = Paths.get(store.toString(), key, folder);
        purge(pp);
    }

    @Override
    public void purgeAll(String key) throws IOException {
        LOGGER.log(Level.INFO, "purge all folders: " + key);
        Path pp = Paths.get(store.toString(), key);
        try ( Stream<Path> stream = Files.walk(pp) ) {
            stream.filter(path -> Files.isDirectory(path)).forEach(path -> purge(pp));
        }
    }
    
    private Folder pathToFolder(Path path) {
        LOGGER.log(Level.INFO, "Path to Folder: " + path);
        String foldername = path.getFileName().toString();
        String modification = "";
        long nbfiles = 0;
        long size = 0;
        try {
            modification = Files.getLastModifiedTime(path).toString();
            nbfiles = Files.list(path).count();
            size = Files.list(path).mapToLong(this::size).sum();
        } catch ( Exception e ) {
            LOGGER.log(Level.SEVERE, "unable to read attributes for path: " + path, e);
        }   
        return new Folder(foldername, modification, nbfiles, size);
    }
    
    private long size(Path p) {
        try {
            return Files.size(p);
        } catch (Exception e) {
            return 0;
        }
    }

    private Picture pathToPicture(Path path) {
        String filename = path.getFileName().toString();
        String datetime = filename.substring(0, filename.indexOf("."));
        String month = path.getParent().getFileName().toString();
        String year = path.getParent().getParent().getFileName().toString();
        String key = path.getParent().getParent().getParent().getFileName().toString();
        return new Picture(key, year, month, datetime, filename);
    }

    private void pack(Path source, ZipOutputStream zos) {
        try (Stream<Path> stream = Files.walk(source) ) {
            stream.filter(path -> !Files.isDirectory(path)).forEach(path -> {
                String sp = path.toAbsolutePath().toString().replace(source.toAbsolutePath().toString(), "").replace(path.getFileName().toString(), "");
                ZipEntry zipEntry = new ZipEntry(sp + path.getFileName().toString());
                try {
                    zos.putNextEntry(zipEntry);
                    zos.write(Files.readAllBytes(path));
                    zos.closeEntry();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "unable to write zip entry", e);
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "unable to walk source path", e);
        }
    }
    
    private void purge(Path source) {
        try (Stream<Path> stream = Files.walk(source)) {
            stream.filter(path -> !Files.isDirectory(path)).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "unable to delete file", e);
                }
            });
            Files.delete(source);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "unable to walk source path", e);
        }
    }
    
    private int compareNames(Path p1, Path p2) {
        return p1.getFileName().compareTo(p2.getFileName());
    }

}
