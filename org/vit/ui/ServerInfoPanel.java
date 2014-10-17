package org.vit.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.vit.model.MyURL;
import org.vit.service.ExploitService;
import org.vit.service.impl.Struts2_S016_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S019_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S09_ExploitServiceImpl;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ServerInfoPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private MyURL myURL;
	private String version;
	private JButton getJButton ;
	private JTextPane serverInfoJTextPane ;
	
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
	
	public ServerInfoPanel(){
		setSize(600, 400);
		
		getJButton = new JButton("\u83b7\u53d6");
		getJButton.addActionListener(this);
		
		serverInfoJTextPane = new JTextPane();
		serverInfoJTextPane.setEditable(false);
		
		JScrollPane scrollPane=new JScrollPane(serverInfoJTextPane);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(533, Short.MAX_VALUE)
					.addComponent(getJButton)
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(getJButton)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
					.addGap(8))
		);
		setLayout(groupLayout);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==getJButton) {
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					showInfo();
				}
			}).start();
		}
	}
	
	public void showInfo(){
		
		serverInfoJTextPane.setText("\u8bf7\u7a0d\u5019...");
		
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
		
		String content="";
		String error="";
		try {
			content=service.getRealPath(myURL)+"\r\n";
		} catch (Exception e) {
			error+=e.getMessage();
		}
		
		try {
			Map<String, String>properties=new LinkedHashMap<String,String>();
			properties.put("\u64cd\u4f5c\u7cfb\u7edf\uff1a", "os.name");
			properties.put("\u5f53\u524d\u7528\u6237\uff1a", "user.name");
			properties.put("\u5f53\u524d\u7528\u6237\u76ee\u5f55\uff1a", "user.home");
			properties.put("\u004a\u0052\u0045\u76ee\u5f55\uff1a", "java.home");
			
			Map<String, String>map= service.getServerInfo(myURL,properties);
			for (Entry<String,String> entry:map.entrySet()) {
				content+=entry.getKey()+entry.getValue()+"\r\n";
			}
		} catch (Exception e) {
			error+=e.toString();
		}
		
		if (error.length()>0) {
			serverInfoJTextPane.setText(content+"\u83b7\u53d6\u670d\u52a1\u5668\u4fe1\u606f\u9519\u8bef\u003a"+error);
		}else {
			serverInfoJTextPane.setText(content);
		}
	}
	
}
