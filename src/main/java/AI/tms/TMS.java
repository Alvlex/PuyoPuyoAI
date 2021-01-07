package AI.tms;

import AI.Strategy;
import AI.pms.PMS;
import app.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TMS implements Strategy {

    private List<Node> depth3 = new ArrayList<>();
    private ArrayList<Template> templates = new ArrayList<>();
    private PMS pms = new PMS(3, 8, 120);
    private boolean chainMade = false;
    private double averageTime;
    private int turn = 0;

    public TMS(){
        templates.add(new Template("Andromeda.csv"));
        templates.add(new Template("deAlice.csv"));
        templates.add(new Template("Diving.csv"));
        templates.add(new Template("GtrFlat.csv"));
        templates.add(new Template("GtrLShape.csv"));
        templates.add(new Template("Landslide.csv"));
        templates.add(new Template("Sandwich2-1-1.csv"));
        templates.add(new Template("Stairs3-1.csv"));
        templates.add(new Template("Turukame.csv"));
        templates.add(new Template("Yayoi.csv"));
    }

    @Override
    public Move makeMove(Board b, Puyo[][] currentPuyo) {
        if (templates.size() == 0){
            return pms.makeMove(b, currentPuyo);
//            Output o = new Output(new Board[]{b});
//            throw new RuntimeException("No valid template to follow!\n" + o.printBoards());
        }
        depth3.clear();
        turn ++;
        Date d = new Date();
        if (chainMade)
            return pms.makeMove(b, currentPuyo);
        for (int i = templates.size() - 1; i >= 0; i --){
            if (getScore(generateStateMatrix(b), templates.get(i)) == Double.NEGATIVE_INFINITY){
                templates.remove(i);
            }
        }
        Node root = new Node(b, null);
        recursiveTree(root, 0, 2, currentPuyo);
        double highestScore = Double.NEGATIVE_INFINITY;
        if (depth3.size() == 0){
            Output o = new Output(new Board[]{b});
            o.updateCurrentPuyo(currentPuyo, 0);
            throw new RuntimeException("\n" + o.printCurrentPuyo() + "\n" + o.printBoards());
        }
        Node selectedNode = depth3.get(0);
        for (Node depth3Node: depth3){
            short[][] matrix = generateStateMatrix(depth3Node.getBoard());
            for (int i = 0; i < templates.size(); i ++){
                double score = getScore(matrix, templates.get(i));
                if (score >= highestScore){
                    highestScore  = score;
                    selectedNode = depth3Node;
                }
            }
        }
        if (highestScore >= 0.99)
            chainMade = true;
        Move m = findNextMove(root, selectedNode, currentPuyo);
        b.dropPuyo(currentPuyo[0], m);
        averageTime = (1 - 1.0 / turn) * averageTime + (1.0 / turn) * (new Date().getTime() - d.getTime());
        return m;
    }

    public double getAverageTime(){
        return averageTime;
    }
    public String getTemplate(){
        if (templates.size() == 1){
            return templates.get(0).name;
        }
        else {
            return templates.size() + "";
        }
    }

    private Move findNextMove(Node root, Node target, Puyo[][] currentPuyo){
        Node temp = target;
        while(temp.getParent() != root){
            temp = temp.getParent();
            if (temp == null){
                Output o = new Output(new Board[]{root.getBoard()});
                o.updateCurrentPuyo(currentPuyo, 0);
                throw new RuntimeException("Parent is null\n" + o.printCurrentPuyo() + "\n" + o.printBoards());
            }
        }
        return findDiff(root, temp, currentPuyo[0]);
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
