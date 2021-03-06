package mask.streetmaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.content.Context.MODE_PRIVATE;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback
{
    SurfaceHolder surfaceHolder;
    Context context;
    private GameThread thread;
    GameEngine engine;
    int width, height;
    Rect left_button_rect, right_button_rect, menu_button_rect, replay_button_rect;

    public CanvasView(Context pContext, GameEngine gEngine) {
        super(pContext);
        context = pContext;
        engine = gEngine;
        InitView();
    }

    void InitView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        initButtons();

        thread = new GameThread(holder, context, engine);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	   /*DO NOTHING*/
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        //Stop the display thread
        thread.SetIsRunning(false);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        //Starts the display thread
        if(!thread.IsRunning()) {
            thread = new GameThread(surfaceHolder, context, engine);
            thread.start();
        }
        else {
            thread.start();
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent me) {
        int pointerIndex = me.getActionIndex();
        Point p = new Point();
        p.y = (int)(me.getY());
        p.x = (int)(me.getX());
        switch (me.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                if(me.getX(pointerIndex)<width/2){
                    engine.moving_left = false;
                }
                if(me.getX(pointerIndex)>width/2){
                    engine.moving_right = false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if(me.getX(pointerIndex)<width/2){
                    engine.moving_left = true;
                }
                if(me.getX(pointerIndex)>width/2){
                    engine.moving_right = true;
                }
                if(menu_button_rect.contains((int)(me.getX(pointerIndex)), (int)(me.getY(pointerIndex)))) {
                    thread.interrupt();
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
                if(replay_button_rect.contains(p.x, p.y) && engine.game_over) {
                    replay();
                }
                break;
            /* MotionEvent.ACTION_MOVE:
                // click and drag out the zone deactivate the move
                if(!(me.getX(pointerIndex)<width/2)) {
                    engine.moving_left=false;
                }
                if(!(me.getX(pointerIndex)>width/2)) {
                    engine.moving_right=false;
                }
                break;*/
            case MotionEvent.ACTION_POINTER_DOWN:
                // second touch
                if(me.getX(pointerIndex)<width/2){
                    engine.moving_left = true;
                }
                if(me.getX(pointerIndex)>width/2){
                    engine.moving_right = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // second finger leave
                if(me.getX(pointerIndex)<width/2){
                    engine.moving_left = false;
                }
                if(me.getX(pointerIndex)>width/2){
                    engine.moving_right = false;
                }
                break;
        }
    return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        engine.character.x = (int)(width*45.0/100);
        initButtons();
    }

    public void initButtons(){
        int menu_x = (int) (width * 75.0/100);
        int menu_y = (int) (height * 2.0/100);
        int replay_x = (int) (width*50.0/100 - engine.replay_button_image.getWidth()/2);
        int replay_y = (int) (height*75.0/100);
        menu_button_rect = new Rect(menu_x, menu_y, menu_x+engine.menu_button_image.getWidth(), menu_y+engine.menu_button_image.getHeight());
        replay_button_rect = new Rect(replay_x, replay_y, replay_x+engine.replay_button_image.getWidth(), replay_y+engine.replay_button_image.getHeight());
        engine.setRectButtons(menu_button_rect, replay_button_rect);
    }

    public void replay() {
        // start new game
        engine.reset();
    }
}