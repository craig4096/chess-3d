import chessgame.ChessPiece;
import chessgame.Chessboard;
import chessgame.Side;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author craig
 */
public class ChessboardTest {
    
    private Chessboard chessboard;
    
    public ChessboardTest() {
        chessboard = new Chessboard();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        chessboard.setDefaultBoard();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void correctStartPositions() {
        Assert.assertTrue(chessboard.get(0, 0) == ChessPiece.BlackRook);
        Assert.assertTrue(chessboard.get(4, 7) == ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.get(3, 7) == ChessPiece.WhiteQueen);
    }
    
    @Test
    public void kingMovesTest1() {
        chessboard.set(3, 4, ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 8);
    }
    
    @Test
    public void kingMovesTest2() {
        chessboard.set(3, 5, ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.possibleMoves(3, 5).size() == 5);
    }
    
    @Test
    public void kingMovesTest3() {
        chessboard.set(3, 4, ChessPiece.BlackPawn);
        chessboard.set(3, 5, ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.possibleMoves(3, 5).size() == 3);
    }
    
    @Test
    public void kingMovesTest4() {
        chessboard.set(0, 5, ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.possibleMoves(0, 5).size() == 3);
    }
    
    @Test
    public void queenMovesTest1() {
        chessboard.set(3, 4, ChessPiece.WhiteQueen);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 19);
    }
    
    @Test
    public void queenMovesTest2() {
        chessboard.set(3, 5, ChessPiece.WhiteQueen);
        Assert.assertTrue(chessboard.possibleMoves(3, 5).size() == 18);
    }
    
    @Test
    public void rookMovesTest1() {
        chessboard.set(3, 4, ChessPiece.WhiteRook);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 11);
    }
    
    @Test
    public void rookMovesTest2() {
        chessboard.set(3, 4, ChessPiece.WhiteRook);
        chessboard.set(4, 4, ChessPiece.WhitePawn);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 7);
    }
    
    @Test
    public void bishopMovesTest1() {
        chessboard.set(3, 4, ChessPiece.WhiteBishop);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 8);
    }
    
    @Test
    public void bishopMovesTest2() {
        chessboard.set(2, 3, ChessPiece.WhitePawn);
        chessboard.set(3, 4, ChessPiece.WhiteBishop);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 5);
    }
    
    @Test
    public void knightMovesTest1() {
        chessboard.set(3, 4, ChessPiece.WhiteKnight);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 6);
    }
    
    @Test
    public void knightMovesTest2() {
        chessboard.set(3, 4, ChessPiece.WhiteKnight);
        chessboard.set(1, 3, ChessPiece.WhitePawn);
        Assert.assertTrue(chessboard.possibleMoves(3, 4).size() == 5);
    }
    
    @Test
    public void knightMovesTest3() {
        chessboard.set(3, 3, ChessPiece.WhiteKnight);
        Assert.assertTrue(chessboard.possibleMoves(3, 3).size() == 8);
    }
    
    @Test
    public void kingSafeTest1() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(4, 2, ChessPiece.BlackRook);
        Assert.assertFalse(chessboard.isKingSafe(Side.White));
    }
    
    @Test
    public void kingSafeTest2() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(7, 5, ChessPiece.BlackQueen);
        Assert.assertFalse(chessboard.isKingSafe(Side.White));
    }
    
    @Test
    public void kingSafeTest3() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(6, 3, ChessPiece.BlackBishop);
        Assert.assertFalse(chessboard.isKingSafe(Side.White));
    }
    
    @Test
    public void kingSafeTest4() {
        chessboard.set(4, 0, ChessPiece.None);
        chessboard.set(4, 2, ChessPiece.BlackKing);
        chessboard.set(6, 2, ChessPiece.WhiteQueen);
        Assert.assertFalse(chessboard.isKingSafe(Side.Black));
    }
    
    @Test
    public void kingSafeTest5() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(6, 4, ChessPiece.BlackQueen);
        Assert.assertTrue(chessboard.possibleMoves(4, 5).size() == 1);
    }
    
    @Test
    public void kingSafeTest6() {
        // checkmate
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(6, 4, ChessPiece.BlackQueen);
        chessboard.set(4, 3, ChessPiece.BlackKnight);
        Assert.assertTrue(chessboard.possibleMoves(4, 5).isEmpty());
    }
    
    @Test
    public void kingSafeTest7() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(1, 2, ChessPiece.WhiteKing);
        Assert.assertTrue(chessboard.possibleMoves(1, 2).size() == 3);
    }
    
    @Test
    public void kingSafeTest8() {
        chessboard.set(4, 7, ChessPiece.None);
        chessboard.set(4, 5, ChessPiece.WhiteKing);
        chessboard.set(5, 3, ChessPiece.BlackPawn);
        Assert.assertTrue(chessboard.possibleMoves(4, 5).size() == 4);
    }
    
    @Test
    public void pawnMovesTest1() {
        Assert.assertTrue(chessboard.possibleMoves(4, 6).size() == 2);
    }
    
    @Test
    public void pawnMovesTest2() {
        Assert.assertTrue(chessboard.possibleMoves(4, 1).size() == 2);
    }
    
    @Test
    public void pawnMovesTest3() {
        chessboard.set(3, 5, ChessPiece.BlackPawn);
        Assert.assertTrue(chessboard.possibleMoves(4, 6).size() == 3);
    }
    
    @Test
    public void pawnMovesTest4() {
        chessboard.set(3, 5, ChessPiece.BlackPawn);
        Assert.assertTrue(chessboard.possibleMoves(3, 6).isEmpty());
    }
    
    @Test
    public void pawnMovesTest5() {
        chessboard.set(3, 5, ChessPiece.BlackPawn);
        chessboard.set(4, 5, ChessPiece.BlackPawn);
        Assert.assertTrue(chessboard.possibleMoves(3, 6).size() == 1);
    }
    
    @Test
    public void pawnPromotionTest1() {
        chessboard.set(2, 6, ChessPiece.BlackPawn);
        chessboard.set(2, 7, ChessPiece.None);
        // 12 possible moves when including the pawn promotions (3 moves * 4 possible promotions each move)
        Assert.assertTrue(chessboard.possibleMoves(2, 6).size() == 12);
    }
    
    @Test
    public void pawnPromotionTest2() {
        chessboard.set(2, 6, ChessPiece.BlackPawn);
        // 8 possible moves when including the pawn promotions (2 moves * 4 possible promotions each move)
        Assert.assertTrue(chessboard.possibleMoves(2, 6).size() == 8);
    }
    
    @Test
    public void pawnPromotionTest3() {
        chessboard.set(2, 1, ChessPiece.WhitePawn);
        // 8 possible moves when including the pawn promotions (2 moves * 4 possible promotions each move)
        Assert.assertTrue(chessboard.possibleMoves(2, 1).size() == 8);
    }
    
    @Test
    public void allPossibleMovesTest1() {
        Assert.assertTrue(chessboard.allPossibleMoves(Side.White).size() == 20);
    }
    
    @Test
    public void allPossibleMovesTest2() {
        Assert.assertTrue(chessboard.allPossibleMoves(Side.Black).size() == 20);
    }
}
