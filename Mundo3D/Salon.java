/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Box;
import javax.media.j3d.Appearance;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TransformGroup;
import java.util.ArrayList;

/**
 *
 * @author rubenmedina
 */

class Salon extends TransformGroup {

    public enum Orientacion {
        FRENTE,
        DERECHA,
        IZQUIERDA
    }

    // Dimensiones internas del salón
    private static final float ANCHO = 6.5f;
    private static final float PROFUNDO = 8.2f;
    private static final float ALTO = 2.4f;
    private static final float GROSOR_PAREDES = 0.12f;
    private static final float ANCHO_PUERTA = 0.9f;
    private static final float ALTO_PUERTA = 2.0f;

    // --- NUEVO: Dimensiones para los huecos de las ventanas traseras ---
    private static final float ANCHO_VENTANA_TRASERA = 2.0f;
    private static final float ALTO_VENTANA_TRASERA = 1.2f;
    private static final float ALTURA_BASE_VENTANA_TRASERA = 1.0f;

    private ArrayList<CajaColision> cajasColisionSalon;
    private String texturaParedes = "ladrillo.png";
    private String texturaPiso = "ladrillo.png";
    private String texturaTecho = "ladrillo.png";
    private String texturaPizarron = "pizarron.png";

    public Salon(Orientacion orientacion) {
        this.cajasColisionSalon = new ArrayList<>();
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        // MODIFICADO: Se pasa la orientación para construir el cuarto correctamente
        construirCuarto(orientacion);
        colocarPizarron(orientacion);
        amueblarSalon(orientacion);
    }

