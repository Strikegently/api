package org.powbot.widgetkit.border;

import org.powbot.widgetkit.WKComponent;

import java.awt.*;

/**
 * Abstract border class
 * @author tommo
 *
 */
public abstract class WKBorder {

    public WKBorder() {

    }

    public abstract void paint(WKComponent component, Graphics2D graphics);

}
