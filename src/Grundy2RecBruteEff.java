import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Jeu de Grundy2
 * Ce programme ne contient que les méthodes permettant de tester jouerGagnant()
 * Cette version est brute sans aucune amélioration
 *
 * @author EDM115, nathan-basol
 */

class Grundy2RecBruteEff {
    long cpt;

    /**
     * Méthode principal du programme
     */
    void principal() {
        /*
        testJouerGagnant();
		testPremier();
		testSuivant();
		testEstGangnanteEfficacite();
        */
        partieJoueurContreOrdinateur();
    }
	
    /**
     * Joue le coup gagnant s'il existe
     * @param jeu plateau de jeu
     * @return vrai s'il y a un coup gagnant, faux sinon
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
        boolean gagnant = false;

        if (jeu == null) {
            System.err.println("jouerGagnant(): le paramètre jeu est null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();
            int ligne = premier(jeu, essai);
			// mise en oeuvre de la règle numéro2 : Une situation (ou position) est dite gagnante pour la machine (ou le joueur, peu importe), s'il existe AU MOINS UNE décomposition (c-à-d UNE action qui consiste à décomposer un tas en 2 tas inégaux) perdante pour l'adversaire.
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
     * Méthode RECURSIVE qui indique si la configuration (du jeu actuel ou jeu d'essai) est perdante
     * @param jeu plateau de jeu actuel (l'état du jeu à un certain moment au cours de la partie)
     * @return vrai si la configuration (du jeu) est perdante, faux sinon
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        boolean ret = true; // par défaut la configuration est perdante
		
        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        } else {
			// si il n'y a plus que des tas de 1 ou 2 allumettes dans le plateau de jeu alors la situation est forcément perdante (ret=true) = FIN de la récursivité
            if (!estPossible(jeu)) {
				ret = true;
            } else {
				// création d'un jeu d'essais qui va examiner toutes les décompositions possibles à partir de jeu
                ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0
				// toute première décomposition : enlever 1 allumette au premier tas qui possède au moins 3 allumettes, ligne = -1 signifie qu'il n'y a plus de tas d'au moins 3 allumettes
                int ligne = premier(jeu, essai);
                while ((ligne != -1) && ret) {
					cpt++;
					// mise en oeuvre de la règle numéro1 : Une situation (ou position) est dite perdante pour la machine (ou le joueur, peu importe) si et seulement si TOUTES ses décompositions possibles (c-à-d TOUTES les actions qui consistent à décomposer un tas en 2 tas inégaux) sont TOUTES gagnantes pour l'adversaire. Si UNE SEULE décomposition (à partir du jeu) est perdante (pour l'adversaire) alors la configuration n'EST PAS perdante. Ici l'appel à "estPerdante" est RECURSIF.
                    if (estPerdante(essai) == true) {
                        ret = false;
                    } else {
						// génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible) à partir du jeu, si ligne = -1 il n'y a plus de décomposition possible
                        ligne = suivant(jeu, essai, ligne);
                    }
                }
            }
        }
		
        return ret;
    }

	/**
     * Indique si la configuration est gagnante
	 * Méthode qui appelle simplement "estPerdante"
     * @param jeu plateau de jeu
     * @return vrai si la configuration est gagnante, faux sinon
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
     * Tests succincts de la méthode joueurGagnant()
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
     * Test d'un cas de la méthode jouerGagnant()
	 * @param jeu le plateau de jeu
	 * @param resJeu le plateau de jeu après avoir joué gagnant
	 * @param res le résultat attendu par jouerGagnant
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");
        // Act
        boolean resExec = jouerGagnant(jeu);
        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
        if (jeu.equals(resJeu) && res == resExec) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }	

    /**
     * Divise en deux tas les alumettes d'une ligne de jeu (1 ligne = 1 tas)
     * @param jeu tableau des alumettes par ligne
     * @param ligne ligne (tas) sur laquelle les alumettes doivent être séparées
     * @param nb nombre d'alumettes RETIREE de la ligne après séparation
     */
    void enlever(ArrayList<Integer> jeu, int ligne, int nb) {
		// traitement des erreurs
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
			// nouveau tas (case) ajouté au jeu (nécessairement en fin de tableau), ce nouveau tas contient le nbre d'allumettes retirées (nb) du tas à séparer			
            jeu.add(nb);
			// le tas restant avec "nb" allumettes en moins
            jeu.set(ligne, jeu.get(ligne) - nb);
        }
    }

    /**
     * Teste s'il est possible de séparer un des tas
     * @param jeu plateau de jeu
     * @return vrai s'il existe au moins un tas de 3 allumettes ou plus, faux sinon
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
     * Crée une toute première configuration d'essai à partir du jeu
     * @param jeu plateau de jeu
     * @param jeuEssai nouvelle configuration du jeu
     * @return le numéro du tas divisé en deux ou (-1) si il n'y a pas de tas d'au moins 3 allumettes
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
        int numTas = -1; // pas de tas à séparer par défaut
		int i;
		
        if (jeu == null) {
            System.err.println("premier(): le paramètre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("estPossible(): le paramètre jeuEssai est null");
        } else {
            // avant la copie du jeu dans jeuEssai il y a un reset de jeuEssai 
            jeuEssai.clear();
            i = 0;
			// recopie case par case, jeuEssai est le même que le jeu au départ
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
            i = 0;
			// rechercher un tas d'allumettes d'au moins 3 allumettes dans le jeu, sinon numTas = -1
			boolean trouve = false;
            while ((i < jeu.size()) && !trouve) {
				// si on trouve un tas d'au moins 3 allumettes
				if (jeuEssai.get(i) >= 3) {
					trouve = true;
					numTas = i;
				}
				i = i + 1;
            }
			// sépare le tas (case numTas) en un tas d'UNE SEULE allumette à la fin du tableau, le tas en case numTas a diminué d'une allumette (retrait d'une allumette), jeuEssai est le plateau de jeu qui fait apparaître cette séparation
            if (numTas != -1) {
                enlever(jeuEssai, numTas, 1);
            }
        }
		
        return numTas;
    }

    /**
     * Tests succincts de la méthode premier()
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
     * Test un cas de la méthode testPremier
	 * @param jeu le plateau de jeu
	 * @param ligne le numéro du tas séparé en premier
	 * @param res le plateau de jeu après une première séparation
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        if (jeuEssai.equals(res) && noLigne == ligne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
     * @param jeu plateau de jeu
     * @param jeuEssai configuration d'essai du jeu après séparation
     * @param ligne le numéro du tas qui est le dernier à avoir été séparé
     * @return le numéro du tas divisé en deux pour la nouvelle configuration, -1 si plus aucune décomposition n'est possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() + ", " + ligne + ") = ");
		int numTas = -1; // par défaut il n'y a plus de décomposition possible
        int i = 0;

		// traitement des erreurs
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramètre jeuEssai est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("estPossible(): le paramètre ligne est trop grand");
        } else {
			int nbAllumEnLigne = jeuEssai.get(ligne);
			int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);
			// si sur la même ligne (passée en paramètre) on peut encore retirer des allumettes, c-à-d si l'écart entre le nombre d'allumettes sur cette ligne et le nombre d'allumettes en fin de tableau est > 2, alors on retire encore 1 allumette sur cette ligne et on ajoute 1 allumette en dernière case		
            if ((nbAllumEnLigne - nbAllDernCase) > 2) {
                jeuEssai.set(ligne, (nbAllumEnLigne - 1));
                jeuEssai.set(jeuEssai.size() - 1, (nbAllDernCase + 1));
                numTas = ligne;
            } else {
			    // sinon il faut examiner le tas (ligne) suivant du jeu pour éventuellement le décomposer, on recrée une nouvelle configuration d'essai identique au plateau de jeu, copie du jeu dans JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
                boolean separation = false;
                i = ligne + 1; // tas suivant
				// si il y a encore un tas et qu'il contient au moins 3 allumettes alors on effectue une première séparation en enlevant 1 allumette
                while (i < jeuEssai.size() && !separation) {
					// le tas doit faire minimum 3 allumettes
                    if (jeu.get(i) > 2) {
                        separation = true;
						// on commence par enlever 1 allumette à ce tas
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
     * Tests succincts de la méthode suivant()
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
     * Test un cas de la méthode suivant
	 * @param jeu le plateau de jeu
	 * @param jeuEssai le plateau de jeu obtenu après avoir séparé un tas
	 * @param ligne le numéro du tas qui est le dernier à avoir été séparé
	 * @param resJeu est le jeuEssai attendu après séparation
	 * @param resLigne est le numéro attendu du tas qui est séparé
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
        if (jeuEssai.equals(resJeu) && noLigne == resLigne) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

	void joueurContreMachine() {
		
	}

	/**
	 * Remplit un jeu avec nb nombres aléatoires 
	 * @param jeu le jeu à remplir
	 * @param nb le nombre de nombres à ajouter
	 */
	void remplirAleatoire(ArrayList<Integer> jeu, int nb) {
		for (int i = 0; i < nb; i++) {
			int random = (int)(Math.random() * 10);
			System.out.println("random = " + random);
            jeu.add(random);
		}
	}

    /**
     * Teste l'efficacité de la méthode rechercheSeq
     */
    void testEstGangnanteEfficacite() {
        System.out.println();
        System.out.println("*** Efficacité de la méthode estGagnante");
        
        ArrayList<Integer> jeu;
        int n;
        boolean indice;
        long t1, t2, diffT;

        n = 3;

        for (int i = 1; i <= 22; i++) {
            jeu = new ArrayList<Integer>();
			jeu.add(n);
            cpt = 0;
            t1 = System.nanoTime();
            indice = estGagnante(jeu);
            t2 = System.nanoTime();
            diffT = (t2 - t1); 

            System.out.println("Pour n = " + n + ", le temps est : " + (diffT * 0.000000001) + " seconde");
            System.out.println("Pour n = " + n + ", le cpt est : " + cpt);
            System.out.println();

            n = n + 1;
			jeu.clear();
        }
    }

