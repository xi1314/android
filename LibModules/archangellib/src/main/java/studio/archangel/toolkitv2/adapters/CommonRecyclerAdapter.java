package studio.archangel.toolkitv2.adapters;

/**
 * Created by Administrator on 2015/11/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import studio.archangel.toolkitv2.util.text.Notifier;
import studio.archangel.toolkitv2.viewholders.AngelCommonViewHolder;

public abstract class CommonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_HEADER = -1;
    protected static final int TYPE_ITEM = 0;
    Context context;
    View header;
    protected List items;
    protected int[] item_layout;
    boolean multi_type = false;
    RecyclerView owner;
    OnItemClickListener listener;
    OnItemToClickListener toClickListener;
    public OnItemLongClickListener longlistener;

    public CommonRecyclerAdapter() {

    }

    public CommonRecyclerAdapter(Context c, int layout_id, List i) {
        this(c, layout_id, null, i);
    }

    public CommonRecyclerAdapter(Context c, int layout_id, View header, List i) {
        this(c, new int[]{layout_id}, header, i, false);
    }

    public CommonRecyclerAdapter(Context c, int[] layout_ids, View header, List i, boolean m) {
        multi_type = m;
        context = c;
        this.header = header;
        this.items = i;
        item_layout = layout_ids;
    }

    protected boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    public boolean hasHeader() {
        return header != null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            if (header != null) {
                attachListenerFor(header);
            }
            return onCreateHeaderVH(header);
        }
        if (!multi_type) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(item_layout[0], parent, false);
            attachListenerFor(view);
            return onCreateItemVH(view);
        } else {
            try {
                return onCreateTypedItemVH(parent, viewType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    void attachListenerFor(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (owner != null) {
                        listener.onItemClick(owner.indexOfChild(view));
                    } else {
                        Notifier.showNormalMsg(context, "adapter's owner not set.");
                    }
                }
            }
        });


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            return;
        }
        ((AngelCommonViewHolder) holder).render(context, items.get(position - (hasHeader() ? 1 : 0)));
        holder.itemView.setTag(items.get(position - (hasHeader() ? 1 : 0)));
        if (toClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (toClickListener != null)
                        toClickListener.onItemClick(holder.itemView, holder.itemView.getTag());
                }
            });
        }

        if (longlistener != null) {
            //longclickListener
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int LayoutPosition = holder.getLayoutPosition(); //得到布局的position
                    longlistener.OnItemLongClick(LayoutPosition, holder.itemView, holder.itemView.getTag());
                    return false;
                }
            });
        }

    }


    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items.size() + (hasHeader() ? 1 : 0);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemToClickListener(OnItemToClickListener listener) {

        this.toClickListener = listener;

    }

    public void setOnItemLongClickListener(OnItemLongClickListener longlistener) {

        this.longlistener = longlistener;

    }

    public void setRecyclerView(RecyclerView owner) {
        this.owner = owner;
    }

    protected abstract RecyclerView.ViewHolder onCreateHeaderVH(View v);

    protected abstract RecyclerView.ViewHolder onCreateItemVH(View v);

    protected RecyclerView.ViewHolder onCreateTypedItemVH(ViewGroup context, int type) throws Exception {
        if (multi_type) {
            throw new Exception("you should override method \"onCreateTypedItemVH\" to use multi-type adapter.");
        }
        return null;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public interface OnItemToClickListener {

        void onItemClick(View view, Object data);

    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(int position, View view, Object data);
    }
}