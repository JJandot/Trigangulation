import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

/** La classe algorithme. */
class Algorithmes {

	/** Algorithme qui prend un ensemble de points et qui retourne un ensemble de segments. */
	static Vector<Segment> algorithme1(Vector<Point> points)
	{
		// Creation de la liste des segments
		Vector<Segment> segments = new Vector<>();

		// Ajout d'un segment entre chaque paire de points consecutifs
		for(int n1 = 0; n1 < points.size() - 1; n1++)
		{
			Point p1 = points.elementAt(n1);
			Point p2 = points.elementAt(n1+1);
			segments.addElement(new Segment(p1,p2));
		}

		Point pFirst = points.firstElement();
		Point pLast = points.lastElement();
		segments.addElement(new Segment(pLast, pFirst));
		marquage(points);
		points = triPoints(points);
        triangule(points, segments);
		return segments;
	}

	/** Retourne un nombre aleatoire entre 0 et n-1. */
	static int rand(int n)
	{
		int r = new Random().nextInt();

		if (r < 0) r = -r;

		return r % n;
	}

    /**
     * Parcours la liste de tous les sommets, et indique si ils appartiennent à la chaine gauche ou droite du polygone,
     * avec un boolean <code>right</code> valant vrai ou faux.
     * @param points le vecteur contenant tous les sommets du polygone, à marquer
     */
	private static void marquage(Vector<Point> points){
        if(points.size() == 0)
            return;
        points.firstElement().right = true;
        for(int i = 1; i < points.size(); ++i){
            points.elementAt(i).right = points.elementAt(i-1).y < points.elementAt(i).y;
		}
    }

    /**
     * Genere la liste générale contenant tous les points, triés et marqués par un numéro, de haut en bas.
     * @param points le vecteur contenant tous les sommets préalablement marqués du polygone courant
     * @return le résultat de <code>getChaine</code>, qui correspond à la liste triée de tous les sommets
     */
	private static Vector<Point> triPoints(Vector<Point> points){
		Vector<Point> chaineGauche = new Vector<>();
		Vector<Point> chaineDroite = new Vector<>();

		for(Point p : points){
			if(p.right)
				chaineDroite.add(p);
			else
				chaineGauche.add(p);
		}
        Collections.reverse(chaineGauche);
        return getChaine(chaineGauche, chaineDroite);
	}

	private static Vector<Point> getChaine(Vector<Point> chaineGauche, Vector<Point> chaineDroite){
        Vector<Point> chaineFigure = new Vector<>();

		while(true){
			if(chaineDroite.isEmpty()){
				chaineFigure.addAll(chaineGauche);
				break;
			}
			else if(chaineGauche.isEmpty()){
				chaineFigure.addAll(chaineDroite);
				break;
			}
			if(chaineDroite.firstElement().y < chaineGauche.firstElement().y){
				chaineFigure.add(chaineDroite.firstElement());
				chaineDroite.remove(chaineDroite.firstElement());
			}
			else{
				chaineFigure.addElement(chaineGauche.firstElement());
				chaineGauche.remove(chaineGauche.firstElement());
			}
		}
		for(int i = 0; i < chaineFigure.size(); ++i)
            chaineFigure.elementAt(i).flag = i;
        return chaineFigure;
	}

	private static boolean isPositive(Point ext1, Point origine, Point ext2){
		double vecteur1x;
		double vecteur2x;
		double vecteur1y;
		double vecteur2y;

		vecteur1x = ext1.x - origine.x;
		vecteur1y = ext1.y - origine.y;

		vecteur2x = ext2.x - origine.x;
		vecteur2y = ext2.y - origine.y;

        if(origine.right)
            return ((vecteur2x * vecteur1y) - (vecteur1x * vecteur2y)) > 0;
		return ((vecteur1x * vecteur2y) - (vecteur2x * vecteur1y)) > 0;
	}

	private static void triangule(Vector<Point> chaineGenerale, Vector<Segment> segments){

		if(chaineGenerale.size() < 4)
			return;

		for(Segment s : segments)
			s.isDiagonale = false;

		Stack<Point> pile = new Stack<>();
		pile.push(chaineGenerale.firstElement());
		pile.push(chaineGenerale.elementAt(1));

		for(int i = 2; i < chaineGenerale.size() - 1; ++i){
			Point courant = chaineGenerale.get(i);
			Point prec = chaineGenerale.get(i-1);

			if(pile.peek().right != courant.right){
				while (pile.size() > 1){
					Segment s = new Segment(courant, pile.pop());
					s.isDiagonale = true;
					segments.addElement(s);
				}
				pile.pop();
				pile.push(prec);
				pile.push(courant);
			}
			else{
				while (pile.size() > 1) {
					Point origine = pile.pop();
                    Point ext1 = pile.pop();
                    if (isPositive(ext1, origine, courant)) {
                        Segment s = new Segment(ext1, courant);
						s.isDiagonale = true;
						segments.add(s);
						pile.push(ext1);
					} else {
						pile.push(ext1);
						pile.push(origine);
						break;
					}
				}
				pile.push(courant);
			}
		}
		pile.pop();
		while (pile.size() > 1){
			Segment s = new Segment(chaineGenerale.lastElement(), pile.pop());
			s.isDiagonale = true;
            segments.add(s);
		}
	}
}
