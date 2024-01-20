package cnuphys.bCNU.graphics.colorscale;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cnuphys.bCNU.graphics.GraphicsUtilities;
import cnuphys.bCNU.graphics.component.CommonBorder;
import cnuphys.bCNU.view.BaseView;

public class ColorModelPanel extends JPanel implements ActionListener {

	// the legend
	private ColorModelLegend _legend;

	// a slider
	// private JSlider _slider;

	// the model
	private ColorScaleModel _model;
	private ColorScaleModel _monoModel;

	// radio buttons
	private JRadioButton _colorRB;
	private JRadioButton _monoRB;
	private BaseView _view;

	/**
	 * Create a panel with a slider and a color legend
	 *
	 * @param model        the color model
	 * @param desiredWidth pixel width
	 * @param name         a name
	 * @param gap          a spacing
	 */
	public ColorModelPanel(ColorScaleModel model, int desiredWidth, String name, int gap, double initRelVal) {
		this(null, model, desiredWidth, name, gap, false, false);
	}

	/**
	 * Create a panel with a slider and a color legend
	 *
	 * @param model        the color model
	 * @param desiredWidth pixel width
	 * @param name         a name
	 * @param gap          a spacing
	 * @param incRB        if <code>true</code> include the color monochrome radio
	 *                     buttons
	 * @param colorDefault if <code>true</code> default to color
	 */
	public ColorModelPanel(BaseView view, ColorScaleModel model, int desiredWidth, String name, int gap,
			boolean includeRadioButtons, boolean colorDefault) {

		_view = view;
		_model = model;

		setLayout(new BorderLayout(4, 4));

		_legend = new ColorModelLegend(_model, desiredWidth, null, gap);
		_legend.setBorder(null);


		if (includeRadioButtons) {
			addNorth(colorDefault);
		}

		add(_legend, BorderLayout.CENTER);
//		add(_slider, BorderLayout.SOUTH);
		setBorder(new CommonBorder(name));
	}

	private void addNorth(boolean colorDefault) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 2));

		ButtonGroup bg = new ButtonGroup();

		_colorRB = new JRadioButton("Color", colorDefault);
		_monoRB = new JRadioButton("Monochrome", !colorDefault);

		_colorRB.addActionListener(this);
		_monoRB.addActionListener(this);

		bg.add(_colorRB);
		bg.add(_monoRB);

		GraphicsUtilities.setSizeSmall(_colorRB);
		GraphicsUtilities.setSizeSmall(_monoRB);

		panel.add(_colorRB);
		panel.add(_monoRB);

		add(panel, BorderLayout.NORTH);
	}



	@Override
	public Insets getInsets() {
		Insets def = super.getInsets();
		return new Insets(def.top + 2, def.left + 2, def.bottom + 2, def.right + 2);
	}

	/**
	 * Is monochrome selected
	 * @return true of monochrome is selected
	 */
	public boolean isMonochrome() {
		return (_monoRB != null) && _monoRB.isSelected() ;
	}


	/**
	 * Get the color scale model if there is one.
	 *
	 * @return the color scale model for accumulation, etc.
	 */
	public ColorScaleModel getColorScaleModel() {
		if (_legend != null) {
			return _legend.getColorScaleModel();
		}

		return null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _colorRB) {
			System.out.println("dude");
			_legend.setColorScaleModel(_model);
			if (_view != null) {
				_view.refresh();
			}

		} else if (e.getSource() == _monoRB) {
			if (_monoModel == null) {
				_monoModel = ColorScaleModel.getMonochromeModel(_model);
			}
			_legend.setColorScaleModel(_monoModel);

			if (_view != null) {
				_view.refresh();
			}
		}

	}

}
