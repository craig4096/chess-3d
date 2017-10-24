/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector3f;

/**
 *
 * @author craig
 */
public class RotationCamState extends AbstractAppState implements AnalogListener, ActionListener {
    
    private Application app;
    
    private float yaw = 0.0f, pitch = 0.0f;
    private float distance = 3.0f;
    private float speed = 2.0f;
    private boolean dragging = false;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.app = app;
        
        app.getInputManager().addMapping("DragDown", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
        app.getInputManager().addMapping("RotateX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        app.getInputManager().addMapping("RotateXNeg", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        app.getInputManager().addMapping("RotateY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        app.getInputManager().addMapping("RotateYNeg", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        
        app.getInputManager().addMapping("ZoomIn", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        app.getInputManager().addMapping("ZoomOut", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        
        app.getInputManager().addListener(this, "ZoomIn", "ZoomOut", "DragDown", "RotateX", "RotateXNeg", "RotateY", "RotateYNeg");
    }
    
    @Override
    public void update(float tpf) {
        float dirX = (float)Math.sin(yaw);
        float dirZ = (float)Math.cos(yaw);
        
        float sinPitch = (float)Math.sin(pitch);
        float posX = sinPitch * dirX;
        float posZ = sinPitch * dirZ;
        float posY = (float)Math.cos(pitch);

        this.app.getCamera().setLocation(new Vector3f(posX * distance, posY * distance, posZ * distance));
        
        this.app.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }
    
    @Override
    public void cleanup() {
        
        this.app.getInputManager().removeListener(this);
        
        this.app.getInputManager().deleteMapping("RotateX");
        this.app.getInputManager().deleteMapping("RotateXNeg");
        this.app.getInputManager().deleteMapping("RotateY");
        this.app.getInputManager().deleteMapping("RotateYNeg");
        
        this.app.getInputManager().deleteMapping("DragDown");
        
        this.app.getInputManager().deleteMapping("ZoomIn");
        this.app.getInputManager().deleteMapping("ZoomOut");
        
        super.cleanup();
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if(dragging) {
            switch(name) {
                case "RotateX":
                    yaw -= (value * speed);
                    break;
                case "RotateXNeg":
                    yaw += (value * speed);
                    break;
                case "RotateY":
                    pitch -= (value * speed);
                    break;
                case "RotateYNeg":
                    pitch += (value * speed);
                    break;
            }
        } else {
            switch(name) {
                case "ZoomOut":
                    distance -= (value * 1.0f);
                    break;
                case "ZoomIn":
                    distance += (value * 1.0f);
                    break;
            }
        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(name.equals("DragDown")) {
            dragging = isPressed;
        }
    }
}
