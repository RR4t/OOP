import java.util.*;

public class TreeVisualizer {

    public static void printTreeStructure(HilbertNode root) {
        System.out.println("Структура дерева Гильберта:");
        System.out.println("═══════════════════════════");
        printNode(root, "", true, true);
        System.out.println("═══════════════════════════");
    }

    //рекурсивный метод для вывода узла
    private static void printNode(HilbertNode node, String prefix, boolean isTail, boolean isRoot) {
        String nodeType = node.isLeaf() ? "Leaf" : "Node";
        String content = getNodeContent(node);

        //вывод текущего узла
        if (isRoot) {
            System.out.println(prefix + "Root " + content);
        } else {
            String connector = isTail ? "└── " : "├── ";
            System.out.println(prefix + connector + content);
        }

        if (!node.isLeaf()) {
            //обработка дочерних узлов
            List<HilbertNode> children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                boolean isLastChild = (i == children.size() - 1);
                String newPrefix = prefix + (isTail ? "    " : "│   ");
                printNode(children.get(i), newPrefix, isLastChild, false);
            }
        } else if (node.isLeaf() && node.getPoints().size() > 0) {
            //вывод точек для листового узла
            List<Point> points = node.getPoints();
            for (int i = 0; i < points.size(); i++) {
                boolean isLastPoint = (i == points.size() - 1);
                String pointPrefix = prefix + (isTail ? "    " : "│   ");
                String pointConnector = isLastPoint ? "└── " : "├── ";
                Point point = points.get(i);
                System.out.println(pointPrefix + pointConnector + point.toString());
            }
        }
    }

    private static String getNodeContent(HilbertNode node) {
        if (node.isLeaf()) {
            return String.format("[maxH=%d, points=%d]",
                    node.getMaxHilbertValue(),
                    node.getPoints().size());
        } else {
            return String.format("[maxH=%d, children=%d]",
                    node.getMaxHilbertValue(),
                    node.getChildren().size());
        }
    }
}
