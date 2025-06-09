/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

/**
 *
 * @author rubenmedina
 */

/**
 * Interfaz para objetos con los que el muñeco (jugador) puede interactuar.
 * Cualquier clase que represente un objeto interactivo (como puertas,
 * interruptores, NPCs, etc.) deberá implementar esta interfaz.
 */
public interface Interaccion {

    /**
     * Define la acción que se ejecutará cuando el muñeco interactúe
     * con este objeto (generalmente al presionar una tecla designada, como 'E').
     */
    public void accion();

    // Opcionalmente, podríamos añadir más métodos comunes a todos los objetos interactuables,
    // por ejemplo:
    // public boolean puedeInteractuar(); // Para verificar si la interacción es posible en el estado actual.
    // public String getMensajeInteraccion(); // Para mostrar un mensaje específico (ej. "Abrir puerta", "Hablar con X").
    // Pero el ejemplo original solo tiene action(), así que nos ceñiremos a eso por ahora
    // para mantener la simplicidad y la fidelidad al ejemplo.
}