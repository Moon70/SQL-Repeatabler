package lunartools.sqlrepeatabler.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lunartools.SwingTools;
import lunartools.sqlrepeatabler.common.view.MenuView;
import lunartools.sqlrepeatabler.core.view.MainPanel;
import lunartools.sqlrepeatabler.infrastructure.config.Settings;

public class SqlRepeatablerView{
	private static Logger logger = LoggerFactory.getLogger(SqlRepeatablerView.class);
	private static final int WINDOW_MINIMUM_WIDTH=1280;
	private static final int WINDOW_MINIMUM_HEIGHT=(int)(WINDOW_MINIMUM_WIDTH/SwingTools.SECTIOAUREA);
	public static final Dimension MINIMUM_FRAME_SIZE=new Dimension(WINDOW_MINIMUM_WIDTH,WINDOW_MINIMUM_HEIGHT);

	private SqlRepeatablerModel model;
	private final JFrame jFrame;
	private MainPanel mainPanel;

	public SqlRepeatablerView(SqlRepeatablerModel model) {
		this.model=model;
		jFrame = new JFrame(SqlRepeatablerModel.PROGRAMNAME+" "+SqlRepeatablerModel.getProgramVersion());
		jFrame.setLayout(new BorderLayout());
		jFrame.setResizable(true);
		jFrame.setIconImages(SwingTools.getDefaultIconImages());
		Settings settings=Settings.getInstance();
		jFrame.setBounds(settings.getViewBounds());

		mainPanel=new MainPanel(model);
		jFrame.add(mainPanel);

		JPanel glass=(JPanel)jFrame.getGlassPane();
		glass.setVisible(true);
		glass.setOpaque(false);
		glass.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent dropTargetDropEvent) {
				try {
					dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
					@SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>)dropTargetDropEvent.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					ArrayList<File> arraylistAcceptedFiles=new ArrayList<>();
					for (File file : droppedFiles) {
						if(file.getName().toLowerCase().endsWith(".sql")) {
							arraylistAcceptedFiles.add(file);
						}else {
							logger.warn("Ignoring unsupported file: "+file);
						}
					}
					if(arraylistAcceptedFiles.size()>0) {
						SqlRepeatablerView.this.model.addSqlInputFiles(arraylistAcceptedFiles);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		jFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				jFrame.repaint(100);
			}
		});
	}

	public void setMenuView(MenuView menuView) {
		jFrame.setJMenuBar(menuView.getMenuBar());
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public JFrame getJFrame() {
		return jFrame;
	}

	public void setVisible(boolean b) {
		jFrame.setVisible(b);
	}

}
