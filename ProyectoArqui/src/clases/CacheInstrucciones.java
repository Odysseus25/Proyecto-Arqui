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
    static class Hilo {
        int hid;
        int hpc;
        int estado;
        int registros[] = new int[32];
    }
    int cache[][] = new int[5][8];
    public CacheInstrucciones(Bus b){bus = b;};
    public void getInstruccion(){};
    public void verificarBloque(){};
    public int findWord(){return 0;};
    Bus bus; 
}
