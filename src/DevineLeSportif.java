import extensions.CSVFile;
import extensions.File;

class DevineLeSportif extends Program {

  // Déclaration des variables constantes
  String reponseUtilisateur;

  String couleur_texte;
  String couleur_de_fond;
  String couleur_erreur;
  String couleur_confirmation;
  String couleur_ecriture;
  String black;

  final String DEVINE_LE_SPORTIF_TXT = "../ressources/txt/DevineLeSportif.txt";
  final String FONCTIONNEMENT_DU_JEU_TXT ="../ressources/txt/FonctionnementDuJeu.txt";
  final String CLASSEMENT_TXT = "../ressources/txt/Classement.txt";
  final String CATEGORIES_TXT = "../ressources/txt/Categories.txt";
  final String JOUEUR_CSV = "../ressources/csv/joueur.csv";
  final String FOOT_CSV = "../ressources/csv/sportif_Foot.csv";
  final String F1_CSV = "../ressources/csv/sportif_F1.csv";
  final String TENNIS_CSV = "../ressources/csv/sportif_Tennis.csv";
  final String SIU = "../ressources/sound/siu.wav";

  void initialisationCouleur() { // Fonction qui change les couleurs du terminal
    couleur_texte = "WHITE";
    couleur_de_fond = "BLACK";
    couleur_erreur = "YELLOW";
    couleur_confirmation = "RED";
    couleur_ecriture = "CYAN";
    black = "BLACK";
  }

  Joueur newJoueur(String nom) { // Fonction qui crée un nouveau joueur
    Joueur joueur = new Joueur();
    joueur.nom = nom;
    joueur.score = 0;
    return joueur;
  }

  Categorie choixCategorie(int numCategorie) { // Fonction qui permet à l'utilisateur de choisir une catégorie
    Categorie categorie = new Categorie();
    categorie.numCategorie = numCategorie;
    return categorie;
  }

  Niveau choixNiveau(int numNiveau) { // Fonction qui permet de choisir un niveau de jeu
    Niveau niveau = new Niveau();
    niveau.niveau = numNiveau;
    return niveau;
  }

  boolean MotVerifie(String mot, String mot2) {
    boolean verifie = false;
    int nbVerifie = 0;
    if (length(mot) == length(mot2)) {
      for (int i = 0; i < length(mot2); i++) {
        if (charAt(mot, i) == charAt(mot2, i)) {
          nbVerifie += 1;
        }
      }
      if (nbVerifie == length(mot2)) {
        verifie = true;
      }
    }
    return verifie;
  }

  void afficherTxt(String nomTxt) { // Fonction affiche le texte ascii "Trouve Le Sportif"
    File file = newFile(nomTxt);
    String ligne = "";
    while (ready(file)) {
      ligne = readLine(file);
      println(ligne);
    }
  }

  int nbLignes(String nomCsv) { // Fonction qui compte le nombre de ligne d'un fichier CSV donné en paramètre
    CSVFile file = loadCSV(nomCsv);
    int nbLigne = rowCount(file);
    return nbLigne;
  }

  int nbCol(String nomCsv) { // Fonction qui compte le nombre de ligne d'un fichier CSV donné en paramètre
    CSVFile file = loadCSV(nomCsv);
    int NBcolonnes = columnCount(file);
    return NBcolonnes;
  }

  String verifInt(String entree) { // Fonction qui verifie si les caractères d'une chaine sont des entiers
    if (equals(entree, "")) {
      return "-1";
    }
    for (int indice = 0; indice < length(entree); indice += 1) {
      if (!(charAt(entree, indice) <= '9' && charAt(entree, indice) >= '0')) {
        return "-1";
      }
    }
    return entree;
  }

  String NormaliserMot(String mot) { // Fonction qui met toutes les lettres d'un mot donné en paramètre en majuscule
    return toUpperCase(mot);
  }

  void sauvegarde(Joueur utilisateur) { // Fonction qui savegarde le nom et le score d'un joueur donnée en paramètre
    CSVFile file_save = loadCSV("../ressources/sauvegardes/sauvegarde.csv");
    String[][] saved_data = new String[rowCount(file_save)][2];
    saved_data[0][0] = "nom";
    saved_data[0][1] = "score";
    for (int i = 1; i < rowCount(file_save); i += 1) {
      saved_data[i][0] = getCell(file_save, i, 0);
      saved_data[i][1] = getCell(file_save, i, 1);
    }
    boolean flag = false;
    for (int i = 1; i < rowCount(file_save); i += 1) {
      if (
        equals(utilisateur.nom, getCell(file_save, i, 0)) &&
        utilisateur.score > stringToInt(getCell(file_save, i, 1))
      ) {
        saved_data[i][1] = "" + utilisateur.score;
        flag = true;
        saveCSV(saved_data, "../ressources/sauvegardes/sauvegarde.csv");
      }
    }
    if (!flag) {
      String[][] new_saved_data = new String[rowCount(file_save) + 1][2];
      for (int i = 0; i < rowCount(file_save); i += 1) {
        new_saved_data[i][0] = saved_data[i][0];
        new_saved_data[i][1] = saved_data[i][1];
      }
      new_saved_data[rowCount(file_save)][0] = utilisateur.nom;
      new_saved_data[rowCount(file_save)][1] = "" + utilisateur.score;
      saveCSV(new_saved_data, "../ressources/sauvegardes/sauvegarde.csv");
    }
  }

  // Programme principal

