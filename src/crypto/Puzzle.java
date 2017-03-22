/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.util.Arrays;

/**
 *
 * @author eghar
 * @created 14/03/16
 */
public class Puzzle {
    
    private int number;
    private byte[] lz;
    private byte[] id;
    private byte[] des;
    
    /**
     *
     * @param number
     * @param id
     * @param des
     */
    public Puzzle(int number, byte[] id, byte[] des){
        this.number = number;
        this.lz = new byte[16];
        this.id = id;
        this.des = des;
    }
    
    /**
     *
     * @return
     */
    public int getNumber(){
        return number;
    }
    
    /**
     *
     * @return
     */
    public byte[] getLz(){
        return lz;
    }
    
    /**
     *
     * @return
     */
    public byte[] getId(){
        return id;
    }
    
    /**
     *
     * @return
     */
    public byte[] getDes(){
        return des;
    }
    
    /**
     *
     * @param number
     */
    public void setNumber(int number){
        this.number = number;
    }
    
    /**
     *
     * @param id
     */
    public void setId(byte[] id){
        this.id = id;
    }
    
    /**
     *
     * @param id
     */
    public void setDes(byte[] id){
        this.des = des;
    }
    
    /**
     *
     * @return
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
    
    public String toString(){
        return Arrays.toString(getPuzzleBytes());
         
    }
    
}
