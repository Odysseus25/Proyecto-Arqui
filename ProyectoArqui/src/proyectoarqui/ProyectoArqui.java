/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoarqui;

import clases.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ulises
 */
public class ProyectoArqui {
   
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //Cargamos Memoria y variables
        ConcurrentLinkedQueue<EstructuraHilo> colaEjecucion = new ConcurrentLinkedQueue<>();
        Memoria mem = new Memoria(2);
        System.out.println("Ingrese el directorio de hilos: ");
        Scanner in = new Scanner(System.in);
        String directorio = in.nextLine();
        File f = new File(directorio);
        
        int hpcActual = 0;
        int hidActual = 0;
        if (f.exists()){
            File[] ficheros = f.listFiles();
            ArrayList<Integer> res = new ArrayList<>();
            ArrayList<Integer> tmp;
            for (File fichero : ficheros) {
                int[] reg= new int[32];
                for(int i = 0; i<reg.length; i++) {
                    reg[i]=0;
                }
                EstructuraHilo hilo = new EstructuraHilo(hidActual, hpcActual, 0, reg);
                tmp = leeArchivo(fichero);
                hidActual++;
                hpcActual+=(tmp.size());
                res.addAll(tmp);
                colaEjecucion.add(hilo);
            }
            if(mem.guardaHilos(res)) {
                
            } else {
                System.err.println("No se pudieron guardar los hilos");
            }
        }
        else {
            System.err.println("Error con el directorio"); 
        }
        
        
        System.out.println(mem);
        System.out.println("largo de la cola: " + colaEjecucion.size());
        
        //Cargamos objetos de simulación
        Bus busInstrucciones = new Bus(mem);
        CyclicBarrier barrera = new CyclicBarrier(1);
        Nucleo n1 = new Nucleo(1, 20, busInstrucciones, barrera);
        Nucleo n2 = new Nucleo(2, 20, busInstrucciones, barrera);
        
