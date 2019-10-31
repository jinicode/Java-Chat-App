import javax.swing.JFrame;

public class ClientTest {
public static void main(String[] args) {
	Client jarvis;
	jarvis=new Client("127.0.0.1");//localhost since we need to pass ip 
	jarvis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jarvis.startRunning();
}
}
