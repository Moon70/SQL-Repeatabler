package lunartools.sqlrepeatabler.gui;

import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.sqlrepeatabler.AddInputDataEvent;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class IOPanel extends JPanel implements Observer{
	private static Logger logger = LoggerFactory.getLogger(IOPanel.class);
	JTextArea inputTextarea;
	private JTextArea outputTextarea;
	private final SqlRepeatablerModel model;

	public IOPanel(SqlRepeatablerModel model) {
		this.model=model;
		model.addObserver(this);

		Font font=new Font("Courier New", Font.PLAIN,12);
		inputTextarea=new JTextArea(50,100);
		inputTextarea.setEditable(false);
		inputTextarea.setFont(font);
		outputTextarea=new JTextArea(50,100);
		outputTextarea.setEditable(false);
		outputTextarea.setFont(font);

		JSplitPane jSplitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		jSplitPaneHorizontal.setLeftComponent(new JScrollPane(inputTextarea));
		jSplitPaneHorizontal.setRightComponent(new JScrollPane(outputTextarea));
		add(jSplitPaneHorizontal);

		inputTextarea.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					ArrayList<File> arraylistAcceptedFiles=new ArrayList<>();
					for (File file : droppedFiles) {
						System.out.println("process: "+file);
						if(file.getName().toLowerCase().endsWith(".sql")) {
							arraylistAcceptedFiles.add(file);
						}else {
							logger.warn("ignoring unsupported file: "+file);
						}
					}
					if(arraylistAcceptedFiles.size()>0) {
						IOPanel.this.model.addSqlInputFiles(arraylistAcceptedFiles);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}

	public void setInputText(String s) {
		inputTextarea.setText(s);
	}

	@Override
	public void update(Observable observable, Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+observable+", "+object);
		}
		if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			outputTextarea.setText(model.getConvertedSqlScript().toString());
			this.repaint();
		}else if(object==SimpleEvents.MODEL_RESET) {
			inputTextarea.setText("");
			outputTextarea.setText("");
		}else if(object instanceof AddInputDataEvent) {
			AddInputDataEvent addInputDataEvent=(AddInputDataEvent)object;
			inputTextarea.append(addInputDataEvent.getInputdata());
		}
	}

}
