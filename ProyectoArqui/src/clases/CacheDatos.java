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
    Bus bus; 
    int cache[][] = new int[(4*4)][8];
    int etiqueta[] = new int[8];
    char validez[] = new char[8];
    private AtomicInteger ocupador = new AtomicInteger(-1);
    
    public CacheDatos(Bus b){
        bus = b;
        for(int i=0; i<cache.length-1; i++) {
            for(int j=0; j<cache[i].length; j++) {
                cache[i][j]=0;
            }
        }
        //'I'=invalido, 'C'=compartido, 'M'=modificado, 'R'=modificado, pero no es el necesitado
        for(int j=0; j<etiqueta.length; j++) {
            validez[j]='I';
        }
    };
    
    //TODO: Liberar recursos si devuelve null;
    public int[] getWord(int block, int nword, int nid){
        if(getOcupador()==-1 || getOcupador()==nid) {
            ocupa(nid);
            switch (verificarBloque(block)) {
                case 'C': //Bloque compartido
                    libera();
                    return findWord(block, nword);
                case 'M': //Bloque modificado
                    libera();
                    return findWord(block, nword);
                case 'I': //Bloque inválido
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        libera();
                        return findWord(block, nword);
                    } else {
                        //TODO: verificar deadlock
                        return null;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    int[] guardar =  new int[4*4];
                    for(int i = 0; i<4*4; i++) {
                        guardar[i] = cache[i][block%8];
                    }
                    boolean resSave = bus.setBloque(etiqueta[block%8], guardar, nid);
                    traeBloque(block, nid);
                    if(validez[block%8]=='C' && resSave) {
                        libera();
                        return findWord(block, nword);
                    } else {
                        return null;
                    }
                default:
                    return null;
            }
        } else {
            return null;
        }
    };
    
    public boolean setWord(int block, int nword, int[] word, int nid){
        if(getOcupador()==-1 || getOcupador()==nid) {
            ocupa(nid);
            int otron = (nid==1)?2:1;
            switch (verificarBloque(block)) {
                case 'C': //Bloque compartido
                    boolean resInv = bus.invalidar(block, otron);
                    if(resInv) {
                        libera();
                        findNSetWord(block, nword, word);
                    } else {
                        //TODO: verificar deadlock
                        return false;
                    }
                    
                    return true;
                case 'M': //Bloque modificado
                    findNSetWord(block, nword, word);
                    libera();
                    return true;
                case 'I': //Bloque inválido
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        findNSetWord(block, nword, word);
                        libera();
                        return true;
                    } else {
                        //TODO: verificar deadlock
                        return false;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    int[] guardar =  new int[4*4];
                    for(int i = 0; i<4*4; i++) {
                        guardar[i] = cache[i][block%8];
                    }
                    boolean resSave = bus.setBloque(etiqueta[block%8], guardar, nid);
                    traeBloque(block, nid);
                    if(validez[block%8]=='C' && resSave) {
                        resInv = bus.invalidar(block, otron);
                        if(resInv) {
                            findNSetWord(block, nword, word);
                            libera();
                            return true;
                        } else {
                            //TODO: verificar deadlock
                            return false;
                        }
                    } else {
                        return false;
                    }
                default:
                    return false;
            } 
        } else {
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
        for(int i=0; i<word.length; i++) {
            cache[(nword*4)+i][block%8] = word[i];
        }
        etiqueta[block%8] = block;
        validez[block%8] = 'M';
    }
    
    public void traeBloque(int block, int nid){
        //System.out.println("traje bloque");
        int[] bloque;
        bloque = bus.getBloqueDatos(block, nid);
        if(bloque == null){
            validez[block%8] = 'I';
        } else {
            for(int i=0; i<bloque.length; i++) {
                cache[i][block%8] = bloque[i];
            }
            validez[block%8] = 'C';
            etiqueta[block%8] = block;
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
    
    public boolean invalidar(int block, int nid) {
        if(getOcupador()==-1) {
            ocupa(nid);
            for(int i = 0; i<this.validez.length; i++) {
                if(this.etiqueta[i]==block) {
                    this.validez[i] = 'I';
                }
            }
            libera();
            return true;
        } else {
            return false;
        }
    }
    
}
