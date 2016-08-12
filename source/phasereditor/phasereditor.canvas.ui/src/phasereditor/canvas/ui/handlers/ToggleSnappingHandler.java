package phasereditor.canvas.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import phasereditor.canvas.ui.editors.CanvasEditor;
import phasereditor.canvas.ui.editors.SceneSettings;

public class ToggleSnappingHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		CanvasEditor editor = (CanvasEditor) HandlerUtil.getActiveEditor(event);
		SceneSettings model = editor.getCanvas().getSettingsModel();
		model.setEnableStepping(!model.isEnableStepping());
		editor.getCanvas().getBackGridPane().repaint();
		
		return null;
	}

}