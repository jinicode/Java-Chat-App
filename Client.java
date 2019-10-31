import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	public JTextArea chatWindow;
	public JTextField userText;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;

	public Client(String host) {
		// in host we r sending the ip address of the server which we want to cinnect to
		super("Client FOMO..");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userText.setText("");

			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 150);
		setVisible(true);
	}

	// connect to server
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (EOFException eofException) {
			showMessage("\n Client terminated connection ");
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	public void connectToServer() throws IOException {
		showMessage("Attempting conncection...\n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to " + connection.getInetAddress().getHostName());
	}

	public void showMessage(String m) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				chatWindow.append(m);

			}
		});
	}

	// setup to send and receive messages
	public void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n your streams are good to go...\n");
	}

	public void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n " + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("I don't know the type of object ");
			}
		} while (!message.equals("SERVER - END"));
	}

	public void closeCrap() {
		ableToType(false);
		showMessage("Closing everything");
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - " + message);
		} catch (IOException ioException) {
			chatWindow.append("\n smthing went wrong ");
		}
	}

	public void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				userText.setEditable(tof);

			}
		});
	}

}
