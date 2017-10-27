package chessgame;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author craig
 */
public class Chessboard {
    
    private ChessPiece[][] pieces = new ChessPiece[8][8];
    
    public Chessboard() {
        this.setDefaultBoard();
    }
    
    /**
     * Sets the board to its original starting state
     */
    public final void setDefaultBoard() {
        // Black side
        this.pieces[0][0] = ChessPiece.BlackRook;
        this.pieces[1][0] = ChessPiece.BlackKnight;
        this.pieces[2][0] = ChessPiece.BlackBishop;
        this.pieces[3][0] = ChessPiece.BlackQueen;
        this.pieces[4][0] = ChessPiece.BlackKing;
        this.pieces[5][0] = ChessPiece.BlackBishop;
        this.pieces[6][0] = ChessPiece.BlackKnight;
        this.pieces[7][0] = ChessPiece.BlackRook;
        
        // Black pawns
        for(int col = 0; col < 8; ++col) {
            this.pieces[col][1] = ChessPiece.BlackPawn;
        }
        
        // Empty spaces
        for(int col = 0; col < 8; ++col) {
            for(int row = 2; row < 6; ++row) {
                this.pieces[col][row] = ChessPiece.None;
            }
        }
        
        // White pawns
        for(int col = 0; col < 8; ++col) {
            this.pieces[col][6] = ChessPiece.WhitePawn;
        }
        
        // White side
        this.pieces[0][7] = ChessPiece.WhiteRook;
        this.pieces[1][7] = ChessPiece.WhiteKnight;
        this.pieces[2][7] = ChessPiece.WhiteBishop;
        this.pieces[3][7] = ChessPiece.WhiteQueen;
        this.pieces[4][7] = ChessPiece.WhiteKing;
        this.pieces[5][7] = ChessPiece.WhiteBishop;
        this.pieces[6][7] = ChessPiece.WhiteKnight;
        this.pieces[7][7] = ChessPiece.WhiteRook;
    }
    
