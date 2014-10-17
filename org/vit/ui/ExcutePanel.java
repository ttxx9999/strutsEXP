package org.vit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.vit.model.MyURL;
import org.vit.service.ExploitService;
import org.vit.service.impl.Struts2_S016_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S019_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S09_ExploitServiceImpl;

public class ExcutePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextPane serverEchoPane;
	private JButton exeJButton;
	private JTextField commandJTextField;
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

	public ExcutePanel(){
		
		setSize(600, 400);
		serverEchoPane = new JTextPane();
		serverEchoPane.setEditable(false);
		JScrollPane serverEchoJscroPane=new JScrollPane(serverEchoPane);
		
		JLabel lblNewLabel = new JLabel("\u547d\u4ee4");
		
		commandJTextField = new JTextField("whoami");
		commandJTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar()==KeyEvent.VK_ENTER) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							serverEchoPane.setText("\u8bf7\u7a0d\u5019...");
							String commandContent=commandJTextField.getText().trim();
							String serverEchoInfo=doExecuteCommand(commandContent);
							serverEchoPane.setText(serverEchoInfo);
							serverEchoPane.setCaretPosition(0);
						}
					}).start();
				}
			}
		});
		
		commandJTextField.setColumns(10);
		
		exeJButton = new JButton("\u6267\u884c");
		exeJButton.addActionListener(this);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(serverEchoJscroPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(commandJTextField, GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(exeJButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(commandJTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(exeJButton))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(serverEchoJscroPane, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==exeJButton) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					serverEchoPane.setText("\u8bf7\u7a0d\u5019...");
					String commandContent=commandJTextField.getText().trim();
					String serverEchoInfo=doExecuteCommand(commandContent);
					serverEchoPane.setText(serverEchoInfo);
					serverEchoPane.setCaretPosition(0);
				}
			}).start();
		}
	}
	
	public String doExecuteCommand(String cmd){
		
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
			return service.doExecutCMD(myURL, cmd);
		} catch (Exception e) {
			return "\u547d\u4ee4\u6267\u884c\u9519\u8bef£º"+e.toString();
		}
		
	}
}
