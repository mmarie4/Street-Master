package mask.streetmaster;

import android.util.Log;

import java.io.Serializable;

public class Character implements Serializable {
    public int[] stuff; // stuff[i]=1 if the stuff i is owned, otherwise it is equal to 0
    public int x;
    public int strength, speed, cred;
    public int bestScore;
    public int money;
    public int max_hp;
    private String stats;

    public Character(int[] pStuff, int pStrength, int pSpeed, int pCred, int pMax_hp, int pMoney, int pBest){
        stuff=pStuff;
        x=0;
        strength=pStrength;
        speed=pSpeed;
        cred=pCred;
        max_hp=pMax_hp;
        money=pMoney;
        bestScore=pBest;
    }

    public Character(){
        stuff=new int[16];
        x=0;
        strength=10;
        speed=20;
        cred=0;
        max_hp=20;
        bestScore=0;
        money=10000000;
    }

    public String getStringStuff() {
        return stuff[0]+"-"+stuff[1]+"-"+stuff[2]+"-"+stuff[3]+"-"+stuff[4]+"-"+stuff[5]+"-"+stuff[6]+"-"+stuff[7]+"-"+
                stuff[8]+"-"+stuff[9]+"-"+stuff[10]+"-"+stuff[11]+"-"+stuff[12]+"-"+stuff[13]+"-"+stuff[14]+"-"+stuff[15];
    }

    public String getStringStats() {
        return strength + "-" +  speed + "-" + cred + "-" +  max_hp + "-" + money +  "-" + bestScore;
    }

}
