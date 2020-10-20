package org.powbot.widgetkit;

import java.awt.*;

/**
 * WidgetKit Label
 *
 * @author rvbiljouw
 */
public class WKLabel extends WKComponent {
    private Font font = Font.decode(Font.MONOSPACED);
    private Color textColor = new Color(255, 255, 255);
    private String text;

    public WKLabel() {
        this("");
    }

    public WKLabel(String text) {
        setText(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void paint(Graphics2D g) {
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        if (height > getSize().getHeight()) {
            super.setSize(new Dimension(width, height));
        } else {
            super.getSize().width = width;
        }
        Point location = getLocation();

        g.setFont(font);
        g.setColor(textColor);
        g.drawString(text, location.x, location.y + (getSize().height / 2));
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
