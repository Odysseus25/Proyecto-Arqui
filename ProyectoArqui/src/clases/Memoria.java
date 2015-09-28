/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Vector;

/**
 *
 * @author dave
 */
public class Memoria {
    int latencia;
    int memInst[]= new int[640];
    int memData[]= new int[1408];
    public Memoria(int latencia) {
        this.latencia=latencia;
        for(int i=0; i<memInst.length; i++) {
            memInst[i]=1;
            memData[i]=1;
        }
        for(int i=memInst.length; i<memData.length; i++) {
            memData[i]=1;
        }
    }
    public boolean guardaHilos(Vector<Integer> instrucciones) {
        if(instrucciones.size()<=memInst.length) {
            return false;
        }
        for(int i=0; i<instrucciones.size(); i++) {
            memInst[i]=instrucciones.get(i);
        }
        return true;
    }
    public void Write(int bloque, int res[]){};
    public int[] Read(int bloque){
        //TODO: delay(latencia);
        if(bloque < 160) {
            int[] res = new int[4];
            res[0] = memInst[bloque*4];
            res[1] = memInst[(bloque*4)+1];
            res[2] = memInst[(bloque*4)+2];
            res[3] = memInst[(bloque*4)+3];
            return res;
        } else {
            //TODO RETURN DATA
            return new int[4];
        }
    };
}
