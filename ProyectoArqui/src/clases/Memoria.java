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
public class Memoria {
    int latencia;
    int memInst[]= new int[640];
    int memData[]= new int[1408];
    public void Write(int dir, int bloque[]){};
    public int[] Read(int dir){return new int[4];};
}
