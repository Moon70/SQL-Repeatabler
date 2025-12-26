package lunartools.sqlrepeatabler.common.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.common.ui.Dialogs;
import lunartools.sqlrepeatabler.common.ui.ThemeManager;
import lunartools.sqlrepeatabler.infrastructure.config.CheckboxSettings;
import lunartools.sqlrepeatabler.infrastructure.config.ProcessingOrder;
import lunartools.sqlrepeatabler.infrastructure.config.Theme;
import lunartools.sqlrepeatabler.main.SqlRepeatablerController;
import lunartools.sqlrepeatabler.main.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlBlock;

public class ActionFactory {
	private SqlRepeatablerController controller;

	public ActionFactory(SqlRepeatablerController controller) {
		this.controller=Objects.requireNonNull(controller);
	}

	public Action createOpenAction() {
		return new OpenAction(controller.getModel(),controller.getView().getJFrame());
	}

	public Action createSaveAsAction() {
		return new SaveAsAction(controller.getModel(), controller.getFileController(), controller.getView().getJFrame());
	}

	public Action createReloadAction() {
		return new AbstractAction("Reload") {
			@Override public void actionPerformed(ActionEvent e) {
				controller.getModel().reload();
			}
		};
	}

	public Action createResetAction() {
		return new AbstractAction("Reset") {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.getModel().reset();
			}
		};
	}

	public Action createExitProgramAction() {
		return new AbstractAction("Exit") {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.shutdown();
			}
		};
	}

	public Action createAboutAction() {
		return new AbstractAction("About "+SqlRepeatablerModel.PROGRAMNAME) {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openAboutDialogue();
			}
		};
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

	public Action createCloseScriptAction(int index,JComponent parentComponent) {
		return new AbstractAction("Close") {
			@Override
			public void actionPerformed(ActionEvent e) {
				SqlRepeatablerModel model=controller.getModel();
				File file=model.getSqlInputFile(index);
				if(!Dialogs.userCanceledFileExistsDialogue("OK to close file?\n"+file.getAbsolutePath(), parentComponent)) {
					return;
				}
				model.closeScript(index);
			}
		};
	}

	public Action createCopyToClipboardAction(int index,JComponent parentComponent) {
		return new AbstractAction("Copy to clipboard") {
			private Logger logger = LoggerFactory.getLogger(AbstractAction.class);
			@Override
			public void actionPerformed(ActionEvent e) {
				SqlRepeatablerModel model=controller.getModel();
				SqlBlock sqlBlock=model.getSingleConvertedSqlScriptBlock(index);
				StringSelection stringSelection = new StringSelection(sqlBlock.toString());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
				logger.info("Copied script to clipboard");
			}
		};
	}

}
