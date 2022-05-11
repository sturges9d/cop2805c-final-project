/*
 * Class......: COP 2805C-22809
 * Name.......: Stephen Sturges
 * Date.......: 04/21/2022
 * Description: Server class for COP 2805C, final project.
 */
package cop2805;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Server {

	public static void main(String[] args) {
		ServerSocket server = null;
		boolean shutdown = false;
		
		// Open the Server Socket.
		try {
			server = new ServerSocket(1236);
			System.out.println("Port bound. Accepting connections...");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} // End of socket try-catch block.
		
		while(!shutdown) {
			Socket client = null;
			InputStream input = null;
			OutputStream output = null;
			WordSearcher ws = new WordSearcher("hamlet.txt");
			
			try {
				// Establish connection and get streams.
				client = server.accept();
				input = client.getInputStream();
				output = client.getOutputStream();
				
				// Read input from Client.
				int n = input.read();
				byte[] data = new byte[n];
				input.read(data);				
				String clientInput = new String(data, StandardCharsets.UTF_8);
				System.out.println("Client requested: " + clientInput);
				
				// Search file for requested string and return line numbers.
				List<Integer> returnList = ws.SearchLines(clientInput);
				System.out.println("Server response: " + returnList);
				for(int i = 0; i < returnList.size(); i++) {
					String response = (returnList.get(i).toString() + "\n");
					output.write(response.getBytes());
				}

				// Close the client connection.
				client.close();
				
				// Exit the while loop.
				if(clientInput.equalsIgnoreCase("shutdown")) {
					System.out.println("Server shutting down...");
					shutdown = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			} // End of IO try-catch block.
		} // End of while loop. 
	} // End of main method.
} // End of Server class.
