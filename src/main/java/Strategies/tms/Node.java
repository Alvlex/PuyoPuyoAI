package Strategies.tms;

import Game.Board;
import Game.Chain;
import Game.Move;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Board b;
    private List<Node> children;
    private Node parent;
    private Move m;

    public Node(Board b, Node parent, Move m){
        this.m = m;
        new Chain(b).runChain(b.findAllPuyo());
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

    Move getMove(){
        return m;
    }

}