    // MODIFICADO: El método ahora recibe la 'orientacion' para construir la pared trasera
    private void construirCuarto(Orientacion orientacion) {
        float medioAncho = ANCHO / 2.0f;
        float medioProfundo = PROFUNDO / 2.0f;
        float centroY = ALTO / 2.0f;

        // Piso, Techo, Paredes laterales y frontal (no cambian)
        Figuras.colocarCaja(this, medioAncho, GROSOR_PAREDES / 2.0f, medioProfundo, 0, -(GROSOR_PAREDES / 2.0f), 0, 0, 0, 0, texturaPiso);
        Figuras.colocarCaja(this, medioAncho, GROSOR_PAREDES / 2.0f, medioProfundo, 0, ALTO, 0, 0, 0, 0, texturaTecho);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, medioProfundo, -medioAncho, centroY, 0, 0, 0, 0, texturaParedes); // Pared Izquierda
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, medioProfundo, medioAncho, centroY, 0, 0, 0, 0, texturaParedes); // Pared Derecha
        
        // Pared Frontal (con puerta)
        float anchoLateralPuerta = (ANCHO - ANCHO_PUERTA) / 2.0f;
        Figuras.colocarCaja(this, anchoLateralPuerta / 2.0f, centroY, GROSOR_PAREDES / 2.0f, -medioAncho + (anchoLateralPuerta / 2.0f), centroY, medioProfundo, 0, 0, 0, texturaParedes);
        Figuras.colocarCaja(this, anchoLateralPuerta / 2.0f, centroY, GROSOR_PAREDES / 2.0f, medioAncho - (anchoLateralPuerta / 2.0f), centroY, medioProfundo, 0, 0, 0, texturaParedes);
        float alturaDintel = ALTO - ALTO_PUERTA;
        Figuras.colocarCaja(this, ANCHO_PUERTA / 2.0f, alturaDintel / 2.0f, GROSOR_PAREDES / 2.0f, 0, ALTO_PUERTA + (alturaDintel / 2.0f), medioProfundo, 0, 0, 0, texturaParedes);

        // --- INICIO DE LA LÓGICA MODIFICADA PARA LA PARED TRASERA ---
        if (orientacion == Orientacion.FRENTE) {
            // Si la orientación es FRENTE, la pared trasera es sólida para el pizarrón.
            Figuras.colocarCaja(this, medioAncho, centroY, GROSOR_PAREDES / 2.0f, 0, centroY, -medioProfundo, 0, 0, 0, texturaParedes);
        
        } else {
            // Si la orientación es DERECHA o IZQUIERDA, construimos la pared con 2 huecos.
            // La pared se construye en 3 partes horizontales para dejar los huecos.
            
            // 1. Muro bajo las ventanas (de lado a lado)
            Figuras.colocarCaja(this, medioAncho, ALTURA_BASE_VENTANA_TRASERA / 2.0f, GROSOR_PAREDES / 2.0f, 
                                0, ALTURA_BASE_VENTANA_TRASERA / 2.0f, -medioProfundo, 
                                0, 0, 0, texturaParedes);

            // 2. Muro sobre las ventanas (dintel de lado a lado)
            float alturaSobreVentana = ALTO - (ALTURA_BASE_VENTANA_TRASERA + ALTO_VENTANA_TRASERA);
            Figuras.colocarCaja(this, medioAncho, alturaSobreVentana / 2.0f, GROSOR_PAREDES / 2.0f,
                                0, ALTURA_BASE_VENTANA_TRASERA + ALTO_VENTANA_TRASERA + (alturaSobreVentana / 2.0f), -medioProfundo,
                                0, 0, 0, texturaParedes);
            
            // 3. Pilares verticales entre y alrededor de las ventanas
            float anchoPilar = (ANCHO - (ANCHO_VENTANA_TRASERA * 2)) / 3.0f;
            float alturaPilar = ALTO_VENTANA_TRASERA / 2.0f;
            float posYPilar = ALTURA_BASE_VENTANA_TRASERA + alturaPilar;

            // Pilar Izquierdo
            Figuras.colocarCaja(this, anchoPilar / 2.0f, alturaPilar, GROSOR_PAREDES / 2.0f,
                                -medioAncho + (anchoPilar / 2.0f), posYPilar, -medioProfundo,
                                0, 0, 0, texturaParedes);
            // Pilar Central
            Figuras.colocarCaja(this, anchoPilar / 2.0f, alturaPilar, GROSOR_PAREDES / 2.0f,
                                0, posYPilar, -medioProfundo,
                                0, 0, 0, texturaParedes);
            // Pilar Derecho
            Figuras.colocarCaja(this, anchoPilar / 2.0f, alturaPilar, GROSOR_PAREDES / 2.0f,
                                medioAncho - (anchoPilar / 2.0f), posYPilar, -medioProfundo,
                                0, 0, 0, texturaParedes);
        }
        // --- FIN DE LA LÓGICA MODIFICADA ---
    }

    private void colocarPizarron(Orientacion orientacion) {
        float anchoPizarron = 3.0f;
        float altoPizarron = 1.2f;
        float grosorPizarron = 0.05f;
        TransformGroup tgPizarron = Figuras.crearCajaConTextura(anchoPizarron / 2.0f, altoPizarron / 2.0f, grosorPizarron / 2.0f, texturaPizarron);
        float offsetSuperficiePared = GROSOR_PAREDES / 2.0f;
        float posCentroPizarronX = (ANCHO / 10.0f) - offsetSuperficiePared - (grosorPizarron / 2.0f);
        float posCentroPizarronZ = (PROFUNDO / 2.0f) - offsetSuperficiePared - (grosorPizarron / 2.0f);
        switch (orientacion) {
            case DERECHA:
                Figuras.rotarTG(tgPizarron, 0, -90, 0);
                Figuras.moverTG(tgPizarron, posCentroPizarronX, 1.2f, -3);
                break;
            case IZQUIERDA:
                Figuras.rotarTG(tgPizarron, 0, 90, 0);
                Figuras.moverTG(tgPizarron, -posCentroPizarronX, 1.2f, -3);
                break;
            case FRENTE:
            default:
                Figuras.moverTG(tgPizarron, 0, 1.2f, -posCentroPizarronZ);
                break;
        }
        this.addChild(tgPizarron);
    }

    private void amueblarSalon(Orientacion orientacion) {
        int filas, columnas;
        float espacioEntreFilas, espacioEntreColumnas;
        float rotacionButaca;
        if (orientacion == Orientacion.FRENTE) {
            filas = 3;
            columnas = 3;
            espacioEntreFilas = 2.5f;
            espacioEntreColumnas = 2.5f;
            rotacionButaca = 180;
        } else {
            filas = 3;
            columnas = 3;
            espacioEntreFilas = 2.5f;
            espacioEntreColumnas = 2.5f;
            rotacionButaca = (orientacion == Orientacion.DERECHA) ? -90 : 90;
        }
        float anchoTotalMuebles, profTotalMuebles;
        if (orientacion == Orientacion.FRENTE) {
            anchoTotalMuebles = (columnas - 1) * espacioEntreColumnas;
            profTotalMuebles = (filas - 1) * espacioEntreFilas;
        } else {
            anchoTotalMuebles = (filas - 1) * espacioEntreFilas;
            profTotalMuebles = (columnas - 1) * espacioEntreColumnas;
        }
        float startX = -(anchoTotalMuebles / 2.0f);
        float startZ = -(profTotalMuebles / 2.0f);
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                float posX = 0, posZ = 0;
                if (orientacion == Orientacion.FRENTE) {
                    posX = startX + j * espacioEntreColumnas;
                    posZ = startZ + i * espacioEntreFilas;
                } else {
                    posX = startX + i * espacioEntreFilas;
                    posZ = startZ + j * espacioEntreColumnas;
                }
                Butaca butaca = new Butaca();
                Figuras.rotarTG(butaca, 0, (int) rotacionButaca, 0);
                Figuras.moverTG(butaca, posX, 0, posZ);
                this.addChild(butaca);
            }
        }
    }

    public ArrayList<CajaColision> getCajasColision() {
        return this.cajasColisionSalon;
    }
}