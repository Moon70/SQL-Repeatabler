package lunartools.sqlrepeatabler.common.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;

import lunartools.sqlrepeatabler.common.ui.ThemeManager;
import lunartools.sqlrepeatabler.infrastructure.config.CheckboxSettings;
import lunartools.sqlrepeatabler.infrastructure.config.ProcessingOrder;
import lunartools.sqlrepeatabler.infrastructure.config.Theme;
import lunartools.sqlrepeatabler.main.SqlRepeatablerController;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;

public class ActionFactory {
	private SqlRepeatablerController controller;

	public ActionFactory(SqlRepeatablerController controller) {
		this.controller=controller;
	}

	public Action createOpenAction() {
		return new OpenAction(controller.getModel(),controller.getView().getJFrame());
	}

	public Action createSaveAsAction() {
		return new SaveAsAction(controller.getModel(), controller.getFileController(), controller.getView().getJFrame());
	}

	public Action createReloadAction() {
		return new ReloadAction(controller);
	}

	public Action createResetAction() {
		return new ResetAction(controller);
	}

	public Action createExitProgramAction() {
		return new ExitProgramAction(controller);
	}

	public Action createAboutAction() {
		return new AboutAction(controller);
	}

	public Action createAsAddedRadioButtonAction() {
		return new AbstractAction(ProcessingOrder.ASADDED.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				controller.getModel().setProcessingOrder(ProcessingOrder.ASADDED);
			}
		};
	}

	public Action createByCreationDateRadioButtonAction() {
		return new AbstractAction(ProcessingOrder.CREATIONDATE.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				controller.getModel().setProcessingOrder(ProcessingOrder.CREATIONDATE);
			}
		};
	}

	public Action createAlphabeticallyRadioButtonAction() {
		return new AbstractAction(ProcessingOrder.ALPHABETICALLY.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				controller.getModel().setProcessingOrder(ProcessingOrder.ALPHABETICALLY);
			}
		};
	}

	public Action createBackgroundColorCheckboxAction() {
		return new AbstractAction(CheckboxSettings.BACKGROUND_COLOR.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
				boolean checked = item.isSelected();
				controller.getModel().enableBackgroundColor(checked);
			}
		};
	}

	public Action createLightThemeRadioButtonAction() {
		return new AbstractAction(Theme.LIGHT.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				ThemeManager.getInstance().applyTheme(Theme.LIGHT);
			}
		};
	}

	public Action createDarkThemeRadioButtonAction() {
		return new AbstractAction(Theme.DARK.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				ThemeManager.getInstance().applyTheme(Theme.DARK);
			}
		};
	}

	public Action createIntellijThemeRadioButtonAction() {
		return new AbstractAction(Theme.INTELLIJ.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				ThemeManager.getInstance().applyTheme(Theme.INTELLIJ);
			}
		};
	}

	public Action createDarculaThemeRadioButtonAction() {
		return new AbstractAction(Theme.DARCULA.getLabel()) {
			@Override public void actionPerformed(ActionEvent e) {
				ThemeManager.getInstance().applyTheme(Theme.DARCULA);
			}
		};
	}

	private class ExitProgramAction extends AbstractAction {
		private SqlRepeatablerController controller;

		public ExitProgramAction(SqlRepeatablerController controller) {
			this.controller = controller;
			this.putValue(NAME, "Exit");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.shutdown();
		}
	}

	private class ReloadAction extends AbstractAction {
		private SqlRepeatablerController controller;

		public ReloadAction(SqlRepeatablerController controller) {
			this.controller = controller;
			this.putValue(NAME, "Reload");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getModel().reload();
		}
	}

	private class ResetAction extends AbstractAction {
		private SqlRepeatablerController controller;

		public ResetAction(SqlRepeatablerController controller) {
			this.controller = controller;
			this.putValue(NAME, "Reset");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getModel().reset();
		}
	}

	private class AboutAction extends AbstractAction {
		private SqlRepeatablerController controller;

		public AboutAction(SqlRepeatablerController controller) {
			this.controller = controller;
			this.putValue(NAME, "About "+SqlRepeatablerModel.PROGRAMNAME);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.openAboutDialogue();
		}
	}

}
