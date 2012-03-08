/**
 * @file Connect.java
 * @package client
 * @project SaveFileManager
 * @date 17 déc. 2009
 * @user hugo
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Connect {

	Socket connection = null;
	
	public Connect() {
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
	
	public void close() {
		try {
			connection.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public InputStream getInputStream() {
		try {
			return connection.getInputStream();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OutputStream getOutputStream() {
		try {
			return connection.getOutputStream();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
