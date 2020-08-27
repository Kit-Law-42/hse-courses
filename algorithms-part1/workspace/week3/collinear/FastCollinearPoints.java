import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // finds all line segments containing 4 or more points
        if (points == null) throw new IllegalArgumentException();
        int len = points.length;
        for (int i = 0; i < len; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < len; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        Point[] clonePoints = points.clone();
        ArrayList<LineSegment> storeSegments = new ArrayList<LineSegment>();

        if (len < 4) {
            lineSegments = new LineSegment[0];
            return;
        }
        
        for (Point point : points) {
            // sort clonepoints array to sort all points by slope (from -ve to +ve)
            Arrays.sort(clonePoints, point.slopeOrder());

            // set slope = slope between point pivot and point 1
            // point 0 = pivot points itself.
            double tmpSlope = point.slopeTo(clonePoints[0]);
            int count = 1;
            int i;

            for (i = 1; i < len; i++) {
                if (point.slopeTo(clonePoints[i]) == tmpSlope) {
                    count++;
                    continue;
                }
                else {
                    if (count >= 3) {
                        Arrays.sort(clonePoints, i - count, i);
                        if (point.compareTo(clonePoints[i - count]) < 0)
                            storeSegments.add(new LineSegment(point, clonePoints[i - 1]));
                    }
                    tmpSlope = point.slopeTo(clonePoints[i]);
                    count = 1;
                }
            }

            if (count >= 3) {
                Arrays.sort(clonePoints, i - count, i);
                if (point.compareTo(clonePoints[i - count]) < 0)
                    storeSegments.add(new LineSegment(point, clonePoints[i - 1]));
            }
        }
        lineSegments = storeSegments.toArray(new LineSegment[storeSegments.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
