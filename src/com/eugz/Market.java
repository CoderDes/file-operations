package com.eugz;

import java.io.*;

public class Market {
    private String inputData;
    private LimitBook limitBook;
    private final String UPDATE = "u";
    private final String QUERY = "q";
    private final String ORDER = "o";

    public Market() {
        this.inputData = "";
        this.limitBook = new LimitBook();
    }

    public void init() {
        readDataFromFile();
        manageOperations();
        writeOutputFile();
    }

    private void readDataFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputData = String.join("\n", inputData, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputData = inputData.trim();
        }
    }

    private void manageOperations() {
        for (String line : inputData.split("\n")) {
            String[] params = line.split(",");
            String operationType = params[0].toLowerCase();
            if (operationType.equals(UPDATE)) {
                int updatePrice = Integer.parseInt(params[1]);
                int size = Integer.parseInt(params[2]);
                String orderType = params[3];
                limitBook.addOrder(new Order(updatePrice, size, orderType));
            } else if (operationType.equals(QUERY)) {
                String queryStr = params[1];
                if (isQueryWithoutSize(params)) {
                    limitBook.query(queryStr);
                } else {
                    int queryPrice = Integer.parseInt(params[2]);
                    limitBook.query(queryStr, queryPrice);
                }
            } else if (operationType.equals(ORDER)) {
                String operation = params[1];
                int orderSize = Integer.parseInt(params[2]);
                limitBook.order(operation, orderSize);
            }
        }
    }

    private boolean isQueryWithoutSize(String[] params) {
        return params.length < 3;
    }

    private void writeOutputFile() {
        File file = new File("output.txt");
        try  {
            file.createNewFile();
            FileWriter writer = new FileWriter("output.txt");
            writer.write(this.limitBook.getQueryResult());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
