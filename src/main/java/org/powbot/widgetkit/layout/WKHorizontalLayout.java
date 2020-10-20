package org.powbot.widgetkit.layout;

import org.powbot.widgetkit.WKComponent;
import org.powbot.widgetkit.WKContainer;
import org.powbot.widgetkit.WKLayout;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class WKHorizontalLayout implements WKLayout {
    @Override
    public void performLayout(WKContainer container) {
        int y = 0;
        for(WKComponent component : container.getComponents()) {
            component.setLocation(new Point(component.getLocation().x, y));
            y += component.getSize().height;
        }
    }
}
