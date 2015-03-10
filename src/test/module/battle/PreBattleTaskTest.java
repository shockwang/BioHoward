package test.module.battle;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import org.junit.Before;
import org.junit.Test;

public class PreBattleTaskTest {
	private class IntBox{
		public int MaxValue;
		public int currentValue;
		public String name;
		
		public IntBox(int value, String name){
			this.MaxValue = value;
			this.currentValue = 0;
			this.name = name;
		}
		
		public boolean isReady(){
			if (currentValue >= MaxValue){
				currentValue = 0;
				return true;
			}
			return false;
		}
	}
	
	private class BattleTask extends TimerTask{
		private ArrayList<IntBox> list = new ArrayList<IntBox>();
		
		public void addElement(int input, String name){
			list.add(new IntBox(input, name));
		}
		
		public void run(){
			double result;
			textpane.setText("");
			for (IntBox num : list) {
				result = (double) num.currentValue / (double) num.MaxValue * 100;
				textpane.setText(String.format("%d%%\t|%s\n", (int) result, num.name));
				if (num.isReady()){
					System.out.println("Someone is ready!");
				}
				else num.currentValue += 100;
			}
		}
	}
	
	private class InputListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextField input = (JTextField) e.getSource();
			System.out.println(input.getText());
			input.setText("");
		}
		
	}
	
	private BattleTask task;
	private JTextPane textpane;
	
	
	@Before
	public void setUp(){
		task = new BattleTask();
		
		// GUI setup
		JFrame demo = new JFrame();
		demo.setSize(400, 300);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textpane = new JTextPane();
		TabStop[] tabs = new TabStop[1];
		tabs[0] = new TabStop(40, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
		
		TabSet tabset = new TabSet(tabs);

	    StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
	        StyleConstants.TabSet, tabset);
	    textpane.setParagraphAttributes(aset, false);
	    textpane.setEditable(false);
		
		JTextField input = new JTextField();
		InputListener listener = new InputListener();
		input.addActionListener(listener);
		
		demo.getContentPane().add(BorderLayout.CENTER, textpane);
		demo.getContentPane().add(BorderLayout.SOUTH, input);
        
        demo.setVisible(true);
        input.requestFocusInWindow();
	}
	
	@Test
	public void someTest(){
		task.addElement(3000, "Tao/Ãü§À");
		//task.addElement(5000, "Kao/°ª¸d");
		
		Timer timer = new Timer();
		timer.schedule(task, 0, 100);
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e){}
		
		timer.cancel();
	}
	
}
