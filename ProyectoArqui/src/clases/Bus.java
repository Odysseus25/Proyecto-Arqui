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
        int oc = getOcupador();
        if(oc == -1 || oc == nid){
            ocupa(nid);
            //TODO: se pide en un ciclo y se usa hasta el otro
            int otron = (nid==1)?2:1;
            int[] save = null;
            int ocC = cachesD[otron-1].getOcupador();
            if(ocC == -1 || ocC == nid){
                cachesD[otron-1].ocupa(nid);
                char bloqueOtroN = cachesD[otron-1].verificarBloque(bloque);
                if(bloqueOtroN=='M') {
                    System.out.println("guardo de la otra cache");
                    int[] guardar =  new int[16];
                    for(int i = 0; i<guardar.length; i++) {
                        guardar[i] = cachesD[otron-1].cache[i][bloque%8];
                    }
                    save = guardar;
                    mem.Write(bloque, guardar, false, nid);
                    cachesD[otron-1].etiqueta[bloque%8] = 'C';
                }
                cachesD[otron-1].libera();
            } else {
                //TODO: verificar deadlock
                cachesD[otron-1].libera();
                libera();
                System.out.println("Ocupa cache dentro de bus: "+cachesD[otron-1].getOcupador());
                return null;
            }
            if(save!=null){
                libera();
                return save;
            }
            int[] readblock = mem.Read(bloque,  espere, nid);
            if(readblock != null){
                libera();
            }
            return readblock;
        }
        else{
            System.out.println("Ocupa el bus: "+getOcupador());
            int[] bOc = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
            return bOc;
        }
    };
    
    public synchronized int[] getBloqueInstr(int bloque, int nid){
        int oc = getOcupador();
        if(oc == -1 || oc == nid){
            ocupa(nid);
            int[] readblock = mem.Read(bloque, true, nid);
            if(readblock != null){
                libera();
            }
            return readblock;
        } else {
            System.out.println("ocupa para inst: "+getOcupador());
            return null;
        }
    };
    
    public synchronized char setBloque(int bloque, int save[], int nid, boolean espere){
        //TODO: modificar.
        int oc = getOcupador();
        if(oc == -1 || oc == nid){
            ocupa(nid);
            boolean res = mem.Write(bloque, save, espere, nid);
            if(res){
                libera();
                return 'C';
            } else {
                //TODO: verificar deadlock
                //System.out.println("esperando latencia memoria para set");
                //libera();
                return 'E';
            }
            
        } else {
            System.out.println("Ocupa el bus: "+getOcupador());
            return 'O';
        }
    };
    
    public synchronized boolean invalidar(int block, int nid) {
        int oc = getOcupador();
        int otron = (nid==1)?2:1;
        if(oc == -1 || oc == nid){
            ocupa(nid);
            boolean res = cachesD[nid-1].invalidar(block, otron);
            //libera();
            return res;
        } else {
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
     * @return 
     */
    public int ocupa(int ocupador) {
        return this.ocupador.getAndSet(ocupador);
    };
    
    /**
     */
    public void libera() {
        this.ocupador.set(-1);
    };
}
