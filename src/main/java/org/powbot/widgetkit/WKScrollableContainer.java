package org.powbot.widgetkit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

/**
 * @author rvbiljouw
 * @author tommo
 */
public class WKScrollableContainer extends WKContainer {

    public static final Color SCROLLBAR_BACKGROUND_COLOR = Color.WHITE;
    public static final Color SCROLLBAR_COLOR = Color.GRAY;
    public static final int SCROLLBAR_WIDTH = 8;

    private WKScrollBar verticalScrollBar = new WKVerticalScrollBar();
    private WKScrollBar horizontalScrollBar = new WKHorizontalScrollBar();
    private BufferedImage componentBuffer;
    private double verticalRatio;
    private double horizontalRatio;

    public WKScrollableContainer() {
        componentBuffer = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
        add(verticalScrollBar);
        add(horizontalScrollBar);
        addMouseListener(new WKMouseAdapter() {
            @Override
            public void onScroll(MouseWheelEvent event) {
                setScrollValues(horizontalScrollBar.getOffset(), verticalScrollBar.getOffset() - (event.getWheelRotation() * 5));
            }
        });
    }

    @Override
    public void paint(Graphics2D g) {
        /*
         * First pass we must find out the max size of the buffer from all components
         */
        int w = (int) getSize().getWidth();
        int h = (int) getSize().getHeight();
        int maxX = 0;
        int maxY = 0;
        for (WKComponent component : getComponents()) {
            if (component instanceof WKScrollBar) continue;
            if (component.getLocation().x + component.getSize().width > maxX) {
                maxX = component.getLocation().x + component.getSize().width;
            }
            if (component.getLocation().y + component.getSize().height > maxY) {
                maxY = component.getLocation().y + component.getSize().height;
            }
        }

        verticalRatio = (double) h / maxY;
        horizontalRatio = (double) w / maxX;

        /*
         * Create the buffer (this should ideally be cached and only resized if needed)
         * But doing it this way also means we dont have to clear the buffer every paint
         * so kill 2 faggots with 1 stone jajajaja
         */
        componentBuffer = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);


        int dx1 = getLocation().x;
        int dy1 = getLocation().y;
        int dx2 = getLocation().x + w;
        int dy2 = getLocation().y + h;
        Rectangle view = getViewport();

        /*
         * Make sure we don't render the scroll bar because it must be on top
         * derp
         * fuckin
         * derp
         */
        for (WKComponent component : getComponents()) {
            if (component instanceof WKScrollBar) continue;
            component.paint((Graphics2D) componentBuffer.getGraphics(), view);
        }

        g.drawImage(componentBuffer, dx1, dy1, dx2, dy2, view.x, view.y, view.x + view.width, view.y + view.height, null);

        /*
         * Finally, we draw the scroll bars on top
         */
        verticalScrollBar.setMaxOffset(h - (int) (h * verticalRatio));
        verticalScrollBar.setBounds(dx2 - SCROLLBAR_WIDTH, dy1, SCROLLBAR_WIDTH, h);
        verticalScrollBar.setBarSize((int) (verticalScrollBar.getSize().getHeight() * verticalRatio));
        decisiveDrawScrollBar(verticalScrollBar, g, verticalRatio);

