/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.PointLight;
import javax.media.j3d.DirectionalLight; // Añadido para más opciones de luz
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

/**
 *
 * @author rubenmedina
 */

public class Figuras {

    // Constante para la generación de normales y coordenadas de textura, si es necesaria globalmente.
    // En el ejemplo, TX.texCoords = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
    // Podríamos definirla aquí o en Texturas.java. Por ahora, asumimos que Texturas.java la tendrá o pasamos el flag directamente.

    /**
     * Crea un TransformGroup que contiene una Caja (Box) con textura.
     * @param sx Dimensión en X
     * @param sy Dimensión en Y
     * @param sz Dimensión en Z
     * @param texturaNombre El nombre del archivo de textura (ej. "madera.png")
     * @return Un TransformGroup con la caja.
     */
    public static TransformGroup crearCajaConTextura(float sx, float sy, float sz, String texturaNombre) {
        // Asumimos que Texturas.crearApariencia usa Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS
        // o que Texturas.getFlagsPrimitivas() devuelve ese valor.
        int flags = com.sun.j3d.utils.geometry.Primitive.GENERATE_NORMALS +
                    com.sun.j3d.utils.geometry.Primitive.GENERATE_TEXTURE_COORDS;
        Box caja = new Box(sx, sy, sz, flags, Texturas.crearApariencia(texturaNombre));
        TransformGroup tgCaja = new TransformGroup();
        tgCaja.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Importante para mover/rotar después
        tgCaja.addChild(caja);
        return tgCaja;
    }

    /**
     * Crea una caja con textura y la añade a un TransformGroup destino, aplicando transformaciones.
     * @param destino El TransformGroup al que se añadirá la caja.
     * @param sx Dimensión X de la caja.
     * @param sy Dimensión Y de la caja.
     * @param sz Dimensión Z de la caja.
     * @param x Posición X.
     * @param y Posición Y.
     * @param z Posición Z.
     * @param rx Rotación en X (grados).
     * @param ry Rotación en Y (grados).
     * @param rz Rotación en Z (grados).
     * @param texturaNombre Nombre del archivo de textura.
     */
   // EN Figuras.java
public static TransformGroup colocarCaja(TransformGroup destino, float sx, float sy, float sz,
                                           float x, float y, float z,
                                           int rx, int ry, int rz, String texturaNombre) {
    TransformGroup tgCaja = crearCajaConTextura(sx, sy, sz, texturaNombre);
    moverTG(tgCaja, x, y, z);
    rotarTG(tgCaja, rx, ry, rz);
    destino.addChild(tgCaja);
    return tgCaja; 
}

    /**
     * Crea un TransformGroup que contiene un Cilindro (Cylinder) con textura.
     * @param radio Radio del cilindro.
     * @param altura Altura del cilindro.
     * @param texturaNombre Nombre del archivo de textura.
     * @return Un TransformGroup con el cilindro.
     */
    public static TransformGroup crearCilindroConTextura(float radio, float altura, String texturaNombre) {
        int flags = com.sun.j3d.utils.geometry.Primitive.GENERATE_NORMALS +
                    com.sun.j3d.utils.geometry.Primitive.GENERATE_TEXTURE_COORDS;
        Cylinder cilindro = new Cylinder(radio, altura, flags, Texturas.crearApariencia(texturaNombre));
        TransformGroup tgCilindro = new TransformGroup();
        tgCilindro.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgCilindro.addChild(cilindro);
        return tgCilindro;
    }

    /**
     * Crea un cilindro con textura y lo añade a un TransformGroup destino, aplicando transformaciones.
     */
    public static void colocarCilindro(TransformGroup destino, float radio, float altura,
                                       float x, float y, float z,
                                       int rx, int ry, int rz, String texturaNombre) {
        TransformGroup tgCilindro = crearCilindroConTextura(radio, altura, texturaNombre);
        moverTG(tgCilindro, x, y, z);
        rotarTG(tgCilindro, rx, ry, rz);
        destino.addChild(tgCilindro);
    }

    /**
     * Crea un TransformGroup que contiene una Esfera (Sphere) con textura.
     */
    public static TransformGroup crearEsferaConTextura(float radio, String texturaNombre) {
        int flags = com.sun.j3d.utils.geometry.Primitive.GENERATE_NORMALS +
                    com.sun.j3d.utils.geometry.Primitive.GENERATE_TEXTURE_COORDS;
        Sphere esfera = new Sphere(radio, flags, Texturas.crearApariencia(texturaNombre));
        TransformGroup tgEsfera = new TransformGroup();
        tgEsfera.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgEsfera.addChild(esfera);
        return tgEsfera;
    }

