package org.powbot.widgetkit.util;

import org.powbot.widgetkit.WKComponent;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * Simulates depth sorting in a 2d environment
 * @author tommo
 *
 */
public class DepthSortComparator implements Comparator<WKComponent> {

    private List<WKComponent> sourceList;

    public DepthSortComparator(List<WKComponent> sourceList) {
        this.sourceList = sourceList;
    }

    @Override
    public int compare(WKComponent c1, WKComponent c2) {
        Rectangle r1 = new Rectangle(c1.getLocation().x, c1.getLocation().y, c1.getSize().width, c1.getSize().height);
        Rectangle r2 = new Rectangle(c2.getLocation().x, c2.getLocation().y, c2.getSize().width, c2.getSize().height);

        if (r1.contains(r2)) {
            return 1;
        } else if (r2.contains(r1)) {
            return -1;
        } else {
            int idxc1 = sourceList.indexOf(c1);
            int idxc2 = sourceList.indexOf(c2);
            int max = Math.max(idxc1, idxc2);

            if (max == idxc1) {
                return 1;
            } else if (max == idxc2) {
                return -1;
            }

            return 0;
        }
    }

}
