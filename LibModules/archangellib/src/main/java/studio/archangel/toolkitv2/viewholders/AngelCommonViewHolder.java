package studio.archangel.toolkitv2.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2015/11/16.
 */
public abstract class AngelCommonViewHolder extends RecyclerView.ViewHolder {
    public AngelCommonViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void render(Context context, Object model);

//    public void onClick(Context context, Object model) {
//
//    }
}
