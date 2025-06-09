/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.nio.charset.StandardCharsets;
import javax.vecmath.Point3d;

/**
 *
 * @author rubenmedina
 */


public class Mundo3D extends JFrame implements KeyListener, MouseMotionListener, MouseListener, SerialPortDataListener {

    private EscenaPrincipal escena;
    private SimpleUniverse universo;
    private Canvas3D lienzo;

    private HashMap<Integer, Boolean> teclasPresionadas = new HashMap<>();
    private float velocidadMovimiento = 0.1f;

    // Atributos para control con mouse
    private int mouseXprevio = -1;
    private float sensibilidadMouse = 0.2f;

    // Atributos para Joystick
    private SerialPort serialPort;
    private volatile int joystickX = 512;
    private volatile int joystickY = 512;
    private StringBuilder serialBuffer = new StringBuilder();
    private static final int JOYSTICK_CENTRO = 512;
    private static final float JOYSTICK_RANGO = 512.0f;
    private static final float JOYSTICK_DEADZONE = 0.15f;

    public Mundo3D() {
    super("Mundo 3D Escolar - Estructura Nueva");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);

    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
    lienzo = new Canvas3D(config);
    add(lienzo);

    universo = new SimpleUniverse(lienzo);
    escena = new EscenaPrincipal();
    universo.addBranchGraph(escena.getBgRaiz());
    universo.getViewingPlatform().setNominalViewingTransform();

    lienzo.addKeyListener(this);
    lienzo.addMouseMotionListener(this);
    lienzo.addMouseListener(this);
    inicializarJoystick();

    new Thread(this::actualizarMovimientoYCamara).start();

    setVisible(true);
    lienzo.requestFocusInWindow();
    }

     // --- Métodos de Mouse ---
    @Override public void mousePressed(MouseEvent e) { mouseXprevio = e.getX(); }
    @Override public void mouseReleased(MouseEvent e) { mouseXprevio = -1; }
    @Override public void mouseDragged(MouseEvent e) { procesarMovimientoMouse(e.getX()); }
    @Override public void mouseMoved(MouseEvent e) { mouseXprevio = e.getX(); }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    
    @Override
public void keyPressed(KeyEvent e) {
     teclasPresionadas.put(e.getKeyCode(), true);

    // --- LÓGICA DE INTERACCIÓN MODIFICADA ---
    if (e.getKeyCode() == KeyEvent.VK_E) {
        // Cuando se presiona 'E', ahora activamos todas las puertas a la vez.
        activarTodasLasPuertas(); 
    }
}

