package AI.pms;

import app.Board;
import app.Chain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Node {
    Board b;
    private List<Node> children;
    private Node parent;
    private int garbage;
    private int connections;

    public Node(){
        prepare(new Board(), null);
    }

    public Node(Board b){
        prepare(b, null);
    }

    private void prepare(Board b, Node parent){
        this.parent = parent;
        this.b = b.copyBoard();
        children = new ArrayList<>();
        Chain c = new Chain(this.b.copyBoard());
        c.runChain(this.b.findAllPuyo());
        garbage = c.score();
        if (c.chainLength() == 0)
            garbage = -1;
        connections = countConnections();
    }

    public Node(Board b, Node parent){
        prepare(b, parent);
    }

    private int countConnections() {
        int connections = 0;
        for (int i = 0; i < b.getNoCols() - 1; i ++){
            for (int j = 0; j < b.getNoRows() - 1; j ++){
                if (b.getPuyo(i,j) != null) {
                    if (b.getPuyo(i + 1, j) != null && b.getPuyo(i, j).getColour().equals(b.getPuyo(i + 1, j).getColour()))
                        connections++;
                    if (b.getPuyo(i,j + 1) != null && b.getPuyo(i, j).getColour().equals(b.getPuyo(i, j + 1).getColour()))
                        connections++;
                }
            }
        }
        return connections;
    }

    public void addChildren(Collection<Node> nodes){
        children.addAll(nodes);
    }

    public List<Node> getChildren(){
        return new ArrayList<>(children);
    }

    public Board getBoard(){
        return b;
    }

    int getGarbage(){
        return garbage;
    }

    Node getParent(){
        return parent;
    }

    void removeParent(){
        parent = null;
    }

    void removeChildren(){
        children.clear();
    }

    int getConnections(){return connections;}
}
