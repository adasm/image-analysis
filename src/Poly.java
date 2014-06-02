/* By Adam Michalowski (c) 2014 */

public class Poly {
    public Point points[];

    public Poly(Point a, Point b, Point c) {
        points = new Point[3];
        points[0] = a; points[1] = b; points[2] = c;
    }

    public Poly(Point a, Point b, Point c, Point d) {
        points = new Point[4];
        points[0] = a; points[1] = b;
        points[2] = c; points[3] = d;
    }
}
