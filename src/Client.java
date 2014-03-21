import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


//tjena
//tja


@SuppressWarnings("serial")
public class Client extends JFrame
{
	
	
	


	ArrayList<String> History = new ArrayList<String>();

	String IP = "localhost";
	
	int PORT = 7657;
	String latest = "";
	JLabel adr = new JLabel("Adress:");
	JTextField inputField = new JTextField(50);
	JEditorPane textBox = new JEditorPane();
	JScrollPane scroll = new JScrollPane(textBox);
	JButton search = new JButton("Search");
	JButton refresh = new JButton("Refresh");
	JButton historyButton = new JButton("history");
	JButton createWebsite = new JButton("Create a new post");
	
	static String NL = System.getProperty("line.separator");







	static PrintWriter output;
	static PrintWriter upload;
	static BufferedReader input;

	public static void main(String[] args) throws BadLocationException{
		new Client();
	

	}

	public Client() throws BadLocationException
	{

		JPanel p = new JPanel(new GridBagLayout());



		this.getContentPane().add(p,BorderLayout.NORTH);
		
		p.setBackground(Color.lightGray);
		
		
	

		GridBagConstraints c = new GridBagConstraints();


		//spacing between components
		c.insets = new Insets(5,5,5,5);


		//adding buttons and other stuff to the north toolbaar

		p.add(adr, c);
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
		p.add(createWebsite,c);
		c.gridx = 6;
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



		add(scroll, BorderLayout.CENTER);








		inputField.addActionListener(new TextFieldListener());
		BasicButtonListener listneer = new BasicButtonListener();
		search.addActionListener(listneer);
		refresh.addActionListener(listneer);
		historyButton.addActionListener(listneer);
		createWebsite.addActionListener(listneer);




		setTitle("Web client");
		setSize(1000,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);


		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket(IP, PORT);
			output = new PrintWriter(socket.getOutputStream(), true);
			upload = new PrintWriter(socket.getOutputStream(), true);
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


			else if(e.getSource() == createWebsite){

				try {

					showCreateWebsite();



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

	public static void appendString(String str,JEditorPane pane,String color) throws BadLocationException, IOException
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		frame.setVisible(true);
		frame.setSize(500,500);

	}

	public static void showCreateWebsite(){
		
		 try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			 } catch (Exception e) {
			            e.printStackTrace();
			 }
		
		final JFrame frame = new JFrame("Create a new post");
		JPanel panel = new JPanel(new GridBagLayout());
		JPanel buttons = new JPanel(new GridBagLayout());
		final JTextField wName = new JTextField(12);

		final JTextField pw = new JTextField(12);
		final JTextArea code = new JTextArea(40,40);
		JScrollPane scrollPane = new JScrollPane(code);
		JScrollPane scrollPanel = new JScrollPane(panel);
		JScrollPane scrollButtons = new JScrollPane(buttons);
		final JButton preview = new JButton("Preview website");
		final JButton publish = new JButton("Publish post");
		JButton addImage = new JButton("Add image");
		JButton addHor = new JButton("Add horizontal line");
		JButton newLine = new JButton("Add new line");
		JButton newHeader = new JButton("Add new header");
		JButton newBody = new JButton("Add new body");
		JButton newArticle = new JButton("Add new article");
		JButton newFont = new JButton("Add new font");
		JButton centerPage = new JButton("Make the page centered");
		JButton newBdiList = new JButton("Add new bdi list");
		JButton newInput = new JButton("Add new inputbox");
		JButton newDfn = new JButton("Add new definition");
		JButton syntax = new JButton("Syntax code");
		
		panel.setBackground(Color.lightGray);
		buttons.setBackground(Color.gray);
		
		publish.setBackground(Color.green);

	
		







		frame.getContentPane().add(scrollPanel,BorderLayout.CENTER);
		frame.getContentPane().add(scrollButtons,BorderLayout.EAST);

		GridBagConstraints c = new GridBagConstraints();



		c.insets = new Insets(1,1,1,1);

		c.gridx = 1;
		c.gridy = 0;
		panel.add(new JLabel("Website admin password"),c);
		c.gridx = 1;
		c.gridy = 1;
		panel.add(pw,c);
		c.gridx = 1;
		c.gridy = 2;
		panel.add(new JLabel("Post title:"), c);

		c.gridx = 1;
		c.gridy = 3;
		panel.add(wName, c);

		c.gridx = 1;
		c.gridy = 4;
		panel.add(new JLabel("HTML code:"), c);

		c.gridx = 1;
		c.gridy = 5;
		panel.add(scrollPane, c);
		
		c.gridx = 1;
		c.gridy = 5;
		panel.add(preview, c);
		c.gridx = 1;
		c.gridy = 6;
		panel.add(publish, c);

		c.gridx = 0;
		c.gridy = 0;
		buttons.add(addImage, c);
		c.gridx = 1;
		c.gridy = 0;
		buttons.add(addHor, c);
		c.gridx = 2;
		c.gridy = 0;
		buttons.add(newLine, c);

		c.gridx = 0;
		c.gridy = 1;
		buttons.add(newHeader, c);
		c.gridx = 1;
		c.gridy = 1;
		buttons.add(newBody, c);
		c.gridx = 2;
		c.gridy = 1;
		buttons.add(newArticle, c);
		c.gridx = 0;
		c.gridy = 2;
		buttons.add(newFont, c);
		c.gridx = 1;
		c.gridy = 2;
		buttons.add(centerPage, c);
		c.gridx = 2;
		c.gridy = 2;
		buttons.add(newBdiList, c);
		c.gridx = 0;
		c.gridy = 3;
		buttons.add(newInput, c);
		c.gridx = 1;
		c.gridy = 3;
		buttons.add(newDfn, c);
		c.gridx = 2;
		c.gridy = 3;
		buttons.add(syntax, c);
		
		




		frame.setSize(2000,2000);
		frame.setVisible(true);

		addImage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<img src='PUT IMAGE LINK HERE'>"+NL);


			}
		});
		
		
		addHor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<hr>"+NL);


			}
		});

		newLine.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<p>"+NL);


			}
		});
		
		newHeader.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<header>"+NL+NL+"PUT CONTENT HERE"+NL+NL+"</header>"+NL);


			}
		});
		
		newBody.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<body>"+NL+NL+"PUT CONTENT HERE"+NL+NL+"</body>"+NL);


			}
		});
		
		newArticle.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<article>"+NL+"<h1> This is a title </h1>"+NL+"<p> This is some text </p>"+NL+"</article>"+NL);


			}
		});
		
		newFont.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.append("<font size='2' color='blue'>This is some text!</font>"+NL);
				


			}
		});
		
		centerPage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String text = "<center>"+NL+NL+code.getText()+NL+NL+"</center>";
				code.setText(text);


			}
		});
		
		
		newBdiList.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String text = "<ul>"+NL+"<li>User <bdi>hrefs</bdi>: 60 points</li>"+NL+"<li>User <bdi>jdoe</bdi>: 80 points</li>"+NL+"<li>User <bdi>إيان</bdi>: 90 points</li>"+NL+"</ul>"+NL;
				code.append(text);


			}
		});
		
		newInput.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String text = "<input list='name'>"+NL;
				code.append(text);


			}
		});
		
		newDfn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String text = "<dfn>Definition term text</dfn>"+NL;
				code.append(text);


			}
		});
		
		syntax.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				code.setText(editWords(code.getText(), "<html>"));


			}
		});
		
		preview.addActionListener(new ActionListener(){
			@SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e){
				
				
					
						try {
							HtmlPreview prev = new HtmlPreview(code.getText());
						} catch (BadLocationException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	

			
			}
		});


		publish.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				String c = code.getText();
				String n = wName.getText();
				if(!c.isEmpty() && !n.isEmpty()){
				
				String webSiteName = "/websitename "+wName.getText();

				boolean x = true;
				while(x == true){

					output.println(webSiteName);


					try {
						if (input.readLine().contains("ok")){

							x = false;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}


				}





				try {
					writeToFile("index.txt",code.getText());


					
					String websiteText = "/website"+"<html>"+readFile("index.txt").replaceAll(NL," ")+"</html>";





					boolean y = true;
					while(y == true){

						output.println(websiteText);

						if(input.readLine().contains("ok")){
							y = false;
						}

					}




				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}



				frame.dispose();
				
				}else{
					JOptionPane.showMessageDialog(null, "ERROR: name or code is empty.");
				}

			}
		}); 




	}

	public static void createFolder(String name){
		File dir = new File(name);
		dir.mkdir();
	}
	
	public static void showHtmlPreview(String text) throws BadLocationException, IOException{
		JFrame fr = new JFrame();
		JPanel pan = new JPanel();
		JEditorPane textPane = new JEditorPane();
		JScrollPane scr = new JScrollPane(textPane);
		
		
		
		fr.add(pan);
		pan.add(scr);
		
		
		HTMLDocument doc = (HTMLDocument)textPane.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit)textPane.getEditorKit();

		editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
		
		//appendString(text, textPane, "black");
		
		fr.setSize(1000,1000);
		fr.setVisible(true);
		
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

	public static void writeToFile(String fileName,String text) throws IOException{




		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));








			out.append(text);



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
	
	public static String editWords(String text,String WORD){
		
		
		String t = "";
		
		for (String word : text.split("\\s+")){
		    if (word.contains(WORD)){
		        
		    	
		    	
		        word = word.toUpperCase()+NL;
		        
		        
		        
		        
		    }
		    
		    t += word;
		}
		
		return t;
		
		
		
	}
	
	public static Color randomColor(){
		
		Random rand1 = new Random();
		int rnd1 = rand1.nextInt(255);
		
		Random rand2 = new Random();
		int rnd2 = rand2.nextInt(255);
		
		Random rand3 = new Random();
		int rnd3 = rand3.nextInt(255);
		
		Color color = new Color(rnd1,rnd2,rnd3);
		
		return color;
	}



}