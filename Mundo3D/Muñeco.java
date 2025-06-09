/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.TransformGroup;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3f; // Para ajustar la posición en reiniciarTransformaciones
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rubenmedina
 */

public class Muñeco extends TransformGroup {
    private TransformGroup tgCabeza = new TransformGroup();
    
    private TransformGroup tgBrazoIzq1 = new TransformGroup(); // Parte superior del brazo (hombro a codo)
    private TransformGroup tgBrazoIzq2 = new TransformGroup(); // Antebrazo (codo a muñeca)
    private TransformGroup tgBrazoDer1 = new TransformGroup();
    private TransformGroup tgBrazoDer2 = new TransformGroup();
    
    private TransformGroup tgPiernaIzq1 = new TransformGroup(); // Muslo
    private TransformGroup tgPiernaIzq2 = new TransformGroup(); // Pantorrilla
    private TransformGroup tgPiernaDer1 = new TransformGroup();
    private TransformGroup tgPiernaDer2 = new TransformGroup();
    
    private boolean enAnimacion = false; // Controla si la animación de caminar está activa
    private Transform3D[] transformacionesReinicio; // Guarda las posturas iniciales de las partes

    public Muñeco() {
        super(); // Llama al constructor de TransformGroup

        // 1. Configurar capacidades
        // Permitir leer y escribir transformaciones para el muñeco completo y sus partes
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        configurarCapacidadesPartes(); // Método helper para las partes

        // 2. Construir el modelo 3D del muñeco
        // El ejemplo usa una serie de llamadas a TG.placeCylinder, TG.placeSphere, TG.placeBox
        // Aquí usaremos Figuras.colocarCilindro, Figuras.colocarEsfera, etc.

        // Posicionamiento inicial del cuerpo completo (ejemplo lo mueve un poco hacia abajo)
        Figuras.moverTG(this, 0, -0.2f, 0); // Ajuste vertical del muñeco completo

        // ---- TORSO ----
        // Usamos un cilindro para el torso, con textura "player_tshirt.png" del ejemplo
        Figuras.colocarCilindro(this, 0.12f, 0.4f, 0, -0.2f, 0, 0, 0, 0, "cielo1.png");

        // ---- CABEZA ----
        // La cabeza se añade como hijo del TransformGroup principal del muñeco
        Figuras.moverTG(tgCabeza, 0, 0.02f, 0); // Offset de la cabeza respecto al torso
        this.addChild(tgCabeza);
        // Esfera para la cabeza base
        Figuras.colocarEsfera(tgCabeza, 0.12f, 0, 0.1f, 0, 0, 0, 0, "cielo1.png");
        // Detalles de la cabeza (orejas, hocico, ojos, etc. - adaptado del ejemplo)
        // Orejas (cajas pequeñas)
        Figuras.colocarCaja(tgCabeza, 0.05f, 0.02f, 0.05f, -0.1f, 0.16f, 0, 0, 0, 20, "cielo1.png");
        Figuras.colocarCaja(tgCabeza, 0.05f, 0.02f, 0.05f, 0.1f, 0.16f, 0, 0, 0, -20, "cielo1.png");
        // Hocico (caja)
        Figuras.colocarCaja(tgCabeza, 0.05f, 0.04f, 0.05f, 0, 0.08f, -0.12f, 0, 0, 0, "cielo1.png");
        // Ojos (esferas pequeñas)
        Figuras.colocarEsfera(tgCabeza, 0.03f, -0.05f, 0.14f, -0.08f, 0, 0, 0, "cielo1.png");
        Figuras.colocarEsfera(tgCabeza, 0.03f, 0.05f, 0.14f, -0.08f, 0, 0, 0, "cielo1.png");
        // Nariz (esfera pequeña)
        Figuras.colocarEsfera(tgCabeza, 0.03f, 0f, 0.1f, -0.16f, 0, 0, 0, "cielo1.png");
        // "Antenas/Mechones" del ejemplo original (eran cajas alargadas)
        Figuras.colocarCaja(tgCabeza, 0.16f, 0.01f, 0.01f, -0.06f, 0.2f, 0, 0, 0, -30, "cielo1.png");
        Figuras.colocarCaja(tgCabeza, 0.16f, 0.01f, 0.01f, 0.06f, 0.2f, 0, 0, 0, 30, "cielo1.png");


        // ---- BRAZOS ----
        // Brazo Izquierdo
        this.addChild(tgBrazoIzq1);
        tgBrazoIzq1.addChild(tgBrazoIzq2);
        Figuras.moverTG(tgBrazoIzq1, -0.1f, 0f, 0); // Posición del hombro
        Figuras.moverTG(tgBrazoIzq2, -0.05f, -0.2f, 0); // Posición del codo relativo al hombro
        Figuras.colocarCilindro(tgBrazoIzq1, 0.04f, 0.2f, -0.05f, -0.1f, 0, 0, 0, 0, "cielo1.png"); // Hombro a codo
        Figuras.colocarCilindro(tgBrazoIzq2, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png");       // Codo a muñeca
        Figuras.colocarEsfera(tgBrazoIzq2, 0.05f, 0, -0.2f, 0, 0, 0, 0, "cielo1.png");                // Mano

        // Brazo Derecho
        this.addChild(tgBrazoDer1);
        tgBrazoDer1.addChild(tgBrazoDer2);
        Figuras.moverTG(tgBrazoDer1, 0.1f, 0f, 0);
        Figuras.moverTG(tgBrazoDer2, 0.05f, -0.2f, 0);
        Figuras.colocarCilindro(tgBrazoDer1, 0.04f, 0.2f, 0.05f, -0.1f, 0, 0, 0, 0, "cielo1.png");
        Figuras.colocarCilindro(tgBrazoDer2, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png");
        Figuras.colocarEsfera(tgBrazoDer2, 0.05f, 0, -0.2f, 0, 0, 0, 0, "cielo1.png");

        // ---- PIERNAS ----
        // Pierna Izquierda
        this.addChild(tgPiernaIzq1);
        tgPiernaIzq1.addChild(tgPiernaIzq2);
        Figuras.moverTG(tgPiernaIzq1, -0.06f, -0.4f, 0); // Posición de la cadera
        Figuras.moverTG(tgPiernaIzq2, 0f, -0.2f, 0);    // Posición de la rodilla relativa a la cadera
        Figuras.colocarCilindro(tgPiernaIzq1, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png"); // Muslo
        Figuras.colocarCilindro(tgPiernaIzq2, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png");   // Pantorrilla/Pie

        // Pierna Derecha
        this.addChild(tgPiernaDer1);
        tgPiernaDer1.addChild(tgPiernaDer2);
        Figuras.moverTG(tgPiernaDer1, 0.06f, -0.4f, 0);
        Figuras.moverTG(tgPiernaDer2, 0f, -0.2f, 0);
        Figuras.colocarCilindro(tgPiernaDer1, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png");
        Figuras.colocarCilindro(tgPiernaDer2, 0.04f, 0.2f, 0, -0.1f, 0, 0, 0, 0, "cielo1.png");

        // 3. Guardar las transformaciones iniciales de las partes
        transformacionesReinicio = new Transform3D[9];
        TransformGroup[] partesCuerpo = {
            tgCabeza, tgBrazoIzq1, tgBrazoIzq2, tgBrazoDer1, tgBrazoDer2,
            tgPiernaIzq1, tgPiernaIzq2, tgPiernaDer1, tgPiernaDer2
        };
        for (int i = 0; i < partesCuerpo.length; i++) {
            transformacionesReinicio[i] = new Transform3D();
            partesCuerpo[i].getTransform(transformacionesReinicio[i]);
        }
    }

    private void configurarCapacidadesPartes() {
        TransformGroup[] partes = {
            tgCabeza, tgBrazoIzq1, tgBrazoIzq2, tgBrazoDer1, tgBrazoDer2,
            tgPiernaIzq1, tgPiernaIzq2, tgPiernaDer1, tgPiernaDer2
        };
        for (TransformGroup parte : partes) {
            parte.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            parte.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }
    }

    public void caminar() {
        if (!enAnimacion) {
            enAnimacion = true;
            new Thread(this::animacionCaminar).start();
        }
    }

    public void detenerCaminar() {
        enAnimacion = false;
        // La llamada a reiniciarTransformaciones se hará típicamente desde el hilo de animación
        // cuando enAnimacion sea false, para asegurar que se complete el ciclo actual.
        // O se puede llamar directamente, pero puede cortar la animación abruptamente.
        // El ejemplo original no llama a resetTGs() aquí directamente, sino que la animación
        // simplemente termina su ciclo y se detiene si 'anim' es false.
        // 'resetTGs' se llama en el ejemplo cuando el control pasa de joystick a teclado o viceversa,
        // o cuando la animación se detiene explícitamente Y el personaje no se mueve.
        // Por ahora, dejaremos que el hilo de animación se encargue de parar y luego se puede llamar a reiniciar.
        // La lógica de 'Mundo3D' llamará a reiniciarTransformaciones() si es necesario después de que la animación pare.
    }
    
    public boolean estaAnimado() {
        return enAnimacion;
    }

    private void animacionCaminar() {
        try {
            // Postura inicial para caminar (ligera inclinación de brazos)
            Figuras.rotarTG(tgBrazoIzq1, 0, 0, -5);
            Figuras.rotarTG(tgBrazoDer1, 0, 0, 5);

            // Primera fase de la animación (un paso)
            for (int i = 0; i < 16; i++) {
                if (!enAnimacion) break; // Salir si se detiene la animación

                if (i % 4 == 0) Figuras.rotarTG(tgCabeza, 0, 0, 1); // Mover cabeza ligeramente

                Figuras.rotarTG(tgBrazoIzq1, 5, 0, 0);
                Figuras.rotarTG(tgPiernaIzq1, -5, 0, 0);
                Figuras.rotarTG(tgBrazoDer1, -5, 0, 0);
                Figuras.rotarTG(tgPiernaDer1, 5, 0, 0);

                // Movimiento secundario de antebrazos/pantorrillas
                Figuras.rotarTG(tgBrazoIzq2, 1, 0, 0);
                Figuras.rotarTG(tgBrazoDer2, 1, 0, 0);
                Figuras.rotarTG(tgPiernaIzq2, -1, 0, 0); // El ejemplo usa -1, podría ser +1 para más naturalidad
                Figuras.rotarTG(tgPiernaDer2, -1, 0, 0);

                Thread.sleep(8); // Pausa corta
            }

            // Bucle principal de la animación mientras 'enAnimacion' sea true
            float offsetY = 0; // Para el movimiento vertical del cuerpo
            while (enAnimacion) {
                // Movimiento hacia adelante
                for (int i = 0; i < 32; i++) {
                    if (!enAnimacion) { reiniciarTransformaciones(); return; } // Salir y reiniciar postura
                    if (i % 4 == 0) Figuras.rotarTG(tgCabeza, 0, 0, -1);

                    Figuras.rotarTG(tgBrazoIzq1, -5, 0, 0);
                    Figuras.rotarTG(tgPiernaIzq1, 5, 0, 0);
                    Figuras.rotarTG(tgBrazoDer1, 5, 0, 0);
                    Figuras.rotarTG(tgPiernaDer1, -5, 0, 0);
                    
                    Figuras.rotarTG(tgBrazoIzq2, 1, 0, 0);
                    Figuras.rotarTG(tgBrazoDer2, 1, 0, 0);
                    Figuras.rotarTG(tgPiernaIzq2, -1, 0, 0);
                    Figuras.rotarTG(tgPiernaDer2, -1, 0, 0);

                    // Movimiento vertical sutil del cuerpo (sube y baja)
                    float dy = (float) Math.cos(i / 10.0) * 0.005f;
                    Figuras.moverTG(this, 0, dy, 0);
                    offsetY += dy;
                    Thread.sleep(12);
                }
                
                // Corregir la altura acumulada
                Figuras.moverTG(this, 0, -offsetY, 0); 
                offsetY = 0;

                // Movimiento hacia atrás (para el otro ciclo de piernas/brazos)
                for (int i = 0; i < 32; i++) {
                    if (!enAnimacion) { reiniciarTransformaciones(); return; }
                    if (i % 4 == 0) Figuras.rotarTG(tgCabeza, 0, 0, 1);

                    Figuras.rotarTG(tgBrazoIzq1, 5, 0, 0);
                    Figuras.rotarTG(tgPiernaIzq1, -5, 0, 0);
                    Figuras.rotarTG(tgBrazoDer1, -5, 0, 0);
                    Figuras.rotarTG(tgPiernaDer1, 5, 0, 0);

                    Figuras.rotarTG(tgBrazoIzq2, -1, 0, 0); // Invertir para el ciclo
                    Figuras.rotarTG(tgBrazoDer2, -1, 0, 0);
                    Figuras.rotarTG(tgPiernaIzq2, 1, 0, 0);
                    Figuras.rotarTG(tgPiernaDer2, 1, 0, 0);
                    
                    float dy = (float) Math.cos(i / 10.0) * 0.005f;
                    Figuras.moverTG(this, 0, dy, 0);
                    offsetY += dy;
                    Thread.sleep(12);
                }
                Figuras.moverTG(this, 0, -offsetY, 0);
                offsetY = 0;
            }
            reiniciarTransformaciones(); // Reiniciar postura al final si la animación se detuvo
        } catch (InterruptedException ex) {
            Logger.getLogger(Muñeco.class.getName()).log(Level.SEVERE, "Hilo de animación interrumpido", ex);
            reiniciarTransformaciones(); // Asegurar reinicio en caso de error
        }
    }

    public void reiniciarTransformaciones() { // Hecho público para que Mundo3D lo pueda llamar
        TransformGroup[] partesCuerpo = {
            tgCabeza, tgBrazoIzq1, tgBrazoIzq2, tgBrazoDer1, tgBrazoDer2,
            tgPiernaIzq1, tgPiernaIzq2, tgPiernaDer1, tgPiernaDer2
        };
        for (int i = 0; i < partesCuerpo.length; i++) {
            if (transformacionesReinicio[i] != null) {
                partesCuerpo[i].setTransform(transformacionesReinicio[i]);
            }
        }
        
        // Lógica para reajustar la posición Y del muñeco completo si se desfasó.
        // (Adaptado de la sección "Fuck this fucking shit bro" del Player.java original)
        Transform3D tActual = new Transform3D();
        Vector3f vActual = new Vector3f();
        this.getTransform(tActual);
        tActual.get(vActual);

        // El original tiene -0.22 y -0.18 como límites.
        // Nuestro 'this' (muñeco completo) se movió a -0.2f al inicio.
        // Si la animación de caminar lo mueve, queremos regresarlo a cerca de -0.2f.
        float yDeseada = -0.2f;
        float tolerancia = 0.02f; // Margen de error

        // Bucle para ajustar suavemente. Esto podría ser problemático si se llama desde
        // el hilo de la GUI sin un Thread.sleep() o si bloquea demasiado.
        // El original lo hace en un bucle con sleep. Por simplicidad, hacemos un ajuste directo.
        // Si se necesita el ajuste suave, este método debería correr en un hilo o ser llamado
        // con cuidado.
        // Figuras.moverTG(this, 0, yDeseada - vActual.y, 0); // Ajuste directo
        
        // Para replicar el bucle de ajuste suave del original:
        new Thread(() -> {
            boolean ajustado = false;
            int intentos = 0;
            int maxIntentos = 100; // Para evitar bucles infinitos
            
            while(!ajustado && intentos < maxIntentos) {
                intentos++;
                this.getTransform(tActual);
                tActual.get(vActual);
                ajustado = true; // Suponemos que está ajustado

                if (vActual.y < yDeseada - tolerancia) {
                    Figuras.moverTG(this, 0, 0.01f, 0);
                    ajustado = false;
                }
                if (vActual.y > yDeseada + tolerancia) {
                    Figuras.moverTG(this, 0, -0.01f, 0);
                    ajustado = false;
                }
                if (!ajustado) {
                    try {
                        Thread.sleep(16); // Pausa como en el original
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt(); // Restaurar estado de interrupción
                        break; 
                    }
                }
            }
            if (intentos >= maxIntentos) {
                // Si no se pudo ajustar, forzar la posición para evitar problemas mayores.
                // Esto es una medida de seguridad, idealmente el ajuste suave funciona.
                this.getTransform(tActual);
                tActual.get(vActual); // Obtener la Y actual después de los intentos
                tActual.setTranslation(new Vector3f(vActual.x, yDeseada, vActual.z)); // Forzar la Y
                this.setTransform(tActual);
                System.err.println("Muneco: Reajuste de Y forzado tras muchos intentos.");
            }

        }).start();
    }
}