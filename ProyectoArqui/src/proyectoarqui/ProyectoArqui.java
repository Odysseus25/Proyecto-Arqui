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
import java.util.Vector;

/**
 *
 * @author Ulises
 */
public class ProyectoArqui {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Memoria mem = new Memoria(2);
        mem.guardaHilos(cargaHilos(args[0]));
    }
    
    public static Vector<Integer> cargaHilos(String directorio) {
        File f = new File(directorio);
        if (f.exists()){
            File[] ficheros = f.listFiles();
            Vector<Integer> res = new Vector<Integer>();
            Vector<Integer> tmp = new Vector<Integer>();
            for (File fichero : ficheros) {
                tmp = leeArchivo(fichero);
                res.addAll(tmp);
            }
            return res;
        }
        else {
            return null; 
        }

    }
    
    public static Vector<Integer> leeArchivo(File archivo) {
        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(archivo));
            
            Vector<Integer> vres = new Vector<Integer>();
            while ((sCurrentLine = br.readLine()) != null) {
                String[] nums = sCurrentLine.split(" ");
                int[] inst = new int[4];
                int res = 65535;
                for(int i=0;i<4;i++) {
                    inst[i] = Integer.parseInt(nums[i]);
                    inst[i] = inst[i] << (4*(3-i));
                    res = res & inst[i];
                }
                vres.add(res);
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
