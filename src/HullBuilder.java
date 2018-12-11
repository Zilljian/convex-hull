import javax.swing.JPanel;
import java.awt.*;
import java.util.Random;
import java.util.Vector;

public class HullBuilder extends JPanel {
    private Vector<Integer> xAxis = new Vector<>();
    private Vector<Integer> yAxis = new Vector<>();
    private Vector<Integer> position = new Vector<>();
    private Vector<Integer> hullStack = new Vector<>();
    private int[] polygonX;
    private int[] polygonY;

    private int pointsNumber;

    public HullBuilder(int pointsNumber) {
        this.pointsNumber = pointsNumber;
        for (int i =0; i < pointsNumber; i++) {
            position.add(i);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        generatePoints();

        Graphics2D field = (Graphics2D) g;
        setBackground(Color.lightGray);
        field.setColor(Color.black);

        for (int i = 0; i < pointsNumber; i++) {
            field.drawOval(xAxis.get(i), yAxis.get(i), 5, 5);
            field.fillOval(xAxis.get(i), yAxis.get(i), 5, 5);
        }

        field.drawPolygon(polygonX, polygonY, hullStack.size());
    }

    private void generatePoints() {
        Dimension size = getSize();
        int w = size.width;
        int h = size.height;

        for (int i = 0; i < pointsNumber; i++) {
            Random r = new Random();
            xAxis.add(w - Math.abs(r.nextInt()) % w);
            yAxis.add(Math.abs(r.nextInt()) % h);
        }

        findMinConvexHull();

        polygonX = new int[hullStack.size()];
        polygonY = new int[hullStack.size()];

        for (int i = 0; i < hullStack.size(); i++) {
            polygonX[i] = xAxis.get(hullStack.get(i));
            polygonY[i] = yAxis.get(hullStack.get(i));
        }
    }

    private void findMinConvexHull() {
        for(int i = 0; i < pointsNumber; i++) {
            if (xAxis.get(position.get(i)) < xAxis.get(position.get(0))) {
                int temp = position.get(i);
                position.setElementAt(position.get(0), i);
                position.setElementAt(temp, 0);
            }
        }

        for (int i = 2; i < pointsNumber; i++) {
            int j =i;
            while(j > 1 &&
                    isMoreLeft(
                        xAxis.get(position.get(0)),
                        yAxis.get(position.get(0)),
                        xAxis.get(position.get(j - 1)),
                        yAxis.get(position.get(j - 1)),
                        xAxis.get(position.get(j)),
                        yAxis.get(position.get(j)))) {
                int temp = position.get(j);
                position.setElementAt(position.get(j-1), j);
                position.setElementAt(temp, j-1);
                j--;
            }
        }

        hullStack.add(position.get(0));
        hullStack.add(position.get(1));

        for (int i = 2; i < pointsNumber; i++) {
            while (isMoreLeft(
                    xAxis.get(hullStack.get(hullStack.size() - 2)),
                    yAxis.get(hullStack.get(hullStack.size() - 2)),
                    xAxis.get(hullStack.lastElement()),
                    yAxis.get(hullStack.lastElement()),
                    xAxis.get(position.get(i)),
                    yAxis.get(position.get(i)))) {

                hullStack.removeElementAt(hullStack.size() - 1);
            }
            hullStack.add(position.get(i));
        }

    }

    private boolean isMoreLeft(int xS, int yS, int x0, int y0, int x1, int y1) {
        return ((x0 - xS)*(y1 - yS) - (y0 - yS)*(x1 - xS)) >= 0;
    }
}
