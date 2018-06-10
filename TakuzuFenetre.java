import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

public class TakuzuFenetre extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	
	///////////////////////////////////////
	// Composants fenetre initialisation //
	///////////////////////////////////////
	private JPanel panInit;
	private JRadioButton[] listTailleGrille;
	private JButton butValid;
	
	private static final Dimension FEN_INIT_SIZE = new Dimension(200, 120);
	
	////////////////////////////
	// Composants fenetre jeu //
	////////////////////////////
	private JPanel panJeu;
	private JComponent[][] grilleCases;
	private JPanel actionBar;
	
	private static final Color COUL_CELL_DEFAULT= new Color(238,238,238);
	private static final Color COUL_CELL_OK= new Color(238/2,(238+255)/2,238/2);
	private static final Color COUL_CELL_ERROR= new Color((238+255)/2,238/2,238/2);
	private static final Border BORDER_BUTTON_HOVER = BorderFactory.createLoweredBevelBorder();
	
	///////////////
	// listeners //
	///////////////
	private buttInitJourList listButInit = new buttInitJourList();
	
	private butCellList listButCell = new butCellList();
	private butRetourInitList listButRetourInit = new butRetourInitList();
	private butQuitterList listButQuitter = new butQuitterList();
	private butHoverBorderList listButHoverBorder = new butHoverBorderList();
	
	///////////////
	// attributs //
	///////////////
	private Takuzu.Grille grille;
	
	
	//////////////
	// methodes //
	//////////////
	public void setGrille(Takuzu.Grille g){
		this.grille=g;
	}
	public Takuzu.Grille getGrille(){
		return this.grille;
	}
	
	//////////////////////////////////////
	//									//
	//			Constructeurs			//
	//									//
	//////////////////////////////////////

	public TakuzuFenetre(){
		super("Takuzu");
		
		this.setLocationRelativeTo(null);
		this.initialiseInit();
		this.affInit(true);
		this.getContentPane().setBackground(Color.blue);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	//////////////////////////////////////
	//									//
	//				INIT				//
	//									//
	//////////////////////////////////////
	
	/**
	 * crée le JPanel d'init
	 */
	public void initialiseInit(){
		
		this.panInit = new JPanel();
		//JPanel container = new JPanel();
		this.panInit.add(Box.createVerticalGlue());
		
		this.panInit.setLayout(new BoxLayout(this.panInit, BoxLayout.Y_AXIS));
		
		JLabel labTaille = new JLabel("Taille de la grille :");
		labTaille.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panInit.add(labTaille);
		
		JPanel panSelec = new JPanel();
		this.listTailleGrille = new JRadioButton[3];
		ButtonGroup group = new ButtonGroup();
		for(int i=0;i<3;i++)
		{
			this.listTailleGrille[i]= new JRadioButton(String.valueOf(4+i*2));
			group.add(this.listTailleGrille[i]);
			panSelec.add(this.listTailleGrille[i]);
		}
		this.listTailleGrille[0].setSelected(true);
		panSelec.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panInit.add(panSelec);
		
		this.butValid = new JButton("Jouer");
		butValid.addActionListener(this.listButInit);
		butValid.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.panInit.add(this.butValid);
		
		this.panInit.add(Box.createVerticalGlue());
			
	}
	
	/**
	 * affiche le JPanel d'init
	 * @param start true si c'est la création, false si c'est le retour a init
	 */
	public void affInit(boolean start)
	{
		Rectangle oldFen = this.getBounds();
		this.clearFenetre();
		
		this.add(this.panInit);
		this.pack();

		if(start) {
			this.setBounds(
					(SCREEN_SIZE.width-FEN_INIT_SIZE.width)/2,
					(SCREEN_SIZE.height-this.getPreferredSize().height)/2,
					FEN_INIT_SIZE.width,
					this.getPreferredSize().height);
		} else {
			this.setBounds(
					oldFen.x+(oldFen.width-FEN_INIT_SIZE.width)/2,
					oldFen.y+(oldFen.height-this.getPreferredSize().height)/2,
					FEN_INIT_SIZE.width,
					this.getPreferredSize().height);
			
		}
		
		this.update(this.getGraphics());
	}
	
	//////////////////////////////////////
	//									//
	//				JEU					//
	//									//
	//////////////////////////////////////
	
	/**
	 * crée le JPanel du jeu
	 */
	public void initialiseJeu(){
		int taille = this.grille.n;
		this.panJeu = new JPanel(new BorderLayout());
		this.grilleCases = new JComponent[taille][taille];
		
		JPanel grid = new JPanel(new GridLayout(taille,taille));
		Color coulFontInit = new Color(175,0,0);
		grid.setPreferredSize(new Dimension(taille*51,taille*51));
		
		for(int i=0;i<taille;i++){
			for(int j=0;j<taille;j++){
				if(Takuzu.est_cellule_initiale(this.grille, i, j)){
					JLabel valCase = new JLabel(String.valueOf(Takuzu.get_val_cellule(this.grille, i, j)));
					valCase.setVerticalAlignment(SwingConstants.CENTER);
					valCase.setHorizontalAlignment(SwingConstants.CENTER);
					valCase.setForeground(coulFontInit);
					valCase.setOpaque(true);
					valCase.setName("lab");
					this.grilleCases[i][j] = valCase;
					grid.add(valCase);
				} else {
					JButton buttCase = new JButton();
					buttCase.setName("("+i+","+j+")");
					buttCase.addActionListener(this.listButCell);
					buttCase.setBackground(COUL_CELL_DEFAULT);
					buttCase.addMouseListener(this.listButHoverBorder);
					this.grilleCases[i][j] = buttCase;
					grid.add(buttCase);
				}
				this.grilleCases[i][j].setPreferredSize(new Dimension(51,51));
				this.grilleCases[i][j].setBorder(this.getBorderCell(i, j));
				this.grilleCases[i][j].setSize(new Dimension(51,51));
				this.grilleCases[i][j].setMaximumSize(new Dimension(51,51));
			}
		}
		
		this.panJeu.add(grid,BorderLayout.CENTER);
		
		//this.getContentPane().add(grid,BorderLayout.CENTER);
		
		
		//barre "menu"
		this.actionBar = new JPanel();
		
		BoxLayout layout = new BoxLayout(this.actionBar, BoxLayout.Y_AXIS);
		this.actionBar.setLayout(layout);
		
		this.actionBar.add(Box.createVerticalGlue());
		
		JLabel text = new JLabel(); //futur "partie gagnée"
		text.setAlignmentX(CENTER_ALIGNMENT);
		this.actionBar.add(text);
		
		JButton butRetourInit = new JButton("Abandonner");
		butRetourInit.addActionListener(this.listButRetourInit);
		butRetourInit.setAlignmentX(CENTER_ALIGNMENT);
		this.actionBar.add(butRetourInit);
		
		JButton butQuitter = new JButton("Quitter");
		butQuitter.addActionListener(this.listButQuitter);
		butQuitter.setAlignmentX(CENTER_ALIGNMENT);
		this.actionBar.add(butQuitter);
		
		this.actionBar.add(Box.createVerticalGlue());
		
		Dimension dimBar = this.actionBar.getPreferredSize();
		dimBar.height=26*3;
		this.actionBar.setPreferredSize(dimBar);
		//this.getContentPane().add(this.actionBar,BorderLayout.SOUTH);
		this.panJeu.add(this.actionBar,BorderLayout.SOUTH);
	}
	
	/**
	 * affiche le JPanel du jeu
	 */
	public void affJeu()
	{
		Rectangle oldFen = this.getBounds();
		this.clearFenetre();
		this.add(this.panJeu);
		
		this.pack();
		
		this.setSize(this.getPreferredSize());
		
		this.setLocation(oldFen.x-(this.getPreferredSize().width-FEN_INIT_SIZE.width)/2, oldFen.y-(this.getPreferredSize().height-oldFen.height)/2);
		
		this.pack();
		this.update(this.getGraphics());
	}
	
	//////////////////////////////////////
	//									//
	//			METHODES JEU			//
	//									//
	//////////////////////////////////////
	public void gagne(){
		for(int i=0;i<this.grille.n;i++){
			for(int j=0;j<this.grille.n;j++){
				JComponent cell = this.grilleCases[i][j];
				if(!cell.getName().equals("lab")) {
					if(cell.hasFocus())
						cell.setBorder(this.getBorderCell(i, j));
					this.grilleCases[i][j].setEnabled(false);
					//this.grilleCases[i][j].removeMouseListener(this.listButHoverBorder);
				}
			}
		}
		
		JLabel text = (JLabel)this.actionBar.getComponent(1);
		text.setText("Partie gagnée !");
		text.setPreferredSize(new Dimension(text.getPreferredSize().width, 18));
		text.setVerticalAlignment(SwingConstants.TOP);
		((JButton)this.actionBar.getComponent(2)).setText("Nouvelle partie");
	}
	
	private void clearFenetre(){
		if(this.getContentPane().getComponentCount()>0) ;
			this.getContentPane().removeAll();
	}
	
	public boolean zero_un_consecutif_ligne(int i, int j){
		//on considere la valeur valide, pas besoin d'exception 
		return !Takuzu.est_cellule_vide(grille, i, j)
			&& Takuzu.get_val_cellule(grille, i, j) == Takuzu.get_val_cellule(grille, i, j-1)
			&& Takuzu.get_val_cellule(grille, i, j) == Takuzu.get_val_cellule(grille, i, j-2);
	}
	
	public boolean zero_un_consecutif_col(int i, int j){
		//on considere la valeur valide, pas besoin d'exception 
		return !Takuzu.est_cellule_vide(grille, i, j)
			&& Takuzu.get_val_cellule(grille, i, j) == Takuzu.get_val_cellule(grille, i-1, j)
			&& Takuzu.get_val_cellule(grille, i, j) == Takuzu.get_val_cellule(grille, i-2, j);
	}
	
	public MatteBorder getBorderCell(int i, int j) {
		return BorderFactory.createMatteBorder(0, 0, (i==this.grille.n-1)?0:1, (j==this.grille.n-1)?0:1, Color.BLACK);
	}
	
	public Dimension getDimCell(int i, int j) {
		return new Dimension((i==this.grille.n-1)?50:51, (j==this.grille.n-1)?50:51);
	}
	
	public void updateCoulGrille() {
		//parcours ligne
		for(int i=0;i<this.grille.n;i++) {
			int nb0=0, nb1=0;
			for(int j=0;j<this.grille.n;j++) {
				this.grilleCases[i][j].setBackground(COUL_CELL_DEFAULT);
				
				if(this.zero_un_consecutif_ligne(i, Math.max(j, 2))) {
					for(int ite=0; (ite<3 && j-ite>=0) ;ite++) {
						grilleCases[i][j-ite].setBackground(COUL_CELL_ERROR);
					}
				}
				
				if(Takuzu.get_val_cellule(grille, i, j) == 0) nb0++;
				else if(Takuzu.get_val_cellule(grille, i, j) == 1) nb1++;
			}
			
			if(nb0+nb1==this.grille.n) {
				if(nb0==nb1) {
					for(int j=0;j<this.grille.n;j++) {
						if(this.grilleCases[i][j].getBackground() != COUL_CELL_ERROR)
							this.grilleCases[i][j].setBackground(COUL_CELL_OK);
					}
				} else {
					for(int j=0;j<this.grille.n;j++) {
						this.grilleCases[i][j].setBackground(COUL_CELL_ERROR);
					}
				}
			}
		}
		
		//parcours col
		for(int j=0;j<this.grille.n;j++) {
			int nb0=0, nb1=0;
			for(int i=0;i<this.grille.n;i++) {
				
				if(this.zero_un_consecutif_col(Math.max(i, 2), j)) {
					for(int ite=0; (ite<3 && i-ite>=0) ;ite++) {
						grilleCases[i-ite][j].setBackground(COUL_CELL_ERROR);
					}
				}
				
				if(Takuzu.get_val_cellule(grille, i, j) == 0) nb0++;
				else if(Takuzu.get_val_cellule(grille, i, j) == 1) nb1++;
			}
			
			if(nb0+nb1==this.grille.n) {
				if(nb0==nb1) {
					for(int i=0;i<this.grille.n;i++) {
						if(this.grilleCases[i][j].getBackground() != COUL_CELL_ERROR) {
							this.grilleCases[i][j].setBackground(COUL_CELL_OK);
						}
					}
				} else {
					for(int i=0;i<this.grille.n;i++) {
						this.grilleCases[i][j].setBackground(COUL_CELL_ERROR);
					}
				}
			}
		}
	}
	
	//////////////////////////////////////
	//									//
	//			LISTENER INIT			//
	//									//
	//////////////////////////////////////
	
	class buttInitJourList implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			setGrille(Takuzu.initialiser_grille("Grilles/grille"+this.getSelection()+".txt"));
			initialiseJeu();
			affJeu();
		}
		
		private int getSelection()
		{
			int i=0;
			int taille=0;
			while(i<3 && taille==0){
				if(listTailleGrille[i].isSelected()){
					taille=4+i*2;
				}
				i++;
			}
			return taille;
		}
	}
	
	//////////////////////////////////////
	//									//
	//			LISTENER JEU			//
	//									//
	//////////////////////////////////////
	class butCellList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton but = (JButton) e.getSource();
			//(i,j)
			int i = Integer.parseInt(but.getName().substring(1, 2));
			int j = Integer.parseInt(but.getName().substring(3, 4));
			int val = Takuzu.get_val_cellule(getGrille(), i, j);
			
			if(val==-1){
				Takuzu.set_val_cellule(getGrille(), i, j, 0);
				but.setText("0");
			} else if(val==0){
				Takuzu.set_val_cellule(getGrille(), i, j, 1);
				but.setText("1");
			} else {
				Takuzu.set_val_cellule(getGrille(), i, j, -1);
				but.setText("");
			}
			
			if(Takuzu.est_partie_gagnee(getGrille()))
				gagne();
			
			updateCoulGrille();
			
			update(getGraphics());
			repaint();
		}
	}
	
	class butRetourInitList implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			setGrille(null);
			
			actionBar=null;
			grilleCases=null;
			panJeu=null;
			
			affInit(false);
		}
	}
	
	class butQuitterList implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	class butHoverBorderList implements MouseListener {

		public void mouseEntered(MouseEvent e) {
			JButton but = (JButton)e.getSource();
			if(but.isEnabled())
			but.setBorder(new CompoundBorder(but.getBorder(), BORDER_BUTTON_HOVER));
		}

		public void mouseExited(MouseEvent e) {
			JButton but = (JButton)e.getSource();
			if(but.isEnabled())
			but.setBorder(((CompoundBorder)but.getBorder()).getOutsideBorder());
		}

		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		
	}
	//////////////////////
	//					//
	//		MAIN		//
	//					//
	//////////////////////
	public static void main(String[] args){
		new TakuzuFenetre();
		
	}
}
