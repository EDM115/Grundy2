import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Grundy2 Game
 * This class adds storage of loosing heaps
 * @author EDM115, nathan-basol
 */
class Grundy2RecPerdantes {

	/**
	 * Global variable used to mesure the efficiency
	 */
	long cpt;

	/**
	 * Global variable used to store the loosing heaps
	 */
	ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>();

	/**
	 * Principal function of the class
	 */
	void principal() {
		/*
		testJouerGagnant();
		testPremier();
		testSuivant();
		*/
		testEstGagnanteEfficacite();
		partieJoueurContreOrdinateur();
	}
	
	/**
	 * Plays the winning move if it exists
	 * @param jeu board game
	 * @return true if there's a winning move, false otherwise
	 */
	boolean jouerGagnant(ArrayList<Integer> jeu) {
		boolean gagnant = false;

		if (jeu == null) {
			System.err.println("jouerGagnant(): le paramètre jeu est null");
		} else {
			ArrayList<Integer> essai = new ArrayList<Integer>();
			int ligne = premier(jeu, essai);
			while (ligne != -1 && !gagnant) {
				if (estPerdante(essai)) {
					jeu.clear();
					gagnant = true;
					for (int i = 0; i < essai.size(); i++) {
						jeu.add(essai.get(i));
					}
				} else {
					ligne = suivant(jeu, essai, ligne);
				}
			}
		}
		
		return gagnant;
	}
	
	/**
	 * RECURSIVE method that indicates if the configuration (of the current game or test game) is losing
	 * @param jeu current game board (the state of the game at some point during the game)
	 * @return true if the configuration (of the game) is losing, false otherwise
	 */
	boolean estPerdante(ArrayList<Integer> jeu) {
		boolean ret = true;
		
		if (jeu == null) {
			System.err.println("estPerdante(): le paramètre jeu est null");
		} else {
			if (!estPossible(jeu)) {
				ret = true;
			} else if (estConnuePerdante(jeu)) {
				ret = true;
			} else {
				ArrayList<Integer> essai = new ArrayList<Integer>();
				int ligne = premier(jeu, essai);
				while ((ligne != -1) && ret) {
					cpt++;
					if (estPerdante(essai)) {
						ret = false;
					} else {
						ligne = suivant(jeu, essai, ligne);
					}
				}
				if (ret) {
					posPerdantes.add(normalise(essai));
				}
			}
		}
		
		return ret;
	}

	/**
	 * Indicates if the configuration is winning
	 * @param jeu game board
	 * @return true if the configuration is winning, false otherwise
	 */
	boolean estGagnante(ArrayList<Integer> jeu) {
		boolean ret = false;
		
		if (jeu == null) {
			System.err.println("estGagnante(): le paramètre jeu est null");
		} else {
			ret = !estPerdante(jeu);
		}

		return ret;
	}

	/**
	 * Short tests of the jouerGagnant() method
	 */
	void testJouerGagnant() {
		System.out.println();
		System.out.println("*** testJouerGagnant() ***");
		System.out.println("Test des cas normaux");
		ArrayList<Integer> jeu1 = new ArrayList<Integer>();
		jeu1.add(6);
		ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
		resJeu1.add(4);
		resJeu1.add(2);
		testCasJouerGagnant(jeu1, resJeu1, true);
	}

