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

public class LaboratorioComputo extends TransformGroup {

    // --- Dimensiones ---
    private static final float ANCHO = 6.5f;
    private static final float PROFUNDO = 8.2f;
    private static final float ALTO = 2.8f;
    private static final float GROSOR_PAREDES = 0.12f;

    // --- Texturas ---
    private String texPared = "ladrillo.png";
    private String texPiso = "piso.png";
    private String texTecho = "ladrillo.png";
    private String texEscritorio = "madera.png";
    private String texMetal = "metal.png";
    
    public LaboratorioComputo() {
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
        
        // --- Pared Frontal (Z=PROFUNDO) - AHORA CON PUERTA A LA DERECHA ---
        float anchoPuerta = 0.9f;
        float altoPuerta = 2.1f;
        float anchoMuroSolido = ANCHO - anchoPuerta; // El resto de la pared será sólida
        
        // Muro sólido a la IZQUIERDA de la puerta
        Figuras.colocarCaja(this, anchoMuroSolido / 2f, centroY, GROSOR_PAREDES / 2f, 
            anchoMuroSolido / 2f, centroY, PROFUNDO, 0, 0, 0, texPared);
            
        // Dintel (viga) sobre el hueco de la puerta (ahora en el lado derecho)
        float alturaDintel = ALTO - altoPuerta;
        Figuras.colocarCaja(this, anchoPuerta / 2f, alturaDintel / 2f, GROSOR_PAREDES / 2f, 
            ANCHO - (anchoPuerta / 2f), altoPuerta + (alturaDintel / 2f), PROFUNDO, 0, 0, 0, texPared);
    }
    
    private void construirMobiliario() {
        // La lógica para amueblar el laboratorio no ha cambiado.
        int filas = 3;
        int columnas = 2;
        float espacioEntreFilas = 2.0f;
        float espacioEntreColumnas = 2.5f;

        float startX = (ANCHO - ((columnas - 1) * espacioEntreColumnas)) / 2.0f;
        float startZ = (PROFUNDO - ((filas - 1) * espacioEntreFilas)) / 2.0f;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                float posX = startX + j * espacioEntreColumnas;
                float posZ = startZ + i * espacioEntreFilas;

                TransformGroup escritorio = crearEscritorioSimple();
                Figuras.moverTG(escritorio, posX, 0, posZ);
                this.addChild(escritorio);

                TransformGroup taburete = crearTaburete();
                Figuras.moverTG(taburete, posX, 0, posZ - 0.5f);
                this.addChild(taburete);

                Computadora computadora = new Computadora();
                float alturaMesa = 0.7f + 0.04f;
                Figuras.moverTG(computadora, posX, alturaMesa, posZ);
                this.addChild(computadora);
            }
        }
    }

    private TransformGroup crearEscritorioSimple() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.5f, 0.04f, 0.4f, 0, 0.7f, 0, 0, 0, 0, texEscritorio);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, -0.45f, 0.35f, 0, 0, 0, 0, texMetal);
        Figuras.colocarCaja(tg, 0.04f, 0.35f, 0.04f, 0.45f, 0.35f, 0, 0, 0, 0, texMetal);
        return tg;
    }

    private TransformGroup crearTaburete() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.2f, 0.04f, 0.2f, 0, 0.5f, 0, 0, 0, 0, texMetal);
        Figuras.colocarCaja(tg, 0.04f, 0.25f, 0.04f, 0, 0.25f, 0, 0, 0, 0, texMetal);
        return tg;
    }
}