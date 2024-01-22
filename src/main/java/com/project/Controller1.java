package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Controller1 implements initialize {

    @FXML
    private Button encriptar, back, seleccionarClave, seleccionarArchivo;

    @FXML
    private TextField clau, arxiu, desti;

    private File claveFile, archivoFile;

    @FXML
    private void encriptar(ActionEvent event) {
        try {
            if (claveFile == null || archivoFile == null) {
                // Mostrar un mensaje de error o manejar de otra manera si no se ha seleccionado la clave o el archivo
                return;
            }

            // Lee la clave pública desde el archivo seleccionado
            byte[] clavePublicaBytes = Files.readAllBytes(claveFile.toPath());
            String clavePublicaStr = new String(clavePublicaBytes);

            // Limpia la cadena de la clave pública eliminando espacios, saltos de línea, etc.
            clavePublicaStr = clavePublicaStr.replaceAll("\\s+", "");

            // Decodifica la clave pública desde Base64
            byte[] clavePublicaDecodificada = Base64.getDecoder().decode(clavePublicaStr);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(clavePublicaDecodificada);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // Lee el archivo desde el TextField
            String rutaArchivo = archivoFile.getAbsolutePath();
            byte[] datosArchivo = Files.readAllBytes(Path.of(rutaArchivo));

            // Inicializa Bouncy Castle
            Security.addProvider(new BouncyCastleProvider());

            // Cifra el archivo con la clave pública RSA
            Cipher cifrador = Cipher.getInstance("RSA", "BC");
            cifrador.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] datosCifrados = cifrador.doFinal(datosArchivo);

            // Obtiene la carpeta de la clave pública
            String carpetaClave = claveFile.getParent();

            // Construye la ruta completa para el archivo encriptado
            String nombreArchivoDestino = desti.getText()+".txt";
            String rutaDestino = carpetaClave + File.separator + nombreArchivoDestino;

            // Escribe los datos cifrados en el archivo de destino
            Files.write(Path.of(rutaDestino), datosCifrados, StandardOpenOption.CREATE);

            // Actualiza la vista o realiza otras acciones según tus necesidades
            UtilsViews.setViewAnimating("ok");
        } catch (Exception e) {
            e.printStackTrace();
             // Manejo adecuado de excepciones en tu aplicación
        }
    }

    @FXML
    private void back(ActionEvent event) {
        UtilsViews.setViewAnimating("layout1");
    }

    @FXML
    private void seleccionarClave(ActionEvent event) {
        // Abre un FileChooser para seleccionar la clave pública
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Clave Pública");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Clave Pública", "*.pub"));
        claveFile = fileChooser.showOpenDialog(null);

        // Actualiza el TextField con el nombre de la clave seleccionada
        if (claveFile != null) {
            clau.setText(claveFile.getName());
        }
    }

    @FXML
    private void seleccionarArchivo(ActionEvent event) {
        // Abre un FileChooser para seleccionar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");
        archivoFile = fileChooser.showOpenDialog(null);

        // Actualiza el TextField con el nombre del archivo seleccionado
        if (archivoFile != null) {
            arxiu.setText(archivoFile.getName());
        }
    }
}
