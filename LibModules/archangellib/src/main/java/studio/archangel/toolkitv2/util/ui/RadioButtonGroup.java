package studio.archangel.toolkitv2.util.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import studio.archangel.toolkitv2.views.AngelRadioButton;

/**
 * Created by Michael on 2015/7/29.
 */
public class RadioButtonGroup {
    LinkedList<AngelRadioButton> buttons;

    public RadioButtonGroup() {
        buttons = new LinkedList<>();
    }

    public void setButtons(AngelRadioButton... buttons) {

        for (AngelRadioButton button : buttons) {
            this.buttons.add(button);
            button.setButtonGroup(this);
        }
    }

    public void setButtons(ArrayList<AngelRadioButton> buttons) {
        for (AngelRadioButton button : buttons) {
            this.buttons.add(button);
            button.setButtonGroup(this);
        }
    }
    //
//    public void addButton(AngelRadioButton button) {
//        if (!buttons.contains(button)) {
//            buttons.add(button);
//        }


//    }

    public void refreshButtonStatus(AngelRadioButton button_checked) {
        for (AngelRadioButton button : buttons) {
            if (button_checked != button) {
                button.setChecked(false);
            }
        }
    }
}
