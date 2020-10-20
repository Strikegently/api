package org.powbot.widgetkit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author rvbiljouw
 */
public interface WKMouseListener {

    /**
     * Called when the mouse enters the component
     * @param event The mouse event
     */
    public void onEnter(MouseEvent event);

    /**
     * Called every time the mouse is moved inside the component
     * @param event The mouse event
     */
    public void onHover(MouseEvent event);

    /**
     * Called when the mouse leaves the component
     * @param event The mouse event
     */
    public void onLeave(MouseEvent event);

    /**
     * Called when the mouse is clicked inside the component
     * @param event The mouse event
     */
    public void onClick(MouseEvent event);

    /**
     * Called when the mouse is pressed, but not yet released inside the component
     * @param event The mouse event
     */
    public void onPress(MouseEvent event);

    /**
     * Called when the mouse is released inside the component
     * @param event The mouse event
     */
    public void onRelease(MouseEvent event);

    /**
     * Called when the mouse is dragged inside the component
     * @param event The mouse event
     */
    public void onDrag(MouseEvent event);

    /**
     * Called when the mouse wheel is scroll or clicked inside the component
     * @param event The mouse wheel event
     */
    public void onScroll(MouseWheelEvent event);

}
