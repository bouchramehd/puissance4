package src.main;

import java.util.Scanner;

class Puissance {
    protected static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Entrez votre nom: ");
        String nom = scanner.nextLine();
        System.out.println("--");

        Partie p = new Partie(new deuxieme_joueur(Jeu.BLEU), new Humain(nom, Jeu.ROUGE));
        p.joue();
    }
}

class Partie {

    private Joueur[] joueurs = new Joueur[2];
    private Jeu jeu;

    public Partie(Joueur joueur1, Joueur joueur2) {
        joueurs[0] = joueur1;
        joueurs[1] = joueur2;
        jeu = new Jeu();
    }

    public void joue() {
        int vainqueur = -1;
        int cJoueur = 0;

        while (vainqueur==-1) {
            joueurs[cJoueur].joue(jeu);
            if (jeu.estPlein()) {
                vainqueur = -1;
            }


            if (jeu.cherche4()) {
                vainqueur = cJoueur;
            }


            cJoueur++;
            cJoueur %= 2;
        }

        System.out.println("La partie est finie.");
        jeu.afficher();
        if (vainqueur == -1) {
            System.out.println("Match nul.");
        } else {
            System.out.println("Le vainqueur est " + joueurs[vainqueur].getNom());
        }
    }
}

class Jeu {

    public final static int VIDE = 0;
    public final static int BLEU = 1;
    public final static int ROUGE = 2;

    private int taille;
    private int[][] grille;

    public Jeu(int taille) {
        initJeu(taille);
    }

    public Jeu() {
        initJeu(8);
    }

    private void initJeu(int taille) {
        this.taille = taille;
        grille = new int[taille][taille];
        for (int col = 0; col < taille ; col++) {
            for (int row = 0; row < taille; row++) {
                grille[col][row] = VIDE;
            }
        }
    }

    public boolean joueCoup(int col, int joueur) {
        if ((col < 0) || (col >= taille)) {
            return false;
        }


        for (int ligne = 0; ligne < taille; ligne++) {
            if (grille[col][ligne] == VIDE) {
                grille[col][ligne] = joueur;
                return true;
            }
        }


        return false;
    }


    public boolean cherche4() {

        for (int ligne = 0; ligne < taille; ligne++) {
            if (cherche4alignes(0, ligne, 1, 0)) {
                return true;
            }
        }


        for (int col = 0; col < taille; col++) {
            if (cherche4alignes(col, 0, 0, 1)) {
                return true;
            }
        }


        for (int col = 0; col < taille; col++) {

            if (cherche4alignes(col, 0, 1, 1)) {
                return true;
            }

            if (cherche4alignes(col, 0, -1, 1)) {
                return true;
            }
        }


        for (int ligne = 0; ligne < taille; ligne++) {

            if (cherche4alignes(0, ligne, 1, 1)) {
                return true;
            }
            // Deuxième diagonale ( \ )
            if (cherche4alignes(taille - 1, ligne, -1, 1)) {
                return true;
            }
        }

        // On n'a rien trouvé
        return false;
    }


    private boolean cherche4alignes(int oCol, int oLigne, int dCol, int dLigne) {
        int couleur = VIDE;
        int compteur = 0;

        int curCol = oCol;
        int curRow = oLigne;

        while ((curCol >= 0) && (curCol < taille) && (curRow >= 0) && (curRow < taille)) {
            if (grille[curRow][curCol] != couleur) {

                couleur = grille[curRow][curCol];
                compteur = 1;
            } else {

                compteur++;
            }


            if ((couleur != VIDE) && (compteur == 4)) {
                return true;
            }


            curCol += dCol;
            curRow += dLigne;
        }

        return false;
    }


    public boolean estPlein() {

        for (int col = 0; col < taille; col++) {
            for (int ligne = 0; ligne < taille; ligne++) {
                if (grille[col][ligne] == VIDE) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getTaille() {
        return taille;
    }

    public void afficher() {
        for (int ligne = taille - 1; ligne >= 0; --ligne) {
            for (int col = 0; col < taille; col++) {
                switch (grille[col][ligne]) {
                    case VIDE:
                        System.out.print(' ');
                        break;
                    case ROUGE:
                        System.out.print('R');
                        break;
                    case BLEU:
                        System.out.print('B');
                        break;
                }
            }
            System.out.println();
        }

        for (int i = 0; i < taille; ++i) {
            System.out.print('-');
        }
        System.out.println();
        for (int i = 1; i <= taille; ++i) {
            System.out.print(i);
        }
        System.out.println();
    }
}

class Joueur {
    private String nom;
    private int couleur;

    public Joueur(String nom, int couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    public String getNom() {
        return nom;
    }

    public int getCouleur() {
        return couleur;
    }


    public void joue(Jeu jeu) {}

}

class Humain extends Joueur {

    public Humain(String nom,int couleur) {
        super(nom, couleur);
    }

    public void joue(Jeu jeu) {
        jeu.afficher();

        boolean valide;
        do {
            System.out.println("Joueur " + this.getNom() + ", entrez un numéro de colonne" +
                    "  (entre 1 et " + jeu.getTaille() + ") : ");

            int col = Puissance.scanner.nextInt();
            col--;

            valide = jeu.joueCoup(col, this.getCouleur());
            if (valide == false) {
                System.out.println("-> Coup NON valide.");
            }
        } while (valide == false);
    }
}

class deuxieme_joueur extends Joueur {

    public deuxieme_joueur(int couleur) {
        super("Le programme", couleur);
    }

    public void joue(Jeu jeu) {
        for (int col = 0; col < jeu.getTaille(); col++) {
            if (jeu.joueCoup(col, this.getCouleur())) {
                System.out.println(this.getNom() + " a joué en " + (col + 1));
                return;
            }
        }
    }
}