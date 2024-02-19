package cnuphys.bCNU.graphics.container;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JLayeredPane;

import cnuphys.bCNU.drawable.DrawableList;
import cnuphys.bCNU.drawable.IDrawable;
import cnuphys.bCNU.item.AItem;
import cnuphys.bCNU.item.ItemList;
import cnuphys.bCNU.view.BaseView;

public class LayeredContainer extends AContainer<JLayeredPane> {

	public LayeredContainer(JLayeredPane component, BaseView view, java.awt.geom.Rectangle2D.Double worldSystem) {
		super(component, view, worldSystem);
	}
	
	@Override
	public ItemList addItemList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addItemList(ItemList itemList) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemList getAnnotationList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemList getItemList(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeItemList(ItemList itemList) {
		// TODO Auto-generated method stub

	}

	@Override
	public AItem getItemAtPoint(Point lp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<AItem> getEnclosedItems(Rectangle rect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<AItem> getItemsAtPoint(Point lp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean anySelectedItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSelectedItems(IContainer container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAllItems(boolean select) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemList getGlassList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDirty(boolean dirty) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAfterDraw(IDrawable afterDraw) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBeforeDraw(IDrawable beforeDraw) {
		// TODO Auto-generated method stub

	}


}
