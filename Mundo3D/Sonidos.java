/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mundo3D;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author rubenmedina
 */

public class Sonidos {

    /**
     * Reproduce un archivo de sonido una vez.
     * @param nombreArchivoSonido El nombre del archivo de sonido (ej. "paso.wav").
     * Se espera que esté en "src/snd/" o una ruta similar.
     */
    public static void reproducirSonido(String nombreArchivoSonido) {
        AudioInputStream audioStream = null;
        try {
            // Construir la ruta al archivo de sonido. El ejemplo usa "src/snd/".
            // Asegúrate de que esta ruta sea correcta para tu proyecto.
            String rutaBase = new File("").getAbsolutePath() + File.separator + "src" + File.separator + "snd" + File.separator;
            File archivoSonido = new File(rutaBase + nombreArchivoSonido);

            if (!archivoSonido.exists()) {
                System.err.println("Error: Archivo de sonido no encontrado: " + rutaBase + nombreArchivoSonido);
                return;
            }

            audioStream = AudioSystem.getAudioInputStream(archivoSonido);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Reproduce el sonido una vez

            // No se usa clip.loop(Clip.LOOP_CONTINUOUSLY); porque el ejemplo original no lo hace,
            // lo que sugiere que es para efectos de sonido de una sola vez.
            // Si necesitas sonidos en bucle, se podría añadir otro método o un parámetro.

        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(Sonidos.class.getName()).log(Level.SEVERE, "Formato de audio no soportado: " + nombreArchivoSonido, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sonidos.class.getName()).log(Level.SEVERE, "Error de E/S al reproducir sonido: " + nombreArchivoSonido, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Sonidos.class.getName()).log(Level.SEVERE, "Línea de audio no disponible: " + nombreArchivoSonido, ex);
        } finally {
            // Es importante cerrar el AudioInputStream para liberar recursos,
            // pero hay que tener cuidado de no cerrarlo ANTES de que el Clip haya terminado de cargarlo,
            // especialmente si el sonido es muy corto o se reproduce en un hilo.
            // El ejemplo lo cierra en el finally, lo que podría ser problemático si `clip.start()`
            // no bloquea hasta que los datos son leídos. Sin embargo, para clips cortos que se cargan
            // completamente en memoria con `clip.open()`, esto suele funcionar.
            // Para mayor seguridad con sonidos largos o streaming, el cierre del stream y del clip
            // debería manejarse con `LineListener` para saber cuándo el clip ha terminado.
            if (audioStream != null) {
                try {
                    audioStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Sonidos.class.getName()).log(Level.WARNING, "Error al cerrar AudioInputStream.", ex);
                }
            }
        }
    }
}
