/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Appearance;
import java.util.ArrayList;

/**
 *
 * @author rubenmedina
 */

public class AulaMagna extends TransformGroup {

    public enum Orientacion {
        FRENTE, DERECHA, IZQUIERDA
    }

    // Dimensiones internas
    private static final float ANCHO = 7.0f;
    private static final float PROFUNDO = 8.5f;
    private static final float ALTO = 4.0f;
    private static final float GROSOR_PAREDES = 0.12f;

    // Dimensiones para la puerta y ventanas
    private static final float ANCHO_PUERTA = 1.0f;
    private static final float ALTO_PUERTA = 2.2f;
    private static final float ANCHO_VENTANA = 2.5f;
    private static final float ALTO_VENTANA = 1.5f;
    private static final float ALTURA_BASE_VENTANA = 1.0f;

    // Texturas
    private String texturaPared = "ladrillo.png";
    private String texturaPiso = "piso.png";
    private String texturaTecho = "ladrillo.png";
    private String texturaPizarron = "pizarron.png";
    private String texturaTarima = "madera.png";
    private String texturaAsiento = "tela.png";

    public AulaMagna(Orientacion orientacion) {
        this.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        construirCuarto();
        construirTarimaYPizarron(orientacion);
        construirAsientosEscalonados(orientacion);
    }

    private void construirCuarto() {
        // Origen (0,0,0) es la esquina interior trasera-izquierda del piso.
        float centroY = ALTO / 2.0f;

        // --- Piso y Techo (sin cambios) ---
        Figuras.colocarCaja(this, ANCHO / 2.0f, GROSOR_PAREDES / 2.0f, PROFUNDO / 2.0f,
            ANCHO / 2.0f, -(GROSOR_PAREDES / 2.0f), PROFUNDO / 2.0f, 0, 0, 0, texturaPiso);
        Figuras.colocarCaja(this, ANCHO / 2.0f, GROSOR_PAREDES / 2.0f, PROFUNDO / 2.0f,
            ANCHO / 2.0f, ALTO, PROFUNDO / 2.0f, 0, 0, 0, texturaTecho);

        // --- Pared Izquierda y Derecha (Ahora SÓLIDAS) ---
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, PROFUNDO / 2.0f,
            0, centroY, PROFUNDO / 2.0f, 0, 0, 0, texturaPared); // Izquierda
        Figuras.colocarCaja(this, GROSOR_PAREDES / 2.0f, centroY, PROFUNDO / 2.0f,
            ANCHO, centroY, PROFUNDO / 2.0f, 0, 0, 0, texturaPared); // Derecha
            
        // --- Pared Trasera (Z=0) - AHORA CON VENTANAS ---
        float anchoPilarVentana = (ANCHO - (ANCHO_VENTANA * 2)) / 3.0f; // Espacio para 2 ventanas y 3 pilares
        // Pilar izquierdo de la pared trasera
        Figuras.colocarCaja(this, anchoPilarVentana / 2.0f, centroY, GROSOR_PAREDES / 2.0f,
            anchoPilarVentana / 2.0f, centroY, 0, 0, 0, 0, texturaPared);
        // Pilar central
        Figuras.colocarCaja(this, anchoPilarVentana / 2.0f, centroY, GROSOR_PAREDES / 2.0f,
            ANCHO / 2.0f, centroY, 0, 0, 0, 0, texturaPared);
        // Pilar derecho
        Figuras.colocarCaja(this, anchoPilarVentana / 2.0f, centroY, GROSOR_PAREDES / 2.0f,
            ANCHO - (anchoPilarVentana / 2.0f), centroY, 0, 0, 0, 0, texturaPared);
        // Muros sobre las ventanas
        float alturaSobreVentana = ALTO - (ALTURA_BASE_VENTANA + ALTO_VENTANA);
        Figuras.colocarCaja(this, ANCHO / 2.0f, alturaSobreVentana / 2.0f, GROSOR_PAREDES / 2.0f,
            ANCHO / 2.0f, ALTURA_BASE_VENTANA + ALTO_VENTANA + (alturaSobreVentana / 2.0f), 0, 0, 0, 0, texturaPared);
        // Muros debajo de las ventanas
        Figuras.colocarCaja(this, ANCHO / 2.0f, ALTURA_BASE_VENTANA / 2.0f, GROSOR_PAREDES / 2.0f,
            ANCHO / 2.0f, ALTURA_BASE_VENTANA / 2.0f, 0, 0, 0, 0, texturaPared);

