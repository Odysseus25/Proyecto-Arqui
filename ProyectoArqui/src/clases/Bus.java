/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author dave
 */
public class Bus {
    Memoria mem;
    private AtomicInteger ocupador;
    String mensaje;
    CacheDatos[] cachesD = new CacheDatos[2];
    public Bus(Memoria m){
        mem = m;
        ocupador = new AtomicInteger(-1);
    };
    
    public void setCache(CacheDatos cd, int nid) {
        cachesD[nid-1] = cd;
    };
    
    public synchronized int[] getBloqueDatos(int bloque, int nid, boolean espere){
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            //TODO: se pide en un ciclo y se usa hasta el otro
            int otron = (nid==1)?2:1;
            if(cachesD[otron-1].getOcupador()==-1 || cachesD[otron-1].getOcupador()==nid) {
                cachesD[otron-1].ocupa(nid);
                char bloqueOtroN = cachesD[otron-1].verificarBloque(bloque);
                if(bloqueOtroN=='M') {
                    int[] guardar =  new int[16];
                    for(int i = 0; i<guardar.length; i++) {
                        guardar[i] = cachesD[otron-1].cache[i][bloque%8];
                    }
                    mem.Write(bloque, guardar, false, nid);
                }
                cachesD[otron-1].libera();
            } else {
                //TODO: verificar deadlock
                cachesD[otron-1].libera();
                System.out.println("Ocupa cache dentro de bus: "+cachesD[otron-1].getOcupador());
                return null;
            }
            int[] readblock = mem.Read(bloque,  espere, nid);
            if(readblock != null){
                libera();
            }
            return readblock;
        }
        else{
            System.out.println("Ocupa el bus: "+getOcupador());
            return null;
        }
    };
    
    public synchronized int[] getBloqueInstr(int bloque, int nid){
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            int[] readblock = mem.Read(bloque, true, nid);
            if(readblock != null){
                libera();
            }
            return readblock;
        } else {
            System.out.println(getOcupador());
            return null;
        }
    };
    
    public synchronized boolean setBloque(int bloque, int save[], int nid, boolean espere){
        //TODO: modificar.
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            boolean res = mem.Write(bloque, save, espere, nid);
            if(res){
                libera();
                return true;
            } else {
                //TODO: verificar deadlock
                System.out.println("esperando latencia memoria");
                libera();
                return false;
            }
            
        } else {
            System.out.println("Ocupa el bus: "+getOcupador());
            return false;
        }
    };
    
    public synchronized boolean invalidar(int block, int nid) {
        
        return cachesD[nid-1].invalidar(block, nid);
        
    };

    /**
     * @return the ocupador
     */
    public int getOcupador() {
        return ocupador.get();
    };

    /**
     * @param ocupador the ocupador to set
     */
    public void ocupa(int ocupador) {
        this.ocupador.set(ocupador);
    };
    
    /**
     */
    public void libera() {
        this.ocupador.set(-1);
    };
}
