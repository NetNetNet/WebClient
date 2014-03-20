import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


public class HtmlPreview {
	
String code = "";
	
	public HtmlPreview(String code) throws BadLocationException, IOException{
		this.code = code;
		
		JFrame frame = new JFrame("Html preview");
		JPanel panel = new JPanel(new BorderLayout());
		JTextPane editor = new JTextPane();
		JScrollPane scroll = new JScrollPane(editor);
		
		editor.setContentType("text/html");
		editor.setEditable(false);
		
		frame.add(panel,BorderLayout.CENTER);
		panel.add(scroll, BorderLayout.CENTER);
		
		HTMLDocument doc = (HTMLDocument)editor.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit)editor.getEditorKit();

		editorKit.insertHTML(doc, doc.getLength(), code, 0, 0, null);
		
		frame.setSize(1000,1000);
		frame.setVisible(true);
		
	}
	


}