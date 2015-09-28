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
    public Bus(Memoria m){mem = m;};
    
    public synchronized int[] getBloque(int bloque){
        return mem.Read(bloque);
    };
    
    public synchronized void setBloque(int bloque, int res[]){
        //TODO
    };
}
