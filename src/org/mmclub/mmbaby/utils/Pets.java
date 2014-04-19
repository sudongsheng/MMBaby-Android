package org.mmclub.mmbaby.utils;

/**
 * Created by Inner on 14-4-15.
 */
public class Pets {

    private String petsName;
    private int petsLevel = 0;
    private int petsImage;
    private int needIntegral = 1000;
    private int currentIntegral;

    public void levelUp (int integral){
        while (integral>=needIntegral){
            integral = integral - needIntegral;
            petsLevel++;
            needIntegral = needIntegral*2;
        }
        currentIntegral = integral;
        }
    public int getPetsLevel(){
        return petsLevel;
    }
    public int getNeedIntegral(){
        return needIntegral;
    }
    public int getCurrentIntegral(){
        return currentIntegral;
    }

}
