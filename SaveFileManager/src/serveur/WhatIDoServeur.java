package serveur;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

/**
 * @author hugo
 *
 */
public class WhatIDoServeur {

	public Date now = new Date();
	public SimpleDateFormat dateStandard = new SimpleDateFormat("HH:mm:ss dd/MM");
	public String date = dateStandard.format(now);
	public final File FICHIER = new File(System.getProperty("user.home")+System.getProperty("file.separator")
				+"ServeurJava");

	public WhatIDoServeur(final String phrase, final String ido) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Serveur.journal.append("("+date + ") "+phrase+" : "+ido+"\n");
				Serveur.journal.setCaretPosition(Serveur.journal.getText().length());
			}
		});
		AjouterHisto("("+date + ") "+phrase+" : "+ido+"\n");
	}
	
	public WhatIDoServeur(final String ido) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Serveur.journal.append("("+date + ") "+ido+"\n");
				Serveur.journal.setCaretPosition(Serveur.journal.getText().length());
			}
		});
		AjouterHisto("("+date + ") "+ido+"\n");
	}
	
	public WhatIDoServeur(final String ido, boolean b) {
		if (b == false) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Serveur.journal.append(ido);
					Serveur.journal.setCaretPosition(Serveur.journal.getText().length());
				}
			});
		AjouterHisto(ido);
		}
	}
	
	private void AjouterHisto(String string) {
		
		try
			{
				FICHIER.mkdirs();
				BufferedWriter bfwriter = new BufferedWriter(new FileWriter(FICHIER.getPath()+System.getProperty("file.separator")+"ServeurSaveFileManager.log",true));
				bfwriter.write(string);
			    bfwriter.close();
			}catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
}

