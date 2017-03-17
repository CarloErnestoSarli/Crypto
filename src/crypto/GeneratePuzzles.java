/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
/**
 *
 * @author carlo
 */
public class GeneratePuzzles {
    
    SecureRandom random = new SecureRandom();
     
    private final int NUM_OF_PUZZLES = 1024;
    
    private Map <Integer, byte[]> keys;
    private byte[] plaintextStart = new byte[16];
    
    public void generate() throws NoSuchAlgorithmException{
    
        for(int i = 0; i < NUM_OF_PUZZLES; i ++){
            
            byte[] destination = new byte[26];
            
            byte identifier[] = new byte[2]; 
            random.nextBytes(identifier);
            
            //generate des key of 8 bytes
            byte[] desKey = generateRandomKey().getEncoded();
           
            // copy ciphertext into start of destination (from pos 0, copy ciphertext.length bytes)
            System.arraycopy(plaintextStart, 0, destination, 0, 16);

            // copy mac into end of destination (from pos ciphertext.length, copy mac.length bytes)
            System.arraycopy(identifier, 0, destination, 16, 2);
            
            System.arraycopy(desKey, 0, destination, 18, 8 );
            
            
            keys.put(i, destination);
            
            String puzzle = destination.toString();
            
            System.out.println( puzzle);
        }
        
    }
    
    public SecretKey generateRandomKey() throws NoSuchAlgorithmException{
		//Use java's key generator to produce a random key.
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
		keyGenerator.init(64);
		SecretKey secretKey = keyGenerator.generateKey();
		
                /*
		//print the key
		String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println(encodedKey);
`               */
		return secretKey;
	}
    
}
