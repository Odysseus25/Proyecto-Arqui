/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dave
 */
public class Memoria {
    private AtomicInteger latenciaM;
    private AtomicInteger[] tiempoLatencia;
    public  int memInst[]= new int[160*4];
    int memData[]= new int[352*4];
    public Memoria(int latencia) {
        latenciaM= new AtomicInteger(latencia);
        tiempoLatencia = new AtomicInteger[2];
        tiempoLatencia[0] = new AtomicInteger(0);
        tiempoLatencia[1] = new AtomicInteger(0);
        for(int i=0; i<memInst.length; i++) {
            memInst[i]=1;
            memData[i]=1;
        }
        for(int i=memInst.length; i<memData.length; i++) {
            memData[i]=1;
        }
    }
    public boolean guardaHilos(ArrayList<Integer> instrucciones) {
        if(instrucciones.size()>=memInst.length) {
            return false;
        }
        for(int i=0; i<instrucciones.size(); i++) {
            memInst[i]=instrucciones.get(i);
        }
        return true;
    }
    
    public boolean Write(int bloque, int res[], boolean espere, int nid){
        //System.out.println("guardando1: "+bloque+", datos: ("+res[0]+","+res[4]+","+res[8]+","+res[12]+"), size: "+res.length);
        if((getTiempoLatencia(nid) < getLatenciaM()) && espere){
            setTiempoLatencia((getTiempoLatencia(nid) + 1), nid);
            //System.out.println("write: nucleo: "+nid +", latencia esperada: "+getTiempoLatencia(nid)+", latencia total: "+getLatenciaM() );
            //System.out.println("guardando3: "+bloque+", datos: "+res[0]+","+res[4]+", size: "+res.length);
            return false;
        } else {
            if(espere) {
                setTiempoLatencia(0, nid);
            }
            
            if(bloque < 40) {
                System.arraycopy(res, 0, memInst, bloque*16, res.length);
                return true;
            } else {
                //System.out.println("guardando: "+bloque+", datos: ("+res[0]+","+res[4]+","+res[8]+","+res[12]+"), size: "+res.length);
                System.arraycopy(res, 0, memData, bloque*16-(memInst.length), res.length);
                return true;
            }
        }
    };
    
    public int[] Read(int bloque, boolean espere, int nid){
       
        if((getTiempoLatencia(nid) < getLatenciaM()) && espere){
            //System.out.println("read: nucleo: "+nid +", latencia esperada: "+getTiempoLatencia(nid)+", latencia total: "+getLatenciaM() );
            int tiempo = getTiempoLatencia(nid); tiempo++;
            setTiempoLatencia(tiempo, nid);
            return null;
        }
        else{
            if(espere) {
                setTiempoLatencia(0, nid);
            }
            int[] res = new int[4*4];
            if(bloque < 40) {
                for(int i=0; i<16; i++) {
                    res[i] = memInst[(bloque*16)+i];
                }
                return res;
            } else {
                System.arraycopy(memData, bloque*16-(memInst.length), res, 0, res.length);
                return res;
            }
        }

    };
    
    @Override
    public String toString(){
        String res = "";
        /*for(int i=0; i<memInst.length; i++) {
            res+=memInst[i]+",";
        }
        res+=" \n ";*/
        for(int i=0; i<memData.length; i++) {
            
            if(i%16 == 0) {
                res+="Bloque "+(i/16+40) + ": ";
            }
            if(i%4 == 0) {
                res+=memData[i]+",";
            }
            if(i%16 == 15) {
                res+="\n";
            }
        }
        return res;
    }


    /**
     * @param latenciaM the latenciaM to set
     */
    public void setLatenciaM(int latenciaM) {
        this.latenciaM.set(latenciaM);
    }

    /**
     * @return 
     */
    public int getLatenciaM() {
        return this.latenciaM.get();
    }

    /**
     * @return the tiempoLatencia
     */
    public int getTiempoLatencia(int nid) {
        return tiempoLatencia[nid-1].get();
    }

    /**
     * @param tiempoLatencia the tiempoLatencia to set
     */
    public void setTiempoLatencia(int tiempoLatencia, int nid) {
        this.tiempoLatencia[nid-1].set(tiempoLatencia);
    }

}