	/**
	 * Testing a case of the jouerGagnant() method
	 * @param jeu game board
	 * @param resJeu the game board after playing gagnant
	 * @param res the result awaited by jouerGagnant
	 */
	void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
		System.out.print("jouerGagnant (" + jeu.toString() + ") : ");
		boolean resExec = jouerGagnant(jeu);
		System.out.print(jeu.toString() + " " + resExec + " : ");
		if (jeu.equals(resJeu) && res == resExec) {
			System.out.println("OK\n");
		} else {
			System.err.println("ERREUR\n");
		}
	}	

	/**
	 * Divide the matches of a line of play into two heaps (1 line = 1 heap)
	 * @param jeu table of matches by line
	 * @param ligne line (heap) on which matches should be separated
	 * @param nb number of matches REMOVED from line after separation
	 */
	void enlever(ArrayList<Integer> jeu, int ligne, int nb) {
		if (jeu == null) {
			System.err.println("enlever() : le paramètre jeu est null");
		} else if (ligne >= jeu.size()) {
			System.err.println("enlever() : le numéro de ligne est trop grand");
		} else if (nb >= jeu.get(ligne)) {
			System.err.println("enlever() : le nb d'allumettes à retirer est trop grand");
		} else if (nb <= 0) {
			System.err.println("enlever() : le nb d'allumettes à retirer est trop petit");
		} else if (2 * nb == jeu.get(ligne)) {
			System.err.println("enlever() : le nb d'allumettes à retirer est la moitié");
		} else {		
			jeu.add(nb);
			jeu.set(ligne, jeu.get(ligne) - nb);
		}
	}

	/**
	 * Tests if it's possible to separate a heap
	 * @param jeu game board
	 * @return true if there is at least a heap of 3 or more matches, false otherwise
	 */
	boolean estPossible(ArrayList<Integer> jeu) {
		boolean ret = false;

		if (jeu == null) {
			System.err.println("estPossible(): le paramètre jeu est null");
		} else {
			int i = 0;
			while (i < jeu.size() && !ret) {
				if (jeu.get(i) > 2) {
					ret = true;
				}
				i = i + 1;
			}
		}

		return ret;
	}

	/**
	 * Create a very first trial configuration from the game
	 * @param jeu game board
	 * @param jeuEssai new game configuration
	 * @return the number of the heap divided in half or -1 if there is no heap of at least 3 matches
	 */
	int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
		int numTas = -1;
		int i;
		
		if (jeu == null) {
			System.err.println("premier(): le paramètre jeu est null");
		} else if (!estPossible((jeu)) ){
			System.err.println("premier(): aucun tas n'est divisible");
		} else if (jeuEssai == null) {
			System.err.println("estPossible(): le paramètre jeuEssai est null");
		} else {
			jeuEssai.clear();
			i = 0;
			while (i < jeu.size()) {
				jeuEssai.add(jeu.get(i));
				i = i + 1;
			}
			i = 0;
			boolean trouve = false;
			while ((i < jeu.size()) && !trouve) {
				if (jeuEssai.get(i) >= 3) {
					trouve = true;
					numTas = i;
				}
				i = i + 1;
			}
			if (numTas != -1) {
				enlever(jeuEssai, numTas, 1);
			}
		}
		
		return numTas;
	}

	/**
	 * Short tests of the premier() method
	 */
	void testPremier() {
		System.out.println();
		System.out.println("*** testPremier()");
		ArrayList<Integer> jeu1 = new ArrayList<Integer>();
		jeu1.add(10);
		jeu1.add(11);
		int ligne1 = 0;
		ArrayList<Integer> res1 = new ArrayList<Integer>();
		res1.add(9);
		res1.add(11);
		res1.add(1);
		testCasPremier(jeu1, ligne1, res1);
	}

	/**
	 * Test a case of the testPremier method
	 * @param jeu game board
	 * @param ligne the number of the heap separated first
	 * @param res game board after the first separation
	 */
	void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
		System.out.print("premier (" + jeu.toString() + ") : ");
		ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
		int noLigne = premier(jeu, jeuEssai);
		System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
		if (jeuEssai.equals(res) && noLigne == ligne) {
			System.out.println("OK\n");
		} else {
			System.err.println("ERREUR\n");
		}
	}

	/**
	 * Generates the following test setup (i.e. ONE possible decomposition)
	 * @param jeu game board
	 * @param jeuEssai play test configuration after separation
	 * @param ligne the number of the heap which is the last to have been separated
	 * @return the number of the heap divided in two for the new configuration, -1 if no more decomposition is possible
	 */
	int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
		int numTas = -1;
		int i = 0;

		if (jeu == null) {
			System.err.println("suivant(): le paramètre jeu est null");
		} else if (jeuEssai == null) {
			System.err.println("suivant() : le paramètre jeuEssai est null");
		} else if (ligne >= jeu.size()) {
			System.err.println("estPossible(): le paramètre ligne est trop grand");
		} else {
			int nbAllumEnLigne = jeuEssai.get(ligne);
			int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);	
			if ((nbAllumEnLigne - nbAllDernCase) > 2) {
				jeuEssai.set(ligne, (nbAllumEnLigne - 1));
				jeuEssai.set(jeuEssai.size() - 1, (nbAllDernCase + 1));
				numTas = ligne;
			} else {
				jeuEssai.clear();
				for (i = 0; i < jeu.size(); i++) {
					jeuEssai.add(jeu.get(i));
				}
				boolean separation = false;
				i = ligne + 1;
				while (i < jeuEssai.size() && !separation) {
					if (jeu.get(i) > 2) {
						separation = true;
						enlever(jeuEssai, i, 1);
						numTas = i;
					} else {
						i = i + 1;
					}
				}				
			}
		}
		
		return numTas;
	}

	/**
	 * Short tests of the suivant() method
	 */
	void testSuivant() {
		System.out.println();
		System.out.println("*** testSuivant() ****");
		int ligne1 = 0;
		int resLigne1 = 0;
		ArrayList<Integer> jeu1 = new ArrayList<Integer>();
		jeu1.add(10);
		ArrayList<Integer> jeuEssai1 = new ArrayList<Integer>();
		jeuEssai1.add(9);
		jeuEssai1.add(1);
		ArrayList<Integer> res1 = new ArrayList<Integer>();
		res1.add(8);
		res1.add(2);
		testCasSuivant(jeu1, jeuEssai1, ligne1, res1, resLigne1);
		int ligne2 = 0;
		int resLigne2 = -1;
		ArrayList<Integer> jeu2 = new ArrayList<Integer>();
		jeu2.add(10);
		ArrayList<Integer> jeuEssai2 = new ArrayList<Integer>();
		jeuEssai2.add(6);
		jeuEssai2.add(4);
		ArrayList<Integer> res2 = new ArrayList<Integer>();
		res2.add(10);
		testCasSuivant(jeu2, jeuEssai2, ligne2, res2, resLigne2);
		int ligne3 = 1;
		int resLigne3 = 1;
		ArrayList<Integer> jeu3 = new ArrayList<Integer>();
		jeu3.add(4);
		jeu3.add(6);
		jeu3.add(3);
		ArrayList<Integer> jeuEssai3 = new ArrayList<Integer>();
		jeuEssai3.add(4);
		jeuEssai3.add(5);
		jeuEssai3.add(3);
		jeuEssai3.add(1);
		ArrayList<Integer> res3 = new ArrayList<Integer>();
		res3.add(4);
		res3.add(4);
		res3.add(3);
		res3.add(2);
		testCasSuivant(jeu3, jeuEssai3, ligne3, res3, resLigne3);
	}

	/**
	 * Test a case of the suivant method
	 * @param jeu game board
	 * @param jeuEssai the game board obtained after separating a heap
	 * @param ligne the number of the heap which is the last to have been separated
	 * @param resJeu is the jeuEssai expected after separation
	 * @param resLigne is the expected number of the heap that is separated
	 */
	void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
		System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
		int noLigne = suivant(jeu, jeuEssai, ligne);
		System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
		if (jeuEssai.equals(resJeu) && noLigne == resLigne) {
			System.out.println("OK\n");
		} else {
			System.err.println("ERREUR\n");
		}
	}

