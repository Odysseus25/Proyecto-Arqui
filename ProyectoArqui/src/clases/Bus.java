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
    public Bus(Memoria m){
        mem = m;
        ocupador = -1;
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
    
    public synchronized void setBloque(int bloque, int res[]){
        //TODO
    };
}
