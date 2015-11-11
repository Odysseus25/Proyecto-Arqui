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
    int cache[][] = new int[(4*4)+1][8];
    public CacheInstrucciones(Bus b){
        bus = b;
        for(int i=0; i<cache.length-1; i++) {
            for(int j=0; j<cache[i].length; j++) {
                cache[i][j]=0;
            }
        }
        for(int j=0; j<cache[16].length; j++) {
            cache[16][j]=-1;
        }
    };
    public int[] getInstruccion(int block, int word, int nid){
        //System.out.println("bloque de inst: "+block);
        if(verificarBloque(block)) {
            return findWord(block, word);
        } else {
            traeBloque(block, nid);
            if(cache[16][block%8] == -2){
                return null;
            }
            else{
                return findWord(block, word);
            }
        }
    };
    public boolean verificarBloque(int block){
        return cache[16][block%8]==block;
        
    };
    
    public int[] findWord(int block, int nword){
        int[] word =  new int[4];
        for(int i=0; i<word.length; i++) {
            word[i] = cache[(nword*4)+i][block%8];
        }
        return word;
    };
    
    public void traeBloque(int block, int nid){
        //System.out.println("traje bloque");
        int[] bloque;
        bloque = bus.getBloqueInstr(block, nid);
        if(bloque == null){
                        cache[16][block%8] = -2;
        }
        else{
            for(int i=0; i<bloque.length; i++) {
                cache[i][block%8] = bloque[i];
            }
            cache[16][block%8] = block;
        }
    };
    Bus bus; 
}
