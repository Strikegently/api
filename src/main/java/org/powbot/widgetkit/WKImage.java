package org.powbot.widgetkit;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * A WidgetKit image component
 * @author tommo
 *
 */
public class WKImage extends WKComponent implements WKMouseListener {

    private BufferedImage image;
    private BufferedImage hoverImage;
    private BufferedImage pressedImage;

    public WKImage(BufferedImage image) {
        this.image = image;
        setSize(new Dimension(image.getWidth(), image.getHeight()));
        addMouseListener(this);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.drawImage(image, getLocation().x, getLocation().y, (int) getSize().getWidth(), (int) getSize().getHeight(), null);
    }

    @Override
    public void onEnter(MouseEvent event) {

    }

    @Override
    public void onHover(MouseEvent event) {

    }

    @Override
    public void onLeave(MouseEvent event) {

    }

    @Override
    public void onClick(MouseEvent event) {

    }

    @Override
    public void onPress(MouseEvent event) {

    }

    @Override
    public void onRelease(MouseEvent event) {

    }

    @Override
    public void onDrag(MouseEvent event) {

    }

    @Override
    public void onScroll(MouseWheelEvent event) {

    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
