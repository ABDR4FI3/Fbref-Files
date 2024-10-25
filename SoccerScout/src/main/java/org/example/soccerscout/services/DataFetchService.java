package org.example.soccerscout.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.util.*;

@Service
public class DataFetchService {

    @Autowired
    private PlayerService playerService;

    public List<Map<String, String>> fetchComparisonData(String player1Name, String player2Name) {
        // Generate the comparison link
        String comparisonLink = playerService.generateComparisonLink(player1Name, player2Name);
        if (comparisonLink.contains("Player not found")) {
            throw new IllegalArgumentException("Comparison link could not be generated for players.");
        }

        // Fetch and parse data from the comparison link
        try {
            Document doc = Jsoup.connect(comparisonLink).get();
            Table table = parseHtmlTableToDataFrame(doc);

            // Convert the Table to a List of Maps for JSON serialization
            List<Map<String, String>> jsonData = new ArrayList<>();
            for (int rowIndex = 0; rowIndex < table.rowCount(); rowIndex++) {
                Map<String, String> rowMap = new HashMap<>();
                for (String columnName : table.columnNames()) {
                    rowMap.put(columnName, table.stringColumn(columnName).get(rowIndex));
                }
                jsonData.add(rowMap);
            }
            return jsonData;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch or parse the data.");
        }
    }

    // Method to parse the HTML table into a Tablesaw DataFrame
    private Table parseHtmlTableToDataFrame(Document doc) {
        // Select the first table element
        Element tableElement = doc.selectFirst("table");
        if (tableElement == null) {
            throw new RuntimeException("No comparison table found.");
        }

        // Parse table headers and create columns, skipping duplicates
        Elements headers = tableElement.select("thead tr th");
        List<String> columnNames = new ArrayList<>();
        Set<String> uniqueColumnNames = new HashSet<>();
        for (Element header : headers) {
            String columnName = header.text();
            if (uniqueColumnNames.add(columnName)) { // Adds only if not a duplicate
                columnNames.add(columnName);
            }
        }

        // Create a new Table with named columns
        Table table = Table.create("Comparison Data");
        for (String columnName : columnNames) {
            table.addColumns(StringColumn.create(columnName));
        }

        // Parse each row and add data to the columns
        Elements rows = tableElement.select("tbody tr");
        for (Element row : rows) {
            Elements cells = row.select("td");
            int currentRow = table.rowCount(); // Track the new row index

            // For each cell, add the data to the appropriate column if the index is within bounds
            for (int i = 0; i < cells.size() && i < columnNames.size(); i++) {
                String cellValue = cells.get(i).text();
                table.stringColumn(i).append(cellValue);
            }
        }

        return table;
    }
}
