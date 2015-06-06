import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * @file MonRunnable.java
 * @package 
 * @project SaveFileManager
 * @date 11 nov. 2009
 * @user hugo
 */
public class MonRunnable implements Runnable{

	public String folder,nomFile,action;
	public Socket service;
	public String sep = System.getProperty("file.separator");
	
	/**
	 * @param service
	 */
	public MonRunnable(Socket service) {
		this.service = service;
	}

	public void run(){
		try {
			new WhatIDoServeur("Nouveau Client",service.getInetAddress().toString()+":"+service.getPort());
			BufferedReader reader = new BufferedReader(new InputStreamReader(service.getInputStream()));
			action = reader.readLine();
			folder = reader.readLine();
			
			if (action.equals("envoyer")) {				
				nomFile = reader.readLine();
				File target = new File(System.getProperty("user.home")+sep+folder);
					target.mkdir();
				receiveFile(target.getPath().toString()+sep+nomFile);
				
				new WhatIDoServeur("Reçu",target.toString()+sep+nomFile);
			} else if (action.equals("recevoir")){				
				File target = new File(System.getProperty("user.home")+sep+folder);
				PrintWriter writer = new PrintWriter(service.getOutputStream());
				if (target.listFiles() !=  null) {
					for (File f : target.listFiles()) {
						writer.println(f.getName().toString());
						writer.flush();
						
						new WhatIDoServeur("Lu",f.getName().toString());
					}
				}else {
					writer.println("Pas de fichiers disponibles");
					writer.flush();
					
					new WhatIDoServeur("Pas de fichiers disponibles");
				}
				writer.close();
				service.close();
			} else if (action.equals("recevoirFile")) {				
				nomFile = reader.readLine();
				File target = new File(System.getProperty("user.home")+sep+folder+sep+nomFile);
				sendFile(target);
				
				new WhatIDoServeur("Envoyé",target.getName().toString());
			}
		}catch (IOException e) {
			e.printStackTrace();
			new WhatIDoServeur("IOExeption",e.toString());
		}
		
	}

	public void receiveFile(String target) throws IOException {
		int lu;
		
		BufferedInputStream inBuffer = new BufferedInputStream(service.getInputStream());
	
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
		service.close();
		
		new WhatIDoServeur("Un fichier reçu, voir",target);

	}

	public void sendFile(File file) throws IOException {
		if(file.exists()) {
			
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				OutputStream fluxsortie = service.getOutputStream();
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
				service.close();
		}else {
			new WhatIDoServeur("Le fichier introuvable",file.toString());
		}
	
	}
	
}
