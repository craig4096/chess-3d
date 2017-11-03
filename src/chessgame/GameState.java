/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author craig
 */
public class GameState extends AbstractAppState implements ActionListener {
    
    private SimpleApplication app;
    private Chessboard chessboard;
    private Node sceneData, chessboardNode, piecesNode, movesNode;
    
    private enum State {
        NORMAL,
        PLAYER_SELECTED_PIECE,
        AI_CALCULATING_MOVE,
        AI_MOVE_CALCULATED
    }
    
    private State state = State.NORMAL;
    private ChessMove calculatedMove = null;
    
    private static final float CHESSBOARD_SIZE = 4.0f;
    private static final float CHESSPIECE_SIZE = (CHESSBOARD_SIZE / 8.0f);
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        chessboard = new Chessboard();
        
        this.app = (SimpleApplication) app;
        
        // initialise nodes for each different category of display item
        sceneData = new Node();
        chessboardNode = new Node();
        piecesNode = new Node();
        movesNode = new Node();
        
        this.app.getRootNode().attachChild(sceneData);
        
        chessboardNode.attachChild(loadChessBoard());
        sceneData.attachChild(chessboardNode);

        populatePieces();
        sceneData.attachChild(piecesNode);
        sceneData.attachChild(movesNode);
        
        app.getCamera().setLocation(new Vector3f(3.0f, 2.0f, 3.0f));
        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.ZERO);
        
        app.getInputManager().setCursorVisible(true);
        
        app.getInputManager().addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(this, "Select");
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0,-1,0).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        sceneData.addLight(sun);
    }
    
    private Side getCurrentSide() {
        switch(state) {
            case NORMAL:
            case PLAYER_SELECTED_PIECE:
                return Side.White;
            case AI_CALCULATING_MOVE:
            case AI_MOVE_CALCULATED:
                return Side.Black;
        }
        return Side.None;
    }
    
    @Override
    public void update(float tpf) {
        // move has finished being calculated by separate thread
        if(state == State.AI_MOVE_CALCULATED) {
            if(calculatedMove != null) {
                chessboard.makeMove(calculatedMove);
                populatePieces();
                
                state = State.NORMAL;
            } else {
                if(chessboard.isKingSafe(getCurrentSide())) {
                    System.out.println("Stalemate!");
                } else {
                    System.out.println("Checkmate!");
                }
            }
        }
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getRootNode().detachChild(sceneData);
        app.getInputManager().setCursorVisible(false);
        app.getInputManager().deleteMapping("Select");
        app.getInputManager().removeListener(this);
    }
    
    private Spatial loadChessBoard() {
        Spatial board = this.app.getAssetManager().loadModel("Models/board.j3o");
        board.setLocalScale(CHESSPIECE_SIZE / 2.0f, CHESSPIECE_SIZE / 2.0f, CHESSPIECE_SIZE / 2.0f);
        return board;
    }
    
    private void populatePieces() {
        piecesNode.detachAllChildren();
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                ChessPiece piece = chessboard.get(x, y);
                if(piece != ChessPiece.None && piece != ChessPiece.Invalid) {
                    Spatial geom = createChessPiece(piece);
                    
                    final float startPos = -(CHESSBOARD_SIZE / 2.0f) + (CHESSPIECE_SIZE / 2.0f);
                    
                    float xPos = startPos + (x * CHESSPIECE_SIZE);
                    float yPos = startPos + (y * CHESSPIECE_SIZE);
                    
                    geom.setLocalTranslation(xPos, 0, yPos);
                    piecesNode.attachChild(geom);
                }
            }
        }
    }
    
    private Spatial createChessPiece(ChessPiece piece) {
        Spatial obj = null;
        switch(piece) {
            case WhitePawn:
            case BlackPawn:
                obj = this.app.getAssetManager().loadModel("Models/pawn.j3o");
                break;
            case WhiteRook:
            case BlackRook:
                obj = this.app.getAssetManager().loadModel("Models/rook.j3o");
                break;
            case WhiteKnight:
            case BlackKnight:
                obj = this.app.getAssetManager().loadModel("Models/knight.j3o");
                break;
            case WhiteBishop:
            case BlackBishop:
                obj = this.app.getAssetManager().loadModel("Models/bishop.j3o");
                break;
            case WhiteQueen:
            case BlackQueen:
                obj = this.app.getAssetManager().loadModel("Models/queen.j3o");
                break;
            case WhiteKing:
            case BlackKing:
                obj = this.app.getAssetManager().loadModel("Models/king.j3o");
                break;
            default: {
                obj = new Geometry("Piece", new Sphere(10, 10, 1.0f));
                break;
            }
        }
        
        obj.setLocalScale(CHESSPIECE_SIZE / 2.0f, CHESSPIECE_SIZE / 2.0f, CHESSPIECE_SIZE / 2.0f);

        if(piece.getSide() == Side.White) {
            obj.rotate(0, (float)Math.toRadians(180.0), 0);
        }
        
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        if(piece.getSide() == Side.White) {
            mat.setColor("Diffuse", ColorRGBA.White);
        } else {
            mat.setColor("Diffuse", ColorRGBA.DarkGray);
        }
        obj.setMaterial(mat);
        return obj;
    }

    private List<ChessMove> possibleMoves = new ArrayList<>();
    
    /**
     * Filters all moves which have the specified x and y position as their destination
     * @param x
     * @param y
     * @param moves
     * @return 
     */
    private List<ChessMove> filterMoves(int x, int y, List<ChessMove> moves) {
        List<ChessMove> filtered = new ArrayList<ChessMove>();
        for(ChessMove move : moves) {
            if(move.x2 == x && move.y2 == y) {
                filtered.add(move);
            }
        }
        return filtered;
    }
    
    private void updatePossibleMovesDisplay() {
        movesNode.detachAllChildren();
        for(ChessMove move : possibleMoves) {
            
            final float margin = 0.05f;
            
            Box b = new Box((CHESSPIECE_SIZE / 2.0f) - margin, 0.001f, (CHESSPIECE_SIZE / 2.0f) - margin);
            Geometry geom = new Geometry("Move", b);
            
            final float startPos = -(CHESSBOARD_SIZE / 2.0f) + (CHESSPIECE_SIZE / 2.0f);

            float xPos = startPos + (move.x2 * CHESSPIECE_SIZE);
            float zPos = startPos + (move.y2 * CHESSPIECE_SIZE);

            geom.setLocalTranslation(xPos, 0.001f, zPos);
            Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", ColorRGBA.Green);
            geom.setMaterial(mat);
            movesNode.attachChild(geom);
        }
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("Select") && isPressed) {
            Vector2f click2d = this.app.getInputManager().getCursorPosition().clone();
            Vector3f click3d = this.app.getCamera().getWorldCoordinates(click2d, 0.0f).clone();
            Vector3f dir = this.app.getCamera().getWorldCoordinates(click2d, 1.0f).subtractLocal(click3d).normalizeLocal();

            Ray ray = new Ray(click3d, dir);
            CollisionResults results = new CollisionResults();

            chessboardNode.collideWith(ray, results);

            if(results.size() > 0) {
                Vector3f point = results.getCollision(0).getContactPoint();

                int x = (int)Math.floor((point.x + (CHESSBOARD_SIZE / 2.0f)) / CHESSPIECE_SIZE);
                int y = (int)Math.floor((point.z + (CHESSBOARD_SIZE / 2.0f)) / CHESSPIECE_SIZE);
                
                System.out.println("Selected position: [" + x + "][" + y + "]");
                
                if(x >= 0 && x < 8 && y >= 0 && y < 8) {

                    switch(state) {
                        case NORMAL:
                            // board was selected in normal mode - get piece player selected
                            ChessPiece piece = chessboard.get(x, y);
                            // Ensure that we only update the possible moves for the current side
                            if(piece.getSide() == getCurrentSide()) {
                                possibleMoves = chessboard.possibleMoves(x, y);
                                updatePossibleMovesDisplay();
                                state = State.PLAYER_SELECTED_PIECE;
                            }
                            break;
                        case PLAYER_SELECTED_PIECE:
                            // Player has selected a move to make - check first it is a valid move
                            List<ChessMove> moves = filterMoves(x, y, possibleMoves);
                            if(!moves.isEmpty()) {
                                ChessMove moveToMake;
                                if(moves.size() > 1) {
                                    // TODO: if multiple moves then we are dealing with a promotion and must prompt the user
                                    moveToMake = moves.get(0);
                                } else {
                                    moveToMake = moves.get(0);
                                }
                                // make the move
                                chessboard.makeMove(moveToMake);
                                
                                // clear all possible moves and update display
                                possibleMoves.clear();
                                updatePossibleMovesDisplay();
                                
                                // Update pieces on board
                                populatePieces();
                                
                                // Now it is the AI's turn to make a move
                                state = State.AI_CALCULATING_MOVE;
                                startCalculatingMove();

                            } else {
                                // Move is not valid - clear possible moves and reset to NORMAL state
                                possibleMoves.clear();
                                updatePossibleMovesDisplay();
                                
                                state = State.NORMAL;
                            }
                            break;
                    }
                }
            }
        }
    }
    
    private void startCalculatingMove() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GameState.this.calculatedMove = GameState.this.chessboard.calculateBestMove(Side.Black, 0);
                
                GameState.this.state = State.AI_MOVE_CALCULATED;
            }
        }).start();
    }
}
