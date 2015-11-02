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
public class CacheDatos {
    int cache[][] = new int[(4*4)][8];
    int etiqueta[] = new int[8];
    int validez[] = new int[8];
    public CacheDatos(Bus b){
        bus = b;
        for(int i=0; i<cache.length-1; i++) {
            for(int j=0; j<cache[i].length; j++) {
                cache[i][j]=0;
            }
        }
        //-1=invalido, 0=compartido, 1=modificado
        for(int j=0; j<etiqueta.length; j++) {
            validez[j]=-1;
        }
    };
    public int[] getWord(int block, int word, int nid){
        //System.out.println("bloque de inst: "+block);
        /*if(verificarBloque(block)) {
            return findWord(block, word);
        } else {
            traeBloque(block, nid);
            if(cache[16][block%8] == -2){
                return null;
            }
            else{
                return findWord(block, word);
            }
        }*/
        return null;
    };
    
    public void setWord(int block, int word, int nid){
        //System.out.println("bloque de inst: "+block);
        /*if(verificarBloque(block)) {
            //return findWord(block, word);
        } else {
            traeBloque(block, nid);
            if(cache[16][block%8] == -2){
                //return null;
            }
            else{
                //return findWord(block, word);
            }
        }*/
    };
    public int verificarBloque(int block){
        if(validez[block%8]==0) {
            if(etiqueta[block%8]==block) {
                return 0;
            }
        } else if(validez[block%8]==1) {
            if(etiqueta[block%8]==block) {
                return 1;
            }
        } else {
            return -1;
        }
        return -1;
        
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
        bloque = bus.getBloque(block, nid);
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
