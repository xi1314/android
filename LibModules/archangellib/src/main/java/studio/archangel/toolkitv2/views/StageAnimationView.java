package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import studio.archangel.toolkitv2.models.Actor;

/**
 * Created by Michael on 2015/2/5.
 */
public class StageAnimationView extends SurfaceView implements SurfaceHolder.Callback {
    int width, height;
    public List<Actor> actors;
    //    boolean playing = false;
//    boolean alive = false;
//    int refresh_rate = 1000 / 20;
    PlayThread play;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //    boolean playing = false;
//    Thread play = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            while (alive) {
//                if (!playing) {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    continue;
//                } else {
//                    try {
//                        Thread.sleep(refresh_rate);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                for (Actor actor : actors) {
//                    actor.move();
//                }
//            }
//        }
//    });

    public StageAnimationView(Context context) {
        super(context);
        init();
    }

    public StageAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public void init() {
        getHolder().addCallback(this);
        play = new PlayThread(getHolder(), this);
        setFocusable(true);
        actors = new ArrayList<Actor>();
//        alive = true;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (playing) {
//            Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas c = new Canvas(b);
//            for (Actor actor : actors) {
//                Bitmap bitmap = actor.getBitmap();
//                c.drawBitmap(bitmap, actor.x, actor.y, null);
//            }
//            canvas.drawBitmap(b, 0, 0, null);
//            invalidate();
//        }
//    }

    public void play() {
//        playing = true;
//        play.start();

    }

    public void pause() {
//        playing = false;
    }

    public void resume() {
//        playing = true;
    }

    public void stop() {
        play.setRunning(false);
//        playing = false;
//        alive = false;
    }

    public void update() {
        for (Actor actor : actors) {
            actor.move();
//            Logger.out("actor's x:" + actor.x);
        }

    }

    public void render(Canvas canvas) {
        if (canvas == null) {
            return;
        }
//        Logger.out("render");
        canvas.drawColor(Color.BLACK);
//        Bitmap b = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        for (Actor actor : actors) {
//            Bitmap bitmap = actor.getBitmap();
//            c.drawBitmap(bitmap, actor.x, actor.y, null);
//        }
//        canvas.drawBitmap(b, 0, 0, paint);
        for (Actor actor : actors) {
            Bitmap bitmap = actor.getBitmap();
            canvas.drawBitmap(bitmap, actor.x, actor.y, null);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        play.setRunning(true);
//        playing=true;
//        play.start();
        play.setRunning(true);
        if (!play.isAlive()) {
            try {
                play.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public class PlayThread extends Thread {


        // desired fps
        private final static int MAX_FPS = 60;
        // maximum number of frames to be skipped
        private final static int MAX_FRAME_SKIPS = 1;
        // the frame period
        private final static int FRAME_PERIOD = 1000 / MAX_FPS;

        // Surface holder that can access the physical surface
        private SurfaceHolder holder;
        // The actual view that handles inputs
        // and draws to the surface
        private StageAnimationView stage;

        // flag to hold game state
        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        public PlayThread(SurfaceHolder h, StageAnimationView s) {
            super();
            this.holder = h;
            this.stage = s;
        }

        @Override
        public void run() {
            Canvas canvas;
//            Log.d(TAG, "Starting game loop");

            long beginTime;        // the time when the cycle begun
            long timeDiff;        // the time it took for the cycle to execute
            int sleepTime;        // ms to sleep (<0 if we're behind)
            int framesSkipped;    // number of frames being skipped

            sleepTime = 0;

            while (running) {
                canvas = null;
                // try locking the canvas for exclusive pixel editing
                // in the surface
                try {
                    canvas = this.holder.lockCanvas();
                    synchronized (holder) {
                        beginTime = System.currentTimeMillis();
                        framesSkipped = 0;    // resetting the frames skipped
                        // update game state
                        this.stage.update();
                        // render state to the screen
                        // draws the canvas on the panel
                        this.stage.render(canvas);
                        // calculate how long did the cycle take
                        timeDiff = System.currentTimeMillis() - beginTime;
                        // calculate sleep time
                        sleepTime = (int) (FRAME_PERIOD - timeDiff);

                        if (sleepTime > 0) {
                            // if sleepTime > 0 we're OK
                            try {
                                // send the thread to sleep for a short period
                                // very useful for battery saving
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                            }
                        }

                        while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                            // we need to catch up
                            this.stage.update(); // update without rendering
                            sleepTime += FRAME_PERIOD;    // add frame period to check if in next frame
                            framesSkipped++;
                        }
                    }
                } finally {
                    // in case of an exception the surface is not left in
                    // an inconsistent state
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }    // end finally
            }
        }

    }

}
