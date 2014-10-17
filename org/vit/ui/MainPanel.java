package org.vit.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.vit.model.MyHttpURL;
import org.vit.model.MyHttpsURL;
import org.vit.model.MyURL;
import org.vit.util.ImgReader;


import javax.swing.JComboBox;


public class MainPanel extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTextField urlJTextField;
	private ServerInfoPanel serverInfoPanel;
	private ExcutePanel excutePanel;
	private JPopupMenu rightJPopupMenu;
	private FileUploadPanel fileUploadPanel;
	private CustomUploadPanel customUploadPanel;
	private FileViewPanel fileViewPanel;
	private JComboBox versionJComboBox;
	
	public MainPanel() {

		setTitle("Struts2 \u6f0f\u6d1e\u7ec8\u6781\u5229\u7528\u5de5\u5177  Powered By VTI");
		setSize(750, 600);
		setMinimumSize(new Dimension(750, 600));
		setIconImage(ImgReader.getImage("resource/icon.png"));
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		urlJTextField = new JTextField("http://localhost:8080/struts/login.action");
		urlJTextField.setColumns(10);
		urlJTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) { 
					rightJPopupMenu=new JPopupMenu();
					final JMenuItem paste = new JMenuItem("\u7c98\u8d34");
					rightJPopupMenu.add(paste);
					rightJPopupMenu.show(urlJTextField, e.getX(), e.getY());
					paste.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (e.getSource() == paste) {
								urlJTextField.paste();
							}
						}
					});
				}
			}
		});
		
		urlJTextField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				String target=urlJTextField.getText().trim();
				String version=versionJComboBox.getSelectedItem().toString();
				MyURL targetURL=null;
				
				if (target.contains("http://")) {
					targetURL=new MyHttpURL(target);
				}else if(target.contains("https://")){
					targetURL=new MyHttpsURL(target);
				}else {
					JOptionPane.showMessageDialog(null,"\u8bf7\u68c0\u67e5\u8f93\u5165\u7684\u0055\u0052\u004c\u662f\u5426\u6b63\u786e");
					return;
				}
				
				serverInfoPanel.setMyURL(targetURL);
				serverInfoPanel.setVersion(version);
				
				excutePanel.setMyURL(targetURL);
				excutePanel.setVersion(version);
				
				fileUploadPanel.setMyURL(targetURL);
				fileUploadPanel.setVersion(version);
				
				customUploadPanel.setMyURL(targetURL);
				customUploadPanel.setVersion(version);
				
				fileViewPanel.setMyURL(targetURL);
				fileViewPanel.setVersion(version);
				
			}
			
			@Override
			public void focusGained(FocusEvent e) {}
		});
		
		String[] versions={"s019","s016","s09"};
		
		versionJComboBox= new JComboBox(versions);
		
		versionJComboBox.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				
				String version=versionJComboBox.getSelectedItem().toString();
				
				serverInfoPanel.setVersion(version);
				
				excutePanel.setVersion(version);
				
				fileUploadPanel.setVersion(version);
				
				customUploadPanel.setVersion(version);
				
				fileViewPanel.setVersion(version);
			}
			
			@Override
			public void focusGained(FocusEvent e) {}
		});
		
		
		JLabel lblNewLabel = new JLabel("URL");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		serverInfoPanel = new ServerInfoPanel();
		excutePanel = new ExcutePanel();
		fileUploadPanel = new FileUploadPanel();
		customUploadPanel = new CustomUploadPanel();
		fileViewPanel=new FileViewPanel();
		
		tabbedPane.addTab("\u670d\u52a1\u5668\u4fe1\u606f", serverInfoPanel);
		tabbedPane.addTab("\u547d\u4ee4\u6267\u884c", excutePanel);
		tabbedPane.addTab("\u4e0a\u4f20\u6587\u4ef6\u5230\u9879\u76ee\u6839\u76ee\u5f55", fileUploadPanel);
		tabbedPane.addTab("\u4e0a\u4f20\u6587\u4ef6\u5230\u81ea\u5b9a\u4e49\u76ee\u5f55", customUploadPanel);
		tabbedPane.addTab("\u6587\u4ef6\u6d4f\u89c8", fileViewPanel);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(19)
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
							.addGap(9))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(27)
							.addComponent(lblNewLabel)
							.addGap(18)
							.addComponent(urlJTextField, GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(versionJComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(37)))
					.addGap(14))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(urlJTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(versionJComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 464, Short.MAX_VALUE)
					.addGap(47))
		);
		getContentPane().setLayout(groupLayout);
	}
	
}
