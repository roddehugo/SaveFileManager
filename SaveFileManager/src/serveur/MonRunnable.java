package serveur;
import java.io.*;
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
		BufferedReader reader = null;
		try {
			
			new WhatIDoServeur("Nouveau Client",service.getInetAddress().toString()+":"+service.getPort());
			reader = new BufferedReader(new InputStreamReader(service.getInputStream()));
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
				PrintWriter writer = null;
				File[] files = target.listFiles();
				try {
					writer = new PrintWriter(service.getOutputStream());
					if (files[0].exists()) {
						for (File f : files) {
							writer.println(f.getName().toString());
							writer.flush();
							new WhatIDoServeur("Lu",f.getName().toString());
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}catch(ArrayIndexOutOfBoundsException e) {
					writer.println("Pas de fichiers disponibles");
					writer.flush();
					new WhatIDoServeur("Pas de fichiers disponibles");
				}finally {
					writer.close();
				}
			} else if (action.equals("recevoirFile")) {				
				nomFile = reader.readLine();
				File target = new File(System.getProperty("user.home")+sep+folder+sep+nomFile);
				sendFile(target);
				new WhatIDoServeur("Envoyé",target.getName().toString());
			} else if (action.equals("supprimer")) {
				nomFile = reader.readLine();
				File target = new File(System.getProperty("user.home")+sep+folder+sep+nomFile);
				target.delete();
				new WhatIDoServeur("Supprimé",target.getName().toString());
			}
		}catch (IOException e) {
			e.printStackTrace();
			new WhatIDoServeur("IOExeption",e.toString());
		}finally {
			try {
				reader.close();
				service.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	public void receiveFile(String target){
		int lu;
			BufferedInputStream inBuffer = null;
		
			BufferedOutputStream outBuffer = null;

		try {
			inBuffer = new BufferedInputStream(service.getInputStream());
			outBuffer = new BufferedOutputStream(new FileOutputStream(target));
			lu = inBuffer.read();
		
			int compteur = 0;
		
			while(lu > -1) {
				outBuffer.write(lu);
				lu = inBuffer.read();
			
				compteur++;
			}
		
			outBuffer.write(lu);
			outBuffer.flush();
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outBuffer.close();
				inBuffer.close();
				service.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendFile(File file){
		if(file.exists()) {
			InputStream in = null;
			BufferedOutputStream tampon = null;
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				OutputStream fluxsortie = service.getOutputStream();
				ByteArrayOutputStream tableaubytes = new ByteArrayOutputStream();
				tampon = new BufferedOutputStream(tableaubytes);

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
			}catch(IOException e) {
				e.printStackTrace();
			}finally {
				try {
					in.close();
					tampon.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}else {
			new WhatIDoServeur("Le fichier est introuvable",file.toString());
		}
	
	}
	
}
