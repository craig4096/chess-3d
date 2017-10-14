
package chessgame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author craig
 */
public class MainMenuState extends AbstractAppState implements ScreenController {
    
    private Application app = null;
    private NiftyJmeDisplay niftyDisplay;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                app.getAssetManager(),
                app.getInputManager(),
                app.getAudioRenderer(),
                app.getGuiViewPort());
        
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/MainMenu.xml", "mainMenu", this);
        
        app.getGuiViewPort().addProcessor(niftyDisplay);
        app.getInputManager().setCursorVisible(true);
    }
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getGuiViewPort().removeProcessor(niftyDisplay);
        app.getInputManager().setCursorVisible(false);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
    
    public void startGame() {
        System.out.println("Starting new game...");
        
        // Switch to new app state
        GameState newState = new GameState();
        
        this.app.getStateManager().detach(this);
        this.app.getStateManager().attach(newState);
    }
    
    public void quitGame() {
        if(this.app == null) {
            System.out.println("App is null");
        } else {
            this.app.stop();
        }
    }
}
