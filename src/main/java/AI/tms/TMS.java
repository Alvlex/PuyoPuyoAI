package AI.tms;

import AI.Strategy;
import app.*;

import java.util.ArrayList;
import java.util.List;

public class TMS implements Strategy {

    List<Node> depth3 = new ArrayList<>();
    List<Template> templates = new ArrayList<>();


    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo) {
        Node root = new Node(b, null);
        recursiveTree(root, 0, 2, currentPuyo);
        double highestScore = 0;
        Node selectedNode = null;
        for (Node depth3Node: depth3){
            short[][] matrix = generateStateMatrix(depth3Node.getBoard());
            for (Template t: templates){
                double score = getScore(matrix, t);
                if (score > highestScore){
                    highestScore  = score;
                    selectedNode = depth3Node;
                }
            }
        }

        return findNextMove(root, selectedNode, currentPuyo[0]);
    }

    private Move findNextMove(Node root, Node target, Puyo[] puyo){
        Node temp = target;
        while(temp.getParent() != root){
            temp = temp.getParent();
            if (temp == null){
                throw new RuntimeException("Parent is null");
            }
        }
        return findDiff(root, temp, puyo);
    }

    private Move findDiff(Node first, Node second, Puyo[] puyo){
        for (int col = 0; col < 6; col ++){
            for (int rot = 0; rot < 4; rot ++){
                Move m = new Move(col, rot);
                Board b1 = first.getBoard().copyBoard();
                b1.dropPuyo(puyo, m);
                if (b1.equalBoards(second.getBoard())){
                    return m;
                }
            }
        }
        Output o = new Output(new Board[]{first.getBoard(), second.getBoard()});
        throw new RuntimeException(o.printBoards() + "\n" + puyo[0].getColour() + " " + puyo[1].getColour());
    }

    private short[][] generateStateMatrix(Board b){
        short[][] matrix = new short[78][78];
        for (int i = 0; i < 78; i ++){
            for (int j = 0; j < 78; j ++){
                Puyo p1 = b.getPuyo(i % 6, Math.floorDiv(i, 6));
                Puyo p2 = b.getPuyo(j % 6, Math.floorDiv(j, 6));
                if (p1 == null || p2 == null){
                    matrix[i][j] = 0;
                }
                else if (p1.getColour().equals(p2.getColour())){
                    matrix[i][j] = 1;
                }
                else{
                    matrix[i][j] = -1;
                }
            }
        }
        return matrix;
    }

    public List<Node> generatePoss(Node parent, Puyo[] puyoPair){
        List<Node> resultStates = new ArrayList<>();

        for (int i = 0; i < 2; i ++){
            for (int col = 0; col < 6; col ++){
                Board temp = parent.getBoard().copyBoard();
                Move m = new Move(col, i * 2);
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp, parent));
            }
            for (int row = 0; row < 5; row ++){
                Move m = new Move(row, i * 2 + 1);
                Board temp = parent.getBoard().copyBoard();
                if (temp.dropPuyo(puyoPair, m))
                    resultStates.add(new Node(temp, parent));
            }
        }
        return resultStates;
    }


    private int recursiveTree(Node root, int depth, int maxDepth, Puyo[][] currentPuyo){
        if (!new Chain(root.getBoard()).isPopping()) {
            if (depth < maxDepth) {
                List<Node> children = generatePoss(root, currentPuyo[depth]);
                int totalLeafNodes = 0;
                for (Node child : children) {
                    totalLeafNodes += recursiveTree(child, depth + 1, maxDepth, currentPuyo);
                }
                root.addChildren(children);
                return totalLeafNodes;
            } else if (depth == maxDepth) {
                List<Node> children = generatePoss(root, currentPuyo[depth]);
                root.addChildren(children);
                depth3.addAll(children);
                return children.size();
            }
        }
        return 0;
    }

    private double getScore(short[][] stateMatrix, Template template){
        double numerator = 0;
        for (int i = 0; i < 78; i ++){
            if (stateMatrix[i][0] != 0){
                for (int j = 0; j < 78; j ++){
                    int temp = stateMatrix[i][j] * template.getEntry(i, j);
                    if (temp < 0){
                        return Double.NEGATIVE_INFINITY;
                    }
                    else{
                        numerator += temp;
                    }
                }
            }
        }
        return numerator / template.getTotalWeight();
    }
}
