import java.util.ArrayList;
import java.util.List;

import static java.awt.geom.Point2D.distance;

public class HilbertRTree {
    private HilbertNode root;
    private int order; //порядок кривой

    public HilbertRTree(int order) {
        this.order = order;
        this.root = new HilbertNode(true);
    }

    //вычисление значения Гильберта для точки
    public long computeHilbertValue(Point point) {
        return xyToHilbert((int) (point.Getx()), (int) point.Gety(), order);
    }

    //преобразование координат в значение Гильберта
    private long xyToHilbert(int x, int y, int n) {
        long hilbert = 0;
        for (int i = n - 1; i >= 0; i--) {
            long mask = 1L << i;//битовая маска
            long rx = (x & mask) > 0 ? 1 : 0;
            long ry = (y & mask) > 0 ? 1 : 0;

            hilbert <<= 2;
            hilbert |= (rx << 1) | (rx ^ ry);

            if (ry == 0) {
                if (rx == 1) {
                    x = (1 << n) - 1 - x;
                    y = (1 << n) - 1 - y;
                }
                int temp = x;
                x = y;
                y = temp;
            }
        }
        return hilbert;
    }

    public void insert(Point point) {
        long hilbertValue = computeHilbertValue(point);
        HilbertNode leaf = findLeafForInsertion(root, hilbertValue);
        insertIntoLeaf(leaf, point, hilbertValue);
        if (leaf.isFull()) {
            splitNode(leaf);
        }
    }

    private HilbertNode findLeafForInsertion(HilbertNode node, long hilbertValue) {
        if (node.isLeaf()) {
            return node;
        }

        for (HilbertNode child : node.getChildren()) {
            if (hilbertValue <= child.getMaxHilbertValue()) {
                return findLeafForInsertion(child, hilbertValue);
            }
        }

        //если не нашли, берем последнего потомка
        List<HilbertNode> children = node.getChildren();
        return findLeafForInsertion(children.get(children.size() - 1), hilbertValue);
    }

    private void insertIntoLeaf(HilbertNode leaf, Point point, long hilbertValue) {
        leaf.addPoint(point, hilbertValue);
    }

    private void splitNode(HilbertNode node) {
        if (node.isLeaf()) {
            splitLeafNode(node);
        } else {
            splitInternalNode(node);
        }
    }

    private void splitLeafNode(HilbertNode leaf) {
        List<Point> points = leaf.getPoints();

        //сортировка точек по значению Гильберта
        List<Point> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort((p1, p2) ->
                Long.compare(computeHilbertValue(p1), computeHilbertValue(p2)));

        HilbertNode node1 = new HilbertNode(true);
        HilbertNode node2 = new HilbertNode(true);

        int splitIndex = sortedPoints.size() / 2;

        //распределение точек между узлами
        for (int i = 0; i < splitIndex; i++) {
            Point point = sortedPoints.get(i);
            node1.addPoint(point, computeHilbertValue(point));
        }
        for (int i = splitIndex; i < sortedPoints.size(); i++) {
            Point point = sortedPoints.get(i);
            node2.addPoint(point, computeHilbertValue(point));
        }

        replaceNodeWithInternal(leaf, node1, node2);
    }

    private void splitInternalNode(HilbertNode node) {
        List<HilbertNode> children = node.getChildren();

        //сортировка потомков по максимальному значению Гильберта
        List<HilbertNode> sortedChildren = new ArrayList<>(children);
        sortedChildren.sort((c1, c2) ->
                Long.compare(c1.getMaxHilbertValue(), c2.getMaxHilbertValue()));

        HilbertNode node1 = new HilbertNode(false);
        HilbertNode node2 = new HilbertNode(false);

        int splitIndex = sortedChildren.size() / 2;

        for (int i = 0; i < splitIndex; i++) {
            HilbertNode child = sortedChildren.get(i);
            node1.addChild(child, child.getMaxHilbertValue());
        }
        for (int i = splitIndex; i < sortedChildren.size(); i++) {
            HilbertNode child = sortedChildren.get(i);
            node2.addChild(child, child.getMaxHilbertValue());
        }

        replaceNodeWithInternal(node, node1, node2);
    }

    //замена узла на внутренний узел с двумя потомками
    private void replaceNodeWithInternal(HilbertNode oldNode, HilbertNode child1, HilbertNode child2) {
        if (oldNode == root) {
            //создание нового корневого узла
            root = new HilbertNode(false);
            root.addChild(child1, child1.getMaxHilbertValue());
            root.addChild(child2, child2.getMaxHilbertValue());
        } else if (root != null){

            System.out.println("Нужно обновить родительский узел");
        }
    }

    public List<Point> searchAll() {
        List<Point> results = new ArrayList<>();
        collectPoints(root, results);
        return results;
    }

    private void collectPoints(HilbertNode node, List<Point> results) {
        if (node.isLeaf()) {
            results.addAll(node.getPoints());
        } else {
            for (HilbertNode child : node.getChildren()) {
                collectPoints(child, results);
            }
        }
    }

    //поиск точек в диапазоне значений Гильберта
    public List<Point> searchByHilbertRange(long minHilbert, long maxHilbert) {
        List<Point> results = new ArrayList<>();
        searchByHilbertRange(root, minHilbert, maxHilbert, results);
        return results;
    }

    private void searchByHilbertRange(HilbertNode node, long minHilbert, long maxHilbert, List<Point> results) {
        if (node.isLeaf()) {
            for (Point point : node.getPoints()) {
                long hilbertValue = computeHilbertValue(point);
                if (hilbertValue >= minHilbert && hilbertValue <= maxHilbert) {
                    results.add(point);
                }
            }
        } else {
            for (HilbertNode child : node.getChildren()) {
                if (child.getMaxHilbertValue() >= minHilbert) {
                    searchByHilbertRange(child, minHilbert, maxHilbert, results);
                }
            }
        }
    }

    public void printTree() {
        TreeVisualizer.printTreeStructure(root);
    }

}

