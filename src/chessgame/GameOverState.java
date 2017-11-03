/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author craig
 */
public class GameOverState extends AbstractAppState implements ScreenController {

    private Application app = null;
    private NiftyJmeDisplay niftyDisplay;
    private boolean win, checkmate;
    private TextRenderer statusText;
    
    public GameOverState(boolean win, boolean checkmate) {
        this.win = win;
        this.checkmate = checkmate;
    }
    
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
        nifty.fromXml("Interface/GameOver.xml", "gameOver", this);
        
        app.getGuiViewPort().addProcessor(niftyDisplay);
        
        Screen screen = nifty.getScreen("gameOver");
        assert(screen != null);
        
        Element element = screen.findElementById("statusText");
        assert(element != null);
        
        statusText = element.getRenderer(TextRenderer.class);
        
        if(this.win) {
            if(this.checkmate) {
                statusText.setText("Checkmate! you win");
            } else {
                statusText.setText("Stalemate! you win");
            }
        } else {
            if(this.checkmate) {
                statusText.setText("Checkmate! opponent wins");
            } else {
                statusText.setText("Stalemate! opponent wins");
            }
        }
    }
    
    @Override
    public void update(float tpf) {
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        app.getGuiViewPort().removeProcessor(niftyDisplay);
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
    
    public void playAgain() {
        app.getStateManager().detach(app.getStateManager().getState(GameState.class));
        app.getStateManager().detach(this);
        
        app.getStateManager().attach(new GameState());
    }
    
    public void mainMenu() {
        app.getStateManager().detach(app.getStateManager().getState(GameState.class));
        app.getStateManager().detach(this);
        
        app.getStateManager().attach(new MainMenuState());
    }
    
}
