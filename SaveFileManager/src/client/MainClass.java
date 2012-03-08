package client;

/**
 * @namefile MainClass.java
 * @packagename 
 * @projectname SaveFileManager
 */

/**
 * @author hugo
 */

import javax.swing.*;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*//
		String[] theme = {"Systeme","Java"};	
		
		String choix = (String) JOptionPane.showInputDialog(null, "Est utilisé pour le moment : Java\nChoisissez votre thème :",
						null, JOptionPane.QUESTION_MESSAGE, null, theme, theme[0]);
		
		if(choix.compareTo(theme[0])==0){
		//*/
			//Pour le Look&Feel par défaut de votre systeme
			String lookAndFeelName = UIManager.getSystemLookAndFeelClassName();
			
			try {
	
				UIManager.setLookAndFeel(lookAndFeelName);
			} 
			catch (ClassNotFoundException e) {} 
			catch (InstantiationException e) {}
			catch (IllegalAccessException e) {}
			catch (UnsupportedLookAndFeelException e) {}
			
		//}//*/
		new Fenetre();
		
	}

}
