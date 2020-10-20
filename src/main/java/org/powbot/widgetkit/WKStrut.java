package org.powbot.widgetkit;

import org.powbot.widgetkit.layout.WKFlowLayout;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class WKStrut extends WKComponent {

    public WKStrut() {
        setBackground(Color.WHITE);
    }

    @Override
    public Dimension getSize() {
        WKContainer parent = getParent();
        if(parent != null) {
            int index = parent.getComponents().indexOf(this);
            int occupiedLeft = 0;
            int occupiedRight = 0;
            for(int i = 0; i < index; i++) {
                occupiedLeft += parent.getComponents().get(i).getSize().width;
            }
            for(int i = index + 1; i < parent.getComponents().size(); i++) {
                occupiedRight += parent.getComponents().get(i).getSize().width;
            }

            int subtract = parent.getInsets().left;
            if(parent.getLayout() instanceof WKFlowLayout) {
                subtract += ((WKFlowLayout)parent.getLayout()).getHorizontalPadding();
            }
            subtract += (occupiedLeft + occupiedRight);
            subtract *= 2;
            return new Dimension(parent.getSize().width - subtract, 20);
        }
        return new Dimension(0, 0);
    }

}
