package client;
import java.awt.Cursor;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.*;

import javax.swing.JOptionPane;

/**
 * @author hugo
 *
 */
public class Envoyer {
	
	public String nom, noma;
	public File archive;
	public Date now = new Date();
	public SimpleDateFormat dateStandard = new SimpleDateFormat("dd.MM.yy-HH.mm.ss-");
	public String date = dateStandard.format(now);
	public Connect connection;
	public String user = System.getProperty("user.name");
	public String sep = System.getProperty("file.separator");
	public Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	
	public Envoyer(Vector<String> selection){
		compresser(selection);
		connection = new Connect();
		String[] messages = {"envoyer",user,date+noma};
		sendMessage(messages);
		File nomFile = new File(nom);
		sendFile(nomFile);
		
		connection.close();
		new WhatIDo("Déconnection"); 
		nomFile.delete();
	}
	
	public void compresser(Vector<String> selection) {
		noma = (String) JOptionPane.showInputDialog(Fenetre.frame, "Entrez le nom de l'archive sans '.zip'", "Nom archive",
				JOptionPane.OK_CANCEL_OPTION, null, null, "archive");
		if (noma != null) {
			try {
				if (!noma.endsWith(".zip")) {
						noma = noma+".zip";
				}
				nom = System.getProperty("user.home")+sep+date+noma;
				
				ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(nom).getCanonicalFile()));
				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(9);
				
				for (int i = 0;i < selection.size();i++) {
					File file = new File(selection.elementAt(i).toString()).getCanonicalFile();
					compressFile(file,"", out);
				}
				out.close();
				new WhatIDo("Compression : " + nom);
			} catch (FileNotFoundException e1) {
				new WhatIDo("Pas de fichiers", e1.toString());
				e1.printStackTrace();
			}
			catch (IOException e) {
				new WhatIDo("Problème I/O compréssion", e.toString());
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param file
	 * @param parentFolder 
	 * @param out 
	 * @throws IOException 
	 */
	public void compressFile(File file, String parentFolder, ZipOutputStream out ) throws IOException {
		String zipName = new StringBuilder(parentFolder).append(file.getName()).append(file.isDirectory() ? sep : "").toString();
	    
	    ZipEntry entry = new ZipEntry(zipName);
	    entry.setSize(file.length());
		entry.setTime(file.lastModified());
	    out.putNextEntry(entry);
	    

	    if (file.isDirectory()) {
	    	for (File f : file.listFiles())
	    		compressFile(f, zipName.toString(), out);
	    	return;
	    }

		InputStream in = new BufferedInputStream(new FileInputStream(file));
	    try {
	        byte[] buf = new byte[8192];
	        int bytesRead;
	        while (-1 != (bytesRead = in.read(buf)))
	            out.write(buf, 0, bytesRead);
	    } finally {
	        in.close();
	    }
	}
	
	public void sendMessage(String[] messages) {
		PrintWriter writer = new PrintWriter(connection.getOutputStream());
		for (String string : messages) {
			writer.println(string);
		}
		writer.flush();
		
	}
	
	public void sendFile(File file) {
		if(file.exists()) {
			try {
							
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				OutputStream fluxsortie = connection.getOutputStream();
				ByteArrayOutputStream tableaubytes = new ByteArrayOutputStream();
				BufferedOutputStream tampon = new BufferedOutputStream(tableaubytes);
			
				int lu = in.read();
				int[] aecrire = new int[4096];
				int compteur = 0;
				int ouonestrendu=0;

			//	Tant qu'on est pas à la fin du fichier
				while(lu > -1) {
				//	On lit les données du fichier
					aecrire[compteur] = lu;
					lu = in.read();
					compteur++;
				
				//	Quand on a rempli le tableau, on envoie un paquet de 4096 octets
					if(compteur == 4096) {
						compteur=0;
						ouonestrendu++;
					//	On remplit le tampon
						for(int x=0;x<4096;x++)
							tampon.write(aecrire[x]);
					
					//	Et on l'envoie
						fluxsortie.write(tableaubytes.toByteArray());
					
						tableaubytes.reset();
					}
				}
			
			//	On envoie le dernier paquet, qui ne fait pas forcément 4096 octets
			//	On remplit le tampon
				for(int x=0;x<compteur;x++)
					tampon.write(aecrire[x]);
			
			//	Et on l'envoie
				tampon.flush();
				fluxsortie.write(tableaubytes.toByteArray());
				fluxsortie.flush();
			
				in.close();
				tampon.close();
				new WhatIDo("Envoi : " + nom);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				new WhatIDo("Echec de fichier introuvable", e.toString());
			} catch (IOException e) {
				new WhatIDo("Echec de l'envoi au serveur", e.toString());
				e.printStackTrace();
			}
		}else {
			System.out.println("Le fichier "+file+" est introuvable");
		}
	
	} 
	
}
