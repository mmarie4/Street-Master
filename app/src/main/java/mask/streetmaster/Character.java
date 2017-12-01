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
    boolean animation_losing_life;
    int[] tab_animation; // 0=non visible,1=visible
    int index_animation;

    public Character(int[] pStuff, int pStrength, int pSpeed, int pCred, int pMax_hp, int pMoney, int pBest){
        stuff=pStuff;
        x=0;
        strength=pStrength;
        speed=pSpeed;
        cred=pCred;
        max_hp=pMax_hp;
        money=pMoney;
        bestScore=pBest;
        animation_losing_life=false;
        tab_animation=new int[40];
        index_animation=0;
        for(int i=0; i<tab_animation.length; i++){
            if (i<5) tab_animation[i]=0;
            if (i>=10 && i<15) tab_animation[i]=1;
            if (i>=20 && i<25) tab_animation[i]=0;
            if (i>=30 && i<35) tab_animation[i]=1;
            if (i>=35) tab_animation[i]=0;
        }
    }

    public Character(){
        stuff=new int[16];
        x=0;
        strength=10;
        speed=20;
        cred=0;
        max_hp=20;
        bestScore=0;
        money=0;
    }

    public String getStringStuff() {
        return stuff[0]+"-"+stuff[1]+"-"+stuff[2]+"-"+stuff[3]+"-"+stuff[4]+"-"+stuff[5]+"-"+stuff[6]+"-"+stuff[7]+"-"+
                stuff[8]+"-"+stuff[9]+"-"+stuff[10]+"-"+stuff[11]+"-"+stuff[12]+"-"+stuff[13]+"-"+stuff[14]+"-"+stuff[15];
    }

    public String getStringStats() {
        return strength + "-" +  speed + "-" + cred + "-" +  max_hp + "-" + money +  "-" + bestScore;
    }

}
