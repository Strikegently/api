package org.powbot.widgetkit.layout;

import org.powbot.widgetkit.WKComponent;
import org.powbot.widgetkit.WKContainer;
import org.powbot.widgetkit.WKLayout;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rvbiljouw
 */
public class WKFlowLayout implements WKLayout {
    private int horizontalPadding = 5;
    private int verticalPadding = 5;

    public WKFlowLayout(int horizontalPadding, int verticalPadding) {
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
    }


    public WKFlowLayout() {

    }

    @Override
    public void performLayout(WKContainer container) {
        List<WKComponent> row = new ArrayList<>();
        int x = 0;
        int y = 0;
        Dimension containerSize = container.getSize();
        for (WKComponent component : container.getComponents()) {
            Dimension size = component.getSize();
            if (x + (size.width + horizontalPadding) <= containerSize.getWidth()) {
                component.setLocation(new Point(x, y));
                x += size.width + horizontalPadding;
                row.add(component);
            } else {
                x = 0;
                y += getRowHeight(row) + verticalPadding;
                row.clear();

                component.setLocation(new Point(x, y));
                x += (size.width + horizontalPadding);
                row.add(component);
            }
        }
    }

    private int getRowHeight(List<WKComponent> row) {
        int maxHeight = 0;
        for(WKComponent cell : row) {
            if(cell.getSize().height > maxHeight) {
                maxHeight = cell.getSize().height;
            }
        }
        return maxHeight;
    }

    public int getHorizontalPadding() {
        return horizontalPadding;
    }

    public int getVerticalPadding() {
        return verticalPadding;
    }
}
