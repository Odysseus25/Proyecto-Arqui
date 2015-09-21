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
public class Nucleo {
    double quantum;
    int nid;
    CacheInstrucciones ci;
    
    public Nucleo(int id, Bus bus){
        nid = id;
        ci = new CacheInstrucciones(bus);
    };
    public void Execute(){};
    public void RequestInst(){};
    public void Write(){};
    public void Read(){};
    public void SaveState(){};
    
} 
