/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dave
 */
public class Nucleo {
    private double quantum;
    int nid;
    private EstructuraHilo estHilo;
    CacheInstrucciones ci;
    int r[] = new int[32];
    int pc;
    int rl;
    CyclicBarrier barrera;
    private boolean terminado;
    private  AtomicBoolean noFin; 
    
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
        this.barrera = barrera;
        for(int i=0; i<32; i++) {
            r[i] = 0;
        }
        terminado=false;
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
     * @return the estHilo
     */
    public EstructuraHilo getEstHilo() {
        return estHilo;
    }

    /**
     * @param hilo the estHilo to set
     */
    public void setEstHilo(EstructuraHilo hilo) {
        this.estHilo = hilo;
        pc=hilo.getHpc();
        r=hilo.getReg();
    }

    /**
     * @return the terminado
     */
    public boolean isTerminado() {
        return terminado;
    }

    /**
     * @param terminado the terminado to set
     */
    public void setTerminado(boolean terminado) {
        this.terminado = terminado;
    }
    
    /**
     * @return the noFin veme la belleza
     */
    public boolean isNoFin() {
        return noFin.get();
    }

    /**
     * @param noFin the noFin to set
     */
    public void setNoFin(boolean noFin) {
        this.noFin.set(noFin);
    }

    /**
     * Clase que implementa el estHilo para ejecutar las instrucciones paralelamente
     */
    public class HiloCPU extends Thread {
        
        

        /**
         * Constructor that passes the arrays and the shared barrier to the thread.
         *
         */
        public HiloCPU() {
            super();
            noFin = new AtomicBoolean();
            noFin.set(true);
        }

        
        /**
         *
         */
        @Override
        public void run() {
            //System.out.println("tid: "+this.getId());
            int[] instruccion = RequestInst();
            
            int op = instruccion[0];
            int r1 = instruccion[1];
            int r2 = instruccion[2];
            int r3 = instruccion[3];
            //System.out.println(""+op+" "+r1+" "+r2+" "+r3);
            switch (op) {
                case 8:     //DADDI, SUMA CON LITERAL
                    System.out.println(nid+": DADDI r"+r1+" #"+r3+" ->r"+r2);
                    pc += 4;
                    r[r2]=r[r1]+r3;
                    System.out.println("r"+r2+"="+r[r2]);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 32:    //DADD, SUMA CON REGISTRO
                    System.out.println(nid+": DADD r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]+r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 34:    //DSUB, RESTA
                    System.out.println(nid+": DSUB r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]-r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 12:    //DMUL, MULTIPLICACION
                    System.out.println(nid+": DMUL r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]*r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 14:    //DDIV, DIVISION
                    System.out.println(nid+": DDIV r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]/r[r2];
                    System.out.println("r"+r3+"="+r[r3]);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 35:    //LW, LOAD
                    //TODO
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 43:    //SW, STORE
                    //TODO
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 4:     //BEQZ, SALTO CONDICIONAL IGUAL A CERO
                    System.out.println(nid+": BEQZ r"+r1+" ->r"+r3);
                    pc += 4;
                    if(r[r1]==0) {
                        pc += (r3*4);
                    }
                    setNoFin(true);
                    System.out.println("pc: " +pc);
                    guardaHilo();
                    break;
                case 5:     //BNEZ, SALTO CONDICIONAL NO IGUAL
                    System.out.println(nid+": BNEZ r"+r1+" ->r"+r3);
                    pc += 4;
                    if(r[r1]!=0) {
                        pc += (r3*4);
                    }
                    setNoFin(true);
                    System.out.println("pc: " +pc);
                    guardaHilo();
                    break;
                case 3:     //JAL, SALTO A PARTIR DE DONDE VOY
                    System.out.println(nid+": JAL r"+r3+" ->pc: "+pc);
                    pc += 4;
                    r[31]=pc;
                    pc+=(r3);
                    System.out.println("pc: "+pc);
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 2:     //JR, SALTO CON REGISTRO
                    pc += 4;
                    pc=r[r1];
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 11:    //LL, LOAD LINKED
                    //TODO
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 13:    //SC, STORE CONDITIONAL
                    //TODO
                    setNoFin(true);
                    guardaHilo();
                    break;
                case 63:    //FIN, FIN
                    System.out.println(nid+": FIN t");
                    setNoFin(false);
                    guardaHilo();
                    break;
                default:
                    System.err.println(nid+": INSTRUCCION INVALIDA");
                    setNoFin(false);
                    guardaHilo();
                    break;
            }
            
            
            try {
                //System.out.println("barrera wait, tid: "+Thread.currentThread().getId());
                barrera.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        
    }
    
    private HiloCPU thread;
    
    /**
     * Ejecuta la instrucción determinada
     * @return true si no es FIN y false en otro caso
     */
    public boolean Execute(){
        if(!isTerminado()) {
            setThread(new HiloCPU());
            getThread().start();
            try {
                getThread().join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*try {
                Thread.sleep(3);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            return isNoFin();
        } else {
            Thread nop;
            nop = new Thread() {
                @Override
                public void run() {
                    try {
                        System.out.println(nid+": NOP");
                        //System.out.println("barrera wait, tid: "+Thread.currentThread().getId());
                        barrera.await();
                    } catch (InterruptedException | BrokenBarrierException ex) {
                        Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
            };

            nop.start();
            try {
                nop.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
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
        this.setEstHilo(new EstructuraHilo(getEstHilo().getHid(), pc, 0, r));
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
