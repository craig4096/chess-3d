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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.scene.Node;
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
    private Side whosMove = Side.White;
    
    private static final float CHESSBOARD_SIZE = 4.0f;
    private static final float CHESSPIECE_SIZE = (CHESSBOARD_SIZE / 8.0f);
    private static final float CHESSBOARD_THICKNESS = 0.1f;
    
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
        sceneData.attachChild(this.loadIndicator());
        sceneData.attachChild(movesNode);
        
        app.getCamera().setLocation(new Vector3f(3.0f, 2.0f, 3.0f));
        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.ZERO);
        
        app.getInputManager().setCursorVisible(true);
        
        app.getInputManager().addMapping("Select", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        app.getInputManager().addListener(this, "Select");
    }
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getRootNode().detachChild(sceneData);
        app.getInputManager().setCursorVisible(false);
        app.getInputManager().deleteMapping("Select");
        app.getInputManager().removeListener(this);
    }
    
    // Debugging only
    private Geometry loadIndicator() {
        Sphere b = new Sphere(10, 10, CHESSBOARD_SIZE / 16.0f);
        Geometry geom = new Geometry("Box", b);
        geom.setLocalTranslation(-6.0f, 0, -6.0f);
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Yellow);
        geom.setMaterial(mat);
        return geom;
    }
    
    private Geometry loadChessBoard() {
        Box b = new Box(CHESSBOARD_SIZE / 2.0f, CHESSBOARD_THICKNESS, CHESSBOARD_SIZE / 2.0f);
        Geometry geom = new Geometry("Chessboard", b);
        Material mat = new Material(this.app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = this.app.getAssetManager().loadTexture("Interface/Chessboard.png");
        tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        return geom;
    }
    
    private void populatePieces() {
        piecesNode.detachAllChildren();
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                ChessPiece piece = chessboard.get(x, y);
                if(piece != ChessPiece.None && piece != ChessPiece.Invalid) {
                    Geometry geom = createChessPiece(piece);
                    
                    final float startPos = -(CHESSBOARD_SIZE / 2.0f) + (CHESSPIECE_SIZE / 2.0f);
                    
                    float xPos = startPos + (x * CHESSPIECE_SIZE);
                    float yPos = startPos + (y * CHESSPIECE_SIZE);
                    
                    geom.setLocalTranslation(xPos, 0, yPos);
                    piecesNode.attachChild(geom);
                }
            }
        }
    }
    
    private Geometry createChessPiece(ChessPiece piece) {
        Sphere b = new Sphere(10, 10, CHESSBOARD_SIZE / 16.0f);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(this.app.getAssetManager(),
          "Common/MatDefs/Misc/Unshaded.j3md");
        if(piece.getSide() == Side.White) {
            mat.setColor("Color", ColorRGBA.Blue);
        } else {
            mat.setColor("Color", ColorRGBA.Red);
        }
        geom.setMaterial(mat);
        return geom;
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
            
            Box b = new Box((CHESSPIECE_SIZE / 2.0f) - margin, 0.01f, (CHESSPIECE_SIZE / 2.0f) - margin);
            Geometry geom = new Geometry("Move", b);
            
            final float startPos = -(CHESSBOARD_SIZE / 2.0f) + (CHESSPIECE_SIZE / 2.0f);

            float xPos = startPos + (move.x2 * CHESSPIECE_SIZE);
            float zPos = startPos + (move.y2 * CHESSPIECE_SIZE);

            geom.setLocalTranslation(xPos, CHESSBOARD_THICKNESS + 0.001f, zPos);
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
                
                // Get all possible moves the player can make when selecting this position (usually will
                // be either one or zero, but can be more due to promotions)
                List<ChessMove> moves = filterMoves(x, y, possibleMoves);
                if(moves.isEmpty()) {
                    // If the moves are empty then we re-calculate as the user has possibly selected a
                    // new piece on the board or cancelled the operation by selecting an empty piece
                    
                    ChessPiece piece = chessboard.get(x, y);
                    // Ensure that we only update the possible moves for the current side
                    if(piece.getSide() == whosMove) {
                        possibleMoves = chessboard.possibleMoves(x, y);
                        updatePossibleMovesDisplay();
                    } else {
                        possibleMoves.clear();
                        updatePossibleMovesDisplay();
                    }
                } else {
                    // User has selected a valid move
                    
                    // TODO: if multiple moves then we are dealing with a promotion and must prompt the user
                    chessboard.makeMove(moves.get(0));
                    
                    // switch to opposing side now that a move has been made
                    whosMove = (whosMove == Side.White) ? Side.Black : Side.White;

                    // Clear possible moves and update display
                    possibleMoves.clear();
                    updatePossibleMovesDisplay();
                    
                    // update pieces on board
                    populatePieces();
                    
                    // now make a move for the opposing side (AI)
                    ChessMove bestMove = chessboard.calculateBestMove(whosMove, 3);
                    if(bestMove != null) {
                        chessboard.makeMove(bestMove);
                        populatePieces();
                        
                        // switch to opposing side now that a move has been made
                        whosMove = (whosMove == Side.White) ? Side.Black : Side.White;
                    }
                }
                System.out.println("Selected position: [" + x + "][" + y + "]");
            }
        }
    }
    
}
