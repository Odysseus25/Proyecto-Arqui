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
public class Bus {
    Memoria mem;
    int ocupador;
    String mensaje;
    CacheDatos[] cachesD = new CacheDatos[2];
    public Bus(Memoria m){
        mem = m;
        ocupador = -1;
    };
    
    public void setCache(CacheDatos cd, int nid) {
        cachesD[nid-1] = cd;
    };
    
    public synchronized int[] getBloque(int bloque, int nid){
        if(ocupador == -1){
            ocupador = nid;
            int[] readblock = mem.Read(bloque);
            if(readblock != null){
                ocupador = -1;
            }
            return readblock;
        }
        else if(ocupador == nid){
            int[] readblock = mem.Read(bloque);
            if(readblock != null){
                ocupador = -1;
            }
            return readblock;
        }
        else{
            return null;
        }
    };
    
    public synchronized boolean setBloque(int bloque, int res[]){
        return false;
        //TODO: 
    };
    
    public synchronized boolean invalidar(int block, int nid) {
        return cachesD[nid-1].invalidar(block);
    };
}
