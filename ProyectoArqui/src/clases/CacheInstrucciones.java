/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author dave
 */
public class CacheInstrucciones {
    int cache[][] = new int[5][8];
    public CacheInstrucciones(Bus b){bus = b;};
    public int getInstruccion(int dir, int word){
        if(verificarBloque(dir)) {
            return findWord(dir, word);
        } else {
            traeBloque();
            return findWord(dir, word);
        }
    };
    public boolean verificarBloque(int dir){
        if(cache[4][dir%(8*4)]==dir) {
            return true;
        } else {
            return false;
        }
        
    };
    public int findWord(int block, int word){return cache[word][block%8];};
    public void traeBloque(){};
    Bus bus; 
}
