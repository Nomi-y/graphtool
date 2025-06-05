import com.opencsv.*;
import com.opencsv.exceptions.*;

import java.io.*;
import java.util.*;

public final class CSVutils {

    public static double[][] readCSV(String filepath, char delimiter) {
        boolean readSuccessful = false;
        double[][] input = { {} };
        while (!readSuccessful) {
            if (filepath != null && !filepath.isEmpty()) {
                input = parseCSV(filepath, delimiter);
                if (input.length > 0)
                    readSuccessful = true;
                else {
                    System.out.println("Failed to read CSV at " + filepath);
                    filepath = null;
                }
            } else {
                filepath = Main.io();
            }
        }
        return input;
    }

    private static double[][] parseCSV(String filepath, char delimiter) {
        if (!filepath.toLowerCase().endsWith(".csv"))
            return new double[0][0];
        List<double[]> rows = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filepath))
                .withCSVParser(new CSVParserBuilder().withSeparator(delimiter).build())
                .build()) {
            String[] nextRecord;
            while ((nextRecord = reader.readNext()) != null) {
                double[] row = new double[nextRecord.length];
                for (int j = 0; j < nextRecord.length; j++) {
                    try {
                        row[j] = Double.parseDouble(nextRecord[j]);
                    } catch (NumberFormatException e) {
                        row[j] = 0;
                    }
                }
                rows.add(row);
            }
        } catch (IOException | CsvValidationException e) {
            return new double[0][0];
        }
        return rows.toArray(new double[0][]);
    }

    public static char detectDelimiter(String filepath) {
        char[] delimiters = { ',', ';', '\t', '|', ':' };
        int maxLines = 50;
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null && lines.size() < maxLines)
                if (!line.trim().isEmpty())
                    lines.add(line);
        } catch (Exception e) {
            System.out.println("Failed to detect delimiter, using default: [,]");
            return ',';
        }
        return getBestDelimiter(delimiters, lines);
    }

    private static char getBestDelimiter(char[] delimiters, List<String> lines) {
        char bestDelimiter = ',';
        int bestScore = 0;
        for (char delim : delimiters) {
            int expectedColumns = -1;
            int score = 0;
            for (String line : lines) {
                int columns = line.split(String.valueOf(delim), -1).length;
                if (expectedColumns == -1)
                    expectedColumns = columns;
                if (columns == expectedColumns)
                    score++;
            }
            if (score > bestScore && expectedColumns > 1) {
                bestScore = score;
                bestDelimiter = delim;
            }
        }
        return bestDelimiter;
    }

    public static boolean writeCSV(double[][] matrix) {
        return false;
    }

}