        horizontalScrollBar.setMaxOffset(w - (int) (w * horizontalRatio));
        horizontalScrollBar.setBounds(dx1, dy2 - SCROLLBAR_WIDTH, w - SCROLLBAR_WIDTH, SCROLLBAR_WIDTH);
        horizontalScrollBar.setBarSize((int) (horizontalScrollBar.getSize().getWidth() * horizontalRatio));
        decisiveDrawScrollBar(horizontalScrollBar, g, horizontalRatio);
    }

    /**
     * Decisively draws the scroll bar if the mode is ALWAYS and/or ONLY_WHEN_NEEDED and scrolling is necessary
     * @param scrollBar
     * @param g
     * @param ratio
     */
    private void decisiveDrawScrollBar(WKScrollBar scrollBar, Graphics2D g, double ratio) {
        if (scrollBar.getMode().equals(WKScrollBarMode.ALWAYS)
                || (scrollBar.getMode().equals(WKScrollBarMode.ONLY_WHEN_NEEDED) && ratio < 1D)) {
            scrollBar.paint(g);
        }
    }

    /**
     * Returns a rectangle representing the current viewport
     * of the inner container
     * @return
     */
    public Rectangle getViewport() {
        int x1 = (int) (horizontalScrollBar.getOffset() * (1 / horizontalRatio));
        int y1 = (int) (componentBuffer.getHeight() - getSize().height - (verticalScrollBar.getOffset() * (1 / verticalRatio)));
        return new Rectangle(x1, y1, getSize().width, getSize().height);
    }

    /**
     * Sets the scroll offsets for the horizontal and vertical scroll bars
     * @param x The x offset from the left
     * @param y The y offset from the bottom
     */
    public void setScrollValues(int x, int y) {
        if (y < 0) y = 0;
        if (y > verticalScrollBar.getMaxOffset()) y = verticalScrollBar.getMaxOffset();

        if (x < 0) x = 0;
        if (x > horizontalScrollBar.getMaxOffset()) x = horizontalScrollBar.getMaxOffset();

        verticalScrollBar.setOffset(y);
        horizontalScrollBar.setOffset(x);
    }

    /**
     * Projects a point inside this scrollable container
     * in regards to horizontal and vertical scrolling
     * @param event The mouse event
     * @return The projected point
     */
    public MouseEvent project(MouseEvent event) {
        if (!verticalScrollBar.getBounds().contains(event.getPoint())) {
            event.translatePoint(0, getViewport().y);
        }
        return event;
    }

    @Override
    public void fireMouseOnPressEvent(MouseEvent press) {
        if (verticalScrollBar.getBounds().contains(press.getPoint())) {
            verticalScrollBar.setCaptureState(new ScrollBarCaptureState(verticalScrollBar.getOffset(), press.getPoint()));
        } else if (horizontalScrollBar.getBounds().contains(press.getPoint())) {
            horizontalScrollBar.setCaptureState(new ScrollBarCaptureState(horizontalScrollBar.getOffset(), press.getPoint()));
        }
        super.fireMouseOnPressEvent(project(press));
    }

    @Override
    public void fireMouseOnDragEvent(MouseEvent drag) {
        int x = horizontalScrollBar.getOffset();
        int y = verticalScrollBar.getOffset();

        if (verticalScrollBar.getCaptureState() != null) {
            y = verticalScrollBar.getCaptureState().initialOffset + (verticalScrollBar.getCaptureState().initialPoint.y - drag.getPoint().y);
        }
        if (horizontalScrollBar.getCaptureState() != null) {
            x = horizontalScrollBar.getCaptureState().initialOffset + (drag.getPoint().x - horizontalScrollBar.getCaptureState().initialPoint.x);
        }

        setScrollValues(x, y);
        super.fireMouseOnDragEvent(project(drag));
    }

    @Override
    public void fireMouseOnReleaseEvent(MouseEvent release) {
        verticalScrollBar.setCaptureState(null);
        horizontalScrollBar.setCaptureState(null);
        super.fireMouseOnReleaseEvent(project(release));
    }

    @Override
    public void fireMouseOnClickEvent(MouseEvent click) {
        super.fireMouseOnClickEvent(project(click));
    }

    @Override
    public void fireMouseOnHoverEvent(MouseEvent hover) {
        super.fireMouseOnHoverEvent(project(hover));
    }

    public WKScrollBar getHorizontalScrollBar() {
        return horizontalScrollBar;
    }

    public WKScrollBar getVerticalScrollBar() {
        return verticalScrollBar;
    }

    /**
     * Represents a scrolling capture state of a scrollbar
     * @author tommo
     *
     */
    private class ScrollBarCaptureState {

        private int initialOffset;
        private Point initialPoint;

        public ScrollBarCaptureState(int initialOffset, Point initialPoint) {
            this.initialOffset = initialOffset;
            this.initialPoint = initialPoint;
        }

    }

    /**
     * Scrollbar availability modes
     * @author tommo
     *
     */
    public static enum WKScrollBarMode {
        NEVER, ONLY_WHEN_NEEDED, ALWAYS
    }

    /**
     * Scroll bar component
     * @author tommo
     *
     */
    public abstract class WKScrollBar extends WKComponent {

        /**
         * The pixel offset from origin (vertical = bottom origin, horizontal = left origin)
         */
        private int offset = 0;

        /**
         * Maximum offset from origin
         */
        private int maxOffset = 0;

        /**
         * Bar size in pixels
         */
        private int barSize;

        /**
         * The capture state of when the scrollbar started dragging
         */
        private ScrollBarCaptureState captureState = null;

        private Color barColor = SCROLLBAR_COLOR;

        private WKScrollBarMode mode = WKScrollBarMode.ONLY_WHEN_NEEDED;

        public WKScrollBar() {
            setBackground(SCROLLBAR_BACKGROUND_COLOR);
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getBarSize() {
            return barSize;
        }

        public void setBarSize(int barSize) {
            this.barSize = barSize;
        }

        /**
         * Returns the colour of the scroll bar itself
         * @return
         */
        public Color getBarColor() {
            return barColor;
        }

        public void setBarColor(Color barColor) {
            this.barColor = barColor;
        }

        public int getMaxOffset() {
            return maxOffset;
        }

        public void setMaxOffset(int maxOffset) {
            this.maxOffset = maxOffset;
        }

        public ScrollBarCaptureState getCaptureState() {
            return captureState;
        }

        public void setCaptureState(ScrollBarCaptureState captureState) {
            this.captureState = captureState;
        }

        public WKScrollBarMode getMode() {
            return mode;
        }

        public void setMode(WKScrollBarMode mode) {
            this.mode = mode;
        }

    }

    /**
     * Vertical scroll bar implementation
     * @author tommo
     *
     */
    private class WKVerticalScrollBar extends WKScrollBar {

        @Override
        public void paint(Graphics2D g) {
            int w = (int) getSize().getWidth();
            int h = (int) getSize().getHeight();

            g.setColor(getBackground());
            g.fillRect(getLocation().x, getLocation().y, w, h);

            g.setColor(getBarColor());
            g.fillRect(getLocation().x, getLocation().y + h - getOffset() - getBarSize(), w, getBarSize());
        }

    }

    /**
     * Horizontal scroll bar implementation
     * @author tommo
     *
     */
    private class WKHorizontalScrollBar extends WKScrollBar {

        @Override
        public void paint(Graphics2D g) {
            int w = (int) getSize().getWidth();
            int h = (int) getSize().getHeight();

            g.setColor(getBackground());
            g.fillRect(getLocation().x, getLocation().y, w, h);

            g.setColor(getBarColor());
            g.fillRect(getLocation().x + getOffset(), getLocation().y, getBarSize(), h);
        }

    }

}
