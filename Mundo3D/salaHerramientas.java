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

public class salaHerramientas extends TransformGroup {

    // --- Dimensiones (Menos profundo) ---
    private static final float ANCHO = 5.0f;
    private static final float PROFUNDO = 4.0f;
    private static final float ALTO = 2.8f;
    private static final float GROSOR_PAREDES = 0.12f;

    // --- Texturas ---
    private String texPared = "ladrillo.png";
    private String texPiso = "piso.png";
    private String texTecho = "ladrillo.png";
    private String texMesa = "metal.png";
    private String texPizarron = "herramientas.png";
    
    public salaHerramientas() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        construirMobiliario();
    }

    private void construirCuarto() {
        // Origen (0,0,0) en la esquina interior trasera-izquierda del piso.
        float centroY = ALTO / 2.0f;

        // --- Piso, Techo y Paredes Sólidas (Trasera, Izquierda, Derecha) ---
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, -(GROSOR_PAREDES / 2f), PROFUNDO / 2f, 0, 0, 0, texPiso);
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, ALTO, PROFUNDO / 2f, 0, 0, 0, texTecho);
        Figuras.colocarCaja(this, ANCHO / 2f, centroY, GROSOR_PAREDES / 2f, ANCHO / 2f, centroY, 0, 0, 0, 0, texPared); // Trasera
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, 0, centroY, PROFUNDO / 2f, 0, 0, 0, texPared); // Izquierda
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, ANCHO, centroY, PROFUNDO / 2f, 0, 0, 0, texPared); // Derecha
        
        // --- Pared Frontal (Z=PROFUNDO) con Puerta a la DERECHA ---
        float anchoPuerta = 0.9f;
        float altoPuerta = 2.1f;
        float anchoMuroSolido = ANCHO - anchoPuerta;
        
        // Muro sólido a la IZQUIERDA de la puerta
        Figuras.colocarCaja(this, anchoMuroSolido / 2f, centroY, GROSOR_PAREDES / 2f, 
            anchoMuroSolido / 2f, centroY, PROFUNDO, 0, 0, 0, texPared);
            
        // Dintel sobre la puerta (ahora en el lado derecho)
        float alturaDintel = ALTO - altoPuerta;
        Figuras.colocarCaja(this, anchoPuerta / 2f, alturaDintel / 2f, GROSOR_PAREDES / 2f, 
            ANCHO - (anchoPuerta / 2f), altoPuerta + alturaDintel / 2f, PROFUNDO, 0, 0, 0, texPared);
    }
    
    private void construirMobiliario() {
        // --- Pizarrón en la pared trasera ---
        Figuras.colocarCaja(this, 1.5f, 0.8f, 0.02f,
            ANCHO / 2f, 1.5f, GROSOR_PAREDES, 0, 0, 0, texPizarron);

        // --- Mesa Larga en el centro ---
        TransformGroup mesa = crearMesaLarga();
        Figuras.moverTG(mesa, ANCHO / 2.0f, 0, PROFUNDO / 2.0f);
        this.addChild(mesa);
    }

    private TransformGroup crearMesaLarga() {
        TransformGroup tg = new TransformGroup();
        float anchoMesa = ANCHO * 0.8f;
        float profundoMesa = 1.0f;
        // Tabla de la mesa
        Figuras.colocarCaja(tg, anchoMesa / 2f, 0.05f, profundoMesa / 2f, 0, 0.9f, 0, 0, 0, 0, texMesa);
        // Patas
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, -(anchoMesa / 2f - 0.1f), 0.45f, -(profundoMesa / 2f - 0.1f), 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, anchoMesa / 2f - 0.1f, 0.45f, -(profundoMesa / 2f - 0.1f), 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, -(anchoMesa / 2f - 0.1f), 0.45f, profundoMesa / 2f - 0.1f, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tg, 0.05f, 0.45f, 0.05f, anchoMesa / 2f - 0.1f, 0.45f, profundoMesa / 2f - 0.1f, 0, 0, 0, "metal.png");
        return tg;
    }
}