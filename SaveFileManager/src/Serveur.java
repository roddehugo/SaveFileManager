import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


/**
 * @file Serveur.java
 * @package 
 * @project SaveFileManager
 * @date 5 juil. 09
 * @user hugo
 */

public class Serveur {
	
	public Socket service;
	public JFrame frame;
	public static JTextArea journal;
		
	public Serveur() throws IOException {
			frame = new JFrame();
			frame.setTitle("Gestion Sauvegarde Serveur");
			frame.setSize(400,300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(true);
			frame.setLocationRelativeTo(null);
			frame.setUndecorated(false);
			frame.setBackground(Color.white);
			frame.setContentPane(contentPane());
			frame.setVisible(true);
			initServeur();		
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	private void initServeur() throws IOException {
		ServerSocket serveur = new ServerSocket(1507);
		new WhatIDoServeur("Serveur Disponible");
		
		while(true) {
			service = serveur.accept();
			Thread client = new Thread(new MonRunnable(service));
			client.start();
		}
		
	}

	/**
	 * @return
	 */
	private JPanel contentPane() {
		JPanel master = new JPanel(new BorderLayout());
		
		journal = new JTextArea();
			journal.setEditable(false);
		
		JScrollPane scrollJ = new JScrollPane(journal);
		
		master.add(scrollJ, BorderLayout.CENTER);
		
		return master;
	}

	
	public static void main(String[] args) throws IOException {
		
		String lookAndFeelName = UIManager.getSystemLookAndFeelClassName();
		
		try {

			UIManager.setLookAndFeel(lookAndFeelName);
		} 
		catch (ClassNotFoundException e) {} 
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		
		new Serveur();
		
	}}
