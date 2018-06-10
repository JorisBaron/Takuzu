/*
 * Production de la documentation : javadoc -encoding ISO-8859-1 Takuzu.java
 * Compilation : javac -encoding ISO-8859-1 Takuzu.java
 * Execution : java Takuzu
 */

import java.util.Random;
import java.util.Scanner;

/**
 * <b>Takuzu est la classe repr?sentant un jeu du Takuzu.</b>
 * <p>
 * Le jeu du Takuzu est un jeu qui ressemble au Sudoku. Il s'agit de
 * remplir une grille avec des 0 et des 1 en respectant les r?gles suivantes :</p>
 * <ul>
 * <li>Il doit y avoir le m?me nombre de 1 et de 0 sur chaque ligne</li>
 * <li>Il doit y avoir le m?me nombre de 1 et de 0 sur chaque colonne.</li>
 * <li>Pas plus de 2 chiffres identiques c?te ? c?te.</li>
 * <li>2 lignes ou 2 colonnes ne peuvent ?tre identiques.</li>
 * </ul>
 * <p>
 * Le jeu propose ? l'utilisateur une grille de taille 4x4 ou 6x6 ou 8x8.
 * Il y a ensuite une initialisation al?atoire de la grille ? partir de configurations
 * pr?-?tablies. 
 * </p>
 * <p>Enfin l'utilisateur est invit? ? saisir des valeurs sous la forme &lt;ligne&gt;&lt;colonne&gt;&lt;val&gt;
 * par exemple AA1 signifie que l'on souhaite placer un 1 dans la case de coordonn?e (A,A).
 * </p>
 * 
 * @author christophe.cerin@iutv.univ-paris13.fr
 * @version 1.0
 */
public class Takuzu {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	/**
	 * <p>Affichage d'une chaine de caract?res avec retour ? la ligne</p>
	 *
	 * @param fg
	 *           Couleur du devant
	 * @param bg
	 *           Couleur du fond
	 * @param txt
	 *           Texte ? afficher
	 * @return
	 *           0
	 */
	public static int color_printf(String fg, String bg, String txt) {
		System.out.println(bg + fg + txt + ANSI_RESET);
		return 0;
	}

	/**
	 * <p>Affichage d'une chaine de caract?res sans retour ? la ligne</p>
	 *
	 * @param fg
	 *           Couleur du devant
	 * @param bg
	 *           Couleur du fond
	 * @param txt
	 *           Texte ? afficher
	 * @return 
	 *           0
	 */
	public static int color_printf1(String fg, String bg, String txt) {
		System.out.print(bg + fg + txt + ANSI_RESET);
		return 0;
	}

	/**
	 * <p>Le type Cellule est compos? d'une valeur de cellule et d'un ?tat (initial ou pas)</p>
	 */
	public static class Cellule {
		public int		val;
		public int		initial;
	};

	/**
	 * <p>Le type Grille est vu comme un tableau ? une seule dimension de cellules
	 * et d'un entier donnant le nombre d'?l?ments sur une ligne (ou une colonne).</p> 
	 */
	public static class Grille {
		public Cellule[] tab;
		public int n;
	};
	
	
	public static void assert1(boolean cond)
	{
		if(!cond) {
			System.out.println("erreur assert");
			System.exit(1);
		}
	}
	
	
	/**
	 * <p>Cr?ation d'une grille de jeu. Attention, il n'y a pas de 
	 * constructeur Takuzu().<p>
	 *
	 * @param n
	 *           Nombre d'?l?ments sur une ligne (ou une colonne)
	 * @return
	 *           Une nouvelle grille de jeu
	 */
	public static Grille creer_grille(int n) {
		assert1(n == 4 || n == 6 || n == 8);

		Grille g = new Grille();
		g.n = n;
		g.tab = new Cellule[n*n];

		int		i;
		for (i = 0; i < n * n; i++) {
			g.tab[i] = new Cellule();
			g.tab[i].val = -1;
			g.tab[i].initial = 0;
		};

		return g;
	}

