package com.company.BoringBackEnd;

import java.util.ArrayList;
import java.util.regex.*;

public class BoringBackEnd {

    public static ArrayList<ArrayList<String>> masterAccounts = null;
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
    public static void createAccount(){

    }
    //john
    public static void deleteAccount(){

    }

    public static void withdraw(){
        String accNum = summaryLine[1];
        String amount = summaryLine[2];
        int newAmount;
        //ensures account number is on the master accounts list
        if (!masterAccountListContains(accNum)){
            errorMessage("account not in master accounts list");
        }
        //ensures balance will not be negative after this transaction
        else if (isBalanceNegative(accNum, amount)){
            errorMessage("balance would be negative");
        }
        //ensures account number field is valid
        else if (!checkValidAccount(accNum)){
            fatalError();
        }
        //ensures amount field is valid
        else if (!checkValidAmount(amount)){
            fatalError();
        }
        else{
            //find account number in master accounts, subtract amount from balance
            for (ArrayList<String> list: masterAccounts){
                if(list.get(0) == accNum){
                    newAmount = Integer.parseInt(list.get(1)) - Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newAmount));
                }
            }
        }


    }

    public static void deposit(){

    }

    public static void transfer(){

    }
    //katherine
    public static boolean isBalanceNegative(String accNum, String amount){
        return true;
    }
    //katherine
    public static boolean accountNumMatchesName(String accNum){
        return true;
    }
    //john
    public static boolean isBalanceZero(String accNum){
        return true;
    }
    //john
    public static boolean masterAccountListContains(String accNum){
        return true;
    }

    public static boolean checkValidAccount(String accNum){
        int account;
        try{
            account = Integer.parseInt(accNum);
        }catch(NumberFormatException e){
            return false;
        }
        //ensures account number is 7 digits
        if (accNum.length() != 7){
            return false;
        }
        //ensures account number does not start with zero
        if (accNum.startsWith("0")){
            return false;
        }
        return true;
    }

    public static boolean checkValidName(String name){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(name);
        //ensures name does not start or end with a space
        if (name.endsWith(" ") || name.startsWith(" ")) {
            return false;
        }
        //ensures name is between 3 and 30 characters
        else if (name.length() > 30 || name.length() < 3){
            return false;
        }
        //ensures name is alphanumeric
        else if (!matcher.matches()) {
            return false;
        }
        return true;
    }

    public static boolean checkValidAmount(String amount){
        int check;
        try{
            check = Integer.parseInt(amount);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    //john
    public static void writeNewMasterAccounts(){

    }
    //john
    public static void writeNewValidAccounts(){

    }
    //john
    public static void fatalError(){

    }

    public static void errorMessage(String message){
        System.out.println("error: " + message + ", transaction skipped");
    }

    public static void main(String[] args) {
        masterAccounts = new ArrayList<ArrayList<String>>();
        readOldMasterAccounts();
        readAllSummaryFiles();
        writeNewMasterAccounts();
        writeNewValidAccounts();
    }
}
