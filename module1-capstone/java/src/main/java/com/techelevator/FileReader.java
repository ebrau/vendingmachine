package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileReader {
    //Instance Variables
    private File uploadFile ;

    //Constructor
    public FileReader(String sourcefile){
        uploadFile = new File(sourcefile);
    }

    //Method
    public Map<String, SlotQuantity> load() {

        Map<String, SlotQuantity> actualInventory = new HashMap<>();

        try (Scanner inputScanner = new Scanner(uploadFile.getAbsoluteFile())) {
            String searchResults = "";
            while (inputScanner.hasNextLine()) {
                searchResults = inputScanner.nextLine().replace("|", "@");
                String[] searchResultsArray = searchResults.split("@", 5);

                //Extracting the values and making them how I want them
                String slotNumber = searchResultsArray[0];
                String name = searchResultsArray[1];
                BigDecimal price = (new BigDecimal (searchResultsArray[2]));
                String type = searchResultsArray[3];

                //Instantiate an item using my new values
                Item testItem = new Item (name, price, type);
                SlotQuantity testItemInventory = new SlotQuantity(5, testItem);

                //Create a Map
                actualInventory.put(slotNumber,testItemInventory);
                }

            } catch(FileNotFoundException e) {
                    System.out.println("Oops Something Went Wrong");

        }finally {
            return actualInventory;
        }
        }
    }
