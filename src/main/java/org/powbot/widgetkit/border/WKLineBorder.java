package org.powbot.widgetkit.border;

import org.powbot.widgetkit.WKComponent;

import java.awt.*;

/**
 * Basic line border implementation
 * @author tommo
 *
 */
public class WKLineBorder extends WKBorder {
    public static final int LEFT = 0;
    public static final int BOTTOM = 1;
    public static final int RIGHT = 2;
    public static final int TOP = 3;
    public static final int ALL = 4;

    private int thickness;
    private Color color;
    private int side = ALL;

    private WKLineBorder(int thickness, Color color, int side) {
        this.thickness = thickness;
        this.color = color;
        this.side = side;
    }

    private WKLineBorder(int thickness, Color color) {
        this.thickness = thickness;
        this.color = color;
    }

    public static WKLineBorder create(int thickness, Color color) {
        return new WKLineBorder(thickness, color);
    }

    public static WKLineBorder create(int thickness, Color color, int side) {
        return new WKLineBorder(thickness, color, side);
    }

    @Override
    public void paint(WKComponent component, Graphics2D graphics) {
        int x = component.getLocation().x;
        int y = component.getLocation().y;
        int w = component.getSize().width;
        int h = component.getSize().height;

        graphics.setColor(color);
        switch(side) {
            case 0:
                graphics.fillRect(x, y, thickness, h);
                break;

            case 1:
                graphics.fillRect(x, y + h - thickness, w, thickness);
                break;

            case 2:
                graphics.fillRect(x + w - thickness, y, thickness, h);
                break;

            case 3:
                graphics.fillRect(x, y, w, thickness);
                break;

            case 4:
                graphics.fillRect(x, y, w, thickness);
                graphics.fillRect(x + w - thickness, y, thickness, h);
                graphics.fillRect(x, y + h - thickness, w, thickness);
                graphics.fillRect(x, y, thickness, h);
                break;

        }
    }

}
