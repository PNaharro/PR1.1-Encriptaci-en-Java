package com.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Controller2 implements initialize {

    @FXML
    private Button desencriptar, back, seleccionarArchivo, seleccionarClave;

    @FXML
    private TextField arxiu_p, clau_p, contra, desti_p;

    private File clavePrivadaFile, archivoEncriptadoFile;

    @FXML
    private void desencriptar(ActionEvent event) {
        try {
            if (clavePrivadaFile == null || archivoEncriptadoFile == null) {
                // Mostrar un mensaje de error o manejar de otra manera si no se ha seleccionado la clave privada o el archivo encriptado
                return;
            }

            // Lee la clave privada desde el archivo seleccionado
            byte[] clavePrivadaBytes = Files.readAllBytes(clavePrivadaFile.toPath());

            // Convierte la clave privada a una cadena Base64 (si no lo está)
            String clavePrivadaBase64 = new String(clavePrivadaBytes, StandardCharsets.UTF_8);
            clavePrivadaBase64 = clavePrivadaBase64.replaceAll("\\s", ""); // Elimina espacios en blanco

            // Imprime la clave privada Base64 para verificar
            System.out.println("Clave privada Base64: " + clavePrivadaBase64);

            // Decodifica la clave privada desde Base64
            byte[] clavePrivadaDecodificada = Base64.getDecoder().decode(clavePrivadaBase64);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clavePrivadaDecodificada);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // Lee el archivo encriptado como un flujo de bytes
            FileInputStream fis = new FileInputStream(archivoEncriptadoFile);
            byte[] datosEncriptados = fis.readAllBytes();
            fis.close();

            // Inicializa Bouncy Castle
            Security.addProvider(new BouncyCastleProvider());

            // Descifra el archivo con la clave privada RSA
            Cipher descifrador = Cipher.getInstance("RSA", "BC");
            descifrador.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] datosDescifrados = descifrador.doFinal(datosEncriptados);

            // Obtiene la carpeta del archivo encriptado
            String carpetaEncriptado = archivoEncriptadoFile.getParent();

            // Construye la ruta completa para el archivo desencriptado
            String nombreArchivoDestino = desti_p.getText()+".txt";
            String rutaDestino = carpetaEncriptado + File.separator + nombreArchivoDestino;

            // Escribe los datos descifrados en el archivo de destino
            FileOutputStream fos = new FileOutputStream(new File(rutaDestino));
            fos.write(datosDescifrados);
            fos.close();

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
    private void seleccionarArchivo(ActionEvent event) {
        // Abre un FileChooser para seleccionar la clave privada
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo Encriptado");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Clave Privada", "*.pem", "*.txt")); // Puedes ajustar las extensiones según tu necesidad
       
       
        archivoEncriptadoFile = fileChooser.showOpenDialog(null);

        // Actualiza el TextField con el nombre del archivo encriptado seleccionado
        if (archivoEncriptadoFile != null) {
            arxiu_p.setText(archivoEncriptadoFile.getName());
        }
    }
    @FXML
    private void seleccionarClave(ActionEvent event) {
        // Abre un FileChooser para seleccionar la clave privada
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Clave Privada");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Clave Privada", "*.pem", "*.key")); // Puedes ajustar las extensiones según tu necesidad

        clavePrivadaFile = fileChooser.showOpenDialog(null);

        // Actualiza el TextField con el nombre de la clave privada seleccionada
        if (clavePrivadaFile != null) {
            clau_p.setText(clavePrivadaFile.getName());
        }
    }
}
