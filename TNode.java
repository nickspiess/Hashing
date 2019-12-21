public class TNode {

    private String item;
    private TNode parent;
    private int depth;

    public TNode(String item, TNode parent, int depth) {
        this.item = item;
        this.parent = parent;
        this.depth = depth;
    }

    public String getItem() {
        return item;
    }

    public TNode getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

}
