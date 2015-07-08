package client;
import java.awt.Cursor;
import java.io.PrintWriter;


/**
 * @file Supprimer.java
 * @package 
 * @project SaveFileManager
 * @date 16 déc. 2009
 * @user hugo
 */
public class Supprimer {

	public Connect connection;
	public String user = System.getProperty("user.name");
	public String sep = System.getProperty("file.separator");
	public Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	
	public Supprimer(String file) {
		connection = new Connect();
		String[] messages = {"supprimer",user,file};
		sendMessage(messages);
	}
	
	public void sendMessage(String[] messages) {
		PrintWriter writer = new PrintWriter(connection.getOutputStream());
		for (String string : messages) {
			writer.println(string);
		}
		writer.flush();
		
	}
}
