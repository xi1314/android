//package studio.archangel.toolkitv2.views;
//
//import android.content.Context;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.text.InputFilter;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import net.simonvt.numberpicker.NumberPicker;
//
//import java.lang.reflect.Field;
//
//import studio.archangel.toolkitv2.R;
//
///**
// * Created by Michael on 2015/8/7.
// */
//public class AngelNumberPickerDialog extends AngelDialog {
//
//    NumberPicker np_picker;
//    TextView tv_text;
//    OnNumberPickedListener listener;
//    NumberPicker.Formatter formatter;
//
//    public AngelNumberPickerDialog(Context context, String title, int color) {
//        super(context, title, "", color);
//        formatter = new NumberPicker.Formatter() {
//            @Override
//            public String format(int value) {
//                return value + "";
//            }
//        };
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        View custom = getLayoutInflater().inflate(R.layout.dialog_numberpicker, null);
//        np_picker = (NumberPicker) custom.findViewById(R.id.dialog_numberpicker_picker);
//        tv_text = (TextView) custom.findViewById(R.id.dialog_numberpicker_text);
//        setCustomView(custom);
//        setButtonStyle(AngelMaterialProperties.DialogStyle.ok_cancel);
//        setOnOkClickedListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onNumberPicked(formatter.format(np_picker.getValue()));
//                }
//            }
//        });
//
//        np_picker.setMaxValue(10);
//        np_picker.setMinValue(0);
//        np_picker.setFocusable(true);
//        np_picker.setFocusableInTouchMode(true);
//        np_picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        np_picker.setDivider(new ColorDrawable(main_color));
//        try {
//            Field f = NumberPicker.class.getDeclaredField("mInputText");
//            f.setAccessible(true);
//            EditText inputText = (EditText) f.get(np_picker);
//            inputText.setFilters(new InputFilter[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public TextView getTextView() {
//        return tv_text;
//    }
//
//    public void configPicker(int max, int min, int def, NumberPicker.Formatter formatter) {
//        np_picker.setMaxValue(max);
//        np_picker.setMinValue(min);
//        np_picker.setValue(def);
//        np_picker.setFormatter(formatter);
//        this.formatter = formatter;
//    }
//
//    public OnNumberPickedListener getOnNumberPickedListener() {
//        return listener;
//    }
//
//    public void setOnNumberPickedListener(OnNumberPickedListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnNumberPickedListener {
//        void onNumberPicked(String value);
//    }
//}
