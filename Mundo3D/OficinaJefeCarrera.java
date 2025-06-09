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

public class OficinaJefeCarrera extends TransformGroup {

    // --- Dimensiones de la Oficina ---
    private static final float ANCHO = 4.0f;
    private static final float PROFUNDO = 6.0f;
    private static final float ALTO = 2.8f;
    private static final float GROSOR_PAREDES = 0.12f;

    // --- Texturas ---
    private String texPared = "ladrillo.png";
    private String texPiso = "piso.png";
    private String texTecho = "ladrillo.png";
    private String texEscritorio = "madera.png";
    private String texSilla = "tela.png";
    private String texMetal = "metal.png";

    public OficinaJefeCarrera() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        construirMobiliarioYDivision();
    }

    private void construirCuarto() {
        // La construcción del cuarto exterior con su puerta y ventana se mantiene igual
        float centroY = ALTO / 2.0f;
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, -(GROSOR_PAREDES / 2f), PROFUNDO / 2f, 0, 0, 0, texPiso);
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, ALTO, PROFUNDO / 2f, 0, 0, 0, texTecho);
        Figuras.colocarCaja(this, ANCHO / 2f, centroY, GROSOR_PAREDES / 2f, ANCHO / 2f, centroY, 0, 0, 0, 0, texPared);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, 0, centroY, PROFUNDO / 2f, 0, 0, 0, texPared);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, ANCHO, centroY, PROFUNDO / 2f, 0, 0, 0, texPared);
        float anchoPuerta = 0.9f;
        float altoPuerta = 2.1f;
        float anchoVentana = 1.8f;
        float anchoPilarMinusculo = 0.1f;
        float anchoMuroIzquierdo = ANCHO - anchoPuerta - anchoPilarMinusculo - anchoVentana;
        if (anchoMuroIzquierdo > 0) {
            Figuras.colocarCaja(this, anchoMuroIzquierdo / 2f, centroY, GROSOR_PAREDES / 2f, anchoMuroIzquierdo / 2f, centroY, PROFUNDO, 0, 0, 0, texPared);
        }
        float pilarX = anchoMuroIzquierdo + anchoPuerta + (anchoPilarMinusculo / 2f);
        Figuras.colocarCaja(this, anchoPilarMinusculo / 2f, centroY, GROSOR_PAREDES / 2f, pilarX, centroY, PROFUNDO, 0, 0, 0, texPared);
        float dintelPuertaX = anchoMuroIzquierdo + (anchoPuerta / 2f);
        float alturaDintel = ALTO - altoPuerta;
        Figuras.colocarCaja(this, anchoPuerta / 2f, alturaDintel / 2f, GROSOR_PAREDES / 2f, dintelPuertaX, altoPuerta + (alturaDintel/2f), PROFUNDO, 0,0,0, texPared);
        float dintelVentanaX = anchoMuroIzquierdo + anchoPuerta + anchoPilarMinusculo + (anchoVentana / 2f);
        float altoVentana = 1.2f;
        float alturaBaseVentana = 1.0f;
        float alturaSobreVentana = ALTO - (alturaBaseVentana + altoVentana);
        Figuras.colocarCaja(this, anchoVentana / 2f, alturaSobreVentana / 2f, GROSOR_PAREDES / 2f, dintelVentanaX, alturaBaseVentana + altoVentana + (alturaSobreVentana/2f), PROFUNDO, 0,0,0, texPared);
        Figuras.colocarCaja(this, anchoVentana / 2f, alturaBaseVentana / 2f, GROSOR_PAREDES / 2f, dintelVentanaX, alturaBaseVentana / 2f, PROFUNDO, 0,0,0, texPared);
    }
    
    private void construirMobiliarioYDivision() {
        // --- INICIO DE LA CORRECCIÓN: Muro Divisor Interno ---
        
        // Dimensiones del muro divisor y su ventana
        float anchoPasillo = 1.2f; // Espacio para pasar desde la puerta
        float anchoMuroDivisor = ANCHO - anchoPasillo;
        float alturaMuroDivisor = ALTO; // Ahora llega hasta el techo
        float posZMedioMuro = PROFUNDO / 2.0f; // Sigue a la mitad de la profundidad
        
        float anchoVentanaDivisor = 2.0f;
        float altoVentanaDivisor = 1.2f;
        float alturaBaseVentanaDivisor = 1.0f;

        // Se construye en 3 partes para dejar el hueco de la ventana
        // 1. Muro bajo la ventana
        Figuras.colocarCaja(this, anchoMuroDivisor / 2f, alturaBaseVentanaDivisor / 2f, GROSOR_PAREDES / 2f,
            anchoPasillo + (anchoMuroDivisor / 2f), alturaBaseVentanaDivisor / 2f, posZMedioMuro, 0, 0, 0, texPared);

        // 2. Muro sobre la ventana
        float alturaSobreVentana = alturaMuroDivisor - (alturaBaseVentanaDivisor + altoVentanaDivisor);
        Figuras.colocarCaja(this, anchoMuroDivisor / 2f, alturaSobreVentana / 2f, GROSOR_PAREDES / 2f,
            anchoPasillo + (anchoMuroDivisor / 2f), alturaBaseVentanaDivisor + altoVentanaDivisor + (alturaSobreVentana/2f), posZMedioMuro, 0, 0, 0, texPared);

        // 3. Pilares a los lados de la ventana
        float anchoPilarDivisor = (anchoMuroDivisor - anchoVentanaDivisor) / 2f;
        float alturaPilarDivisor = altoVentanaDivisor / 2f;
        float posYPilarDivisor = alturaBaseVentanaDivisor + alturaPilarDivisor;
        
        // Pilar izquierdo de la ventana
        Figuras.colocarCaja(this, anchoPilarDivisor / 2f, alturaPilarDivisor, GROSOR_PAREDES / 2f,
            anchoPasillo + (anchoPilarDivisor / 2f), posYPilarDivisor, posZMedioMuro, 0, 0, 0, texPared);
        // Pilar derecho de la ventana
        Figuras.colocarCaja(this, anchoPilarDivisor / 2f, alturaPilarDivisor, GROSOR_PAREDES / 2f,
            ANCHO - (anchoPilarDivisor / 2f), posYPilarDivisor, posZMedioMuro, 0, 0, 0, texPared);

        // --- FIN DE LA CORRECCIÓN ---


        // --- El mobiliario se queda como estaba ---
        TransformGroup escritorioJefe = crearEscritorio();
        Figuras.moverTG(escritorioJefe, ANCHO - 0.7f, 0, 1.0f);
        Figuras.rotarTG(escritorioJefe, 0, 90, 0);
        this.addChild(escritorioJefe);

        TransformGroup sillaJefe = crearSilla();
        Figuras.moverTG(sillaJefe, ANCHO - 1.8f, 0, 1.0f);
        Figuras.rotarTG(sillaJefe, 0, 90, 0);
        this.addChild(sillaJefe);

        TransformGroup escritorioSec = crearEscritorio();
        Figuras.moverTG(escritorioSec, ANCHO - 0.7f, 0, PROFUNDO - 1.5f);
        Figuras.rotarTG(escritorioSec, 0, 90, 0);
        this.addChild(escritorioSec);

        TransformGroup sillaSec = crearSilla();
        Figuras.moverTG(sillaSec, ANCHO - 1.8f, 0, PROFUNDO - 1.5f);
        Figuras.rotarTG(sillaSec, 0, 90, 0);
        this.addChild(sillaSec);
    }
    
    // --- Métodos de Ayuda para crear muebles (sin cambios) ---
    private TransformGroup crearEscritorio() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.6f, 0.04f, 0.4f, 0, 0.7f, 0, 0, 0, 0, texEscritorio);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, -0.55f, 0.35f, -0.35f, 0, 0, 0, texMetal);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, 0.55f, 0.35f, -0.35f, 0, 0, 0, texMetal);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, -0.55f, 0.35f, 0.35f, 0, 0, 0, texMetal);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, 0.55f, 0.35f, 0.35f, 0, 0, 0, texMetal);
        return tg;
    }

    private TransformGroup crearSilla() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.25f, 0.04f, 0.25f, 0, 0.4f, 0, 0, 0, 0, texSilla);
        Figuras.colocarCaja(tg, 0.25f, 0.3f, 0.04f, 0, 0.8f, -0.2f, 0, 0, 0, texSilla);
        Figuras.colocarCaja(tg, 0.02f, 0.2f, 0.02f, 0, 0.2f, 0, 0, 0, 0, texMetal);
        return tg;
    }
}