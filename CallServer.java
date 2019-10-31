import javax.swing.JFrame;

public class CallServer {
	public static void main(String[] args) {
		Server jinit = new Server();
		jinit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jinit.startRunning();
	}
}
