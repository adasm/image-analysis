/* By Adam Michalowski (c) 2014 */

public class Pair implements Comparable<Pair> {
    public Point point;
    public float value;

    public Pair(Point source, Point target, Value pairValue) {
        point = target;
        value = pairValue.evaluate(source, target);
    }

    public static interface Value {
        public float evaluate(Point a, Point b);
    }

    @Override
    public int compareTo(Pair other) {
        return Float.compare(value, other.value);
    }
}
