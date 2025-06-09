/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f;
/**
 *
 * @author rubenmedina
 */

public class Puerta extends TransformGroup implements Interaccion {

    private TransformGroup tgPanel; // El panel de la puerta que rota
    private CajaColision cajaColision;
    private boolean estaAbierta = false;
    private boolean enAnimacion = false;

    /**
     * Constructor de una puerta interactiva.
     * @param cajaColision La caja de colisión que bloqueará el paso cuando la puerta esté cerrada.
     * @param ancho Ancho de la puerta.
     * @param alto Alto de la puerta.
     * @param grosor Grosor de la puerta.
     * @param textura El nombre del archivo de textura para la puerta.
     */
   public Puerta(float ancho, float alto, float grosor, String textura) {
    // El constructor ya no recibe una CajaColision, la crea él mismo.
    
    // El tgPanel es el que realmente rota, como si fuera la bisagra.
    tgPanel = new TransformGroup();
    tgPanel.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    // Creamos la puerta (la caja visual) y la posicionamos para que su eje de rotación
    // esté en uno de sus bordes (en X = -ancho/2), no en el centro.
    TransformGroup tgVisualPuerta = new TransformGroup();
    // ¡OJO! La caja visual ahora es hija de tgVisualPuerta
    Figuras.colocarCaja(tgVisualPuerta, ancho / 2f, alto / 2f, grosor / 2f,
                        ancho / 2f, alto / 2f, 0, // Posición relativa al punto de giro
                        0, 0, 0, textura);

    tgPanel.addChild(tgVisualPuerta);
    this.addChild(tgPanel);

    // ---- LÍNEA CLAVE ----
    // La caja de colisión de la puerta ahora se refiere a la parte visual que se mueve.
    // Se crea aquí, pasando el TransformGroup que contiene la geometría de la puerta.
    this.cajaColision = new CajaColision(tgVisualPuerta); 
}

// Añade este método para poder obtener la caja de colisión desde fuera
public CajaColision getCajaColision() {
    return this.cajaColision;
}


// El método accion() se queda como está, pero asegúrate de que la línea que modifica
// .habilitada esté correcta.
@Override
public void accion() {
    if (enAnimacion) {
        return;
    }
    enAnimacion = true;
    
    new Thread(() -> {
        try {
            Sonidos.reproducirSonido("puertaSonido.wav");

            int anguloPorFrame = estaAbierta ? 5 : -5;

            for (int i = 0; i < 18; i++) {
                Figuras.rotarTG(tgPanel, 0, anguloPorFrame, 0);
                Thread.sleep(16);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        estaAbierta = !estaAbierta;
        cajaColision.habilitada = !estaAbierta; // <-- Esta línea ahora funcionará.
        enAnimacion = false;
    }).start();
}

   
}