/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.ArrayList;

/**
 * @author dave
 */
public class Memoria {
    int latencia;
    public  int memInst[]= new int[640*4];
    int memData[]= new int[1408*4];
    public Memoria(int latencia) {
        this.latencia=latencia;
        for(int i=0; i<memInst.length; i++) {
            memInst[i]=1;
            memData[i]=1;
        }
        for(int i=memInst.length; i<memData.length; i++) {
            memData[i]=1;
        }
    }
    public boolean guardaHilos(ArrayList<Integer> instrucciones) {
        if(instrucciones.size()>=memInst.length) {
            return false;
        }
        for(int i=0; i<instrucciones.size(); i++) {
            memInst[i]=instrucciones.get(i);
        }
        return true;
    }
    public void Write(int bloque, int res[]){};
    public int[] Read(int bloque){
        //TODO: latencia
        if(bloque < 160) {
            int[] res = new int[4*4];
            for(int i=0; i<16; i++) {
                res[i] = memInst[(bloque*16)+i];
            }
            return res;
        } else {
            //TODO RETURN DATA
            return new int[4*4];
        }
    };
    
    @Override
    public String toString(){
        
        return "(" + (memInst[0]) + ", " + (memInst[1]) + ", " + (memInst[2]) + ", " + (memInst[3]) + 
                ", " + (memInst[4]) + ", " + (memInst[5]) + ", " + (memInst[6]) + ", " + (memInst[7]) +               
                ", " + (memInst[8]) + ", " + (memInst[9]) + ", " + (memInst[10]) + ", " + (memInst[11]) +               
                ", " + (memInst[12]) + ", " + (memInst[13]) + ", " + (memInst[14]) + ", " + (memInst[15]) + 
                ", " + (memInst[16]) + ", " + (memInst[17]) + ", " + (memInst[18]) + ", " + (memInst[19]) +
                ", " + (memInst[20]) + ", " + (memInst[21]) + ", " + (memInst[22]) + ", " + (memInst[23]) +               
                ", " + (memInst[24]) + ", " + (memInst[25]) + ", " + (memInst[26]) + ", " + (memInst[27]) +               
                ", " + (memInst[28]) + ", " + (memInst[29]) + ", " + (memInst[30]) + ", " + (memInst[31]) + 
                ", " + (memInst[32]) + ", " + (memInst[33]) + ", " + (memInst[34]) + ", " + (memInst[35]) +
                ", " + (memInst[36]) + ", " + (memInst[37]) + ", " + (memInst[38]) + ", " + (memInst[39]) +               
                ", " + (memInst[40]) + ", " + (memInst[41]) + ", " + (memInst[42]) + ", " + (memInst[43]) +               
                ", " + (memInst[44]) + ", " + (memInst[45]) + ", " + (memInst[46]) + ", " + (memInst[47]) + 
                ", " + (memInst[48]) + ", " + (memInst[49]) + ", " + (memInst[50]) + ", " + (memInst[51]) +
                ", " + (memInst[52]) + ", " + (memInst[53]) + ", " + (memInst[54]) + ", " + (memInst[55]) +               
                ", " + (memInst[56]) + ", " + (memInst[57]) + ", " + (memInst[58]) + ", " + (memInst[59]) +               
                ", " + (memInst[60]) + ", " + (memInst[61]) + ", " + (memInst[62]) + ", " + (memInst[63]) + 
                ", " + (memInst[64]) + ", " + (memInst[65]) + ", " + (memInst[66]) + ", " + (memInst[67]) +
                ", " + (memInst[68]) + ", " + (memInst[69]) + ", " + (memInst[70]) + ", " + (memInst[71]) +               
                ", " + (memInst[72]) + ", " + (memInst[73]) + ", " + (memInst[74]) + ", " + (memInst[75]) +               
                ", " + (memInst[76]) + ", " + (memInst[77]) + ", " + (memInst[78]) + ", " + (memInst[79]) + 
                ", " + (memInst[80]) + ", " + (memInst[81]) + ", " + (memInst[82]) + ", " + (memInst[83]) +
                ", " + (memInst[84]) + ", " + (memInst[85]) + ", " + (memInst[86]) + ", " + (memInst[87]) +               
                ", " + (memInst[88]) + ", " + (memInst[89]) + ", " + (memInst[90]) + ", " + (memInst[91]) +               
                ", " + (memInst[92]) + ", " + (memInst[93]) + ", " + (memInst[94]) + ", " + (memInst[95]) + 
                ", " + (memInst[96]) + ", " + (memInst[97]) + ", " + (memInst[98]) + ", " + (memInst[99]) +
                ", " + (memInst[100]) + ", " + (memInst[101]) + ", " + (memInst[102]) + ", " + (memInst[103]) +               
                ", " + (memInst[104]) + ", " + (memInst[105]) + ", " + (memInst[106]) + ", " + (memInst[107]) +               
                ", " + (memInst[108]) + ", " + (memInst[109]) + ", " + (memInst[110]) + ", " + (memInst[111]) + 
                ", " + (memInst[112]) + ", " + (memInst[113]) + ", " + (memInst[114]) + ", " + (memInst[115]) +
                ", " + (memInst[116]) + ", " + (memInst[117]) + ", " + (memInst[118]) + ", " + (memInst[119]) +               
                ", " + (memInst[120]) + ", " + (memInst[121]) + ", " + (memInst[122]) + ", " + (memInst[123]) +               
                ", " + (memInst[124]) + ", " + (memInst[125]) + ", " + (memInst[126]) + ", " + (memInst[127]) + 
                ", " + (memInst[128]) + ", " + (memInst[129]) + ", " + (memInst[130]) + ", " + (memInst[131]) +
                ")";
    }
}
