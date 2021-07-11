package com.techelevator;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class VendingMachineTests {

    @Test
    public void isInStock_whenItemIsInStockAndHasValidKey_shouldReturnTrue() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        String key = "A4";

        //Act
        boolean result = testVM.isInStock(key);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void isInStock_whenItemIsOutOfStockAndHasValidKey_shouldReturnFalse() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        String key = "A4";
        testVM.actualInventory.get(key).setInventoryCount(0);

        //Act
        boolean result = testVM.isInStock(key);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void isInStock_whenGivenInvalidKey_shouldReturnFalse() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        String key = "E4";

        //Act
        boolean result = testVM.isInStock(key);

        //Assert
        Assert.assertFalse(result);
    }

    @Test
    public void isThereEnoughBalance_ifBalanceIsGreaterThanItemPriceAndKeyIsInCorrectCase_shouldReturnTrue() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal test = new BigDecimal("5.00");
        String choice = "test";
        testVM.feedMoney(test, choice);

        //Act
        boolean result = testVM.isThereEnoughBalance("A4");

        //Assert
        Assert.assertTrue(result);
    }
    @Test
    public void isThereEnoughBalance_ifBalanceIsLessThanItemPriceAndKeyIsInCorrectCase_shouldReturnFalse() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal test = new BigDecimal("0.00");
        String choice = "test";
        testVM.feedMoney(test, choice);

        //Act
        boolean result = testVM.isThereEnoughBalance("A4");

        //Assert
        Assert.assertFalse(result);
    }
    @Test
    public void isThereEnoughBalance_ifBalanceIsEqualToItemPriceAndKeyIsInCorrectCase_shouldReturnTrue() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal test = new BigDecimal("3.65");
        testVM.setBalance(test);

        //Act
        boolean result = testVM.isThereEnoughBalance("A4");

        //Assert
        Assert.assertTrue(result);
    }

   @Test
    public void giveChange_shouldSetPreviousBalanceBackToZero() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal testMoney = new BigDecimal("2.00");
        BigDecimal expected = new BigDecimal("0.00");
        String choice = "test";
        testVM.feedMoney(testMoney, choice);
        testVM.giveChange(choice);

        //Act
       BigDecimal result = testVM.getBalance();

        //Assert
       Assert.assertEquals(expected, result);
    }

    @Test
    public void giveChange_withNoChangeEntered_shouldReturnZero() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("0.00");
        String choice = "test";
        testVM.giveChange(choice);

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void feedMoney_startingWith0Balance_shouldReturnMoneyFed() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("5.00");
        String choice = "test";
        testVM.feedMoney(expected, choice);

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }
    @Test
    public void feedMoney_startingWithNon0Balance_shouldReturnSumOfMoneyFedAndPreviousBalance() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("7.00");
        BigDecimal moneyFed = new BigDecimal("5.00");
        BigDecimal setBalance = new BigDecimal("2.00");
        String choice = "test";
        testVM.feedMoney(setBalance, choice);
        testVM.feedMoney(moneyFed, choice);

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void feedMoney_withInvalidAmount_shouldReturnOriginalBalance() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("0.00");
        BigDecimal moneyFed = new BigDecimal("3.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_purchasingOneItem_shouldReturnBalanceMinusPurchasePrice() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("9.25");
        BigDecimal moneyFed = new BigDecimal("10.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);
        testVM.purchase("D3");

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_purchasingItemMoreThanBalance_shouldReturnUnchangedBalance() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("1.00");
        BigDecimal moneyFed = new BigDecimal("1.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);
        testVM.purchase("C4");

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_purchasingOneItemOfRemainingBalance_shouldReturn0() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("0.00");
        BigDecimal moneyFed = new BigDecimal("1.50");
        testVM.getBalance().add(moneyFed);
        testVM.purchase("C4");

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_purchasingMultipleItems_shouldReturnPreviousBalanceMinusSumOfItems() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("7.00");
        BigDecimal moneyFed = new BigDecimal("10.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);
        testVM.purchase("C4");
        testVM.purchase("C4");

        //Act
        BigDecimal result = testVM.getBalance();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_whenPurchasingOneItem_shouldUpdateInventoryCount() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal moneyFed = new BigDecimal("10.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);
        testVM.purchase("C4");
        int expected = 4;

        //Act
        int result = testVM.actualInventory.get("C4").getInventoryCount();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void purchase_whenPurchasingMultipleItems_shouldUpdateInventoryCountMultipleTimes() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal moneyFed = new BigDecimal("10.00");
        String choice = "test";
        testVM.feedMoney(moneyFed, choice);
        testVM.purchase("C4");
        testVM.purchase("C4");
        int expected = 3;

        //Act
        int result = testVM.actualInventory.get("C4").getInventoryCount();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void item_whenTypingValidKey_returnsType() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        String expected = "Gum";

        //Act
        String result = testVM.actualInventory.get("D1").getItem().getType();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void item_whenTypingValidKey_returnsName() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        String expected = "U-Chews";

        //Act
        String result = testVM.actualInventory.get("D1").getItem().getName();

        //Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void item_whenTypingValidKey_returnsPrice() {
        //Arrange
        VendingMachine testVM = new VendingMachine(new FileReader("vendingmachine.csv"));
        BigDecimal expected = new BigDecimal("0.85");

        //Act
        BigDecimal result = testVM.actualInventory.get("D1").getItem().getPrice();

        //Assert
        Assert.assertEquals(expected, result);
    }


}
