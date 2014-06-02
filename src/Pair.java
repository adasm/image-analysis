/* By Adam Michalowski (c) 2014 */

public class Pair implements Comparable<Pair> {
    public Point point;
    public double value;

    public Pair(Point source, Point target, Value pairValue) {
        point = target;
        value = pairValue.evaluate(source, target);
    }

    public static interface Value {
        public double evaluate(Point a, Point b);
    }

    @Override
    public int compareTo(Pair other) {
        return Double.compare(value, other.value);
    }
}
