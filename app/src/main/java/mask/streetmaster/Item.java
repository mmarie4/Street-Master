package mask.streetmaster;

public class Item {
    public String text;
    public String name;
    public int price;
    public int strength;
    public int speed;
    public int hp;
    public int cred;

    public Item(String pName, int pPrice, int pStrength, int pSpeed, int pCred, int pHp) {
        name=pName;
        price=pPrice;
        strength=pStrength;
        speed=pSpeed;
        cred=pCred;
        hp=pHp;
        text=generateText();
    }

    public String generateText(){
        return name+"\n\nStrength +"+strength+"\nSpeed +"+speed+"\nStreet cred +"+cred+"\nHP +"+hp+"\n\nPrice: "+price+ "$";
    }
}
