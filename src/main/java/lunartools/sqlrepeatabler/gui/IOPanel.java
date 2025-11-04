package lunartools.sqlrepeatabler.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.FileTools;
import lunartools.sqlrepeatabler.SimpleEvents;
import lunartools.sqlrepeatabler.SqlRepeatablerModel;
import lunartools.sqlrepeatabler.parser.SqlScript;

public class IOPanel extends JPanel{
	private static Logger logger = LoggerFactory.getLogger(IOPanel.class);
	private JTextArea inputTextarea;
	private JTextArea outputTextarea;
//	private SqlScriptPanel inputPanel;
//	private SqlScriptPanel outputPanel;
	private SqlScriptEditorPane inputPanel;
	private SqlScriptEditorPane outputPanel;
	private final SqlRepeatablerModel model;
	private final int sqlFileIndex;

	private static final boolean TEST=true;
	
	public IOPanel(SqlRepeatablerModel model, int sqlFileIndex) {
		this.model=model;
		this.sqlFileIndex=sqlFileIndex;
		
		model.addChangeListener(this::updateModelChanges);

		setLayout(new BorderLayout());
		
		Font font=new Font("Courier New", Font.PLAIN,12);
		if(TEST) {
//			inputPanel=new SqlScriptPanel(model);
//			outputPanel=new SqlScriptPanel(model);
			inputPanel=new SqlScriptEditorPane(model);
			outputPanel=new SqlScriptEditorPane(model);
		}else {
			inputTextarea=new JTextArea(48,110);
			inputTextarea.setEditable(false);
			inputTextarea.setFont(font);
			outputTextarea=new JTextArea(48,110);
			outputTextarea.setEditable(false);
			outputTextarea.setFont(font);
		}
		
		

		JSplitPane jSplitPaneHorizontal = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
		jSplitPaneHorizontal.setResizeWeight(0.5);
		jSplitPaneHorizontal.setOneTouchExpandable(true);
		
		if(TEST) {
			JScrollPane scrollPaneLeft=new JScrollPane(inputPanel);
			scrollPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			jSplitPaneHorizontal.setLeftComponent(scrollPaneLeft);

			JScrollPane scrollPaneRight=new JScrollPane(outputPanel);
			scrollPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			jSplitPaneHorizontal.setRightComponent(scrollPaneRight);
		}else {
			jSplitPaneHorizontal.setLeftComponent(new JScrollPane(inputTextarea));
			jSplitPaneHorizontal.setRightComponent(new JScrollPane(outputTextarea));
		}
		add(jSplitPaneHorizontal,BorderLayout.CENTER);

		try {
			ArrayList<File> files=model.getSqlInputFiles();
			StringBuffer sbContent=FileTools.readFileToStringBuffer(files.get(sqlFileIndex), StandardCharsets.UTF_8.name());
			if(TEST) {
//				String s=sbContent.toString().replaceAll("\n","<br>");
//				s=s.replaceAll("\r","");
//				s=s.replaceAll(" ","&nbsp;");
//				s=s.replaceAll("\t","&nbsp;&nbsp;&nbsp;&nbsp;");
//				s=s.replaceAll("CREATED","WAMBO");
//				inputPanel.setText(s);
			}else {
				inputTextarea.setText(sbContent.toString());
			}
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
				if(TEST) {
					SqlScript sqlScript=model.getSqlScript(sqlFileIndex);
					inputPanel.setText(sqlScript.toHtml());
					
					outputPanel.setText(model.getSingleConvertedSqlScriptBlock(sqlFileIndex).toHtml());
					
					this.repaint();
				}else {
					outputTextarea.setText(model.getSingleConvertedSqlScriptBlock(sqlFileIndex).toString());
				}
			}
		}else if(object==SimpleEvents.MODEL_RESET) {
			if(TEST) {
				inputPanel.setText("");
				outputPanel.setText("");
			}else {
				inputTextarea.setText("");
				outputTextarea.setText("");
			}
		}
	}

}
