package Strategies.pms;

import Game.Board;
import Game.Chain;

public class Node {
    private Board b;
    private Node parent;
    private int connections;
    private int garbage;
    private int chainLength;

    public Node(){
        prepare(new Board(), null);
    }

    public Node(Board b){
        prepare(b, null);
    }

    private void prepare(Board b, Node parent){
        this.parent = parent;
        this.b = b.copyBoard();
        this.connections = countConnections();
        this.garbage = countGarbage();
        this.chainLength = countChainLength();
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

    public Board getBoard(){
        return b;
    }

    int countGarbage(){
        Chain c = new Chain(this.b.copyBoard());
        c.runChain(this.b.findAllPuyo());
        if (c.chainLength() == 0)
            return -1;
        return c.score();
    }

    int getGarbage(){
        return garbage;
    }

    int countChainLength(){
        Chain c = new Chain(this.b.copyBoard());
        c.runChain(this.b.findAllPuyo());
        return c.chainLength();
    }

    int getChainLength(){
        return chainLength;
    }

    Node getParent(){
        return parent;
    }

    void removeParent(){
        parent = null;
    }

    int getConnections(){return connections;}
}
