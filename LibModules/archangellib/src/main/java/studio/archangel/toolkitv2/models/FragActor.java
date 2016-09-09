package studio.archangel.toolkitv2.models;

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

import studio.archangel.toolkitv2.util.Util;

/**
 * Created by Michael on 2015/2/5.
 */
public class FragActor extends Actor {
    public int color, color_darker;
    public Paint paint;
    Path path1, path2;
    Bitmap bitmap;
    float last_angle = 0;
    int last_hash = -1;
    Camera camera;
    //    Canvas canvas;

    int centerX, centerY;
    float delta_x, delta_y, delta_z;
    public float factor_x, factor_y, factor_z;
    int width, height;

    public FragActor(int c) {
        color = c;
        color_darker = Util.createDarkerColor(color);

        camera = new Camera();
        path1 = new Path();
        path1.moveTo(0, 0);
        path1.lineTo(30, 80);
        path1.lineTo(190, 110);
//        path1.lineTo(100, 0);
        path1.close();
        path2 = new Path();
        path2.moveTo(0, 0);
//        path2.lineTo(30, 80);
        path2.lineTo(190, 110);
        path2.lineTo(100, 0);
        path2.close();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        width = height = 400;
        centerX = width / 2;
        centerY = height / 2;
        movePath(190, 110);
        refreshBitmap();
    }

    void movePath(int x, int y) {
        Matrix matrix = new Matrix();
        matrix.setTranslate((width - x) / 2, (height - y) / 2);
//        matrix.setTranslate(x / 2, y / 2);
        path1.transform(matrix);
        path2.transform(matrix);
    }
//    public void setBitmap(Bitmap b) {
//        bitmap = b;
//    }

    public void rotate(float new_angle) {
        delta_x += 1 * factor_x;
        delta_y += 1 * factor_y;
        delta_z -= 1 * factor_z;
        if (delta_x == 360) {
            delta_x = 0;
        }
        if (delta_y == 360) {
            delta_y = 0;
        }
        if (delta_z == -360) {
            delta_z = 0;
        }
//        matrix = Util.getRotateMatrix(camera, deltaX/2, "x", centerX, centerY, matrix);
//        camera.save();
//        camera.rotateY(deltaX);
////        camera.rotateX(-deltaY);
////        camera.translate(0, 0, -centerX);
//        camera.getMatrix(matrix);
//        camera.restore();
//        // The center of rotation to the center of the picture, if not these two, the center of rotation is the point (0,0)
////        matrix.preTranslate(-centerX, -centerY);
////        matrix.postTranslate(centerX, centerY);
////        camera.save();
//        path1.transform(matrix);
//        path2.transform(matrix);

//        Matrix m = new Matrix();
//        RectF bounds = new RectF();
//        path.computeBounds(bounds, true);
//        m.postRotate(new_angle - last_angle, (bounds.right + bounds.left) / 2, (bounds.bottom + bounds.top) / 2);
//        path.transform(m);
        this.last_angle = new_angle;
    }

    void refreshBitmap() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Bitmap output = Bitmap.createBitmap(190, 110, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        Logger.out(deltaX);
        camera.save();
        camera.rotateX(-delta_x);
        camera.rotateY(-delta_y);
        camera.rotateZ(-delta_z);

        Matrix matrix = new Matrix();
        // R
        camera.getMatrix(matrix);

        // 恢复camera到“其在save时刻的状态”
        camera.restore();

        // T1
        matrix.preTranslate(-centerX, -centerY);
        // T2
        matrix.postTranslate(centerX, centerY);
//        Matrix matrix1 = Util.getRotateMatrix(camera, deltaX , "x", centerX, centerY);
//        Matrix matrix2 = Util.getRotateMatrix(camera, deltaY , "y", centerX, centerY);

        canvas.concat(matrix);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawPath(path1, paint);
        paint.setColor(color_darker);
        canvas.drawPath(path2, paint);
        paint.setColor(color);

    }

    @Override
    public Bitmap getBitmap() {
        int hash = ("w" + w + "h" + h + "angle" + last_angle).hashCode();
        if (last_hash != -1) {
            if (hash != last_hash) {
                refreshBitmap();
            }
        }
        last_hash = hash;
//        Bitmap output = bitmap;
//        Bitmap output = Bitmap.createBitmap(190, 110, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        canvas.drawARGB(0, 0, 0, 0);
//        canvas.drawPath(path, paint);
        return bitmap;
    }

}