///////////////////////////////////////////////////////////////////////////

	/**
	 * Fills a game with nb random numbers
	 * @param jeu the game to fill
	 * @param nb the number of numbers to add
	 */
	void remplirAleatoire(ArrayList<Integer> jeu, int nb) {
		for (int i = 0; i < nb; i++) {
			int random = (int)(Math.random() * 10);
			jeu.add(random);
		}
	}

	/**
	 * Testing the efficiency of the method estGagnante
	 */
	void testEstGagnanteEfficacite() {
		System.out.println();
		System.out.println("*** Efficacité de la méthode estGagnante");
		int n = 3;
		long t1, t2, diffT;
		
		for (int i = 1; i <= 29; i++) {
			ArrayList<Integer> jeu = new ArrayList<Integer>();
			jeu.add(n);
			cpt = 0;
			t1 = System.nanoTime();
			boolean indice = estGagnante(jeu);
			t2 = System.nanoTime();
			diffT = (t2 - t1); 
			System.out.println("Pour n = " + n + ", le temps est : " + diffT + " nanosecondes");
			System.out.println("Pour n = " + n + ", le cpt est : " + cpt);
			System.out.println();
			n++;
			jeu.clear();
		}
	}

	/**
	* Creates an array with matches
	* @param nb number of matches
	* @return the final array
	**/
	char[] creerTableauAllumettes(int nb) {
		char[] ret = new char[nb * 3];
		int i = 0;

		while (i < ret.length) {
			ret[i] = ' ';
			ret[i + 1] = '|';
			ret[i + 2] = ' ';
			i += 3;
		}

		return ret;
	}

	/**
	* Prints the different matches arrays
	* @param tab game board
	**/
	void creerEtAfficherLesTableaux(ArrayList<Integer> tab) {
		int i = 0;
		int ligne = 0;

		while (i < tab.size()) {
			if (tab.get(i) >= 1) {
				char[] creerTab = creerTableauAllumettes(tab.get(i));
				System.out.print(ligne + " :");
				System.out.println(creerTab);
				ligne++;
			}
			i++;
		}
	}

	/**
	* Tests if the player gave the right line number
	* @param tab game board
	* @param ligne the line selected by the player
	* @return true only if the line is correct
	**/
	boolean bonChoixLigne(ArrayList<Integer> tab, int ligne) {
		boolean ret = true;

		if (ligne < 0 || ligne >= tab.size() || tab.get(ligne) <= 2) {
			ret = false;
		}

		return ret;
	}

	/**
	 * Tests if the array can be separated by the number the player entered
	* @param tab game board
	* @param ligne the line selected by the player
	* @param separer the number the player entered
	* @return true if the array can be separated by the number the player entered
	**/
	boolean bonneSeparation(ArrayList<Integer> tab, int ligne, int separer) {
		boolean ret = false;

		if (bonChoixLigne(tab, ligne)) {
			if (tab.get(ligne) % 2 == 0) {
				if ((separer > 0) && (separer < tab.get(ligne)) && (separer != (tab.get(ligne) / 2))) {
					ret = true;
				}
			} else {
				if ((separer > 0) && (separer < tab.get(ligne))) {
					ret = true;
				}
			}
		}

		return ret;
	}

	/**
	* Returns the next empty spot in the array
	* @param tab game board
	* @return the next empty spot in the array
	**/
	int premierZero(ArrayList<Integer> tab) {
		int ret = tab.size() + 1;

		return ret;
	}

	/**
	* Separates the array at a given line
	* @param tab game board
	* @param ligne the line selected by the player
	* @param separer the number of matches the player entered
	* @return the array with the separated line
	**/
	ArrayList<Integer> separation(ArrayList<Integer> tab, int ligne, int separer) {
		int tmp;

		if (bonneSeparation(tab, ligne, separer)) {
			try {
				if (tab.get(ligne + 1) == 0) {
					tmp = tab.get(ligne) - separer;
					tab.add(ligne + 1, tmp);
					tab.remove(ligne);
					tab.add(ligne, separer);
				} else {
					int zero = premierZero(tab);
					tmp = tab.get(ligne) - separer;
					tab.add(zero, tmp);
					tab.remove(ligne);
					tab.add(ligne, separer);
				}
			} catch (IndexOutOfBoundsException e) {
				tmp = tab.get(ligne) - separer;
				tab.add(ligne + 1, tmp);
				tab.remove(ligne);
				tab.add(ligne, separer);
			}
		}

		return tab;
	}

	/**
	* Determine who is going to play
	* @param joueur even or odd number
	* @param joueur1 name of the first player
	* @param joueur2 name of the second player
	* @return returns the name of the player1 if the number is even, returns the name of the player2 if the number is odd
	**/
	String quiVaJouer(int joueur, String joueur1, String joueur2) {
		String ret;

		if (joueur % 2 == 0) {
			ret = joueur1;
		} else {
			ret = joueur2;
		}

		return ret;
	}

	/**
	* Asks the player for the starting number of matches as long as the number of matches entered is incorrect
	* @return an array of integers with index 0 initialized
	**/
	ArrayList<Integer> demandeNombreAllumettes() {
		int initialisation = SimpleInput.getInt("Nombre d'allumettes : "); 

		while (initialisation <= 2) {
			initialisation = SimpleInput.getInt("Nombre d'allumettes : "); 
		}
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add(0, initialisation);

		return ret;
	}

	/**
	* Checks if an heap is separable
	* @param tab game board
	* @return true only if one heap is separable
	**/
	boolean unSeulTasSeparable(ArrayList<Integer> tab) {
		boolean ret = false;
		int compteur = 0;
		int i = 0;

		while (i < tab.size()) {
			if (tab.get(i) > 2) {
				compteur++;
			}
			i++;
		}
		if (compteur == 1) {
			ret = true;
		}

		return ret;
	}

	/**
	* Searches for the index of the only separable heap
	* @param tab game board with the only separable heap
	* @return the index of that heap
	**/
	int indiceDuSeulTasSeparable(ArrayList<Integer> tab){
		int ret = 0;
		int i = 0;
		boolean sort = false;

		while ((i < tab.size()) && !sort) {
			if (tab.get(i) > 2) {
				ret = i;
				sort = true;
			}
			i++;
		}

		return ret;
	}

	/**
	* Asks the player for a valid line as long as the line entered is incorrect
	* @param tab game board
	* @return valid line entered by the player
	**/
	int demandeLigne(ArrayList<Integer> tab) {
		int ret;

		if (!unSeulTasSeparable(tab)) {
			ret = SimpleInput.getInt("Ligne : ");
			while (!bonChoixLigne(tab, ret)) {
				ret = SimpleInput.getInt("Ligne : ");
			}
		} else {
			ret = indiceDuSeulTasSeparable(tab);
		}

		return ret;
	}

	/**
	* Ask the player for a valid number of matches to separate as long as the number entered is incorrect
	* @param tab game board
	* @param ligne valid line
	* @return valid separation entered by the player
	**/
	int demandeSeparer(ArrayList<Integer> tab, int ligne) {
		int ret = SimpleInput.getInt("Nombre d'allumettes à séparer : ");

		while (!bonneSeparation(tab, ligne, ret)) {
			ret = SimpleInput.getInt("Nombre d'allumettes à séparer : ");
		}

		return ret;
	}

	/**
	 * Tests if an array contains only numbers less than or equal to 2
	* @param tab game board
	* @return true only if the array contains only numbers less than or equal to 2
	**/
	boolean finPartie(ArrayList<Integer> tab) {
		boolean ret = false;
		int i = 0;
		boolean sort = true;

		while (i < tab.size()) {
			if (tab.get(i) > 2) {
				sort = false;
			} 
			i++;
		}
		if (sort) {
			ret = true;
		}

		return ret;
	}

	/**
	* Prints a line
	**/
	void ligneDeSeparation() {
		System.out.println();
		System.out.println("-----------------");
		System.out.println();
	}

	/**
	* Asks who plays first
	* @return 0 if the player plays first, 1 if the AI plays first
	**/
	int choixPremierJoueur() {
		System.out.println("Premier à jouer");
		System.out.println("- 0 le joueur");
		System.out.println("- 1 l'ordinateur");
		int ret = SimpleInput.getInt("qui est le premier à jouer : ");

		while ((ret > 1) || (ret < 0)) {
			ret = SimpleInput.getInt("qui est le premier à jouer : ");
		}

		return ret;
	}

	/**
	 * The AI chooses a random number between 0 and the number of matches in the heap chosen
	* @param tab game board 
	* @param ligne valid line
	* @return a valid separation chosen by the AI
	**/
	int demandeSeparerOrdinateur(ArrayList<Integer> tab, int ligne) {
		int ret = (int)(Math.random() * (tab.get(ligne)));

		while (!bonneSeparation(tab, ligne, ret)) {
			ret = (int)(Math.random() * (tab.get(ligne)));
		}

		return ret;
	}

	/**
	 * The AI choose a random number between 0 and the size of the array
	* @param tab game board
	* @return valid line number chosen by the AI
	**/
	int demandeLigneOrdinateur(ArrayList<Integer> tab) {
		int ret = (int)(Math.random() * (tab.size()));

		while (!bonChoixLigne(tab, ret)) {
			ret = (int)(Math.random() * (tab.size()));
		}

		return ret;
	}

	/**
	 * Determine if the configuration is known as losing in posPerdantes
     * @param jeu game board
     * @return true if the game is in posPerdantes
     */
    boolean estConnuePerdante(ArrayList<Integer> jeu) {
        // création d'une copie de jeu triée sans 1, ni 2
        ArrayList<Integer> copie = normalise(jeu);
        boolean ret = false;
        int i = 0;

        while (!ret && i < posPerdantes.size()) {
            if (sontIdentiques(copie, posPerdantes.get(i))) {
                ret = true;
            }
            i++;
        }

        return ret;
    }

	/**
	* Plays a Player vs AI game
	**/
	void partieJoueurContreOrdinateur() {
		// Initialisation du tableau d'allumettes
		ArrayList<Integer> tabAllumettes = demandeNombreAllumettes();
		// Demande le nom du joueur + initilalisation du nom de l'ordinateur
		String joueur1 = SimpleInput.getString("Nom du joueur : ");
		String ordi = "IA";
		ligneDeSeparation();
		// Demande qui joue en premier
		int joueur = choixPremierJoueur();
		String quiJoue = quiVaJouer(joueur, joueur1, ordi);
		ligneDeSeparation();
		// Affichage de la ligne 0 contenant un certains nombre d'allumettes
		creerEtAfficherLesTableaux(tabAllumettes);
		// La partie continue tant que l'on peut séparer les allumettes
		while (!finPartie(tabAllumettes)) {
			// Détermine qui va jouer cette manche et affichage du nom
			quiJoue = quiVaJouer(joueur, joueur1, ordi);
			ligneDeSeparation();
			System.out.println("Tour du joueur : " + quiJoue);
			// Initialisation des variables ligne et separer
			int ligne;
			int separer;
			// Cas de quand le joueur 1 joue
			if ((joueur % 2) == 0) {
				// Demande la ligne au joueur
				ligne = demandeLigne(tabAllumettes);
				// Demande le nombre d'allumettes qu'il veut séparer
				separer = demandeSeparer(tabAllumettes, ligne);
				separation(tabAllumettes, ligne, separer);
				ligneDeSeparation();
			// Cas de quand l'ordinateur joue
			} else {
				if (jouerGagnant(tabAllumettes)) {
					;
				} else {
					// l'ordinateur choisi une ligne au hasard
					ligne = demandeLigneOrdinateur(tabAllumettes);
					// l'ordinateur choisi une séparation au hasard
					separer = demandeSeparerOrdinateur(tabAllumettes, ligne);
					separation(tabAllumettes, ligne, separer);
				}
				ligneDeSeparation();
			}
			// On affiche les tableaux d'allumettes
			creerEtAfficherLesTableaux(tabAllumettes);
			// On incrémente la variable joueur pour changer de joueur
			joueur++;
		}
		// Affichage du nom du gagnant
		ligneDeSeparation();
		System.out.println(quiJoue + " a gagné !");
	}

	/**
	 * Normalize the game board by removing the heaps with 1 or 2 matches
	 * @param jeu game board
	 * @return normalized game board
	 */
	ArrayList<Integer> normalise(ArrayList<Integer> jeu) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		if (jeu == null) {
			System.err.println("normalise(): le paramètre jeu est null");
		} else {
			for (int i = 0; i < jeu.size(); i++) {
				if (jeu.get(i) > 2) {
					ret.add(jeu.get(i));
				}
			}
			Collections.sort(ret);
		}
		
		return ret;
	}

	/**
	 * Indicates if two game boards are identical
	 * @param jeu1 game board 1
	 * @param jeu2 game board 2
	 * @return true if the game boards are identical, false otherwise
	 */
	boolean sontIdentiques(ArrayList<Integer> jeu1, ArrayList<Integer> jeu2) {
		boolean ret = true;
		
		if (jeu1 == null || jeu2 == null) {
			System.err.println("sontIdentiques(): un des paramètres est null");
		} else {
			if (jeu1.size() != jeu2.size()) {
				ret = false;
			} else {
				for (int i = 0; i < jeu1.size() && ret; i++) {
					if (jeu1.get(i) != jeu2.get(i)) {
						ret = false;
					}
				}
			}
		}
		
		return ret;
	}
}
