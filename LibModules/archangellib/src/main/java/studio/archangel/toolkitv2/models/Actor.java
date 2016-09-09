package studio.archangel.toolkitv2.models;

import android.graphics.Bitmap;

/**
 * Created by Michael on 2015/2/5.
 */
public abstract class Actor {
    public int x, y, w, h;
    public int frame;
    public Track track;

    public Actor() {


    }

    public void init() {

    }

    public void start() {
    }

    public void stop() {
    }

    public abstract Bitmap getBitmap();

    public void move() {
        if (track != null) {
            track.moveAlongTrack();
        }
        if (frame == Integer.MAX_VALUE) {
            frame = 0;
        }
        frame++;
    }

    public interface Track {
        public void moveAlongTrack();
    }
}
