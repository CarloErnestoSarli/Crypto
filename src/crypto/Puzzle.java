/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.util.Arrays;

/**
 *
 * @author carlosarli
 * @author edharper
 * @created 14/03/16
 */
public class Puzzle {
    
    private int m_Number;
    private byte[] lz;
    private byte[] id;
    private byte[] des;
    
    /**
     * Constructor for the puzzle class
     * @param number
     * @param id
     * @param des
     */
    public Puzzle(int number, byte[] id, byte[] des){
        m_Number = number;
        this.lz = new byte[16];
        this.id = id;
        this.des = des;
    }
    
    /**
     * getter for the variable m_Number
     * @return m_Number
     */
    public int getNumber(){
        return m_Number;
    }
    
    /**
     * getter for the 16 byte all 0s array
     * @return lz
     */
    public byte[] getLz(){
        return lz;
    }
    
    /**
     * getter for the array containing the id of the puzzle
     * @return id
     */
    public byte[] getId(){
        return id;
    }
    
    /**
     * getter for the 8byte key of the puzzle
     * @return des
     */
    public byte[] getDes(){
        return des;
    }
    
    /**
     * setter for the variable m_Number
     * @param number
     */
    public void setNumber(int number){
        m_Number = number;
    }
    
    /**
     * setter for the array containing the id of the puzzle
     * @param id
     */
    public void setId(byte[] id){
        this.id = id;
    }
    
    /**
     *  setter for the array containing the des key of the puzzle
     * @param id
     */
    public void setDes(byte[] id){
        this.des = des;
    }
    
    /**
     * Creates the puzzle combining the three different byte arrays generated before
     * @return puzzleBytes
     */
    public byte[] getPuzzleBytes(){
        byte[] puzzleBytes = new byte[getLz().length + getId().length + getDes().length];
        
        // Copy leading zeros
        System.arraycopy(getLz(), 0, puzzleBytes, 0, getLz().length);
        
        // Copy unique Id bytes
        System.arraycopy(getId(), 0, puzzleBytes, getLz().length, getId().length);
        
        // Copy key bytes
        System.arraycopy(getDes(), 0, puzzleBytes, getId().length + getLz().length, getDes().length);
        return puzzleBytes;
    }
    
    /**
     * prints the puzzles out as strings
     *  
     */
    
    public String toString(){
        
        return "id = " + Arrays.toString(getId()) + " " + " Key = " + Arrays.toString(getDes());
         
    }
    
}
