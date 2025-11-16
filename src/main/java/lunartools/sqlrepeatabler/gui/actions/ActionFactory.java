package lunartools.sqlrepeatabler.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import lunartools.sqlrepeatabler.SqlRepeatablerController;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.settings.ProcessingOrder;

public class ActionFactory {
	private SqlRepeatablerController controller;
	
	public ActionFactory(SqlRepeatablerController controller) {
		this.controller=controller;
	}

	public Action createOpenAction() {
		return new OpenAction(controller);
	}

	public Action createSaveAsAction() {
		return new SaveAsAction(controller);
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
		return new AbstractAction(ProcessingOrder.ASADDED.getKey()) {
            @Override public void actionPerformed(ActionEvent e) {
            	controller.getModel().setProcessingOrder(ProcessingOrder.ASADDED);
            }
        };
	}
	
	public Action createByCreationDateRadioButtonAction() {
		return new AbstractAction(ProcessingOrder.CREATIONDATE.getKey()) {
            @Override public void actionPerformed(ActionEvent e) {
            	controller.getModel().setProcessingOrder(ProcessingOrder.CREATIONDATE);
            }
        };
	}
	
	public Action createAlphabeticallyRadioButtonAction() {
		return new AbstractAction(ProcessingOrder.ALPHABETICALLY.getKey()) {
            @Override public void actionPerformed(ActionEvent e) {
            	controller.getModel().setProcessingOrder(ProcessingOrder.ALPHABETICALLY);
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
