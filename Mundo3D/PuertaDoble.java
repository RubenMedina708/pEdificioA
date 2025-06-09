/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author rubenmedina
 */

public class PuertaDoble extends TransformGroup implements Interaccion {

    // --- Componentes Clave: Ahora tenemos dos de casi todo ---
    private TransformGroup tgPanelIzquierdo;
    private TransformGroup tgPanelDerecho;
    private CajaColision colisionIzquierda;
    private CajaColision colisionDerecha;

    private boolean estaAbierta = false;
    private boolean enAnimacion = false;

    /**
     * Constructor para una puerta doble que se abre desde el centro.
     * @param anchoTotal El ancho total del hueco de la puerta.
     * @param alto El alto de la puerta.
     * @param grosor El grosor de cada panel de la puerta.
     * @param textura La textura para los paneles.
     */
    public PuertaDoble(float anchoTotal, float alto, float grosor, String textura) {
        // --- Panel Izquierdo ---
        tgPanelIzquierdo = new TransformGroup();
        tgPanelIzquierdo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        float anchoPanel = anchoTotal / 2.0f; // Cada panel es la mitad del ancho total

        // La caja visual del panel izquierdo. Su origen (0,0,0) es su bisagra.
        TransformGroup tgVisualIzquierda = new TransformGroup();
        Figuras.colocarCaja(tgVisualIzquierda, anchoPanel / 2f, alto / 2f, grosor / 2f,
                            anchoPanel / 2f, alto / 2f, 0, // Se mueve a la derecha de su bisagra
                            0, 0, 0, textura);
        tgPanelIzquierdo.addChild(tgVisualIzquierda);
        this.colisionIzquierda = new CajaColision(tgVisualIzquierda);


        // --- Panel Derecho ---
        tgPanelDerecho = new TransformGroup();
        tgPanelDerecho.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // La caja visual del panel derecho. Su origen (0,0,0) es su bisagra.
        TransformGroup tgVisualDerecha = new TransformGroup();
        Figuras.colocarCaja(tgVisualDerecha, anchoPanel / 2f, alto / 2f, grosor / 2f,
                            -anchoPanel / 2f, alto / 2f, 0, // Se mueve a la izquierda de su bisagra
                            0, 0, 0, textura);
        
        // El panel derecho se posiciona a la derecha del izquierdo
        Figuras.moverTG(tgPanelDerecho, anchoTotal, 0, 0);
        tgPanelDerecho.addChild(tgVisualDerecha);
        this.colisionDerecha = new CajaColision(tgVisualDerecha);


        // Añadimos ambos paneles a la puerta principal
        this.addChild(tgPanelIzquierdo);
        this.addChild(tgPanelDerecho);
    }

    @Override
    public void accion() {
        if (enAnimacion) {
            return; // No hacer nada si ya se está moviendo
        }
        enAnimacion = true;

        new Thread(() -> {
            try {
                Sonidos.reproducirSonido("PuertaSonido.wav"); // Puedes usar otro sonido si quieres

                // Determina la dirección de la rotación
                int anguloPorFrame = estaAbierta ? 5 : -5;

                // --- La Magia: Rotar cada panel en dirección opuesta ---
                for (int i = 0; i < 18; i++) { // 18 frames * 5 grados = 90 grados
                    Figuras.rotarTG(tgPanelIzquierdo, 0, anguloPorFrame, 0);      // El izquierdo rota en un sentido
                    Figuras.rotarTG(tgPanelDerecho, 0, -anguloPorFrame, 0);     // El derecho rota en el sentido opuesto
                    Thread.sleep(16);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Actualizar el estado al final de la animación
            estaAbierta = !estaAbierta;
            colisionIzquierda.habilitada = !estaAbierta;
            colisionDerecha.habilitada = !estaAbierta;
            enAnimacion = false;
        }).start();
    }

    /**
     * Devuelve una lista con las dos cajas de colisión de esta puerta.
     */
    public List<CajaColision> getCajasColision() {
        List<CajaColision> lista = new ArrayList<>();
        lista.add(colisionIzquierda);
        lista.add(colisionDerecha);
        return lista;
    }
}
