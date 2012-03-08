import java.awt.Cursor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JFileChooser;

/**
 * @file Recevoir.java
 * @package 
 * @project SaveFileManager
 * @date 10 août 09
 * @user hugo
 */

public class Recevoir {
	
	public Socket connection;
	public String user = System.getProperty("user.name");
	public String sep = System.getProperty("file.separator");
	public Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	
	public Recevoir() {
		connect();
		String[] messages = {"recevoir",user};
		sendMessage(messages);
		recupListFiles();
		try {
			connection.close();
			new WhatIDo("Déconnection");
		} catch (UnknownHostException e) {
			new WhatIDo("Problème de connection", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			new WhatIDo("Problème de connection", e.toString());
			e.printStackTrace();
		} 
	}

	public Recevoir(Vector<String> files) {
		Fenetre.frame.setCursor(waitCursor);
		for (String nomFile : files) {
			connect();
			String[] messages = {"recevoirFile",user,nomFile};
			sendMessage(messages);
			
			File target = null;
			JFileChooser chooserF = new JFileChooser();
			chooserF.setFileFilter(chooserF.getAcceptAllFileFilter());
			chooserF.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int i = chooserF.showOpenDialog(Fenetre.frame);
			if(i == JFileChooser.APPROVE_OPTION) {
				target = chooserF.getSelectedFile();
			}
    		
			try {
				receiveFile(target.getPath().toString()+sep+nomFile);
				connection.close();
			}catch (IOException e) {
				e.printStackTrace();
				new WhatIDo("Problème de I/O recevoir", e.toString());
			}
		}
		Fenetre.frame.setCursor(Cursor.getDefaultCursor());
	}
	
	public void receiveFile(String target) throws IOException {
		int lu;
		
		BufferedInputStream inBuffer = new BufferedInputStream(connection.getInputStream());
	
		BufferedOutputStream outBuffer = new BufferedOutputStream(new FileOutputStream(target));
	
		lu = inBuffer.read();
	
		int compteur = 0;
	
		while(lu > -1) {
			outBuffer.write(lu);
			lu = inBuffer.read();
		
			compteur++;
		}
	
		outBuffer.write(lu);
		outBuffer.flush();

		outBuffer.close();
		inBuffer.close();
		
		new WhatIDo("Fichier reçu"+target);
	}
	
	public void sendMessage(String[] messages) {
		try {
			PrintWriter writer = new PrintWriter(connection.getOutputStream());
			for (String string : messages) {
				writer.println(string);
			}
			writer.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void recupListFiles() {
		try {
			BufferedReader data = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			Fenetre.selection.clear();
			String line;
			while ((line = data.readLine()) != null) {
				Fenetre.selection.addElement(line);
			}
			Fenetre.liste.updateUI();
			Fenetre.enlever.setEnabled(true);
			new WhatIDo("Récuperation des fichiers");
		}
		catch (IOException e) {
			e.printStackTrace();
			new WhatIDo("Problème de I/O"+e.toString());
		}
	}
	
	public void connect() {			
		String ip = Fenetre.getIp();
		int port = Fenetre.getPort();
		try {
			connection = new Socket(ip,port);
			new WhatIDo("Connection établie avec", ip+" sur le port "+port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			new WhatIDo("Pas de serveur disponible",e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			new WhatIDo("Probème I/O serveur",e.toString());
		}
			
	}
}
