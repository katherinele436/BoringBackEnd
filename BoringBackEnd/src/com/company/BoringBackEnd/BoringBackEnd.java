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
//takes two Strings: an Account Number and an Account Name. Adds a new account to the New Master Accounts List,
    public static void createAccount(String accNum, String accName){
        String[] newAccount=[accNum, "0", accName];
        newMasterAccounts.add(newAccount);
    }
//takes an array of strings. Deletes an account from the New Master Accounts List.
//Only Used BEFORE oldMasterAccounts has been transferred over to newMasterAccounts
    public static void deleteAccount(String[] account){
        if(isBalanceZero(account)){
            if(masterAccountListContains(account[0])){
                for(i=0; i<oldMasterAccounts.size(); i++ ){
                    if(oldMasterAccounts[i]=account){
                        oldMasterAccounts.remove(i);
                        return;
                    }//end if
                }//end for
            }else{
               errorMessage("Error: Account Not in Found in Old Master Accounts File");
               return;
            }//end if
        }else{
            errorMessage("Error: Account does not have 0 balance");
            return;
        }//end if

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
    public static boolean isBalanceZero(String[] account){
        int accountBalance=Integer.parseInt(account[1]);
        if(accountBalance==0){
            return true;
        }//end if
        return false;
    }
//takes a string that holds an account number and returns true if that account number is in the old master accounts list
    public static boolean masterAccountListContains(String accNum){
        for(i : oldMasterAccounts){
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
