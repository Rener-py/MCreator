/*
 * MCreator (https://mcreator.net/)
 * Copyright (C) 2020 Pylo and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.mcreator.ui.component;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.Path2D;
import java.util.List;

public class JSelectableList<E> extends JList<E> {

	private final Path2D rubberBand = new Path2D.Double();

	protected boolean dndCustom = false;

	@Nullable protected CustomDNDListener<E> listener;

	private final JSelectableListMouseListenerWithDND<E> slmlwdnd;

	@Nullable protected JComponent additionalDNDComponent;

	public JSelectableList(ListModel<E> dataModel) {
		this(dataModel, null);
	}

	public JSelectableList(ListModel<E> dataModel, @Nullable MouseListener priorityMouseListener) {
		super(dataModel);

		slmlwdnd = new JSelectableListMouseListenerWithDND<>(this);
		addMouseListener(slmlwdnd);
		addMouseMotionListener(slmlwdnd);

		if (priorityMouseListener != null)
			addMouseListener(priorityMouseListener);
	}

	public void enableDNDCustom(CustomDNDListener<E> listener) {
		this.dndCustom = true;
		this.listener = listener;
	}

	public void setAdditionalDNDComponent(@Nullable JComponent additionalDNDComponent) {
		this.additionalDNDComponent = additionalDNDComponent;
	}

	public void cancelDND() {
		this.slmlwdnd.stopDNDAction();
	}

	Path2D getRubberBand() {
		return rubberBand;
	}

	private final Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 6 },
			0);

	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (rubberBand.getBounds().getWidth() > 0 && rubberBand.getBounds().getHeight() > 0) {
			Graphics2D g2 = (Graphics2D) g;
			Stroke defaultStroke = g2.getStroke();
			g2.setColor((Color) UIManager.get("MCreatorLAF.MAIN_TINT"));
			g2.setStroke(dashed);
			g2.draw(rubberBand);
			g2.setColor(new Color(g2.getColor().getRed(), g2.getColor().getGreen(), g2.getColor().getBlue(), 50));
			g2.setStroke(defaultStroke);
			g2.fill(rubberBand);
		}
	}

	public interface CustomDNDListener<E> {

		void dndComplete(Object target, List<E> sources);

	}

}
