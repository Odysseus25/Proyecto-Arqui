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
    public int getInstruccion(int dir){
        return 0;
    };
    public boolean verificarBloque(int dir){return false;};
    public int findWord(int block, int word){return 0;};
    Bus bus; 
}
