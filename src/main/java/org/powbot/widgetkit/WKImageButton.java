package org.powbot.widgetkit;

import org.powbot.widgetkit.layout.WKFlowLayout;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A WigetKit component representing an image as a button
 *
 * @author tommo
 */
public class WKImageButton extends WKContainer {
    private final WKLabel caption = new WKLabel();
    private final WKImage image;

    public WKImageButton(BufferedImage image, String text) {
        setSize(new Dimension(image.getWidth() + (text.length() * 7), image.getHeight()));
        setLayout(new WKFlowLayout(5, 0));
        this.image = new WKImage(image);
        this.caption.setText(text);
        this.caption.setSize(new Dimension(0, image.getHeight() + 10));
        add(this.image);
        add(this.caption);
    }

    public WKImageButton(BufferedImage image) {
        this(image, "");
    }

    public WKImage getImage() {
        return image;
    }
}
