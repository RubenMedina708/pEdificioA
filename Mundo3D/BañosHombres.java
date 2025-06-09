/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.*;
/**
 *
 * @author rubenmedina
 */

public class BañosHombres extends TransformGroup { 

    // --- Dimensiones (Tamaño de un salón) ---
    private static final float ANCHO = 6.5f;
    private static final float PROFUNDO = 8.2f;
    private static final float ALTO = 2.5f;
    private static final float GROSOR_PAREDES = 0.12f;

    // --- Texturas (Respetando tus nombres) ---
    private String texParedExt = "ladrillo.png";
    private String texPared = "muroBaño.png";
    private String texPiso = "piso.png";
    private String texMueble = "baño.png";
    private String texMetal = "metal.png";
    private String texPuertaCubiculo = "puertaBaño.png";

    public BañosHombres() {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        agregarMobiliario();
    }

    private void construirCuarto() {
        // El origen (0,0,0) es la esquina interior trasera-izquierda del piso del baño.
        float centroY = ALTO / 2.0f;

        // --- Piso y Techo ---
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, -(GROSOR_PAREDES / 2f), PROFUNDO / 2f, 0, 0, 0, texPiso);
        Figuras.colocarCaja(this, ANCHO / 2f, GROSOR_PAREDES / 2f, PROFUNDO / 2f, ANCHO / 2f, ALTO, PROFUNDO / 2f, 0, 0, 0, texPared);

