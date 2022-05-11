/*
 * Class......: COP 2805C-22809
 * Name.......: Stephen Sturges
 * Date.......: 04/21/2022
 * Description: Client class for COP 2805C, final project.
 */
package cop2805;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Client {
	
	public static void constructGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			JFrame.setDefaultLookAndFeelDecorated(true);
		}
		SearchFrame frame = new SearchFrame();
		frame.setVisible(true);
	} // End of constructGUI method.
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				constructGUI();
			}
		});
	} // End of main method.
	
} // End of Client class.

class ButtonListener implements ActionListener{
	SearchFrame frame;
	
	public ButtonListener(SearchFrame frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent event) {
		try	{	
			// Clear JList.
			frame.listModel.clear();
			
			// Read input from user.
			String userString = frame.searchField.getText();
			
			// Open Socket.
			Socket connection = new Socket("127.0.0.1",1236);
			InputStream input = connection.getInputStream();
			OutputStream output = connection.getOutputStream();
			
			// Send data to server.
			output.write(userString.length());
			output.write(userString.getBytes());

			// Read data from Server.
			BufferedReader serverInput = new BufferedReader(new InputStreamReader(input));
			String line = "";
			int i = 0;
			while(line != null) {
				line = serverInput.readLine();
				if(line != null)
					frame.listModel.add(i, Integer.parseInt(line));
				i++;
			}

			// Close Socket.
			if(!connection.isClosed())
				connection.close();

		} catch (IOException e) {
			e.printStackTrace();
		} // End of try-catch block.
	} // End of actionPerformed method.
} // End of ButtonListener class.

class SearchFrame extends JFrame{
	public JLabel searchLabel;
	public JTextField searchField;
	public JLabel listLabel;
	DefaultListModel<Integer> listModel = new DefaultListModel<Integer>();
	public JList<Integer> responseList;
	public JButton transmitButton;
	
	public SearchFrame() {
		super();
		init();
	} // End of SearchFrame constructor.
	
	private void init() {
		// Initialize elements.
		Font fontBold = new Font("Deafult",Font.BOLD,12);
		Font font = new Font("Default",Font.PLAIN,12);
		
		searchLabel = new JLabel("Word to Search For:");
		searchLabel.setFont(fontBold);
		
		searchField = new JTextField();
		searchField.setFont(font);
		
		listLabel = new JLabel("Response:");
		listLabel.setFont(fontBold);
		
		responseList = new JList<Integer>(listModel);
		responseList.setFont(fontBold);
		JScrollPane listScrollPane = new JScrollPane(responseList);
		
		transmitButton = new JButton("Transmit");
		transmitButton.setFont(font);
		transmitButton.addActionListener(new ButtonListener(this));
		
		// Define frame properties.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Word Searcher");
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// Add elements to frame.
		this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(searchLabel, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 1;
		c.gridy = 0;
		this.add(searchField, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		this.add(listLabel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 80;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		this.add(listScrollPane, c);
		
		c.fill = GridBagConstraints.NONE;
		c.ipady = 0;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		this.add(transmitButton,c);
		
		// Frame size and placement properties.
		int frameWidth = 350;
		int frameHeight = 190;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(
			(int) ((screenSize.getWidth()/2) - (frameWidth/2)),
			(int) ((screenSize.getHeight()/2) - (frameHeight/2)),
			frameWidth,
			frameHeight
		);
		this.setResizable(false);
	} // End of init method.
} // End of SearchFrame class.
