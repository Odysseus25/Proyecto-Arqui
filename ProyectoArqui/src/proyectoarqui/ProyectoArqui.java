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
        String div = "_______________________________________________________________________________";
        //Se inicia programa, pedimos datos
        System.out.println("Bienvenido, ésta es la simulación de la arquitectura MIPS realizada por: ");
        System.out.println("Ulises González - B12989 \n "
                                + " David Solano - B36738 \n");
        System.out.println("¿Desea la simulación en modo lento o modo rápido? l / r");
        Scanner in = new Scanner(System.in);  
        String modo = in.nextLine();
        boolean rapido;
        if(modo.charAt(0) == 'r'||modo.charAt(0)== 'R'){
           rapido = true;
        }
        else{
            rapido = false;
        }
        System.out.println("Ingrese el valor del quantum: ");
        int quantum = Integer.parseInt(in.nextLine());
        
        
        //Cargamos Memoria y colaEjecucion
        ConcurrentLinkedQueue<EstructuraHilo> colaEjecucion = new ConcurrentLinkedQueue<>();
        Memoria mem = cargaMemoria(colaEjecucion);
        //Cargamos objetos de simulación
        Bus busInstrucciones = new Bus(mem);
        Bus busDatos = new Bus(mem);
        CyclicBarrier barrera = new CyclicBarrier(3);
        Nucleo n1 = new Nucleo(1, quantum, busInstrucciones, busDatos, barrera);
        Nucleo n2 = new Nucleo(2, quantum, busInstrucciones, busDatos, barrera);
        busDatos.setCache(n1.getCD(), n1.getNid());
        busDatos.setCache(n2.getCD(), n2.getNid());
        
        //Empezamos a simular!
        int reloj=0;
        //Booleanos que determinan si N1 y N2 están ejecutando algo
        boolean finN1=false;
        boolean finN2=false;
        //Vectores para ir guardando información que se debe desplegar al final de la simulación
        int[] ciclosPorHilo = new int[colaEjecucion.size()];
        for(int i=0; i<ciclosPorHilo.length; i++) {
            ciclosPorHilo[i] = 0;
        }
        EstructuraHilo[] finHilos = new EstructuraHilo[colaEjecucion.size()];
        //Cargamos los hilos iniciales en N1 y N2
        if(!colaEjecucion.isEmpty()) {
            System.out.println("Cargamos hilo de cola en núcleo 1...");
            EstructuraHilo t1 = colaEjecucion.poll();
            n1.setEstHilo(t1);
            n1.setTerminado(false);
        } else {
            n1.setTerminado(true);
        }
        if(!colaEjecucion.isEmpty()) {
            System.out.println("Cargamos hilo de cola en núcleo 2...");
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
            
            System.out.println(div);
            if(finN1&&finN2) {//Si ambos núcleos terminaron, carguemos hilos en cada uno (de ser posible)
                if(!colaEjecucion.isEmpty()) {
                    System.out.println("Cargamos hilo de cola en núcleo 1...");
                    EstructuraHilo t1 = colaEjecucion.poll();
                    n1.setEstHilo(t1);
                    n1.setTerminado(false);
                } else {//Si no hay nada que cargar para N1, márquelo terminado
                    n1.setTerminado(true);
                }
                if(!colaEjecucion.isEmpty()) {
                    System.out.println("Cargamos hilo de cola en núcleo 2...");
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
                    System.out.println("Cargamos hilo de cola en núcleo 1...");
                    EstructuraHilo t1 = colaEjecucion.poll();
                    n1.setEstHilo(t1);
                    n1.setTerminado(false);
                } else {//Si no hay nada que cargar para N1, márquelo terminado
                    n1.setTerminado(true);
                }
                tiempo1=0;
            } else if(finN2) {//Si solo N2 terminó, cargue solo N2
                if(!colaEjecucion.isEmpty()) {
                    System.out.println("Cargamos hilo de cola en núcleo 2...");
                    EstructuraHilo t2 = colaEjecucion.poll();
                    n2.setEstHilo(t2);
                    n2.setTerminado(false);
                } else {//Si no hay nada que cargar para N2, márquelo terminado
                    n2.setTerminado(true);
                }
                tiempo2=0;
            } 
            
            //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
           /* String regN1=(n1.isTerminado())?"No hay hilo para cargar al núcleo":""+n1;
            String regN2=(n2.isTerminado())?"No hay hilo para cargar al núcleo":""+n2;
            System.out.println("Nucleo 1: "+regN1+"\n"+"Nucleo 2: "+regN2);*/
            
            while(true) {//Ejecuto instrucción por instrucción
                System.out.println("Reloj: "+reloj);
                //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
                String n1Hid = (n1.isTerminado())?"No hay hilo para cargar al núcleo":""+n1.getEstHilo().getHid();
                String n2Hid = (n2.isTerminado())?"No hay hilo para cargar al núcleo":""+n2.getEstHilo().getHid();
                String n1Hpc = (n1.isTerminado())?"No hay hilo para cargar al núcleo":""+n1.getEstHilo().getHpc();
                String n2Hpc = (n2.isTerminado())?"No hay hilo para cargar al núcleo":""+n2.getEstHilo().getHpc();
                System.out.println("->ID del hilo ejecutándose en el núcleo 1: ["+n1Hid+"], PC del núcleo 1: ["+n1Hpc+"]");
                System.out.println("->ID del hilo ejecutándose en el núcleo 2: ["+n2Hid+"], PC del núcleo 2: ["+n2Hpc+"]");
                if(!rapido){
                    in.nextLine();
                }
                n1.Execute();
                n2.Execute();
                try {//Espero a que se ejecuten las instrucciones de cada núcleo
                   // System.out.println("barrera wait, mtid: "+Thread.currentThread().getId());
                    barrera.await();
                } catch (BrokenBarrierException ex) {
                    System.err.println("MFT");
                    Logger.getLogger(ProyectoArqui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                finN1=n1.isFin();
                finN2=n2.isFin();
                if(finN1) {
                    finHilos[n1.getEstHilo().getHid()] = n1.getEstHilo();
                    System.out.println("FIN de hilo #"+n1.getEstHilo().getHid());
                }
                if(finN2) {
                    finHilos[n2.getEstHilo().getHid()] = n2.getEstHilo();
                    System.out.println("FIN de hilo #"+n2.getEstHilo().getHid());
                }
                reloj++;
                ciclosPorHilo[n1.getEstHilo().getHid()]++;
                ciclosPorHilo[n2.getEstHilo().getHid()]++;
                if(!n1.isEsperando()){
                    tiempo1++;
                }
                if(!n2.isEsperando()){
                    tiempo2++;
                }
                
                if((tiempo1>=n1.getQuantum())&&!n1.isTerminado()) {//Si se acaba el quantum y no se ha terminado, guarde el hilo
                    System.out.println("Se acabó el quantum de N1");
                    n1.guardaHilo();
                    colaEjecucion.add(n1.getEstHilo());
                    finN1=true;
                } 
                if(tiempo2>=n2.getQuantum()&&!n2.isTerminado()) {//Si se acaba el quantum y no se ha terminado, guarde el hilo
                    System.out.println("Se acabó el quantum de N2");
                    n2.guardaHilo();
                    colaEjecucion.add(n2.getEstHilo());
                    finN2=true;
                } 
                if(finN1||finN2) {//Si se acabó uno de los dos quantums, salga del ciclo
                    break;
                }
            }
            //Determino si debo seguir o no el ciclo externo
            finN1=n1.isFin()||(tiempo1>=n1.getQuantum())||n1.isTerminado();
            finN2=n2.isFin()||(tiempo2>=n2.getQuantum())||n2.isTerminado();
            //Si algún núcleo está terminado (no hay hilo que darle), no muestre su información
            /*regN1=(n1.isTerminado())?"Duerme":""+n1;
            regN2=(n2.isTerminado())?"Duerme":""+n2;
            //Imprimo resultados de vuelta
            System.out.println("Nucleo 1: "+regN1+"\n"+"Nucleo 2: "+regN2);*/
            
        }
        System.out.println(div);
        System.out.println(div);
        System.out.println("RESULTADOS:");
        for(int i=0; i<finHilos.length; i++) {
            System.out.println("---->Hilo #"+i+":");
            System.out.println("-->Ciclos de reloj ocupados: "+ciclosPorHilo[i]);
            System.out.println("-->Registros Finales: "+finHilos[i]);
        }
        
    }
    
    //Carga la memoria pidiendo un directorio y leyendo cada archivo en ese directorio
    public static Memoria cargaMemoria(ConcurrentLinkedQueue<EstructuraHilo> colaEjecucion) {
        Scanner in = new Scanner(System.in);
        System.out.println("Ingrese la latencia del bus: ");
        int latenciaB = Integer.parseInt(in.nextLine());
        System.out.println("Ingrese la latencia de la memoria: ");
        int latenciaM = Integer.parseInt(in.nextLine());
        latenciaM += latenciaB;
        Memoria mem = new Memoria(latenciaM*4);
        System.out.println("Ingrese el directorio de hilos: ");
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
        
        
       // System.out.println(mem);
       // System.out.println("largo de la cola: " + colaEjecucion.size());
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