        // --- Pared Frontal (Z=PROFUNDO) - AHORA CON PUERTA A LA DERECHA ---
        // 1. Muro sólido a la izquierda de la puerta
        float anchoMuroSolido = ANCHO - ANCHO_PUERTA;
        Figuras.colocarCaja(this, anchoMuroSolido / 2.0f, centroY, GROSOR_PAREDES / 2.0f,
            anchoMuroSolido / 2.0f, centroY, PROFUNDO, 0, 0, 0, texturaPared);
        // 2. Dintel (viga sobre la puerta)
        float alturaDintel = ALTO - ALTO_PUERTA;
        Figuras.colocarCaja(this, ANCHO_PUERTA / 2.0f, alturaDintel / 2.0f, GROSOR_PAREDES / 2.0f,
            ANCHO - (ANCHO_PUERTA / 2.0f), ALTO_PUERTA + (alturaDintel / 2.0f), PROFUNDO,
            0, 0, 0, texturaPared);
    }
    
    // El resto de los métodos (construirTarimaYPizarron, construirAsientosEscalonados, etc.)
    // se quedan exactamente como estaban en la respuesta anterior, ya que la orientación
    // interna no cambia.
    
    private void construirTarimaYPizarron(Orientacion orientacion) {
        float altoTarima = 0.4f;
        float profTarima = 1.5f;
        float anchoPizarron = 4.0f;
        float altoPizarron = 1.5f;

        TransformGroup tgTarima = new TransformGroup();
        Figuras.colocarCaja(tgTarima, anchoPizarron / 2.0f, altoTarima / 2.0f, profTarima / 2.0f, 0, 0, 0, 0, 0, 0, texturaTarima);
        TransformGroup tgPizarron = new TransformGroup();
        Figuras.colocarCaja(tgPizarron, anchoPizarron / 2.0f, altoPizarron / 2.0f, 0.025f, 0, 0, 0, 0, 0, 0, texturaPizarron);

        if(orientacion == Orientacion.IZQUIERDA) {
            Figuras.moverTG(tgTarima, profTarima / 2.0f, altoTarima / 2.0f, PROFUNDO / 2.0f);
            Figuras.rotarTG(tgTarima, 0, 90, 0);
            Figuras.moverTG(tgPizarron, GROSOR_PAREDES + 0.02f, altoTarima + (altoPizarron / 2.0f), PROFUNDO / 2.0f);
            Figuras.rotarTG(tgPizarron, 0, 90, 0);
        }
        
        this.addChild(tgTarima);
        this.addChild(tgPizarron);
    }
    
  private void construirAsientosEscalonados(Orientacion orientacion) {
        int filas = 4;
        int columnas = 3; // Ajustado para que quepa mejor
        float espacioX = 1.5f;
        float espacioZ = 1.5f;
        float alturaEscalon = 0.2f;
        float alturaInicial = 0.0f;

        for (int i = 0; i < filas; i++) {
            float alturaFila = alturaInicial + (i * alturaEscalon);
            for (int j = 0; j < columnas; j++) {

                TransformGroup tgAsiento = crearAsientoSimple();
                float posX = 0, posZ = 0;
                int rotY = 0;


                switch(orientacion) {
                    case IZQUIERDA:
                        // La tarima ocupa hasta X=1.5f aprox. Empezamos las sillas un poco más allá. 
                        // Las COLUMNAS se distribuyen en Z. 
                        // Las FILAS se alejan de la pared izquierda, aumentando en X. 
                        posX = 1.7f + i * espacioX;
                        posZ = -6.5f + j * espacioZ;
                        rotY = 90; // Rotar para mirar hacia la pared izquierda (hacia -X global del salón)
                        break;

                    case DERECHA:
                        posX = (ANCHO - 2.0f) - i * espacioX;
                        posZ = 1.2f + j * espacioZ;
                        rotY = -90; // Mirar hacia la pared derecha (+X) 
                        break;

                    case FRENTE:
                    default:
                        posX = 1.0f + j * espacioX;
                        posZ = (PROFUNDO - 2.0f) - i * espacioZ;
                        rotY = 180; // Mirar hacia la pared del fondo (-Z) 
                        break;     
                }

                Figuras.rotarTG(tgAsiento, 0, -rotY, 0);
                Figuras.moverTG(tgAsiento, posX, alturaFila, posZ);
                this.addChild(tgAsiento);
            }
        }
    }

        private TransformGroup crearAsientoSimple() {
        TransformGroup grupoAsiento = new TransformGroup();
        float alturaBase = 0.2f;
        // La base del asiento se centra en su origen Y 
        Figuras.colocarCaja(grupoAsiento, 0.25f, alturaBase, 0.25f, 0, alturaBase, 0, 0,0,0, texturaAsiento);
         // El respaldo se coloca relativo a la base 
        Figuras.colocarCaja(grupoAsiento, 0.25f, 0.3f, 0.05f, 0, alturaBase * 2 + 0.25f, -0.2f, 0,0,0, texturaAsiento);
        return grupoAsiento;
        }
}