package chessgame;

/**
 *
 * @author craig
 */
public class ChessMove {
    int x1, x2, y1, y2;
    ChessPiece capturedPiece;
    ChessPiece promotion;
    int score;
    
    public ChessMove() {
        x1 = y1 = x2 = y2 = 0;
        capturedPiece = promotion = ChessPiece.None;
        score = 0;
    }
    
    public ChessMove(int x1, int y1, int x2, int y2, ChessPiece capturedPiece) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.capturedPiece = capturedPiece;
        this.promotion = ChessPiece.None;
        this.score = 0;
    }
    
    public ChessMove(int x1, int y1, int x2, int y2, ChessPiece capturedPiece, ChessPiece promotion) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.capturedPiece = capturedPiece;
        this.promotion = promotion;
        this.score = 0;
    }
}
