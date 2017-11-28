package mask.streetmaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    SurfaceHolder _surfaceHolder;
    Paint _backgroundPaint;
    GameEngine engine;
    boolean  _isOnRun;

    public GameThread(SurfaceHolder surfaceHolder, Context context, GameEngine pEngine) {
        _surfaceHolder = surfaceHolder;
        engine = pEngine;

        //black painter below to clear the screen before the game is rendered
        _backgroundPaint = new Paint();
        _backgroundPaint.setARGB(255, 0, 0, 0);
        _isOnRun = true;
    }

    @Override
    public void run() {
        long FPS = 40;
        long ticksPS = (long) (1000.0 / FPS);
        long startTime;
        long sleepTime;
        while (_isOnRun) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            engine.Update();
            try {
                c = _surfaceHolder.lockCanvas();
                synchronized (_surfaceHolder) {
                    engine.Draw(c);
                }
            } finally {
                if (c != null) {
                    _surfaceHolder.unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
            //Log.e("tag", "loop time: " + (ticksPS-sleepTime));
            try {
                if (sleepTime > 0) {
                    sleep(sleepTime);
                    //Log.e("tag", "sleeptime: " + sleepTime);
                }
                else{
                    sleep(4);
                }
            } catch (Exception e) {}
        }
}


    public boolean IsRunning() {
        return _isOnRun;
    }

    public void SetIsRunning(boolean state) {
        _isOnRun = state;
    }
}