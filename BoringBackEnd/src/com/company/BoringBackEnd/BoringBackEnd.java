package com.company.BoringBackEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BoringBackEnd {
    public static ArrayList<ArrayList<String>> oldMasterAccounts = null;
    public static ArrayList<ArrayList<String>> newMasterAccounts = null;
    public static String[] summaryLine = null;
    public static String oldMasterAccountsFilename;
    public static String newMasterAccountsFilename;
    public static String validAccountsFilename;
    public static String transactionSummaryFilename;

//katherine
    public static void readOldMasterAccounts() throws Exception{
        ArrayList<String> oldMaster= new ArrayList<>();
        FileReader theList = new FileReader(oldMasterAccountsFilename);
        BufferedReader readList = new BufferedReader(theList);
        String line;
        while ((line = readList.readLine()) != null) {
            oldMaster.add(line);
        }
        readList.close();
        oldMasterAccounts.add(oldMaster);
    }
//katherine
    public static void readAllSummaryFiles(){

    }
//katherine
    public static void readMergeSummaryFile() throws  Exception{

        ArrayList<String> mergeSum = new ArrayList<>();
        FileReader theList = new FileReader(transactionSummaryFilename);
        BufferedReader readList = new BufferedReader(theList);
        String line;
        while ((line = readList.readLine()) != null) {
            checkTransactionCode(parseSummaryLine(line));
        }
        readList.close();

    }
//katherine
    public static String[] parseSummaryLine(String line) throws Exception{
        if(line == null){
                return null;
        }
        summaryLine = line.split(" ");
        return summaryLine;
    }
//katherine
    public static boolean checkTransactionCode(String[] summaryLine){
        switch (summaryLine[0]){
            case "NEW":
                createAccount();
                break;
            case "DEL":
                deleteAccount();
                break;
            case "WDR":
                withdraw();
                break;
            case "XFR":
                transfer();
                break;
            case "DEP":
                deposit();
                break;
            case "EOS":
                break;
            default:
                errorMessage("invalid transaction");
                break;


        }
    }
//john
    public static void createAccount(){

    }
//john
    public static void deleteAccount(){

    }
//mike
    public static void withdraw(){

    }
//mike
    public static void deposit(){

    }
//mike
    public static void transfer(){

    }
//katherine
    public static boolean isBalanceNegative(String accNum, String amount){
        double accountBalance= Double.parseDouble(amount);
        if (accountBalance < 0){
            errorMessage("Account balance is negative");
            return true;
        }
        return false;
    }
//katherine
    public static boolean accountNumMatchesName(String accNum, String accName){
        for (ArrayList<String> list: newMasterAccounts){
            if (list.get(1).equals(accNum) && list.get(4).equals(accName)){
                return true;
            }
        }


        return false;
    }
//john
    public static boolean isBalanceZero(){
        return true;
    }
//john
    public static boolean masterAccountListContains(String accNum){
        return true;
    }

    public static boolean checkValidAccount(){
        return true;
    }

    public static boolean checkValidName(){
        return true;
    }

    public static boolean checkValidAmount(){
        return true;
    }
//john
    public static void writeNewMasterAccounts(){

    }
//john
    public static void writeNewValidAccounts(){

    }
    // takes a String as a parameter, outputs it to console and Exits the program with error code 1
    public static void fatalError(String errorMsg){

        System.exit(1);
    }

    public static void errorMessage(String message){

    }

    public static void main(String[] args) {

    }
}
