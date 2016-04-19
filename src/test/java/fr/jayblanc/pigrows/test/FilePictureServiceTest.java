package fr.jayblanc.pigrows.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.BeforeClass;
import org.junit.Test;

import fr.jayblanc.pigrows.service.LocalFilePictureService;

public class FilePictureServiceTest {
    
    @BeforeClass
    public static void setEnv() {
        System.setProperty("PIGROWS_HOME", "/tmp/pigrows");
    }
    
    @Test
    public void testPostPicture() throws IllegalArgumentException, IOException, NoSuchAlgorithmException {
        LocalFilePictureService service = LocalFilePictureService.getInstance();
        String dig1 = service.store("123456", "L123456_D20160401_T11:44:15_M.jpg", new ByteArrayInputStream("tagada54".getBytes()));
        String dig2 = service.store("a32b54", "La32b54_D20170502_T13:44:15_S.png", new ByteArrayInputStream("tagada54".getBytes()));
        String dig3 = service.store("f12b54", "Lf12b54_D20151202_T00:12:53_M.bmp", new ByteArrayInputStream("tagada55".getBytes()));
        System.out.println(dig1);
        System.out.println(dig2);
        System.out.println(dig3);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testPostPicture2() throws IllegalArgumentException, IOException, NoSuchAlgorithmException {
        LocalFilePictureService service = LocalFilePictureService.getInstance();
        service.store("123456", "L123456_D20160401_T11:44:15_Z.jpg", new ByteArrayInputStream("tagada54".getBytes()));
    }

    

}
