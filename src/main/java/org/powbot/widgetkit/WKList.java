package org.powbot.widgetkit;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A list component.
 *
 * @author rvbiljouw
 */
public class WKList<T> extends WKComponent {
	private Font font = Font.decode(Font.MONOSPACED);
	private Color textColor = new Color(255, 255, 255);
	private Color rowColor = new Color(40, 40, 40, 150);
	private Color selectedRowColor = new Color(160, 208, 76);
	private Color rowBorderColor = new Color(33, 33, 33);
	private List<T> items = new ArrayList<>();
	private int fontHeight = -1;
	private int selectedIndex = -1;

	public WKList(List<T> items) {
		this();
		this.items = items;
	}

	@SafeVarargs
	public WKList(T... items) {
		this(Arrays.asList(items));
	}

	public WKList() {
		initListeners();
	}

	private void initListeners() {
		addMouseListener(new WKMouseAdapter() {
			@Override
			public void onClick(MouseEvent event) {
				int x = getLocation().x;
				int y = getLocation().y;
				int width = getSize().width;
				int height = getSize().height;

				for (int idx = 0; idx < items.size(); idx++) {
					int rowHeight = fontHeight + 4;
					Rectangle box = new Rectangle(x, y + (idx * rowHeight), width, rowHeight);
					if (box.contains(event.getX(), event.getY())) {
						selectedIndex = idx;
						return;
					}
				}
			}
		});
	}

	@Override
	public void paint(Graphics2D g) {
		super.paint(g);
		int x = getLocation().x;
		int y = getLocation().y;
		int width = getSize().width;
		int height = getSize().height;
		int rowHeight = fontHeight + 4;

		g.setFont(font);
		fontHeight = g.getFontMetrics().getHeight();

		if (y + (items.size() * rowHeight) > height) {
			setBounds(x, y, width, y + (items.size() * rowHeight));
		}

		for (int idx = 0; idx < items.size(); idx++) {

			T item = items.get(idx);
			String textRepresentation = String.valueOf(item);
			int textWidth = g.getFontMetrics().stringWidth(textRepresentation);

			if (textWidth > getSize().width) {
				setBounds(x, y, textWidth, getSize().height);
			}

			if (selectedIndex != idx) {
				g.setColor(rowColor);
			} else {
				g.setColor(selectedRowColor);
			}
			g.fillRect(x, y + (idx * rowHeight), width, rowHeight);
			g.setColor(textColor);
			g.drawString(textRepresentation, x + 10, (y + (idx * rowHeight)) + (rowHeight / 2) + 4);
			g.setColor(rowBorderColor);
			g.drawLine(x, y + ((idx + 1) * rowHeight), x + width, y + ((idx + 1) * rowHeight));
		}
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getRowColor() {
		return rowColor;
	}

	public void setRowColor(Color rowColor) {
		this.rowColor = rowColor;
	}

	public Color getSelectedRowColor() {
		return selectedRowColor;
	}

	public void setSelectedRowColor(Color selectedRowColor) {
		this.selectedRowColor = selectedRowColor;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public void addItem(T item) {
		items.add(item);
	}

	public void removeItem(T item) {
		items.remove(item);
	}

	public T getSelectedItem() {
		if (selectedIndex > -1) {
			return items.get(selectedIndex);
		}
		return null;
	}
}