    /**
     * Crea una esfera con textura y la añade a un TransformGroup destino, aplicando transformaciones.
     */
    public static void colocarEsfera(TransformGroup destino, float radio,
                                     float x, float y, float z,
                                     int rx, int ry, int rz, String texturaNombre) {
        TransformGroup tgEsfera = crearEsferaConTextura(radio, texturaNombre);
        moverTG(tgEsfera, x, y, z);
        rotarTG(tgEsfera, rx, ry, rz);
        destino.addChild(tgEsfera);
    }

    /**
     * Crea un Shape3D que representa un plano texturizado.
     * @param x Posición X de la esquina inferior izquierda.
     * @param y Posición Y de la esquina inferior izquierda.
     * @param z Posición Z del plano.
     * @param ancho Ancho del plano.
     * @param alto Alto del plano.
     * @param texturaNombre Nombre del archivo de textura.
     * @param sinCulling Si es true, deshabilita el culling (se ven ambas caras).
     * @return Un Shape3D representando el plano.
     */
    public static Shape3D crearPlanoPersonalizado(float x, float y, float z, float ancho, float alto, String texturaNombre, boolean sinCulling) {
        Appearance apariencia = Texturas.crearApariencia(texturaNombre);

        // El ejemplo de TG.java usa TransparencyAttributes.BLENDED y lo pone en 0.0f.
        // Esto es útil si la textura tiene canal alfa y se quiere que funcione.
        TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.0f);
        apariencia.setTransparencyAttributes(ta);

        QuadArray planoGeom = new QuadArray(4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2 | QuadArray.NORMALS);
        // Definir coordenadas de los vértices
        planoGeom.setCoordinate(0, new Point3f(x,       y,        z));
        planoGeom.setCoordinate(1, new Point3f(x+ancho, y,        z));
        planoGeom.setCoordinate(2, new Point3f(x+ancho, y+alto,   z));
        planoGeom.setCoordinate(3, new Point3f(x,       y+alto,   z));

        // Coordenadas de textura (UV mapping)
        planoGeom.setTextureCoordinate(0, 0, new TexCoord2f(0.0f, 0.0f));
        planoGeom.setTextureCoordinate(0, 1, new TexCoord2f(1.0f, 0.0f));
        planoGeom.setTextureCoordinate(0, 2, new TexCoord2f(1.0f, 1.0f));
        planoGeom.setTextureCoordinate(0, 3, new TexCoord2f(0.0f, 1.0f));
        
        // Normales (todas apuntando en +Z para un plano en XY)
        Vector3f normal = new Vector3f(0.0f, 0.0f, 1.0f);
        for (int i = 0; i < 4; i++) {
            planoGeom.setNormal(i, normal);
        }


        if (sinCulling) {
            PolygonAttributes polyAttr = new PolygonAttributes();
            polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
            apariencia.setPolygonAttributes(polyAttr);
        }
            
