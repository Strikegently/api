package org.powbot.widgetkit;

import org.powbot.widgetkit.border.WKBorder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for WidgetKit components
 *
 * @author rvbiljouw
 */
public abstract class WKComponent {
    private final List<WKMouseListener> mouseListeners = new ArrayList<>();
    private final List<WKKeyListener> keyListeners = new ArrayList<>();
    private WKContainer parent;
    private WKBorder border;
    private Point location = new Point(0, 0);
    private Dimension size = new Dimension(1, 1);
    private boolean visible = false;
    private Color background = new Color(0f, 0f, 0f, 0f);
    private boolean focused = false;

    public WKComponent() {

    }

    public void paint(Graphics2D g) {
        g.setColor(background);
        g.fillRect(location.x, location.y, size.width, size.height);
        if (border != null) {
            border.paint(this, g);
        }
    }

    /**
     * Paints this component only if inside clip bounds
     *
     * @param g    The graphics object
     * @param clip The clip bounds
     */
    public void paint(Graphics2D g, Rectangle clip) {
        Rectangle rect = getBounds();
        if (clip.intersects(rect) || clip.contains(rect)) {
            paint(g);
        }
    }

    public void fireMouseOnClickEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onClick(event);
        }
    }

    public void fireMouseOnEnterEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onEnter(event);
        }
    }

    public void fireMouseOnScrollEvent(MouseWheelEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onScroll(event);
        }
    }

    public void fireMouseOnLeaveEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onLeave(event);
        }
    }

    public void fireMouseOnHoverEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onHover(event);
        }
    }

    public void fireMouseOnPressEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onPress(event);
        }
    }

    public void fireMouseOnReleaseEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onRelease(event);
        }
    }

    public void fireMouseOnDragEvent(MouseEvent event) {
        for (WKMouseListener listener : mouseListeners) {
            listener.onDrag(event);
        }
    }

    public void fireKeyOnTypedEvent(KeyEvent event) {
        for (WKKeyListener listener : keyListeners) {
            listener.onKeyTyped(event);
        }
    }

    public WKBorder getBorder() {
        return border;
    }

    public void setBorder(WKBorder border) {
        this.border = border;
    }

    /**
     * Gets the location of the component
     *
     * @return component
     */
    public Point getLocation() {
        WKContainer parent = getParent();
        if (parent != null) {
            Point offset = parent.getLocation();
            Insets insets = parent.getInsets();
            return new Point(location.x + offset.x + insets.left,
                    location.y + offset.y + insets.top);
        }
        return location;
    }

    /**
     * Sets the location of the component
     *
     * @param location location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Gets the size of the component in px
     *
     * @return size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Sets the size of the component in px
     *
     * @param size size in px
     */
    public void setSize(Dimension size) {
        this.size = size;
    }

    /**
     * Sets the size bounds of the component in px
     *
     * @param x      The x location
     * @param y      The y location
     * @param width  The width of the component
     * @param height The height of the component
     */
    public void setBounds(int x, int y, int width, int height) {
        location.setLocation(x, y);
        size.setSize(width, height);
    }

    /**
     * Sets the size bounds
     *
     * @param bounds The bounds rectangle
     */
    public void setBounds(Rectangle bounds) {
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Returns a rectangle representing the bounds of this component
     *
     * @return The bounds rectangle
     */
    public Rectangle getBounds() {
        Point location = getLocation();
        Dimension size = getSize();
        return new Rectangle(location.x, location.y, size.width, size.height);
    }

    /**
     * Check to see if the component is visible.
     *
     * @return visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets a flag indicating the visibility of the component.
     *
     * @param visible visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the background color of the component
     *
     * @return background
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Sets the background color of the component
     *
     * @param background background color
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     * Adds a mouse listener to the component
     *
     * @param mouseListener listener
     */
    public void addMouseListener(WKMouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    /**
     * Removes a mouse listener from the component
     *
     * @param mouseListener listener
     */
    public void removeMouseListener(WKMouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    /**
     * Adds a key listener to the component.
     *
     * @param keyListener listener
     */
    public void addKeyListener(WKKeyListener keyListener) {
        keyListeners.add(keyListener);
    }

    /**
     * Removes a key listener from the component.
     *
     * @param keyListener listener
     */
    public void removeKeyListener(WKKeyListener keyListener) {
        keyListeners.remove(keyListener);
    }

    /**
     * Clears this component of any focus flags
     */
    public void clearFocus() {
        setFocused(false);
    }

    /**
     * Checks if this component currently has focus
     *
     * @return true if this component has focus, false if not
     */
    public boolean hasFocus() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public WKContainer getParent() {
        return parent;
    }

    public void setParent(WKContainer parent) {
        this.parent = parent;
    }
}
