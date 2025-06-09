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
      MurosEdificio edificioPrincipal = new MurosEdificio("ladrillo.png"); 

    TransformGroup tgEdificio = edificioPrincipal.getTransformGroup();
    tgEdificio.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
    tgEdificio.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 

    edificioPrincipal.setPosicion(0.0f, -1.4f, 5.0f); 
    tgMundo.addChild(tgEdificio);

    float grosorMuro = 0.2f;
    float anchoPiso = 52f - (2 * grosorMuro);
    float profPiso = 20.0f - (2 * grosorMuro);
    float grosorPiso = 0.15f;
    float elevacionPiso = 0.06f;

  
    float posYCentroPisoInteriorLocal = -(grosorPiso / 2.0f) + elevacionPiso;

    Figuras.colocarCaja(tgEdificio,
        anchoPiso / 2.0f, grosorPiso / 2.0f, profPiso / 2.0f,
        0.0f, posYCentroPisoInteriorLocal, 0.0f, // Usamos la nueva posición Y
        0, 0, 0, "piso.png");

    Salon salonA6 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA6, -6.42f, 2.5f, -5.8f);
    tgEdificio.addChild(salonA6);
    Puerta puertaSalonA6 = new Puerta(0.9f, 2.0f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaA6 = new Transform3D();
    t3dPuertaA6.setTranslation(new Vector3f(-7f, +1.1f, 3.3f));
    puertaSalonA6.setTransform(t3dPuertaA6);
    tgMundo.addChild(puertaSalonA6);
    CajaColision colisionPuertaA6 = puertaSalonA6.getCajaColision();
    cajasColision.add(colisionPuertaA6);
    listaInteracciones.put(puertaSalonA6, colisionPuertaA6);

    float anchoVentanaSalon = 2.0f;
    float altoVentanaSalon = 1.2f;
    float grosorVentanaSalon = 0.06f;
    float alturaBaseVentana = 1.0f;

    float ventana1_X_local = -2.417f; 
    float ventana2_X_local = 0.416f;  
    VentanaDoble ventanaA6_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon-2.3f, grosorVentanaSalon);
    Figuras.moverTG(ventanaA6_1, ventana1_X_local, alturaBaseVentana+1.1f, -4.1f);
    salonA6.addChild(ventanaA6_1); 
    for (CajaColision caja : ventanaA6_1.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA6_1, ventanaA6_1.getCajasColision().get(0));

    VentanaDoble ventanaA6_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA6_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA6.addChild(ventanaA6_2);
    for (CajaColision caja : ventanaA6_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA6_2, ventanaA6_2.getCajasColision().get(0));
    
    
    Salon salonA5 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA5, -6.42f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA5);
    float anchoPuertaSalon = 0.9f;
    float altoPuertaSalon = 2.0f;
    float grosorPuertaSalon = 0.08f;
    Puerta puertaSalonA5 = new Puerta(anchoPuertaSalon, altoPuertaSalon, grosorPuertaSalon, "puertaAula.png");
    float puertaGlobalX = -6.42f; 
    float puertaGlobalY = -1.0f;  
    float puertaGlobalZ = -5.8f + (8.2f / 2.0f);
    Transform3D t3dPuerta = new Transform3D();
    t3dPuerta.setTranslation(new Vector3f(puertaGlobalX+-0.46f, puertaGlobalY-0.44f, puertaGlobalZ+5.f));
    puertaSalonA5.setTransform(t3dPuerta);
    tgMundo.addChild(puertaSalonA5);
    CajaColision colisionDeLaPuerta = puertaSalonA5.getCajaColision();
    cajasColision.add(colisionDeLaPuerta);
    listaInteracciones.put(puertaSalonA5, colisionDeLaPuerta);
    
    VentanaDoble ventanaA5_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA5_1, ventana1_X_local, alturaBaseVentana, -4.1f);
    salonA5.addChild(ventanaA5_1);
    for (CajaColision caja : ventanaA5_1.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA5_1, ventanaA5_1.getCajasColision().get(0));

    VentanaDoble ventanaA5_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon+1f, grosorVentanaSalon);
    Figuras.moverTG(ventanaA5_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA5.addChild(ventanaA5_2);
    for (CajaColision caja : ventanaA5_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA5_2, ventanaA5_2.getCajasColision().get(0));


    Salon salonA4 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA4, 11.42f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA4);
    Puerta puertaSalonA4 = new Puerta(0.9f, 2.0f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaA4 = new Transform3D();
    t3dPuertaA4.setTranslation(new Vector3f(11f, -1.42f, 3.3f));
    puertaSalonA4.setTransform(t3dPuertaA4);
    tgMundo.addChild(puertaSalonA4);
    CajaColision colisionPuertaA4 = puertaSalonA4.getCajaColision();
    cajasColision.add(colisionPuertaA4);
    listaInteracciones.put(puertaSalonA4, colisionPuertaA4);
    VentanaDoble ventanaA4_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA4_1, ventana1_X_local, alturaBaseVentana, -4.1f);
    salonA4.addChild(ventanaA4_1);
    for (CajaColision caja : ventanaA4_1.getCajasColision()) { cajasColision.add(caja); }  
    listaInteracciones.put(ventanaA4_1, ventanaA4_1.getCajasColision().get(0));

    VentanaDoble ventanaA4_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA4_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA4.addChild(ventanaA4_2);
    for (CajaColision caja : ventanaA4_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA4_2, ventanaA4_2.getCajasColision().get(0));


    Salon salonA2 = new Salon(Salon.Orientacion.IZQUIERDA);
    Figuras.moverTG(salonA2, 18.0f, 0.0f, -5.8f);
    tgEdificio.addChild(salonA2);
    Puerta puertaSalonA2 = new Puerta(1f, 2.0f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaA2 = new Transform3D();
    t3dPuertaA2.setTranslation(new Vector3f(17.5f, -1.42f, 3.3f));
    puertaSalonA2.setTransform(t3dPuertaA2);
    tgMundo.addChild(puertaSalonA2);
    CajaColision colisionPuertaA2 = puertaSalonA2.getCajaColision();
    cajasColision.add(colisionPuertaA2);
    listaInteracciones.put(puertaSalonA2, colisionPuertaA2);
    VentanaDoble ventanaA2_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA2_1, ventana1_X_local, alturaBaseVentana, -4.1f);
    salonA2.addChild(ventanaA2_1);
    for (CajaColision caja : ventanaA2_1.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA2_1, ventanaA2_1.getCajasColision().get(0));

    VentanaDoble ventanaA2_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA2_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA2.addChild(ventanaA2_2);
    for (CajaColision caja : ventanaA2_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA2_2, ventanaA2_2.getCajasColision().get(0));



    Salon salonA3 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA3, 12.9f, 0.0f, 5.80f);
    Figuras.rotarTG(salonA3, 0, 180, 0);
    tgEdificio.addChild(salonA3);
    Puerta puertaSalonA3 = new Puerta(1f, 2.0f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaA3 = new Transform3D();
    t3dPuertaA3.setTranslation(new Vector3f(12.4f, -1.42f, 6.7f));
    puertaSalonA3.setTransform(t3dPuertaA3);
    tgMundo.addChild(puertaSalonA3);
    CajaColision colisionPuertaA3 = puertaSalonA3.getCajaColision();
    cajasColision.add(colisionPuertaA3);
    listaInteracciones.put(puertaSalonA3, colisionPuertaA3);
    VentanaDoble ventanaA3_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA3_1, ventana1_X_local, alturaBaseVentana, -4.1f);
    salonA3.addChild(ventanaA3_1);
    for (CajaColision caja : ventanaA3_1.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA3_1, ventanaA3_1.getCajasColision().get(0));

    VentanaDoble ventanaA3_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA3_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA3.addChild(ventanaA3_2);
    for (CajaColision caja : ventanaA3_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA3_2, ventanaA3_2.getCajasColision().get(0));

    
    Salon salonA1 = new Salon(Salon.Orientacion.DERECHA);
    Figuras.moverTG(salonA1, 19.5f, 0.0f, 5.80f);
    Figuras.rotarTG(salonA1, 0, 180, 0);
    tgEdificio.addChild(salonA1);
    Puerta puertaSalonA1 = new Puerta(1f, 2.0f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaA1 = new Transform3D();
    t3dPuertaA1.setTranslation(new Vector3f(19f, -1.42f, 6.7f));
    puertaSalonA1.setTransform(t3dPuertaA1);
    tgMundo.addChild(puertaSalonA1);
    CajaColision colisionPuertaA1 = puertaSalonA1.getCajaColision();
    cajasColision.add(colisionPuertaA3);
    listaInteracciones.put(puertaSalonA1, colisionPuertaA1);
    VentanaDoble ventanaA1_1 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA1_1, ventana1_X_local, alturaBaseVentana, -4.1f);
    salonA1.addChild(ventanaA1_1);
    for (CajaColision caja : ventanaA1_1.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA1_1, ventanaA1_1.getCajasColision().get(0));

    VentanaDoble ventanaA1_2 = new VentanaDoble(anchoVentanaSalon, altoVentanaSalon, grosorVentanaSalon);
    Figuras.moverTG(ventanaA1_2, ventana2_X_local, alturaBaseVentana, -4.1f);
    salonA1.addChild(ventanaA1_2);
    for (CajaColision caja : ventanaA1_2.getCajasColision()) { cajasColision.add(caja); }
    listaInteracciones.put(ventanaA1_2, ventanaA1_2.getCajasColision().get(0));


    AulaMagna aulaMagna = new AulaMagna(AulaMagna.Orientacion.IZQUIERDA); 
    float salonPosX_Derecho = (43.74f/2.0f - 0.2f) - 3.4f;
    float salonPosZ = (-20.0f/2.0f + 0.2f);
    float aulaMagnaPosX = salonPosX_Derecho - 35.0f;
    Figuras.moverTG(aulaMagna, aulaMagnaPosX, 0.0f, salonPosZ);
    tgEdificio.addChild(aulaMagna);
    Puerta puertaMagna = new Puerta(0.9f, 2.1f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaMagna = new Transform3D();
    t3dPuertaMagna.setTranslation(new Vector3f(-10.7f, -1.3f, 3.64f));
    puertaMagna.setTransform(t3dPuertaMagna);
    tgMundo.addChild(puertaMagna);
    CajaColision colisionPuertaMagna = puertaMagna.getCajaColision();
    cajasColision.add(colisionPuertaMagna);
    listaInteracciones.put(puertaMagna, colisionPuertaMagna);
    float aulaMagnaBaseX = -16.73f;
    float aulaMagnaBaseY = -1.4f; 
    float aulaMagnaBaseZ = -9.8f;
    float anchoVentanaAM = 2.5f;
    float altoVentanaAM = 1.5f;
    float grosorVentanaAM = 0.06f;
    float alturaBaseVentanaAM = 1.0f; 
    VentanaDoble ventanaAM1 = new VentanaDoble(anchoVentanaAM, altoVentanaAM, grosorVentanaAM);
    float ventana1X_AM = aulaMagnaBaseX + 0.667f;
    float ventana1Y_AM = aulaMagnaBaseY + alturaBaseVentanaAM;
    float ventana1Z_AM = aulaMagnaBaseZ + 0.0f;
    Transform3D t3dVenAM1 = new Transform3D();
    t3dVenAM1.setTranslation(new Vector3f(ventana1X_AM, ventana1Y_AM+1.4f, ventana1Z_AM));
    ventanaAM1.setTransform(t3dVenAM1);
    tgEdificio.addChild(ventanaAM1);
    for (CajaColision caja : ventanaAM1.getCajasColision()) {
    cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaAM1, ventanaAM1.getCajasColision().get(0));

    VentanaDoble ventanaAM2 = new VentanaDoble(anchoVentanaAM, altoVentanaAM, grosorVentanaAM);
    float ventana2X_AM = aulaMagnaBaseX + 3.834f;
    float ventana2Y_AM = aulaMagnaBaseY + alturaBaseVentanaAM; 
    float ventana2Z_AM = aulaMagnaBaseZ + 0.0f; 
    Transform3D t3dVenAM2 = new Transform3D();
    t3dVenAM2.setTranslation(new Vector3f(ventana2X_AM, ventana2Y_AM+1.4f, ventana2Z_AM));
    ventanaAM2.setTransform(t3dVenAM2);
    tgEdificio.addChild(ventanaAM2);
    for (CajaColision caja : ventanaAM2.getCajasColision()) {
    cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaAM2, ventanaAM2.getCajasColision().get(0));
    

    Oficina oficina = new Oficina();
    float oficinaPosX = 5.6f;
    float oficinaPosZ = 4.8f;
    Figuras.moverTG(oficina, oficinaPosX, 0.0f, oficinaPosZ);
    tgEdificio.addChild(oficina);

    Puerta puertaOficina = new Puerta(0.9f, 2.1f, 0.08f, "puertaChica.png");
    Transform3D t3dPuertaOficina = new Transform3D();
    t3dPuertaOficina.setTranslation(new Vector3f(8.7f, 0f, 4.8f)); 
    puertaOficina.setTransform(t3dPuertaOficina);
    tgEdificio.addChild(puertaOficina); 
    CajaColision colisionPuertaOficina = puertaOficina.getCajaColision();
    cajasColision.add(colisionPuertaOficina);
    listaInteracciones.put(puertaOficina, colisionPuertaOficina);

    float anchoVentanaOficina = 1.5f; 
    float altoVentanaOficina = 2.5f;  
    float grosorVentanaOficina = 0.06f;  
    VentanaDoble ventanaOficina = new VentanaDoble(anchoVentanaOficina, altoVentanaOficina, grosorVentanaOficina);
    float ventanaGlobalX = 5.6f;
    float ventanaGlobalY = -1.4f + 1.0f; 
    float ventanaGlobalZ = 4.8f;
    Figuras.moverTG(ventanaOficina, ventanaGlobalX, ventanaGlobalY, ventanaGlobalZ);
    tgEdificio.addChild(ventanaOficina); 
    for (CajaColision caja : ventanaOficina.getCajasColision()) {
        cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaOficina, ventanaOficina.getCajasColision().get(0));


    Escalera escalera = new Escalera();
    float escaleraPosX = -oficinaPosX +7f; 
    float escaleraPosY = 0.0f; 
    float escaleraPosZ = oficinaPosZ +1f ; 
    Figuras.moverTG(escalera, escaleraPosX, escaleraPosY, escaleraPosZ);
    tgEdificio.addChild(escalera);


    BañosHombres banoH = new BañosHombres();
    float banoH_X = escaleraPosX -14f - 1.0f; 
    float banoH_Z = 1.5f; 
    Figuras.moverTG(banoH, banoH_X, 0.0f, banoH_Z);
    tgEdificio.addChild(banoH);
    Puerta puertaBañoH = new Puerta(1f, 2.08f, 0.08f, "PuertaBañoH.png");
    Transform3D t3dPuertaBañoH = new Transform3D();
    t3dPuertaBañoH.setTranslation(new Vector3f(-13.6f, -1.4f, 6.5f));
    puertaBañoH.setTransform(t3dPuertaBañoH);
    tgMundo.addChild(puertaBañoH);
    CajaColision colisionPuertaBañoH = puertaBañoH.getCajaColision();
    cajasColision.add(colisionPuertaBañoH);
    listaInteracciones.put(puertaBañoH, colisionPuertaBañoH);

    
    BañoMujeres banoM = new BañoMujeres();
    float banoM_X = escaleraPosX - 7.5f - 1.0f; 
    float banoM_Z = 1.5f;
    Figuras.moverTG(banoM, banoM_X, 0.0f, banoM_Z);
    tgEdificio.addChild(banoM);
    Puerta puertaBañoM = new Puerta(1f, 2.08f, 0.08f, "PuertaBañoM.png");
    Transform3D t3dPuertaBañoM = new Transform3D();
    t3dPuertaBañoM.setTranslation(new Vector3f(-1.6f, -1.4f, 6.5f));
    puertaBañoM.setTransform(t3dPuertaBañoM);
    tgMundo.addChild(puertaBañoM);
    CajaColision colisionPuertaBañoM = puertaBañoM.getCajaColision();
    cajasColision.add(colisionPuertaBañoM);
    listaInteracciones.put(puertaBañoM, colisionPuertaBañoM);
   
    
    
    float anchoPuertaP = 2f;
    float altoPuertaP = 2.2f;
    float grosorPuertaP = 0.08f;
    float puertaPosX = -0.5f;
    float puertaPosY = 0.0f;
    float puertaPosZ = 10.0f - (grosorPuertaP / 2f);
    Puerta puertaPrincipal = new Puerta(anchoPuertaP, altoPuertaP, grosorPuertaP, "puertaChica.png");
    Figuras.moverTG(puertaPrincipal, puertaPosX-0.2f, puertaPosY, puertaPosZ-0.5f);
    tgEdificio.addChild(puertaPrincipal);
    CajaColision colisionDeLaPuerta1 = puertaPrincipal.getCajaColision();
    cajasColision.add(colisionDeLaPuerta); 
    listaInteracciones.put(puertaPrincipal, colisionDeLaPuerta);
   

    OficinaJefeCarrera oficinaJefe = new OficinaJefeCarrera();
    Figuras.moverTG(oficinaJefe, 27f, 0.0f, -2.4f);
    Figuras.rotarTG(oficinaJefe, 0, -90, 0);
    tgEdificio.addChild(oficinaJefe);
    Puerta puertaJefe = new Puerta(1f, 2.3f, 0.08f, "puertaChica.png");
    Transform3D t3dPuertaJefe = new Transform3D();
    t3dPuertaJefe.setTranslation(new Vector3f(21f, -1.42f, 3.7f));
    puertaJefe.setTransform(t3dPuertaJefe);
    Figuras.rotarTG(puertaJefe, 0, -90, 0);
    tgMundo.addChild(puertaJefe);
    CajaColision colisionPuertaJefe = puertaJefe.getCajaColision();
    cajasColision.add(colisionPuertaJefe);
    listaInteracciones.put(puertaJefe, colisionPuertaJefe);
    float anchoVentanaFrontal = 1.8f;
    float altoVentanaFrontal = 1.2f;
    float grosorVentana = 0.06f;
    float ventana1J_X_local = 2.2f; 
    float ventana1J_Y_local = alturaBaseVentana;
    float ventana1J_Z_local = 6.0f; 
    VentanaDoble ventanaOJ_Frontal = new VentanaDoble(anchoVentanaFrontal, altoVentanaFrontal, grosorVentana);
    Figuras.moverTG(ventanaOJ_Frontal, ventana1J_X_local, ventana1J_Y_local, ventana1J_Z_local);
    oficinaJefe.addChild(ventanaOJ_Frontal);
    for (CajaColision caja : ventanaOJ_Frontal.getCajasColision()) {
    cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaOJ_Frontal, ventanaOJ_Frontal.getCajasColision().get(0));

    float anchoVentanaInterior = 2.0f;
    float altoVentanaInterior = 1.2f;
    float ventana2J_X_local = 1.6f;
    float ventana2J_Y_local = alturaBaseVentana;
    float ventana2J_Z_local = 3.0f;
    VentanaDoble ventanaOJ_Interior = new VentanaDoble(anchoVentanaInterior, altoVentanaInterior, grosorVentana);
    Figuras.moverTG(ventanaOJ_Interior, ventana2J_X_local, ventana2J_Y_local, ventana2J_Z_local);
    oficinaJefe.addChild(ventanaOJ_Interior); 
    for (CajaColision caja : ventanaOJ_Interior.getCajasColision()) {
    cajasColision.add(caja);
        }
    listaInteracciones.put(ventanaOJ_Interior, ventanaOJ_Interior.getCajasColision().get(0));

    Laboratorio laboratorio = new Laboratorio();
    float aulaMagnaAncho = 7.0f;
    float labPosX = aulaMagnaPosX + aulaMagnaAncho - 17f; 
    float labPosY = 0.0f;   
    float labPosZ = -1.2f;  
    Figuras.moverTG(laboratorio, labPosX+0.5f, labPosY, labPosZ);
    Figuras.rotarTG(laboratorio, 0, 0, 0);
    tgEdificio.addChild(laboratorio);
    Puerta puertaLab = new Puerta(1f, 2.4f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaLab = new Transform3D();
    t3dPuertaLab.setTranslation(new Vector3f(-20f, -1.4f, 3.8f));
    puertaLab.setTransform(t3dPuertaLab);
    Figuras.rotarTG(puertaLab, 0, -90, 0);
    tgMundo.addChild(puertaLab);
    CajaColision colisionPuertaLab = puertaLab.getCajaColision();
    cajasColision.add(colisionPuertaLab);
    listaInteracciones.put(puertaLab, colisionPuertaLab);
     float labBaseX = -2.4f;
    float labBaseY = -1.4f; 
    float labBaseZ = -1.2f;

    float anchoVentanaLab = 2f; 
    float altoVentanaLab = 1.2f;   
    float grosorVentanaLab = 0.06f;
    float alturaBaseVentanaLab = 1.0f; 
    VentanaDoble ventanaLab1 = new VentanaDoble(anchoVentanaLab, altoVentanaLab, grosorVentanaLab);

    float ventana1X = labBaseX + 0.8f;
    float ventana1Y = labBaseY + alturaBaseVentanaLab;
    float ventana1Z = labBaseZ + 6.0f;

    Transform3D t3dVen1 = new Transform3D();
    t3dVen1.setTranslation(new Vector3f(ventana1X-24f, ventana1Y+1.4f, ventana1Z));
    ventanaLab1.setTransform(t3dVen1);

    tgEdificio.addChild(ventanaLab1);

    for (CajaColision caja : ventanaLab1.getCajasColision()) {
         cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaLab1, ventanaLab1.getCajasColision().get(0));

    VentanaDoble ventanaLab2 = new VentanaDoble(anchoVentanaLab, altoVentanaLab, grosorVentanaLab);
    float ventana2X = labBaseX + 3.4f;
    float ventana2Y = labBaseY + alturaBaseVentanaLab; 
    float ventana2Z = labBaseZ + 6.0f; 
    Transform3D t3dVen2 = new Transform3D();
    t3dVen2.setTranslation(new Vector3f(ventana2X-24f, ventana2Y+1.4f, ventana2Z));
    ventanaLab2.setTransform(t3dVen2);
    tgEdificio.addChild(ventanaLab2);
    for (CajaColision caja : ventanaLab2.getCajasColision()) {
        cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaLab2, ventanaLab2.getCajasColision().get(0));

    VentanaDoble ventanaLab3 = new VentanaDoble(anchoVentanaLab, altoVentanaLab, grosorVentanaLab);
    Figuras.rotarTG(ventanaLab3, 0, -90, 0);
    float ventana3X = labBaseX + 3.4f;
    float ventana3Y = labBaseY + alturaBaseVentanaLab;
    float ventana3Z = labBaseZ + 6.0f;
    Figuras.moverTG(ventanaLab3, ventana3X-2.3f, ventana3Y+1.4f, ventana3Z+21.5f);
    tgEdificio.addChild(ventanaLab3);

    for (CajaColision caja : ventanaLab3.getCajasColision()) {
    cajasColision.add(caja);
    }
    listaInteracciones.put(ventanaLab3, ventanaLab3.getCajasColision().get(0));

 
    LaboratorioComputo labComputo = new LaboratorioComputo();
    float labCPosX = aulaMagnaPosX + aulaMagnaAncho; 
    float labCPosY = 0.0f; 
    float labCPosZ = 5.8f;  
    Figuras.moverTG(labComputo, labPosX+13f, labPosY, labPosZ+11f);
    Figuras.rotarTG(labComputo, 0, 180, 0);
    tgEdificio.addChild(labComputo);
    Puerta puertaLabC = new Puerta(1f, 2.08f, 0.08f, "puertaAula.png");
    Transform3D t3dPuertaLabC = new Transform3D();
    t3dPuertaLabC.setTranslation(new Vector3f(-20.2f, -1.4f, 6.6f));
    puertaLabC.setTransform(t3dPuertaLabC);
    tgMundo.addChild(puertaLabC);
    CajaColision colisionPuertaLabC = puertaLabC.getCajaColision();
    cajasColision.add(colisionPuertaLabC);
    listaInteracciones.put(puertaLabC, colisionPuertaLabC);
   
    
    salaHerramientas SalaHerramientas = new salaHerramientas();
    float labAncho = 6.0f;
    float labHPosX = -2.4f;
    float salaHerramientasPosX = labPosX + labAncho + 1.0f; 
    float salaHerramientasPosY = 0.0f;  
    float salaHerramientasPosZ = -5.4f;
    Figuras.moverTG(SalaHerramientas, salaHerramientasPosX-2f, salaHerramientasPosY, salaHerramientasPosZ);
    tgEdificio.addChild(SalaHerramientas);
    Puerta puertaHerramientas = new Puerta(0.9f, 2.1f, 0.08f, "puertaChica.png");
    Transform3D t3dPuertaHerramientas = new Transform3D();
    t3dPuertaHerramientas.setTranslation(new Vector3f(-17.7f, -1.3f, 3.64f));
    puertaHerramientas.setTransform(t3dPuertaHerramientas);
    tgMundo.addChild(puertaHerramientas);
    CajaColision colisionPuertaHerramientas = puertaHerramientas.getCajaColision();
    cajasColision.add(colisionPuertaHerramientas);
    listaInteracciones.put(puertaHerramientas, colisionPuertaHerramientas);
    
    
    
    float anchoPuertaTrasera = 11.4f;
    float alturaPuertaTrasera = 2.0f;
    float grosorPuertaTrasera = 0.1f; 
    PuertaDoble puertaTrasera = new PuertaDoble(anchoPuertaTrasera, alturaPuertaTrasera, grosorPuertaTrasera, "PuertaGrande.png");
    float puertaGGlobalX = 3.0f - (anchoPuertaTrasera / 2.0f); 
    float puertaGGlobalY = -3f; 
    float puertaGGlobalZ = 5.0f - (20.0f / 2.0f); 
    Transform3D t3dPuertaTrasera = new Transform3D();
    t3dPuertaTrasera.setTranslation(new Vector3f(puertaGlobalX+3.2f, puertaGlobalY+1f, puertaGlobalZ-8.4f));
    puertaTrasera.setTransform(t3dPuertaTrasera);
    tgEdificio.addChild(puertaTrasera);
    for (CajaColision caja : puertaTrasera.getCajasColision()) {
        cajasColision.add(caja); 
    }
    listaInteracciones.put(puertaTrasera, puertaTrasera.getCajasColision().get(0));
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
    for (CajaColision caja : cajasColision) {
        if (caja.habilitada) { 
            
        }
    }
    return false;
}
}