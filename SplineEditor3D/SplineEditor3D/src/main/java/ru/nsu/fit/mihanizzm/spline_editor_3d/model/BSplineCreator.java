package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public class BSplineCreator {
    private static final int DIMENSION_SIZE = 4;
    private static final float[][] M = {
            { -1/6f, 3/6f, -3/6f, 1/6f },
            { 3/6f, -1f, 3/6f, 0f },
            { -3/6f, 0/6f, 3/6f, 0f },
            { 1/6f, 4/6f, 1/6f, 0f },
    };

    private static float[] multiplyMatrixMAndVector(float[] vector) {
        if (vector.length != DIMENSION_SIZE) {
            log.error("Wrong size array given in matrix and vector multiplication: {}", Arrays.toString(vector));
            return new float[DIMENSION_SIZE];
        }
        float[] result = new float[DIMENSION_SIZE];

        for (int i = 0; i < DIMENSION_SIZE; ++i) {
            for (int j = 0; j < DIMENSION_SIZE; ++j) {
                result[i] += M[i][j] * vector[j];
            }
        }

        return result;
    }

    private static float multiplyVectorTAndVector(float[] T, float[] vector) {
        if (T.length != DIMENSION_SIZE) {
            log.error("Wrong size T array given in vector-vector multiplication: {}", Arrays.toString(T));
        } else if (vector.length != DIMENSION_SIZE) {
            log.error("Wrong size vector array given in vector-vector multiplication: {}", Arrays.toString(vector));
        }
        float result = 0f;

        for (int i = 0; i < DIMENSION_SIZE; ++i) {
            result += T[i] * vector[i];
        }

        return result;
    }

    private static float[] getSplinePointsOfOneAxis(float[] coordinates, int amountOfSections) {
        if (coordinates.length != DIMENSION_SIZE) {
            log.error("Wrong size of given coordinates array: {}", Arrays.toString(coordinates));
            return new float[DIMENSION_SIZE];
        }
        float[] tPoints = new float[amountOfSections + 1];
        for (int i = 0; i < amountOfSections; ++i) {
            tPoints[i] = i / (float) amountOfSections;
        }
        tPoints[amountOfSections] = 1f;

        float[] resultPoints = new float[amountOfSections + 1];
        float[] tempVector = multiplyMatrixMAndVector(coordinates);

        for (int i = 0; i < amountOfSections + 1; ++i) {
            float[] T = { tPoints[i] * tPoints[i] * tPoints[i], tPoints[i] * tPoints[i], tPoints[i], 1 };
            resultPoints[i] = multiplyVectorTAndVector(T, tempVector);
        }

        return resultPoints;
    }

    public static float[] getSplinePoints(float[] coordinates, int amountOfSections) {
        float[] result = new float[amountOfSections * (coordinates.length - 3) + 1];

        for (int i = 0; i < coordinates.length - 3; ++i) {
            float[] currentVector = { coordinates[i], coordinates[i + 1], coordinates[i + 2], coordinates[i + 3] };
            float[] tempRes = getSplinePointsOfOneAxis(currentVector, amountOfSections);
            System.arraycopy(tempRes, 0, result, i * amountOfSections, tempRes.length);
        }

        return result;
    }
}
