/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author dave
 */
public class Hilo {
        int hid;
        int hpc;
        private int estado;
        int reg[] = new int[32];
        public Hilo(int hid, int hpc, int estado, int[] reg){
            this.hid=hid;
            this.hpc=hpc;
            this.estado=estado;
            this.reg=reg;
        };

        /**
         * @return the estado
         */
        public int getEstado() {
            return estado;
        }

        /**
         * @param estado the estado to set
         */
        public void setEstado(int estado) {
            this.estado = estado;
        }
    }    