  void algorithm() {
    Categorie categorie = new Categorie();
    Niveau niveau = new Niveau();

    // Interface utilisateur
    int i = 0;
    int y = 0;
    double aleaLignes;
    double aleaColonnes;
    int probaSportif;
    int probaIndice;

    String image = "";
    initialisationCouleur();
    background(couleur_de_fond);
    text(couleur_texte);
    String jouer = "oui";
    while (jouer != "non") {
      clearScreen();
      afficherTxt(DEVINE_LE_SPORTIF_TXT);
      println("");
      print("Appuyer sur entrée pour commencer à jouer ");
      readString();
      int essai = 5;
      clearScreen();
      println("Binevenue dans le jeu Devine Le Sportif !");
      println("");
      print("Quel est ton prénom ? ");
      String prenomJoueur = readString();
      Joueur utilisateur = newJoueur(prenomJoueur);
      utilisateur.score = 0;
      while (length(prenomJoueur) <= 0) {
        println("");
        text(couleur_erreur);
        println("Erreur ! Ton prénom n'est pas valide, réessaie");
        text(couleur_texte);
        print("Quel est ton prénom ? ");
        prenomJoueur = readString();
        utilisateur = newJoueur(prenomJoueur);
      }
      clearScreen();
      afficherTxt(CATEGORIES_TXT);
      println(
        "D'accord " + prenomJoueur + ", dans ce jeu, il y a 3 catégories :"
      );
      println("");
      println("Catégorie 1 : Football");
      println("Catégorie 2 : Tennis");
      println("Catégorie 3 : Formule 1");
      println();
      println("Pour connaître le classement, choisissez le menu Classement");
      println();
      println("4- Classement");
      println();
      println(
        "Pour connaître le fonctionnement du jeu, choisissez le menu aide"
      );
      println();
      println("5- Aide");
      println("");
      print("Quel catégorie veux-tu choisir ? "); // Choix de la catégorie
      categorie = choixCategorie(stringToInt(verifInt(readString())));
      while (categorie.numCategorie < 1 || categorie.numCategorie > 5) {
        println("");
        text(couleur_erreur);
        println(
          "Erreur ! Cette caégorie n'éxiste pas, tu dois choisir une catégorie entre 1 et 5."
        );
        text(couleur_texte);
        print("Quelle catégorie veux-tu choisir ? ");
        categorie = choixCategorie(stringToInt(verifInt(readString())));
      }
      if (categorie.numCategorie == 1) { // Catégorie Football
        clearScreen();
        CSVFile foot = loadCSV(FOOT_CSV);
        println("Il y a plusieurs niveaux :");
        println("");
        println("Niveau 1 : 3 indices");
        println("Niveau 2 : 2 indices");
        println("Niveau 3 : 1 indice");
        println("");
        print("Quel niveau veux-tu choisir ? ");
        niveau = choixNiveau(stringToInt(verifInt(readString())));
        while (niveau.niveau < 1 || niveau.niveau > 3) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
          );
          text(couleur_texte);
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
        }
        println("");
        if (niveau.niveau == 2) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(FOOT_CSV);
            aleaColonnes = random() * nbCol(FOOT_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
              String nomFootballeur = getCell(foot, probaSportif, 1);
              String indice = getCell(foot, probaSportif, probaIndice);
              String indice2 = getCell(foot, probaSportif, probaIndice2);
              while (equals(indice, indice2) && probaIndice2 <= 2) {
                aleaColonnes2 = random() * nbCol(FOOT_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(foot, probaSportif, probaIndice2);
              }
              if (probaIndice == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le joueur à trouver : " +
                  indice +
                  " ; " +
                  indice2
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                if (nomFootballeur == getCell(foot, 3, 1)) {
                  text(black);
                  playSound(SIU);
                  text(couleur_texte);
                }
                utilisateur.score = utilisateur.score + 8;
                println();
                println(
                  "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                  nomFootballeur
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce joueur, il s'agissait de " +
                  nomFootballeur +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 3) {
          essai = 5;
          while (essai != 0) {
            aleaLignes = random() * nbLignes(FOOT_CSV);
            aleaColonnes = random() * nbCol(FOOT_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            if (probaSportif > 0 && probaIndice > 2) {
              String nomFootballeur = getCell(foot, probaSportif, 1);
              String indice = getCell(foot, probaSportif, probaIndice);
              if (probaIndice == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le joueur à trouver : " + indice
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                if (nomFootballeur == getCell(foot, 3, 1)) {
                  text(black);
                  playSound(SIU);
                  text(couleur_texte);
                }
                utilisateur.score = utilisateur.score + 10;
                println();
                println(
                  "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                  nomFootballeur
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce joueur, il s'agissait de " +
                  nomFootballeur +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 1) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(FOOT_CSV);
            aleaColonnes = random() * nbCol(FOOT_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            double aleaColonnes3 = random() * nbCol(FOOT_CSV); //3eme indice
            int probaIndice3 = (int) aleaColonnes3; //3eme indice
            if (
              probaSportif > 0 &&
              probaIndice > 2 &&
              probaIndice2 > 2 &&
              probaIndice3 > 2
            ) {
              String nomFootballeur = getCell(foot, probaSportif, 1);
              String indice = getCell(foot, probaSportif, probaIndice);
              String indice2 = getCell(foot, probaSportif, probaIndice2);
              String indice3 = getCell(foot, probaSportif, probaIndice3);
              while (
                equals(indice, indice2) &&
                equals(indice, indice3) &&
                equals(indice2, indice3) &&
                (probaIndice2 < 2 && probaIndice3 < 2)
              ) {
                aleaColonnes2 = random() * nbCol(FOOT_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(foot, probaSportif, probaIndice2);
                aleaColonnes3 = random() * nbCol(FOOT_CSV);
                probaIndice3 = (int) aleaColonnes3;
                indice3 = getCell(foot, probaSportif, probaIndice3);
              }
              if (probaIndice == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else if (probaIndice3 == 10) {
                println("Voici l'indice sur le joueur à trouver : ");
                afficherTxt(indice3);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le joueur à trouver : " +
                  indice +
                  " ; " +
                  indice2 +
                  " ; " +
                  indice3
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                if (nomFootballeur == getCell(foot, 3, 1)) {
                  text(black);
                  playSound(SIU);
                  text(couleur_texte);
                }
                utilisateur.score = utilisateur.score + 6;
                println();
                println(
                  "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                  nomFootballeur
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce joueur, il s'agissait de " +
                  nomFootballeur +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        }
      } else if (categorie.numCategorie == 2) { // Catégorie Tennis
        clearScreen();
        CSVFile tennis = loadCSV(TENNIS_CSV);
        println("Il y a plusieurs niveaux :");
        println("");
        println("Niveau 1 : 3 indices");
        println("Niveau 2 : 2 indices");
        println("Niveau 3 : 1 indice");
        println("");
        print("Quel niveau veux-tu choisir ? ");
        niveau = choixNiveau(stringToInt(verifInt(readString())));
        while (niveau.niveau < 1 || niveau.niveau > 3) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
          );
          text(couleur_texte);
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
        }
        println("");
        if (niveau.niveau == 2) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              String indice2 = getCell(tennis, probaSportif, probaIndice2);
              while (equals(indice, indice2) && probaIndice2 <= 2) {
                aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(tennis, probaSportif, probaIndice2);
              }
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " +
                  indice +
                  " ; " +
                  indice2
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 8;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 3) {
          essai = 5;
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            if (probaSportif > 0 && probaIndice > 2) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " + indice
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 10;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 1) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            double aleaColonnes3 = random() * nbCol(TENNIS_CSV); //3eme indice
            int probaIndice3 = (int) aleaColonnes3; //3eme indice
            if (
              probaSportif > 0 &&
              probaIndice > 2 &&
              probaIndice2 > 2 &&
              probaIndice3 > 2
            ) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              String indice2 = getCell(tennis, probaSportif, probaIndice2);
              String indice3 = getCell(tennis, probaSportif, probaIndice3);
              while (
                equals(indice, indice2) &&
                equals(indice, indice3) &&
                equals(indice2, indice3) &&
                (probaIndice2 < 2 && probaIndice3 < 2)
              ) {
                aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(tennis, probaSportif, probaIndice2);
                aleaColonnes3 = random() * nbCol(TENNIS_CSV);
                probaIndice3 = (int) aleaColonnes3;
                indice3 = getCell(tennis, probaSportif, probaIndice3);
              }
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else if (probaIndice3 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice3);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " +
                  indice +
                  " ; " +
                  indice2 +
                  " ; " +
                  indice3
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 6;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        }
      } else if (categorie.numCategorie == 3) { // Catégorie f1
        clearScreen();
        CSVFile f1 = loadCSV(F1_CSV);
        println("Il y a plusieurs niveaux :");
        println("");
        println("Niveau 1 : 3 indices");
        println("Niveau 2 : 2 indices");
        println("Niveau 3 : 1 indice");
        println("");
        print("Quel niveau veux-tu choisir ? ");
        niveau = choixNiveau(stringToInt(verifInt(readString())));
        while (niveau.niveau < 1 || niveau.niveau > 3) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
          );
          text(couleur_texte);
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
        }
        println("");
        if (niveau.niveau == 2) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(F1_CSV);
            aleaColonnes = random() * nbCol(F1_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
              String nomPilote = getCell(f1, probaSportif, 1);
              String indice = getCell(f1, probaSportif, probaIndice);
              String indice2 = getCell(f1, probaSportif, probaIndice2);
              while (equals(indice, indice2) && probaIndice2 <= 2) {
                aleaColonnes2 = random() * nbCol(F1_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(f1, probaSportif, probaIndice2);
              }
              if (probaIndice == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le Pilote à trouver : " +
                  indice +
                  " ; " +
                  indice2
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomPilote)) {
                utilisateur.score = utilisateur.score + 8;
                println();
                println(
                  "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                  nomPilote
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce Pilote, il s'agissait de " +
                  nomPilote +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 3) {
          essai = 5;
          while (essai != 0) {
            aleaLignes = random() * nbLignes(F1_CSV);
            aleaColonnes = random() * nbCol(F1_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            if (probaSportif > 0 && probaIndice > 2) {
              String nomPilote = getCell(f1, probaSportif, 1);
              String indice = getCell(f1, probaSportif, probaIndice);
              if (probaIndice == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le Pilote à trouver : " + indice
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomPilote)) {
                utilisateur.score = utilisateur.score + 10;
                println();
                println(
                  "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                  nomPilote
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce Pilote, il s'agissait de " +
                  nomPilote +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 1) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(F1_CSV);
            aleaColonnes = random() * nbCol(F1_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            double aleaColonnes3 = random() * nbCol(F1_CSV); //3eme indice
            int probaIndice3 = (int) aleaColonnes3; //3eme indice
            if (
              probaSportif > 0 &&
              probaIndice > 2 &&
              probaIndice2 > 2 &&
              probaIndice3 > 2
            ) {
              String nomPilote = getCell(f1, probaSportif, 1);
              String indice = getCell(f1, probaSportif, probaIndice);
              String indice2 = getCell(f1, probaSportif, probaIndice2);
              String indice3 = getCell(f1, probaSportif, probaIndice3);
              while (
                equals(indice, indice2) &&
                equals(indice, indice3) &&
                equals(indice2, indice3) &&
                (probaIndice2 < 2 && probaIndice3 < 2)
              ) {
                aleaColonnes2 = random() * nbCol(F1_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(f1, probaSportif, probaIndice2);
                aleaColonnes3 = random() * nbCol(F1_CSV);
                probaIndice3 = (int) aleaColonnes3;
                indice3 = getCell(f1, probaSportif, probaIndice3);
              }
              if (probaIndice == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else if (probaIndice3 == 11) {
                println("Voici l'indice sur le Pilote à trouver : ");
                afficherTxt(indice3);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le Pilote à trouver : " +
                  indice +
                  " ; " +
                  indice2 +
                  " ; " +
                  indice3
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomPilote)) {
                utilisateur.score = utilisateur.score + 6;
                println();
                println(
                  "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                  nomPilote
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce Pilote, il s'agissait de " +
                  nomPilote +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        }
      } else if (categorie.numCategorie == 4) {
        clearScreen();
        afficherTxt(CLASSEMENT_TXT);
        CSVFile Class = loadCSV("../ressources/sauvegardes/sauvegarde.csv");
        String[][] save = new String[rowCount(Class)][2];
        for (int x = 0; x < rowCount(Class); x += 1) {
          save[x][0] = getCell(Class, x, 0);
          save[x][1] = getCell(Class, x, 1);
        }
        for (int x = 1; x < rowCount(Class) - 1; x += 1) {
          if (
            stringToInt(getCell(Class, x, 1)) <
            stringToInt(getCell(Class, x + 1, 1))
          ) {
            String nomActu = save[x][0];
            String scoreActu = save[x][1];
            save[x][0] = save[x + 1][0];
            save[x][1] = save[x + 1][1];
            save[x + 1][0] = nomActu;
            save[x + 1][1] = scoreActu;
            saveCSV(save, "../ressources/sauvegardes/sauvegarde.csv");
          }
        }
        println("Le classement actuel pour ce jeu est :");
        println();
        for (int x = 1; x < 11; x += 1) {
          print(x + "- ");
          print(getCell(Class, x, 0) + " " + getCell(Class, x, 1));
          println();
        }
        println();
        println("Choisissez une catégorie :");
        println("");
        println("Catégorie 1 : Football");
        println("Catégorie 2 : Tennis");
        println("Catégorie 3 : Formule 1");
        println("");
        println(
          "Pour connaître le fonctionnement du jeu, choisissez le menu aide"
        );
        println();
        println("4- Aide");
        println("");
        print("Quel catégorie veux-tu choisir ? "); // Choix de la catégorie
        categorie = choixCategorie(stringToInt(verifInt(readString())));
        while (categorie.numCategorie < 1 || categorie.numCategorie > 4) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Cette caégorie n'éxiste pas, tu dois choisir une catégorie entre 1 et 4."
          );
          text(couleur_texte);
          print("Quelle catégorie veux-tu choisir ? ");
          println();
          categorie = choixCategorie(stringToInt(verifInt(readString())));
        }
        if (categorie.numCategorie == 1) { // Catégorie Football
          clearScreen();
          CSVFile foot = loadCSV(FOOT_CSV);
          println("Il y a plusieurs niveaux :");
          println("");
          println("Niveau 1 : 3 indices");
          println("Niveau 2 : 2 indices");
          println("Niveau 3 : 1 indice");
          println("");
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
          while (niveau.niveau < 1 || niveau.niveau > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
            );
            text(couleur_texte);
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
          }
          println("");
          if (niveau.niveau == 2) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                String indice2 = getCell(foot, probaSportif, probaIndice2);
                while (equals(indice, indice2) && probaIndice2 <= 2) {
                  aleaColonnes2 = random() * nbCol(FOOT_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(foot, probaSportif, probaIndice2);
                }
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " +
                    indice +
                    " ; " +
                    indice2
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 8;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 3) {
            essai = 5;
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              if (probaSportif > 0 && probaIndice > 2) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " + indice
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 10;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 1) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              double aleaColonnes3 = random() * nbCol(FOOT_CSV); //3eme indice
              int probaIndice3 = (int) aleaColonnes3; //3eme indice
              if (
                probaSportif > 0 &&
                probaIndice > 2 &&
                probaIndice2 > 2 &&
                probaIndice3 > 2
              ) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                String indice2 = getCell(foot, probaSportif, probaIndice2);
                String indice3 = getCell(foot, probaSportif, probaIndice3);
                while (
                  equals(indice, indice2) &&
                  equals(indice, indice3) &&
                  equals(indice2, indice3) &&
                  (probaIndice2 < 2 && probaIndice3 < 2)
                ) {
                  aleaColonnes2 = random() * nbCol(FOOT_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(foot, probaSportif, probaIndice2);
                  aleaColonnes3 = random() * nbCol(FOOT_CSV);
                  probaIndice3 = (int) aleaColonnes3;
                  indice3 = getCell(foot, probaSportif, probaIndice3);
                }
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice3 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice3);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " +
                    indice +
                    " ; " +
                    indice2 +
                    " ; " +
                    indice3
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 6;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          }
        } else if (categorie.numCategorie == 2) { // Catégorie Tennis
        clearScreen();
        CSVFile tennis = loadCSV(TENNIS_CSV);
        println("Il y a plusieurs niveaux :");
        println("");
        println("Niveau 1 : 3 indices");
        println("Niveau 2 : 2 indices");
        println("Niveau 3 : 1 indice");
        println("");
        print("Quel niveau veux-tu choisir ? ");
        niveau = choixNiveau(stringToInt(verifInt(readString())));
        while (niveau.niveau < 1 || niveau.niveau > 3) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
          );
          text(couleur_texte);
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
        }
        println("");
        if (niveau.niveau == 2) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              String indice2 = getCell(tennis, probaSportif, probaIndice2);
              while (equals(indice, indice2) && probaIndice2 <= 2) {
                aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(tennis, probaSportif, probaIndice2);
              }
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " +
                  indice +
                  " ; " +
                  indice2
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 8;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 3) {
          essai = 5;
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            if (probaSportif > 0 && probaIndice > 2) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " + indice
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 10;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        } else if (niveau.niveau == 1) {
          while (essai != 0) {
            aleaLignes = random() * nbLignes(TENNIS_CSV);
            aleaColonnes = random() * nbCol(TENNIS_CSV);
            probaSportif = (int) aleaLignes;
            probaIndice = (int) aleaColonnes;
            double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
            int probaIndice2 = (int) aleaColonnes2; //2eme indice
            double aleaColonnes3 = random() * nbCol(TENNIS_CSV); //3eme indice
            int probaIndice3 = (int) aleaColonnes3; //3eme indice
            if (
              probaSportif > 0 &&
              probaIndice > 2 &&
              probaIndice2 > 2 &&
              probaIndice3 > 2
            ) {
              String nomTennisman = getCell(tennis, probaSportif, 1);
              String indice = getCell(tennis, probaSportif, probaIndice);
              String indice2 = getCell(tennis, probaSportif, probaIndice2);
              String indice3 = getCell(tennis, probaSportif, probaIndice3);
              while (
                equals(indice, indice2) &&
                equals(indice, indice3) &&
                equals(indice2, indice3) &&
                (probaIndice2 < 2 && probaIndice3 < 2)
              ) {
                aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                probaIndice2 = (int) aleaColonnes2;
                indice2 = getCell(tennis, probaSportif, probaIndice2);
                aleaColonnes3 = random() * nbCol(TENNIS_CSV);
                probaIndice3 = (int) aleaColonnes3;
                indice3 = getCell(tennis, probaSportif, probaIndice3);
              }
              if (probaIndice == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice);
                println();
                print("Votre réponse : ");
              } else if (probaIndice2 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice2);
                println();
                print("Votre réponse : ");
              } else if (probaIndice3 == 8) {
                println("Voici l'indice sur le tennisman à trouver : ");
                afficherTxt(indice3);
                println();
                print("Votre réponse : ");
              } else {
                println(
                  "Voici les indices sur le tennisman à trouver : " +
                  indice +
                  " ; " +
                  indice2 +
                  " ; " +
                  indice3
                );
                println();
                print("Votre réponse : ");
              }
              reponseUtilisateur = NormaliserMot(readString());
              if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                utilisateur.score = utilisateur.score + 6;
                println();
                println(
                  "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                  nomTennisman
                );
                println();
                println("Votre score : " + utilisateur.score + " pts");
                println();
              } else {
                essai = essai - 1;
                println();
                text(couleur_erreur);
                println(
                  "Ce n'est pas ce tennisman, il s'agissait de " +
                  nomTennisman +
                  ", réessaie"
                );
                println();
                text(couleur_texte);
                println("Nombre d'essai(s) restant(s) : " + essai);
                println();
              }
            }
          }
          println(
            "Vous avez perdu ! Votre score est de " + utilisateur.score + " pts"
          );
          println();
          print("Souhaitez-vous sauvegarder ? (oui/non) ");
          String sauvegardeEffectuée = "oui";
          String sauvegardeUtilisateur = readString();
          while (
            !equals(sauvegardeUtilisateur, "oui") &&
            !equals(sauvegardeUtilisateur, "non")
          ) {
            text(couleur_erreur);
            println();
            println("Erreur, ce n'est pas possible !");
            text(couleur_texte);
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            sauvegardeUtilisateur = readString();
          }
          if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
            sauvegarde(utilisateur);
          }
        }
      } else if (categorie.numCategorie == 3) { // Catégorie f1
          clearScreen();
          CSVFile f1 = loadCSV(F1_CSV);
          println("Il y a plusieurs niveaux :");
          println("");
          println("Niveau 1 : 3 indices");
          println("Niveau 2 : 2 indices");
          println("Niveau 3 : 1 indice");
          println("");
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
          while (niveau.niveau < 1 || niveau.niveau > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
            );
            text(couleur_texte);
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
          }
          println("");
          if (niveau.niveau == 2) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                String indice2 = getCell(f1, probaSportif, probaIndice2);
                while (equals(indice, indice2) && probaIndice2 <= 2) {
                  aleaColonnes2 = random() * nbCol(F1_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(f1, probaSportif, probaIndice2);
                }
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " +
                    indice +
                    " ; " +
                    indice2
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 8;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 3) {
            essai = 5;
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              if (probaSportif > 0 && probaIndice > 2) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " + indice
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 10;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 1) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              double aleaColonnes3 = random() * nbCol(F1_CSV); //3eme indice
              int probaIndice3 = (int) aleaColonnes3; //3eme indice
              if (
                probaSportif > 0 &&
                probaIndice > 2 &&
                probaIndice2 > 2 &&
                probaIndice3 > 2
              ) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                String indice2 = getCell(f1, probaSportif, probaIndice2);
                String indice3 = getCell(f1, probaSportif, probaIndice3);
                while (
                  equals(indice, indice2) &&
                  equals(indice, indice3) &&
                  equals(indice2, indice3) &&
                  (probaIndice2 < 2 && probaIndice3 < 2)
                ) {
                  aleaColonnes2 = random() * nbCol(F1_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(f1, probaSportif, probaIndice2);
                  aleaColonnes3 = random() * nbCol(F1_CSV);
                  probaIndice3 = (int) aleaColonnes3;
                  indice3 = getCell(f1, probaSportif, probaIndice3);
                }
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice3 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice3);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " +
                    indice +
                    " ; " +
                    indice2 +
                    " ; " +
                    indice3
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 6;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          }
        } else if (categorie.numCategorie == 4) {
          clearScreen();
          afficherTxt(FONCTIONNEMENT_DU_JEU_TXT);
          println();
          println(
            "Le but est de tout d'abord choisir un catégorif de sportif,"
          );
          println(
            "Ensuite, il faut deviner le sportif selon un indice qui vous est donné,"
          );
          println(
            "Pour répondre, il suffira d'entrer une réponse avec les lettres de votre clavier."
          );
          println(
            "N'oubliez-pas ! Commencez votre réponse par une lettre majuscule."
          );
          println(
            "Si votre réponse est bonne, 10 pts vous sont ajouté à votre score,"
          );
          println("Si votre réponse est mauvaise, vous perdez 1 essai.");
          println(
            "Lorsque votre nombre d'essai tombe à 0, le jeu est terminé,"
          );
          println(
            "Lorsque le jeu est terminé, il vous sera demandé de sauvegarder ou non votre nom et score."
          );
          println(
            "Vous pourrez ensuite consulter votre résultat par rapport à d'autres en sélectionnant la catégorie 'Classement'."
          );
          println("Bonne chance à vous !");
          println();
          println("Choisissez une catégorie :");
          println("");
          println("Catégorie 1 : Football");
          println("Catégorie 2 : Tennis");
          println("Catégorie 3 : Formule 1");
          println("");
          print("Quel catégorie veux-tu choisir ? "); // Choix de la catégorie
          categorie = choixCategorie(stringToInt(verifInt(readString())));
          while (categorie.numCategorie < 1 || categorie.numCategorie > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Cette caégorie n'éxiste pas, tu dois choisir une catégorie entre 1 et 3."
            );
            text(couleur_texte);
            print("Quelle catégorie veux-tu choisir ? ");
            println();
            categorie = choixCategorie(stringToInt(verifInt(readString())));
          }
          if (categorie.numCategorie == 1) { // Catégorie Football
            clearScreen();
            CSVFile foot = loadCSV(FOOT_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  String indice2 = getCell(foot, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(FOOT_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(foot, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(FOOT_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  String indice2 = getCell(foot, probaSportif, probaIndice2);
                  String indice3 = getCell(foot, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(FOOT_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(foot, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(FOOT_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(foot, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          } else if (categorie.numCategorie == 2) { // Catégorie Tennis
            clearScreen();
            CSVFile tennis = loadCSV(TENNIS_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  String indice2 = getCell(tennis, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(tennis, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(TENNIS_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  String indice2 = getCell(tennis, probaSportif, probaIndice2);
                  String indice3 = getCell(tennis, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(tennis, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(TENNIS_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(tennis, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          } else if (categorie.numCategorie == 3) { // Catégorie f1
            clearScreen();
            CSVFile f1 = loadCSV(F1_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  String indice2 = getCell(f1, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(F1_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(f1, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(F1_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  String indice2 = getCell(f1, probaSportif, probaIndice2);
                  String indice3 = getCell(f1, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(F1_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(f1, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(F1_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(f1, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          }
        }
      } else if (categorie.numCategorie == 5) { // Menu Aide
        clearScreen();
        afficherTxt(FONCTIONNEMENT_DU_JEU_TXT);
        println("");
        println("Le but est de tout d'abord choisir un catégorie de sportif,");
        println(
          "Ensuite, il faut deviner le sportif selon le ou les indice qui vous sont donnés."
        );
        println(
          "Pour répondre, il suffira d'entrer une réponse avec les lettres de votre clavier."
        );
        println("Si votre réponse est bonne, vous gagnez 10 points.");
        println("Si votre réponse est mauvaise, vous perdez 1 essai.");
        println("Lorsque votre nombre d'essai tombe à 0, le jeu est terminé,");
        println(
          "Lorsque le jeu est terminé, il vous sera demandé de sauvegarder ou non votre nom et score."
        );
        println(
          "Vous pourrez ensuite consulter votre résultat par rapport à d'autres en sélectionnant la catégorie 'Classement'."
        );
        println("Bonne chance à vous !");
        println();
        println("Choisissez une catégorie :");
        println("");
        println("Catégorie 1 : Football");
        println("Catégorie 2 : Tennis");
        println("Catégorie 3 : Formule 1");
        println("");
        println("Pour connaître le classement, choisissez le menu Classement");
        println();
        println("4- Classement");
        println();
        print("Quel catégorie veux-tu choisir ? "); // Choix de la catégorie
        categorie = choixCategorie(stringToInt(verifInt(readString())));
        while (categorie.numCategorie < 1 || categorie.numCategorie > 4) {
          println("");
          text(couleur_erreur);
          println(
            "Erreur ! Cette caégorie n'éxiste pas, tu dois choisir une catégorie entre 1 et 4."
          );
          text(couleur_texte);
          print("Quelle catégorie veux-tu choisir ? ");
          println();
          categorie = choixCategorie(stringToInt(verifInt(readString())));
        }
        if (categorie.numCategorie == 1) { // Catégorie Football
          clearScreen();
          CSVFile foot = loadCSV(FOOT_CSV);
          println("Il y a plusieurs niveaux :");
          println("");
          println("Niveau 1 : 3 indices");
          println("Niveau 2 : 2 indices");
          println("Niveau 3 : 1 indice");
          println("");
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
          while (niveau.niveau < 1 || niveau.niveau > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
            );
            text(couleur_texte);
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
          }
          println("");
          if (niveau.niveau == 2) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                String indice2 = getCell(foot, probaSportif, probaIndice2);
                while (equals(indice, indice2) && probaIndice2 <= 2) {
                  aleaColonnes2 = random() * nbCol(FOOT_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(foot, probaSportif, probaIndice2);
                }
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " +
                    indice +
                    " ; " +
                    indice2
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 8;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 3) {
            essai = 5;
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              if (probaSportif > 0 && probaIndice > 2) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " + indice
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 10;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 1) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(FOOT_CSV);
              aleaColonnes = random() * nbCol(FOOT_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              double aleaColonnes3 = random() * nbCol(FOOT_CSV); //3eme indice
              int probaIndice3 = (int) aleaColonnes3; //3eme indice
              if (
                probaSportif > 0 &&
                probaIndice > 2 &&
                probaIndice2 > 2 &&
                probaIndice3 > 2
              ) {
                String nomFootballeur = getCell(foot, probaSportif, 1);
                String indice = getCell(foot, probaSportif, probaIndice);
                String indice2 = getCell(foot, probaSportif, probaIndice2);
                String indice3 = getCell(foot, probaSportif, probaIndice3);
                while (
                  equals(indice, indice2) &&
                  equals(indice, indice3) &&
                  equals(indice2, indice3) &&
                  (probaIndice2 < 2 && probaIndice3 < 2)
                ) {
                  aleaColonnes2 = random() * nbCol(FOOT_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(foot, probaSportif, probaIndice2);
                  aleaColonnes3 = random() * nbCol(FOOT_CSV);
                  probaIndice3 = (int) aleaColonnes3;
                  indice3 = getCell(foot, probaSportif, probaIndice3);
                }
                if (probaIndice == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice3 == 10) {
                  println("Voici l'indice sur le joueur à trouver : ");
                  afficherTxt(indice3);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le joueur à trouver : " +
                    indice +
                    " ; " +
                    indice2 +
                    " ; " +
                    indice3
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                  if (nomFootballeur == getCell(foot, 3, 1)) {
                    text(black);
                    playSound(SIU);
                    text(couleur_texte);
                  }
                  utilisateur.score = utilisateur.score + 6;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                    nomFootballeur
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce joueur, il s'agissait de " +
                    nomFootballeur +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          }
        } else if (categorie.numCategorie == 2) { // Catégorie Tennis
          clearScreen();
          CSVFile tennis = loadCSV(TENNIS_CSV);
          println("Il y a plusieurs niveaux :");
          println("");
          println("Niveau 1 : 3 indices");
          println("Niveau 2 : 2 indices");
          println("Niveau 3 : 1 indice");
          println("");
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
          while (niveau.niveau < 1 || niveau.niveau > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
            );
            text(couleur_texte);
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
          }
          println("");
          if (niveau.niveau == 2) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(TENNIS_CSV);
              aleaColonnes = random() * nbCol(TENNIS_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                String nomTennisman = getCell(tennis, probaSportif, 1);
                String indice = getCell(tennis, probaSportif, probaIndice);
                String indice2 = getCell(tennis, probaSportif, probaIndice2);
                while (equals(indice, indice2) && probaIndice2 <= 2) {
                  aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(tennis, probaSportif, probaIndice2);
                }
                if (probaIndice == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le tennisman à trouver : " +
                    indice +
                    " ; " +
                    indice2
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                  utilisateur.score = utilisateur.score + 8;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                    nomTennisman
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce tennisman, il s'agissait de " +
                    nomTennisman +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 3) {
            essai = 5;
            while (essai != 0) {
              aleaLignes = random() * nbLignes(TENNIS_CSV);
              aleaColonnes = random() * nbCol(TENNIS_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              if (probaSportif > 0 && probaIndice > 2) {
                String nomTennisman = getCell(tennis, probaSportif, 1);
                String indice = getCell(tennis, probaSportif, probaIndice);
                if (probaIndice == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le tennisman à trouver : " + indice
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                  utilisateur.score = utilisateur.score + 10;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                    nomTennisman
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce tennisman, il s'agissait de " +
                    nomTennisman +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 1) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(TENNIS_CSV);
              aleaColonnes = random() * nbCol(TENNIS_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              double aleaColonnes3 = random() * nbCol(TENNIS_CSV); //3eme indice
              int probaIndice3 = (int) aleaColonnes3; //3eme indice
              if (
                probaSportif > 0 &&
                probaIndice > 2 &&
                probaIndice2 > 2 &&
                probaIndice3 > 2
              ) {
                String nomTennisman = getCell(tennis, probaSportif, 1);
                String indice = getCell(tennis, probaSportif, probaIndice);
                String indice2 = getCell(tennis, probaSportif, probaIndice2);
                String indice3 = getCell(tennis, probaSportif, probaIndice3);
                while (
                  equals(indice, indice2) &&
                  equals(indice, indice3) &&
                  equals(indice2, indice3) &&
                  (probaIndice2 < 2 && probaIndice3 < 2)
                ) {
                  aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(tennis, probaSportif, probaIndice2);
                  aleaColonnes3 = random() * nbCol(TENNIS_CSV);
                  probaIndice3 = (int) aleaColonnes3;
                  indice3 = getCell(tennis, probaSportif, probaIndice3);
                }
                if (probaIndice == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice3 == 8) {
                  println("Voici l'indice sur le tennisman à trouver : ");
                  afficherTxt(indice3);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le tennisman à trouver : " +
                    indice +
                    " ; " +
                    indice2 +
                    " ; " +
                    indice3
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                  utilisateur.score = utilisateur.score + 6;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                    nomTennisman
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce tennisman, il s'agissait de " +
                    nomTennisman +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          }
        } else if (categorie.numCategorie == 3) { // Catégorie f1
          clearScreen();
          CSVFile f1 = loadCSV(F1_CSV);
          println("Il y a plusieurs niveaux :");
          println("");
          println("Niveau 1 : 3 indices");
          println("Niveau 2 : 2 indices");
          println("Niveau 3 : 1 indice");
          println("");
          print("Quel niveau veux-tu choisir ? ");
          niveau = choixNiveau(stringToInt(verifInt(readString())));
          while (niveau.niveau < 1 || niveau.niveau > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
            );
            text(couleur_texte);
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
          }
          println("");
          if (niveau.niveau == 2) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                String indice2 = getCell(f1, probaSportif, probaIndice2);
                while (equals(indice, indice2) && probaIndice2 <= 2) {
                  aleaColonnes2 = random() * nbCol(F1_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(f1, probaSportif, probaIndice2);
                }
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " +
                    indice +
                    " ; " +
                    indice2
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 8;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 3) {
            essai = 5;
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              if (probaSportif > 0 && probaIndice > 2) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " + indice
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 10;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          } else if (niveau.niveau == 1) {
            while (essai != 0) {
              aleaLignes = random() * nbLignes(F1_CSV);
              aleaColonnes = random() * nbCol(F1_CSV);
              probaSportif = (int) aleaLignes;
              probaIndice = (int) aleaColonnes;
              double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
              int probaIndice2 = (int) aleaColonnes2; //2eme indice
              double aleaColonnes3 = random() * nbCol(F1_CSV); //3eme indice
              int probaIndice3 = (int) aleaColonnes3; //3eme indice
              if (
                probaSportif > 0 &&
                probaIndice > 2 &&
                probaIndice2 > 2 &&
                probaIndice3 > 2
              ) {
                String nomPilote = getCell(f1, probaSportif, 1);
                String indice = getCell(f1, probaSportif, probaIndice);
                String indice2 = getCell(f1, probaSportif, probaIndice2);
                String indice3 = getCell(f1, probaSportif, probaIndice3);
                while (
                  equals(indice, indice2) &&
                  equals(indice, indice3) &&
                  equals(indice2, indice3) &&
                  (probaIndice2 < 2 && probaIndice3 < 2)
                ) {
                  aleaColonnes2 = random() * nbCol(F1_CSV);
                  probaIndice2 = (int) aleaColonnes2;
                  indice2 = getCell(f1, probaSportif, probaIndice2);
                  aleaColonnes3 = random() * nbCol(F1_CSV);
                  probaIndice3 = (int) aleaColonnes3;
                  indice3 = getCell(f1, probaSportif, probaIndice3);
                }
                if (probaIndice == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice2 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice2);
                  println();
                  print("Votre réponse : ");
                } else if (probaIndice3 == 11) {
                  println("Voici l'indice sur le Pilote à trouver : ");
                  afficherTxt(indice3);
                  println();
                  print("Votre réponse : ");
                } else {
                  println(
                    "Voici les indices sur le Pilote à trouver : " +
                    indice +
                    " ; " +
                    indice2 +
                    " ; " +
                    indice3
                  );
                  println();
                  print("Votre réponse : ");
                }
                reponseUtilisateur = NormaliserMot(readString());
                if (MotVerifie(reponseUtilisateur, nomPilote)) {
                  utilisateur.score = utilisateur.score + 6;
                  println();
                  println(
                    "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                    nomPilote
                  );
                  println();
                  println("Votre score : " + utilisateur.score + " pts");
                  println();
                } else {
                  essai = essai - 1;
                  println();
                  text(couleur_erreur);
                  println(
                    "Ce n'est pas ce Pilote, il s'agissait de " +
                    nomPilote +
                    ", réessaie"
                  );
                  println();
                  text(couleur_texte);
                  println("Nombre d'essai(s) restant(s) : " + essai);
                  println();
                }
              }
            }
            println(
              "Vous avez perdu ! Votre score est de " +
              utilisateur.score +
              " pts"
            );
            println();
            print("Souhaitez-vous sauvegarder ? (oui/non) ");
            String sauvegardeEffectuée = "oui";
            String sauvegardeUtilisateur = readString();
            while (
              !equals(sauvegardeUtilisateur, "oui") &&
              !equals(sauvegardeUtilisateur, "non")
            ) {
              text(couleur_erreur);
              println();
              println("Erreur, ce n'est pas possible !");
              text(couleur_texte);
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              sauvegardeUtilisateur = readString();
            }
            if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
              sauvegarde(utilisateur);
            }
          }
        } else if (categorie.numCategorie == 4) {
          clearScreen();
          CSVFile Class = loadCSV("../ressources/sauvegardes/sauvegarde.csv");
          afficherTxt(CLASSEMENT_TXT);
          String[][] save = new String[rowCount(Class)][2];
          for (int x = 0; x < rowCount(Class); x += 1) {
            save[x][0] = getCell(Class, x, 0);
            save[x][1] = getCell(Class, x, 1);
          }
          for (int x = 1; x < rowCount(Class) - 1; x += 1) {
            if (
              stringToInt(getCell(Class, x, 1)) <
              stringToInt(getCell(Class, x + 1, 1))
            ) {
              String nomActu = save[x][0];
              String scoreActu = save[x][1];
              save[x][0] = save[x + 1][0];
              save[x][1] = save[x + 1][1];
              save[x + 1][0] = nomActu;
              save[x + 1][1] = scoreActu;
              saveCSV(save, "../ressources/sauvegardes/sauvegarde.csv");
            }
          }
          println("Le classement actuel pour ce jeu est :");
          println();
          for (int x = 1; x < 11; x += 1) {
            print(x + "- ");
            print(getCell(Class, x, 0) + " " + getCell(Class, x, 1));
            println();
          }
          println();
          println("Choisissez une catégorie :");
          println("");
          println("Catégorie 1 : Football");
          println("Catégorie 2 : Tennis");
          println("Catégorie 3 : Formule 1");
          println("");
          print("Quel catégorie veux-tu choisir ? "); // Choix de la catégorie
          categorie = choixCategorie(stringToInt(verifInt(readString())));
          while (categorie.numCategorie < 1 || categorie.numCategorie > 3) {
            println("");
            text(couleur_erreur);
            println(
              "Erreur ! Cette caégorie n'éxiste pas, tu dois choisir une catégorie entre 1 et 3."
            );
            text(couleur_texte);
            print("Quelle catégorie veux-tu choisir ? ");
            println();
            categorie = choixCategorie(stringToInt(verifInt(readString())));
          }
          if (categorie.numCategorie == 1) { // Catégorie Football
            clearScreen();
            CSVFile foot = loadCSV(FOOT_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  String indice2 = getCell(foot, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(FOOT_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(foot, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(FOOT_CSV);
                aleaColonnes = random() * nbCol(FOOT_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(FOOT_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(FOOT_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomFootballeur = getCell(foot, probaSportif, 1);
                  String indice = getCell(foot, probaSportif, probaIndice);
                  String indice2 = getCell(foot, probaSportif, probaIndice2);
                  String indice3 = getCell(foot, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(FOOT_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(foot, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(FOOT_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(foot, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 10) {
                    println("Voici l'indice sur le joueur à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le joueur à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomFootballeur)) {
                    if (nomFootballeur == getCell(foot, 3, 1)) {
                      text(black);
                      playSound(SIU);
                      text(couleur_texte);
                    }
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Footballeur, il s'agissait bien de " +
                      nomFootballeur
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce joueur, il s'agissait de " +
                      nomFootballeur +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          } else if (categorie.numCategorie == 2) { // Catégorie Tennis
            clearScreen();
            CSVFile tennis = loadCSV(TENNIS_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  String indice2 = getCell(tennis, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(tennis, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(TENNIS_CSV);
                aleaColonnes = random() * nbCol(TENNIS_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(TENNIS_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(TENNIS_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomTennisman = getCell(tennis, probaSportif, 1);
                  String indice = getCell(tennis, probaSportif, probaIndice);
                  String indice2 = getCell(tennis, probaSportif, probaIndice2);
                  String indice3 = getCell(tennis, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(TENNIS_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(tennis, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(TENNIS_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(tennis, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 8) {
                    println("Voici l'indice sur le tennisman à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le tennisman à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomTennisman)) {
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Tennisman, il s'agissait bien de " +
                      nomTennisman
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce tennisman, il s'agissait de " +
                      nomTennisman +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          } else if (categorie.numCategorie == 3) { // Catégorie f1
            clearScreen();
            CSVFile f1 = loadCSV(F1_CSV);
            println("Il y a plusieurs niveaux :");
            println("");
            println("Niveau 1 : 3 indices");
            println("Niveau 2 : 2 indices");
            println("Niveau 3 : 1 indice");
            println("");
            print("Quel niveau veux-tu choisir ? ");
            niveau = choixNiveau(stringToInt(verifInt(readString())));
            while (niveau.niveau < 1 || niveau.niveau > 3) {
              println("");
              text(couleur_erreur);
              println(
                "Erreur ! Ce niveau n'éxiste pas, tu dois choisir un entre 1 et 3."
              );
              text(couleur_texte);
              print("Quel niveau veux-tu choisir ? ");
              niveau = choixNiveau(stringToInt(verifInt(readString())));
            }
            println("");
            if (niveau.niveau == 2) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                if (probaSportif > 0 && probaIndice > 2 && probaIndice2 > 2) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  String indice2 = getCell(f1, probaSportif, probaIndice2);
                  while (equals(indice, indice2) && probaIndice2 <= 2) {
                    aleaColonnes2 = random() * nbCol(F1_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(f1, probaSportif, probaIndice2);
                  }
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " +
                      indice +
                      " ; " +
                      indice2
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 8;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 3) {
              essai = 5;
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                if (probaSportif > 0 && probaIndice > 2) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " + indice
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 10;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            } else if (niveau.niveau == 1) {
              while (essai != 0) {
                aleaLignes = random() * nbLignes(F1_CSV);
                aleaColonnes = random() * nbCol(F1_CSV);
                probaSportif = (int) aleaLignes;
                probaIndice = (int) aleaColonnes;
                double aleaColonnes2 = random() * nbCol(F1_CSV); //2eme indice
                int probaIndice2 = (int) aleaColonnes2; //2eme indice
                double aleaColonnes3 = random() * nbCol(F1_CSV); //3eme indice
                int probaIndice3 = (int) aleaColonnes3; //3eme indice
                if (
                  probaSportif > 0 &&
                  probaIndice > 2 &&
                  probaIndice2 > 2 &&
                  probaIndice3 > 2
                ) {
                  String nomPilote = getCell(f1, probaSportif, 1);
                  String indice = getCell(f1, probaSportif, probaIndice);
                  String indice2 = getCell(f1, probaSportif, probaIndice2);
                  String indice3 = getCell(f1, probaSportif, probaIndice3);
                  while (
                    (
                      equals(indice, indice2) ||
                      equals(indice, indice3) ||
                      equals(indice2, indice3)
                    ) &&
                    (probaIndice2 < 2 && probaIndice3 < 2)
                  ) {
                    aleaColonnes2 = random() * nbCol(F1_CSV);
                    probaIndice2 = (int) aleaColonnes2;
                    indice2 = getCell(f1, probaSportif, probaIndice2);
                    aleaColonnes3 = random() * nbCol(F1_CSV);
                    probaIndice3 = (int) aleaColonnes3;
                    indice3 = getCell(f1, probaSportif, probaIndice3);
                  }
                  if (probaIndice == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice2 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice2);
                    println();
                    print("Votre réponse : ");
                  } else if (probaIndice3 == 11) {
                    println("Voici l'indice sur le Pilote à trouver : ");
                    afficherTxt(indice3);
                    println();
                    print("Votre réponse : ");
                  } else {
                    println(
                      "Voici les indices sur le Pilote à trouver : " +
                      indice +
                      " ; " +
                      indice2 +
                      " ; " +
                      indice3
                    );
                    println();
                    print("Votre réponse : ");
                  }
                  reponseUtilisateur = NormaliserMot(readString());
                  if (MotVerifie(reponseUtilisateur, nomPilote)) {
                    utilisateur.score = utilisateur.score + 6;
                    println();
                    println(
                      "Bravo, vous avez trouvé le Pilote, il s'agissait bien de " +
                      nomPilote
                    );
                    println();
                    println("Votre score : " + utilisateur.score + " pts");
                    println();
                  } else {
                    essai = essai - 1;
                    println();
                    text(couleur_erreur);
                    println(
                      "Ce n'est pas ce Pilote, il s'agissait de " +
                      nomPilote +
                      ", réessaie"
                    );
                    println();
                    text(couleur_texte);
                    println("Nombre d'essai(s) restant(s) : " + essai);
                    println();
                  }
                }
              }
              println(
                "Vous avez perdu ! Votre score est de " +
                utilisateur.score +
                " pts"
              );
              println();
              print("Souhaitez-vous sauvegarder ? (oui/non) ");
              String sauvegardeEffectuée = "oui";
              String sauvegardeUtilisateur = readString();
              while (
                !equals(sauvegardeUtilisateur, "oui") &&
                !equals(sauvegardeUtilisateur, "non")
              ) {
                text(couleur_erreur);
                println();
                println("Erreur, ce n'est pas possible !");
                text(couleur_texte);
                print("Souhaitez-vous sauvegarder ? (oui/non) ");
                sauvegardeUtilisateur = readString();
              }
              if (MotVerifie(sauvegardeUtilisateur, sauvegardeEffectuée)) {
                sauvegarde(utilisateur);
              }
            }
          }
        }
      }
      i += 1;
      println();
      print("Souhaitez-vous rejouer ? (oui/non) ");
      String repRejouer = readString();
      while (!equals(repRejouer, "oui") && !equals(repRejouer, "non")) {
        text(couleur_erreur);
        println();
        println("Erreur, ce n'est pas possible !");
        text(couleur_texte);
        print("Souhaitez-vous rejouer ? (oui/non) ");
        repRejouer = readString();
      }
      if (equals(repRejouer, "oui")) {
        jouer = "oui";
      } else {
        jouer = "non";
      }
    }
  }
}
