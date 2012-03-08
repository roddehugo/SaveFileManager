import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hugo
 *
 */
public class WhatIDo {

	public Date now = new Date();
	public SimpleDateFormat dateStandard = new SimpleDateFormat("HH:mm:ss dd/MM");
	public String date = dateStandard.format(now);

	public WhatIDo(String phrase, String ido) {
		Fenetre.journal.append("("+date + ") "+phrase+" : "+ido+"\n");
		Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
	}
	
	public WhatIDo(String ido) {
		Fenetre.journal.append("("+date + ") "+ido+"\n");
		Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
	}
	
	public WhatIDo(String ido, boolean b) {
		if (b == false) {
		Fenetre.journal.append(ido);
		Fenetre.journal.setCaretPosition(Fenetre.journal.getText().length());
		}
	}
}