        //Empezamos a simular!
        int reloj=0;
        //Booleanos que determinan si N1 y N2 están ejecutando algo
        boolean finN1=false;
        boolean finN2=false;
        //Cargamos los hilos iniciales en N1 y N2
        if(!colaEjecucion.isEmpty()) {
            EstructuraHilo t1 = colaEjecucion.poll();
            n1.setEstHilo(t1);
            n1.setTerminado(false);
        } else {
            n1.setTerminado(true);
        }
        if(!colaEjecucion.isEmpty()) {
            EstructuraHilo t2 = colaEjecucion.poll();
            n2.setEstHilo(t2);
            n2.setTerminado(false);
        } else {
            n2.setTerminado(true);
        }
        System.out.println("HID-N1: "+n1.getEstHilo().getHid()+", HPC-N1: "+n1.getEstHilo().getHpc());
        System.out.println("HID-N2: "+n2.getEstHilo().getHid()+", HPC-N2: "+n2.getEstHilo().getHpc());
        //Tiempo de cada CPU inicia en 0
        int tiempo1=0;
        int tiempo2=0;
        //Si la cola no está vacía o aún se está ejecutando algún hilo, continúe ejecutando
        while(!colaEjecucion.isEmpty()||!(finN1&&finN2)) {
            System.out.println("Reloj: "+reloj);
            
            if(finN1&&finN2) {//Si ambos núcleos terminaron, carguemos hilos en cada uno (de ser posible)
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t1 = colaEjecucion.poll();
                    n1.setEstHilo(t1);
                    n1.setTerminado(false);
                } else {
                    n1.setTerminado(true);
                    System.out.println("cola empty, n1");
                }
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t2 = colaEjecucion.poll();
                    n2.setEstHilo(t2);
                    n2.setTerminado(false);
                } else {
                    n2.setTerminado(true);
                    System.out.println("cola empty, n2");
                }
                System.out.println("HID-N1: "+n1.getEstHilo().getHid()+", HPC-N1: "+n1.getEstHilo().getHpc());
                System.out.println("HID-N2: "+n2.getEstHilo().getHid()+", HPC-N2: "+n2.getEstHilo().getHpc());
                tiempo1=0;
                tiempo2=0;
            } else if(finN1) {//Si solo N1 terminó, cargue solo N1
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t1 = colaEjecucion.poll();
                    n1.setEstHilo(t1);
                    n1.setTerminado(false);
                } else {
                    n1.setTerminado(true);
                }
                System.out.println("HID-N1: "+n1.getEstHilo().getHid()+", HPC-N1: "+n1.getEstHilo().getHpc());
                System.out.println("HID-N2: "+n2.getEstHilo().getHid()+", HPC-N2: "+n2.getEstHilo().getHpc());
                tiempo1=0;
            } else if(finN2) {//Si solo N2 terminó, cargue solo N2
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t2 = colaEjecucion.poll();
                    n2.setEstHilo(t2);
                    n2.setTerminado(false);
                } else {
                    n2.setTerminado(true);
                }
                System.out.println("HID-N1: "+n1.getEstHilo().getHid()+", HPC-N1: "+n1.getEstHilo().getHpc());
                System.out.println("HID-N2: "+n2.getEstHilo().getHid()+", HPC-N2: "+n2.getEstHilo().getHpc());
                tiempo2=0;
            } 
            if(n1.isTerminado()) {//Si el núcleo 1 no tiene nada que cargar, reinicie su tiempo (->no se salga en nop)
                tiempo1=0;
            }
            if(n2.isTerminado()) {//Si el núcleo 2 no tiene nada que cargar, reinicie su tiempo (->no se salga en nop)
                tiempo2=0;
            }
        
            finN1=false;
            finN2=false;
            System.out.println("Nucleo 1: "+n1+"\n"+"Nucleo 2: "+n2);
            while(n1.Execute()&&n2.Execute()) {//Ejecuto instrucción por instrucción
                reloj++;
                tiempo1++;
                tiempo2++;
                if((tiempo1>=n1.getQuantum())&&!n1.isTerminado()) {//Si se acaba el quantum y no se ha terminado, guarde el hilo
                    System.out.println("SE ACABÓ EL QUANTUM DE N1");
                    n1.guardaHilo();
                    colaEjecucion.add(n1.getEstHilo());
                    finN1=true;
                } 
                if(tiempo2>=n2.getQuantum()&&!n2.isTerminado()) {//Si se acaba el quantum y no se ha terminado, guarde el hilo
                    System.out.println("SE ACABÓ EL QUANTUM DE N2");
                    n2.guardaHilo();
                    colaEjecucion.add(n2.getEstHilo());
                    finN2=true;
                } 
                if(finN1||finN2) {//Si se acabó uno de los dos quantums, salga del ciclo
                    break;
                }
            }
            //Determino si debo seguir o no el ciclo externo
            finN1=!n1.isNoFin()||(tiempo1>=n1.getQuantum())||n1.isTerminado();
            finN2=!n2.isNoFin()||(tiempo2>=n2.getQuantum())||n2.isTerminado();
            System.out.println("Nucleo 1: "+n1+"\n"+"Nucleo 2: "+n2);
            System.out.println("________________________________");
        }
        
        
    }
    

    
    public static ArrayList<Integer> leeArchivo(File archivo) {
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader( archivo));
            
            ArrayList<Integer> vres = new ArrayList<>();
            while ((sCurrentLine = br.readLine()) != null) {
                
                String[] nums = sCurrentLine.split(" ");
                int[] inst = new int[4];
                //Ciclo que parsea la linea, para meter la instruccion en un solo entero
                for(int i=0;i<4;i++) {
                    inst[i] = Integer.parseInt(nums[i]);
                    //System.out.print(""+inst[i]+ " ");
                    vres.add(inst[i]);
                }
                
            }
            return vres;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                    if (br != null)br.close();
            } catch (IOException ex) {
                    ex.printStackTrace();
            }
        }
        return null;
    }
    
    
}
