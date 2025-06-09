/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.Appearance;
import javax.media.j3d.TransformGroup;
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
/**
 *
 * @author rubenmedina
 */

public class VentanaDoble extends TransformGroup implements Interaccion {

    private TransformGroup tgPanelIzquierdo;
    private TransformGroup tgPanelDerecho;
    private CajaColision colisionIzquierda;
    private CajaColision colisionDerecha;

    private boolean estaAbierta = false;
    private boolean enAnimacion = false;

     public VentanaDoble(float anchoTotal, float alto, float grosor) {
        float anchoPanel = anchoTotal / 2.0f;

        // --- Panel Izquierdo ---
        tgPanelIzquierdo = new TransformGroup();
        tgPanelIzquierdo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup tgVisualIzquierda = crearPanelDeVentana(anchoPanel, alto, grosor);

        // --- INICIO DE LA CORRECCIÓN ---
        // Movemos la parte visual para que su base esté en Y=0.
        Figuras.moverTG(tgVisualIzquierda, anchoPanel / 2f, alto / 2f, 0);
        // --- FIN DE LA CORRECCIÓN ---
        
        tgPanelIzquierdo.addChild(tgVisualIzquierda);
        this.colisionIzquierda = new CajaColision(tgVisualIzquierda);


        // --- Panel Derecho ---
        tgPanelDerecho = new TransformGroup();
        tgPanelDerecho.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Figuras.moverTG(tgPanelDerecho, anchoTotal, 0, 0);

        TransformGroup tgVisualDerecha = crearPanelDeVentana(anchoPanel, alto, grosor);

        // --- INICIO DE LA CORRECCIÓN ---
        // Hacemos lo mismo para el panel derecho.
        Figuras.moverTG(tgVisualDerecha, -anchoPanel / 2f, alto / 2f, 0);
        // --- FIN DE LA CORRECCIÓN ---

        tgPanelDerecho.addChild(tgVisualDerecha);
        this.colisionDerecha = new CajaColision(tgVisualDerecha);

        this.addChild(tgPanelIzquierdo);
        this.addChild(tgPanelDerecho);
    }
    /**
     * MÉTODO SIMPLIFICADO: Ahora solo construye un panel centrado en su propio origen.
     * La lógica de posicionamiento se mueve al constructor principal.
     */
    private TransformGroup crearPanelDeVentana(float ancho, float alto, float grosor) {
        TransformGroup tgPanel = new TransformGroup();
        // NOTA: Ya no movemos el panel aquí. Se construye en (0,0,0).

        // Crear el marco de la ventana
        Figuras.colocarCaja(tgPanel, ancho / 2f, 0.05f, grosor / 2f, 0, (alto / 2f) - 0.05f, 0, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgPanel, ancho / 2f, 0.05f, grosor / 2f, 0, -(alto / 2f) + 0.05f, 0, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgPanel, 0.05f, alto / 2f, grosor / 2f, (ancho / 2f) - 0.05f, 0, 0, 0, 0, 0, "metal.png");
        Figuras.colocarCaja(tgPanel, 0.05f, alto / 2f, grosor / 2f, -(ancho / 2f) + 0.05f, 0, 0, 0, 0, 0, "metal.png");

        // Crear el panel de cristal
        Appearance aparienciaCristal = Texturas.crearAparienciaCristal();
        TransformGroup tgCristal = Figuras.crearCajaConTextura((ancho / 2f) - 0.05f, (alto / 2f) - 0.05f, (grosor / 2f) * 0.5f, "");

        Box cristalBox = (Box) tgCristal.getChild(0);
        cristalBox.setAppearance(aparienciaCristal);

        tgPanel.addChild(tgCristal);
        return tgPanel;
    }

    @Override
    public void accion() {
        if (enAnimacion) return;
        enAnimacion = true;

        new Thread(() -> {
            try {
                Sonidos.reproducirSonido("ventana.wav");
                int anguloPorFrame = estaAbierta ? 5 : -5;
                for (int i = 0; i < 18; i++) {
                    Figuras.rotarTG(tgPanelIzquierdo, 0, anguloPorFrame, 0);
                    Figuras.rotarTG(tgPanelDerecho, 0, -anguloPorFrame, 0);
                    Thread.sleep(16);
                }
            } catch (InterruptedException e) { e.printStackTrace(); }

            estaAbierta = !estaAbierta;
            colisionIzquierda.habilitada = !estaAbierta;
            colisionDerecha.habilitada = !estaAbierta;
            enAnimacion = false;
        }).start();
    }

    public List<CajaColision> getCajasColision() {
        List<CajaColision> lista = new ArrayList<>();
        lista.add(colisionIzquierda);
        lista.add(colisionDerecha);
        return lista;
    }
}