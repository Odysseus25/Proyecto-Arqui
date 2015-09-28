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
    public CacheInstrucciones(Bus b){
        bus = b;
        for(int i=0; i<cache.length-1; i++) {
            for(int j=0; j<cache[i].length; j++) {
                cache[i][j]=0;
            }
        }
        for(int j=0; j<cache[4].length; j++) {
            cache[4][j]=-1;
        }
    };
    public int getInstruccion(int block, int word){
        if(verificarBloque(block)) {
            return findWord(block, word);
        } else {
            traeBloque(block);
            return findWord(block, word);
        }
    };
    public boolean verificarBloque(int block){
        if(cache[4][block%8]==block) {
            return true;
        } else {
            return false;
        }
        
    };
    
    public int findWord(int block, int word){return cache[word][block%8];};
    
    public void traeBloque(int block){
        int[] bloque = new int[4];
        bloque = bus.getBloque(block);
        for(int i=0; i<4; i++) {
            cache[i][block%8] = bloque[i];
        }
        cache[4][block%8] = block;
    };
    Bus bus; 
}
