package mask.streetmaster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

public class GameActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Character character;
    SurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // get the character from the save
        sharedPreferences = getSharedPreferences("SAVE", MODE_PRIVATE);
        String stats = sharedPreferences.getString("STATS", "");
        String stuff = sharedPreferences.getString("STUFF", "");
        character = MainActivity.createCharacterFromSave(stats, stuff);

        //sets the activity view as CanvasView class
        GameEngine engine = new GameEngine(character, getApplicationContext());
        view = new CanvasView(this, engine);
        setContentView(view);
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
}
