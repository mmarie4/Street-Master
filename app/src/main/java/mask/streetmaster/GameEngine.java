package mask.streetmaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


public class GameEngine  {
    Paint _paint;
    Character character;
    boolean moving_left, moving_right;
    Bitmap bmp_background, bmp_character, gameover_background, bmp_background_scaled;
    Context context;
    int width, height;
    Rect background_rect, left_button_rect, right_button_rect, character_rect, menu_button_rect, replay_button_rect, gameover_rect;
    Bitmap left_button_image, right_button_image, menu_button_image, replay_button_image;
    Ennemy[] cops, trees, gangsters;
    int score, money, current_hp;
    boolean game_over;

    public GameEngine(Character c, Context pContext) {
        character = c;
        _paint = new Paint();
        _paint.setFilterBitmap(true);
        context = pContext;
        score=0;
        money=0;
        game_over=false;
        current_hp=character.max_hp;

        character_rect = new Rect();
        background_rect = new Rect();
        gameover_rect = new Rect();

        left_button_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.left);
        right_button_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.right);
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
    }

    public void Update() {
        if(moving_left) Log.e("MOVING: ", "MOVING LEFT");
        if(moving_right) Log.e("MOVING: ", "MOVING RIGHT");
        if(!game_over){
            score++;
            if(current_hp<=0) {
                current_hp=0;
                if(score > character.bestScore) character.bestScore = score;
                character.money += money;
                game_over = true;
            }
            updateCharacter();
            handleTrees();
            handleGangsters();
            handleCops();
        }
    }

    public void updateCharacter() {
        if(moving_left) {
            character.x -= (int)(character.speed*width*0.05/100);
        }
        if(moving_right) {
            character.x += (int)(character.speed*width*0.05/100);
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

        drawCops(canvas);
        drawGangsters(canvas);
        drawTrees(canvas);
        drawCharacter(canvas);
        _paint.setTextSize(45);
        _paint.setColor(Color.WHITE);
        canvas.drawText("Score: "+score, (float)(width*3.0/100), (float)(height*3.0/100), _paint);
        canvas.drawText("Money: "+money, (float)(width*3.0/100), (float)(height*6.0/100), _paint);
        _paint.setColor(Color.RED);
        canvas.drawRect((float)(width*3.0/100),(float)(height*8.0/100), (float)(width*3.0/100 + (current_hp*width*1.0/100)), (float)(height*10.0/100), _paint);
        if(game_over){
            gameover_rect = new Rect(0, 0, width, height);
            canvas.drawBitmap(gameover_background, null, gameover_rect, _paint);
            _paint.setColor(Color.WHITE);
            _paint.setTextSize(60);
            canvas.drawText("GAME OVER!", (int)(width*20.0/100), (int)(height*45.0/100), _paint);
            canvas.drawText("Score: "+score, (int)(width*20.0/100), (int)(height*50.0/100), _paint);
            canvas.drawText("Best score: "+character.bestScore, (int)(width*20.0/100), (int)(height*55.0/100), _paint);
            canvas.drawText("Money earned: "+money+" $", (int)(width*20.0/100), (int)(height*60.0/100), _paint);
            canvas.drawBitmap(replay_button_image, null, replay_button_rect, _paint);
        }
        drawButtons(canvas);
    }

    public void drawButtons(Canvas canvas) {
        canvas.drawBitmap(left_button_image, null, left_button_rect, _paint);
        canvas.drawBitmap(right_button_image, null, right_button_rect, _paint);
        canvas.drawBitmap(menu_button_image, null, menu_button_rect, _paint);
    }

    public void drawCharacter(Canvas canvas) {
        canvas.drawBitmap(bmp_character, null, character_rect, _paint);
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

    public void drawGangsters(Canvas canvas) {
        for(int i=0; i<gangsters.length; i++) {
            if(gangsters[i].active) {
                canvas.drawBitmap(gangsters[i].image, null, gangsters[i].box, _paint);
            }
        }
    }

    public void setRectButtons(Rect left, Rect right, Rect menu, Rect replay) {
        left_button_rect = left;
        right_button_rect = right;
        menu_button_rect = menu;
        replay_button_rect = replay;
    }

    public void handleCops() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.95) {
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
                    current_hp=0;
                }
            }
        }
    }

    public void handleTrees() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.95) {
            int i=0;
            while(trees[i].active && i<trees.length-1) {
                i++;
            }
            if(!trees[i].active) trees[i].replaceSpawn(width);
        }
        // move active trees and desactivate trees outside map and check collisions
        for(int j=0; j<trees.length; j++) {
            if(trees[j].active) {
                trees[j].y += trees[j].speed*height*0.025/100;
                trees[j].updateBox();
                if(trees[j].y >= height) {
                    trees[j].setActive(false);
                }
                if(trees[j].box.intersects(character_rect.left, character_rect.top, character_rect.right, character_rect.bottom)) {
                    if(!trees[j].fight_done) {
                        current_hp -= 5;
                        trees[j].fight_done = true;
                    }
                }
            }
        }
    }

    public void handleGangsters() {
        // random spawn
        double random_spawn = Math.random();
        if(random_spawn>0.90) {
            int i=0;
            while(gangsters[i].active && i<gangsters.length-1) {
                i++;
            }
            if(!gangsters[i].active) gangsters[i].replaceSpawn(width);
        }
        // move active gangsters and desactivate gangsters outside map
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
                            current_hp -= 5;
                            gangsters[j].fight_done = true;
                        }else{
                            money += 5+character.cred;
                            gangsters[j].setActive(false);
                        }
                    }
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
        for(int i = 0; i<cops.length; i++) cops[i].reset();
        for(int j = 0; j<trees.length; j++) trees[j].reset();
        for(int k = 0; k<gangsters.length; k++) gangsters[k].reset();
    }

    public void setScaledBackground() {
        bmp_background_scaled = Bitmap.createScaledBitmap(bmp_background, width, height, true);
    }
}
