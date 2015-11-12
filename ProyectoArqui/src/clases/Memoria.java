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
    private int latenciaM;
    public int tiempoLatencia;
    public  int memInst[]= new int[640*4];
    int memData[]= new int[1408*4];
    public Memoria(int latencia) {
        this.latenciaM=latencia;
        this.tiempoLatencia = 0;
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
    
    public boolean Write(int bloque, int res[], boolean espere){
        if((tiempoLatencia < latenciaM) && espere){
            tiempoLatencia++;
            return false;
        }
        else{
            tiempoLatencia = 0;
            if(bloque < 160) {
                return false;
            } else {
                System.arraycopy(res, 0, memData, bloque*16-(memInst.length), res.length);
                return true;
            }
        }
    };
    
    public int[] Read(int bloque, boolean espere){
        if((tiempoLatencia < latenciaM) && espere){
            tiempoLatencia++;
            return null;
        }
        else{
            tiempoLatencia = 0;
            int[] res = new int[4*4];
            if(bloque < 160) {
                for(int i=0; i<16; i++) {
                    res[i] = memInst[(bloque*16)+i];
                }
                return res;
            } else {
                System.arraycopy(memData, bloque*16-(memInst.length), res, 0, res.length);
                return res;
            }
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
                ")\n" +
                "(" + (memData[768]) + ", " + (memData[769]) + ", " + (memData[770]) + ", " + (memData[771]) + 
                ", " + (memData[772]) + ", " + (memData[773]) + ", " + (memData[774]) + ", " + (memData[775]) +               
                ", " + (memData[776]) + ", " + (memData[777]) + ", " + (memData[778]) + ", " + (memData[779]) +               
                ", " + (memData[780]) + ", " + (memData[781]) + ", " + (memData[782]) + ", " + (memData[783]) + 
                ", " + (memData[784]) + ", " + (memData[785]) + ", " + (memData[786]) + ", " + (memData[787]) +
                ", " + (memData[788]) + ", " + (memData[789]) + ", " + (memData[790]) + ", " + (memData[791]) +               
                ", " + (memData[24]) + ", " + (memData[25]) + ", " + (memData[26]) + ", " + (memData[27]) +               
                ", " + (memData[28]) + ", " + (memData[29]) + ", " + (memData[30]) + ", " + (memData[31]) + 
                ", " + (memData[32]) + ", " + (memData[33]) + ", " + (memData[34]) + ", " + (memData[35]) +
                ", " + (memData[36]) + ", " + (memData[37]) + ", " + (memData[38]) + ", " + (memData[39]) +               
                ", " + (memData[40]) + ", " + (memData[41]) + ", " + (memData[42]) + ", " + (memData[43]) +               
                ", " + (memData[44]) + ", " + (memData[45]) + ", " + (memData[46]) + ", " + (memData[47]) + 
                ", " + (memData[48]) + ", " + (memData[49]) + ", " + (memData[50]) + ", " + (memData[51]) +
                ", " + (memData[52]) + ", " + (memData[53]) + ", " + (memData[54]) + ", " + (memData[55]) +               
                ", " + (memData[56]) + ", " + (memData[57]) + ", " + (memData[58]) + ", " + (memData[59]) +               
                ", " + (memData[60]) + ", " + (memData[61]) + ", " + (memData[62]) + ", " + (memData[63]) + 
                ", " + (memData[64]) + ", " + (memData[65]) + ", " + (memData[66]) + ", " + (memData[67]) +
                ", " + (memData[68]) + ", " + (memData[69]) + ", " + (memData[70]) + ", " + (memData[71]) +               
                ", " + (memData[72]) + ", " + (memData[73]) + ", " + (memData[74]) + ", " + (memData[75]) +               
                ", " + (memData[76]) + ", " + (memData[77]) + ", " + (memData[78]) + ", " + (memData[79]) + 
                ", " + (memData[80]) + ", " + (memData[81]) + ", " + (memData[82]) + ", " + (memData[83]) +
                ", " + (memData[84]) + ", " + (memData[85]) + ", " + (memData[86]) + ", " + (memData[87]) +               
                ", " + (memData[88]) + ", " + (memData[89]) + ", " + (memData[90]) + ", " + (memData[91]) +               
                ", " + (memData[92]) + ", " + (memData[93]) + ", " + (memData[94]) + ", " + (memData[95]) + 
                ", " + (memData[96]) + ", " + (memData[97]) + ", " + (memData[98]) + ", " + (memData[99]) +
                ", " + (memData[100]) + ", " + (memData[101]) + ", " + (memData[102]) + ", " + (memData[103]) +               
                ", " + (memData[104]) + ", " + (memData[105]) + ", " + (memData[106]) + ", " + (memData[107]) +               
                ", " + (memData[108]) + ", " + (memData[109]) + ", " + (memData[110]) + ", " + (memData[111]) + 
                ", " + (memData[112]) + ", " + (memData[113]) + ", " + (memData[114]) + ", " + (memData[115]) +
                ", " + (memData[116]) + ", " + (memData[117]) + ", " + (memData[118]) + ", " + (memData[119]) +               
                ", " + (memData[120]) + ", " + (memData[121]) + ", " + (memData[122]) + ", " + (memData[123]) +               
                ", " + (memData[124]) + ", " + (memData[125]) + ", " + (memData[126]) + ", " + (memData[127]) + 
                ", " + (memData[128]) + ", " + (memData[129]) + ", " + (memData[130]) + ", " + (memData[131]) +
                ")";
    }

    /**
     * @return the latenciaM
     */
    public int getLatenciaM() {
        return latenciaM;
    }

    /**
     * @param latenciaM the latenciaM to set
     */
    public void setLatenciaM(int latenciaM) {
        this.latenciaM = latenciaM;
    }
}
