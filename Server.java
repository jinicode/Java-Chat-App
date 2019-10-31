
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Server extends JFrame {// server is a standalone computer which anyone can access
	public JTextArea chatWindow;
	public JTextField userText;
	private ObjectOutputStream output;// outputstream is the data that flows from your computer to your friends
										// computer
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;// in java they actually name connection socket
	// setup the socket means setting up the connection

	public Server() {
		super("Jinit's Messaging App ");
		userText = new JTextField();
		userText.setEditable(false);// before u r connected to anyone u r not allowed to talk to anyone
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());// returns command string associated with it i.e textfield
				userText.setText("");

			}
		});
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300, 150);
		setVisible(true);
	}

	// setting up the server
	public void startRunning() {
		try {
			server = new ServerSocket(6789, 100);// server is a computer which can be accessed by a lot of people on
													// which there r many applications and u connect to a single appn by
													// port number 2nd parameter is the num of people who can access it
			while (true) {
				try {
					// connect and have conversation with some1 else
					waitForConnection();
					setupStreams();
					whileChatting();
				} catch (EOFException eofException) {// end of stream
					showMessage("\n Server ended the connection ");
				} finally {
					closeCrap();
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void waitForConnection() throws IOException {
		showMessage("Waiting for sm1 to connect.... ");
		connection = server.accept();// connection is the socket and server.accept() accepts sm1 request to connect
										// to u and sends it to socket
		showMessage("now connected to " + connection.getInetAddress().getHostName());
	}

	public void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());// creating a conncection to whatever computer we
																		// want to connect to
		output.flush();// smtimes data gets stop this sends it
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are setup \n");

	}

//during the conversation
	public void whileChatting() throws IOException {
		String message = "Wou are now connected ";
		sendMessage(message);
		ableToType(true);
		do {
			// have conversation
			try {
				message = (String) input.readObject();// input is socket from which they will send ...it views it as
														// object and store it in string
				showMessage("\n " + message);
			} catch (ClassNotFoundException classNotFoundException) {
				showMessage("\n idk what it sends ");
			}
		} while (!message.equals("CLIENT - END"));

	}

	// closing the streamss and sockets
	public void closeCrap() {
		showMessage("\n Closing connections....\n ");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	// send a message to client
	public void sendMessage(String message) {
		try {
			output.writeObject("SERVER- " + message);// sends message to output stream
			output.flush();
			showMessage("\nSERVER -" + message);
		} catch (IOException ioException) {
			chatWindow.append("\n ERROR ,I cant send it ");
		}
	}

	// update chat window
	public void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				chatWindow.append(text);
			}
		});
	}

	public void ableToType(final boolean tof) {
		// let the user type
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				userText.setEditable(tof);
			}
		});

	}
}

