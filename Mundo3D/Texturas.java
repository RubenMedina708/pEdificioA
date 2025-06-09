/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import java.awt.Container;
import java.io.File;
import java.util.HashMap;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.TexCoord2f;

/**
 *
 * @author rubenmedina
 */


public class Texturas {

    // Flags para la generación de primitivas.
    public static final int FLAGS_PRIMITIVAS_TEXTURIZADAS = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
    
    // Caché para almacenar y reutilizar las apariencias ya creadas.
    private static HashMap<String, Appearance> cacheApariencias = new HashMap<>();

    /**
     * Crea una apariencia que reacciona a la iluminación de la escena (con sombreado).
     * Revisa el caché antes de crear una nueva.
     */
    public static Appearance crearApariencia(String nombreArchivoTextura) {
        String claveCache = "sombreado_" + nombreArchivoTextura;
        if (cacheApariencias.containsKey(claveCache)) {
            return cacheApariencias.get(claveCache);
        }

        Appearance apariencia = new Appearance();
        Texture textura = cargarTextura(nombreArchivoTextura);

        if (textura == null) {
            return crearAparienciaDeError();
        }

        apariencia.setTexture(textura);
        
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        apariencia.setTextureAttributes(texAttr);

        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.2f, 0.2f));
        material.setDiffuseColor(new Color3f(0.7f, 0.7f, 0.7f));
        material.setSpecularColor(new Color3f(0.1f, 0.1f, 0.1f));
        apariencia.setMaterial(material);
        
        cacheApariencias.put(claveCache, apariencia);
        return apariencia;
    }

    /**
     * Crea una apariencia que NO reacciona a la luz (siempre brillante), similar al estilo de "EdificioA".
     * Revisa el caché antes de crear una nueva.
     */
    public static Appearance crearAparienciaPlana(String nombreArchivoTextura) {
        String claveCache = "plano_" + nombreArchivoTextura;
        if (cacheApariencias.containsKey(claveCache)) {
            return cacheApariencias.get(claveCache);
        }

        Appearance apariencia = new Appearance();
        Texture textura = cargarTextura(nombreArchivoTextura);
        
        if (textura == null) {
            return crearAparienciaDeError();
        }

        apariencia.setTexture(textura);
        
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.REPLACE);
        apariencia.setTextureAttributes(texAttr);

        cacheApariencias.put(claveCache, apariencia);
        return apariencia;
    }
    
    /**
     * Crea una apariencia especial para las caras de un Skybox.
     * Revisa el caché antes de crear una nueva.
     */
     public static Appearance crearAparienciaSkybox(String nombreArchivoTextura) {
        String claveCache = "skybox_" + nombreArchivoTextura;
        if (cacheApariencias.containsKey(claveCache)) {
            return cacheApariencias.get(claveCache);
        }
        
        Appearance apariencia = new Appearance();
        Texture textura = cargarTextura(nombreArchivoTextura); // Asumiendo que tienes el método helper que te di
        
        if (textura == null) {
            // Devuelve una apariencia de error si la textura no se carga
            return crearAparienciaDeError(); 
        }
        
        apariencia.setTexture(textura);

        // Atributos para que se dibuje como un fondo
        RenderingAttributes ra = new RenderingAttributes();
        ra.setDepthBufferEnable(false);
        ra.setDepthBufferWriteEnable(false);
        apariencia.setRenderingAttributes(ra);
        
        // Atributos para que la textura no sea afectada por la luz
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.REPLACE);
        apariencia.setTextureAttributes(texAttr);
        
        // --- INICIO DE LA CORRECCIÓN ---
        // Esta es la parte clave para que el interior de la caja sea visible.
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE); // Le dice a Java 3D que no descarte ninguna cara
        apariencia.setPolygonAttributes(pa);
        // --- FIN DE LA CORRECCIÓN ---

        cacheApariencias.put(claveCache, apariencia);
        return apariencia;
    }

    /**
     * Crea una apariencia de cristal transparente que reacciona a la luz.
     */
    public static Appearance crearAparienciaCristal() {
        String claveCache = "cristal_transparente";
        if (cacheApariencias.containsKey(claveCache)) {
            return cacheApariencias.get(claveCache);
        }
        
        Appearance apariencia = new Appearance();

        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparencyMode(TransparencyAttributes.BLENDED);
        ta.setTransparency(0.6f);
        apariencia.setTransparencyAttributes(ta);

        Material material = new Material();
        material.setAmbientColor(new Color3f(0.2f, 0.2f, 0.3f));
        material.setDiffuseColor(new Color3f(0.3f, 0.3f, 0.5f));
        material.setSpecularColor(new Color3f(0.9f, 0.9f, 1.0f));
        material.setShininess(100.0f);
        material.setLightingEnable(true);
        apariencia.setMaterial(material);
        
        cacheApariencias.put(claveCache, apariencia);
        return apariencia;
    }

    /**
     * Modifica las coordenadas de textura de un Shape3D para escalar la textura.
     */
    public static void setearEscalaTextura(Shape3D shape, float escalaX, float escalaY) {
        if (shape == null || shape.getGeometry() == null || !(shape.getGeometry() instanceof GeometryArray)) {
            System.err.println("Error en setearEscalaTextura: Shape3D o su geometría no son válidos.");
            return;
        }
        GeometryArray geom = (GeometryArray) shape.getGeometry();
        if ((geom.getVertexFormat() & GeometryArray.TEXTURE_COORDINATE_2) == 0) {
             System.err.println("Error en setearEscalaTextura: La geometría no tiene coordenadas de textura.");
            return;
        }

        int numVertices = geom.getVertexCount();
        TexCoord2f[] nuevasCoordTextura = new TexCoord2f[numVertices];

        for (int i = 0; i < numVertices; i++) {
            TexCoord2f coordActual = new TexCoord2f();
            geom.getTextureCoordinate(0, i, coordActual);
            
            coordActual.set(coordActual.x * escalaX, coordActual.y * escalaY);
            nuevasCoordTextura[i] = coordActual;
        }
        geom.setTextureCoordinates(0, 0, nuevasCoordTextura);
    }
    
    // --- Métodos Privados de Ayuda ---

    /**
     * Método centralizado para cargar un archivo de textura desde el disco.
     */
    private static Texture cargarTextura(String nombreArchivo) {
        String rutaBase = new File("").getAbsolutePath() + File.separator + "src" + File.separator + "img" + File.separator;
        try {
            TextureLoader cargador = new TextureLoader(rutaBase + nombreArchivo, new Container());
            return cargador.getTexture();
        } catch (Exception e) {
            System.err.println("Error al cargar la textura: " + rutaBase + nombreArchivo);
            return null;
        }
    }
    
    /**
     * Devuelve una apariencia de color magenta brillante para indicar un error de carga de textura.
     */
    private static Appearance crearAparienciaDeError() {
        Appearance aparienciaError = new Appearance();
        Material materialError = new Material();
        materialError.setDiffuseColor(new Color3f(1.0f, 0.0f, 1.0f)); // Magenta
        aparienciaError.setMaterial(materialError);
        return aparienciaError;
    }
}