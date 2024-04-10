package DatabaseConnection;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class dbSingleton {

    private static dbSingleton instance;

    private dbSingleton(){

    }

    public static dbSingleton getInstance(){
        if (instance == null) {
            instance = new dbSingleton();
        }
        return instance;
    }
    
    // Logic Completed (need modification: return a hashtable-like variable)
    public Boolean insert(String tableName,  List<Map<String, String>> data) {
        try {
            FileWriter writer = new FileWriter("./Database/" + tableName + ".csv", true);
            PrintWriter out = new PrintWriter(writer);

            //get the fields
            BufferedReader reader = new BufferedReader(new FileReader("./Database/" + tableName + ".csv"));
            String line = reader.readLine();
            String[] fields = line.split(",");


            for (Map<String, String> map : data) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    for (int i = 0; i < fields.length; i++) {
                        if(key.equals(fields[i])){
                            out.print(value);
                            if(i<fields.length-1){
                                out.print(",");
                            }
                        }
                    }
                }

                out.println();
            }

            out.close();
            // System.out.println("Record inserted successfully!");
            return true;
        } catch (IOException e) {
            // System.err.println("Error occurred while inserting record: " + e.getMessage());
            return false;
        }
    }
    
    // Logic Completed (need modification: return a hashtable-like variable)
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
            
            // if (count > 0){
            //     System.out.println(count + " record(s) deleted successfully!");
            // }else{
            //     System.out.println("No matching record to delete!");
            // }

            return count;
            
        } catch (IOException e) {
            // System.err.println("Error occurred while deleting record: " + e.getMessage());
            return -1;
        }
    }

    // Logic Completed (need modification: return a hashtable-like variable)
    public int update(String tableName, String keyField, String keyValue, String[] newData) {
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
            for (int i = 0; i < fields.length; i++) {
                if (keyField.equals(fields[i])) {
                    attributeIndex = i;
                    break;
                }
            }
    
            while ((line = reader.readLine()) != null) {
                boolean updated = false;
                fields = line.split(",");
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].equals(keyValue) && attributeIndex == j) {
                        count++;
                        updated = true;
                        writer.print(newData[0]);
                        for (int k = 1; k < newData.length; k++) {
                            writer.print("," + newData[k]);
                        }
                        writer.println();
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
     
            // if (count > 0) {
            //     System.out.println(count + " record(s) updated successfully!");
            // } else {
            //     System.out.println("No matching record to update!");
            // }

            return count;

        } catch (IOException e) {
            // System.err.println("Error occurred while updating record: " + e.getMessage());
            return -1;
        }
    }
    

    // Logic Completed (need modification: return a hashtable-like variable)
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
            errorMessage.put("Error Message", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }

    // Logic Completed (need modification: return a hashtable-like variable)
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
            errorMessage.put("Error Message", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }
    
    // Logic Completed (need modification: return a hashtable-like variable)
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
            errorMessage.put("Error Message", e.getMessage());
            error.add(errorMessage);
            return error;
        }
    }
}
