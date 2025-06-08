package model;
import servlet.*;
import model.SecurityServlet;

public class TestEncryptionServlet {
    public static void main(String[] args) {
        String str = "tsnow123";
        
        System.out.println("Original Str: " + str);
        
        String encrypStr = SecurityServlet.encrypt(str);
        
        System.out.println("Encrypted version: " + encrypStr);
        
        System.out.println("Decrypted version: " + SecurityServlet.decrypt(encrypStr));
    }
}
