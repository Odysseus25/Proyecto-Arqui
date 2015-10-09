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
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Ulises
 */
public class ProyectoArqui {
   
    /**
     * @param args the command line arguments
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
            Vector<Integer> res = new Vector<Integer>();
            Vector<Integer> tmp = new Vector<Integer>();
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
        
        //Empezamos a simular!
        int reloj=0;
        while(!colaEjecucion.isEmpty()) {
            EstructuraHilo t = colaEjecucion.poll();
            System.out.println("HID: "+t.getHid()+", HPC: "+t.getHpc());
            n1.setHilo(t);
            int tiempo=0;
            System.out.println(n1);
            while(n1.Execute()) {
                tiempo++;
                if(tiempo>=n1.getQuantum()) {
                    System.out.println("SE ACABÓ EL QUANTUM");
                    n1.guardaHilo();
                    colaEjecucion.add(n1.getHilo());
                    break;
                } 
            }
            System.out.println(n1);
            System.out.println("________________________________");
        }
        
        
    }
    

    
    public static Vector<Integer> leeArchivo(File archivo) {
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader( archivo));
            
            Vector<Integer> vres = new Vector<Integer>();
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
