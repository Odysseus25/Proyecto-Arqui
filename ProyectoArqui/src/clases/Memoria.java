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
public class Memoria {
    int latencia;
    int memInst[]= new int[640];
    int memData[]= new int[1408];
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
