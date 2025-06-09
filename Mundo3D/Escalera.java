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

public class Escalera extends TransformGroup {

    // --- Dimensiones de la Escalera ---
    private static final float ANCHO_TRAMO = 2.0f;
    private static final float PROFUNDO_DESCANSO = 1.2f;
    private static final float ANCHO_HUECO_CENTRAL = 0.2f;
    private static final float ALTURA_A_SUBIR = 3.0f;
    private static final int NUM_ESCALONES_POR_TRAMO = 8;
    
    private static final float ALTO_ESCALON = (ALTURA_A_SUBIR / 2.0f) / NUM_ESCALONES_POR_TRAMO;
    private static final float PROFUNDO_ESCALON = 0.3f;

    // Dimensiones de las paredes
    private static final float GROSOR_PARED = 0.12f;

    // Texturas
    private String texturaEscalon = "piso.png";
    private String texturaPared = "ladrillo.png";
    
    public Escalera() {
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        construirEscalera();
        construirParedes();
        construirMuroBajoEscalera(); // NUEVO: Llamada para crear la pared inferior
    }

    private void construirEscalera() {
        // ... (Este método se queda exactamente igual que antes) ...
        float anchoTotalEscalera = (ANCHO_TRAMO * 2) + ANCHO_HUECO_CENTRAL;
        float alturaDescanso = ALTURA_A_SUBIR / 2.0f;
        float posZDescanso = (NUM_ESCALONES_POR_TRAMO * PROFUNDO_ESCALON) + (PROFUNDO_DESCANSO / 2.0f);
        
        for (int i = 0; i < NUM_ESCALONES_POR_TRAMO; i++) {
            float posX = ANCHO_TRAMO / 2.0f;
            float posY = (i * ALTO_ESCALON) + (ALTO_ESCALON / 2.0f);
            float posZ = (i * PROFUNDO_ESCALON) + (PROFUNDO_ESCALON / 2.0f);
            Figuras.colocarCaja(this, ANCHO_TRAMO / 2.0f, ALTO_ESCALON / 2.0f, PROFUNDO_ESCALON / 2.0f, posX, posY, posZ, 0, 0, 0, texturaEscalon);
        }
        Figuras.colocarCaja(this, anchoTotalEscalera / 2.0f, ALTO_ESCALON / 2.0f, PROFUNDO_DESCANSO / 2.0f, anchoTotalEscalera / 2.0f, alturaDescanso, posZDescanso, 0, 0, 0, texturaEscalon);
        for (int i = 0; i < NUM_ESCALONES_POR_TRAMO; i++) {
            float posX = anchoTotalEscalera - (ANCHO_TRAMO / 2.0f);
            float posY = alturaDescanso + ((i + 1) * ALTO_ESCALON);
            float posZ = posZDescanso + (PROFUNDO_DESCANSO / 2.0f) - (PROFUNDO_ESCALON / 2.0f) - (i * PROFUNDO_ESCALON);
            Figuras.colocarCaja(this, ANCHO_TRAMO / 2.0f, ALTO_ESCALON / 2.0f, PROFUNDO_ESCALON / 2.0f, posX, posY, posZ, 0, 0, 0, texturaEscalon);
        }
        float alturaLlegada = ALTURA_A_SUBIR;
        float posZLlegada = posZDescanso - (PROFUNDO_DESCANSO / 2.0f) - (NUM_ESCALONES_POR_TRAMO * PROFUNDO_ESCALON);
        Figuras.colocarCaja(this, anchoTotalEscalera / 2.0f, ALTO_ESCALON / 2.0f, PROFUNDO_DESCANSO / 2.0f, anchoTotalEscalera / 2.0f, alturaLlegada, posZLlegada, 0, 0, 0, texturaEscalon);
    }
    
    private void construirParedes() {
        // ... (Este método se queda exactamente igual que antes) ...
        float anchoTotal = (ANCHO_TRAMO * 2) + ANCHO_HUECO_CENTRAL;
        float profundidadTotal = (NUM_ESCALONES_POR_TRAMO * PROFUNDO_ESCALON) + PROFUNDO_DESCANSO;
        float alturaTotalParedes = ALTURA_A_SUBIR + 0.5f;

        // Paredes Laterales
        Figuras.colocarCaja(this, GROSOR_PARED / 2.0f, alturaTotalParedes / 2.0f, profundidadTotal / 2.0f, -(GROSOR_PARED / 2.0f), alturaTotalParedes / 2.0f, profundidadTotal / 2.0f, 0, 0, 0, texturaPared);
        Figuras.colocarCaja(this, GROSOR_PARED / 2.0f, alturaTotalParedes / 2.0f, profundidadTotal / 2.0f, anchoTotal + (GROSOR_PARED / 2.0f), alturaTotalParedes / 2.0f, profundidadTotal / 2.0f, 0, 0, 0, texturaPared);
        
        // Pared Frontal Sólida
        Figuras.colocarCaja(this, anchoTotal / 2.0f, alturaTotalParedes / 2.0f, GROSOR_PARED / 2.0f, anchoTotal / 2.0f, alturaTotalParedes / 2.0f, profundidadTotal + (GROSOR_PARED / 2.0f), 0, 0, 0, texturaPared);

        // Pared Trasera Parcial (Entrada)
        float anchoMuroTrasero = anchoTotal / 2.0f;
        float posX_MuroTrasero = anchoTotal - (anchoMuroTrasero / 2.0f);
        Figuras.colocarCaja(this, anchoMuroTrasero / 2.0f, alturaTotalParedes / 2.0f, GROSOR_PARED / 2.0f, posX_MuroTrasero, alturaTotalParedes / 2.0f, -(GROSOR_PARED / 2.0f), 0, 0, 0, texturaPared);
    }
    
    /**
     * NUEVO MÉTODO: Construye una pared sólida debajo del segundo tramo de escaleras.
     */
    private void construirMuroBajoEscalera() {
        float anchoTotal = (ANCHO_TRAMO * 2) + ANCHO_HUECO_CENTRAL;
        // La altura de este muro debe llegar hasta el primer descanso.
        float alturaMuro = ALTURA_A_SUBIR ;
        // La profundidad debe cubrir la longitud de un tramo de escalones.
        float profundidadMuro = NUM_ESCALONES_POR_TRAMO * PROFUNDO_ESCALON;

        // Posición X: Centrado debajo del segundo tramo de escaleras.
        float posX = anchoTotal - (ANCHO_TRAMO / 2.0f);
        // Posición Y: La mitad de su altura, para que su base esté en Y=0.
        float posY = alturaMuro / 2.0f;
        // Posición Z: Centrado a lo largo de la profundidad del tramo.
        float posZ = profundidadMuro / 2.0f;

        Figuras.colocarCaja(this,
            ANCHO_TRAMO / 2.0f,      // Mitad del ancho del muro (igual al ancho del tramo)
            alturaMuro / 2.0f,       // Mitad de la altura del muro
            profundidadMuro / 2.0f,  // Mitad de la profundidad del muro
            posX, posY, posZ,        // Posición del centro del muro
            0, 0, 0, texturaPared);  // Sin rotación, con la misma textura de pared
    }
}