package studio.archangel.toolkitv2.models;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;

/**
 * Created by Michael on 2015/2/5.
 */
public abstract class AnimationController implements ValueAnimator.AnimatorUpdateListener {
    IntEvaluator evaluator = new IntEvaluator();
    Actor actor;

    public AnimationController(Actor a) {
        actor = a;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animator) {
//        int currentValue = (Integer) animator.getAnimatedValue();
//        Log.d(TAG, current value:  + currentValue);

        //计算当前进度占整个动画过程的比例，浮点型，0-1之间
        float fraction = animator.getAnimatedFraction();
        processMovement(actor, fraction);
        //这里我偷懒了，不过有现成的干吗不用呢
        //直接调用整型估值器通过比例计算出宽度，然后再设给Button
//        target.getLayoutParams().width = mEvaluator.evaluate(fraction, start, end);
//        target.requestLayout();
    }

    public abstract void processMovement(Actor actor, float fraction);
}
