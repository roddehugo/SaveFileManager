package client;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

/**
 * @author hugo
 *
 */
public class WhatIDo {

	public Date now = new Date();
	public SimpleDateFormat dateStandard = new SimpleDateFormat("HH:mm:ss dd/MM");
	public String date = dateStandard.format(now);

	public WhatIDo(final String phrase, final String ido) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Fenetre.journal.append("("+date + ") "+phrase+" : "+ido+"\n");
				Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
			}
		});
	}
	
	public WhatIDo(final String ido) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Fenetre.journal.append("("+date + ") "+ido+"\n");
				Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
			}
		});
	}
	
	public WhatIDo(final String ido, boolean b) {
		if (b == false) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Fenetre.journal.append(ido);
					Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
				}
			});
		}
	}
}

