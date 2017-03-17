/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author carlosarli
 */
public class Crypto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // TODO code application logic here
        GeneratePuzzles gen = new GeneratePuzzles();
        gen.generate();
    }
    
}
