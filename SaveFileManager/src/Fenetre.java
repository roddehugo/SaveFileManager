import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.*;

/**
 * @author hugo
 *
 */
public class Fenetre implements ActionListener {
	
	public static JFrame frame;
	public static final Color fond = Color.white;
	public static JTextArea journal;
	public static JButton ajouter, enlever, benvoyer, brecevoir, bquitter, bnouveau,binfo;
	public static Vector<String> selection = new Vector<String>();
	public static JList liste;
	public static String name;
	public Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	public String user = System.getProperty("user.name");
	public String sep = System.getProperty("file.separator");
	public static JTextField ip, pot;
	public static File home;

	public Fenetre() {
		
		frame= new JFrame();
		frame.setTitle("Gestion Sauvegarde Serveur");
		frame.setSize(700,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/go-home.png")));
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(false);
		frame.setBackground(Color.white);
		frame.setContentPane(contentPane());
		frame.setVisible(true);
		
	}
	
	private Container contentPane() {
		
		JPanel panel = new JPanel(new BorderLayout());
		JPanel add = new JPanel(new GridLayout(10,1,5,5));
			add.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		JPanel act = new JPanel(new BorderLayout());
			act.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		JPanel south = new JPanel(new BorderLayout());
		JPanel ipPanel = new JPanel(new BorderLayout());
		JPanel portPanel = new JPanel(new BorderLayout());
			
		journal = new JTextArea(10,1);
			journal.setEditable(false);

		liste = new JList(selection);
			liste.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JLabel labIp = new JLabel("IP : ");
		ip = new JTextField("192.168.1.15");
		
		JLabel labPot = new JLabel("Port : ");
		pot = new JTextField("1507");
			
		Date now = new Date();
		SimpleDateFormat dateStandard = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat dateheure = new SimpleDateFormat("HH:mm:ss");
		String date = dateStandard.format(now);
		String time = dateheure.format(now);
			
		new WhatIDo("\nBienvenue ! " + "Nous sommes le "+date+", il est "+time+"\n" + "Connecté en tant que : "+user+"\n",false);
		
		JScrollPane scrollJ = new JScrollPane(journal);
		JScrollPane scrollT = new JScrollPane(liste);
		
		bnouveau = new JButton("Nouveau",new ImageIcon(getClass().getResource("/file.png")));
			bnouveau.addActionListener(this);
			add.add(bnouveau);
		ajouter = new JButton("Ajouter",new ImageIcon(getClass().getResource("/action_add.png")));
			ajouter.addActionListener(this);
			add.add(ajouter);
		enlever = new JButton("Enlever",new ImageIcon(getClass().getResource("/action_delete.png")));
			enlever.addActionListener(this);
			enlever.setEnabled(false);
			add.add(enlever);	
		benvoyer = new JButton("Envoyer",new ImageIcon(getClass().getResource("/arrow_top.png")));
			benvoyer.addActionListener(this);
			benvoyer.setEnabled(true);
			add.add(benvoyer);
		brecevoir = new JButton("Recevoir",new ImageIcon(getClass().getResource("/arrow_down.png")));
			brecevoir.addActionListener(this);
			brecevoir.setEnabled(true);
			add.add(brecevoir);
		binfo = new JButton("Help",new ImageIcon(getClass().getResource("/comments.png")));
			binfo.addActionListener(this);
			binfo.setEnabled(true);
			add.add(binfo);
		bquitter = new JButton("Quitter",new ImageIcon(getClass().getResource("/action_delete.png")));
			bquitter.addActionListener(this);
			add.add(bquitter);



		panel.add(south, BorderLayout.SOUTH);
			south.add(scrollJ);
		panel.add(act, BorderLayout.CENTER);
			act.add(add, BorderLayout.EAST);
				add.add(ipPanel);
					ipPanel.add(labIp, BorderLayout.NORTH);
					ipPanel.add(ip, BorderLayout.SOUTH);
				add.add(portPanel);
					portPanel.add(labPot, BorderLayout.NORTH);
					portPanel.add(pot, BorderLayout.SOUTH);
			act.add(scrollT, BorderLayout.CENTER);
			
		return panel;
	}	
	
	/**
	 * @return the ip
	 */
	public static String getIp() {
		
		return ip.getText().toString();
	}

	/**
	 * @return the pot
	 */
	public static int getPort() {
		int port = 0;
		try {
			port = Integer.parseInt(pot.getText());
		} catch (NumberFormatException f) {
			f.printStackTrace();
			new WhatIDo("Entrer un port valide",f.toString());
		}
		return port;
	}

	public void clear(Vector<String> vec) {
		vec.clear();
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if (src == binfo) {
			JOptionPane.showMessageDialog(frame, "Programme crée par Sarathai\n" + 
					"Licence creative commons\n" + 
					"\n" + 
					"Pour envoyer des fichiers sur le serveur, cliquer d'abord sur le bouton ajouter,\n" + 
					"puis sélectionnez le dossier ou fichier à ajouter. Répétez cette opération autant de foi que nécessaire.\n" + 
					"Puis cliquez sur le bouton envoyer en ayant d'abord rempli les champs de l'adresse ip et du port.\n" + 
					"\n" + 
					"Pour recevoir des fichiers sauvegardés sur le serveur, cliquez sur le bouton recevoir,\n" + 
					"puis sélectionnez le ou les fichier(s) voulu(s) dans la liste de gauche, et enfin recliquez\n" + 
					"sur le bouton recevoir pour importer les fichiers.\n" + 
					"\n" + 
					"Pour toutes les infos, bien lire le texte qui s'affiche dans le champ de texte en bas.", "Informations", JOptionPane.INFORMATION_MESSAGE);
		}else if (src == ajouter) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(chooser.getAcceptAllFileFilter());
    		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		int i = chooser.showOpenDialog(Fenetre.frame);
        if(i == JFileChooser.APPROVE_OPTION) {
        	frame.setCursor(waitCursor);        	
        	
        	File file = chooser.getSelectedFile();
        	
        	if (file.isDirectory()) {
        		name = file.getAbsolutePath().toString()+sep;
    			selection.addElement(name);
    			new WhatIDo("Ajouté à la séléction",name);
        	}else {
				name = file.getAbsolutePath().toString();
        		selection.addElement(name);
    			new WhatIDo("Ajouté à la séléction",name);
        	}
        	liste.updateUI();
        	enlever.setEnabled(true);
        	frame.setCursor(Cursor.getDefaultCursor());
        }
        }else if (src == enlever) {
        	try {
        		int j = liste.getSelectedIndex();
        		new WhatIDo("Supprimé de la séléction",selection.elementAt(j).toString());
        		selection.removeElementAt(j);
			} catch (ArrayIndexOutOfBoundsException e1) {
				new WhatIDo("Supprimé de la séléction",selection.elementAt(0).toString());
        		selection.removeElementAt(0);
			}
        	if (selection.size()==0) {
        		selection.clear();
        		liste.clearSelection();
        		enlever.setEnabled(false);
        	}
        	liste.updateUI();
		}else if (src == bquitter) {
			System.exit(0);
		}else if (src == bnouveau) {
			selection.clear();
			liste.clearSelection();
			liste.updateUI();
			new WhatIDo("Nouvelle sauvegarde");
		}else if (src == benvoyer) {
			if (!selection.isEmpty()) {
				new Envoyer(selection);
			}else {
				new WhatIDo("Veuillez ajouter des fichiers ou dossiers");
			}
		}else if (src == brecevoir) {
			if (!liste.isSelectionEmpty()) {
				Vector<String> vec = new Vector<String>();
				for (int i : liste.getSelectedIndices()) {
					vec.addElement(selection.elementAt(i));
				}
				new Recevoir(vec);
			} else {
				new Recevoir();
			}
		}
	}
	
}