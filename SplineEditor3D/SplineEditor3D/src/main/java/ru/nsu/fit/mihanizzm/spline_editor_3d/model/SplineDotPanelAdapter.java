package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineDotPanel;

import java.awt.geom.Point2D;
import java.io.IOException;

public class SplineDotPanelAdapter extends TypeAdapter<SplineDotPanel> {
    @Override
    public void write(JsonWriter out, SplineDotPanel dotPanel) throws IOException {
        if (dotPanel == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("idx").value(dotPanel.getIdx());
        out.name("isSelected").value(dotPanel.isSelected());
        out.name("coordinates").beginObject()
                .name("x").value(dotPanel.getCoordinates().x)
                .name("y").value(dotPanel.getCoordinates().y)
                .endObject();
        out.endObject();
    }

    @Override
    public SplineDotPanel read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            return null;
        }
        in.beginObject();
        int idx = 0;
        boolean isSelected = false;
        Point2D.Float coordinates = new Point2D.Float();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "idx":
                    idx = in.nextInt();
                    break;
                case "isSelected":
                    isSelected = in.nextBoolean();
                    break;
                case "coordinates":
                    in.beginObject();
                    while (in.hasNext()) {
                        String coordinateName = in.nextName();
                        switch (coordinateName) {
                            case "x":
                                coordinates.x = (float) in.nextDouble();
                                break;
                            case "y":
                                coordinates.y = (float) in.nextDouble();
                                break;
                            default:
                                in.skipValue();
                        }
                    }
                    in.endObject();
                    break;
                default:
                    in.skipValue();
            }
        }
        in.endObject();
        return new SplineDotPanel(idx, isSelected, coordinates);
    }
}

