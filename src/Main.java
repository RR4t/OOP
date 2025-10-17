import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        System.out.println("Вставка случайных точек");

        HilbertRTree tree = new HilbertRTree(4); // 16x16 пространство
        Random random = new Random(42);
        for (int i = 0; i < 11; i++) {
            int x = random.nextInt(16);
            int y = random.nextInt(16);
            Point point = new Point(x, y);
            point.setHilbertValue(tree.computeHilbertValue(point));
            tree.insert(point);
            System.out.printf("Вставлена точка %s %n",
                    point, i, tree.computeHilbertValue(point));
        }

        // визуализация
        tree.printTree();

        // методы
        System.out.println("\nВсе точки в дереве:");
        System.out.println(tree.searchAll());
        System.out.println("\nПоиск по диапазону");
        System.out.println(tree.searchByHilbertRange(50,150));
    }
}
