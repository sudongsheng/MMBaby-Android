package me.ahfun.mmbaby.utils;

/**
 * Created by Inner on 14-4-15.
 */
public class Pets {

    private int petsLevel = 0;
    private int firstNeedIntegral = 1000;
    private int currentIntegral;
    private int needIntegral;

    public void levelUp(int integral) {
        while (integral >= firstNeedIntegral) {
            petsLevel++;
            integral = integral - firstNeedIntegral;
            firstNeedIntegral = firstNeedIntegral + 1000;
        }
        if (petsLevel<=20){
            currentIntegral = integral;
            needIntegral = firstNeedIntegral;
        }else {
            petsLevel = 20;
            currentIntegral = 40000;
            needIntegral=40000;
        }

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
