package AI.pms;

import app.Board;
import app.Chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node {
    private Board b;
    private List<Node> children;
    private int garbage;

    public Node(Board b){
        this.b = b;
        children = new ArrayList<>();
        Chain c = new Chain(this.b.copyBoard());
        c.runChain(this.b.findAllPuyo());
        garbage = c.score();
        if (c.chainLength() == 0)
            garbage = -1;
    }

    void addChild(Node n){
        children.add(n);
    }

    void addChildren(Collection<Node> nodes){
        children.addAll(nodes);
    }

    public List<Node> getChildren(){
        return new ArrayList<>(children);
    }

    Board getBoard(){
        return b.copyBoard();
    }

    int getGarbage(){
        return garbage;
    }
}