///////////////////////////////////////////////////////////////////////////

    /**
    * creer un tableau avec des allumettes 
    * @param nb nombre d'allumettes
    * @return un tableau avec le nombre d'allumettes
    **/
    char[] creerTableauAllumettes(int nb) {
        char[] ret = new char[nb];
        int i = 0;
        while (i < ret.length) {
            ret[i] = '|';
            i = i + 1;
        }
        return ret;
    }
    
   
    
    /**
    * affiche les différents tableaux d'allumettes
    * @param tab tableau d'entiers
    **/
    void creerEtAfficherLesTableaux(ArrayList<Integer> tab) {
        int i = 0;
        int ligne = 0;
        while (i < tab.size()) {
            if (tab.get(i) >= 1) {
                char[] creerTab = creerTableauAllumettes(tab.get(i));
                System.out.print(ligne + " : ");
                System.out.println(creerTab);
                ligne = ligne + 1;
            }
            i = i + 1;
        }
    }
    
    /**
    * teste si le joueur donne un bon numéro de ligne
    * @param tab tableau d'entiers
    * @param ligne la ligne que le joueur séléctionne
    * @return vrai ssi la ligne est bonne
    **/
    boolean bonChoixLigne(ArrayList<Integer> tab, int ligne) {
        boolean ret = true;
        if (ligne < 0 || ligne >= tab.size()) {
            ret = false;
        } else {
            if (tab.get(ligne) <= 2) {
                ret = false;
            }
        }
        return ret;
    }
    
    /**
    * teste si le tableau peut etre séparé par le nombre qu'a rentré le joueur
    * @param tab tableau d'entiers
    * @param ligne la ligne que le joueur selectionne
    * @param separer le nombre d'allumettes que je joueur veux séparer
    * @return vrai ssi c'est separable
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
    * renvoie l'indice du premier zero d'un tableau (le tableau comporte forcémment un zero)
    * @param tab tableau d'entiers 
    **/
    int premierZero(ArrayList<Integer> tab) {
        int ret = tab.size() + 1;
        return ret;
    }
    
    /**
    * separe une ligne d'un tableau
    * @param tab tableau d'entiers
    * @param ligne la ligne que le joueur séléctionne
    * @param separer le nombre d'allumettes que le joueur veut séparer
    * @return tableau avec la séparation faite
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
    * determine qui va jouer
    * @param joueur numéro qui est soit paire ou impaire
    * @param joueur1 nom du premier joueur
    * @param joueur2 nom du deuxième joueur 
    * @return renvoie le nom du joueur1 si le numéro est paire et inversement
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
    * demande au joueur le nombre d'allumettes de départ tant que le nombre d'allumettes saisis est incorrecte
    * @return un tableau d'entier avec l'indice 0 initialisé 
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
    * vérifie si un sul tas est séparable
    * @param tab tableau d'entiers
    * @return vrai ssi un seul tas est séparabme 
    **/
    boolean unSeulTasSeparable(ArrayList<Integer> tab) {
        boolean ret;
        int compteur = 0;
        int i = 0;
        while (i < tab.size()) {
            if (tab.get(i) > 2) {
                compteur = compteur + 1;
            }
            i = i + 1;
        }
        if (compteur == 1) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }
    
    /**
    * cherche l'indice du seul tas séparable
    * @param tab tableau d'entiers avec un seul tas séparable 
    * @return l'indice du seul tas séparable
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
            i = i + 1;
        }
        return ret;
    }
    
    /**
    * demande au joueur de rentrer une ligne valide
    * @param tab tableau d'entiers
    * @return numéro de ligne valide rentré par le joueur
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
    * demande au joueur de rentrer une separation valide
    * @param tab tableau d'entiers
    * @param ligne ligne valide
    * @return une séparation valide rentrée par le joueur
    **/
    int demandeSeparer(ArrayList<Integer> tab, int ligne) {
        int ret = SimpleInput.getInt("Nombre d'allumettes à séparer : ");
        while (!bonneSeparation(tab, ligne, ret)) {
            ret = SimpleInput.getInt("Nombre d'allumettes à séparer : ");
        }
        return ret;
    }
    
    /**
    * teste si un tableau ne contient que des nombres inférieurs ou égaux à 2
    * @param tab tableau d'entiers
    * @return vrai ssi un tableau ne contient qeu des nombres inférieurs ou égaux à 2
    **/
    boolean finPartie(ArrayList<Integer> tab) {
        boolean ret = false;
        int i = 0;
        boolean sort = true;
        while (i < tab.size()) {
            if (tab.get(i) > 2) {
                sort = false;
            } 
            i = i + 1;
        }
        if (!sort) {
            ret = false;
        } else {
            ret = true;
        }
        return ret;
    }
    
    /**
    * affiche une ligne
    **/
    void ligneDeSeparation() {
        System.out.println();
        System.out.println("-----------------");
        System.out.println();
    }
    
    /**
    * demande au joueur qui joue en premier entre lui et l'ordinateur 
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
    * l'ordinateur rentre aléatoirement une séparation entre 0 et le nombre d'allumettes de la ligne choisie
    * @param tab tableau d'entiers
    * @param ligne ligne valide
    * @return une séparation valide rentrée par l'ordinateur
    **/
    int demandeSeparerOrdinateur(ArrayList<Integer> tab, int ligne) {
        int ret = (int) (Math.random() * (tab.get(ligne)));
        while (!bonneSeparation(tab, ligne, ret)) {
            ret = (int) (Math.random() * (tab.get(ligne)));
        }
        return ret;
    }

    /**
    * l'ordinateur choisir un nombre au hasard entre 0 et la taille du tableau
    * @param tab tableau d'entiers
    * @return numéro de ligne valide rentré par l'ordinateur
    **/
    int demandeLigneOrdinateur(ArrayList<Integer> tab) {
        int ret = (int) (Math.random() * (tab.size()));
        while (!bonChoixLigne(tab, ret)) {
            ret = (int) (Math.random() * (tab.size()));
        }
        return ret;
    }
   

    /**
    * joue une partie joueur contre ordinateur
    **/
    void partieJoueurContreOrdinateur() {
        // Initialisation du tableau d'allumettes
        ArrayList<Integer> tabAllumettes = demandeNombreAllumettes();
       
        // Demande le nom du joueur + initilalisation du nom de l'ordinateur
        String joueur1 = SimpleInput.getString("Nom du joueur : ");
        String ordi = "Ordi";
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
            joueur = joueur + 1;
        }
        
        // Affichage du nom du gagnant
        ligneDeSeparation();
        System.out.println(quiJoue + " a gagné !");
    }
}
