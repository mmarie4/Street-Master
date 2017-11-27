package mask.streetmaster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CustomActivity extends AppCompatActivity {
    Character character;
    Item[] items;
    ImageButton item0, item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11;
    int money;
    String stats;
    String stuff;
    TextView tv_strength;
    TextView tv_speed;
    TextView tv_cred;
    TextView tv_hp;
    TextView tv_money;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        final ImageButton menu = (ImageButton) findViewById(R.id.menu_button);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        // create the list of items
        items = generateItems();

        // get character from the save
        sharedPreferences = getSharedPreferences("SAVE", MODE_PRIVATE);
        stats = sharedPreferences.getString("STATS", "");
        stuff = sharedPreferences.getString("STUFF", "");
        character = MainActivity.createCharacterFromSave(stats, stuff);

        // get and set the texts
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/CHILLER.TTF");

        tv_strength = (TextView) findViewById(R.id.textView_strength);
        String text_strength = "Strength: " + character.strength;
        tv_strength.setText(text_strength);
        tv_strength.setTypeface(custom_font);

        tv_speed = (TextView) findViewById(R.id.textView_speed);
        String text_speed = "Speed: " + character.speed;
        tv_speed.setText(text_speed);
        tv_speed.setTypeface(custom_font);

        tv_cred = (TextView) findViewById(R.id.textView_cred);
        String text_cred= "Street cred: " + character.cred;
        tv_cred.setText(text_cred);
        tv_cred.setTypeface(custom_font);

        tv_hp = (TextView) findViewById(R.id.textView_hp);
        String text_hp= "HP: " + character.max_hp;
        tv_hp.setText(text_hp);
        tv_hp.setTypeface(custom_font);

        tv_money = (TextView) findViewById(R.id.textView_money);
        String text_money= "Money: " + character.money + " $";
        tv_money.setText(text_money);
        tv_money.setTypeface(custom_font);

        // define buttons for items
        setButtonsItems();
        updateButtons();
    }

    public void goItem(int numberItem) {
        Intent intent = new Intent(CustomActivity.this, ItemActivity.class);
        intent.putExtra("PRICE", items[numberItem].price);
        intent.putExtra("MONEY", character.money);
        intent.putExtra("TEXT", items[numberItem].text);
        intent.putExtra("NUMBER", numberItem);
        if(character.stuff[numberItem]==1){
            intent.putExtra("EQUIPPED", true);
        }else{
            intent.putExtra("EQUIPPED", false);
        }
        startActivityForResult(intent, 1);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Immersive mode
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public Item[] generateItems() {
        Item[] items = new Item[12];
        items[0] = new Item("CAP", 50, 1, 0, 2, 2); // price, strength, speed, street cred, hp
        items[1] = new Item("GOLD CHAIN", 100, 0, 0, 5, 2);
        items[2] = new Item("TRACKSUIT", 150, 3, 3, 3, 5);
        items[3] = new Item("BASEBALL BAT", 250, 10, 0, 5, 0);
        items[4] = new Item("KNIFE", 300, 10, 0, 10, 0);
        items[5] = new Item("SNEAKERS", 500, 0, 5, 0, 3);
        items[6] = new Item("PITBULL", 1000, 10, 0, 20, 3);
        items[7] = new Item("KATANA", 1000, 20, 0, 20, 0);
        items[8] = new Item("BULLETPROOF VEST", 2000, 0, 0, 5, 20);
        items[9] = new Item("GLOCK", 3000, 50, 0, 50, 0);
        items[10] = new Item("SHOTGUN", 5000, 70, 0, 70, 0);
        items[11] = new Item("FLIP FLOPS", 1000000, 1000, 20, 1000, 1000);
        return items;
    }

    public void updateStatusAfterBuy(int id){
        character.money-=items[id].price;

        character.stuff[id] = 1;
        stuff = character.getStringStuff();

        character.strength+=items[id].strength;
        character.speed+=items[id].speed;
        character.cred+=items[id].cred;
        character.max_hp+=items[id].hp;
        stats = character.getStringStats();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("STUFF", stuff);
        editor.putString("STATS", stats);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result = data.getIntExtra("result", 0);
                updateStatusAfterBuy(result);
                updateTextView();
                updateButtons();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    public void updateTextView(){
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/CHILLER.TTF");

        String text_strength = "Strength: " + character.strength;
        tv_strength.setText(text_strength);
        tv_strength.setTypeface(custom_font);

        String text_speed = "Speed: " + character.speed;
        tv_speed.setText(text_speed);
        tv_speed.setTypeface(custom_font);

        String text_cred= "Street cred: " + character.cred;
        tv_cred.setText(text_cred);
        tv_cred.setTypeface(custom_font);

        String text_hp= "HP: " + character.max_hp;
        tv_hp.setText(text_hp);
        tv_hp.setTypeface(custom_font);

        String text_money= "Money: " + character.money + " $";
        tv_money.setText(text_money);
        tv_money.setTypeface(custom_font);
    }

    public void updateButtons() {
        if (character.stuff[0]==1)item0.setImageResource(R.drawable.item0_equipped);
        if (character.stuff[1]==1)item1.setImageResource(R.drawable.item1_equipped);
        if (character.stuff[2]==1)item2.setImageResource(R.drawable.item2_equipped);
        if (character.stuff[3]==1)item3.setImageResource(R.drawable.item3_equipped);
        if (character.stuff[4]==1)item4.setImageResource(R.drawable.item4_equipped);
        if (character.stuff[5]==1)item5.setImageResource(R.drawable.item5_equipped);
        if (character.stuff[6]==1)item6.setImageResource(R.drawable.item6_equipped);
        if (character.stuff[7]==1)item7.setImageResource(R.drawable.item7_equipped);
        if (character.stuff[8]==1)item8.setImageResource(R.drawable.item8_equipped);
        if (character.stuff[9]==1) item9.setImageResource(R.drawable.item9_equipped);
        if (character.stuff[10]==1) item10.setImageResource(R.drawable.item10_equipped);
        if (character.stuff[11]==1) item11.setImageResource(R.drawable.item11_equipped);
    }

    public void setButtonsItems() {
        item0 = (ImageButton) findViewById(R.id.item0_button);
        item0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(0);
            }
        });

        item1 = (ImageButton) findViewById(R.id.item1_button);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(1);
            }
        });

        item2 = (ImageButton) findViewById(R.id.item2_button);
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(2);
            }
        });

        item3 = (ImageButton) findViewById(R.id.item3_button);
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(3);
            }
        });

        item4 = (ImageButton) findViewById(R.id.item4_button);
        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(4);
            }
        });

        item5 = (ImageButton) findViewById(R.id.item5_button);
        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(5);
            }
        });

        item6 = (ImageButton) findViewById(R.id.item6_button);
        item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(6);
            }
        });

        item7 = (ImageButton) findViewById(R.id.item7_button);
        item7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(7);
            }
        });

        item8 = (ImageButton) findViewById(R.id.item8_button);
        item8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(8);
            }
        });

        item9 = (ImageButton) findViewById(R.id.item9_button);
        item9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(9);
            }
        });

        item10 = (ImageButton) findViewById(R.id.item10_button);
        item10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(10);
            }
        });

        item11 = (ImageButton) findViewById(R.id.item11_button);
        item11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goItem(11);
            }
        });
    }

}
