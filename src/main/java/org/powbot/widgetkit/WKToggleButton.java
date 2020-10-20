package org.powbot.widgetkit;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 */
public class WKToggleButton extends WKImageButton {
    private boolean selected = false;

    public WKToggleButton(String text) {
        super(getDeselectedIcon(), text);
        addMouseListener(new WKMouseAdapter() {
            @Override
            public void onClick(MouseEvent event) {
                setSelected(!selected);
            }
        });
    }

    private static BufferedImage getSelectedIcon() {
        BufferedImage image = new BufferedImage(12, 14, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.GREEN);
        graphics.fillRect(0, 0, 12, 14);
        return image;
    }

    private static BufferedImage getDeselectedIcon() {
        BufferedImage image = new BufferedImage(12, 14, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.RED);
        graphics.fillRect(0, 0, 12, 14);
        return image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        getImage().setImage(!selected ? getDeselectedIcon() : getSelectedIcon());
    }
}
