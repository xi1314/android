/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.remair.heixiu.view;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import com.remair.heixiu.R;
import java.util.ArrayList;


//import com.jakewharton.nineoldandroids.sample.R;
//import com.nineoldandroids.animation.ValueAnimator;
//import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidflakes.
 */
public class FlakeView extends View {

    private final Bitmap droidFlower;
    Bitmap droid1;       // The bitmap that all flakes use
    Bitmap droid2;
    Bitmap droid3;
    Bitmap droid4;
    int numFlakes = 0;  // Current number of flakes
    ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes
    ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    // Animator used to drive all separate flake animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all flakes for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and fps
    int frames = 0;     // Used to track frames per second
    Paint textPaint;    // Used for rendering fps text
    float fps = 0;      // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake during rendering
    String fpsString = "";
    String numFlakesString = "";

    /**
     * Constructor. Create objects used throughout the life of the View: the Paint and
     * the animator
     */
    public FlakeView(Context context) {
        super(context);
        droid1 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_1);
        droid2 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_2);
        droid3 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_3);
        droid4 = BitmapFactory.decodeResource(getResources(), R.drawable.icon_4);
        droidFlower = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
        bitmaps.add(droid1);
        bitmaps.add(droid2);
        bitmaps.add(droid3);
        bitmaps.add(droid4);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);

        // This listener is where the action is for the flak animations. Every frame of the
        // animation, we calculate the elapsed time and update every flake's position and rotation
        // according to its speed.
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += (flake.speed * secs);
                    if (flake.y > getHeight()) {
                        // If a flake falls off the bottom, send it back to the top
                        flake.y = 0 - flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                // Force a redraw to see the flakes in their new positions and orientations
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
    }

    public int getNumFlakes() {
//        if (numFlakes > 75) {
//            return 75;
//        }
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    /**
     * Add the specified number of droidflakes.
     */
    public void addFlakes(int quantity) {
        if (numFlakes % 30 == 0)//���ﶨ�廨��������Լ���ɫ��ʽ
        {
            flakes.add(Flake.createFlakeBig(getWidth(), droidFlower, false));
        }
        for (int i = 0; i < quantity; ++i) {
            if (i % 20 == 0)
                flakes.add(Flake.createFlake(getWidth(), droid1));
            else if (i % 20 == 1)
                flakes.add(Flake.createFlake(getWidth(), droid2));
            else if (i % 20 == 2)
                flakes.add(Flake.createFlake(getWidth(), droid1));
            else if (i % 20 == 3)
                flakes.add(Flake.createFlake(getWidth(), droid2));
            else if (i % 20 == 4)
                flakes.add(Flake.createFlake(getWidth(), droid1));
            else if (i % 20 == 5)
                flakes.add(Flake.createFlake(getWidth(), droid3));
            else if (i % 20 == 6)
                flakes.add(Flake.createFlake(getWidth(), droid4));
           /* else if (i % 20 == 7)
                flakes.add(Flake.createFlake(getWidth(), droid8));
            else if (i % 20 == 8)
                flakes.add(Flake.createFlake(getWidth(), droid9));
            else if (i % 20 == 9)
                flakes.add(Flake.createFlake(getWidth(), droid10));
            else if (i % 20 == 10)
                flakes.add(Flake.createFlake(getWidth(), droid11));
            else if (i % 20 == 11)
                flakes.add(Flake.createFlake(getWidth(), droid12));
            else if (i % 20 == 12)
                flakes.add(Flake.createFlake(getWidth(), droid13));
            else if (i % 20 == 13)
                flakes.add(Flake.createFlake(getWidth(), droid14));
            else if (i % 20 == 14)
                flakes.add(Flake.createFlake(getWidth(), droid15));
            else if (i % 20 == 15)
                flakes.add(Flake.createFlake(getWidth(), droid16));
            else if (i % 20 == 16)
                flakes.add(Flake.createFlake(getWidth(), droid17));
            else if (i % 20 == 17)
                flakes.add(Flake.createFlake(getWidth(), droid18));
            else if (i % 20 == 18)
                flakes.add(Flake.createFlake(getWidth(), droid19));
            else if (i % 20 == 19)
                flakes.add(Flake.createFlake(getWidth(), droid20));
*/
        }
        setNumFlakes(numFlakes + quantity);
    }

    /**
     * Subtract the specified number of droidflakes. We just take them off the end of the
     * list, leaving the others unchanged.
     */
    public void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            if (index < 0 || index >= numFlakes) {
                return;
            }
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidflakes, then restart it with 8 flakes
        flakes.clear();
        numFlakes = 0;
        addFlakes(0);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to rotate around its center),
        // rotate by its current rotation, translate by its location, then draw its bitmap
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width / 2, -flake.height / 2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        // fps counter: count how many frames we draw and once a second calculate the
        // frames per second
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = deltaTime / 1000f;
            fps = frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
        //pfs���ܵĻ�������

//        canvas.drawText(numFlakesString, getWidth() - 200, getHeight() - 50, textPaint);
//        canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);
    }

    public void pause() {
        // Make sure the animator's not spinning in the background when the activity is paused.
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

//    public void stop() {
//        for (int i = 0; i < bitmaps.size(); i++) {
//            bitmaps.get(i).recycle();
//        }
//    }

}
