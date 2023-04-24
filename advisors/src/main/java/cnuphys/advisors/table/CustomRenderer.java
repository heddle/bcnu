package cnuphys.advisors.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import cnuphys.advisors.io.DataModel;

public class CustomRenderer extends DefaultTableCellRenderer {
	
	protected DataModel model;
	
	public CustomRenderer(DataModel model) {
		this.model = model;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);

		if (model.renderer != null) {
			Color textColor = model.getHighlightTextColor(row, column);
			if (textColor != null) {
				cellComponent.setForeground(textColor);
			}
			
			Color bgColor = model.getHighlightBackgroundColor(row, column);
			if (bgColor != null) {
				cellComponent.setBackground(bgColor);
			}
			
			Font font = model.getHighlightFont(row, column);
			if (font != null) {
				cellComponent.setFont(font);
			}

			
		}
		return cellComponent;
	}

}
