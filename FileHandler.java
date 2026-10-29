/**
 * Handles file persistence operations including saving and loading Magazine objects
 * via Java object serialization and JavaFX FileChooser.
 * 
 * @author Chang Yun Shiang
 * @version 2.0
 */

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileHandler {

    /**
     * Prompts the user with a FileChooser to save the current Magazine object to disk.
     * 
     * @param ownerStage the parent JavaFX stage for the dialog window
     * @param magazine   the Magazine instance to serialize and save
     */
    public static void saveMagazine(Stage ownerStage, Magazine magazine) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Magazine Service Data");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Serialized Data Files (*.ser)", "*.ser")
        );
        fileChooser.setInitialFileName("magazine_data.ser");

        File file = fileChooser.showSaveDialog(ownerStage);
        if (file != null) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(magazine);
                System.out.println("[SUCCESS] Magazine data successfully saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("[ERROR] Failed to save file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Prompts the user with a FileChooser to load a saved Magazine object from disk.
     * 
     * @param ownerStage the parent JavaFX stage for the dialog window
     * @return the deserialized Magazine instance, or null if loading was cancelled or failed
     */
    public static Magazine loadMagazine(Stage ownerStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Magazine Service Data");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Serialized Data Files (*.ser)", "*.ser")
        );

        File file = fileChooser.showOpenDialog(ownerStage);
        if (file != null) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Magazine magazine = (Magazine) ois.readObject();
                System.out.println("[SUCCESS] Magazine data successfully loaded from " + file.getAbsolutePath());
                return magazine;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("[ERROR] Failed to load file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}