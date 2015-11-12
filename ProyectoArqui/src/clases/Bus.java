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
    
    public synchronized int[] getBloqueDatos(int bloque, int nid){
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            //TODO: se pide en un ciclo y se usa hasta el otro
            int otron = (nid==1)?2:1;
            if(cachesD[otron-1].getOcupador()==-1 || cachesD[otron-1].getOcupador()==nid) {
                cachesD[otron-1].ocupa(nid);
                char bloqueOtroN = cachesD[otron-1].verificarBloque(bloque);
                if(bloqueOtroN=='M') {
                    int[] guardar =  new int[4*4];
                    for(int i = 0; i<4*4; i++) {
                        guardar[i] = cachesD[otron-1].cache[i][bloque%8];
                    }
                    mem.Write(bloque, guardar, false);
                }
                cachesD[otron-1].libera();
            } else {
                //TODO: verificar deadlock
                cachesD[otron-1].libera();
                System.out.println(cachesD[otron-1].getOcupador());
                return null;
            }
            int[] readblock = mem.Read(bloque,  true);
            if(readblock != null){
                libera();
            }
            return readblock;
        }
        else{
            System.out.println(getOcupador());
            return null;
        }
    };
    
    public synchronized int[] getBloqueInstr(int bloque, int nid){
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            int[] readblock = mem.Read(bloque, true);
            if(readblock != null){
                libera();
            }
            return readblock;
        } else {
            System.out.println(getOcupador());
            return null;
        }
    };
    
    public synchronized boolean setBloque(int bloque, int save[], int nid){
        //TODO: modificar.
        if(getOcupador() == -1 || getOcupador() == nid){
            ocupa(nid);
            boolean res = mem.Write(bloque, save, true);
            if(res){
                libera();
                return true;
            } else {
                //TODO: verificar deadlock
                libera();
                return false;
            }
            
        } else {
            System.out.println(getOcupador());
            return false;
        }
    };
    
    public synchronized boolean invalidar(int block, int nid) {
        if(getOcupador() == -1 || getOcupador() == nid){
            return cachesD[nid-1].invalidar(block, nid);
        } else  {
            System.out.println(getOcupador());
            return false;
        }
        
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
