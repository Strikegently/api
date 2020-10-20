package org.powbot.widgetkit;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class WKSpacer extends WKComponent {

    public WKSpacer(int width, int height) {
        setSize(new Dimension(width, height));
        setBackground(new Color(0, 0, 0, 0));
    }

}