	/**
	 *<p>Test si l'indice pour acc?der ? la grille est valide.</p>
	 *
	 * @param g 
	 *        Grille de jeu
	 * @param indice
	 *        Indice ? tester (&gt;= 0 and &lt;= g.n - 1)
	 * @return Boll?en positionn? ? true si l'indice est valide ; false sinon
	 */
	public static boolean est_indice_valide(Grille g, int indice) {
		if (indice >= 0 && indice <= g.n - 1)
			//verifier si l 'indice correspond au tableau.
		{
			return true;
		} else {
			return false;
		}
	}   

	/**
	 *<p>Test si nous avons affaire ? une cellule valide (dans la grille).</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param i
	 *        Num?ro de ligne
	 * @param j
	 *        Num?ro de colonne
	 * @return Bool?en positionn? ? true si cellule (i,j) est dans la grille ; false sinon
	 */
	public static boolean est_cellule(Grille g, int i, int j) {
		boolean	res1 = est_indice_valide(g, i);
		//verifier si l 'indice est est bon au niveau des lignes.
		boolean	res2 = est_indice_valide(g, j);
		//verifier si l 'indice est est bon au niveau des colonnes.
		if (res1 && res2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *<p>Retourne le contenu d'une cellule.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param i
	 *        Num?ro de ligne
	 * @param j
	 *        Num?ro de colonne
	 * @return Valeur de la cellule (i,j)
	 */
	public static int get_val_cellule(Grille g, int i, int j) {
		boolean		res = est_cellule(g, i, j);
		assert1(res == true);
		int		res2 = g.tab[i * (g.n) + j].val;
		//permet de se deplacer dans le tableau(i = lignes et j = collones)
		return res2;
	}

	/**
	 *<p>Test si une cellule est vide.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param i
	 *        Num?ro de ligne
	 * @param j
	 *        Num?ro de colonne
	 * @param val
	 *        Valeur (0 ou 1) ? affecter ? la cellule (i,j)
	 */
	public static void set_val_cellule(Grille g, int i, int j, int val) {
		assert1(est_cellule(g, i, j));
		if (val >= -1 && val <= 1) {
			g.tab[i * (g.n) + j].val = val;
		}
	}

	/**
	 *<p>Test si une cellule est une cellule initiale.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param i
	 *        Num?ro de ligne
	 * @param j
	 *        Num?ro de colonne
	 * @return Bool?en positionn? ? true si la cellule est une cellule initiale ; false sinon
	 */
	public static boolean est_cellule_initiale(Grille g, int i, int j) {
		assert1(est_cellule(g, i, j));
		int		res = g.tab[i * (g.n) + j].initial;
		//on accede au champ initial avec la meme methode que les deux questions precedentes.
		if (res == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *<p>Test si une cellule est vide.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param i
	 *        Num?ro de ligne
	 * @param j
	 *        Num?ro de colonne
	 *
	 * @return Bool?en positionn? ? true si la cellule est vide ; false sinon
	 */
	public static boolean est_cellule_vide(Grille g, int i, int j) {
		assert1(est_cellule(g, i, j));
		int	res = get_val_cellule(g, i, j);
		if (res == -1)
		{
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>M?thode d'affichage de la grille.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 */
	public static void afficher_grille(Grille g) {
		char c = 'A';
		int i, j, z, v;
		String	buf="";
		String p="    ";

		final String ANSI_CLS = "\u001b[2J";
		final String ANSI_HOME = "\u001b[H";
		System.out.print(ANSI_CLS + ANSI_HOME);
		System.out.flush();

		for (i = 0; i < g.n; i++) {
			p = p + c;
			p = p + "       ";
			c++;
		}
		color_printf(ANSI_WHITE_BACKGROUND, ANSI_RED, p);

		p = "----";
		for (i = 0; i < g.n; i++) {
			p = p + "--------";
		}
		color_printf(ANSI_BLACK_BACKGROUND, ANSI_WHITE, p);

		c -= g.n;

		//On dessine le jeu
		z = 2;
		for (i = 0; i < g.n; i++) {
			buf = "";
			buf = buf + " " + c + " ";
			color_printf1(ANSI_WHITE_BACKGROUND, ANSI_RED, buf);
			color_printf1(ANSI_WHITE_BACKGROUND, ANSI_RED, "|");

			z++;
			c++;

			p = "";
			for (j = 0; j < g.n; j++) {
				v = get_val_cellule(g, i, j);
				if (v == -1)
					p = p + "        ";
				else
					p = p + v + "       ";
			}
			color_printf(ANSI_WHITE_BACKGROUND, ANSI_RED, p);

			p = "----";
			for (j = 0; j < g.n; j++) {
				p = p + "--------";
			}
			color_printf(ANSI_BLACK_BACKGROUND, ANSI_WHITE, p);
			z++;
		}

		return;
	}

	/**
	 *<p>Permet de choisir une grille 4x4 ou 6x6 ou 8x8.</p>
	 *
	 * @param s
	 *        Chaine de caract?res repr?sentant un chemin
	 * @return Chaine de caract?res repr?sentant un jeu de donn?es al?atoire
	 */
	public static String choisir_grille(String s)
	{
		int myInt = 0;
		Scanner in = new Scanner(System.in);
		do {
			color_printf(ANSI_GREEN_BACKGROUND, ANSI_RED, "Input a number among 4,6 et 8: ");
			while (!in.hasNextInt()) {
				System.out.println("That's not an int! try again...");
				in.nextLine();
			}
			myInt = in.nextInt();
		} while ((myInt != 4) && (myInt != 6) && (myInt != 8));

		return s.substring(0, 14) + myInt + s.substring(15);

	}

	/**
	 * <p>V?rifie si la grille ne comporte pas plus de deux 1 ou deux 0 cons?cutifs.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @return Bool?en positionn? ? true si on n'a pas deux 0 et deux 1 cons?cutifs ;
	 *         false sinon (il y a plus de deux 0 ou plus de deux 1 cons?cutifs)
	 */
	public static boolean pas_zero_un_consecutifs(Grille g)
	{
		int		i;
		int		j;
		int		compteur = 0;
		for (i = 0; i < (g.n); i++) {
			for (j = 0; j < (g.n) - 2; j++) {
				if (get_val_cellule(g, i, j) == get_val_cellule(g, i, j + 1)
						&& get_val_cellule(g, i, j) == get_val_cellule(g, i, j + 2)) {
					compteur++;
				} 
			}
		}

		for (i = 0; i < (g.n) - 2; i++) {
			for (j = 0; j < (g.n); j++) {
				if (get_val_cellule(g, i, j) == get_val_cellule(g, i + 1, j)
						&& get_val_cellule(g, i, j) == get_val_cellule(g, i + 2, j)) {
					compteur++;
					//meme chose mais au niveau des lignes.
				}
			}
		}

		if (compteur == 0)
			//Si le compteur n 'a pas change cela veut dire que
			// le test des memes nombres n ' est pas passe et donc il n ' y a pas de 3
			// zero ou 3 un consecutifs.
			return true;
		else
			return false;
	}

	/**
	 * <p>V?rifie si la grille a le m?me nombre de 0 et de 1.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @return Bool?en positionn? ? true si la grille a le m?me nombe de 0 que 
	 *         de 1 ; false sinon
	 *
	 */
	public static boolean meme_nombre_zero_un(Grille g)
	{
		int		un;
		int		zero;
		int		res1 = 0;
		int		res2 = 0;
		int		i;
		int		j;

		for (i = 0; i < g.n; i++) {
			un = 0;
			zero = 0;
			for (j = 0; j < g.n; j++) {
				if (get_val_cellule(g, i, j) == 1)
					//Si la valeur de la cellule est 1 on incremente le compteur un.
					un++;
				else
					//Sinon on increente le compteur 0 zero
					zero++;
			}
			if (un == zero)
				//Si il y a autant de un que de zero alors on incremente le compteur res1.
				res1++;
		}

		for (i = 0; i < g.n; i++) {
			un = 0;
			zero = 0;
			for (j = 0; j < g.n; j++) {
				if (get_val_cellule(g, j, i) == 1)
					//Meme chose au niveau des colonnes
					un++;
				else
					zero++;
			}

			if (un == zero) {
				res2++;
			}
		}

		if (res1 + res2 == g.n * 2)
			// (g->n) * 2 represente le nombre de lignes et de colonnes au total.
			// Si res1 + res2 = (g->n) * 2 alors il y a autant de 1 que de 0
			return true;
		else
			return false;
	}

	/**
	 * <p>V?rifie si les lignes et les colonnes sont distinctes.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @return Bool?en positionn? ? true si les lignes et les colones sont distinctes ;
	 *         false sinon
	 *
	 */
	public static boolean lignes_colonnes_distinctes(Grille g)
	{
		int		verif;
		int		i;
		int		j;
		int		k;
		int		diff = 0;

		//vertical.La fonction compare la 1ere colonne avec les autres puis la deuxieme 
		//avec les autres et ainsi de suite.
		for (k = 0; k < g.n - 1; k++) {
			for (i = 1 + k; i < g.n; i++) {
				verif = 0;
				//on remet a 0 quand on passe a une nouvelle colonne.
				for (j = 0; j < g.n; j++) {
					if (get_val_cellule(g, j, k) == get_val_cellule(g, j, i))
						verif++;
				}
				if (verif == g.n)
					diff++;
				//Si verif est egal au nombre de cellule de la colonne
				//cela veut dire que les 2 colonnes sont identiques.
			}
		}

		//horizontal. La fonction compare la 1er e ligne avec les autres
		// puis la deuxieme avec les autres et ainsi de suite.
		for (k = 0; k < g.n - 1; k++) {
			for (i = 1 + k; i < g.n; i++) {
				verif = 0;
				//on remet a 0 quand on passe a une nouvelle ligne.
				for (j = 0; j < g.n; j++) {
					if (get_val_cellule(g, k, j) == get_val_cellule(g, i, j))
						verif++;
				}
				if (verif == g.n)
					diff++;
			}
		}

		if (diff == 0)
			//Si diff est egal a 0 cela veut dire qu 'il n' y a pas de colonnes ou lignes identiques.
			return true;
		else
			return false;

	}

	/**
	 * <p>V?rifie si la grille de jeu a ?t? enti?rement remplie.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @return Bool?en positionn? ? true si la grille est pleine, false sinon
	 *
	 */
	public static boolean est_grille_pleine (Grille g)
	{
		int i;
		int j;
		int compteur = 0;
		for (i = 0; i < (g.n); i++)
		{
			for (j = 0; j < (g.n); j++)
			{
				if (!est_cellule_vide (g, i, j))
				{
					compteur++;
				}  //Si la cellule n 'est pas vide, alors on incrémente le compteur.
			}
		}
		if (compteur == (g.n) * (g.n))
		{
			return true;
		}//si le compteur est egal au nombre de cellule de la grille alors c 'est une grille pleine.
		else
		{
			return false;
		} //sinon elle n 'est pas pleine.
	}

	/**
	 * <p>D?termine si une partie est gagnante.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @return Bool?en positionn? ? true si la partie est termin?e, false sinon.
	 *
	 */
	public static boolean est_partie_gagnee(Grille g)
	{
		/*
	    System.out.println(pas_zero_un_consecutifs(g));
	    System.out.println(meme_nombre_zero_un(g));
	    System.out.println(lignes_colonnes_distinctes(g));
	    System.out.println(est_grille_pleine(g));
		 */
		if (pas_zero_un_consecutifs(g) && meme_nombre_zero_un(g)
				&& lignes_colonnes_distinctes(g) && est_grille_pleine(g)) {
			return true;
		}  else
			return false;
	}

	/**
	 * <p>Initialisation de la grille avec un jeu de donn?es al?atoire.
	 * </p>
	 *
	 * @param nom
	 *        Chaine de caracteres pr?c?demment saisie et permettant d'identifier le
	 *        jeu de donn?es
	 * @return La grille initialis?e 
	 *
	 */
	public static Grille initialiser_grille(String nom)
	{
		//declarer des tableaux correspondant aux grilles
		int	[]	t1         = {4, 4, 0, 1, 1, 2, 2, 0, 2, 3, 0, 3, 2, 0};
		int []	t2         = {4, 5, 0, 1, 0, 0, 3, 0, 2, 0, 1, 2, 1, 1, 3, 2, 0};
		int	[]	t3         = {4, 4, 0, 0, 0, 2, 1, 1, 2, 3, 1, 3, 1, 1};
		int	[]	t4         = {4, 5, 0, 1, 1, 0, 2, 1, 1, 0, 1, 1, 1, 1, 3, 2, 1};
		int	[]	t5         = {4, 4, 0, 3, 0, 1, 0, 1, 1, 1, 1, 2, 0, 1};
		
		int	[]	t6         = {6, 9, 0, 2, 0, 0, 3, 0, 2, 5, 0, 3, 2, 1, 3, 4, 1, 4, 0, 0, 4, 2, 1, 4, 4, 1, 5, 0, 0};
		int	[]	t7         = {6, 9, 0, 2, 0, 1, 0, 1, 1, 5, 1, 3, 1, 1, 3, 3, 1, 3, 5, 1, 4, 0, 0, 5, 2, 0, 5, 3, 0};
		int	[]	t8         = {6, 9, 0, 3, 0, 0, 4, 0, 1, 0, 1, 1, 3, 0, 2, 4, 1, 3, 1, 1, 3, 4, 1, 3, 5, 1, 5, 0, 1};
		int	[]	t9         = {6, 10, 0, 1, 0, 0, 4, 0, 1, 5, 1, 2, 1, 0, 3, 0, 0, 3, 2, 1, 3, 5, 1, 4, 2, 1, 4, 5, 1, 5, 0, 0};
		int	[]	t10        = {6, 7, 0, 3, 0, 1, 1, 1, 2, 3, 1, 4, 0, 1, 4, 4, 1, 5, 2, 0, 5, 3, 0};
		
		int	[]	t11        = {8, 15, 0, 0, 0, 0, 5, 1, 1, 0, 0, 1, 3, 0, 1, 6, 0, 2, 2, 1, 2, 5, 1, 3, 1, 1, 3, 7, 0, 5, 1, 0, 5, 6, 0, 5, 7, 0, 7, 2, 1, 7, 5, 0, 7, 6, 0};
		int	[]	t12        = {8, 16, 0, 1, 1, 0, 4, 1, 0, 7, 0, 1, 1, 1, 2, 3, 0, 2, 4, 0, 2, 7, 0, 3, 5, 1, 4, 3, 0, 4, 6, 0, 5, 0, 1, 5, 2, 1, 5, 6, 0, 6, 2, 1, 6, 3, 0, 7, 5, 0};
		int	[]	t13        = {8, 14, 0, 2, 1, 0, 5, 1, 1, 2, 1, 2, 5, 1, 2, 7, 0, 3, 2, 1, 3, 3, 1, 4, 4, 0, 4, 6, 1, 5, 1, 0, 6, 1, 0, 6, 5, 1, 6, 6, 1, 7, 6, 1};
		int	[]	t14        = {8, 16, 0, 1, 1, 0, 6, 0, 0, 7, 0, 1, 4, 1, 3, 2, 1, 3, 4, 1, 3, 5, 1, 4, 2, 1, 4, 4, 0, 4, 6, 1, 6, 1, 0, 6, 2, 0, 6, 5, 1, 7, 1, 0, 7, 4, 1, 7, 7, 0};
		int	[]	t15        = {8, 12, 0, 2, 0, 0, 5, 1, 0, 7, 1, 1, 4, 1, 1, 7, 1, 2, 3, 1, 5, 0, 0, 5, 1, 0, 5, 6, 0, 6, 0, 0, 6, 2, 0, 7, 6, 0};
		int  []          t = t1;

		//selectionner la grille en fonction de ce que contient le nom nom

		int		n = Integer.parseInt(nom.substring(14,15));
		int         max = 5;
		int         min = 1;
		Random      rand = new Random(System.currentTimeMillis());
		int         nb_ini = rand.nextInt((max - min) + 1) + min;
		//int		nb_ini = (int)(nom[17]) - '0';
		//printf("%s*** %d %d ---\n", nom, n, nb_ini);
		switch (n) {
		case 4:
			switch (nb_ini) {
			case 1:
				t = t1;
				break;
			case 2:
				t = t2;
				break;
			case 3:
				t = t3;
				break;
			case 4:
				t = t4;
				break;
			case 5:
				t = t5;
				break;
			}
			break;
		case 6:
			switch (nb_ini) {
			case 1:
				t = t6;
				break;
			case 2:
				t = t7;
				break;
			case 3:
				t = t8;
				break;
			case 4:
				t = t9;
				break;
			case 5:
				t = t10;
				break;
			}
			break;
		case 8:
			switch (nb_ini) {
			case 1:
				t = t11;
				break;
			case 2:
				t = t12;
				break;
			case 3:
				t = t13;
				break;
			case 4:
				t = t14;
				break;
			case 5:
				t = t15;
				break;
			}
			break;
		}

		n = t[0];
		nb_ini = t[1];

		Grille         g = creer_grille(n);
		int		i;
		int		j;
		int		x;

		for(i=0;i<g.n * g.n;i++) {
			g.tab[i].val = -1;
			g.tab[i].initial = 0;
		}

		for (x = 0 + 2; x < (nb_ini * 3) + 2; x = x + 3) {
			i = t[x];
			j = t[x + 1];
			g.tab[g.n * i + j].val = t[x + 2];
			g.tab[g.n * i + j].initial = 1;
		}

		return g;

	}

	/**
	 * <p>V?rifie si un mouvement est valide.</p>
	 *
	 * @param g
	 *        Grille de jeu
	 * @param mouv
	 *        Mouvement souhait? (ex: AA1)
	 * @return Bool?en (vrai si mouvement autoris? ; faux sinon)
	 *
	 */
	public static boolean est_mouvement_valide(Grille g, String mouv)
	{
		if (mouv.length() != 3 && mouv.length() != 2)
			return false;
		int val = -1;
		int ligne =(int)mouv.charAt(0) - (int)'A';
		int colonne = (int)mouv.charAt(1) - (int)'A';
		if (mouv.length() == 3)
			val = (int)mouv.charAt(2) - (int)'0';
		else
			val = -1;
		/*
	    System.out.println(mouv);
	    System.out.println(ligne);
	    System.out.println(colonne);
	    System.out.println(val);
		 */

		if (est_cellule(g, ligne, colonne)
				&& !est_cellule_initiale(g, ligne, colonne)) {
			if (val == -1) {
				if (get_val_cellule(g, ligne, colonne) != -1)
					set_val_cellule(g, ligne, colonne, val);
			} else
				set_val_cellule(g, ligne, colonne, val);
			return true;
		} else
			return false;
	}

	/**
	 * <p>Tant qu'un mouvement est encore possible, saisir une nouvelle action.</p>
	 *
	 * @param g
	 *        Grille sur laquelle on op?re
	 *
	 * @see Takuzu#est_mouvement_valide
	 *
	 */
	public static void tour_de_jeu(Grille g)
	{
		int	ligne;
		int	colonne;
		int	val;
		String mouvement;
		Scanner scanIn = new Scanner(System.in);

		do {
			color_printf(ANSI_GREEN_BACKGROUND, ANSI_RED, "Enter a string: ");
			mouvement = scanIn.nextLine();
		} while (!est_mouvement_valide(g, mouvement));
	}

	/**
	 * <p>Lance le jeu de Takuzu par 1) cr?ation d'une grille 2) affichage de
	 * la grille initialis?e al?atoirement 3) appel ? tour_de_jeu</p>
	 *
	 * @param ch
	 *       Chaine contenant un chemin vers un jeu de donn?es al?atoire
	 *
	 * @see Takuzu#initialiser_grille
	 * @see Takuzu#afficher_grille
	 * @see Takuzu#tour_de_jeu
	 * @see Takuzu#est_partie_gagnee
	 */
	public static void jouer(String ch)
	{

		Grille         g = initialiser_grille(ch);
		do {
			afficher_grille(g);
			tour_de_jeu(g);
		}
		while (!est_partie_gagnee(g));
		afficher_grille(g);
	}

	/**
	 * <p>Fonction principale: 1) creation de la grille avec initialisation al?atoire
	 * 2) appel ? la m?thode jouer().</p>
	 *
	 * @param args
	 *       Arguments de la ligne de commande (non utilis?s)
	 * @see Takuzu#jouer
	 * @see Takuzu#choisir_grille
	 *
	 */
	
	public static void main(String[] args){
		/*
		Scanner sc = new Scanner(System.in);
		color_printf(ANSI_GREEN_BACKGROUND, ANSI_RED, "-------- Takuzu GAME -------!");
		//Grille g = creer_grille(4);
		String ch="Grilles/grilleX.txt";
		ch = choisir_grille(ch);
		System.out.println("Init. of game with file: " + ch);
		String str = sc.nextLine();
		jouer(ch);
		//afficher_grille(g);
		
		 */
		
	}

}
