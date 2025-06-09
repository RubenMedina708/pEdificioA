/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import com.sun.j3d.utils.geometry.Box;
import javax.vecmath.Vector3d;
/**
 *
 * @author rubenmedina
 */

public class CajaColision {

    public TransformGroup objeto; // El objeto 3D al que pertenece esta caja
    public boolean habilitada = true; // La colisión está activa por defecto

    /**
     * Constructor que asocia esta caja de colisión con un objeto 3D.
     * @param objeto El TransformGroup que contiene la geometría para colisionar.
     */
    public CajaColision(TransformGroup objeto) {
        this.objeto = objeto;
    }

    /**
     * Comprueba si esta caja de colisión choca con otra.
     * @param otraCaja La otra caja de colisión con la que se quiere comprobar.
     * @return true si hay colisión, false si no.
     */
    public boolean chocaCon(CajaColision otraCaja) {
        // Si alguna de las dos cajas no está habilitada, no hay colisión.
        if (!this.habilitada || !otraCaja.habilitada) {
            return false;
        }

        // Se asume que el primer hijo del TransformGroup es la forma geométrica (Box)
        Box caja1 = (Box) this.objeto.getChild(0);
        Box caja2 = (Box) otraCaja.objeto.getChild(0);

        // Obtener transformaciones absolutas
        Transform3D t1 = obtenerTransformacionAbsoluta(this.objeto);
        Transform3D t2 = obtenerTransformacionAbsoluta(otraCaja.objeto);

        // Posiciones
        Vector3d pos1 = new Vector3d();
        t1.get(pos1);
        Vector3d pos2 = new Vector3d();
        t2.get(pos2);

        // Dimensiones de las cajas (la mitad del tamaño total)
        double x1 = caja1.getXdimension() / 2.0;
        double y1 = caja1.getYdimension() / 2.0;
        double z1 = caja1.getZdimension() / 2.0;

        double x2 = caja2.getXdimension() / 2.0;
        double y2 = caja2.getYdimension() / 2.0;
        double z2 = caja2.getZdimension() / 2.0;

        // Comprueba si se solapan en los tres ejes (algoritmo AABB)
        return (Math.abs(pos1.x - pos2.x) <= x1 + x2) &&
               (Math.abs(pos1.y - pos2.y) <= y1 + y2) &&
               (Math.abs(pos1.z - pos2.z) <= z1 + z2);
    }

    /**
     * Calcula la transformación absoluta (coordenadas del mundo) de un TransformGroup.
     * Es privado y estático porque es una utilidad interna.
     */
    private static Transform3D obtenerTransformacionAbsoluta(TransformGroup tg) {
        Transform3D t = new Transform3D();
        tg.getLocalToVworld(t); // Método más directo para obtener la transformación mundial
        return t;
    }
}