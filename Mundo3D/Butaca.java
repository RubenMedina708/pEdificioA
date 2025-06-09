/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 *
 * @author rubenmedina
 */

public class Butaca extends TransformGroup {

    // Texturas a usar
    private String texturaMadera = "metal.png";
    private String texturaMetal = "metal.png";

    public Butaca() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        // --- Silla ---
        // Asiento
        Figuras.colocarCaja(this, 0.25f, 0.05f, 0.25f, 0, 0.5f, 0, 0, 0, 0, texturaMadera);
        // Respaldo
        Figuras.colocarCaja(this, 0.25f, 0.35f, 0.05f, 0, 1.0f, -0.2f, 0, 0, 0, texturaMadera);
        // Patas de la silla
        float alturaPataSilla = 0.25f;
        float xPataSilla = 0.22f;
        float zPataSilla = 0.22f;
        Figuras.colocarCaja(this, 0.05f, alturaPataSilla, 0.05f, -xPataSilla, alturaPataSilla, -zPataSilla, 0, 0, 0, texturaMetal);
        Figuras.colocarCaja(this, 0.05f, alturaPataSilla, 0.05f, xPataSilla, alturaPataSilla, -zPataSilla, 0, 0, 0, texturaMetal);
        Figuras.colocarCaja(this, 0.05f, alturaPataSilla, 0.05f, -xPataSilla, alturaPataSilla, zPataSilla, 0, 0, 0, texturaMetal);
        Figuras.colocarCaja(this, 0.05f, alturaPataSilla, 0.05f, xPataSilla, alturaPataSilla, zPataSilla, 0, 0, 0, texturaMetal);

        // --- Mesa (Paleta) ---
        float alturaPataMesa = 0.7f;
        float grosorPataMesa = 0.05f;
        // Patas de la mesa (la butaca tiene dos patas que sostienen la tabla)
        Figuras.colocarCaja(this, grosorPataMesa, alturaPataMesa, grosorPataMesa, 0.3f, alturaPataMesa, 0.45f, 0, 0, 0, texturaMetal);
        Figuras.colocarCaja(this, grosorPataMesa, alturaPataMesa, 0.05f, -0.3f, alturaPataMesa, 0.45f, 0, 0, 0, texturaMetal);

        // Tabla de la mesa
        float anchoTabla = 0.9f;
        float profundoTabla = 1.0f;
        float grosorTabla = 0.04f;
        float yTabla = alturaPataMesa * 2; // Altura de las patas de la mesa
        Figuras.colocarCaja(this, anchoTabla, grosorTabla, profundoTabla, 0f, yTabla, 0.45f, 0, 0, 0, texturaMadera);

        // Escalar toda la butaca para que tenga un tamaño razonable
        Figuras.escalarTG(this, 0.5);
    }
}
