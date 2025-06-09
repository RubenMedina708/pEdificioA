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


public class SillaOficina extends TransformGroup {

    /**
     * Constructor para una silla que se puede posicionar y rotar.
     * @param x Posición en el eje X (izquierda/derecha).
     * @param y Posición en el eje Y (arriba/abajo).
     * @param z Posición en el eje Z (adelante/atrás).
     * @param rotY Grados de rotación en el eje Y.
     */
    public SillaOficina(float x, float y, float z, int rotY) {
        // 1. Crear un TG para la silla completa y aplicar su posición/rotación global
        TransformGroup tgSilla = new TransformGroup();
        Figuras.moverTG(tgSilla, x, y, z);
        Figuras.rotarTG(tgSilla, 0, rotY, 0);

        // 2. Construir las partes de la silla (asiento, respaldo, patas)
        // Sus posiciones ahora son RELATIVAS al centro de la silla (0,0,0).
        
        // Asiento
        Figuras.colocarCaja(tgSilla, 0.25f, 0.04f, 0.25f, 0, 0.4f, 0, 0, 0, 0, "tela.png");
        // Respaldo
        Figuras.colocarCaja(tgSilla, 0.25f, 0.3f, 0.04f, 0, 0.8f, -0.2f, 0, 0, 0, "tela.png");
        // Base de 4 patas
        Figuras.colocarCaja(tgSilla, 0.02f, 0.2f, 0.02f, 0, 0.2f, 0, 0, 0, 0, "metal.png");
        
        // 3. Añadir la silla ya posicionada y rotada a 'this'
        this.addChild(tgSilla);
    }
}