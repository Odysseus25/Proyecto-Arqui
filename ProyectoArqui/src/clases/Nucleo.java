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
        /**
         * Constructor that passes the arrays and the shared barrier to the thread.
         *
         * @param barrera
         */
        public HiloCPU(CyclicBarrier barrera) {
            super();
            this.barrera = barrera;
        }

        /**
         *
         */
        @Override
        public void run() {
            Execute();
            try {
                barrera.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BrokenBarrierException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }
    
    HiloCPU thread;
    
    public Nucleo(int id, Bus bus, CyclicBarrier barrera){ // recibe el bus compartido que tiene la memoria compartida
        nid = id;
        ci = new CacheInstrucciones(bus);
        thread = new HiloCPU(barrera);
    };
    public boolean Execute(){
        int instruccion = RequestInst();
        // máscaras para obtener los datos de la instrucción
        int maskb1 = 61440; 
        int maskb2 = 3840;
        int maskb3 = 240;
        int maskb4 = 15;
        
        int op = (instruccion&maskb1)>>(4*3);
        int r1 = (instruccion&maskb2)>>(4*2);
        int r2 = (instruccion&maskb2)>>(4*1);
        int r3 = (instruccion&maskb2);
        switch (op) {
            case 8:     //DADDI, SUMA CON LITERAL
                r[r2]=r[r1]+r3;
                break;
            case 32:    //DADD, SUMA CON REGISTRO
                r[r3]=r[r1]+r[r2];
                break;
            case 34:    //DSUB, RESTA
                r[r3]=r[r1]-r[r2];
                break;
            case 12:    //DMUL, MULTIPLICACION
                r[r3]=r[r1]*r[r2];
                break;
            case 14:    //DDIV, DIVISION
                r[r3]=r[r1]/r[r2];
                break;
            case 35:    //LW, LOAD
                //TODO
                break;
            case 43:    //SW, STORE
                //TODO
                break;
            case 4:     //BEQZ, SALTO CONDICIONAL IGUAL A CERO
                if(r[r1]==0) {
                    pc = r3;
                }
                break;
            case 5:     //BNEZ, SALTO CONDICIONAL NO IGUAL
                if(r[r1]!=0) {
                    pc = r3;
                }
                break;
            case 3:     //JAL, SALTO A PARTIR DE DONDE VOY
                r[31]=pc;
                pc+=r3;
                break;
            case 2:     //JR, SALTO CON REGISTRO
                pc=r[r1];
                break;
            case 11:    //LL, LOAD LINKED
                //TODO
                break;
            case 13:    //SC, STORE CONDITIONAL
                //TODO
                break;
            case 63:    //FIN, FIN
                return false;                                        
            default:
                System.err.println("INSTRUCCION INVALIDA");
                break;
        }
        return true;
    };
    
    public int RequestInst(){
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
    
    
    
} 
