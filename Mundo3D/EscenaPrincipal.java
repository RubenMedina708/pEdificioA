/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Text2D;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.Appearance;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rubenmedina
 */

public class EscenaPrincipal {

    private BranchGroup bgRaiz;
    private Muñeco muñeco;
    private TransformGroup tgAnclaCamara;
    private TransformGroup tgMundo;
    private ArrayList<CajaColision> cajasColision;
    private HashMap<Interaccion, CajaColision> listaInteracciones;
    private TransformGroup tgTextoInteraccion;

    public EscenaPrincipal() {
        bgRaiz = new BranchGroup();
        bgRaiz.setCapability(BranchGroup.ALLOW_DETACH);

        cajasColision = new ArrayList<>();
        listaInteracciones = new HashMap<>();

        tgAnclaCamara = new TransformGroup();
        tgAnclaCamara.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        bgRaiz.addChild(tgAnclaCamara);

        tgMundo = new TransformGroup();
        tgMundo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tgMundo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tgAnclaCamara.addChild(tgMundo);

        muñeco = new Muñeco();
        tgAnclaCamara.addChild(muñeco);

        construirEntorno();
        construirEdificioConInteriores();
        construirLuces();
        construirTextoInteraccion();

        System.out.println("Escena Principal creada. Colisiones activas: " + cajasColision.size());
    }

    private void construirEntorno() {
        Appearance aparienciaSuelo = Texturas.crearApariencia("pasto.png");
        Box suelo = new Box(100.0f, 0.1f, 100.0f, Texturas.FLAGS_PRIMITIVAS_TEXTURIZADAS, aparienciaSuelo);
        TransformGroup tgSuelo = new TransformGroup();
        Figuras.moverTG(tgSuelo, 0.0f, -1.45f, 0.0f);
        tgSuelo.addChild(suelo);
        tgMundo.addChild(tgSuelo);


        float tamSkybox = 150f;

        Appearance appSky = Texturas.crearAparienciaSkybox("cielo1.png");

        Box skybox = new Box(tamSkybox, tamSkybox, tamSkybox, Texturas.FLAGS_PRIMITIVAS_TEXTURIZADAS, new Appearance());
        skybox.getShape(Box.FRONT).setAppearance(appSky);
        skybox.getShape(Box.BACK).setAppearance(appSky);
        skybox.getShape(Box.LEFT).setAppearance(appSky);
        skybox.getShape(Box.RIGHT).setAppearance(appSky);
        skybox.getShape(Box.TOP).setAppearance(appSky);
        skybox.getShape(Box.BOTTOM).setAppearance(appSky);
        tgMundo.addChild(skybox);
    }

    private void construirEdificioConInteriores() {
      MurosEdificio edificioPrincipal = new MurosEdificio("ladrillo.png"); // <--- ASÍ DEBERÍA SER LA LLAMADA

TransformGroup tgEdificio = edificioPrincipal.getTransformGroup();
tgEdificio.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); // Para getTransform()
tgEdificio.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); // Para setPosicion()

edificioPrincipal.setPosicion(0.0f, -1.4f, 5.0f); 
tgMundo.addChild(tgEdificio);

        // --- 2. Crear y añadir el piso interior general ---
    float grosorMuro = 0.2f;
    float anchoPiso = 52f - (2 * grosorMuro);
    float profPiso = 20.0f - (2 * grosorMuro);
    float grosorPiso = 0.15f;

    // **INICIO DE LA CORRECCIÓN**
    // Vamos a levantar el piso interior una pequeña cantidad para evitar el Z-fighting
    float elevacionPiso = 0.06f;

    // La base del edificio está en Y=0 local. Para que la superficie del piso interior
    // quede ligeramente por encima de esa base, su centro Y debe ser:
    float posYCentroPisoInteriorLocal = -(grosorPiso / 2.0f) + elevacionPiso;
    // **FIN DE LA CORRECCIÓN**

    Figuras.colocarCaja(tgEdificio,
        anchoPiso / 2.0f, grosorPiso / 2.0f, profPiso / 2.0f,
        0.0f, posYCentroPisoInteriorLocal, 0.0f, // Usamos la nueva posición Y
        0, 0, 0, "piso.png");



        Salon salonA6 = new Salon(Salon.Orientacion.DERECHA);
    // Usamos la posición que ajustaste:
    Figuras.moverTG(salonA6, -6.42f, 2.5f, -5.8f);
    tgEdificio.addChild(salonA6);

    // ** Salón 1: A la izquierda, con pizarrón y butacas mirando a la DERECHA **
    Salon salonA5 = new Salon(Salon.Orientacion.DERECHA);
    // Usamos la posición que ajustaste:
    Figuras.moverTG(salonA5, -6.42f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA5);
    
  // --- INICIO: AÑADIR PUERTA INTERACTIVA AL SALON A5 (VERSIÓN CORREGIDA) ---

