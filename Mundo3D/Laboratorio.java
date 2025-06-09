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

public class Laboratorio extends TransformGroup {

    // --- Dimensiones del Laboratorio ---
    private static final float ANCHO = 6.0f;
    private static final float PROFUNDO = 6.0f;
    private static final float ALTO = 2.8f;
    private static final float GROSOR_PAREDES = 0.12f;

    // --- Dimensiones para aberturas ---
    private static final float ANCHO_PUERTA = 0.9f;
    private static final float ALTO_PUERTA = 2.1f;
    private static final float ANCHO_VENTANA = 1.8f;
    private static final float ALTO_VENTANA = 1.2f;
    private static final float ALTURA_BASE_VENTANA = 1.0f;

    // --- Texturas ---
    private String texPared = "ladrillo.png";
    private String texPiso = "piso.png";
    private String texTecho = "ladrillo.png";
    private String texMesa = "metal.png";
    private String texTaburete = "metal.png";
    
    public Laboratorio() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        construirMobiliario();
    }

    private void construirCuarto() {
        float centroY = ALTO / 2.0f;

        // --- Piso y Techo (sin cambios) ---
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, -(GROSOR_PAREDES / 2f), PROFUNDO / 2f, 0, 0, 0, texPiso);
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, ALTO, PROFUNDO / 2f, 0, 0, 0, texTecho);

        // --- Pared Trasera (Z=0) - Sólida ---
        Figuras.colocarCaja(this, ANCHO / 2f, centroY, GROSOR_PAREDES / 2f, ANCHO / 2f, centroY, 0, 0, 0, 0, texPared);

        // --- Pared Izquierda (X=0) - CON VENTANA EN EL EXTREMO TRASERO ---
        float profMuroSolidoIzq = PROFUNDO - ANCHO_VENTANA;
        // Muro sólido en la parte frontal de esta pared
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, profMuroSolidoIzq / 2f, 0, centroY, ANCHO_VENTANA + (profMuroSolidoIzq / 2f), 0, 0, 0, texPared);
        // Dintel sobre la ventana
        float alturaDintelVentana = ALTO - (ALTURA_BASE_VENTANA + ALTO_VENTANA);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, alturaDintelVentana / 2f, ANCHO_VENTANA / 2f, 0, ALTURA_BASE_VENTANA + ALTO_VENTANA + (alturaDintelVentana / 2f), ANCHO_VENTANA / 2f, 0, 0, 0, texPared);
        // Muro bajo la ventana
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, ALTURA_BASE_VENTANA / 2f, ANCHO_VENTANA / 2f, 0, ALTURA_BASE_VENTANA / 2f, ANCHO_VENTANA / 2f, 0, 0, 0, texPared);

        // --- Pared Derecha (X=ANCHO) - CON PUERTA EN EL EXTREMO TRASERO ---
        float profMuroSolidoDer = PROFUNDO - ANCHO_PUERTA;
        // Muro sólido en la parte frontal de esta pared
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, profMuroSolidoDer / 2f, ANCHO, centroY, ANCHO_PUERTA + (profMuroSolidoDer / 2f), 0, 0, 0, texPared);
        // Dintel sobre la puerta
        float alturaDintelPuerta = ALTO - ALTO_PUERTA;
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, alturaDintelPuerta / 2f, ANCHO_PUERTA / 2f, ANCHO, ALTO_PUERTA + (alturaDintelPuerta / 2f), ANCHO_PUERTA / 2f, 0, 0, 0, texPared);
        
        // --- Pared Frontal (Z=PROFUNDO) - CON DOS VENTANAS ---
        // Se construye en 3 partes horizontales para evitar superposiciones
        // 1. Muro debajo de las ventanas (de lado a lado)
        Figuras.colocarCaja(this, ANCHO / 2f, ALTURA_BASE_VENTANA / 2f, GROSOR_PAREDES / 2f, ANCHO / 2f, ALTURA_BASE_VENTANA / 2f, PROFUNDO, 0, 0, 0, texPared);
        // 2. Muro sobre las ventanas (de lado a lado)
        float alturaSobreVentana = ALTO - (ALTURA_BASE_VENTANA + ALTO_VENTANA);
        Figuras.colocarCaja(this, ANCHO / 2f, alturaSobreVentana / 2f, GROSOR_PAREDES / 2f, ANCHO / 2f, ALTURA_BASE_VENTANA + ALTO_VENTANA + (alturaSobreVentana / 2f), PROFUNDO, 0, 0, 0, texPared);
        // 3. Pilares verticales que llenan los espacios entre las ventanas
        float anchoPilar = (ANCHO - (ANCHO_VENTANA * 2)) / 3f; // Ancho de los 3 pilares
        float alturaPilar = ALTO_VENTANA / 2f;
        float posYPilar = ALTURA_BASE_VENTANA + alturaPilar;
        Figuras.colocarCaja(this, anchoPilar / 2f, alturaPilar, GROSOR_PAREDES / 2f, anchoPilar / 2f, posYPilar, PROFUNDO, 0, 0, 0, texPared); // Pilar Izq
        Figuras.colocarCaja(this, anchoPilar / 2f, alturaPilar, GROSOR_PAREDES / 2f, ANCHO / 2f, posYPilar, PROFUNDO, 0, 0, 0, texPared); // Pilar Central
        Figuras.colocarCaja(this, anchoPilar / 2f, alturaPilar, GROSOR_PAREDES / 2f, ANCHO - (anchoPilar / 2f), posYPilar, PROFUNDO, 0, 0, 0, texPared); // Pilar Der
    }
    
    private void construirMobiliario() {
        // La lógica del mobiliario no se modifica
        TransformGroup mesa1 = crearMesaLarga();
        Figuras.moverTG(mesa1, ANCHO / 2.0f, 0, PROFUNDO * 0.3f);
        this.addChild(mesa1);
        TransformGroup mesa2 = crearMesaLarga();
        Figuras.moverTG(mesa2, ANCHO / 2.0f, 0, PROFUNDO * 0.7f);
        this.addChild(mesa2);
        for (int i = 0; i < 3; i++) {
            TransformGroup taburete1a = crearTaburete();
            Figuras.moverTG(taburete1a, 1.5f + i * 1.5f, 0, PROFUNDO * 0.3f - 0.6f);
            this.addChild(taburete1a);
            TransformGroup taburete1b = crearTaburete();
            Figuras.moverTG(taburete1b, 1.5f + i * 1.5f, 0, PROFUNDO * 0.3f + 0.6f);
            Figuras.rotarTG(taburete1b, 0, 180, 0);
            this.addChild(taburete1b);
            TransformGroup taburete2a = crearTaburete();
            Figuras.moverTG(taburete2a, 1.5f + i * 1.5f, 0, PROFUNDO * 0.7f - 0.6f);
            this.addChild(taburete2a);
            TransformGroup taburete2b = crearTaburete();
            Figuras.moverTG(taburete2b, 1.5f + i * 1.5f, 0, PROFUNDO * 0.7f + 0.6f);
            Figuras.rotarTG(taburete2b, 0, 180, 0);
            this.addChild(taburete2b);
        }
    }

    private TransformGroup crearMesaLarga() {
        float anchoMesa = ANCHO * 0.8f;
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, anchoMesa / 2f, 0.05f, 0.5f, 0, 0.9f, 0, 0, 0, 0, texMesa);
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, -(anchoMesa / 2f - 0.1f), 0.45f, -0.4f, 0, 0, 0, texTaburete);
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, anchoMesa / 2f - 0.1f, 0.45f, -0.4f, 0, 0, 0, texTaburete);
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, -(anchoMesa / 2f - 0.1f), 0.45f, 0.4f, 0, 0, 0, texTaburete);
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, anchoMesa / 2f - 0.1f, 0.45f, 0.4f, 0, 0, 0, texTaburete);
        return tg;
    }

    private TransformGroup crearTaburete() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.2f, 0.04f, 0.2f, 0, 0.6f, 0, 0, 0, 0, texTaburete);
        Figuras.colocarCaja(tg, 0.04f, 0.3f, 0.04f, 0, 0.3f, 0, 0, 0, 0, texTaburete);
        return tg;
    }
}