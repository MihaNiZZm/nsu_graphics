package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineDotPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class FileManager extends JFileChooser {
    private static FileManager instance;
    private static final String FILES_DESCRIPTION = "Spline Viewer 3D files: \".sv3\"";
    private static final String IMPORT_FILE = "Import file";
    private static final String EXPORT_FILE = "Export file";
    private File lastOpenedFile = null;
    private File lastSavedFile = null;
    private final FileNameExtensionFilter extensionFilter;

    private FileManager() {
        extensionFilter = new FileNameExtensionFilter(FILES_DESCRIPTION, "sv3");
        setFileFilter(extensionFilter);
        setMultiSelectionEnabled(false);
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }

        return instance;
    }

    public void importFile() {
        if (lastOpenedFile == null) {
            setCurrentDirectory(lastSavedFile);
        }
        else {
            setCurrentDirectory(lastOpenedFile);
        }
        setDialogTitle(IMPORT_FILE);
        setDialogType(OPEN_DIALOG);

        int result = showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            lastOpenedFile = getSelectedFile();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(lastOpenedFile))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                String json = stringBuilder.toString();

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(SplineDotPanel.class, new SplineDotPanelAdapter())
                        .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                        .create();

                Settings newSettings = gson.fromJson(json, Settings.class);

                SettingsManager.getInstance().setSettings(newSettings);
                SettingsManager.getInstance().updateState();
                showSuccessfulImportMessage();
            }
            catch (IOException e) {
                showImportErrorMessage();
            }
            catch (JsonSyntaxException e) {
                showJsonSyntaxErrorMessage();
            }
        }
    }

    public void exportFile() {
        if (lastSavedFile == null) {
            setCurrentDirectory(lastOpenedFile);
        }
        else {
            setCurrentDirectory(lastSavedFile);
        }
        setDialogTitle(EXPORT_FILE);
        setDialogType(SAVE_DIALOG);

        int result = showSaveDialog(null);
        if (result == APPROVE_OPTION) {
            lastSavedFile = getSelectedFile();
            String filePath = lastSavedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".sv3")) {
                filePath += ".sv3";
                lastSavedFile = new File(filePath);
            }

            try (FileWriter fileWriter = new FileWriter(lastSavedFile)) {
                SettingsManager.getInstance().saveState();
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(SplineDotPanel.class, new SplineDotPanelAdapter())
                        .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                        .create();
                String json = gson.toJson(SettingsManager.getInstance().getSettings());
                fileWriter.write(json);
                showSuccessfulExportMessage();
            } catch (IOException e) {
                showExportErrorMessage();
            }
        }
    }

    private void showSuccessfulExportMessage() {
        JOptionPane.showMessageDialog(this,
                "The file was saved successfully.",
                EXPORT_FILE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showExportErrorMessage() {
        JOptionPane.showMessageDialog(this,
                "Couldn't save the file. Please, try again.",
                EXPORT_FILE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showSuccessfulImportMessage() {
        JOptionPane.showMessageDialog(this,
                "The file was imported successfully.",
                IMPORT_FILE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showImportErrorMessage() {
        JOptionPane.showMessageDialog(this,
                "Couldn't open the file. Please, try again.",
                IMPORT_FILE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showJsonSyntaxErrorMessage() {
        JOptionPane.showMessageDialog(this,
                "\".json\" file you are trying to open has syntax error(s).",
                IMPORT_FILE,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
