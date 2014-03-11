import java.awt.BorderLayout;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.ArrayList;


import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


@SuppressWarnings("serial")
public class Client extends JFrame {
	ArrayList<String> History = new ArrayList<String>();
	
	String IP = "localhost";
	int PORT = 7657;
	String latest = "";
	JTextField inputField = new JTextField(50);
	JEditorPane textBox = new JEditorPane();
	JButton search = new JButton("Search");
	JButton refresh = new JButton("Refresh");
	JButton historyButton = new JButton("history");




	PrintWriter output;
	BufferedReader input;

	public static void main(String[] args) throws BadLocationException{
		new Client();
	}

	public Client() throws BadLocationException
	{
		
		JPanel p = new JPanel(new GridBagLayout());

		
		
		this.getContentPane().add(p,BorderLayout.NORTH);
		
		GridBagConstraints c = new GridBagConstraints();
		
		
		
		c.insets = new Insets(5,5,5,5);
		
		p.add(new JLabel("Adress:"), c);
		c.gridx = 1;
		c.gridy = 0;
		
		p.add(inputField, c);
		c.gridx = 2;
		c.gridy = 0;
		p.add(search,c);
		c.gridx = 3;
		c.gridy = 0;
		p.add(refresh, c);
		c.gridx = 4;
		c.gridy = 0;
		p.add(historyButton,c);
		c.gridx = 5;
		c.gridy = 0;
		
		
		
		//this.pack();


		

				/*layout.setVerticalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				           .addComponent(c1)
				           .addComponent(c2)
				           .addComponent(c3))
				      .addComponent(c4)
				);*/





		textBox.setEditable(false);
		textBox.setContentType("text/html;charset=UTF-8");

		//setLayout(new BorderLayout());
		
		
		
		add(new JScrollPane(textBox), BorderLayout.CENTER);
		
		
	





		inputField.addActionListener(new TextFieldListener());
		BasicButtonListener listneer = new BasicButtonListener();
		search.addActionListener(listneer);
		refresh.addActionListener(listneer);
		historyButton.addActionListener(listneer);




		setTitle("Web client");
		setSize(1000,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);


		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket(IP, PORT);
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


			String inputString = input.readLine();

			appendString(inputString+"\n",textBox,"black");
		} catch (IOException exception) {
			System.out.println("Error: " + exception);
		}
		while(true){
			try {
				String in = input.readLine();
				if(in == null){
					JOptionPane.showMessageDialog(null, "Server closed!, Closing browser...");
					System.exit(0);

					return;
				}
				if(in.equals("/clear/")){
					textBox.setText("");
					continue;
				}

				appendString(in+"\n",textBox,"black");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}



	}



	class TextFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				String userInput = inputField.getText();
				latest = userInput;
				

				output.println(userInput);
				inputField.setText("");
				
				if(userInput.length() > 1){
				saveHistory(userInput);
				}

			} catch (Exception ex) {
				System.out.println(ex);
			}


		}
	}

	class BasicButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == search){

				try {


					String userInput = inputField.getText();
					latest = userInput;
					
					
					
					

					output.println(userInput);
					inputField.setText("");
					
					if(userInput.length() > 1){
					saveHistory(userInput);
					}
					
					
					

				} catch (Exception ex) {
					System.out.println(ex);
				}

			}
			else if(e.getSource() == refresh){

				try {


					String userInput = latest;


					output.println(userInput);
					inputField.setText("");


				} catch (Exception ex) {
					System.out.println(ex);
				}
				


			}else if(e.getSource() == historyButton){
				
				try {


					showHistory(History);


				} catch (Exception ex) {
					System.out.println(ex);
				}
				
			}else{

				try {


					String userInput = inputField.getText();
					latest = userInput;
					
					

					output.println(userInput);
					inputField.setText("");
					
					saveHistory(userInput);


				} catch (Exception ex) {
					System.out.println(ex);
				}

			}


		}
	}

	public void appendString(String str,JEditorPane pane,String color) throws BadLocationException, IOException
	{


		HTMLDocument doc = (HTMLDocument)pane.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit)pane.getEditorKit();

		editorKit.insertHTML(doc, doc.getLength(), str, 0, 0, null);


	}
	
	public void saveHistory(String s) throws IOException{
		
		
		
		History.add(s+" | "+getDate());
		writeListToFile("History.txt",History);
		
		
		
		
		
		
		
	}
	
	public static void showHistory(ArrayList<String> info){
		JFrame frame = new JFrame("Browser history");

		JTextArea text = new JTextArea();
		
		text.setEditable(false);
		

		frame.add(new JScrollPane(text), BorderLayout.CENTER);
		
		//String[] data;
		
		//for(int i = 0; i < info.size();i += 1){
			//text.append(info.get(i)+"\n");
		//}
		try {
			text.append(readFile("history.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		frame.setVisible(true);
		frame.setSize(500,500);
		
	}
	
	
	public static void writeListToFile(String fileName,ArrayList<String> text) throws IOException{
		        try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		        		
		            	for(int x = 0; x < text.size(); x += 1 ){
		            	
		            	out.write(text.get(x));
		                out.newLine();
		            	}
		            
		            out.close();
		        } catch (IOException e) {}
		    
	}
	
	public static String readFile(String fileName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        String everything = sb.toString();
	        
	        return everything;
	    } finally {
	        br.close();
	    }
	    
	    
	    
	}
	
	public static java.util.Date getDate(){
	
		java.util.Date date = new java.util.Date();
		
		return date;
	 
	}



}