private void activarTodasLasPuertas() {
    // 1. Validar que la escena y la lista de interacciones existan.
    if (escena == null || escena.getListaInteracciones().isEmpty()) {
        return;
    }

    // 2. Obtener el mapa de todos los objetos interactivos.
    // La clave de este mapa es el objeto que implementa Interaccion (nuestras puertas).
    HashMap<Interaccion, CajaColision> interacciones = escena.getListaInteracciones();

    // 3. Recorrer cada objeto interactivo y llamar a su método accion().
    // Esto hará que cada puerta inicie su animación de abrir/cerrar.
    for (Interaccion objeto : interacciones.keySet()) {
        objeto.accion(); // Llama al método accion() de cada puerta.
    }
}

    // --- Métodos de Teclado ---
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) { teclasPresionadas.put(e.getKeyCode(), false); }

    private void procesarMovimientoMouse(int posXActualMouse) {
    if (mouseXprevio != -1) {
        int deltaX = posXActualMouse - mouseXprevio;
        float rotacionY = deltaX * sensibilidadMouse;
        GirarCamaraHorizontal(-rotacionY);
    }
    mouseXprevio = posXActualMouse;
    }

    private void GirarCamaraHorizontal(float grados) {
    if (escena != null && escena.getTgAnclaCamara() != null) {
    Figuras.rotarTG(escena.getTgAnclaCamara(), 0, (int)grados, 0);
    }
    }

    private void moverMundoYAnimar(float deltaX_local, float deltaZ_local) {
    if (escena == null || escena.getTgMundo() == null || escena.getTgAnclaCamara() == null) return;

    Transform3D t3dAncla = new Transform3D();
    escena.getTgAnclaCamara().getTransform(t3dAncla);
    Matrix3f matrizRotacion = new Matrix3f();
    t3dAncla.getRotationScale(matrizRotacion);
    
    Vector3f movLocal = new Vector3f(deltaX_local, 0, deltaZ_local);
    matrizRotacion.transform(movLocal);
    
     // --- LÓGICA DE COLISIÓN ---
    Transform3D t3dMundoActual = new Transform3D();
    escena.getTgMundo().getTransform(t3dMundoActual);
    Vector3f posMundoActual = new Vector3f();
    t3dMundoActual.get(posMundoActual);
    
    // Calcular la posición del mundo en el siguiente frame
    float proxMundoX = posMundoActual.x - movLocal.x;
    float proxMundoZ = posMundoActual.z - movLocal.z;

    // Revisar si esa próxima posición choca con algo
    if (escena.hayColision(proxMundoX, proxMundoZ)) {
    // Si hay colisión, CANCELAMOS el movimiento y la animación
    if (escena.getMuñeco() != null && escena.getMuñeco().estaAnimado()) {
    escena.getMuñeco().detenerCaminar();
    }
    return;
    }
    // --- FIN DE LÓGICA DE COLISIÓN ---

    // Si no hay colisión, proceder a mover el mundo
    Figuras.moverTG(escena.getTgMundo(), -movLocal.x, 0, -movLocal.z);

    // Lógica de animación
    if (escena.getMuñeco() != null) {
    if ((deltaX_local != 0 || deltaZ_local != 0) && !escena.getMuñeco().estaAnimado()) {
         escena.getMuñeco().caminar();
    } else if (deltaX_local == 0 && deltaZ_local == 0 && escena.getMuñeco().estaAnimado()) {
        escena.getMuñeco().detenerCaminar();
        }
    }

   }

        private void actualizarMovimientoYCamara() {
            while (true) {
            float deltaX_local = 0;
            float deltaZ_local = 0;

            // --- Procesar Teclado (Input local: adelante es +Z) ---
            if (teclasPresionadas.getOrDefault(KeyEvent.VK_W, false)) deltaZ_local -= velocidadMovimiento;
            if (teclasPresionadas.getOrDefault(KeyEvent.VK_S, false)) deltaZ_local += velocidadMovimiento;
            if (teclasPresionadas.getOrDefault(KeyEvent.VK_A, false)) deltaX_local -= velocidadMovimiento;
            if (teclasPresionadas.getOrDefault(KeyEvent.VK_D, false)) deltaX_local += velocidadMovimiento;

            // --- Procesar Joystick ---
            float joyXNorm = (joystickX - JOYSTICK_CENTRO) / JOYSTICK_RANGO;
            float joyYNorm = (JOYSTICK_CENTRO - joystickY) / JOYSTICK_RANGO;

            if (Math.abs(joyXNorm) < JOYSTICK_DEADZONE) joyXNorm = 0f;
            if (Math.abs(joyYNorm) < JOYSTICK_DEADZONE) joyYNorm = 0f;
            
            if (joyXNorm != 0 || joyYNorm != 0) {
            deltaX_local += joyXNorm * velocidadMovimiento;
                deltaZ_local += joyYNorm * -velocidadMovimiento; // Invertir Y para que arriba sea -Z
            }

            // Normalizar para evitar movimiento diagonal más rápido
            if (deltaX_local != 0 && deltaZ_local != 0) {
                deltaX_local *= 0.7071f;
                deltaZ_local *= 0.7071f;
            }

            // Aplicar movimiento si hay input
            if (deltaX_local != 0 || deltaZ_local != 0) {
            // La colisión se debería verificar aquí antes de mover
            moverMundoYAnimar(deltaX_local, deltaZ_local);
            } else {
                if (escena.getMuñeco() != null && escena.getMuñeco().estaAnimado()) {
                    escena.getMuñeco().detenerCaminar();
                }
            }

            // La cámara ya no se actualiza explícitamente aquí, su ancla se rota por el mouse
            // y el personaje (que es hijo del ancla) se queda en su centro.

        try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Métodos de Joystick ---
    private void inicializarJoystick() {
    String nombrePuerto = "COM3"; // ¡ASEGÚRATE DE QUE ESTE SEA TU PUERTO CORRECTO!
        System.out.println("Intentando conectar al joystick en el puerto: " + nombrePuerto);
        serialPort = SerialPort.getCommPort(nombrePuerto);
        serialPort.setBaudRate(9600);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

        if (serialPort.openPort()) {
            System.out.println("Puerto serial abierto para Joystick: " + nombrePuerto);
            serialPort.addDataListener(this);
        } else {
            System.err.println("Error al abrir el puerto serial para Joystick: " + nombrePuerto);
        }
    }
    
    @Override
    public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
        try {
            byte[] newData = new byte[serialPort.bytesAvailable()];
            serialPort.readBytes(newData, newData.length);
             String dataChunk = new String(newData, StandardCharsets.UTF_8);
            serialBuffer.append(dataChunk);

            String bufferContenido = serialBuffer.toString();
            int lastNewline = bufferContenido.lastIndexOf('\n');
            if (lastNewline >= 0) {
                String lineasCompletas = bufferContenido.substring(0, lastNewline + 1);
                serialBuffer = new StringBuilder(bufferContenido.substring(lastNewline + 1));

                String[] lineas = lineasCompletas.split("\\r?\\n");
                if (lineas.length > 0) {
                      String ultimaLinea = lineas[lineas.length - 1].trim();
                    if (!ultimaLinea.isEmpty()) {
                        String[] valores = ultimaLinea.split(",");
                            if (valores.length >= 2) {
                            joystickX = Integer.parseInt(valores[0].trim());
                            joystickY = Integer.parseInt(valores[1].trim());
                         }
                     }
                 }
            }
        } catch (Exception e) {   
      }
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Mundo3D());
    }

}