// 1. Definir las dimensiones de la puerta.
// Segun Salon.java, ANCHO_PUERTA es 0.9f y ALTO_PUERTA es 2.0f.
float anchoPuertaSalon = 0.9f;
float altoPuertaSalon = 2.0f;
float grosorPuertaSalon = 0.08f;

// 2. Crear el objeto Puerta. Su constructor ya no necesita una CajaColision.
Puerta puertaSalonA5 = new Puerta(anchoPuertaSalon, altoPuertaSalon, grosorPuertaSalon, "puertaChica.png");
float puertaGlobalX = -6.42f; // Centro X del salón
float puertaGlobalY = -2.0f;   // Nivel del piso
float puertaGlobalZ = -5.8f + (8.2f / 2.0f); // Posición Z del salón + la mitad de su profundidad
Transform3D t3dPuerta = new Transform3D();
t3dPuerta.setTranslation(new Vector3f(puertaGlobalX, puertaGlobalY, puertaGlobalZ));
puertaSalonA5.setTransform(t3dPuerta);

// 4. Añadir la puerta al TransformGroup del mundo para que no se mueva con la cámara.
tgMundo.addChild(puertaSalonA5);

// 5. Una vez creada la puerta, OBTENER su caja de colisión interna.
CajaColision colisionDeLaPuerta = puertaSalonA5.getCajaColision();

// 6. Registrar esa caja de colisión para la detección de colisiones e interacciones.
cajasColision.add(colisionDeLaPuerta);
listaInteracciones.put(puertaSalonA5, colisionDeLaPuerta);

// --- FIN: AÑADIR PUERTA INTERACTIVA AL SALON A5 (VERSIÓN CORREGIDA) ---

    // ** Salón 2: Al lado contrario (derecha), con pizarrón y butacas mirando a la IZQUIERDA **
    Salon salonA4 = new Salon(Salon.Orientacion.DERECHA);
    // Calculamos la posición simétrica en X
    Figuras.moverTG(salonA4, 11.42f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA4);

    Salon salonA2 = new Salon(Salon.Orientacion.IZQUIERDA);
    // Usamos la posición que ajustaste:
    Figuras.moverTG(salonA2, 18.0f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA2);

    Salon salonA3 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA3, 12.9f, 0.0f, 5.80f);
    Figuras.rotarTG(salonA3, 0, 180, 0);
    tgEdificio.addChild(salonA3);
    
    Salon salonA1 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA1, 19.5f, 0.0f, 5.80f);
    Figuras.rotarTG(salonA1, 0, 180, 0);
    tgEdificio.addChild(salonA1);

    // ** Aula Magna al lado del salón derecho, con pizarrón a la IZQUIERDA **
    AulaMagna aulaMagna = new AulaMagna(AulaMagna.Orientacion.IZQUIERDA); // <-- PASAR LA ORIENTACIÓN AQUÍ

    // El posicionamiento del Aula Magna completa no cambia
    float salonPosX_Derecho = (43.74f/2.0f - 0.2f) - 3.4f;
    float salonPosZ = (-20.0f/2.0f + 0.2f);
    float aulaMagnaPosX = salonPosX_Derecho - 35.0f;
    Figuras.moverTG(aulaMagna, aulaMagnaPosX, 0.0f, salonPosZ);
    tgEdificio.addChild(aulaMagna);

    // Creamos la instancia de la oficina
    Oficina oficina = new Oficina();

    float oficinaPosX = 5.6f;
    
    float oficinaPosZ = 4.8f;

    // Movemos la oficina a su lugar y la añadimos al edificio
    Figuras.moverTG(oficina, oficinaPosX, 0.0f, oficinaPosZ);
    tgEdificio.addChild(oficina);

    // --- 5. INICIO: AÑADIR LA ESCALERA EN U ---
    Escalera escalera = new Escalera();
    
    // La posicionamos al lado de la pared izquierda de la oficina.
    // El borde izquierdo de la oficina está en X = oficinaPosX.
    // La escalera tiene su origen en su esquina. La colocaremos pegada a la pared del edificio.
    float escaleraPosX = -oficinaPosX +7f; // Pegada a la misma pared que la oficina
    float escaleraPosY = 0.0f; // La base de la escalera está en el piso de la oficina
    // La colocamos justo detrás de la oficina
    float escaleraPosZ = oficinaPosZ +1f ; // Un valor de ejemplo para Z, detrás de la oficina

    Figuras.moverTG(escalera, escaleraPosX, escaleraPosY, escaleraPosZ);
    tgEdificio.addChild(escalera);
    // --- FIN: AÑADIR LA ESCALERA EN U ---


    // --- INICIO: AÑADIR LOS BAÑOS ---
    // Los colocaremos al lado izquierdo de la escalera

    // Baño de Hombres
    BañosHombres banoH = new BañosHombres();
    // Posición X: A la izquierda de la escalera (ancho de la escalera es ~4.2f)
    // Dejamos un pequeño pasillo de 1.0f de ancho
    float banoH_X = escaleraPosX -14f - 1.0f; // 5.0f es el ancho del baño
    float banoH_Z = 1.5f; // Misma profundidad Z que la escalera
    Figuras.moverTG(banoH, banoH_X, 0.0f, banoH_Z);
    tgEdificio.addChild(banoH);

    // Baño de Mujeres
    BañoMujeres banoM = new BañoMujeres();
    // Posición X: A la izquierda del baño de hombres
    float banoM_X = escaleraPosX - 7.5f - 1.0f; // Otro pasillo entre baños
    float banoM_Z = 1.5f;
    Figuras.moverTG(banoM, banoM_X, 0.0f, banoM_Z);
    tgEdificio.addChild(banoM);
    // --- FIN: AÑADIR LOS BAÑOS ---

        // Colisiones (Temporalmente desactivadas para pruebas)
        /*
        ArrayList<CajaColision> colisionesEdificio = edificioPrincipal.getCajasColision();
        cajasColision.addAll(colisionesEdificio);
        */

      // --- INICIO: AÑADIR PUERTA PRINCIPAL INTERACTIVA (VERSIÓN CORREGIDA) ---

