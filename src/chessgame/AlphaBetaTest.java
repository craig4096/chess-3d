/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author craig
 */
public class AlphaBetaTest extends Chessboard {
    
    @Override
    public void makeMove(ChessMove move) {}
    
    @Override
    public void undoMove(ChessMove move) {}
    
    @Override
    public List<ChessMove> allPossibleMoves(Side side) {
        System.out.print("Enter number of moves: ");
        Scanner scanner = new Scanner(System.in);
        int numMoves = scanner.nextInt();
        List<ChessMove> moves = new ArrayList<ChessMove>();
        for(int i = 0; i < numMoves; ++i) {
            moves.add(new ChessMove());
        }
        return moves;
    }
    
    @Override
    public int getRating(Side side, List<ChessMove> possibleMoves, int depth) {
        System.out.print("Enter board rating: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
