package DatabaseConnection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class dbSingleton {

    private static dbSingleton instance;
    private static Map<String, Integer> lastIdMap;

    private dbSingleton(){

    }

    public static dbSingleton getInstance(){
        if (instance == null) {
            instance = new dbSingleton();
        }
        return instance;
    }
    
    public static void initializeLastIdMap(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    
            String line;
            lastIdMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                String[] record = line.split(":");
                lastIdMap.put(record[0], Integer.parseInt(record[1]));
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveCacheData(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            PrintWriter out = new PrintWriter(writer);

            for (Map.Entry<String, Integer> entry : lastIdMap.entrySet()) {
                out.println(entry.getKey() + ":" + entry.getValue());
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNextId(String tableName) {
        return lastIdMap.getOrDefault(tableName, 0) + 1;
    }
    
    /**
     * @param tableName The name of database table to insert records into
     * @param data A list containing maps representing the data to be inserted, where each map represents a record,
     *             with key representing column name and value representing column value
     * @return true if the records were inserted successfully; false if an error occurred during insertion
     */
    public Boolean insert(String tableName,  List<Map<String, String>> data) {
        try {
            FileWriter writer = new FileWriter("./Database/" + tableName + ".csv", true);
            PrintWriter out = new PrintWriter(writer);

            BufferedReader reader = new BufferedReader(new FileReader("./Database/" + tableName + ".csv"));
            String line = reader.readLine();
            String[] fields = line.split(",");

            for (Map<String, String> map : data) {

                for (int i = 0; i < fields.length; i++) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                    if(key.equals(fields[i])){
                        out.print(value);
                        if(i<fields.length-1){
                            out.print(",");
                        }
                    }

                    }
                }

                out.println();
                // lastIdMap.put(tableName, dbSingleton.getNextId(tableName));
            }
            out.close();
            return true;
        } 
        
        catch (IOException e) {
            return false;
        }
    }


    
    /**
     * @param tableName The name of the database table from which records are to be deleted
     * @param keyField The attribute for deletion
     * @param keyValue The value of the keyField used to identify records for deletion
     * @return The number of records deleted; -1 if an error occurred during deletion
     */
    public int delete(String tableName, String keyField, String keyValue){
        try {
            File inputFile = new File("./Database/" + tableName + ".csv");
            File tempFile = new File("Database/temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String line = reader.readLine();
            writer.println(line);

            int count = 0;
            String[] fields;
            int attributeIndex = -1;
            fields = line.split(",");
            for (int i = 0; i < fields.length; i++){
                if (keyField.equals(fields[i])){
                    attributeIndex = i;
                    break;
                }
            }

            while ((line = reader.readLine()) != null) {
                boolean deleteFlag = false;
                fields = line.split(",");
                for (int j = 0; j < fields.length; j++){
                    if (fields[j].equals(keyValue) && attributeIndex == j){
                        count++;
                        deleteFlag = true;
                        break;
                    }
                }
                if (!deleteFlag){
                    writer.println(line);
                }
            }

            writer.close();
            reader.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
            
            return count;
            
        } catch (IOException e) {
            return -1;
        }
    }



    public int update(String tableName, String keyField, String keyValue, List<Map<String, String>> newData) {
        try {
            File inputFile = new File("./Database/" + tableName + ".csv");
            File tempFile = new File("Database/temp.csv");
    
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
    
            String line = reader.readLine();
            String[] headerFields = line.split(",");
            writer.println(line);
    
            int count = 0;
            int attributeIndex = -1;
            for (int i = 0; i < headerFields.length; i++) {
                if (keyField.equals(headerFields[i])) {
                    attributeIndex = i;
                    break;
                }
            }
    
            while ((line = reader.readLine()) != null) {
                boolean updated = false;
                String[] dataFields = line.split(",");
                for (int j = 0; j < dataFields.length; j++) {
                    if (dataFields[j].equals(keyValue) && attributeIndex == j) {
                        count++;
                        updated = true;
                        for (Map<String, String> map : newData) {
                            for (int k = 0; k < headerFields.length; k++) {
                                String fieldName = headerFields[k];
                                if (!map.containsKey(fieldName)) {
                                    throw new IllegalArgumentException("Incomplete data. Missing field: " + fieldName);
                                }
                                writer.print(map.get(fieldName));
                                if (k < headerFields.length - 1) {
                                    writer.print(",");
                                }
                            }
                            writer.println();
                        }
                        break;
                    }
                }
                if (!updated) {
                    writer.println(line);
                }
            }
    
            writer.close();
            reader.close();
    
            inputFile.delete();
            tempFile.renameTo(inputFile);
    
            return count;
    
        } catch (IOException e) {
            return -1;
        }
    }    
    



    /**
     * @param tableName The name of the database table to perform the fuzzy search
     * @param keyField The attribute for fuzzy search
     * @param keyValue The value of the keyField used for fuzzily searching records
     * @return A list of maps containing records that match the fuzzy search criteria; 
     *         each map represents a record, with key representing column name and value representing column value
     *         If an error occurs during the search, it returns a list containing a single map with the error message.
     */
    public List<Map<String, String>> fuzzySearch(String tableName, String keyField, String keyValue) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./Database/"+ tableName + ".csv"));
    
            String line = reader.readLine();
            int attributeIndex = -1;
            String[] fields = line.split(",");
            for (int i = 0; i < fields.length; i++){
                if (keyField.equals(fields[i])){
                    attributeIndex = i;
                    break;
                }
            }

            List<Map<String, String>> result = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Map<String, String> recordHash = new HashMap<>();
                String[] recordArr = line.split(",");
                for (int j = 0; j < fields.length; j++){
                    if (recordArr[j].contains(keyValue) && attributeIndex == j){
                        for (int k = 0; k < recordArr.length; k++){
                            recordHash.put(fields[k], recordArr[k]);
                        }
                        result.add(recordHash);
                    }
                }
            }
            reader.close();
            return result;
        } catch (IOException e) {
            List<Map<String, String>> error = new ArrayList<>();
            Map<String, String> errorMessage= new HashMap<>();
            errorMessage.put("ErrorMessage", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }

    /**
     * @param tableName The name of the database table to perform the precision search
     * @param keyField The name of the attribute for precision search
     * @param keyValue The value of the keyField used for precisely searching records
     * @return A list of maps containing records that match the fuzzy search criteria; 
     *         each map represents a record, with keys representing column names and values representing column values
     *         If an error occurs during the search, it returns a list containing a single map with the error message.
     */
    public List<Map<String, String>> preciseSearch(String tableName, String keyField, String keyValue) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./Database/"+ tableName + ".csv"));
    
            String line = reader.readLine();
            int attributeIndex = -1;
            String[] fields = line.split(",");
            for (int i = 0; i < fields.length; i++){
                if (keyField.equals(fields[i])){
                    attributeIndex = i;
                    break;
                }
            }

            List<Map<String, String>> result = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                Map<String, String> recordHash = new HashMap<>();
                String[] recordArr = line.split(",");
                for (int j = 0; j < fields.length; j++){
                    if (recordArr[j].equals(keyValue) && attributeIndex == j){
                        for (int k = 0; k < recordArr.length; k++){
                            recordHash.put(fields[k], recordArr[k]);
                        }
                        result.add(recordHash);
                    }
                }
            }
            reader.close();
            return result;
        } catch (IOException e) {
            List<Map<String, String>> error = new ArrayList<>();
            Map<String, String> errorMessage= new HashMap<>();
            errorMessage.put("ErrorMessage", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }
    
    /**
     * @param tableName The name of database table to retrieve records from
     * @return A list of maps containing all records from the specified table;
     *         each map represents a record, with key representing column name and value representing column value
     *         If an error occurs during the retrieval, it returns a list containing a single map with the error message.
     */
    public List<Map<String, String>> listAll(String tableName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./Database/" + tableName + ".csv"));
            
            String line = reader.readLine();
            String[] fields = line.split(",");
            List<Map<String, String>> result = new ArrayList<>();
            
            while ((line = reader.readLine()) != null) {
                Map<String, String> recordHash = new HashMap<>();
                String[] recordArr = line.split(",");
                for (int i = 0; i < recordArr.length; i++){
                    recordHash.put(fields[i], recordArr[i]);
                }
                result.add(recordHash);
            }
            reader.close();
            return result;

        } catch (IOException e) {
            List<Map<String, String>> error = new ArrayList<>();
            Map<String, String> errorMessage= new HashMap<>();
            errorMessage.put("ErrorMessage", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }
}
