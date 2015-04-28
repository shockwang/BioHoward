package module.client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI {
	private JFrame frame;
	private JTextField input;
	private JTextArea status;
	private JTextArea screen;
	private static boolean isExists = false;
	private DataOutputStream outToServer = null;
	
	public ClientGUI(){
		if (isExists) return;
		isExists = true;
		
		frame = new JFrame();
		frame.setSize(600, 700);
		//frame.setResizable(false);
		
		status = new JTextArea();
		status.setEditable(false);
		status.setFont(new Font("monospaced", Font.PLAIN, 16));
		
		screen = new JTextArea();
		screen.setEditable(false);
		screen.setFont(new Font("monospaced", Font.PLAIN, 16));
		JScrollPane scroll = new JScrollPane(screen);
		screen.getDocument().addDocumentListener(new LimitLinesDocumentListener(200));
		
		input = new JTextField();
		input.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField src = (JTextField) e.getSource();
				screen.append(src.getText() + "\n");
				
				try {
					byte[] b = (src.getText() + "\n").getBytes("UTF-8");
					outToServer.write(b);
				} catch (IOException err){
					screen.append("Error: send message to server error.\n");
				} catch (NullPointerException err){
					screen.append("Error: OutToServer is null.\n");
				} finally {
					src.setText("");
				}
			}
		});
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c0 = new GridBagConstraints();
		c0.gridx = 0;
		c0.gridy = 0;
		c0.weightx = 1;
		c0.weighty = 0.0;
		c0.fill = GridBagConstraints.BOTH;
		c0.anchor = GridBagConstraints.WEST;
		c0.ipady = 470;
		frame.add(scroll, c0);
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 1;
		c1.weightx = 1;
		c1.weighty = 0.0;
		c1.fill = GridBagConstraints.BOTH;
		c1.anchor = GridBagConstraints.WEST;
		c1.ipady = 130;
		frame.add(status, c1);
		
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 2;
		c2.weightx = 1;
		c2.weighty = 0.0;
		c2.fill = GridBagConstraints.BOTH;
		c2.anchor = GridBagConstraints.WEST;
		c2.ipady = 10;
		frame.add(input, c2);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set frame location on the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		frame.setVisible(true);
		input.requestFocusInWindow();
	}
	
	public JTextArea getStatusPanel(){ return status;}
	
	public JTextArea getScreenPanel(){ return screen;}
	
	public void setOutToServer(DataOutputStream in){
		try {
			if (this.outToServer != null) this.outToServer.close();
			this.outToServer = in;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JFrame getMainFrame() {return frame;}
}
