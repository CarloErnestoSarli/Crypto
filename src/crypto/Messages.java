/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import static crypto.PuzzleGen.cipher;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * ALICE & BOB
 * Encrypts and decrypts messages
 * @author carlosarli
 * @author edharper
 * @date 21/03/2017
 */
public class Messages {

    /**
     *
     */
    public CryptoLib cl;
    static Cipher cipher;
    
    /**
     * BOB
     * Encrypts string message.
     * @param message
     * @param sharedKey
     * @return encryptedText
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public String sendMessage(String message, byte[] sharedKey) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        cipher = Cipher.getInstance("DES");
        SecretKey encryptionKey = cl.createKey(sharedKey);
        byte[] messageBytes = message.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            
        // Encrypt puzzles
        byte[] encryptedPuzzle = cipher.doFinal(messageBytes);
        
        Base64.Encoder encoder = Base64.getEncoder();
	String encryptedText = encoder.encodeToString(encryptedPuzzle);
        return encryptedText;
    }
    
    /**
     * ALICE
     * Decrypts string encrypted message
     * @param encryptedMessage
     * @param sharedKey
     * @return decryptedText
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     */
    public String decryptMessage(String encryptedMessage, byte[] sharedKey) throws IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException{
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedMessage);
        
        SecretKey encryptionKey = cl.createKey(sharedKey);
        //Initialise the cipher to be in decryption mode, using the given key.
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey);

        //Perform the decryption
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);

        //Convert byte representation of plaintext into a string
        String decryptedText = new String(decryptedByte);

        return decryptedText;
    }
}
