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

public class Salon extends TransformGroup {

    // Enum para definir la orientación interna del salón
    public enum Orientacion {
    FRENTE, // Pizarrón en la pared del fondo (hacia Z negativa)
    DERECHA, // Pizarrón en la pared derecha (hacia X positiva)
    IZQUIERDA // Pizarrón en la pared izquierda (hacia X negativa)
    }

    // Dimensiones internas del salón
    private static final float ANCHO = 6.5f;
    private static final float PROFUNDO = 8.2f;
    private static final float ALTO = 2.4f;
    private static final float GROSOR_PAREDES = 0.12f;
    private static final float ANCHO_PUERTA = 0.9f;
    private static final float ALTO_PUERTA = 2.0f;

    private ArrayList<CajaColision> cajasColisionSalon;

    // Texturas
    private String texturaParedes = "ladrillo.png";
    private String texturaPiso = "ladrillo.png";
    private String texturaTecho = "ladrillo.png";
    private String texturaPizarron = "pizarron.png";

    public Salon(Orientacion orientacion) {
        this.cajasColisionSalon = new ArrayList<>();
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        colocarPizarron(orientacion);
        amueblarSalon(orientacion);
    }

    private void construirCuarto() {
        // La construcción del cuarto (paredes, piso, techo) se mantiene igual
        float medioAncho = ANCHO / 2.0f;
        float medioProfundo = PROFUNDO / 2.0f;
        float centroY = ALTO / 2.0f;

        Figuras.colocarCaja(this, medioAncho, GROSOR_PAREDES / 2.0f, medioProfundo, 0, -(GROSOR_PAREDES / 2.0f), 0, 0, 0, 0, texturaPiso);
        Figuras.colocarCaja(this, medioAncho, GROSOR_PAREDES / 2.0f, medioProfundo, 0, ALTO, 0, 0, 0, 0, texturaTecho);
        Figuras.colocarCaja(this, medioAncho, centroY, GROSOR_PAREDES / 2.0f, 0, centroY, -medioProfundo, 0, 0, 0, texturaParedes);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, medioProfundo, -medioAncho, centroY, 0, 0, 0, 0, texturaParedes);
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, medioProfundo, medioAncho, centroY, 0, 0, 0, 0, texturaParedes);
        
        float anchoLateralPuerta = (ANCHO - ANCHO_PUERTA) / 2.0f;
        Figuras.colocarCaja(this, anchoLateralPuerta / 2.0f, centroY, GROSOR_PAREDES / 2.0f, -medioAncho + (anchoLateralPuerta / 2.0f), centroY, medioProfundo, 0, 0, 0, texturaParedes);
        Figuras.colocarCaja(this, anchoLateralPuerta / 2.0f, centroY, GROSOR_PAREDES / 2.0f, medioAncho - (anchoLateralPuerta / 2.0f), centroY, medioProfundo, 0, 0, 0, texturaParedes);
        float alturaDintel = ALTO - ALTO_PUERTA;
        Figuras.colocarCaja(this, ANCHO_PUERTA / 2.0f, alturaDintel / 2.0f, GROSOR_PAREDES / 2.0f, 0, ALTO_PUERTA + (alturaDintel / 2.0f), medioProfundo, 0, 0, 0, texturaParedes);
    }

    private void colocarPizarron(Orientacion orientacion) {
        float anchoPizarron = 3.0f;
        float altoPizarron = 1.2f;
        float grosorPizarron = 0.05f;

        TransformGroup tgPizarron = Figuras.crearCajaConTextura(anchoPizarron / 2.0f, altoPizarron / 2.0f, grosorPizarron / 2.0f, texturaPizarron);

        // La superficie INTERIOR de las paredes está a medio grosor del borde.
        float offsetSuperficiePared = GROSOR_PAREDES / 2.0f;
        // El pizarrón debe pegarse a esa superficie, así que su centro estará a medio grosor del pizarrón de distancia.
        float posCentroPizarronX = (ANCHO / 10.0f) - offsetSuperficiePared - (grosorPizarron / 2.0f);
        float posCentroPizarronZ = (PROFUNDO / 2.0f) - offsetSuperficiePared - (grosorPizarron / 2.0f);

        switch (orientacion) {
            case DERECHA:
                Figuras.rotarTG(tgPizarron, 0, -90, 0);
                Figuras.moverTG(tgPizarron, posCentroPizarronX, 1.2f, -3); // Pegado a la pared derecha
                break;
            case IZQUIERDA:
                Figuras.rotarTG(tgPizarron, 0, 90, 0);
                Figuras.moverTG(tgPizarron, -posCentroPizarronX, 1.2f, -3); // Pegado a la pared izquierda
                break;
            case FRENTE:
            default:
                Figuras.moverTG(tgPizarron, 0, 1.2f, -posCentroPizarronZ); // Pegado a la pared del fondo
                break;
        }
        this.addChild(tgPizarron);
    }

    private void amueblarSalon(Orientacion orientacion) {
        int filas, columnas;
        float espacioEntreFilas, espacioEntreColumnas;
        float rotacionButaca;

        if (orientacion == Orientacion.FRENTE) {
            filas = 3; columnas = 3;
            espacioEntreFilas = 2.5f; espacioEntreColumnas = 2.5f;
            rotacionButaca = 180; // Mirar hacia -Z (hacia el pizarrón)
        } else { // Para DERECHA e IZQUIERDA
            filas = 3; columnas = 3; // Ajustamos para que quepan mejor
            espacioEntreFilas = 2.5f; espacioEntreColumnas = 2.5f;
            rotacionButaca = (orientacion == Orientacion.DERECHA) ? -90 : 90; // Mirar hacia +X o -X
        }

        // **INICIO DE LÓGICA DE CENTRADO CORREGIDA**
        float anchoTotalMuebles, profTotalMuebles;
        // La disposición de la cuadrícula cambia si la orientación es lateral
        if (orientacion == Orientacion.FRENTE) {
            anchoTotalMuebles = (columnas - 1) * espacioEntreColumnas;
            profTotalMuebles = (filas - 1) * espacioEntreFilas;
        } else { // Para DERECHA e IZQUIERDA, la profundidad y el ancho se intercambian
            anchoTotalMuebles = (filas - 1) * espacioEntreFilas;
            profTotalMuebles = (columnas - 1) * espacioEntreColumnas;
        }

        // El punto de inicio de la cuadrícula es la esquina superior-izquierda del bloque de muebles
        float startX = -(anchoTotalMuebles / 2.0f);
        float startZ = -(profTotalMuebles / 2.0f);
        // **FIN DE LÓGICA DE CENTRADO CORREGIDA**

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                float posX = 0, posZ = 0;

                // Calcular la posición de cada butaca dentro de la cuadrícula centrada
                if (orientacion == Orientacion.FRENTE) {
                    posX = startX + j * espacioEntreColumnas;
                    posZ = startZ + i * espacioEntreFilas;
                } else { // Para DERECHA e IZQUIERDA
                    posX = startX + i * espacioEntreFilas;
                    posZ = startZ + j * espacioEntreColumnas;
                }

                Butaca butaca = new Butaca();
                Figuras.rotarTG(butaca, 0, (int)rotacionButaca, 0);
                Figuras.moverTG(butaca, posX, 0, posZ);
                this.addChild(butaca);
            }
        }
    }

    public ArrayList<CajaColision> getCajasColision() {
        return this.cajasColisionSalon;
    }
}