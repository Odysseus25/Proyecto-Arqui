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
        private int hid;
        private int hpc;
        private int estado;
        private int reg[] = new int[32];
        public EstructuraHilo(int hid, int hpc, int estado, int[] reg){
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

    /**
     * @return the hid
     */
    public int getHid() {
        return hid;
    }

    /**
     * @param hid the hid to set
     */
    public void setHid(int hid) {
        this.hid = hid;
    }

    /**
     * @return the hpc
     */
    public int getHpc() {
        return hpc;
    }

    /**
     * @param hpc the hpc to set
     */
    public void setHpc(int hpc) {
        this.hpc = hpc;
    }

    /**
     * @return the reg
     */
    public int[] getReg() {
        return reg;
    }

    /**
     * @param reg the reg to set
     */
    public void setReg(int[] reg) {
        this.reg = reg;
    }
    @Override
    public String toString(){
        String registros = "reg(";
        for(int i=0; i<reg.length-1; i++) {
            registros += i+":" +reg[i]+", ";
        }
        registros += reg[reg.length-1];
        registros += ")";
        return registros;
    }
}    
