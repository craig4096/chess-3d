/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgame;

/**
 *
 * @author craig
 */
public enum Side {
    White,
    Black,
    None;
    
    public Side opposite() {
        switch(this) {
            case White: return Black;
            case Black: return White;
        }
        return None;
    }
}
