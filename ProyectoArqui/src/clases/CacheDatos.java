/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author dave
 */
public class CacheDatos {
    Bus bus; 
    int cache[][] = new int[(4*4)][8];
    int etiqueta[] = new int[8];
    char validez[] = new char[8];
    private AtomicBoolean ocupado = new AtomicBoolean(false);
    
    public CacheDatos(Bus b){
        bus = b;
        for(int i=0; i<cache.length-1; i++) {
            for(int j=0; j<cache[i].length; j++) {
                cache[i][j]=0;
            }
        }
        //-1=invalido, 0=compartido, 1=modificado
        for(int j=0; j<etiqueta.length; j++) {
            validez[j]='I';
        }
    };
    
    public int[] getWord(int block, int word, int nid){
        if(!ocupado.get()) {
            switch (verificarBloque(block)) {
                case 'C': //Bloque compartido
                    return findWord(block, word);
                case 'M': //Bloque modificado
                    return findWord(block, word);
                case 'I': //Bloque inválido
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        return findWord(block, word);
                    } else {
                        return null;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    //TODO: guardar bloque (Note que puede devolver falso aquí)
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        return findWord(block, word);
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
    
    public boolean setWord(int block, int word, int nid){
        if(!ocupado.get()) {
            switch (verificarBloque(block)) {
                case 'C': //Bloque compartido
                    //TODO: Invalido y modifico (Note que puede devolver falso si debe esperar para invalidar) 
                    return true;
                case 'M': //Bloque modificado
                    //TODO: Modifico
                    return true;
                case 'I': //Bloque inválido
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        //TODO: Modifico
                        return true;
                    } else {
                        return false;
                    }
                case 'R': //Bloque modificado, pero no es el que necesito
                    //TODO: guardar bloque (Note que puede devolver falso aquí)
                    traeBloque(block, nid);
                    if(validez[block%8]=='C') {
                        //TODO: Invalido y modifico (Note que puede devolver falso si debe esperar para invalidar)
                        return true;
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
    
    public int verificarBloque(int block){
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
    
    public void traeBloque(int block, int nid){
        //System.out.println("traje bloque");
        int[] bloque;
        bloque = bus.getBloque(block, nid);
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
    public boolean getOcupado() {
        return this.ocupado.get();
    }


    public void libera() {
        this.ocupado.set(false);
    }
    

    public void ocupa() {
        this.ocupado.set(true);
    }
    
    public boolean invalidar(int block) {
        if(!getOcupado()) {
            for(int i = 0; i<this.validez.length; i++) {
                if(this.etiqueta[i]==block) {
                    this.validez[i] = 'I';
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
}
