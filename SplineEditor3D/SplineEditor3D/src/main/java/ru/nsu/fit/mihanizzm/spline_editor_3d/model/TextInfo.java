package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

public class TextInfo {
    public static final String ABOUT_MESSAGE = """
                This program is a study project that shows possibilities of creating
            B-spline curves and building 3D-model by rotating these spline curves
            in three dimensions.
            
                There are parameters you can change in a spline editor:
            - N (amount of segments on each spline part)
            - M (amount of generative spline curves in a 3D-model)
            - M1 (amount of segments in each circle arc between two generative spline curves)
            - K (amount of pivot points of spline)
            
                You can create, move and delete pivot points of b-spline using mouse:
            - left click - create/select point
            - right click - delete point
            - left click + drag - move point
            
                You can also navigate through b-spline editor panel using mouse:
            mouse wheel - zoom in/zoom out
            mouse drag - move around the editor panel
            
                There are also options of changing color of a spline and 3D-model
            and normalizing pivot points in the editor.
            
                3D Viewer also has options. There are options such as:
            - rotating a model using mouse drag
            - zooming in/out using mouse wheel
            - reset rotation angles
            
                You can save and load files of your project using "Import" and "Export"
            buttons. All files are being saved in ".sv3" format.
            
                There are options available in the toolbar and in the menu of the program.
            All toolbar options have its equivalent in menu.
            
            Creator: Sartakov Mikhail Yurievich
            Telegram: @mihanizzm
            Version: 1.0.0
            """;
}
