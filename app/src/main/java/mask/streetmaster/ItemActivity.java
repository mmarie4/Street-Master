package mask.streetmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {
    int price;
    int money;
    String text;
    int number;
    boolean equiped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        final ImageButton back = (ImageButton) findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });

        // get the info from the intent
        price = getIntent().getIntExtra("PRICE", 0);
        money = getIntent().getIntExtra("MONEY", 0);
        text = getIntent().getStringExtra("TEXT");
        number = getIntent().getIntExtra("NUMBER", 0);
        equiped = getIntent().getBooleanExtra("EQUIPPED", false);

        // set the texts
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/CHILLER.TTF");
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(text);
        tv.setTypeface(custom_font);
        TextView tv_money = (TextView) findViewById(R.id.textView_money);
        String text_money= "Money: " + money + " $";
        tv_money.setText(text_money);
        tv_money.setTypeface(custom_font);

        // set the image
        ImageView image = (ImageView) findViewById(R.id.imageView);
        String str = "item" + number;
        image.setImageResource(getResources().getIdentifier(str, "drawable", getPackageName()));

        // set the buy button
        final ImageButton buy_button = (ImageButton) findViewById(R.id.buy_button);
        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", number); // number of the item
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        if((money<price) || (equiped)) {
            buy_button.setImageResource(R.drawable.buy_button_off);
            buy_button.setEnabled(false);
        }
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
