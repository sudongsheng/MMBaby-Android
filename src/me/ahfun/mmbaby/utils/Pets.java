package me.ahfun.mmbaby.utils;

/**
 * Created by Inner on 14-4-15.
 */
public class Pets {

    private String petsName;
    private int petsLevel = 0;
    private int petsImage;
    private int firstNeedIntegral = 1000;
    private int currentIntegral;
    private int needIntegral;

    public void levelUp(int integral) {
        while (integral >= firstNeedIntegral) {
            petsLevel++;
            integral = integral - firstNeedIntegral;
            firstNeedIntegral = firstNeedIntegral * 2;
        }
        currentIntegral = integral;
        needIntegral = firstNeedIntegral;
    }
    public int imageUp(int petsLevel){
        return petsImage;
    }

    public int getPetsLevel() {
        return petsLevel;
    }

    public int getNeedIntegral() {
        return needIntegral;
    }

    public int getCurrentIntegral() {
        return currentIntegral;
    }

}