// 1. Definir dimensiones y posición de la puerta. (Esto no cambia)
float anchoPuertaP = 1.0f;
float altoPuertaP = 2.2f;
float grosorPuertaP = 0.08f;
float puertaPosX = -0.5f;
float puertaPosY = 0.0f;
float puertaPosZ = 10.0f - (grosorPuertaP / 2f);

// 2. Crear el objeto Puerta usando el constructor corregido.
// Nota que ya no se le pasa una CajaColision.
Puerta puertaPrincipal = new Puerta(anchoPuertaP, altoPuertaP, grosorPuertaP, "puertaChica.png");

// 3. Posicionar la puerta y añadirla al edificio. (Esto no cambia)
Figuras.moverTG(puertaPrincipal, puertaPosX, puertaPosY, puertaPosZ);
tgEdificio.addChild(puertaPrincipal);

// 4. Una vez creada y posicionada la puerta, OBTENER su caja de colisión.
CajaColision colisionDeLaPuerta2 = puertaPrincipal.getCajaColision();

// 5. Registrar esa única caja de colisión en las listas correspondientes.
cajasColision.add(colisionDeLaPuerta); // Se añade a la lista de colisiones.
listaInteracciones.put(puertaPrincipal, colisionDeLaPuerta); // Se usa para el mapa de interacciones.

