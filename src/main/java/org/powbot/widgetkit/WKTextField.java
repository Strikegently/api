package org.powbot.widgetkit;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * WidgetKit Label
 *
 * @author rvbiljouw
 */
public class WKTextField extends WKComponent {
    private long lastCursorDrawn = System.currentTimeMillis();
    private Font font = Font.decode(Font.MONOSPACED);
    private Color textColor = new Color(255, 255, 255);
    private String text;

    public WKTextField() {
        this("");
    }

    public WKTextField(String text) {
        setText(text);
        initListeners();
    }

    private void initListeners() {
        addKeyListener(new WKKeyListener() {
            @Override
            public void onKeyTyped(KeyEvent event) {
                if (event.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    if (text.length() > 0) {
                        text = text.substring(0, text.length() - 1);
                    }
                } else if(event.getKeyChar() != KeyEvent.VK_ENTER) {
                    text += event.getKeyChar();
                }
            }
        });
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
        if (width > getSize().getWidth() || height > getSize().getHeight()) {
            System.out.println("Changing textbox size to " + width + "," + height);
            super.setSize(new Dimension(width, height));
        }
        Point location = getLocation();

        g.setFont(font);
        g.setColor(textColor);
        long cursorTimeOut = System.currentTimeMillis() - lastCursorDrawn;
        if (hasFocus() && cursorTimeOut > 1000) {
            g.drawString(text + "|", location.x, location.y + height);
            if (cursorTimeOut > 2000) {
                lastCursorDrawn = System.currentTimeMillis();
            }
        } else {
            g.drawString(text, location.x, location.y + height);
        }
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
