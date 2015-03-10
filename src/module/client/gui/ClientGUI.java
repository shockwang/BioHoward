package module.client.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
		frame.setSize(600, 600);
		
		status = new JTextArea();
		status.setEditable(false);
		
		screen = new JTextArea();
		screen.setEditable(false);
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
		
		JPanel container = new JPanel();
		GridLayout layout = new GridLayout(2, 0);
		container.setLayout(layout);
		container.add(scroll);
		container.add(status);
		
		frame.getContentPane().add(BorderLayout.SOUTH, input);
		frame.getContentPane().add(BorderLayout.CENTER, container);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
	
}
