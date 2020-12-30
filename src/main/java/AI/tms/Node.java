package AI.tms;

import app.Board;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Board b;
    private List<Node> children;
    private Node parent;

    public Node(Board b, Node parent){
        this.b = b;
        this.parent = parent;
        children = new ArrayList<>();
    }

    void addChildren(List<Node> children){
        this.children.addAll(children);
    }
    List<Node> getChildren(){
        return children;
    }

    Node getParent(){
        return parent;
    }

    Board getBoard(){
        return b;
    }

}
