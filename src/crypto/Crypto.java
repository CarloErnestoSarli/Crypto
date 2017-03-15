/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author carlosarli
 * @created 14/03/16
 */
public class Crypto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
        PuzzleGen pg = new PuzzleGen();
        PuzzleCracking pc = new PuzzleCracking();
        
        pg.generate();
        pg.puzzleEnrypt();
        pg.puzzleStorage();
        pc.readFile();
        pc.crackPuzzle();
        
    }
    
}
