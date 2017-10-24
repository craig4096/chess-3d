package chessgame;

/**
 *
 * @author craig
 */
public enum ChessPiece {
    Invalid(Side.None),
    None(Side.None),
    WhitePawn(Side.White),
    WhiteRook(Side.White),
    WhiteBishop(Side.White),
    WhiteKnight(Side.White),
    WhiteQueen(Side.White),
    WhiteKing(Side.White),
    BlackPawn(Side.Black),
    BlackRook(Side.Black),
    BlackBishop(Side.Black),
    BlackKnight(Side.Black),
    BlackQueen(Side.Black),
    BlackKing(Side.Black);
    
    private final Side side;
    
    ChessPiece(Side side) {
        this.side = side;
    }
    
    public Side getSide() {
        return this.side;
    }
}
