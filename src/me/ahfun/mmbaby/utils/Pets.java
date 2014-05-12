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
        if (petsLevel<3){
            currentIntegral = integral;
            needIntegral = firstNeedIntegral;
        }else {
            petsLevel = 2;
            currentIntegral = 4000;
            needIntegral=4000;
        }

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
