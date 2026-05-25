package com.jatp.automation.drivers;

import com.jatp.core.model.Widget;
import org.eclipse.swtbot.swtbot.SWTbot;
import org.eclipse.swtbot.swtbot.widgets.SWTbotButton;
import java.util.logging.Logger;

/**
 * Driver for SWTBot automation.
 */
public class SwtBotDriver {
    private final SWTbot bot = new SWTbot();

    /**
     * Clicks a button using SWTBot.
     * @param widget The widget to click.
     * @return true if successful.
     */
    public boolean clickButton(Widget widget) {
        try {
            // In a real scenario, we'd use the widget's label or path to find it.
            SWTbotButton btn = bot.button(widget.label());
            btn.click();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enters text into a text field using SWTBot.
     * @param widget The target text field.
     * @param text The text to enter.
     * @return true if successful.
     */
    public boolean enterText(Widget widget, String text) {
        try {
            // Simple implementation using label
            bot.text(widget.label()).setText(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
