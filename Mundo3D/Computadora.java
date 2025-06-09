/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
/**
 *
 * @author rubenmedina
 */

public class Computadora extends TransformGroup {

    // Texturas
    private String texPantalla = "pantalla.png";
    private String texCarcasa = "metal.png";

    public Computadora() {
        // Monitor (pantalla)
        Figuras.colocarCaja(this, 0.3f, 0.2f, 0.02f, 0, 0.25f, 0, 0, 0, 0, texCarcasa);
        // La pantalla en sí, un poco al frente
        Figuras.colocarCaja(this, 0.28f, 0.18f, 0.01f, 0, 0.25f, 0.02f, 0, 0, 0, texPantalla);
        
        // Base del monitor
        Figuras.colocarCaja(this, 0.1f, 0.02f, 0.1f, 0, 0.04f, 0, 0, 0, 0, texCarcasa);
        // Soporte vertical
        Figuras.colocarCaja(this, 0.02f, 0.05f, 0.02f, 0, 0.09f, 0, 0, 0, 0, texCarcasa);
    }
}
