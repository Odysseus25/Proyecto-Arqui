/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dave
 */
public class Nucleo {
    double quantum;
    int nid;
    EstructuraHilo hilo;
    CacheInstrucciones ci;
    int r[] = new int[32];
    int pc;
    int rl;
    
    public class HiloCPU implements Runnable {
        
        CyclicBarrier barrera;
        private boolean ultimaInstruccion; 
        /**
         * Constructor that passes the arrays and the shared barrier to the thread.
         *
         * @param barrera
         */
        public HiloCPU(CyclicBarrier barrera) {
            super();
            this.barrera = barrera;
            ultimaInstruccion = true;
        }
        
        /**
         *
         */
        @Override
        public void run() {
            int[] instruccion = RequestInst();
            
            int op = instruccion[0];
            int r1 = instruccion[1];
            int r2 = instruccion[2];
            int r3 = instruccion[3];
            switch (op) {
                case 8:     //DADDI, SUMA CON LITERAL
                    r[r2]=r[r1]+r3;
                    setUltimaInstruccion(true);
                    break;
                case 32:    //DADD, SUMA CON REGISTRO
                    r[r3]=r[r1]+r[r2];
                    setUltimaInstruccion(true);
                    break;
                case 34:    //DSUB, RESTA
                    r[r3]=r[r1]-r[r2];
                    setUltimaInstruccion(true);
                    break;
                case 12:    //DMUL, MULTIPLICACION
                    r[r3]=r[r1]*r[r2];
                    setUltimaInstruccion(true);
                    break;
                case 14:    //DDIV, DIVISION
                    r[r3]=r[r1]/r[r2];
                    setUltimaInstruccion(true);
                    break;
                case 35:    //LW, LOAD
                    //TODO
                    setUltimaInstruccion(true);
                    break;
                case 43:    //SW, STORE
                    //TODO
                    setUltimaInstruccion(true);
                    break;
                case 4:     //BEQZ, SALTO CONDICIONAL IGUAL A CERO
                    if(r[r1]==0) {
                        pc = r3;
                    }
                    setUltimaInstruccion(true);
                    break;
                case 5:     //BNEZ, SALTO CONDICIONAL NO IGUAL
                    if(r[r1]!=0) {
                        pc = r3;
                    }
                    setUltimaInstruccion(true);
                    break;
                case 3:     //JAL, SALTO A PARTIR DE DONDE VOY
                    r[31]=pc;
                    pc+=r3;
                    setUltimaInstruccion(true);
                    break;
                case 2:     //JR, SALTO CON REGISTRO
                    pc=r[r1];
                    setUltimaInstruccion(true);
                    break;
                case 11:    //LL, LOAD LINKED
                    //TODO
                    setUltimaInstruccion(true);
                    break;
                case 13:    //SC, STORE CONDITIONAL
                    //TODO
                    setUltimaInstruccion(false);
                    break;
                case 63:    //FIN, FIN
                    setUltimaInstruccion(false);
                default:
                    System.err.println("INSTRUCCION INVALIDA");
                    setUltimaInstruccion(false);
                    break;
            }
            
            try {
                barrera.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * @return the ultimaInstruccion veme la belleza
         */
        public boolean isUltimaInstruccion() {
            return ultimaInstruccion;
        }

        /**
         * @param ultimaInstruccion the ultimaInstruccion to set
         */
        public void setUltimaInstruccion(boolean ultimaInstruccion) {
            this.ultimaInstruccion = ultimaInstruccion;
        }
        
        
    }
    
    HiloCPU thread;
    
    public Nucleo(int id, Bus bus, CyclicBarrier barrera){ // recibe el bus compartido que tiene la memoria compartida
        nid = id;
        ci = new CacheInstrucciones(bus);
        thread = new HiloCPU(barrera);
        for(int i=0; i<32; i++) {
            r[i] = 0;
        }
            
    };
    public boolean Execute(){
        thread.run();
        return thread.isUltimaInstruccion();
    };
    
    public int[] RequestInst(){
        int block = pc/16; //busco el bloque en el que está la dirección
        int word = pc/4; //# de palabra dentro del bloque
        return ci.getInstruccion(block, word%4); //pide a la cache la instrucción
    };
    
    public void Write(int dir, int[] bloque){};
    public int[] Read(int dir){return (new int[4]);};
    
    public EstructuraHilo getHilo(){
        return hilo;
    };
    
    public void setHilo(EstructuraHilo h){
        this.hilo=h;
    };
    
    public void guardaHilo(){
        this.hilo = new EstructuraHilo(hilo.getHid(), pc, 0, r);
    }
    
} 
