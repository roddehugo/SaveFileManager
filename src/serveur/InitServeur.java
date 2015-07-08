package serveur;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @file InitServeur.java
 * @package 
 * @project SaveFileManager
 * @date 16 déc. 2009
 * @user hugo
 */
public class InitServeur implements Runnable{

	public Socket service;
	public final int PORT = 1507;
	public void run() {
		Thread client = null;
		try {
			ServerSocket serveur = new ServerSocket(PORT);
			new WhatIDoServeur("Serveur Disponible sur le port",""+PORT);

			while(true) {
				service = serveur.accept();
				client = new Thread(new MonRunnable(service));
				client.start();
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				service.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
