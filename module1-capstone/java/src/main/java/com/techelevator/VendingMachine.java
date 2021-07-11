package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class VendingMachine {
    //Instance Variables
    public Map<String, SlotQuantity> actualInventory = new HashMap<>();
    private BigDecimal balance = new BigDecimal("0.00");

    //Getters
    public BigDecimal getBalance() {
        return balance;
    }

    //Setters
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    //Constructors
    public VendingMachine(FileReader fileReader) {
        this.actualInventory = fileReader.load();
    }

    //Methods
    public void displayInventory() {
        Map<String, SlotQuantity> orderedInventory = new TreeMap<String, SlotQuantity>(actualInventory);
        for (Map.Entry<String, SlotQuantity> entry : orderedInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getItem().getName() + ", $" + entry.getValue().getItem().getPrice() + ", " + entry.getValue().getItem().getType() + ", " + entry.getValue().getInventoryCount());
        }
    }

    public void feedMoney(BigDecimal money, String choice) {
        //Define Bills Accepted
        BigDecimal dollarBill = new BigDecimal("1.00");
        BigDecimal twoDollarBill = new BigDecimal("2.00");
        BigDecimal fiveDollarBill = new BigDecimal("5.00");
        BigDecimal tenDollarBill = new BigDecimal("10.00");

        //Define local variables
        BigDecimal previousBalance = new BigDecimal(String.valueOf(this.balance));

        if (money.compareTo(dollarBill) == 0 || money.compareTo(twoDollarBill) == 0 || money.compareTo(fiveDollarBill) == 0 || money.compareTo(tenDollarBill) == 0) {
            previousBalance = this.balance;
            this.balance = this.balance.add(money);
            log(choice, previousBalance);
        } else {
            this.balance = balance;
            System.out.println("This machine only accepts $1, $2, $5 & $10 bills.");
        }
        this.balance = balance;
    }

    public void purchase(String key) {
        //Define local variables
        BigDecimal previousBalance = new BigDecimal(String.valueOf(this.balance));
        BigDecimal itemPrice = new BigDecimal(actualInventory.get(key).getItem().getPrice().toString());

        if (this.balance.compareTo(itemPrice) == 1 || this.balance.compareTo(itemPrice) == 0) {
            previousBalance = this.balance;
            this.balance = balance.subtract(itemPrice);
            actualInventory.get(key).setInventoryCount(actualInventory.get(key).getInventoryCount() - 1);
            purchaseLog(key, previousBalance);
        } else {
            this.balance = balance;
        }
    }

    public void dispense(String key) {
        if (actualInventory.get(key).getItem().getType().equals("Chip")) {
            System.out.println("Crunch Crunch, Yum!");
        } else if (actualInventory.get(key).getItem().getType().equals("Candy")) {
            System.out.println("Munch Munch, Yum!");
        } else if (actualInventory.get(key).getItem().getType().equals("Drink")) {
            System.out.println("Glug Glug, Yum!");
        } else if (actualInventory.get(key).getItem().getType().equals("Gum")) {
            System.out.println("Chew Chew, Yum!");
        }
    }

    public void giveChange(String choice) {
        int quarters = 0;
        int dimes = 0;
        int nickels = 0;
        BigDecimal quarterWorth = new BigDecimal(".25");
        BigDecimal dimeWorth = new BigDecimal(".10");
        BigDecimal nickelWorth = new BigDecimal(".05");
        BigDecimal previousBalance = this.balance;

        while (balance.compareTo(quarterWorth) == 1 || balance.compareTo(quarterWorth) == 0) {
            this.balance = balance.subtract(quarterWorth);
            quarters++;
        }
        while (balance.compareTo(dimeWorth) == 1 || balance.compareTo(dimeWorth) == 0) {
            this.balance = balance.subtract(dimeWorth);
            dimes++;
        }
        while (balance.compareTo(nickelWorth) == 1 || balance.compareTo(nickelWorth) == 0) {
            this.balance = balance.subtract(nickelWorth);
            nickels++;
        }
        log(choice, previousBalance);
        System.out.println("Your change is " + quarters + " quarter(s), " + dimes + " dime(s), and " + nickels + " nickel(s).");
    }

    public void purchaseLog(String key, BigDecimal previousBalance) {
        File logFile = new File("log.txt");
        if (logFile.exists()) {
            try (PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile, true),true)) {

                DateTimeFormatter americanDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String americanDate = LocalDate.now().format(americanDateFormat);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
                String rightFormat = LocalTime.now().format(timeFormatter);
                if (actualInventory.containsKey(key)) {
                    logWriter.write(americanDate + " " + rightFormat + " " + actualInventory.get(key).getItem().getName() + " " + key.toUpperCase() + ": \\$" + previousBalance + " \\$" + getBalance() + "\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Oops Something Went Wrong");
            }
        }
    }

    public void log (String choice, BigDecimal previousBalance) {
        BigDecimal zero = new BigDecimal("0.00");
        File logFile = new File("log.txt");
        if (logFile.exists()) {
            try (PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile,true),true)) {

                DateTimeFormatter americanDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                String americanDate = LocalDate.now().format(americanDateFormat);

                DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
                String rightFormat = LocalTime.now().format(timeFormatter);

                if (choice.equals("Add Money")) {
                    logWriter.write(americanDate + " " + rightFormat + " " + choice.toUpperCase() + ": \\$" + previousBalance + " \\$" + getBalance() + "\n");
                }

                if (choice.equals("Cash out")) {
                    logWriter.write(americanDate + " " + rightFormat + " " + choice.toUpperCase() + ": \\$" + previousBalance + " \\$" + getBalance() + "\n");
                    balance = zero;
                }

            } catch (FileNotFoundException e) {
                System.out.println("Oops Something Went Wrong");
            }
        }
    }

    public boolean isInStock (String key) {
        try {
            if (actualInventory.get(key).getInventoryCount() > 0) {
                return true;
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return false;
    }

    public boolean isThereEnoughBalance (String key){
        BigDecimal itemPrice = new BigDecimal(actualInventory.get(key).getItem().getPrice().toString());
        if (getBalance().compareTo(itemPrice) == 1 || getBalance().compareTo(itemPrice) == 0) {
            return true;
        }
        return false;
    }

}


