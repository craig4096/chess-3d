package chessgame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public Main() {
        super(new MainMenuState());
    }
    
    public static void main(String[] args) {
        
        /*
        AlphaBetaTest test = new AlphaBetaTest();
        ChessMove bestMove = test.calculateBestMove(Side.White, 4);
        */
        
        Main app = new Main();
        
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Chess");
        settings.setSettingsDialogImage("");
        
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
