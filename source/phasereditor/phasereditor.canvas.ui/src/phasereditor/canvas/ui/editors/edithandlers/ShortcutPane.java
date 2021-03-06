// The MIT License (MIT)
//
// Copyright (c) 2015, 2018 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.canvas.ui.editors.edithandlers;

import java.util.function.Consumer;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.RGB;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import phasereditor.canvas.core.BaseObjectModel;
import phasereditor.canvas.ui.CanvasUI;
import phasereditor.canvas.ui.editors.ObjectCanvas;
import phasereditor.canvas.ui.editors.grid.NumberCellEditor;
import phasereditor.canvas.ui.shapes.BaseObjectControl;
import phasereditor.canvas.ui.shapes.IObjectNode;

/**
 * @author arian
 *
 */
public abstract class ShortcutPane extends GridPane implements IEditHandlerNode {

	protected final IObjectNode _object;
	protected final ObjectCanvas _canvas;
	protected final Node _node;
	protected final BaseObjectModel _model;
	protected final BaseObjectControl<?> _control;
	private boolean _updated;
	private Point2D _startPoint;
	private Point2D _initPos;

	public ShortcutPane(IObjectNode object) {
		_object = object;
		_control = _object.getControl();
		_node = _object.getControl().getNode();
		_model = _object.getModel();
		_canvas = _object.getControl().getCanvas();

		setPadding(new Insets(10));
		setHgap(5);
		setVgap(5);

		setEffect(new DropShadow());

		setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, new Insets(0))));

		setOnDragDetected(e -> {

			layoutXProperty().unbind();
			layoutYProperty().unbind();

			_initPos = localToParent(0, 0);
			_startPoint = localToParent(e.getX(), e.getY());
			e.consume();
		});

		setOnMouseReleased(e -> {
			_initPos = null;
			_startPoint = null;
			e.consume();
		});

		setOnMouseDragged(e -> {
			if (_initPos != null) {
				Point2D point = localToParent(e.getX(), e.getY());
				double x = _initPos.getX() + (point.getX() - _startPoint.getX());
				double y = _initPos.getY() + (point.getY() - _startPoint.getY());
				relocate(x, y);
			}
			e.consume();
		});

		setOnMouseMoved(e -> {
			e.consume();
		});

		setPrefSize(160, -1);
	}

	protected static Label createTitle(String label) {
		Label title = new Label(label);
		title.setStyle("-fx-opacity:0.5;-fx-text-fill:white;");
		return title;
	}

	protected Label createTextField(double text, String name, Consumer<Double> consumer) {
		Label label = new Label(Double.toString(text));
		label.setMaxWidth(150);

		label.setStyle("-fx-text-fill:white;");
		label.setCursor(Cursor.TEXT);

		setHgrow(label, Priority.ALWAYS);

		label.setOnMouseClicked(e -> {

			// this is a useful patch!
			String text2 = label.getText();
			int i = text2.indexOf("=");
			if (i != -1) {
				text2 = text2.substring(i + 1).trim();
			}

			InputDialog dlg = new InputDialog(_canvas.getEditor().getEditorSite().getShell(), name,
					"Enter a new value expression:", text2, value -> {
						return NumberCellEditor.scriptEngineValidate(value);
					});

			if (dlg.open() == Window.OK) {
				String value = dlg.getValue();
				Double result = NumberCellEditor.scriptEngineEval(value);
				label.setText(result.toString());
				consumer.accept(result);
			}

		});
		return label;

	}

	@Override
	public IObjectNode getObject() {
		return _object;
	}

	@Override
	public void updateHandler() {
		if (_updated) {
			return;
		}

		_updated = true;

		updateFromPreferences();

	}

	public void updateFromPreferences() {
		IPreferenceStore store = CanvasUI.getPreferenceStore();

		// position

		String pos = store.getString(CanvasUI.PREF_PROP_CANVAS_SHORTCUT_PANE_POSITION);

		switch (pos) {
		case CanvasUI.PREF_VALUE_CANVAS_SHORTCUT_PANE_POSITION_TOP_LEFT:
			layoutXProperty().unbind();
			layoutYProperty().unbind();
			relocate(10, 10);
			break;
		case CanvasUI.PREF_VALUE_CANVAS_SHORTCUT_PANE_POSITION_TOP_RIGHT:
			layoutXProperty().unbind();
			layoutYProperty().unbind();

			layoutXProperty().bind(_canvas.getScene().widthProperty().add(widthProperty().add(10).multiply(-1)));
			setLayoutY(10);
			break;

		case CanvasUI.PREF_VALUE_CANVAS_SHORTCUT_PANE_POSITION_BOTTOM_LEFT:
			layoutXProperty().unbind();
			layoutYProperty().unbind();

			layoutYProperty().bind(_canvas.getScene().heightProperty().add(heightProperty().add(10).multiply(-1)));
			setLayoutX(10);
			break;

		case CanvasUI.PREF_VALUE_CANVAS_SHORTCUT_PANE_POSITION_BOTTOM_RIGHT:
			layoutXProperty().bind(_canvas.getScene().widthProperty().add(widthProperty().add(10).multiply(-1)));
			layoutYProperty().bind(_canvas.getScene().heightProperty().add(heightProperty().add(10).multiply(-1)));
			break;

		default:
			break;
		}

		// color

		{
			RGB rgb = StringConverter.asRGB(store.getString(CanvasUI.PREF_PROP_CANVAS_SHORTCUT_PANE_BG_COLOR));
			Color color = Color.rgb(rgb.red, rgb.green, rgb.blue);
			setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, new Insets(0))));
		}
//		{
//			RGB rgb = StringConverter.asRGB(store.getString(CanvasUI.PREF_PROP_CANVAS_SHORTCUT_PANE_FG_COLOR));
//			Color color = Color.rgb(rgb.red, rgb.green, rgb.blue);
//			String style = "-fx-text-fill:rgb(" + (int) (color.getRed() * 255) + "," + (int) (color.getGreen() * 255)
//					+ "," + (int) (color.getBlue() * 255) + ");";
//			for (Node node : getChildren()) {
//				if (node instanceof Label) {
//					node.setStyle(style);
//				} else if (node instanceof Button) {
//					node.setStyle(style);
//					Button btn = (Button) node;
//					Node graphic = btn.getGraphic();
//					if (graphic != null) {
//						if (graphic instanceof Label) {
//							graphic.setStyle(style);
//						} else if (graphic instanceof ImageView) {
//							
//							Blend multiply = new Blend(BlendMode.MULTIPLY);
//
//							double width = btn.getWidth();
//							double height = btn.getHeight();
//
//							ColorInput value = new ColorInput(0, 0, width, height, color);
//							multiply.setTopInput(value);
//
//							Blend atop = new Blend(BlendMode.SRC_ATOP);
//							atop.setTopInput(multiply);
//							node.setEffect(atop);
//							
//						}
//					}
//				}
//			}
//		}
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
