package chessgame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private MainMenuState mainMenu;
    
    public static void main(String[] args) {
        
        AlphaBetaTest test = new AlphaBetaTest();
        ChessMove bestMove = test.calculateBestMove(Side.White, 4);
        
        /*
        Main app = new Main();
        app.start();
        */
    }

    @Override
    public void simpleInitApp() {
        mainMenu = new MainMenuState();
        stateManager.attach(mainMenu);
        
        flyCam.setEnabled(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
