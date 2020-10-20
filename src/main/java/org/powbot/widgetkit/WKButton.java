package org.powbot.widgetkit;

import java.awt.Color;
import java.awt.Graphics2D;

/**                                        \
 * A WidgetKit button.
 * @author rvbiljouw
 */
public class WKButton extends WKContainer {

    private WKLabel label;
    private Color hover;

    public WKButton() {
        this("");
    }

    public WKButton(String text) {
        this.label = new WKLabel(text);
        add(label);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
    }

}
