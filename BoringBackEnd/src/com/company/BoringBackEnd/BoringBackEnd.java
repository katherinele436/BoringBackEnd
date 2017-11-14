package com.company.BoringBackEnd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BoringBackEnd {
    public static ArrayList<ArrayList<String>> masterAccounts = null;
    public static ArrayList<String[]> oldMasterAccounts = null;
    public static ArrayList<String[]> newMasterAccounts = null;
    public static String[] summaryLine = null;
    public static String oldMasterAccountsFilename;
    public static String newMasterAccountsFilename;
    public static String validAccountsFilename;
    public static String transactionSummaryFilename;

//katherine
    public static ArrayList<String> readOldMasterAccounts() throws Exception{
        ArrayList<String> oldMaster = new ArrayList<>();
        FileReader theList = new FileReader(oldMasterAccountsFilename);
        BufferedReader readList = new BufferedReader(theList);
        String line;
        while ((line = readList.readLine()) != null) {
            oldMaster.add(line);
        }
        readList.close();;
        return oldMaster;
    }
//katherine
    public static void readAllSummaryFiles(){

    }
//katherine
    public static ArrayList<String> readMergeSummaryFile() throws  Exception{

        ArrayList<String> mergeSum = new ArrayList<>();
        FileReader theList = new FileReader(transactionSummaryFilename);
        BufferedReader readList = new BufferedReader(theList);
        String line;
        while ((line = readList.readLine()) != null && !line.equals("EOS 0000000 000 0000000")) {
            mergeSum.add(line);
        }
        readList.close();
        return mergeSum;

    }
//katherine
    public static String[] parseSummaryLine() throws Exception{
        FileReader theList = new FileReader(transactionSummaryFilename);
        BufferedReader readList = new BufferedReader(theList);
        String line = readList.readLine();
        if(line == null){
                return null;
        }
        summaryLine = line.split(" ");
        return summaryLine;
    }
//katherine
    public static boolean checkTransactionCode(String transaction){
        return (!transaction.equals("EOS") || !transaction.equals("NEW") || !transaction.equals("DEL") || !transaction.equals("WDR") || !transaction.equals("XFR") || !transaction.equals("DEP"));
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
        for (ArrayList<String> list: masterAccounts){
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
