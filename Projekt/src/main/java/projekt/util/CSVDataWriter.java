package projekt.util;

import projekt.model.Statistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CSVDataWriter {
    private final String filename;

    public CSVDataWriter() {
        this.filename = "src/main/output/output-" + UUID.randomUUID() + ".csv";
        try (FileWriter writer = new FileWriter(this.filename, false)) {
            writeLine(writer, getHeaders());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeStatistics(Statistics statistics) {
        ArrayList<String> line = new ArrayList<>();
        line.add(String.valueOf(statistics.animalCount()));
        line.add(String.valueOf(statistics.plantCount()));
        line.add(String.valueOf(statistics.freeSpacesCount()));
        for (List<Integer> list : statistics.popularGenes()) {
            line.add("\"" + list.toString() + "\"");
        }

        int offset = 0;
        if (statistics.popularGenes().size() < 10) {
            offset = 10 - statistics.popularGenes().size();
        }

        for (int i = 0; i < offset; i++) {
            line.add("\"\"");
        }

        line.add(String.valueOf(statistics.averageEnergy()));
        line.add(String.valueOf(statistics.averageLifespan()));
        line.add(String.valueOf(statistics.averageChildCount()));

        try (FileWriter writer = new FileWriter(this.filename, true)) {
            writeLine(writer, line);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeLine(FileWriter writer, ArrayList<String> values) throws IOException {
        String line = String.join(",", values);
        writer.write(line);
        writer.write("\n");
    }

    private ArrayList<String> getHeaders() {
        ArrayList<String> result = new ArrayList<>(Arrays.asList("Animals count", "Plants count", "Free fields count"));

        for (int i = 1; i <= 10; i++) {
            result.add(i + ". most popular gene");
        }

        result.add("Average energy");
        result.add("Average lifespan");
        result.add("Average fertility");
        return result;
    }
}
