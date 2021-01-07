package tmsPrep;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class Weights {

    HashMap<Character, Integer> weights = new HashMap<>();

    Weights(char[] letters){
        Scanner x = new Scanner(System.in);
        for (char letter: letters){
            if (letter == '0')
                weights.put('0', 0);
            else if (Character.isLowerCase(letter))
                weights.put(letter, 1);
            else{
//                System.out.println("What is the weight for " + letter);
//                weights.put(letter, x.nextInt());
                weights.put(letter, 1000);
            }
        }
    }

    int get(char c){
        return weights.get(c);
    }
}