        // --- Paredes ---
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, 0, centroY, PROFUNDO / 2f, 0, 0, 0, texPared); // Izquierda (Sólida)
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2f, centroY, PROFUNDO / 2f, ANCHO, centroY, PROFUNDO / 2f, 0, 0, 0, texPared); // Derecha (Sólida)
        Figuras.colocarCaja(this, ANCHO / 2f, centroY, GROSOR_PAREDES / 2f, ANCHO / 2f, centroY, PROFUNDO, 0, 0, 0, texParedExt); // Frontal (Sólida)

        // --- Pared Trasera (Z=0) con Puerta a la IZQUIERDA ---
        float anchoPuerta = 0.9f;
        float altoPuerta = 2.1f;
        float anchoMuroSolido = ANCHO - anchoPuerta;
        
        // Muro a la DERECHA de la puerta
        Figuras.colocarCaja(this, anchoMuroSolido / 2f, centroY, GROSOR_PAREDES / 2f, anchoPuerta + anchoMuroSolido / 2f, centroY, 0, 0, 0, 0, texParedExt);
        // Dintel sobre la puerta
        float alturaDintel = ALTO - altoPuerta;
        Figuras.colocarCaja(this, anchoPuerta / 2f, alturaDintel / 2f, GROSOR_PAREDES / 2f, anchoPuerta / 2f, altoPuerta + alturaDintel/2f, 0, 0, 0, 0, texParedExt);
    }
    
    private void agregarMobiliario() {
        // --- 1. Lavamanos: Pegados a la pared trasera, lado IZQUIERDO (cerca de la puerta) ---
        // ESTA SECCIÓN NO SE TOCA
        for (int i = 0; i < 4; i++) {
            TransformGroup tgLavabo = crearLavabo();
            float posX = 1.5f + (i * 1.2f); // Empezando desde la izquierda
            float posZ = 0.5f; // Cerca de la pared trasera
            Figuras.moverTG(tgLavabo, posX, 0, posZ);
            this.addChild(tgLavabo);
        }

        // --- 2. Pared de Mingitorios: AHORA EN EL LADO IZQUIERDO ---
        float profParedMingitorios = PROFUNDO - 4.5f; // Mantenemos la profundidad Z
        float posX_ParedMingitorios = 5f; // <-- CAMBIO: Posición X movida a la izquierda
        Figuras.colocarCaja(this, 1.5f, ALTO / 2f, 0.06f, posX_ParedMingitorios, ALTO / 2f, profParedMingitorios, 0, 0, 0, texPared); // Rotación en Y ahora es 0

        // Mingitorios en esa nueva pared, mirando hacia la pared derecha
        for (int i = 0; i < 3; i++) {
            TransformGroup tgMingitorio = crearMingitorio();
            float posX = posX_ParedMingitorios - 0.8f + (i*0.8f);
            float posZ = profParedMingitorios - 0.3f; // En el lado "frontal" de la pared divisoria
            Figuras.moverTG(tgMingitorio, posX, 0, posZ);
            Figuras.rotarTG(tgMingitorio, 0, 0, 0); // Rotación 0 para que miren al frente (+Z)
            this.addChild(tgMingitorio);
        }

        // --- 3. Sanitarios con Cubículos: AHORA EN LA PARED DERECHA ---
        float anchoCubiculo = 1.0f;
        float profundoCubiculo = 1.4f;

        for (int i = 0; i < 3; i++) {
            TransformGroup tgCubiculo = crearCubiculoConSanitario();
            
            // Posición X: Pegado a la pared derecha
            float posX = 4f - (anchoCubiculo/2f); 
            // Posición Z: Distribuidos desde el fondo hacia el frente
            float posZ = PROFUNDO - 0.8f - (i * (profundoCubiculo + 0.1f));
            
            Figuras.moverTG(tgCubiculo, posX, 0, posZ);
            Figuras.rotarTG(tgCubiculo, 0, 90, 0); // Rotar para que la entrada mire a la izquierda
            
            this.addChild(tgCubiculo);
        }
    }
    
    private TransformGroup crearCubiculoConSanitario() {
        TransformGroup tgCubiculo = new TransformGroup();
        float ancho = 1.5f;
        float profundo = 1.4f;
        float altoPared = 2.0f;
        float grosorPared = 0.05f;

        // Pared lateral del cubículo (la que lo separa del siguiente)
        Figuras.colocarCaja(tgCubiculo, grosorPared / 2f, altoPared / 2f, profundo / 2f, -ancho / 2f, altoPared / 2f, profundo / 2f, 0, 0, 0, texPared);
            
        // Sanitario dentro, al fondo
        TransformGroup tgSanitario = crearSanitario();
        Figuras.moverTG(tgSanitario, 0, 0, profundo - 0.4f);
        tgCubiculo.addChild(tgSanitario);

        // Puerta del cubículo
        float anchoPuerta = ancho - (grosorPared * 2);
        TransformGroup tgPuerta = new TransformGroup();
        Figuras.colocarCaja(tgPuerta, anchoPuerta / 2f, (altoPared - 0.1f) / 2f, grosorPared / 2f, 0,0,0, 0, 0, 0, texPuertaCubiculo);
        
        // Rotar la puerta para que mire a la pared más cercana (la frontal del baño)
        Figuras.rotarTG(tgPuerta, 0, 180, 0); 
        Figuras.moverTG(tgPuerta, 0, (altoPared - 0.1f) / 2f, 0);
        tgCubiculo.addChild(tgPuerta);
            
        return tgCubiculo;
    }

    private TransformGroup crearSanitario() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.2f, 0.2f, 0.3f, 0, 0.2f, 0, 0,0,0, texMueble);
        Figuras.colocarCaja(tg, 0.1f, 0.3f, 0.3f, 0, 0.55f, -0.2f, 0,0,0, texMueble);
        return tg;
    }
    
    private TransformGroup crearMingitorio() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.2f, 0.4f, 0.1f, 0, 0.7f, 0, 0,0,0, texMueble);
        return tg;
    }
    
    private TransformGroup crearLavabo() {
        TransformGroup tg = new TransformGroup();
        Figuras.colocarCaja(tg, 0.4f, 0.05f, 0.3f, 0, 0.8f, 0, 0,0,0, texMueble);
        Figuras.colocarCaja(tg, 0.3f, 0.4f, 0.25f, 0, 0.4f, 0, 0,0,0, texMueble);
        Figuras.colocarCaja(tg, 0.05f, 0.1f, 0.02f, 0, 0.9f, -0.15f, 0,0,0, texMetal);
        return tg;
    }
}