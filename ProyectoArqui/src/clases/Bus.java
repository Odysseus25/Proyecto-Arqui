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
    public Bus(Memoria m){mem = m;};
    public synchronized int[] getBloque(int dir){return (new int[4]);};
    public synchronized void setBloque(int dir, int bloque[]){};
    Memoria mem;
}
