/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.Appearance;
import javax.media.j3d.TransformGroup;

/**
 *
 * @author rubenmedina
 */

public class Oficina extends TransformGroup {

    // Dimensiones de la oficina
    private static final float ANCHO = 4.0f;
    private static final float PROFUNDO = 5.0f;
    private static final float ALTO = 2.8f;
    private static final float GROSOR_PAREDES = 0.12f;

    // Dimensiones de la puerta y ventana
    private static final float ANCHO_PUERTA = 0.9f;
    private static final float ALTO_PUERTA = 2.1f;
    private static final float ANCHO_VENTANA = 1.5f;
    private static final float ALTO_VENTANA = 1.0f;
    private static final float ALTURA_BASE_VENTANA = 1.0f;

    // Texturas
    private String texturaPared = "ladrillo.png";
    private String texturaPiso = "piso.png";
    private String texturaTecho = "ladrillo.png";
    private String texturaPizarron = "pizarron.png";
    private String texturaEscritorio = "madera.png";
    private String texturaSilla = "tela.png";
    private String texturaMetal = "metal.png";

    public Oficina() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        construirMobiliario();
    }

    // En Oficina.java

private void construirCuarto() {
    float centroY = ALTO / 2.0f;

    // --- Piso, Techo y Paredes Laterales/Frontal (sin cambios) ---
    Figuras.colocarCaja(this, ANCHO / 2.0f, GROSOR_PAREDES / 2.0f, PROFUNDO / 2.0f, ANCHO / 2.0f, -(GROSOR_PAREDES / 2.0f), PROFUNDO / 2.0f, 0, 0, 0, texturaPiso);
    Figuras.colocarCaja(this, ANCHO / 2.0f, GROSOR_PAREDES / 2.0f, PROFUNDO / 2.0f, ANCHO / 2.0f, ALTO, PROFUNDO / 2.0f, 0, 0, 0, texturaTecho);
    Figuras.colocarCaja(this, ANCHO / 2.0f, centroY, GROSOR_PAREDES / 2.0f, ANCHO / 2.0f, centroY, PROFUNDO, 0, 0, 0, texturaPared); // Frontal
    Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, PROFUNDO / 2.0f, 0, centroY, PROFUNDO / 2.0f, 0, 0, 0, texturaPared); // Izquierda
    Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, PROFUNDO / 2.0f, ANCHO, centroY, PROFUNDO / 2.0f, 0, 0, 0, texturaPared); // Derecha

    // --- Pared Trasera (Z=0) - RECONSTRUIDA ---
    // La construiremos pieza por pieza para asegurar que no haya huecos.
    // Ubicaremos la ventana a la izquierda y la puerta a la derecha de esta pared.

    // 1. Dintel superior (viga sobre todo)
    float alturaDintel = ALTO - ALTO_PUERTA;
    Figuras.colocarCaja(this, ANCHO / 2.0f, alturaDintel / 2.0f, GROSOR_PAREDES / 2.0f,
        ANCHO / 2.0f, ALTO_PUERTA + (alturaDintel / 2.0f), 0, 0, 0, 0, texturaPared);
    
    // 2. Muro bajo la ventana
    Figuras.colocarCaja(this, ANCHO_VENTANA / 2.0f, ALTURA_BASE_VENTANA / 2.0f, GROSOR_PAREDES / 2.0f,
        ANCHO_VENTANA / 2.0f, ALTURA_BASE_VENTANA / 2.0f, 0, 0, 0, 0, texturaPared);
        
    // 3. Pilar entre la ventana y la puerta
    float anchoPilar = ANCHO - ANCHO_VENTANA - ANCHO_PUERTA;
    // La altura de este pilar es la altura de la puerta, ya que va de lado a lado
    Figuras.colocarCaja(this, anchoPilar / 2.0f, ALTO_PUERTA / 2.0f, GROSOR_PAREDES / 2.0f,
        ANCHO_VENTANA + (anchoPilar / 2.0f), ALTO_PUERTA / 2.0f, 0, 0, 0, 0, texturaPared);
}

// En Oficina.java

private void construirMobiliario() {
    // --- Pizarrón en la pared DERECHA (X = ANCHO) ---
    Figuras.colocarCaja(this, 1.2f, 0.8f, 0.02f,
        ANCHO - (GROSOR_PAREDES / 2.0f) - 0.02f,
        1.4f,
        PROFUNDO / 2.0f,
        0, -90, 0,
        texturaPizarron);

    // --- Mobiliario ---

    // ** Escritorio 1 y Silla 1 (pegados a la pared Trasera) **
    
    // Posición y rotación deseadas para la primera estación de trabajo
    float escritorio1_X = 1.5f;
    float escritorio1_Z = 0.6f; // Cerca de la pared trasera (Z=0)
    int escritorio1_RotY = 90; // Rotado para mirar a la pared derecha
    
    EscritorioOficina escritorio1 = new EscritorioOficina(escritorio1_X, 0, escritorio1_Z, escritorio1_RotY);
    this.addChild(escritorio1);

    // La silla se coloca relativa al escritorio
    SillaOficina silla1 = new SillaOficina(-escritorio1_X +2f, 0, escritorio1_Z, escritorio1_RotY);
    this.addChild(silla1);

    // ** Escritorio 2 y Silla 2 (pegados a la pared Frontal) **
    
    // Posición y rotación deseadas para la segunda estación de trabajo
    float escritorio2_X = 1.5f;
    float escritorio2_Z = PROFUNDO - 0.6f; // Cerca de la pared frontal (Z=PROFUNDO)
    int escritorio2_RotY = 90; // También rotado para mirar a la pared derecha
    
    EscritorioOficina escritorio2 = new EscritorioOficina(escritorio2_X, 0, escritorio2_Z, escritorio2_RotY);
    this.addChild(escritorio2);

    // La silla se coloca relativa al escritorio
    SillaOficina silla2 = new SillaOficina(-escritorio2_X +2f, 0, escritorio2_Z, escritorio2_RotY);
    this.addChild(silla2);
}
    
    
}