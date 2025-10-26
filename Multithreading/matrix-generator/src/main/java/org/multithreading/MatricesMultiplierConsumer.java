package org.multithreading;

import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class MatricesMultiplierConsumer extends Thread {
    private ThreadSafeQueue queue;
    private FileWriter fileWriter;

    public MatricesMultiplierConsumer(FileWriter fileWriter, ThreadSafeQueue queue) {
        this.fileWriter = fileWriter;
        this.queue = queue;
    }

    private static void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
        for (int r = 0; r < Main.N; r++) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (int c = 0; c < Main.N; c++) {
                stringJoiner.add(String.format("%.2f", matrix[r][c]));
            }
            fileWriter.write(stringJoiner.toString());
            fileWriter.write('\n');
        }
        fileWriter.write('\n');
    }

    @Override
    public void run(){
        while(true){
            MatricesPair matricesPair = queue.remove();
            if(matricesPair == null){
                System.out.println("No more matrices to read from the queue, Consumer Thread is terminating");
                break;
            }

            float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);

            try {
                saveMatrixToFile(fileWriter, result);
            } catch (IOException e) {
            }
        }

        try{
            fileWriter.flush();
            fileWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float[][] multiplyMatrices(float[][] matrix1, float[][] matrix2) {
        float[][] result = new float[Main.N][Main.N];
        for (int r = 0; r < Main.N; r++) {
            for (int c = 0; c < Main.N; c++) {
                for (int k = 0; k < Main.N; k++) {
                    result[r][c] += matrix1[r][k] * matrix2[k][c];
                }
            }
        }
        return result;
    }
}