    /**
     * Returns a list of all possible moves for a particular side
     * @param side side to check
     * @return array of all possible moves for side
     */
    public List<ChessMove> allPossibleMoves(Side side) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                if(get(x, y).getSide() == side) {
                    moves.addAll(possibleMoves(x, y));
                }
            }
        }
        return moves;
    }
    
    /**
     * Calculates all potential moves for a particular position on the board
     * @param x column index on board (0 - 7)
     * @param y row index on board (0 - 7)
     * @return array of all possible moves
     */
    public List<ChessMove> possibleMoves(int x, int y) {
        ChessPiece piece = get(x, y);
        switch(piece) {
            case WhitePawn:   return possiblePawnMoves(x, y, Side.White);
            case BlackPawn:   return possiblePawnMoves(x, y, Side.Black);
            case WhiteKnight: return possibleKnightMoves(x, y, Side.White);
            case BlackKnight: return possibleKnightMoves(x, y, Side.Black);
            case WhiteBishop: return possibleBishopMoves(x, y, Side.White);
            case BlackBishop: return possibleBishopMoves(x, y, Side.Black);
            case WhiteRook:   return possibleRookMoves(x, y, Side.White);
            case BlackRook:   return possibleRookMoves(x, y, Side.Black);
            case WhiteQueen:  return possibleQueenMoves(x, y, Side.White);
            case BlackQueen:  return possibleQueenMoves(x, y, Side.Black);
            case WhiteKing:   return possibleKingMoves(x, y, Side.White);
            case BlackKing:   return possibleKingMoves(x, y, Side.Black);
        }
        return new ArrayList<ChessMove>();
    }
    
    /**
     * Makes a move
     * @param move move to make
     */
    public void makeMove(ChessMove move) {
        if(move.promotion != ChessPiece.None) {
            pieces[move.x2][move.y2] = move.promotion;
        } else {
            pieces[move.x2][move.y2] = pieces[move.x1][move.y1];
        }
        pieces[move.x1][move.y1] = ChessPiece.None;
    }
    
    /**
     * Undos a move
     * @param move move to undo
     */
    public void undoMove(ChessMove move) {
        // If the move included a promotion
        if(move.promotion != ChessPiece.None) {
            // demote piece back to a pawn
            if(move.promotion.getSide() == Side.White) {
                pieces[move.x1][move.y1] = ChessPiece.WhitePawn;
            } else {
                pieces[move.x1][move.y1] = ChessPiece.BlackPawn;
            }
        } else {
            pieces[move.x1][move.y1] = pieces[move.x2][move.y2];
        }
        pieces[move.x2][move.y2] = move.capturedPiece;
    }
    
    /**
     * Determines whether the specified x and y position is in the valid chessboard range
     * @param x x position to check
     * @param y y position to check
     * @return valid or invalid position
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
    
    public ChessPiece get(int x, int y) {
        if(isValidPosition(x, y)) {
            return pieces[x][y];
        }
        return ChessPiece.Invalid;
    }
    
    public void set(int x, int y, ChessPiece piece) {
        if(isValidPosition(x, y)) {
            pieces[x][y] = piece;
        }
    }
    
    /**
     * Returns the board position of the king for the given side
     * @param side white or black
     * @return board position of king
     */
    private BoardPosition getKingPosition(Side side) {
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                ChessPiece piece = get(x, y);
                if(piece == ChessPiece.WhiteKing && side == Side.White ||
                   piece == ChessPiece.BlackKing && side == Side.Black)
                {
                    return new BoardPosition(x, y);
                }
            }
        }
        return null;
    }
    
    /**
     * Checks whether the king for the specific side is safe given
     * the current layout of the chessboard
     * @param side white or black
     * @return whether king is safe
     */
    public boolean isKingSafe(Side side) { 
        // Get the king position
        BoardPosition kingPos = getKingPosition(side);
        // Should always have a king on the board
        if(kingPos != null) {
            return !isPositionUnderThreat(kingPos.x, kingPos.y, side);
        } else {
            assert(false);
        }
        return false;
    }
    
    /**
     * Checks whether a specific position is under threat
     * @param x position x to check
     * @param y position y to check
     * @param side side that we are checking if is under threat
     * @return whether position on board can be taken by opposing side or not
     */
    private boolean isPositionUnderThreat(int x, int y, Side side) {
        // Check for Bishop or Queen
        if(checkForBishopOrQueen(x, y, -1, -1, side)) return true;
        if(checkForBishopOrQueen(x, y,  1, -1, side)) return true;
        if(checkForBishopOrQueen(x, y,  1,  1, side)) return true;
        if(checkForBishopOrQueen(x, y, -1,  1, side)) return true;
        
        // Check for Rook or Queen
        if(checkForRookOrQueen(x, y,  0, -1, side)) return true;
        if(checkForRookOrQueen(x, y,  1,  0, side)) return true;
        if(checkForRookOrQueen(x, y,  0,  1, side)) return true;
        if(checkForRookOrQueen(x, y, -1,  0, side)) return true;
        
        // Check for knights
        if(checkForKnight(x, y,  1, -2, side)) return true;
        if(checkForKnight(x, y,  2, -1, side)) return true;
        if(checkForKnight(x, y,  2,  1, side)) return true;
        if(checkForKnight(x, y,  1,  2, side)) return true;
        if(checkForKnight(x, y, -1, -2, side)) return true;
        if(checkForKnight(x, y, -2, -1, side)) return true;
        if(checkForKnight(x, y, -2,  1, side)) return true;
        if(checkForKnight(x, y, -1,  2, side)) return true;
        
        // Check pawns
        if(checkForPawn(x, y, -1, side)) return true;
        if(checkForPawn(x, y,  1, side)) return true;
        
        // Check for opposite king
        if(checkForOpposingKing(x, y, side)) return true;
        
        return false;
    }
    
    private boolean checkForOpposingKing(int x, int y, Side side) {
        for(int cx = x-1; cx <= x+1; ++cx) {
            for(int cy = y-1; cy <= y+1; ++cy) {
                if(cx == 0 && cy == 0) continue;
                
                ChessPiece piece = get(cx, cy);
                if(piece == ChessPiece.BlackKing && side == Side.White ||
                   piece == ChessPiece.WhiteKing && side == Side.Black) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkForPawn(int x, int y, int dx, Side side) {
        int dy = (side == Side.White) ? -1 : 1;
        ChessPiece piece = get(x + dx, y + dy);
        return
           piece == ChessPiece.WhitePawn && side == Side.Black ||
           piece == ChessPiece.BlackPawn && side == Side.White;
    }
    
    private boolean checkForKnight(int x, int y, int dx, int dy, Side side) {
        ChessPiece piece = get(x + dx, y + dy);
        return
           piece == ChessPiece.WhiteKnight && side == Side.Black ||
           piece == ChessPiece.BlackKnight && side == Side.White;
    }

    private boolean checkForBishopOrQueen(int x, int y, int dx, int dy, Side side) {
        ChessPiece piece = getNextNonEmptyPiece(x, y, dx, dy);
        return
           piece == ChessPiece.WhiteBishop && side == Side.Black ||
           piece == ChessPiece.BlackBishop && side == Side.White ||
           piece == ChessPiece.WhiteQueen && side == Side.Black ||
           piece == ChessPiece.BlackQueen && side == Side.White;
    }
    
    private boolean checkForRookOrQueen(int x, int y, int dx, int dy, Side side) {
        ChessPiece piece = getNextNonEmptyPiece(x, y, dx, dy);
        return
           piece == ChessPiece.WhiteRook && side == Side.Black ||
           piece == ChessPiece.BlackRook && side == Side.White ||
           piece == ChessPiece.WhiteQueen && side == Side.Black ||
           piece == ChessPiece.BlackQueen && side == Side.White;
    }
    
    /**
     * Given a starting x and y position, returns the next non-empty chess piece
     * along the direction dx and dy
     * @param x starting position x
     * @param y starting position y
     * @param dx x direction (-1 to 1)
     * @param dy y direction (-1 to 1)
     * @return next non-empty piece
     */
    private ChessPiece getNextNonEmptyPiece(int x, int y, int dx, int dy) {
        int cx = x + dx;
        int cy = y + dy;
        ChessPiece piece;
        do {
            piece = get(cx, cy);
            cx += dx;
            cy += dy; 
        } while(piece == ChessPiece.None);
        return piece;
    }
    
    /**
     * Checks whether making the specified move would compromise the king or not
     * @param move move to check
     * @param side side of king to check
     * @return whether making the move would compromise the king or not
     */
    private boolean wouldMoveCompromiseKing(ChessMove move, Side side) {
        ChessPiece originalPiece = get(move.x1, move.y1);
        set(move.x1, move.y1, ChessPiece.None);
        set(move.x2, move.y2, originalPiece);
        
        boolean compromised = !isKingSafe(side);
        
        set(move.x1, move.y1, originalPiece);
        set(move.x2, move.y2, move.capturedPiece);
        
        return compromised;
    }
    
    /**
     * Calculates all possible moves a pawn can make, including promotions
     * @param x pawn position x
     * @param y pawn position y
     * @param side pawn side
     * @return array of all possible moves
     */
    private List<ChessMove> possiblePawnMoves(int x, int y, Side side) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        checkPawnCaptureMove(x, y, side, -1, moves);
        checkPawnForwardMoves(x, y, side, moves);
        checkPawnCaptureMove(x, y, side,  1, moves);
        return moves;
    }
    
    private boolean isPawnInStartingLocation(int y, Side side) {
        return side == Side.White ? (y == 6) : (y == 1);
    }
    
    private int getPawnMoveDirection(Side side) {
        return side == Side.White ? -1 : 1;
    }
    
    private void checkPawnForwardMoves(int x, int y, Side side, List<ChessMove> moves) {
        int dy = getPawnMoveDirection(side);
        int ty = y + dy;
        ChessPiece piece = get(x, ty);        
        if(piece == ChessPiece.None) { // can only move forward if piece is empty
            for(ChessPiece promotion : getPawnPromotions(ty, side)) {
                ChessMove move = new ChessMove(x, y, x, ty, piece, promotion);
                if(!wouldMoveCompromiseKing(move, side)) {
                    moves.add(move);
                }
            }
            
            // If the pawn is in the starting location we also need to check forward two moves
            if(isPawnInStartingLocation(y, side)) {
                // Check two squares ahead
                int ty2 = y + (dy * 2);
                piece = get(x, ty2);
                if(piece == ChessPiece.None) {
                    ChessMove move = new ChessMove(x, y, x, ty2, piece);
                    if(!wouldMoveCompromiseKing(move, side)) {
                        moves.add(move);
                    }
                }
            }
        }
    }
    
    private void checkPawnCaptureMove(int x, int y, Side side, int dx, List<ChessMove> moves) {
        int dy = getPawnMoveDirection(side);
        int tx = x + dx;
        int ty = y + dy;
        ChessPiece piece = get(tx, ty);
        if(piece != ChessPiece.Invalid && piece != ChessPiece.None && piece.getSide() != side) {
            for(ChessPiece promotion : this.getPawnPromotions(ty, side)) {
                ChessMove move = new ChessMove(x, y, tx, ty, piece, promotion);
                if(!wouldMoveCompromiseKing(move, side)) {
                    moves.add(move);
                }
            }
        }
    }
    
    /**
     * Returns an array of all possible pawn promotions given its current y position
     * on the board
     * @param y pawn y position
     * @param side pawn side
     * @return list of all possible promotions
     */
    private List<ChessPiece> getPawnPromotions(int y, Side side) {
        List<ChessPiece> promotions = new ArrayList<ChessPiece>();
        if(side == Side.White) {
            if(y == 0) {
                promotions.add(ChessPiece.WhiteQueen);
                promotions.add(ChessPiece.WhiteRook);
                promotions.add(ChessPiece.WhiteBishop);
                promotions.add(ChessPiece.WhiteKnight);
            } else {
                promotions.add(ChessPiece.None);
            }
        } else if(side == Side.Black) {
            if(y == 7) {
                promotions.add(ChessPiece.BlackQueen);
                promotions.add(ChessPiece.BlackRook);
                promotions.add(ChessPiece.BlackBishop);
                promotions.add(ChessPiece.BlackKnight);
            } else  {
                promotions.add(ChessPiece.None);
            }
        }
        return promotions;
    }
    
    /**
     * Calculates all possible moves a Knight can make
     * @param x knight position x
     * @param y knight position y
     * @param side knight side (white or black)
     * @return list of all possible moves
     */
    private List<ChessMove> possibleKnightMoves(int x, int y, Side side) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        checkKnightMove(x, y, side,  1, -2, moves);
        checkKnightMove(x, y, side,  2, -1, moves);
        checkKnightMove(x, y, side,  2,  1, moves);
        checkKnightMove(x, y, side,  1,  2, moves);
        checkKnightMove(x, y, side, -1, -2, moves);
        checkKnightMove(x, y, side, -2, -1, moves);
        checkKnightMove(x, y, side, -2,  1, moves);
        checkKnightMove(x, y, side, -1,  2, moves);
        return moves;
    }
    
    /**
     * Used by the {@link #possibleKingMoves(int, int, chessgame.Side)} to check possible knight
     * moves at specific offsets and add them to the output moves array
     * @param x knight position x
     * @param y knight position y
     * @param side knight side (white or black)
     * @param dx offset x
     * @param dy offset y
     * @param moves (output) array of moves to add potential move to
     */
    private void checkKnightMove(int x, int y, Side side, int dx, int dy, List<ChessMove> moves) {
        int tx = x + dx;
        int ty = y + dy;
        ChessPiece piece = get(tx, ty);
        if(piece != ChessPiece.Invalid && piece.getSide() != side) {
            ChessMove move = new ChessMove(x, y, tx, ty, piece);
            if(!wouldMoveCompromiseKing(move, side)) {
                moves.add(move);
            }
        }
    }
    
    /**
     * Calculates all possible moves a Bishop can make
     * @param x bishop position x
     * @param y bishop position y
     * @param side bishop side (white or black)
     * @return array of all possible moves
     */
    private List<ChessMove> possibleBishopMoves(int x, int y, Side side) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        calcMovesInDirection(x, y, side, -1, -1, moves); // top-left
        calcMovesInDirection(x, y, side,  1, -1, moves); // top-right
        calcMovesInDirection(x, y, side, -1,  1, moves); // bottom-left
        calcMovesInDirection(x, y, side,  1,  1, moves); // bottom-right
        return moves;
    }
    
    /**
     * Calculates all possible moves a Rook can make
     * @param x rook position x
     * @param y rook position y
     * @param side rook side (white or black)
     * @return array of all possible moves
     */
    private ArrayList<ChessMove> possibleRookMoves(int x, int y, Side side) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        calcMovesInDirection(x, y, side,  0, -1, moves); // up
        calcMovesInDirection(x, y, side, -1,  0, moves); // left
        calcMovesInDirection(x, y, side,  1,  0, moves); // right
        calcMovesInDirection(x, y, side,  0,  1, moves); // down
        return moves;
    }
    
    /**
     * Calculates all possible moves the queen can make from the specified position
     * @param x x position on board
     * @param y y position on board
     * @param side white or black
     * @return array of all valid moves the queen can make
     */
    private ArrayList<ChessMove> possibleQueenMoves(int x, int y, Side side) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        calcMovesInDirection(x, y, side, -1, -1, moves); // top-left
        calcMovesInDirection(x, y, side,  0, -1, moves); // up
        calcMovesInDirection(x, y, side,  1, -1, moves); // top-right
        calcMovesInDirection(x, y, side, -1,  0, moves); // left
        calcMovesInDirection(x, y, side,  1,  0, moves); // right
        calcMovesInDirection(x, y, side, -1,  1, moves); // bottom-left
        calcMovesInDirection(x, y, side,  0,  1, moves); // down
        calcMovesInDirection(x, y, side,  1,  1, moves); // bottom-right
        return moves;
    }
    
    /**
     * For Queen, Bishop and Rooks - calculated all potential moves for a specific direction
     * @param x starting x position
     * @param y starting y position
     * @param side white or black
     * @param dx direction x (-1 to 1)
     * @param dy direction y (-1 to 1)
     * @param moves (output) moves array
     */
    private void calcMovesInDirection(int x, int y, Side side, int dx, int dy, List<ChessMove> moves) {
        // walk along the direction vector, checking cells as we go
        for(int cx = x + dx, cy = y + dy;; cx += dx, cy += dy) {
            ChessPiece piece = get(cx, cy);
            if(piece == ChessPiece.Invalid || piece.getSide() == side) {
                break;
            }

            ChessMove move = new ChessMove(x, y, cx, cy, piece);
            if(!wouldMoveCompromiseKing(move, side)) {
                moves.add(move);
            }

            // If we hit a piece which is not on the same side we cannot
            // go any further
            if(piece != ChessPiece.None && piece.getSide() != side) {
                break;
            }
        }
    }
    
    /**
     * Calculates all possible moves a king can make from the specified position
     * @param x board column position
     * @param y board row position
     * @param side white or black
     * @return array of all possible moves the king can make
     */
    private List<ChessMove> possibleKingMoves(int x, int y, Side side) {
        List<ChessMove> moves = new ArrayList<ChessMove>();
        for(int i = -1; i < 2; ++i) {
            for(int j = -1; j < 2; ++j) {
                if(i == 0 && j == 0) {
                    continue;
                }
                
                final int mx = x + i;
                final int my = y + j;
                
                ChessPiece piece = get(mx, my);
                if(piece != ChessPiece.Invalid && piece.getSide() != side) {
                    
                    ChessMove move = new ChessMove(x, y, mx, my, piece);
                    // Add the move only if it doesn't compromise the king
                    if(!wouldMoveCompromiseKing(move, side)) {
                        moves.add(move);
                    }
                }
            }
        }
        return moves;
    }
    
    public ChessMove calculateBestMove(Side side, int depth) {
        return maxAlphaBeta(side, 0, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    private ChessMove minAlphaBeta(Side side, int depth, final int targetDepth, int alpha, int beta) {
        System.out.println("Entering min node, depth: " + depth);
        ChessMove minMove = null;
        // Iterate over all possible moves
        List<ChessMove> possibleMoves = allPossibleMoves(side == Side.White ? Side.Black : Side.White);
        for(ChessMove move : possibleMoves) {
            makeMove(move);
            int rating;
            // if we have reached the desired depth
            if(depth == (targetDepth - 1)) {
                rating = getRating(side);
            } else {
                // Find the max move for the opposite side
                ChessMove maxMove = maxAlphaBeta(side, depth + 1, targetDepth, alpha, beta);
                if(maxMove != null) {
                    rating = maxMove.score;
                } else {
                    // If no moves available it means either a checkmate / stalemate position or
                    // the all move ratings are below 'alpha', therefore we just set rating to alpha
                    rating = alpha;
                }
            }
            if(rating < beta) {
                beta = rating;
                minMove = move;
                minMove.score = beta;
            }
            undoMove(move);
            
            // If alpha is greater than beta, we know we can't find a better
            // solution so just return beta
            if(alpha >= beta) {
                break;
            }
        }
        System.out.println("Exiting min node");
        return minMove;
    }
    
    private ChessMove maxAlphaBeta(Side side, int depth, final int targetDepth, int alpha, int beta) {
        System.out.println("Entering max node, depth: " + depth);
        ChessMove maxMove = null;
        // Iterate over all possible moves
        List<ChessMove> possibleMoves = allPossibleMoves(side);
        for(ChessMove move : possibleMoves) {
            makeMove(move);
            int rating;
            // if we have reached the desired depth
            if(depth == (targetDepth - 1)) {
                rating = getRating(side);
            } else {
                // Find the min move for the opposite side
                ChessMove minMove = minAlphaBeta(side, depth + 1, targetDepth, alpha, beta);
                if(minMove != null) {
                    rating = minMove.score;
                } else {
                    // If no moves available it means either a checkmate / stalemate position or
                    // the all move ratings are above 'beta', therefore we just set rating to beta
                    rating = beta;
                }
            }
            if(rating > alpha) {
                alpha = rating;
                maxMove = move;
                maxMove.score = alpha;
            }
            undoMove(move);
            
            // If alpha is greater than beta, we know we can't find a better
            // solution so just return beta
            if(alpha >= beta) {
                break;
            }
        }
        System.out.println("Exiting max node");
        return maxMove;
    }
    
    /**
     * Calculates a board rating for a particular side
     * @param side white or black
     * @return rating value
     */
    public int getRating(Side side) {
        return 0;
    }
}
