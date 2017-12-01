package mask.streetmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;


public class GameEngine  {
    Paint _paint;
    Character character;
    boolean moving_left, moving_right;
    Bitmap bmp_background, bmp_character, gameover_background, bmp_background_scaled;
    Context context;
    int width, height;
    Rect background_rect, character_rect, menu_button_rect, replay_button_rect, gameover_rect;
    Bitmap menu_button_image, replay_button_image;
    Ennemy[] cops, trees, gangsters, coins;
    int score, money, current_hp;
    double cred_cop;
    boolean game_over;

    public GameEngine(Character c, Context pContext) {
        character = c;
        context = pContext;
        _paint = new Paint();
        _paint.setFilterBitmap(true);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),"fonts/CHILLER.TTF");
        _paint.setTypeface(tf);
        score=0;
        money=0;
        cred_cop=0;
        game_over=false;
        current_hp=character.max_hp;

        character_rect = new Rect();
        background_rect = new Rect();
        gameover_rect = new Rect();

        bmp_background = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgroundgame);
        bmp_character = BitmapFactory.decodeResource(context.getResources(), R.drawable.character);
        menu_button_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_button);
        replay_button_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.replay);
        gameover_background = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);

        cops = new Ennemy[10];
        for(int i=0; i<cops.length; i++) cops[i] = new Ennemy(1, BitmapFactory.decodeResource(context.getResources(), R.drawable.cops));
        trees = new Ennemy[10];
        for(int j=0; j<trees.length; j++) trees[j] = new Ennemy(0, BitmapFactory.decodeResource(context.getResources(), R.drawable.tree));
        gangsters = new Ennemy[10];
        for(int k=0; k<gangsters.length; k++) gangsters[k] = new Ennemy(2, BitmapFactory.decodeResource(context.getResources(), R.drawable.gangster));
        coins = new Ennemy[10];
        for(int k=0; k<coins.length; k++) coins[k] = new Ennemy(3, BitmapFactory.decodeResource(context.getResources(), R.drawable.coins));

    }

    public void Update() {
        if(!game_over){
            score++;
            if(current_hp<=0) {
                current_hp=0;
                if(score > character.bestScore) character.bestScore = score;
                character.money += money;
                game_over = true;
                // if it was during an animation
                if(character.animation_losing_life) {
                    character.animation_losing_life=false;
                    character.index_animation=0;
                    character.speed = character.speed*2;
                }
                // save score  and money
                SharedPreferences sharedPreferences = context.getSharedPreferences("SAVE", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("STUFF", character.getStringStuff());
                editor.putString("STATS", character.getStringStats());
                editor.commit();
            }
            updateCharacter();
            handleTrees();
            handleGangsters();
            handleCops();
            handleCoins();
        }
    }

    public void updateCharacter() {
        // animation
        if(character.animation_losing_life) {
            if(character.index_animation>=character.tab_animation.length) {
                character.animation_losing_life=false;
                character.speed = character.speed*2;
                character.index_animation=0;
            }

        }
        if(moving_left) {
            character.x -= (int)(character.speed*width*0.08/100);
        }
        if(moving_right) {
            character.x += (int)(character.speed*width*0.08/100);
        }
        if(character.x <= 0) {
            character.x = 0;
        }
        if(character.x >= (width-character_rect.width())) {
            character.x = (width-character_rect.width());
        }
        character_rect.set(character.x, (int)(height*80.0/100), character.x+bmp_character.getWidth(), (int)(height*80.0/100)+bmp_character.getHeight());
    }

    public void Draw(Canvas canvas){
        if((canvas.getWidth()!=width) || (canvas.getHeight()!=height)){
            width = canvas.getWidth();
            height = canvas.getHeight();
            background_rect.set(0, 0, width, height);
            setScaledBackground();
        }

        canvas.drawBitmap(bmp_background_scaled, null, background_rect, _paint);

        drawTrees(canvas);
        drawCops(canvas);
        drawGangsters(canvas);
        drawCoins(canvas);
        drawCharacter(canvas);
        _paint.setTextSize(55);
        _paint.setColor(Color.WHITE);
        canvas.drawText("Score: "+score, (float)(width*3.0/100), (float)(height*3.0/100), _paint);
        canvas.drawText("Money: "+money+" $", (float)(width*3.0/100), (float)(height*6.0/100), _paint);
        _paint.setColor(Color.RED);
        canvas.drawRect((float)(width*3.0/100),(float)(height*8.0/100), (float)(width*3.0/100 + (current_hp*width*1.0/100)), (float)(height*10.0/100), _paint);
        if(game_over){
            gameover_rect = new Rect(0, 0, width, height);
            canvas.drawBitmap(gameover_background, null, gameover_rect, _paint);
            _paint.setColor(Color.WHITE);
            _paint.setTextSize(80);
            canvas.drawText("GAME OVER!", (int)(width*20.0/100), (int)(height*45.0/100), _paint);
            canvas.drawText("Score: "+score, (int)(width*20.0/100), (int)(height*50.0/100), _paint);
            canvas.drawText("Best score: "+character.bestScore, (int)(width*20.0/100), (int)(height*55.0/100), _paint);
            canvas.drawText("Money earned: "+money+" $", (int)(width*20.0/100), (int)(height*60.0/100), _paint);
            canvas.drawBitmap(replay_button_image, null, replay_button_rect, _paint);
        }
        drawButtons(canvas);
    }

    public void drawButtons(Canvas canvas) {
        canvas.drawBitmap(menu_button_image, null, menu_button_rect, _paint);
    }

    public void drawCharacter(Canvas canvas) {
        if(character.animation_losing_life){
            if(character.tab_animation[character.index_animation] == 1){
                canvas.drawBitmap(bmp_character, null, character_rect, _paint);
            }
            character.index_animation++;
        }else{
            canvas.drawBitmap(bmp_character, null, character_rect, _paint);
        }
    }

    public void drawCops(Canvas canvas) {
        for(int i=0; i<cops.length; i++) {
            if(cops[i].active) {
                canvas.drawBitmap(cops[i].image, null, cops[i].box, _paint);
            }
        }
    }

    public void drawTrees(Canvas canvas) {
        for(int i=0; i<trees.length; i++) {
            if(trees[i].active) {
                canvas.drawBitmap(trees[i].image, null, trees[i].box, _paint);
            }
        }
    }

    public void drawCoins(Canvas canvas) {
        for(int i=0; i<coins.length; i++) {
            if(coins[i].active) {
                canvas.drawBitmap(coins[i].image, null, coins[i].box, _paint);
            }
        }
    }

    public void drawGangsters(Canvas canvas) {
        for(int i=0; i<gangsters.length; i++) {
            if(gangsters[i].active) {
                canvas.drawBitmap(gangsters[i].image, null, gangsters[i].box, _paint);
            }
        }
    }

    public void setRectButtons(Rect menu, Rect replay) {
        menu_button_rect = menu;
        replay_button_rect = replay;
    }

    public void handleCops() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.97) {
            int i=0;
            while(cops[i].active && i<cops.length-1) {
                i++;
            }
            if(!cops[i].active) cops[i].replaceSpawn(width);
        }
        // move active cops, desactivate cops outside map, and check collision with character
        for(int j=0; j<cops.length; j++) {
            if(cops[j].active) {
                cops[j].y += cops[j].speed*height*0.025/100;
                cops[j].updateBox();
                if(cops[j].y >= height) {
                    cops[j].setActive(false);
                }
                if(cops[j].box.intersects(character_rect.left, character_rect.top, character_rect.right, character_rect.bottom)) {
                    cred_cop = (int)(Math.random()*1000);
                    if(character.cred>=cred_cop){
                        cops[j].setActive(false);
                    }else{
                        current_hp=0;
                    }
                }
            }
        }
    }

    public void handleTrees() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.98) {
            int i=0;
            while(trees[i].active && i<trees.length-1) {
                i++;
            }
            if(!trees[i].active) trees[i].replaceSpawn(width);
        }
        // move active trees and deactivate trees outside map and check collisions
        for(int j=0; j<trees.length; j++) {
            if(trees[j].active) {
                trees[j].y += trees[j].speed*height*0.025/100;
                trees[j].updateBox();
                if(trees[j].y >= height) {
                    trees[j].setActive(false);
                }
                if(trees[j].box.intersects(character_rect.left, character_rect.top, character_rect.right, character_rect.bottom)) {
                    if(!trees[j].fight_done) {
                        if(!character.animation_losing_life) {
                            character.speed = character.speed/2;
                            character.animation_losing_life=true;
                            character.index_animation=0;
                            current_hp -= 5;
                        }
                        trees[j].fight_done = true;
                    }
                }
            }
        }
    }

    public void handleGangsters() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.96) {
            int i=0;
            while(gangsters[i].active && i<gangsters.length-1) {
                i++;
            }
            if(!gangsters[i].active) gangsters[i].replaceSpawn(width);
        }
        // move active gangsters and deactivate gangsters outside map
        for(int j=0; j<gangsters.length; j++) {
            if(gangsters[j].active) {
                gangsters[j].y += gangsters[j].speed*height*0.025/100;
                gangsters[j].updateBox();
                if(gangsters[j].y >= height) {
                    gangsters[j].setActive(false);
                }
                // handle collisions
                if(gangsters[j].box.intersects(character_rect.left, character_rect.top, character_rect.right, character_rect.bottom)) {
                    if(!gangsters[j].fight_done) {
                        gangsters[j].strength = (int)(Math.random()*200);
                        if(gangsters[j].strength>character.strength) {
                            if(!character.animation_losing_life){
                                character.speed = character.speed/2;
                                character.animation_losing_life=true;
                                character.index_animation=0;
                                current_hp -= 5;
                            }
                            gangsters[j].fight_done = true;
                        }else{
                            score += 100;
                            gangsters[j].setActive(false);
                        }
                    }
                }
            }
        }
    }

    public void handleCoins() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.97) {
            int i=0;
            while(coins[i].active && i<coins.length-1) {
                i++;
            }
            if(!coins[i].active) coins[i].replaceSpawn(width);
        }
        // move active trees and desactivate trees outside map and check collisions
        for(int j=0; j<coins.length; j++) {
            if(coins[j].active) {
                coins[j].y += coins[j].speed*height*0.025/100;
                coins[j].updateBox();
                if(coins[j].y >= height) {
                    coins[j].setActive(false);
                }
                if(coins[j].box.intersects(character_rect.left, character_rect.top, character_rect.right, character_rect.bottom)) {
                    money += 1;
                    coins[j].setActive(false);
                }
            }
        }
    }

    public void reset() {
        score=0;
        money=0;
        current_hp=character.max_hp;
        character.x=(int)(width*50.0/100-bmp_character.getWidth());
        game_over=false;
        moving_left=false;
        moving_right=false;
        for(int i = 0; i<cops.length; i++) cops[i].reset();
        for(int j = 0; j<trees.length; j++) trees[j].reset();
        for(int k = 0; k<gangsters.length; k++) gangsters[k].reset();
        for(int l = 0; l<coins.length; l++) coins[l].reset();
    }

    public void setScaledBackground() {
        bmp_background_scaled = Bitmap.createScaledBitmap(bmp_background, width, height, true);
    }
}
