import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Calculator extends JFrame implements ActionListener{
	JLabel lblHistory,lblResult;
	String strMemory[]= {"MC","MR","M+","M-","MS"},strOthers[]= {"%","Sqrt","Sqr"," 1/x","CE","C","\u232b","/","7","8","9","*","4","5","6","-","1","2","3","+","\u2213","0",".","="};
	JPanel panTop,panBottom,panMemory;
	JButton btnMemory[]=new JButton[5],btnOthers[]=new JButton[24];
	double history,memory;
	boolean flag,digit;
	char op;
	Calculator(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lblHistory=new JLabel("");
		lblHistory.setHorizontalAlignment(SwingConstants.RIGHT);
		
		lblResult=new JLabel("0");
		lblResult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblResult.setFont(new Font(Font.SERIF,Font.BOLD,35));
		
		panTop=new JPanel(new GridLayout(3,1));
		panMemory=new JPanel(new GridLayout(1,5,10,10));
		panBottom=new JPanel(new GridLayout(6,4,10,5));
		
		for(int i=0;i<btnMemory.length;i++) {
			btnMemory[i]=new JButton(strMemory[i]);
			btnMemory[i].setBorderPainted(false);
			btnMemory[i].setFocusable(false);
			btnMemory[i].setBorder(BorderFactory.createEmptyBorder());
			btnMemory[i].setBackground(new Color(240,240,240));
			btnMemory[i].addActionListener(this);
			panMemory.add(btnMemory[i]);
			btnMemory[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					JButton btn=(JButton)me.getSource();
					btn.setBackground(Color.LIGHT_GRAY);
				}
				public void mouseExited(MouseEvent me) {
					JButton btn=(JButton)me.getSource();
					btn.setBackground(new Color(240,240,240));
				}
			});
		}
		for(int i=0;i<btnOthers.length;i++) {
			btnOthers[i]=new JButton(strOthers[i]);
			btnOthers[i].setBorderPainted(false);
			btnOthers[i].setFocusable(false);
			btnOthers[i].addActionListener(this);
			btnOthers[i].setBorder(BorderFactory.createEmptyBorder());
			panBottom.add(btnOthers[i]);
			if(Character.isDigit(strOthers[i].charAt(0))){
				btnOthers[i].setBackground(Color.white);
				btnOthers[i].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						JButton btn=(JButton)me.getSource();
						btn.setBackground(Color.LIGHT_GRAY);
					}
					public void mouseExited(MouseEvent me) {
						JButton btn=(JButton)me.getSource();
						btn.setBackground(Color.WHITE);
					}
				});
			}
			else {
				btnOthers[i].setBackground(Color.lightGray);
				btnOthers[i].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent me) {
						JButton btn=(JButton)me.getSource();
						btn.setBackground(new Color(153,204,255));
					}
					public void mouseExited(MouseEvent me) {
						JButton btn=(JButton)me.getSource();
						btn.setBackground(Color.LIGHT_GRAY);
					}
				});				
			}
		}
		panTop.add(lblHistory);
		panTop.add( lblResult);
		panTop.add(panMemory);
		add(panTop,"North");
		add(panBottom);
		setSize(400,500);
		setTitle("CalCulator");
		this.setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Calculator();
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		String s1=((JButton)ae.getSource()).getText();
		double curValue=Double.parseDouble(lblResult.getText());
		if(Character.isDigit(s1.charAt(0))) {
			if(flag==false)//concat
				lblResult.setText(val(lblResult.getText() + s1.charAt(0)));
			else{//overlap
				lblResult.setText(s1.charAt(0)+"");
				flag=false;
			}
			digit=true;
		}
		else if(isOperator(s1.charAt(0))) {
			if(digit==false) {//op after op so we have to replace old op with new op
				op=s1.charAt(0);
				lblHistory.setText(lblHistory.getText().substring(0, lblHistory.getText().length()-1)+op);
				return;
			}
			digit=false;
			lblHistory.setText(lblHistory.getText()+val(lblResult.getText())+s1.charAt(0));
			if(op!='\u0000') {
				double current=curValue;
				double result=0;
				switch(op) {
					case '+':
						result=history+current;
						break;
					case '-':
						result=history-current;
						break;
					case '*':
						result=history*current;
						break;
					case '/':
						result=history/current;
						break;
					default:
						result=0;
				}				
				lblResult.setText(val(result+""));
			}
			history=Double.parseDouble(lblResult.getText());
			op=s1.charAt(0);
			flag=true;
		}
		else if(s1.charAt(0)=='=') {
			double current=curValue;
			double result;
			switch(op) {
				case '+':
					result=history+current;
					break;
				case '-':
					result=history-current;
					break;
				case '*':
					result=history*current;
					break;
				case '/':
					result=history/current;
					break;
				default:
					result=0;
			}
			history=0;
			op='\u0000';
			flag=true;
			lblHistory.setText("");
			lblResult.setText(val(result+""));
		}
		else if(s1.charAt(0)=='\u2213') {
			lblResult.setText(val(curValue*-1+""));
		}
		else if(s1.equalsIgnoreCase("%")) {
			lblResult.setText(val(history*curValue/100+""));
			lblHistory.setText("");
			history=0;
			op='\u0000';
			flag=true;
		}
		else if(s1.equalsIgnoreCase("sqrt")) {
			lblResult.setText(val(Math.sqrt(curValue)+"")+"");
			flag=true;
		}
		else if(s1.equalsIgnoreCase("sqr")) {
			lblResult.setText(val(curValue*curValue+""));
			flag=true;
		}
		else if(s1.equalsIgnoreCase(" 1/x")) {
			lblResult.setText(val(1/curValue+""));
			flag=true;
		}
		else if(s1.equalsIgnoreCase("CE")) {
			lblResult.setText("0");
			flag=true;
		}
		else if(s1.equalsIgnoreCase("C")) {
			lblResult.setText("0");
			lblHistory.setText("");
			history=0;
			op='\u0000';
			flag=true;
		}
		else if(s1.charAt(0)=='\u232b') {
			String ss=lblResult.getText().substring(0, lblResult.getText().length()-1);
			if(ss.isEmpty())
				ss="0";
			lblResult.setText(ss);
		}
		else if(s1.equalsIgnoreCase(".")) {
			if(lblResult.getText().indexOf('.')==-1)
				lblResult.setText(lblResult.getText()+".");
		}
		else if(s1.equalsIgnoreCase("MS")) {
			flag=true;
			memory=Double.parseDouble(lblResult.getText());
		}
		else if(s1.equalsIgnoreCase("M+")) {
			flag=true;
			memory+=Double.parseDouble(lblResult.getText());
		}
		else if(s1.equalsIgnoreCase("M-")) {
			flag=true;
			memory-=Double.parseDouble(lblResult.getText());
		}
		else if(s1.equalsIgnoreCase("MR")) {
			flag=true;
			lblResult.setText(val(memory+""));
		}
		else if(s1.equalsIgnoreCase("MC")) {
			memory=0;
			flag=true;
		}
	}  
 	String val(String s) {
		if(s.isEmpty())
			return "0";
		double n=Double.parseDouble(s);
		if(n==(int)n)
			return (int)n+"";
		else
			return n+"";
	}
	boolean isOperator(char ch){
		if(ch=='+' || ch== '-' || ch=='*' || ch=='/')
			return true;
		return false;
	}
}
