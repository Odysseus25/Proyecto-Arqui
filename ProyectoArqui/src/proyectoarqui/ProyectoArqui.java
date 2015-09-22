/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoarqui;

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
        int num = 32783;
        int m1 =  61440;
        int res1 = (num&m1);
        int res2 = res1>>(4*3);
        System.out.println(res1+", "+res2);
    
    }
    
}
