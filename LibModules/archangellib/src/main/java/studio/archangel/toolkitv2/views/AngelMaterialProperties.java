package studio.archangel.toolkitv2.views;

/**
 * Created by Michael on 2015/5/12.
 */
public class AngelMaterialProperties {

    public static final EffectMode[] effect_mode_array = {EffectMode.dark, EffectMode.light};
    public static final TriggerMode[] trigger_mode_array = {TriggerMode.start, TriggerMode.end};
    public static final BorderStyle[] border_style_array = {BorderStyle.flat, BorderStyle.round_corner, BorderStyle.round};
    public static final DialogStyle[] dialog_style_array = {DialogStyle.ok, DialogStyle.ok_cancel, DialogStyle.ok_neutral_cancel};
    public static final FillStyle[] fill_style_array = {FillStyle.fill, FillStyle.border};
    public static final RedDotStyle[] reddot_style_array = {RedDotStyle.text, RedDotStyle.simple};

    public enum EffectMode {
        dark, light;
    }

    public enum TriggerMode {
        start, end;
    }

    public enum BorderStyle {
        flat, round_corner, round
    }

    public enum FillStyle {
        fill, border
    }

    public enum DialogStyle {
        ok, cancel, ok_cancel, ok_neutral_cancel
    }

    public enum TimePickerDialogStyle {
        hour_min_sec, hour_min,
    }

    public enum RedDotStyle {
        text, simple,
    }
}
