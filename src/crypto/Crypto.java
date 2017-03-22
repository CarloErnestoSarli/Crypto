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
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.BadPaddingException
     * @throws java.security.InvalidKeyException
     * @throws java.security.spec.InvalidKeySpecException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // TODO code application logic here
        PuzzleGen pg = new PuzzleGen();
        PuzzleCracking pc = new PuzzleCracking();
        Messages m = new Messages();
        
        byte[] sharedKeyBob =  new byte[8];
        byte[] sharedKeyAlice =  new byte[8];
        
        //Generate and print out puzzles
        System.out.println("GENERATED PUZZLES: ");
        pg.generate();
        
        // Encrypt puzzles
        pg.puzzleEnrypt();
        // Sore Puzzles in file "Puzzles.txt"
        pg.puzzleStorage();
        

        // Cracking Puzzles
        System.out.println("****** CRACKING PUZZLES **************");
        // Read in puzzles.txt and add to array
        pc.readFile();
        // Brute force crack puzzles and save puzzlekey
        sharedKeyBob = pc.crackPuzzle();
        
        // ALICE PUZZLE LOOKUP //
        // Alice looking up puzzle
        byte[] puzzle = pc.puzzleLookUp();
        System.out.println("Alice received puzzle number and looks up puzzle: " + Arrays.toString(puzzle));
        // Alice getting shared key
        sharedKeyAlice = pc.sharedKeyAlice(puzzle);
        
        // Alice message encryption and send
        String aliceMessage = "Hey bob, first message test";
        System.out.println("Alice has message to encrypt: " + aliceMessage);
        String encryptedMessage = m.sendMessage(aliceMessage, sharedKeyAlice);
        System.out.println("Alice : Sending encrypted message to bob: " + encryptedMessage);
        
        // Bob receives and decrypts message
        String decryptedMessage = m.decryptMessage(encryptedMessage, sharedKeyBob);
        System.out.println("Bob: Receives message and decrypts message to: " + decryptedMessage);
    }
    
}
