/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author dave
 */
public class CacheDatos {
    int bInv;
    private Nucleo core;
    Bus bus; 
    int cache[][] = new int[(4*4)][8];
    int etiqueta[] = new int[8];
    char validez[] = new char[8];
    private AtomicInteger ocupador = new AtomicInteger(-1);
    
    public CacheDatos(Bus b){
        bus = b;
        for (int[] cache1 : cache) {
            for (int j = 0; j < cache1.length; j++) {
                cache1[j] = 1;
            }
        }
        //'I'=invalido, 'C'=compartido, 'M'=modificado, 'R'=modificado, pero no es el necesitado
        for(int j=0; j<etiqueta.length; j++) {
            validez[j]='I';
        }
        bInv=-1;
    };
    
    //TODO: Liberar recursos si devuelve null;
    /**
     *
     * @param address
     * @param nid
     * @return
     */
     public int[] getWord(int address, int nid){
        int nword = (address/4)%4;
        int block = address/16;
        int oc = getOcupador();
        if(oc==-1 || oc==nid) {
            ocupa(nid);
            switch (verificarBloque(block)) {
                case 'C': //Bloque compartido
                    libera();
                    return findWord(block, nword);
                case 'M': //Bloque modificado
                    libera();
                    return findWord(block, nword);
                case 'I': //Bloque inválido
                    char trae = traeBloque(block, nid);
                    if(trae=='C') {
                        libera();
                        return findWord(block, nword);
                    } else if(trae=='O') {
                        //TODO: verificar deadlock
                        libera();
                        return null;
                    } else if (trae=='E') {
                        return null;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    int[] guardar =  new int[16];
                    for(int i = 0; i<16; i++) {
                        guardar[i] = cache[i][block%8];
                    }
                    //System.out.println("N:"+nid+" (R)le caigo encima (get), guardo: "+etiqueta[block%8]);
                    char resSave = bus.setBloque(etiqueta[block%8], guardar, nid, false);
                    if(resSave=='C'){
                        trae = traeBloque(block, nid);
                        if(trae=='C') {
                            libera();
                            return findWord(block, nword);
                        } else if(trae=='O') {
                            //TODO: verificar deadlock
                            libera();
                            return null;
                        } else if (trae=='E') {
                            return null;
                        }
                    } else if(resSave == 'E') {
                        return null;
                    } else if(resSave== 'O') {
                        libera();
                        return null;
                    }
                default:
                    libera();
                    return null;
            }
        } else {
            //System.out.println("ocupa el bus(get): "+getOcupador());
            return null;
        }
    };
    
    public boolean setWord(int address, int[] word, int nid){
        int nword = ((int)(address/4))%4;
        int block = address/16;
        //System.out.println("direccion: "+address+", palabra: "+nword+", bloque: "+block);
        int oc = getOcupador();
        if(oc==-1 || oc==nid) {
            ocupa(nid);
            int otron = (nid==1)?2:1;
            char verBlock = verificarBloque(block);
            //System.out.println("verblock: "+verBlock);
            switch (verBlock) {
                case 'C': //Bloque compartido
                    boolean resInv = bus.invalidar(block, otron);
                    if(resInv) {
                        findNSetWord(block, nword, word);
                        libera();
                    } else {
                        //TODO: verificar deadlock
                        libera();
                        return false;
                    }
                    return true;
                case 'M': //Bloque modificado
                    findNSetWord(block, nword, word);
                    libera();
                    return true;
                case 'I': //Bloque inválido
                    char trae = traeBloque(block, nid);
                    if(trae=='C') {
                        findNSetWord(block, nword, word);
                        libera();
                        return true;
                    } else if(trae=='O') {
                        //TODO: verificar deadlock
                        libera();
                        return false;
                    } else if (trae=='E') {
                        return false;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    int[] guardar =  new int[4*4];
                    for(int i = 0; i<4*4; i++) {
                        guardar[i] = cache[i][block%8];
                    }
                    char resSave = bus.setBloque(etiqueta[block%8], guardar, nid, false);
                    if(resSave=='C'){
                        trae = traeBloque(block, nid);
                        //System.out.println("N:"+nid+" (R)le caigo encima, guardo: "+etiqueta[block%8]+", valido? "+validez[block%8]);
                        if(trae=='C') {
                            resInv = bus.invalidar(block, otron);
                            if(resInv) {
                                findNSetWord(block, nword, word);
                                libera();
                                return true;
                            } else {
                                //TODO: verificar deadlock
                                libera();
                                return false;
                            }
                        } else if (trae=='O')  {
                            libera();
                            //System.out.println("no guardo bloque (bus busy) o espera latencia (R) "+resSave);
                            return false;
                        } else {
                            return false;
                        }
                    } else if(resSave == 'E'){
                        return false;
                    } else if(resSave == 'O') {
                        libera();
                        return false;
                    }
                default:
                    libera();
                    return false;
            } 
        } else {
            //System.out.println("Ocupado por(set): "+getOcupador());
            return false;
        }
    };
    
    public char verificarBloque(int block){
        if(validez[block%8]=='C') {
            if(etiqueta[block%8]==block) {
                return 'C';
            }
        } else if(validez[block%8]=='M') {
            if(etiqueta[block%8]==block) {
                return 'M';
            } else {
                //TODO: escribir bloque modificado a memoria
                return 'R';
            }
        } else {
            return 'I';
        }
        return 'I';
        
    };
    
    public int[] findWord(int block, int nword){
        int[] word =  new int[4];
        for(int i=0; i<word.length; i++) {
            word[i] = cache[(nword*4)+i][block%8];
        }
        return word;
    };
    
    public void findNSetWord(int block, int nword, int[] word) {
        if(bInv==block) {
            bInv=-1;
            //System.out.println("OK");
        }
        for(int i=0; i<word.length; i++) {
            cache[(nword*4)+i][block%8] = word[i];
        }
        etiqueta[block%8] = block;
        validez[block%8] = 'M';
        //System.out.println(this);
    }
    
    public char traeBloque(int block, int nid){
        //System.out.println("traje bloque");
        int[] bloque;
        bloque = bus.getBloqueDatos(block, nid, true);
        if(bloque != null){
            if(bloque[0]!=-1) {
                //System.out.println("traje bloque: "+block+", data0: "+bloque[0]+", data3: "+bloque[3]);
                for(int i=0; i<bloque.length; i++) {
                    cache[i][block%8] = bloque[i];
                }
                validez[block%8] = 'C';
                etiqueta[block%8] = block;
                return 'C';
            } else {
                return 'O';
            }
        } else {
            return 'E';
        } 
    };

    /**
     * @return the ocupado
     */
    public int getOcupador() {
        return this.ocupador.get();
    }


    public void libera() {
        this.ocupador.set(-1);
    }
    

    public void ocupa(int nid) {
        this.ocupador.set(nid);
    }
    
    public synchronized boolean invalidar(int block, int nid) {
        bInv=block;
        return true;
    }
    
    public void invalida() {
        if(bInv!=-1) {
            for(int i = 0; i<this.validez.length; i++) {
                if(this.etiqueta[i]==bInv) {
                    this.validez[i] = 'I';
                }
            }
            bInv=-1;
            bus.libera();
        }
    }
    
    /*public void escribe() {
        for(int i=0; i<cache.length; i++) {
            if(etiqueta())
        }
    }*/
    
    @Override
    public String toString() {
        String res = "";
        for(int i=0; i<cache.length; i+=4) {
                for(int j=0; j<cache[i].length; j++) {
                    int val = cache[i][j];
                    if(val<0) {
                        res += "-";
                        val *= -1;
                    } else {
                        res += " ";
                    }
                    if(val<10) {
                        res+="  ";
                    } else if(val<100) {
                        res+=" ";
                    }
                    res += val+",";
                }
                res+="\n";
        }
        res+="Etiquetas:\n";
        for(int i=0; i<etiqueta.length; i++) {
            if(etiqueta[i]<10) {
                res+="   ";
            } else if(etiqueta[i]<100) {
                res+="  ";
            } else if(etiqueta[i]<1000) {
                res+=" ";
            }
            res+=etiqueta[i]+",";
        }
        res+="\n";
        for(int i=0; i<etiqueta.length; i++) {
            res+="   ";
            res+=validez[i]+",";
        }
        
        return res;
    }

    /**
     * @return the core
     */
    public Nucleo getCore() {
        return core;
    }

    /**
     * @param core the core to set
     */
    public void setCore(Nucleo core) {
        this.core = core;
    }
    
}