        return new Shape3D(planoGeom, apariencia);
    }

    /**
     * Crea un plano personalizado y lo añade a un TransformGroup destino con transformaciones.
     */
    public static void colocarPlano(TransformGroup destino, float ancho, float alto,
                                    float x, float y, float z,
                                    int rx, int ry, int rz, String texturaNombre, boolean sinCulling) {
        Shape3D plano = crearPlanoPersonalizado(0, 0, 0, ancho, alto, texturaNombre, sinCulling); // Crear en origen
        TransformGroup tgPlano = new TransformGroup();
        tgPlano.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgPlano.addChild(plano);
        moverTG(tgPlano, x, y, z);
        rotarTG(tgPlano, rx, ry, rz);
        destino.addChild(tgPlano);
    }

    /**
     * Crea un plano tipo "Billboard" simple que siempre mira a la cámara (simulado con dos planos cruzados).
     * El ejemplo SBBPlane crea dos planos perpendiculares.
     * @param x Posición X.
     * @param y Posición Y.
     * @param z Posición Z.
     * @param ancho Ancho del billboard.
     * @param alto Alto del billboard.
     * @param texturaNombre Nombre de la textura.
     * @return Un TransformGroup que contiene el billboard.
     */
    public static TransformGroup crearBillboardSimple(float x, float y, float z, float ancho, float alto, String texturaNombre) {
        TransformGroup tgBillboard = new TransformGroup();
        tgBillboard.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Plano 1 (frontal)
        Shape3D plano1 = crearPlanoPersonalizado(-ancho/2, 0, 0, ancho, alto, texturaNombre, true);
        
        // Plano 2 (cruzado, rotado 90 grados en Y)
        Shape3D plano2 = crearPlanoPersonalizado(-ancho/2, 0, 0, ancho, alto, texturaNombre, true);
        TransformGroup tgPlano2 = new TransformGroup();
        rotarTG(tgPlano2, 0, 90, 0);
        tgPlano2.addChild(plano2);

        tgBillboard.addChild(plano1);
        tgBillboard.addChild(tgPlano2);
        
        moverTG(tgBillboard, x, y, z); // Mover todo el billboard a su posición
        return tgBillboard;
    }
    
    /**
     * Devuelve un BoundingSphere genérico para luces, sonidos, etc.
     * @return Un BoundingSphere.
     */
    public static BoundingSphere getLimitesVisuales() {
        return new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 200.0); // Radio grande
    }


    /**
     * Crea una PointLight con color y atenuación por defecto.
     * @param pos Posición de la luz.
     * @param color Color de la luz.
     * @return El objeto PointLight.
     */
    public static PointLight crearLuzPuntual(Point3f pos, Color3f color) {
        PointLight luz = new PointLight(color, pos, new Point3f(1.0f, 0.0f, 0.0f)); // Atenuación por defecto
        luz.setInfluencingBounds(getLimitesVisuales());
        luz.setCapability(PointLight.ALLOW_STATE_WRITE); // Para encender/apagar
        luz.setCapability(PointLight.ALLOW_COLOR_WRITE); // Para cambiar color
        return luz;
    }
    
    /**
     * Crea una DirectionalLight.
     * @param color Color de la luz.
     * @param direccion Vector de dirección de la luz.
     * @return El objeto DirectionalLight.
     */
    public static DirectionalLight crearLuzDireccional(Color3f color, Vector3f direccion) {
        DirectionalLight luz = new DirectionalLight(color, direccion);
        luz.setInfluencingBounds(getLimitesVisuales());
        luz.setCapability(DirectionalLight.ALLOW_STATE_WRITE);
        luz.setCapability(DirectionalLight.ALLOW_COLOR_WRITE);
        return luz;
    }


    public static void moverTG(TransformGroup tg, float x, float y, float z) {
        Transform3D transformActual = new Transform3D();
        tg.getTransform(transformActual); // Obtener transformación actual

        Transform3D transformNueva = new Transform3D();
        transformNueva.setTranslation(new Vector3f(x, y, z)); // Crear transformación de traslación

        transformActual.mul(transformNueva); // Aplicar la nueva traslación a la actual
        tg.setTransform(transformActual);    // Establecer la transformación combinada
    }

    /**
     * Rota un TransformGroup relativo a su orientación actual.
     * Las rotaciones se aplican en orden: X, luego Y, luego Z.
     * @param tg El TransformGroup a rotar.
     * @param x Grados a rotar en X.
     * @param y Grados a rotar en Y.
     * @param z Grados a rotar en Z.
     */
    public static void rotarTG(TransformGroup tg, int x, int y, int z) {
        Transform3D transformActual = new Transform3D();
        tg.getTransform(transformActual);

        Transform3D rotX = new Transform3D();
        rotX.rotX(Math.toRadians(x));

        Transform3D rotY = new Transform3D();
        rotY.rotY(Math.toRadians(y));

        Transform3D rotZ = new Transform3D();
        rotZ.rotZ(Math.toRadians(z));

        // Aplicar rotaciones: Tactual = Tactual * RotX * RotY * RotZ
        transformActual.mul(rotX);
        transformActual.mul(rotY);
        transformActual.mul(rotZ);
        
        tg.setTransform(transformActual);
    }
    
    /**
     * Escala un TransformGroup relativo a su escala actual.
     * @param tg El TransformGroup a escalar.
     * @param factor Escala uniforme.
     */
    public static void escalarTG(TransformGroup tg, double factor) {
        Transform3D transformActual = new Transform3D();
        tg.getTransform(transformActual);

        Transform3D transformEscala = new Transform3D();
        transformEscala.setScale(factor);

        transformActual.mul(transformEscala);
        tg.setTransform(transformActual);
    }
}