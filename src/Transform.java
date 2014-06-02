/* By Adam Michalowski (c) 2014 */

import Jama.Matrix;

import java.awt.geom.Point2D;

public class Transform {
    public Matrix matrix = new Matrix(3,3);
    public boolean valid = false;

    public Transform(Poly poly) {
        if(poly.points.length == 3)
            affinite(poly);
        else
            perspective(poly);
    }

    public Point2D.Float transform(Point p) {
        double dv[][] = {{p.x, p.y, 1}};
        Matrix v = new Matrix(dv);
        Matrix mo = v.times(matrix);
        float z = 1;//(float)mo.get(0,2);
        return new Point2D.Float((float)mo.get(0, 0) / z, (float)mo.get(0, 1) / z);
    }

    private void affinite(Poly p) {
        double X[][] =
                {
                        {p.points[0].x, p.points[0].y, 1, 0, 0, 0},
                        {p.points[1].x, p.points[1].y, 1, 0, 0, 0},
                        {p.points[2].x, p.points[2].y, 1, 0, 0, 0},
                        {0, 0, 0, p.points[0].x, p.points[0].y, 1},
                        {0, 0, 0, p.points[1].x, p.points[1].y, 1},
                        {0, 0, 0, p.points[2].x, p.points[2].y, 1}
                };
        double Y[][] =
                {
                        {p.points[0].nearest.x},
                        {p.points[1].nearest.x},
                        {p.points[2].nearest.x},
                        {p.points[0].nearest.y},
                        {p.points[1].nearest.y},
                        {p.points[2].nearest.y}
                };

        Matrix matX = new Matrix(X);
        if(matX.det() == 0) return;
        Matrix out = (matX).inverse().times(new Matrix(Y));

        double A[][] =
                {
                        {out.get(0, 0), out.get(1, 0), out.get(2, 0)},
                        {out.get(3, 0), out.get(4, 0), out.get(5, 0)},
                        {0, 0, 1}
                };

        matrix = new Matrix(A);
        valid = true;
    }

    private void perspective(Poly p) {
        double X[][] =
                {
                        {p.points[0].x, p.points[0].y, 1, 0, 0, 0, -(p.points[0].x)*(p.points[0].nearest.x), -(p.points[0].y)*(p.points[0].nearest.x)},
                        {p.points[1].x, p.points[1].y, 1, 0, 0, 0, -(p.points[1].x)*(p.points[1].nearest.x), -(p.points[1].y)*(p.points[1].nearest.x)},
                        {p.points[2].x, p.points[2].y, 1, 0, 0, 0, -(p.points[2].x)*(p.points[2].nearest.x), -(p.points[2].y)*(p.points[2].nearest.x)},
                        {p.points[3].x, p.points[3].y, 1, 0, 0, 0, -(p.points[3].x)*(p.points[3].nearest.x), -(p.points[3].y)*(p.points[3].nearest.x)},
                        {0, 0, 0, p.points[0].x, p.points[0].y, 1, -(p.points[0].x)*(p.points[0].nearest.y), -(p.points[0].y)*(p.points[0].nearest.y)},
                        {0, 0, 0, p.points[1].x, p.points[1].y, 1, -(p.points[1].x)*(p.points[1].nearest.y), -(p.points[1].y)*(p.points[1].nearest.y)},
                        {0, 0, 0, p.points[2].x, p.points[2].y, 1, -(p.points[2].x)*(p.points[2].nearest.y), -(p.points[2].y)*(p.points[2].nearest.y)},
                        {0, 0, 0, p.points[3].x, p.points[3].y, 1, -(p.points[3].x)*(p.points[3].nearest.y), -(p.points[3].y)*(p.points[3].nearest.y)}
                };
        double Y[][] =
                {
                        {p.points[0].nearest.x},
                        {p.points[1].nearest.x},
                        {p.points[2].nearest.x},
                        {p.points[3].nearest.x},
                        {p.points[0].nearest.y},
                        {p.points[1].nearest.y},
                        {p.points[2].nearest.y},
                        {p.points[3].nearest.y}
                };

        Matrix matX = new Matrix(X);
        if(matX.det() == 0) return;
        Matrix out = (matX).inverse().times(new Matrix(Y));

        double A[][] =
                {
                        {out.get(0, 0), out.get(1, 0), out.get(2, 0)},
                        {out.get(3, 0), out.get(4, 0), out.get(5, 0)},
                        {out.get(6, 0), out.get(7, 0), 1}
                };

        matrix = new Matrix(A);
        valid = true;
    }
}
