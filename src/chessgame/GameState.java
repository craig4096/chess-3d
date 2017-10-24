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

/**
 *
 * @author craig
 */
public class GameState extends AbstractAppState implements ActionListener {
    
    private SimpleApplication app;
    private Chessboard chessboard;
    private Node sceneData, chessboardNode;
    
    private static final float CHESSBOARD_SIZE = 4.0f;
    private static final float CHESSPIECE_SIZE = (CHESSBOARD_SIZE / 8.0f);
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        chessboard = new Chessboard();
        
        this.app = (SimpleApplication) app;
        
        sceneData = new Node();
        this.app.getRootNode().attachChild(sceneData);
       
        chessboardNode = new Node();
        chessboardNode.attachChild(loadChessBoard());
        sceneData.attachChild(chessboardNode);
        
        populatePieces();
        
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
    
    private Geometry loadChessBoard() {
        Box b = new Box(CHESSBOARD_SIZE / 2.0f, 0.1f, CHESSBOARD_SIZE / 2.0f);
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
        for(int x = 0; x < 8; ++x) {
            for(int y = 0; y < 8; ++y) {
                ChessPiece piece = chessboard.get(x, y);
                if(piece != ChessPiece.None && piece != ChessPiece.Invalid) {
                    Geometry geom = createChessPiece(piece);
                    
                    final float startPos = -(CHESSBOARD_SIZE / 2.0f) + (CHESSPIECE_SIZE / 2.0f);
                    
                    float xPos = startPos + (x * CHESSPIECE_SIZE);
                    float yPos = startPos + (y * CHESSPIECE_SIZE);
                    
                    geom.setLocalTranslation(xPos, 0, yPos);
                    sceneData.attachChild(geom);
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
                
                double x = Math.floor((point.x + (CHESSBOARD_SIZE / 2.0f)) / CHESSPIECE_SIZE);
                double y = Math.floor((point.z + (CHESSBOARD_SIZE / 2.0f)) / CHESSPIECE_SIZE);
                
                System.out.println("Selected position: [" + x + "][" + y + "]");
            }
        }
    }
    
}
