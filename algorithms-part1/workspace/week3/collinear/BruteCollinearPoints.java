import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    // finds all line segments containing 4 or more points
    private LineSegment[] lines;
    private int size = 0;

    public BruteCollinearPoints(Point[] points) {
        // finds all line segments containing 4 points
        if (points == null) throw new IllegalArgumentException();


        // check null points / illegal points in array.
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        if (points.length < 4) {
            lines = new LineSegment[0];
            size = 0;
            return;
        }
        
        Point[] ps = points.clone();
        ArrayList<LineSegment> res = new ArrayList<>();
        Arrays.sort(ps);

        // set length as the length of input points array
        int len = ps.length;

        // loop though
        for (int p = 0; p < len - 3; p++) {
            for (int q = p + 1; q < len - 2; q++) {
                double pq = ps[p].slopeTo(ps[q]);
                for (int r = q + 1; r < len - 1; r++) {
                    double qr = ps[q].slopeTo(ps[r]);
                    if (pq != qr) continue;
                    for (int s = r + 1; s < len; s++) {
                        double rs = ps[r].slopeTo(ps[s]);
                        if (qr == rs) {
                            Point[] temp = new Point[4];
                            temp[0] = ps[p];
                            temp[1] = ps[q];
                            temp[2] = ps[r];
                            temp[3] = ps[s];
                            Arrays.sort(temp);
                            if (ps[p] == temp[0]) res.add(new LineSegment(temp[0], temp[3]));
                        }
                    }
                }
            }
        }
        lines = new LineSegment[res.size()];
        size = res.size();
        for (int i = 0; i < res.size(); i++) {
            lines[i] = res.get(i);
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return size;
    }

    // the line segments
    public LineSegment[] segments() {
        return lines.clone();
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
