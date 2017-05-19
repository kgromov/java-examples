package golovach.lecture04.lab03;

/**
 * Created by konstantin on 09.05.2017.
 */
public class TNodeUtils {

    public static TNode createTree(int size) {
        TNode tNode = null;
        return tNode;
    }

    public static int size(TNode tNode) {
        return (tNode.right != null ? size(tNode.right) : 0) +
                (tNode.left != null ? size(tNode.left) : 0) + 1;
    }

    public static int height(TNode tNode) {
        int leftHeight = tNode.left != null ? height(tNode.left) : 0;
        int rightHeight = tNode.right != null ? height(tNode.right) : 0;
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static int max(TNode tNode) {
        int leftMax = Math.max((tNode.left != null) ? max(tNode.left) : tNode.value, tNode.value);
        int rightMax = Math.max((tNode.right != null) ? max(tNode.right) : tNode.value, tNode.value);
        return Math.max(leftMax, rightMax);
    }

    private static void print(TNode root, int depth) {
        if (root != null) {
            print(root.right, depth + 1);
            for (int k = 0; k < depth; k++) {
                System.out.print("   ");
            }
            System.out.println(root.value);
            print(root.left, depth + 1);
        }
    }

    public static void main(String[] args) {
        TNode root = new TNode(0, null, null);
        TNode leftTree = new TNode(1, new TNode(11, null, null), new TNode(12, null, null));
        TNode rightTree = new TNode(2, null, new TNode(22, null, null));
        root.left = leftTree;
        root.right = rightTree;
        System.out.println("Tree size = " + size(root));
        System.out.println("Tree height = " + height(root));
        System.out.println("Tree max leaf value = " + max(root));
    }
}