// --- FIN: AÑADIR PUERTA PRINCIPAL INTERACTIVA (VERSIÓN CORREGIDA) ---

    // --- INICIO: AÑADIR OFICINA DEL JEFE DE CARRERA ---
    OficinaJefeCarrera oficinaJefe = new OficinaJefeCarrera();

    Figuras.moverTG(oficinaJefe, 27f, 0.0f, -2.4f);
    Figuras.rotarTG(oficinaJefe, 0, -90, 0);
    tgEdificio.addChild(oficinaJefe);
    // --- FIN: AÑADIR OFICINA DEL JEFE DE CARRERA ---

    // --- AÑADIR EL LABORATORIO ---
    Laboratorio laboratorio = new Laboratorio();

    // Lo posicionamos, por ejemplo, al lado del Aula Magna
    // Necesitamos la posición X final del Aula Magna para colocarlo al lado
    // Asumiendo que el Aula Magna está en la posición que calculamos antes
    float aulaMagnaAncho = 7.0f;

    float labPosX = aulaMagnaPosX + aulaMagnaAncho - 17f; // A la derecha del Aula Magna, con un margen
    float labPosY = 0.0f;   // En el piso del edificio
    float labPosZ = -1.2f;  // Misma profundidad que los otros salones
    
    Figuras.moverTG(laboratorio, labPosX+0.5f, labPosY, labPosZ);
    Figuras.rotarTG(laboratorio, 0, 0, 0);
    tgEdificio.addChild(laboratorio);
    
    // --- AÑADIR EL LABORATORIO DE CÓMPUTO ---
    LaboratorioComputo labComputo = new LaboratorioComputo();

    // Lo posicionamos, por ejemplo, al lado del Aula Magna

    float labCPosX = aulaMagnaPosX + aulaMagnaAncho; // A la derecha del Aula Magna, con un pasillo
    float labCPosY = 0.0f;  // En el piso del edificio
    float labCPosZ = 5.8f;  

    Figuras.moverTG(labComputo, labPosX+13f, labPosY, labPosZ+11f);
    Figuras.rotarTG(labComputo, 0, 180, 0);
    tgEdificio.addChild(labComputo);

    // --- AÑADIR LA SALA DE HERRAMIENTAS ---
    salaHerramientas SalaHerramientas = new salaHerramientas();

    // Lo posicionamos, por ejemplo, al lado del Laboratorio
    // Necesitamos la posición X final del Laboratorio para colocarlo al lado
    float labAncho = 6.0f;
    float labHPosX = -2.4f; // Posición de ejemplo que usamos para el Laboratorio

    float salaHerramientasPosX = labPosX + labAncho + 1.0f; // A la derecha del Laboratorio, con un pasillo
    float salaHerramientasPosY = 0.0f;  // En el piso del edificio
    float salaHerramientasPosZ = -5.4f;

    Figuras.moverTG(SalaHerramientas, salaHerramientasPosX-2f, salaHerramientasPosY, salaHerramientasPosZ);
    tgEdificio.addChild(SalaHerramientas);

        System.out.println("ADVERTENCIA: Colisiones están desactivadas para esta prueba.");
    }
    
    private void construirLuces(){
        DirectionalLight luzDirecta = new DirectionalLight(new Color3f(1.0f, 1.0f, 0.9f), new Vector3f(-1f, -1f, -1f));
        luzDirecta.setInfluencingBounds(Figuras.getLimitesVisuales());
        tgAnclaCamara.addChild(luzDirecta);

        AmbientLight luzAmbiental = new AmbientLight(new Color3f(0.5f, 0.5f, 0.5f));
        luzAmbiental.setInfluencingBounds(Figuras.getLimitesVisuales());
        tgAnclaCamara.addChild(luzAmbiental);
    }

    private void construirTextoInteraccion(){
      tgTextoInteraccion = new TransformGroup();
      tgTextoInteraccion.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      Text2D textoInteraccionObj = new Text2D("Interactuar (E)", new Color3f(1f, 1f, 1f), "Arial", 30, java.awt.Font.BOLD);
       tgTextoInteraccion.addChild(textoInteraccionObj);
       Figuras.moverTG(tgTextoInteraccion, 0, -100, 0);
       bgRaiz.addChild(tgTextoInteraccion);
    }

    public BranchGroup getBgRaiz() { return bgRaiz; }
    public Muñeco getMuñeco() { return muñeco; }
    public TransformGroup getTgAnclaCamara() { return tgAnclaCamara; }
    public TransformGroup getTgMundo() { return tgMundo; }
    public ArrayList<CajaColision> getCajasColision() { return cajasColision; }
    public HashMap<Interaccion, CajaColision> getListaInteracciones() { return listaInteracciones; }

    public void mostrarTextoInteraccion(boolean mostrar) {
        if (tgTextoInteraccion != null) {
            Transform3D t3d = new Transform3D();
            if (mostrar) { t3d.setTranslation(new Vector3f(-0.15f, -0.7f, -2.0f)); }
            else { t3d.setTranslation(new Vector3f(0f, -100f, 0f)); }
            tgTextoInteraccion.setTransform(t3d);
        }
    }

    public boolean hayColision(float posXGlobalMundo, float posZGlobalMundo) {
    // ... (Tu lógica de colisión existente)
    // Dentro de tu bucle que revisa las 'cajasColision':
    for (CajaColision caja : cajasColision) {
        if (caja.habilitada) { // <-- AÑADIR ESTA COMPROBACIÓN
            // Si está habilitada, hacer la comprobación de colisión
            // ... tu lógica para ver si choca con 'caja'
        }
    }
    return false; // o true si hay colisión
}
}