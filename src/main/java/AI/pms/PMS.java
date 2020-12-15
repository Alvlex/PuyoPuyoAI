package AI.pms;

import AI.Strategy;
import app.*;

import java.util.ArrayList;
import java.util.List;

public class PMS implements Strategy {

    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo) {
        Node root = new Node(b);
        generateTree(root, currentPuyo, new Board());
        Node target = selectNode(root, new ArrayList<>());
        Move m = findNextMove(root, target, currentPuyo[0]);
        b.dropPuyo(currentPuyo[0], m);
        return m;
    }

    public Move makeMove(Board b, Puyo[][] currentPuyo, Board opponent){
        Node root = new Node(b);
        generateTree(root, currentPuyo, opponent);
        Node target = selectNode(root, new ArrayList<>());
        return findNextMove(root, target, currentPuyo[0]);
    }

    private Move findNextMove(Node root, Node target, Puyo[] puyo){
        List<Node> children = root.getChildren();
        for(Node n: children){
            List<Node> walkthrough = new ArrayList<>();
            walkthrough.add(n);
            while(!walkthrough.isEmpty()){
                Node current = walkthrough.remove(0);
                if (current == target){
                    return findDiff(root, n, puyo);
                }
                walkthrough.addAll(current.getChildren());
            }
        }
        throw new RuntimeException();
    }

    private Move findDiff(Node first, Node second, Puyo[] puyo){
        for (int col = 0; col < 6; col ++){
            for (int rot = 0; rot < 4; rot ++){
                Move m = new Move(col, rot);
                Board b1 = first.getBoard();
                b1.dropPuyo(puyo, m);
                if (b1.equalBoards(second.getBoard())){
                    return m;
                }
            }
        }
        Output o = new Output(new Board[]{first.getBoard(), second.getBoard()});
        throw new RuntimeException(o.printBoards());
    }

    public List<Node> generatePoss(Board b, Puyo[] puyoPair){
        List<Node> resultStates = new ArrayList<>();

        for (int i = 0; i < 2; i ++){
            for (int col = 0; col < 6; col ++){
                Board temp = b.copyBoard();
                Move m = new Move(col, i * 2);
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp));
            }
            for (int row = 0; row < 5; row ++){
                Move m = new Move(row, i * 2 + 1);
                Board temp = b.copyBoard();
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp));
            }
        }
        return resultStates;
    }

    public void generateTree(Node root, Puyo[][] next3Pairs, Board opponent){
        List<Node> tempList = new ArrayList<>();
        tempList.add(root);
        Chain c = new Chain(opponent.copyBoard());
        c.runChain(opponent.findAllPuyo());
        int maxDepth = Math.min(3, c.chainLength() == 0 ? 3 : c.chainLength());
        for (int i = 0; i < maxDepth; i ++){
            for (int j = tempList.size() - 1; j >= 0; j --){
                Node tempNode = tempList.remove(0);
                if (tempNode.getGarbage() == -1){
                    tempNode.addChildren(generatePoss(tempNode.getBoard(), next3Pairs[i]));
                }
                tempList.addAll(tempNode.getChildren());
            }
        }
    }

    private Node selectNode(Node root, List<Boolean> heuristics){
        Node mGNode = root;
        List<Node> walkthrough = root.getChildren();
        if (heuristics.contains(true)){
            while(!walkthrough.isEmpty()){
                Node current = walkthrough.remove(0);
                if (current.getGarbage() > mGNode.getGarbage()){
                    mGNode = current;
                }
                walkthrough.addAll(current.getChildren());
            }
            if (mGNode != root){
                return mGNode;
            }
        }
        else{
            for (int i = 0; i < walkthrough.size(); i ++){
                walkthrough.addAll(walkthrough.remove(0).getChildren());
            }
            while(!walkthrough.isEmpty()){
                Node current = walkthrough.remove(0);
                if (current.getGarbage() > mGNode.getGarbage()){
                    mGNode = current;
                }
                walkthrough.addAll(current.getChildren());
            }
            if (mGNode != root){
                return mGNode;
            }
        }
        return maxConnections(root);
    }

    private int countConnections(Node n) {
        Board b = n.getBoard();
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

    private Node maxConnections(Node root){
        List<Node> walkthrough = root.getChildren();
        Node max = root.getChildren().get(0);
        while(!walkthrough.isEmpty()){
            Node current = walkthrough.remove(0);
            if (countConnections(current) > countConnections(max)){
                max = current;
            }
            walkthrough.addAll(current.getChildren());
        }
        return max;
    }
}
