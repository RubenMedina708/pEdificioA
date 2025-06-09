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

public class EscritorioOficina extends TransformGroup {

    /**
     * Constructor para un escritorio que se puede posicionar y rotar.
     * @param x Posición en el eje X (izquierda/derecha).
     * @param y Posición en el eje Y (arriba/abajo).
     * @param z Posición en el eje Z (adelante/atrás).
     * @param rotY Grados de rotación en el eje Y.
     */
    public EscritorioOficina(float x, float y, float z, int rotY) {
        // 1. Crear un TransformGroup para el escritorio completo.
        // Este TG controlará la posición y rotación de todo el mueble.
        TransformGroup tgEscritorio = new TransformGroup();
        Figuras.moverTG(tgEscritorio, x, y, z);
        Figuras.rotarTG(tgEscritorio, 0, rotY, 0);

        // 2. Construir las partes del escritorio (tabla y patas).
        // Todas las partes se añaden a tgEscritorio, por lo que heredarán su posición y rotación.
        // Sus posiciones ahora son RELATIVAS al centro del escritorio (0,0,0).
        
        // Tabla del escritorio
        Figuras.colocarCaja(tgEscritorio, 0.6f, 0.04f, 0.4f, 0, 0.7f, 0, 0, 0, 0, "madera.png");
        
        // Patas
        Figuras.colocarCaja(tgEscritorio, 0.04f, 0.35f, 0.04f, -0.55f, 0.35f, -0.35f, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgEscritorio, 0.04f, 0.35f, 0.04f, 0.55f, 0.35f, -0.35f, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgEscritorio, 0.04f, 0.35f, 0.04f, -0.55f, 0.35f, 0.35f, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgEscritorio, 0.04f, 0.35f, 0.04f, 0.55f, 0.35f, 0.35f, 0, 0, 0, "metal.png");
        
        // 3. Añadir el escritorio ya posicionado y rotado a 'this' (la instancia de la clase)
        this.addChild(tgEscritorio);
    }
}