package studio.archangel.toolkitv2.views;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import studio.archangel.toolkitv2.R;

public class AngelMenuDialog extends android.app.Dialog {

    String message;
    TextView tv_msg;
    String title;
    TextView titleTextView;
    ListView list;
    AdapterView.OnItemClickListener listener;
    int main_color, res_list_item_layout, res_list_item_layout_text;
    String[] items;
//    Activity act;
//    FragmentActivity act_v4;

    public AngelMenuDialog(Context context, String title, String message) {
        super(context, R.style.AnimDialog);
        this.message = message;
        this.title = title;
//        if (context instanceof Activity) {
//            act = (Activity) context;
//        } else if (context instanceof FragmentActivity) {
//            act_v4 = (FragmentActivity) context;
//        }
    }

    public AngelMenuDialog(Context context, String title, String message, int color) {
        this(context, title, message);
        main_color = context.getResources().getColor(color);

    }

    public AngelMenuDialog(Context context, String title, String[] items, int res_list_item_layout, int res_list_item_layout_text, AdapterView.OnItemClickListener listener) {
        super(context, R.style.AnimDialog);
        this.title = title;
        this.items = items;
        this.res_list_item_layout = res_list_item_layout;
        this.res_list_item_layout_text = res_list_item_layout_text;
        this.listener = listener;
//        if (context instanceof Activity) {
//            act = (Activity) context;
//        } else if (context instanceof FragmentActivity) {
//            act_v4 = (FragmentActivity) context;
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.view_dialog_menu);
        this.titleTextView = (TextView) findViewById(R.id.view_dialog_menu_title);
        setTitle(title);
        this.tv_msg = (TextView) findViewById(R.id.view_dialog_menu_message);
        setMessage(message);
        list = (ListView) findViewById(R.id.items);
        if (items != null) {
            String[] added_items = new String[items.length + 1];
            System.arraycopy(items, 0, added_items, 0, items.length);
            added_items[items.length] = "取消";
            list.setAdapter(new ItemAdapter(this, getContext(), res_list_item_layout, res_list_item_layout_text, added_items, listener));
            list.setVisibility(View.VISIBLE);
            tv_msg.setVisibility(View.GONE);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        if (message == null || message.isEmpty())
            tv_msg.setVisibility(View.GONE);
        else {
            tv_msg.setVisibility(View.VISIBLE);
//            tv_msg.setText(message);
        }
        tv_msg.setText(message);
    }

    public String getTitle() {
        return title;
    }

//    @Override
//    public void show() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || (act == null && act_v4 == null)) {
//            super.show();
//            return;
//        }
//        //Here's the magic..
//        //Set the dialog to not focusable (makes navigation ignore us adding the window)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//
//        //Show the dialog!
//        super.show();
//
//        //Set the dialog to immersive
//        if (act != null) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    act.getWindow().getDecorView().getSystemUiVisibility());
//        } else if (act_v4 != null) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    act_v4.getWindow().getDecorView().getSystemUiVisibility());
//
//        }
//
//
//        //Clear the not focusable flag from the window
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
//    }

    public void setTitle(String title) {
        this.title = title;
        if (title == null || title.isEmpty())
            titleTextView.setVisibility(View.GONE);
        else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }

    class ItemAdapter extends ArrayAdapter<String> {
        int res_layout, res_button;
        LayoutInflater inflater;
        AdapterView.OnItemClickListener listener;
        AngelMenuDialog owner;
        String[] items;

        public ItemAdapter(AngelMenuDialog dialog, Context context, int resource, int textViewResourceId, String[] objects, AdapterView.OnItemClickListener onItemClickListener) {
            super(context, resource, textViewResourceId, objects);
            items = objects;
            res_layout = resource;
            res_button = textViewResourceId;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listener = onItemClickListener;
            owner = dialog;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return createViewFromResource(position, convertView, parent, res_layout);
        }

        private View createViewFromResource(final int position, View convertView, ViewGroup parent, int resource) {
            View view;
            AngelMaterialButton button;

            if (convertView == null) {
                view = inflater.inflate(resource, parent, false);
            } else {
                view = convertView;
            }

            try {
                button = (AngelMaterialButton) view.findViewById(res_button);
            } catch (ClassCastException e) {
                Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
                throw new IllegalStateException(
                        "ArrayAdapter requires the resource ID to be a TextView", e);
            }

            String item = getItem(position);
            button.setText(item);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//            button.setLayoutParams(params);
//            button.setPadding(Utils.dpToPx(24, getContext().getResources()), 0, 0, 0);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    owner.dismiss();
                    if (position != items.length - 1) {
                        listener.onItemClick(null, null, position, 0);
                    }

                }
            });


            return view;
        }
    }

}
