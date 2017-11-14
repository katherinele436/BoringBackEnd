package com.company.BoringBackEnd;

import java.util.*;

public class BoringBackEnd {

    public static ArrayList<String[]> oldMasterAccounts = null;
    public static ArrayList<String[]> newMasterAccounts = null;
    public static String[] summaryLine = null;
    public static String oldMasterAccountsFilename;
    public static String newMasterAccountsFilename;
    public static String validAccountsFilename;
    public static String transactionSummaryFilename;

//katherine
    public static void readOldMasterAccounts(){

    }
//katherine
    public static void readAllSummaryFiles(){

    }
//katherine
    public static void readSummaryFile(){

    }
//katherine
    public static void parseSummaryLine(){

    }
//katherine
    public static void checkTransactionCode(){

    }
//john
    public static void createAccount(String accNum, String accName){
        String[] newAccount=[accNum, "0", accName];
        newMasterAccounts.add(newAccount);
    }
//john
    public static void deleteAccount(){

    }

    public static void withdraw(){

    }

    public static void deposit(){

    }

    public static void transfer(){

    }
//katherine
    public static boolean isBalanceNegative(){
        return true;
    }
//katherine
    public static boolean accountNumMatchesName(){
        return true;
    }
//takes an array of Strings and an int value and determines if the account balance will be zero or not
    public static boolean isBalanceZero(String[] account, int transact){
        int accountBalance=Integer.parseInt(account[1]);
        if(accountBalance-transact<=0){
            return true;
        }//end if
        return false;
    }
//takes a string that holds an account number and returns true if that account number is in the old master accounts list
    public static boolean masterAccountListContains(String accNum){
        for(i in oldMasterAccounts){
            String[] arr=oldMasterAccounts[i];
            if(arr[0]==accNum){
                return true;
            }//end if
        }//end for
        return false;
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
//john
    //reads out an error message, than exits the program with error code 1
    public static void fatalError(String errorMsg){
        errorMessage(errorMsg);
        System.exit(1);
    }

    public static void errorMessage(String message){

    }

    public static void main(String[] args) {

    }
}
