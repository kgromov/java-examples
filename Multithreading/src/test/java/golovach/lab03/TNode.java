package golovach.lab03;

/**
 * Created by konstantin on 06.05.2017.
 */
public class TNode {
    public int value;
    public TNode left;
    public TNode right;

    public TNode(int value, TNode left, TNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return toString(new StringBuilder(), this).toString();
    }

    private static StringBuilder toString(StringBuilder string, TNode tNode) {
        string.append('{');
        if (tNode != null) {
            string.append(tNode.value);
            toString(string.append(", "), tNode.left);
            toString(string.append(", "), tNode.right);
        }
        return string.append('}');
    }
}
