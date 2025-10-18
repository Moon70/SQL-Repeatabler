package lunartools.sqlrepeatabler.gui;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.FileTools;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;

public class IOPanel extends JPanel{
	private static Logger logger = LoggerFactory.getLogger(IOPanel.class);
	JTextArea inputTextarea;
	private JTextArea outputTextarea;
	private final SqlRepeatablerModel model;
	private final int sqlFileIndex;

	public IOPanel(SqlRepeatablerModel model, int sqlFileIndex) {
		this.model=model;
		this.sqlFileIndex=sqlFileIndex;
		
		model.addChangeListener(this::updateModelChanges);

		Font font=new Font("Courier New", Font.PLAIN,12);
		inputTextarea=new JTextArea(48,110);
		inputTextarea.setEditable(false);
		inputTextarea.setFont(font);
		
		outputTextarea=new JTextArea(48,110);
		outputTextarea.setEditable(false);
		outputTextarea.setFont(font);

		JSplitPane jSplitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		jSplitPaneHorizontal.setLeftComponent(new JScrollPane(inputTextarea));
		jSplitPaneHorizontal.setRightComponent(new JScrollPane(outputTextarea));
		add(jSplitPaneHorizontal);

		try {
			ArrayList<File> files=model.getSqlInputFiles();
			StringBuffer sbContent=FileTools.readFileToStringBuffer(files.get(sqlFileIndex), StandardCharsets.UTF_8.name());
			inputTextarea.setText(sbContent.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateModelChanges(Object object) {
		if(logger.isTraceEnabled()) {
			logger.trace("update: "+object);
		}
		if(object==SimpleEvents.MODEL_SQLINPUTFILESCHANGED) {
			
		}else if(object==SimpleEvents.MODEL_CONVERTEDSQLSCRIPTCHANGED) {
			if(model.hasSqlConvertedScripts()) {
				outputTextarea.setText(model.getSingleConvertedSqlScript(sqlFileIndex).toString());
				this.repaint();
			}
		}else if(object==SimpleEvents.MODEL_RESET) {
			inputTextarea.setText("");
			outputTextarea.setText("");
		}
	}

}
