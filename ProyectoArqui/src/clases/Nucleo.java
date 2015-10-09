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
    private double quantum;
    int nid;
    private EstructuraHilo hilo;
    CacheInstrucciones ci;
    int r[] = new int[32];
    int pc;
    int rl;
    
    /**
     * Constructor
     * @param id
     * @param quantum
     * @param bus
     * @param barrera 
     */
    public Nucleo(int id, double quantum, Bus bus, CyclicBarrier barrera){ // recibe el bus compartido que tiene la memoria compartida
        nid = id;
        this.quantum = quantum;
        ci = new CacheInstrucciones(bus);
        thread = new HiloCPU(barrera);
        for(int i=0; i<32; i++) {
            r[i] = 0;
        }

    };    

    /**
     * @return the quantum
     */
    public double getQuantum() {
        return quantum;
    }

    /**
     * @param quantum the quantum to set
     */
    public void setQuantum(double quantum) {
        this.quantum = quantum;
    }

    /**
     * @return the thread
     */
    public HiloCPU getThread() {
        return thread;
    }

    /**
     * @param thread the thread to set
     */
    public void setThread(HiloCPU thread) {
        this.thread = thread;
    }

    /**
     * @return the hilo
     */
    public EstructuraHilo getHilo() {
        return hilo;
    }

    /**
     * @param hilo the hilo to set
     */
    public void setHilo(EstructuraHilo hilo) {
        this.hilo = hilo;
        pc=hilo.getHpc();
        r=hilo.getReg();
    }

    /**
     * Clase que implementa el hilo para ejecutar las instrucciones paralelamente
     */
    public class HiloCPU extends Thread {
        
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
            //System.out.println(""+op+" "+r1+" "+r2+" "+r3);
            switch (op) {
                case 8:     //DADDI, SUMA CON LITERAL
                    System.out.println("DADDI r"+r1+" #"+r3+" ->r"+r2);
                    pc += 4;
                    r[r2]=r[r1]+r3;
                    System.out.println("r"+r2+"="+r[r2]);
                    setUltimaInstruccion(true);
                    break;
                case 32:    //DADD, SUMA CON REGISTRO
                    System.out.println("DADD r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]+r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setUltimaInstruccion(true);
                    break;
                case 34:    //DSUB, RESTA
                    System.out.println("DSUB r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]-r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setUltimaInstruccion(true);
                    break;
                case 12:    //DMUL, MULTIPLICACION
                    System.out.println("DMUL r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]*r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setUltimaInstruccion(true);
                    break;
                case 14:    //DDIV, DIVISION
                    System.out.println("DDIV r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]/r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
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
                    System.out.println("BEQZ r"+r1+" ->r"+r3);
                    pc += 4;
                    if(r[r1]==0) {
                        pc += (r3*4);
                    }
                    setUltimaInstruccion(true);
                    System.out.println("pc: " +pc);
                    break;
                case 5:     //BNEZ, SALTO CONDICIONAL NO IGUAL
                    System.out.println("BNEZ r"+r1+" ->r"+r3);
                    pc += 4;
                    if(r[r1]!=0) {
                        pc += (r3*4);
                    }
                    setUltimaInstruccion(true);
                    System.out.println("pc: " +pc);
                    break;
                case 3:     //JAL, SALTO A PARTIR DE DONDE VOY
                    System.out.println("JAL r"+r3+" ->pc: "+pc);
                    pc += 4;
                    r[31]=pc;
                    pc+=(r3);
                    System.out.println("pc: "+pc);
                    setUltimaInstruccion(true);
                    break;
                case 2:     //JR, SALTO CON REGISTRO
                    pc += 4;
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
                    System.out.println("FIN t");
                    setUltimaInstruccion(false);
                    break;
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
    
    private HiloCPU thread;
    
    /**
     * Ejecuta la instrucción determinada
     * @return true si no es FIN y false en otro caso
     */
    public boolean Execute(){
        try {
            getThread().run();
            getThread().join();
            return getThread().isUltimaInstruccion();
        } catch (InterruptedException ex) {
            System.err.println("QUE PUTAS");
            Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    };
    
    public int[] RequestInst(){
        int block = pc/16;                          //busco el bloque en el que está la dirección
        int word = pc/4;                            //# de palabra dentro del bloque
        return ci.getInstruccion(block, word%4);    //pide a la cache la instrucción
    };
    
    public void Write(int dir, int[] bloque){};
    public int[] Read(int dir){return (new int[4]);};
    

    
    public void guardaHilo(){
        this.setHilo(new EstructuraHilo(getHilo().getHid(), pc, 0, r));
    }
    
    @Override
    public String toString(){
        String registros = "reg(";
        for(int i=0; i<r.length; i++) {
            registros += i+":" +r[i]+", ";
        }
        registros += ")";
        return registros;
    }
    
} 
