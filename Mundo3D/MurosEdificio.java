/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author rubenmedina
 */

public class MurosEdificio {

    private TransformGroup tgMuros;
    private ArrayList<CajaColision> cajasColisionMuros;

    // --- Dimensiones Fijas para esta estructura ---
    private static final float ANCHO_TOTAL_EDIFICIO = 45.0f;
    private static final float PROFUNDIDAD_TOTAL_EDIFICIO = 20.0f;
    private static final float ALTURA_MUROS = 0.0f;
    private static final float GROSOR_MUROS = 0.2f;

    private static final float ANCHO_PUERTA_FRONTAL = 1.0f;
    private static final float ALTURA_PUERTA_FRONTAL = 2.0f;
    private static final float POS_X_PUERTA_FRONTAL = 0.0f;

    private static final float ANCHO_PUERTA_TRASERA = 6.53f;
    private static final float ALTURA_PUERTA_TRASERA = 2.0f;
    private static final float POS_X_PUERTA_TRASERA = 0.0f;
    
    private String texturaExteriorNombre;

    // Constructor que SOLO toma la textura, ya que las dimensiones son fijas
    public MurosEdificio(String texturaExt) { // <--- ¡ESTE ES EL CONSTRUCTOR CORRECTO!
        this.texturaExteriorNombre = texturaExt;

        tgMuros = new TransformGroup();
        tgMuros.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        cajasColisionMuros = new ArrayList<>();
        construirMuros();
    }

    // El método construirMuros() usaría las constantes de la clase:
    // ANCHO_TOTAL_EDIFICIO, ALTURA_MUROS, this.texturaExteriorNombre, etc.
    // (El cuerpo de construirMuros que te di antes es correcto con estas constantes)
    private void construirMuros() {
        Appearance aparienciaMuro = Texturas.crearApariencia(this.texturaExteriorNombre);

        float medioAncho = ANCHO_TOTAL_EDIFICIO / 2.0f;
        float medioProfundidad = PROFUNDIDAD_TOTAL_EDIFICIO / 2.0f;
        float centroYMuro = ALTURA_MUROS / 2.0f;

        // --- MURO FRONTAL ---
        float anchoLateralPFrontal = (ANCHO_TOTAL_EDIFICIO - ANCHO_PUERTA_FRONTAL) / 2.0f;
        if (anchoLateralPFrontal > 0.001f) {
            TransformGroup muro = Figuras.colocarCaja(tgMuros, anchoLateralPFrontal / 2.0f, ALTURA_MUROS / 2.0f, GROSOR_MUROS / 2.0f,
                                          -medioAncho + (anchoLateralPFrontal / 2.0f), centroYMuro, medioProfundidad,
                                          0, 0, 0, this.texturaExteriorNombre);
// 2. Ahora creamos la caja de colisión pasándole el muro que acabamos de crear.
cajasColisionMuros.add(new CajaColision(muro)); // <-- LÍNEA CORREGIDA
        }
        float alturaDintelPFrontal = ALTURA_MUROS - ALTURA_PUERTA_FRONTAL;
        if (alturaDintelPFrontal > 0.001f) {
            Figuras.colocarCaja(tgMuros, ANCHO_PUERTA_FRONTAL / 2.0f, alturaDintelPFrontal / 2.0f, GROSOR_MUROS / 2.0f,
                                POS_X_PUERTA_FRONTAL, ALTURA_PUERTA_FRONTAL + (alturaDintelPFrontal / 2.0f), medioProfundidad,
                                0, 0, 0, this.texturaExteriorNombre);
          
        }
        if (anchoLateralPFrontal > 0.001f) {
            Figuras.colocarCaja(tgMuros, anchoLateralPFrontal / 2.0f, ALTURA_MUROS / 2.0f, GROSOR_MUROS / 2.0f,
                                medioAncho - (anchoLateralPFrontal / 2.0f), centroYMuro, medioProfundidad,
                                0, 0, 0, this.texturaExteriorNombre);
            
        }

        // --- MURO TRASERO ---
        float anchoLateralPTrasera = (ANCHO_TOTAL_EDIFICIO - ANCHO_PUERTA_TRASERA) / 2.0f;
        if (anchoLateralPTrasera > 0.001f) {
            Figuras.colocarCaja(tgMuros, anchoLateralPTrasera / 2.0f, ALTURA_MUROS / 2.0f, GROSOR_MUROS / 2.0f,
                                -medioAncho + (anchoLateralPTrasera / 2.0f), centroYMuro, -medioProfundidad,
                                0, 0, 0, this.texturaExteriorNombre);
             
        }
        float alturaDintelPTrasera = ALTURA_MUROS - ALTURA_PUERTA_TRASERA;
        if (alturaDintelPTrasera > 0.001f) {
            Figuras.colocarCaja(tgMuros, ANCHO_PUERTA_TRASERA / 2.0f, alturaDintelPTrasera / 2.0f, GROSOR_MUROS / 2.0f,
                                POS_X_PUERTA_TRASERA, ALTURA_PUERTA_TRASERA + (alturaDintelPTrasera / 2.0f), -medioProfundidad,
                                0, 0, 0, this.texturaExteriorNombre);
            
        }
        if (anchoLateralPTrasera > 0.001f) {
            Figuras.colocarCaja(tgMuros, anchoLateralPTrasera / 2.0f, ALTURA_MUROS / 2.0f, GROSOR_MUROS / 2.0f,
                                medioAncho - (anchoLateralPTrasera / 2.0f), centroYMuro, -medioProfundidad,
                                0, 0, 0, this.texturaExteriorNombre);
          
        }

        // --- MURO IZQUIERDO ---
        Figuras.colocarCaja(tgMuros, GROSOR_MUROS / 2.0f, ALTURA_MUROS / 2.0f, PROFUNDIDAD_TOTAL_EDIFICIO / 2.0f,
                            -medioAncho, centroYMuro, 0.0f,
                            0, 0, 0, this.texturaExteriorNombre);
        

        // --- MURO DERECHO ---
        Figuras.colocarCaja(tgMuros, GROSOR_MUROS / 2.0f, ALTURA_MUROS / 2.0f, PROFUNDIDAD_TOTAL_EDIFICIO / 2.0f,
                            medioAncho, centroYMuro, 0.0f,
                            0, 0, 0, this.texturaExteriorNombre);
        
    }

    public TransformGroup getTransformGroup() { return tgMuros; }
    public ArrayList<CajaColision> getCajasColision() { return cajasColisionMuros; }

    public void setPosicion(float x, float y, float z) {
        Transform3D t3dExistente = new Transform3D();
        tgMuros.getTransform(t3dExistente);
        javax.vecmath.Vector3f nuevaPosicion = new javax.vecmath.Vector3f(x, y, z);
        t3dExistente.setTranslation(nuevaPosicion);
        tgMuros.setTransform(t3dExistente);
    }
}