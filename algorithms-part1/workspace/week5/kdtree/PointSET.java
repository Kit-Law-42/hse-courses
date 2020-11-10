import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(
            Point2D p) {
        checkNull(p);
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNull(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        Stack<Point2D> stack = new Stack<Point2D>();

        for (Point2D p : points) {
            if (rect.contains(p)) {
                stack.push(p);
            }
        }

        return stack;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (isEmpty()) {
            return null;
        }

        Point2D neighbor = null;

        for (Point2D point : points) {
            if (neighbor == null) {
                neighbor = point;
            }

            if (point.distanceSquaredTo(p) < neighbor.distanceSquaredTo(p)) {
                neighbor = point;
            }
        }

        return neighbor;
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {

    }
}
