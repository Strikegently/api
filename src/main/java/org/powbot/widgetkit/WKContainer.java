package org.powbot.widgetkit;

import org.powbot.widgetkit.util.DepthSortComparator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.*;
import java.util.List;

/**
 * A WidgetKit container
 *
 * @author rvbiljouw
 */
public class WKContainer extends WKComponent {

    /**
     * A set containing all components which the mouse is currently inside.
     * Note that a stack would be ideal here, but since input events aren't fired
     * from biggest to smallest size in component hierarchies we can't ever guarantee
     * the component on the top of the stack is the the 'foremost' component.
     */
    private Set<WKComponent> hoverSet = new HashSet<WKComponent>();

    /**
     * List of components in this container.
     * The reason it's a linked list as opposed to an array list is because when depth sorting
     * components, if they overlap with no obvious foremost component, it will resort to using the latter component
     * in this list which is _usually_ the later added component.
     */
    private final List<WKComponent> components = new LinkedList<>();

    private DepthSortComparator depthSortComparator = new DepthSortComparator(components);

    private Insets insets = new Insets(0, 0, 0, 0);

    private WKLayout layout;


    @Override
    public void paint(Graphics2D g) {
        super.paint(g);

        for (WKComponent component : components) {
            if (!component.isVisible()) {
                continue;
            }

            component.paint(g);
        }
    }

    /**
     * Paints this container with clip bounds
     *
     * @param g    The graphics object
     * @param clip The clip bounds
     */
    @Override
    public void paint(Graphics2D g, Rectangle clip) {
        super.paint(g, clip);
        for (WKComponent component : components) {
            if (!component.isVisible()) {
                continue;
            }
            Rectangle rect = component.getBounds();
            if (clip.intersects(rect) || clip.contains(rect)) {
                component.paint(g);
            }
        }
    }

    @Override
    public void fireMouseOnPressEvent(MouseEvent press) {
        super.fireMouseOnPressEvent(press);
        for (WKComponent c : getComponentsAt(press.getPoint())) {
            c.fireMouseOnPressEvent(press);
        }
    }

    @Override
    public void fireMouseOnDragEvent(MouseEvent drag) {
        super.fireMouseOnDragEvent(drag);
        for (WKComponent c : getComponentsAt(drag.getPoint())) {
            c.fireMouseOnDragEvent(drag);
        }
    }

    @Override
    public void fireMouseOnReleaseEvent(MouseEvent release) {
        super.fireMouseOnReleaseEvent(release);
        for (WKComponent c : getComponentsAt(release.getPoint())) {
            c.fireMouseOnReleaseEvent(release);
        }
    }

    @Override
    public void fireMouseOnScrollEvent(MouseWheelEvent scroll) {
        super.fireMouseOnScrollEvent(scroll);
        for (WKComponent c : getComponentsAt(scroll.getPoint())) {
            c.fireMouseOnScrollEvent(scroll);
        }
    }

    @Override
    public void fireMouseOnClickEvent(MouseEvent click) {
        super.fireMouseOnClickEvent(click);

        clearFocus();

        for (WKComponent c : getComponentsAt(click.getPoint())) {
            c.fireMouseOnClickEvent(click);
        }

        WKComponent foremost = getComponentAt(click.getPoint());
        if (foremost != null) {
            foremost.setFocused(true);
        }
    }

    @Override
    public void fireMouseOnHoverEvent(MouseEvent hover) {
        super.fireMouseOnHoverEvent(hover);
        for (WKComponent component : components) {
            Point location = component.getLocation();
            Dimension size = component.getSize();
            Rectangle bounds = new Rectangle(
                    location.x, location.y,
                    size.width, size.height);
            boolean contains = bounds.contains(hover.getPoint());
            boolean inSet = hoverSet.contains(component);
            if (contains && inSet) {
                component.fireMouseOnHoverEvent(hover);
            } else if (contains && !inSet) {
                component.fireMouseOnEnterEvent(hover);
                hoverSet.add(component);
            } else if (!contains && inSet) {
                component.fireMouseOnLeaveEvent(hover);
                hoverSet.remove(component);
            }
        }
    }

    @Override
    public void fireKeyOnTypedEvent(KeyEvent event) {
        super.fireKeyOnTypedEvent(event);
        for (WKComponent component : components) {
            if (component.hasFocus()) {
                component.fireKeyOnTypedEvent(event);
            }
        }
    }

    public void add(WKComponent component) {
        component.setVisible(true);
        component.setParent(this);
        components.add(component);
        if (layout != null) {
            layout.performLayout(this);
        }
    }

    public void remove(WKComponent component) {
        component.setVisible(false);
        component.setParent(null);
        components.remove(component);
        if (layout != null) {
            layout.performLayout(this);
        }
    }

    /**
     * Checks if this container has a specific component
     *
     * @param component The component to check
     * @return true if this container has a component which is <i>equal</i> to the specified component
     */
    public boolean contains(WKComponent component) {
        for (WKComponent c : components) {
            if (c.equals(component)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of components this container contains
     *
     * @return The list of components
     */
    public List<WKComponent> getComponents() {
        return components;
    }

    /**
     * Returns a list of all components which contain the specified point
     *
     * @param point The point
     * @return List of components
     */
    public List<WKComponent> getComponentsAt(Point point) {
        List<WKComponent> list = new ArrayList<WKComponent>();
        for (WKComponent c : components) {
            Point location = c.getLocation();
            Dimension size = c.getSize();
            Rectangle bounds = new Rectangle(
                    location.x, location.y,
                    size.width, size.height);
            if (bounds.contains(point)) {
                list.add(c);
            }
        }

        return list;
    }

    /**
     * Attempts to lookup the foremost component at a given point
     *
     * @param point The point to lookup the component at
     * @return The foremost component, or null if inexistent
     */
    public WKComponent getComponentAt(Point point) {
        List<WKComponent> sorted = new ArrayList<>();
        sorted.addAll(components);
        Collections.sort(components, depthSortComparator);

        if (sorted.size() == 0) {
            return null;
        }

        return sorted.get(sorted.size() - 1);
    }

    /**
     * Recursively clears all of this container's components of any focus flags
     */
    @Override
    public void clearFocus() {
        super.clearFocus();
        for (WKComponent c : components) {
            c.clearFocus();
        }
    }

    public WKLayout getLayout() {
        return layout;
    }

    public void setLayout(WKLayout layout) {
        this.layout = layout;
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }
}
