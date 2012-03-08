package client;
import java.awt.Cursor;
import java.io.*;
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
	
	public Connect connection;
	public String user = System.getProperty("user.name");
	public String sep = System.getProperty("file.separator");
	public Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	
	public Recevoir() {
		connection = new Connect();
		String[] messages = {"recevoir",user};
		sendMessage(messages);
		recupListFiles();
		connection.close();
		new WhatIDo("Déconnection"); 
	}

	public Recevoir(Vector<String> files) {
		Fenetre.frame.setCursor(waitCursor);
		for (String nomFile : files) {
			connection = new Connect();
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
    		
			receiveFile(target.getPath().toString()+sep+nomFile);
			connection.close();
		}
		Fenetre.frame.setCursor(Cursor.getDefaultCursor());
	}
	
	public void receiveFile(final String target){
		try {
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
		}catch(IOException e) {
			e.printStackTrace();
		}

		new WhatIDo("Fichier reçu"+target);
	}
	
	public void sendMessage(String[] messages) {
		PrintWriter writer = new PrintWriter(connection.getOutputStream());
		for (String string : messages) {
			writer.println(string);
		}
		writer.flush();
		
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
}
