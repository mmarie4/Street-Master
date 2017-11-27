package mask.streetmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    Character character;
    String stats;
    String stuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // restore character from the save
        SharedPreferences sharedPreferences = getSharedPreferences("SAVE", MODE_PRIVATE);
        if (sharedPreferences.contains("STATS")) {
            stats = sharedPreferences.getString("STATS", "");
            stuff = sharedPreferences.getString("STUFF", "");
            character = createCharacterFromSave(stats, stuff);
        }else{
            character = new Character();
            stats = save(character, sharedPreferences);
            stuff = character.getStringStuff();
        }

        // create buttons and text views
        final ImageButton play = (ImageButton) findViewById(R.id.playButton);
        final ImageButton custom = (ImageButton) findViewById(R.id.customButton);
        final ImageButton instructions = (ImageButton) findViewById(R.id.instructionsButton);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPlay();
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCustom();
            }
        });
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InstructionsActivity.class);
                startActivity(intent);
            }
        });
    }


    public void goPlay(){
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("STATS", stats);
        intent.putExtra("STUFF", stuff);
        startActivity(intent);
    }

    public void goCustom(){
        Intent intent = new Intent(MainActivity.this, CustomActivity.class);
        intent.putExtra("STATS", stats);
        intent.putExtra("STUFF", stuff);
        intent.putExtra("FROM", "menu");
        startActivity(intent);
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

    public static Character createCharacterFromSave(String stats, String stuff) {
        String[] tab_stats = stats.split("-");
        String[] tab_stuff = stuff.split("-");
        Character character = new Character(stringTabToInt(tab_stuff), Integer.parseInt(tab_stats[0]), Integer.parseInt(tab_stats[1]), Integer.parseInt(tab_stats[2]),
                Integer.parseInt(tab_stats[3]), Integer.parseInt(tab_stats[4]), Integer.parseInt(tab_stats[5]));
        return character;
    }

    public String save(Character character, SharedPreferences sharedPreferences) {
        String stats = character.getStringStats();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("STUFF", character.getStringStuff());
        editor.putString("STATS", stats);
        editor.commit();
        return stats;
    }

    public static int[] stringTabToInt(String[] tab) {
        int[] res = new int[tab.length];
        for(int i =0; i<tab.length; i++){
            res[i] = Integer.parseInt(tab[i]);
        }
        return res;
    }
}
