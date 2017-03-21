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
 *
 * @author eghar
 * @created 15/03/16
 */
public class PuzzleCracking {
    
    public ArrayList<String> puzzles = new ArrayList();
    
    public String chosenPuzzle;
    
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
     * @return 
     */
    public String choosePuzzle(){
        Random rand = new Random();
        int rn = rand.nextInt(1023) + 0;
        
        chosenPuzzle = puzzles.get(rn);
        return chosenPuzzle;
    }
    
    /**
     * Generates 8 byte array for DES Key creation
     * @param i
     * @return 
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
     */
    public void crackPuzzle() throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,NoSuchPaddingException{
        cipher = Cipher.getInstance("DES");
        byte[] decryptedByte = new byte[26];
        
        //Convert chosen puzzle back to byte array
        byte[] encryptedPuzzle = cl.stringToByteArray(choosePuzzle());
        System.out.println("ENCRYPTED PUZZLE: " + Arrays.toString(encryptedPuzzle));
        
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
                
                System.out.println("DECRYPTED: " + j + "   " + Arrays.toString(decryptedByte)+ "   " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
                   j++;
                
                if(checkPuzzle(decryptedByte)){
                    
                    saveKey(secretKey); 
                    
                    getPuzzleNumber(decryptedByte);
                }
            }catch(BadPaddingException e){
                
                  //System.out.println("FAILED: " + i + "   " + Arrays.toString(decryptedByte));
            }
            
        }
    }
    
    //create a methiod to check leading 0
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
    
    public byte[] getPuzzleNumber(byte[] decryptedPuzzle){
        
        for(int i = 16; i<18; i++){
            if(i == 16){
                puzzleNumber[0] = decryptedPuzzle[i];
            }else{
                puzzleNumber[1] = decryptedPuzzle[i];
            }
                  
        }
        System.out.println("PUZZLE NUMBER: " + puzzleNumber);
        return puzzleNumber;
    }
    
    public void saveKey(SecretKey key){
        try{
            //Init Print Writer.
            PrintWriter writer = new PrintWriter("key.txt", "UTF-8");
          
                
            // Convert encrypted puzzle to string
            String keyToString = Base64.getEncoder().encodeToString(key.getEncoded()) ;
                
            // Write string to file
            writer.println(keyToString);
        
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /*
    public void savePuzzleNumber(byte[] puzzleNumber){
        
        try {
            
            PrintWriter writer = new PrintWriter("puzzleNumber.txt", "UTF-8");
            
            String pnToString = Arrays.toString(puzzleNumber);
            
            writer.println(pnToString);
        
            writer.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PuzzleCracking.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PuzzleCracking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    */
    
    public byte[] keyLookUp(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader("unEncryptedPuzzles.txt"));
            String puzzle = reader.readLine();
            boolean rightPuzzle;
            byte[] currentPuzzle = new byte[26];
            
            while(puzzle!=null){
                currentPuzzle = cl.stringToByteArray(puzzle);
                
                if(currentPuzzle[16]==puzzleNumber[0]&& currentPuzzle[17]==puzzleNumber[1]) {
                    rightPuzzle = true;
                }else{
                    rightPuzzle = false;
                }
       
                puzzle = reader.readLine();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
