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
        //Cargamos Memoria y colaEjecucion
        ConcurrentLinkedQueue<EstructuraHilo> colaEjecucion = new ConcurrentLinkedQueue<>();
        Memoria mem = cargaMemoria(colaEjecucion);
        
        //Cargamos objetos de simulación
        Bus busInstrucciones = new Bus(mem);
        CyclicBarrier barrera = new CyclicBarrier(3);
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
                } else {//Si no hay nada que cargar para N1, márquelo terminado
                    n1.setTerminado(true);
                }
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t2 = colaEjecucion.poll();
                    n2.setEstHilo(t2);
                    n2.setTerminado(false);
                } else {//Si no hay nada que cargar para N2, márquelo terminado
                    n2.setTerminado(true);
                }
                tiempo1=0;
                tiempo2=0;
            } else if(finN1) {//Si solo N1 terminó, cargue solo N1
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t1 = colaEjecucion.poll();
                    n1.setEstHilo(t1);
                    n1.setTerminado(false);
                } else {//Si no hay nada que cargar para N1, márquelo terminado
                    n1.setTerminado(true);
                }
                tiempo1=0;
            } else if(finN2) {//Si solo N2 terminó, cargue solo N2
                if(!colaEjecucion.isEmpty()) {
                    EstructuraHilo t2 = colaEjecucion.poll();
                    n2.setEstHilo(t2);
                    n2.setTerminado(false);
                } else {//Si no hay nada que cargar para N2, márquelo terminado
                    n2.setTerminado(true);
                }
                tiempo2=0;
            } 
            
            //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
            String n1Hid = (n1.isTerminado())?"Duerme":""+n1.getEstHilo().getHid();
            String n2Hid = (n2.isTerminado())?"Duerme":""+n2.getEstHilo().getHid();
            String n1Hpc = (n1.isTerminado())?"Duerme":""+n1.getEstHilo().getHpc();
            String n2Hpc = (n2.isTerminado())?"Duerme":""+n2.getEstHilo().getHpc();
            System.out.println("HID-N1: "+n1Hid+", HPC-N1: "+n1Hpc);
            System.out.println("HID-N2: "+n2Hid+", HPC-N2: "+n2Hpc);
        
            //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
            String regN1=(n1.isTerminado())?"Duerme":""+n1;
            String regN2=(n2.isTerminado())?"Duerme":""+n2;
            System.out.println("Nucleo 1: "+regN1+"\n"+"Nucleo 2: "+regN2);
            
            while(true) {//Ejecuto instrucción por instrucción
                n1.Execute();
                n2.Execute();
                try {
                    System.out.println("barrera wait, mtid: "+Thread.currentThread().getId());
                    barrera.await();
                    
                } catch (BrokenBarrierException ex) {
                    System.err.println("MFT");
                    Logger.getLogger(ProyectoArqui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                finN1=!n1.isNoFin();
                finN2=!n2.isNoFin();
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
            //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
            regN1=(n1.isTerminado())?"Duerme":""+n1;
            regN2=(n2.isTerminado())?"Duerme":""+n2;
            //Imprimo resultados de vuelta
            System.out.println("Nucleo 1: "+regN1+"\n"+"Nucleo 2: "+regN2);
            System.out.println("________________________________");
        }
        
        
    }
    
    //Carga la memoria pidiendo un directorio y leyendo cada archivo en ese directorio
    public static Memoria cargaMemoria(ConcurrentLinkedQueue<EstructuraHilo> colaEjecucion) {
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
        return mem;
    }
    
    //Lee cada archivo de hilo para meterlo en la memoria
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
