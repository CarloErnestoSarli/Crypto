/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @author carlosarli
 * @author edharper
 * @created 15/03/16
 */
public class PuzzleCracking {
    
   
    public ArrayList<String> puzzles = new ArrayList();
    
   
    public String chosenPuzzle;

 
    public String sharedKey;

    
    public byte[] sharedKeyBob = new byte[8];

    
    public byte[] sharedKeyAlice = new byte[8];
    
    static Cipher cipher;

    
    public CryptoLib cl;
    
    byte[] puzzleNumber = new byte[2];
            
    /**
     * Reads "puzzles.txt" and stores encrypted puzzles in ArrayList
     */
    public void readFile(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("puzzles.txt"));
            String puzzle = reader.readLine();
            
            while(puzzle!=null){
                puzzles.add(puzzle);
                puzzle = reader.readLine();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * chooses random puzzle to decrypt.
     * @return chosen puzzle
     */
    public String choosePuzzle(){
        Random rand = new Random();
        int rn = rand.nextInt(1023) + 0;
        
        chosenPuzzle = puzzles.get(rn);
        return chosenPuzzle;
    }
    
    /**
     * Generates 8 byte array for DES Key creation
     * @param i a random puzzle
     * @return  desBytes the des key
     */
    public byte[] genEncryptionKeyBytes(int i){        
        // Gen leading bytes
        byte[] leadingBytes = cl.smallIntToByteArray(i);
        // Create 48 0's
        byte[] endingZeros = new byte[4];
        // Target byte array
        byte[] desBytes = new byte[8];
        
        // Append leadingBytes and endingZeros to give 8 byte array
        System.arraycopy(leadingBytes, 0, desBytes, 0, leadingBytes.length);
        System.arraycopy(endingZeros, 0, desBytes, leadingBytes.length, endingZeros.length);
        
        // Bytes for DES KEY DEBUG
        //System.out.println("Bytes array for DES KEY:  " + Arrays.toString(desBytes));
        return desBytes;
    }
    
    /**
     * Cracks puzzle by looping through and generating all possible keys
     * then cracks the puzzles, it then selects the puzzle with leading 0s,
     * saves the key used to crack it and the cracked puzzle number
     * @return sharedKeyBob
     * @throws java.security.spec.InvalidKeySpecException
     * @throws java.security.InvalidKeyException
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.NoSuchPaddingException
     */
    public byte[] crackPuzzle() throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,NoSuchPaddingException{
        cipher = Cipher.getInstance("DES");
        byte[] decryptedByte = new byte[26];
        
        //Convert chosen puzzle back to byte array
        byte[] encryptedPuzzle = cl.stringToByteArray(choosePuzzle());
        //System.out.println("ENCRYPTED PUZZLE: " + Arrays.toString(encryptedPuzzle));
        
        int j=0;
        for(int i=0; i<65535; i++){
            
            // Generate bytes for DES Key
            byte[] desBytes = genEncryptionKeyBytes(i);
            
            // Create DES key from bytes
            SecretKey secretKey = cl.createKey(desBytes);
            
            try{
                // Add secret key.
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                 
                // Decrpyt puzzle 
                decryptedByte = cipher.doFinal(encryptedPuzzle);
                
                //System.out.println("DECRYPTED: " + j + "   " + Arrays.toString(decryptedByte)+ "   " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
                   j++;
                
                if(checkPuzzle(decryptedByte)){
                    System.out.println("Bobs Decrypted Puzzle: " + Arrays.toString(decryptedByte));
                    // Save shared key
                    sharedKeyBob = getSharedKeyBob(decryptedByte); 
                    
                    // Save puzzle number
                    puzzleNumber = getPuzzleNumber(decryptedByte);
                }
                
            }catch(BadPaddingException e){
                
                  //System.out.println("FAILED: " + i + "   " + Arrays.toString(decryptedByte));
            }
            
        }
        System.out.println("Bob's shared key: " + Arrays.toString(sharedKeyBob));
        System.out.println("Bob sends alice puzzle number: " + Arrays.toString(puzzleNumber));
        return sharedKeyBob;
    }
    
    /**
     * Checks puzzle is the puzzle we are looking for
     * @param decryptedPuzzle the decrypted puzzle to be checked
     * @return allZero which is true if the puzzle starts with 16 0s
     */
    private boolean checkPuzzle(byte[] decryptedPuzzle){
        
        boolean allZero = false;
        
        for(int i = 0; i<16; i ++){
            
            if(decryptedPuzzle[i] == 0){
                allZero = true;
                
            }else{
                allZero = false;
            }      
        }
        return allZero;
    }
        
    /**
     * BOB
     * Gets bobs shared key from the byte array he decrypts
     * @param decryptedByte
     * @return sharedKeyBob
     */     
    public byte[] getSharedKeyBob(byte[] decryptedByte){  

        // Copies the shared key from the decrypted bite
        System.arraycopy(decryptedByte, 18, sharedKeyBob, 0, 8);

        return sharedKeyBob;
    }
    
    /**
     * BOB
     * Gets puzzle number from the decrypted puzzle
     * @param decryptedPuzzle
     * @return puzzleNumber
     */
    public byte[] getPuzzleNumber(byte[] decryptedPuzzle){
        
        for(int i = 16; i<18; i++){
            if(i == 16){
                puzzleNumber[0] = decryptedPuzzle[i];
            }else{
                puzzleNumber[1] = decryptedPuzzle[i];
            }
                  
        }
        return puzzleNumber;
    }
    
    /**
     * ALICE
     * Puzzle lookup using puzzle number, it reads all the puzzles from the file and
     * checks that the id corresponds to the cracked puzzle id
     * @return currentPuzzle
     */
    public byte[] puzzleLookUp(){
        byte[] currentPuzzle = new byte[26];
        try{
            BufferedReader reader = new BufferedReader(new FileReader("unEncryptedPuzzles.txt"));
            String puzzle = reader.readLine();
            boolean rightPuzzle;
            
            while(puzzle!=null){
                currentPuzzle = cl.stringToByteArray(puzzle);
                
                if(currentPuzzle[16]==puzzleNumber[0]&& currentPuzzle[17]==puzzleNumber[1]) {
                    rightPuzzle = true;
                    break;
                }else{
                    rightPuzzle = false;
                }
                
                puzzle = reader.readLine();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        return currentPuzzle;
    }
    
    /**
     * ALICE
     * Gets Alice's shared key from the current puzzle she has looked up
     * @param currentPuzzle
     * @return sharedKeyAlice
     */
    public byte[] sharedKeyAlice(byte[] currentPuzzle){
        try{
            //Init Print Writer.
            PrintWriter writer = new PrintWriter("sharedKey.txt", "UTF-8");
            
            System.arraycopy(currentPuzzle, 18, sharedKeyAlice, 0, 8);
            // Convert encrypted puzzle to string
                
            // Write string to file
            System.out.println("Shared Key Alice: " + Arrays.toString(sharedKeyAlice));
        
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }  
        return sharedKeyAlice;
    }
}
