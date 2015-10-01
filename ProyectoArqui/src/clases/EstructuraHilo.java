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
public class EstructuraHilo {
        int hid;
        int hpc;
        private int estado;
        int reg[] = new int[32];
        public EstructuraHilo(int hid, int hpc, int estado, int[] reg){
            this.hid=hid;
            this.hpc=hpc;
            this.estado=estado;
            this.reg=reg;
            for(int i=0; i<32; i++) {
                reg[i] = 0;
            }
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
