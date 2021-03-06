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
    private int nid;
    private EstructuraHilo estHilo;
    CacheInstrucciones ci;
    private CacheDatos cd;
    int r[] = new int[32];
    int pc;
    public int rl;
    public boolean llact;
    public int bloquell;
    CyclicBarrier barrera;
    private boolean terminado;
    private boolean esperando;
    private  AtomicBoolean fin; 
    
    /**
     * Constructor
     * @param id
     * @param quantum
     * @param busInst
     * @param busData
     * @param barrera 
     */
    public Nucleo(int id, double quantum, Bus busInst, Bus busData, CyclicBarrier barrera){ // recibe el bus compartido que tiene la memoria compartida
        rl=-1;
        nid = id;
        this.quantum = quantum;
        ci = new CacheInstrucciones(busInst);
        cd = new CacheDatos(busData);
        this.barrera = barrera;
        for(int i=0; i<32; i++) {
            r[i] = 0;
        }
        terminado=false;
        esperando = false;
        cd.setCore(this);
        llact = false;
        bloquell=-1;
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
    public boolean isFin() {
        return fin.get();
    }

    /**
     * @param noFin the noFin to set
     */
    public void setFin(boolean noFin) {
        this.fin.set(noFin);
    }

    /**
     * @return the esperando
     */
    public boolean isEsperando() {
        return esperando;
    }

    /**
     * @param esperando the esperando to set
     */
    public void setEsperando(boolean esperando) {
        this.esperando = esperando;
    }

    /**
     * @return the cd
     */
    public CacheDatos getCD() {
        return cd;
    }

    /**
     * @param cd the cd to set
     */
    public void setCD(CacheDatos cd) {
        this.cd = cd;
    }

    /**
     * @return the nid
     */
    public int getNid() {
        return nid;
    }

    /**
     * @param nid the nid to set
     */
    public void setNid(int nid) {
        this.nid = nid;
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
            fin = new AtomicBoolean();
            fin.set(false);
        }
        /**
         *
         */
        @Override
        public void run() {
            //System.out.println("hid: "+getEstHilo().getHid());
            int[] instruccion = RequestInst();
                int op;
                int r1;
                int r2;
                int r3;
                
            if(instruccion == null){
                op = -1;
                r1 = -1;
                r2 = -1;
                r3 = -1;
            }
            else{
                op = instruccion[0];
                r1 = instruccion[1];
                r2 = instruccion[2];
                r3 = instruccion[3];
            }
            

            //System.out.println(""+op+" "+r1+" "+r2+" "+r3);
            switch (op) {
                case 8:     //DADDI, SUMA CON LITERAL
                    //System.out.println("N"+nid+": DADDI r"+r1+" #"+r3+" ->r"+r2);
                    pc += 4;
                    r[r2]=r[r1]+r3;
                    //System.out.println("N"+nid+": r"+r2+"="+r[r2]);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 32:    //DADD, SUMA CON REGISTRO
                   // System.out.println("N"+nid+": DADD r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]+r[r2];
                   // System.out.println("N"+nid+": r"+r3+"="+r[r3]);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 34:    //DSUB, RESTA
                   // System.out.println("N"+nid+": DSUB r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]-r[r2];
                   // System.out.println("N"+nid+": r"+r3+"="+r[r3]);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 12:    //DMUL, MULTIPLICACION
                   // System.out.println("N"+nid+": DMUL r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]*r[r2];
                    //System.out.println("N"+nid+": r"+r3+"="+r[r3]);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 14:    //DDIV, DIVISION
                    //System.out.println("N"+nid+": DDIV r"+r1+" r"+r2+" ->r"+r3);
                    pc += 4;
                    r[r3]=r[r1]/r[r2];
                   // System.out.println("N"+nid+": r"+r3+"="+r[r3]);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 35:    //LW, LOAD r2<-M(r1+r3)
                    //TODO: 
                    int[] word = cd.getWord(r[r1]+r3, nid);
                    if(word!=null) {
                        pc += 4;
                        setEsperando(false);
                        r[r2] = word[0];
                    } else {
                        setEsperando(true);
                        //System.out.println("N" + nid + ": Esperando latencia, LW");
                    }
                    setFin(false);
                    guardaHilo();
                    break;
                case 43:    //SW, STORE M(r1+r3)<-r2
                    //TODO:
                    int[] save = new int[4];
                    save[0] = r[r2];
                    boolean res = cd.setWord(r[r1]+r3, save, nid);
                    if(res) {
                        //System.out.println("sw: guardo: "+save[0]+" en: "+(r[r1]+r3));
                        pc += 4;
                        setEsperando(false);
                        //System.out.println("N" + nid + " STORED BITCH");
                    } else {
                        setEsperando(true);
                        //System.out.println("N" + nid + ": Esperando latencia, SW");
                    }
                    setFin(false);
                    guardaHilo();
                    //System.out.println("Memoria: "+cd.bus.mem);
                    break;
                case 4:     //BEQZ, SALTO CONDICIONAL IGUAL A CERO
                    //System.out.println("N"+nid+": BEQZ r"+r1+"="+r[r1]+" ->r"+r3);
                    pc += 4;
                    if(r[r1]==0) {
                        pc += (r3*4);
                    }
                    setFin(false);
                   // System.out.println("N"+nid+": pc=" +pc);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 5:     //BNEZ, SALTO CONDICIONAL NO IGUAL
                    //System.out.println("N"+nid+": BNEZ r"+r1+"="+r[r1]+" ->r"+r3);
                    pc += 4;
                    if(r[r1]!=0) {
                        pc += (r3*4);
                    }
                    setFin(false);
                   // System.out.println("N"+nid+": pc=" +pc);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 3:     //JAL, SALTO A PARTIR DE DONDE VOY
                    //System.out.println("N"+nid+": JAL r"+r3+" ->pc: "+pc);
                    pc += 4;
                    r[31]=pc;
                    pc+=(r3);
                    //System.out.println("N"+nid+": pc="+pc);
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 2:     //JR, SALTO CON REGISTRO
                    pc += 4;
                    pc=r[r1];
                    setFin(false);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case 50:    //LL, LOAD LINKED
                    //TODO
                    int[] word1 = cd.getWord(r[r1]+r3, nid);
                    if(word1!=null) {
                        pc += 4;
                        setEsperando(false);
                        rl = r[r1]+r3;
                        llact = true;
                        bloquell = (rl/16);
                        r[r2] = word1[0];
                        //System.out.println("N" + nid + " LOAD LINKED BITCH: "+rl+". R"+r2+": "+r[r2]);
                    } else {
                        rl=-1;
                        setEsperando(true);
                        //System.out.println("N" + nid + ": Esperando latencia, LL");
                    }
                    setFin(false);
                    guardaHilo();
                    break;
                case 51:    //SC, STORE CONDITIONAL
                    //TODO
                    if(rl==r[r1]+r3) {
                        int[] save1 = new int[4];
                        save1[0] = r[r2];
                        
                        boolean res1 = cd.setWord(r[r1]+r3, save1, nid);
                        if(res1) {
                            //System.out.println("sc: guardo: "+save1[0]+" en: "+(r[r1]+r3));
                            llact=false;
                            bloquell=-1;
                            pc += 4;
                            setEsperando(false);
                            //System.out.println("N" + nid + " STORED CONDITIONAL BITCH: "+rl+". R"+r2+": "+r[r2]);
                            rl=-1;
                        } else {
                            setEsperando(true);
                            //System.out.println("N" + nid + ": Esperando latencia, SC");
                        }
                    } else {
                        rl=-1;
                        llact=false;
                        bloquell=-1;
                        //System.out.println("N" + nid + " NO STORED CONDITONAL BITCH");
                        r[r2]=0;
                        pc += 4;
                        setEsperando(false);
                    }
                    //rl=-1;
                    setFin(false);
                    guardaHilo();
                    break;
                case 63:    //FIN, FIN
                   // System.out.println("N"+nid+": FIN t");
                    setFin(true);
                    guardaHilo();
                    setEsperando(false);
                    break;
                case -1:
                    //System.out.println("N" + nid + ": Esperando latencia, INST");
                    setFin(false);
                    setEsperando(true);
                    break;
                default:
                    //System.err.println("N"+nid+": INSTRUCCION INVALIDA");
                    setFin(true);
                    guardaHilo();
                    setEsperando(false);
                    break;
            }
            try {//Espero en la barrera para poder sincronizar el reloj.
               // System.out.println("barrera wait, tid: "+Thread.currentThread().getId());
                barrera.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        
    }
    
    private HiloCPU thread;
    
    /**
     * Ejecuta la instrucción determinada
     */
    public void Execute(){
        if(!isTerminado()) {
            //Si tiene un hilo cargado, ejecútelo
            setThread(new HiloCPU());
            getThread().start();
            //return isNoFin();
        } else {
            //En caso contrario ejecute un "NOP" para sincronizar el reloj
            Thread nop;
            nop = new Thread() {
                @Override
                public void run() {
                   // System.out.println("N"+nid+": NOP");
                    setFin(false);
                    try {
                        //System.out.println("barrera wait, tid: "+Thread.currentThread().getId());
                        barrera.await();
                    } catch (InterruptedException | BrokenBarrierException ex) {
                        Logger.getLogger(Nucleo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }  
            };

            nop.start();
            //return true;
        }
    };
    
    public int[] RequestInst(){
        int block = pc/16;                          //busco el bloque en el que está la dirección
        int word = pc/4;                            //# de palabra dentro del bloque
        return ci.getInstruccion(block, word%4, getNid());    //pide a la cache la instrucción
    };
    
    public void Write(int dir, int[] bloque){};
    public int[] Read(int dir){return (new int[4]);};
    

    
    public void guardaHilo(){
        this.setEstHilo(new EstructuraHilo(getEstHilo().getHid(), pc, 0, r));
    }
    
    public void invalidar() {
        if(llact) {
            if(bloquell==cd.bInv) {
                //System.err.println("bll="+bloquell+", bInv="+cd.bInv);
                rl = -1;
                llact=false;
                bloquell=-1;
            }
        }
    
        cd.invalida();
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
