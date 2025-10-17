import java.util.ArrayList;
import java.util.List;

class HilbertNode {
    private static final int MAX_ENTRIES = 6;

    private List<Point> points;
    private List<HilbertNode> children; // узлы
    private boolean isLeaf;
    private long maxHilbertValue;

    public HilbertNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        if (isLeaf) {
            this.points = new ArrayList<>();
        } else {
            this.children = new ArrayList<>();
        }
        this.maxHilbertValue = -1;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public boolean isFull() {
        return isLeaf ? points.size() >= MAX_ENTRIES : children.size() >= MAX_ENTRIES;
    }

    public long getMaxHilbertValue() {
        return maxHilbertValue;
    }

    public void addPoint(Point point, long hilbertValue) {
        if (!isLeaf) throw new IllegalStateException("Нельзя добавить точку для узла без листьев ");
        points.add(point);
        updateMaxHilbertValue(hilbertValue);
    }

    public void addChild(HilbertNode child, long hilbertValue) {
        if (isLeaf) throw new IllegalStateException("Нельзя добавить дочерний элемент к листовому узлу");
        children.add(child);
        updateMaxHilbertValue(hilbertValue);
    }

    private void updateMaxHilbertValue(long value) {
        if (value > maxHilbertValue) {
            maxHilbertValue = value;
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<HilbertNode> getChildren() {
        return children;
    }
    
    @Override
    public String toString() {
        if (isLeaf) {
            return String.format("Leaf[H%d, points=%s]", maxHilbertValue, points);
        } else {
            return String.format("Node[H%d, children=%d]", maxHilbertValue, children.size());
        }
    }
}