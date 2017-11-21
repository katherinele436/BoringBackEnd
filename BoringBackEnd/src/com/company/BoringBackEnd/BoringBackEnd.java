package com.company.BoringBackEnd;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoringBackEnd {
    public static ArrayList<ArrayList<String>> oldMasterAccounts = null;
    public static ArrayList<ArrayList<String>> newMasterAccounts = null;
    public static String[] summaryLine = null;
    public static String oldMasterAccountsFilename;
    public static String newMasterAccountsFilename;
    public static String validAccountsFilename;
    public static String transactionSummaryFilename;

    //reads the old master accounts file and places the data in an arraylist
    public static void readOldMasterAccounts(){
        FileReader theList = null;
        try{
            theList = new FileReader(oldMasterAccountsFilename);
        }catch (FileNotFoundException e){
            fatalError(600);
        }
        BufferedReader readList = new BufferedReader(theList);
        try {
            String line = readList.readLine();
            String[] data = new String[3];
            while (line != null) {
                ArrayList<String> oldMaster= new ArrayList<>();
                data = line.split(" ");
                oldMaster.add(data[0]);
                oldMaster.add(data[1]);
                oldMaster.add(data[2]);
                oldMasterAccounts.add(oldMaster);
                line = readList.readLine();
            }
            readList.close();
        }catch (IOException e){
            fatalError(601);
        }
    }
    //reads the merged transaction summary file line by line
    public static void readMergeSummaryFile(){
        FileReader theList = null;
        try {
            theList = new FileReader(transactionSummaryFilename);
        }catch(FileNotFoundException e){
            fatalError(500);
        }
        BufferedReader readList = new BufferedReader(theList);
        try {
            String line = readList.readLine();
            while (line != null) {
                parseSummaryLine(line);
                checkTransactionCode();
                line = readList.readLine();
            }
            readList.close();
        }catch(IOException e){
            fatalError(501);
        }
    }
    //parses each line of the summary file into 5 strings of data
    public static void parseSummaryLine(String line){
        summaryLine = line.split(" ");
    }
    //switch statement that calls a relevant transaction function for the parsed line
    public static void checkTransactionCode(){
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
                fatalError(100);
                break;
        }
    }
    //creates an account in the master accounts if all fields valid, and account is unused
    public static void createAccount(){
        String accNum = summaryLine[1];
        String accName = summaryLine[4];
        if(masterAccountListContains(accNum)){
            errorMessage("account number already exists");
        }
        else if(!checkValidAccount(accNum) || !checkValidName(accName)){
            fatalError(201);
        }
        else {
            ArrayList<String> list = new ArrayList<String>();
            list.add(accNum);
            list.add("000");
            list.add(accName);
            oldMasterAccounts.add(list);
        }
    }
    //deletes account from master accounts if balance is zero, account number matches name,
    //account is in master accounts, and all other fields of the summary line are valid
    public static void deleteAccount(){
        String accNum = summaryLine[1];
        String accName = summaryLine[4];
        if(!checkValidName(accName) || !checkValidAccount(accNum)){
            fatalError(202);
        }
        if(!masterAccountListContains(accNum)){
            errorMessage("account not in master accounts list");
            return;
        }
        if(accountNumMatchesName(accNum,accName)){
            if(isBalanceZero(accNum)){
                int count=0;
                for(ArrayList<String> list: oldMasterAccounts){
                    if(list.get(0).equals(accNum) && list.get(2).equals(accName)){
                        oldMasterAccounts.remove(count);
                        return;
                    }//end if
                    count++;
                }//end for
            }else{
               errorMessage("account does not have 0 balance");
               return;
            }//end if
        }else{
            errorMessage("account number does not match name");
            return;
        }//end if
    }
    //subtracts amount from account within master accounts if the balance would not be negative,
    //the account is in master accounts, and all other fields are valid
    public static void withdraw(){
        String accNum = summaryLine[3];
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
            fatalError(203);
        }
        //ensures amount field is valid
        else if (!checkValidAmount(amount)){
            fatalError(204);
        }
        else{
            //find account number in master accounts, subtract amount from balance
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0).equals(accNum)){
                    newAmount = Integer.parseInt(list.get(1)) - Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newAmount));
                }
            }
        }
    }
    //adds amount to account if account is in master accounts and all other fields are valid
    public static void deposit(){
        String accNum = summaryLine[1];
        String amount = summaryLine[2];
        int newAmount;
        //ensures account number field is valid
        if (!checkValidAccount(accNum)){
            fatalError(205);
        }
        //ensures account number is on the master accounts list
        else if (!masterAccountListContains(accNum)){
            errorMessage("account not in master accounts list");
        }
        //ensures amount field is valid
        else if (!checkValidAmount(amount)){
            fatalError(206);
        }
        else{
            //find account number in master accounts, subtract amount from balance
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0).equals(accNum)){
                    newAmount = Integer.parseInt(list.get(1)) + Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newAmount));
                }
            }
        }
    }
    //adds amount to one account and subtracts from other account if both are in master accounts,
    //all fields are valid, and the from account would not have negative balance
    public static void transfer(){
        String toAccount = summaryLine[1];
        String fromAccount = summaryLine[3];
        String amount = summaryLine[2];
        int newToAmount, newFromAmount;
        //ensures account number field is valid
        if (!checkValidAccount(toAccount) || !checkValidAccount(fromAccount)){
            fatalError(207);
        }
        //ensures account number is on the master accounts list
        else if (!masterAccountListContains(toAccount) || !masterAccountListContains(fromAccount)){
            errorMessage("account not in master accounts list");
        }
        //ensures balance will not be negative after this transaction
        else if (isBalanceNegative(fromAccount, amount)){
            errorMessage("balance would be negative");
        }
        //ensures amount field is valid
        else if (!checkValidAmount(amount)){
            fatalError(208);
        }
        else{
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0).equals(toAccount)){
                    newToAmount = Integer.parseInt(list.get(1)) + Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newToAmount));
                }
            }
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0).equals(fromAccount)){
                    newFromAmount = Integer.parseInt(list.get(1)) + Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newFromAmount));
                }
            }
        }
    }

    //conditional checking if balance would be negative after subtracting amount
    public static boolean isBalanceNegative(String accNum, String amount){
        for (ArrayList<String> list: oldMasterAccounts){
            if(list.get(0).equals(accNum)){
                int newBalance = Integer.parseInt(list.get(1)) - Integer.parseInt(amount);
                if(newBalance < 0){
                    return true;
                }
            }
        }
        return false;
    }
    //conditional checking if the account name matches the account number within master accounts
    public static boolean accountNumMatchesName(String accNum, String accName){
        for (ArrayList<String> list: oldMasterAccounts){
            if (list.get(1).equals(accNum) && list.get(4).equals(accName)){
                return true;
            }
        }
        return false;
    }
    //conditional checking if the account balance is zero for a specified account number
    public static boolean isBalanceZero(String accNum){
        for(ArrayList<String> list: oldMasterAccounts) {
            if(list.get(0).equals(accNum)){
                int accountBalance = Integer.parseInt(list.get(1));
                if(accountBalance == 0){
                    return true;
                }//end if
                return false;
            }
        }//end for
        return false;
    }
    //conditional checking if master accounts list contains the specified account number
    public static boolean masterAccountListContains(String accNum){
        for(ArrayList<String> list: oldMasterAccounts){
            if(list.get(0).equals(accNum)){
                return true;
            }//end if
        }//end for
        return false;
    }
    //checks if the account number field is valid
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
    //checks if the account name field is valid
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
    //checks if the amount field is valid
    public static boolean checkValidAmount(String amount){
        int check;
        try{
            check = Integer.parseInt(amount);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
  
    //sorts new master accounts list and writes to new master accounts file
    public static void writeNewMasterAccounts(){
        //First Sort New Master Accounts Array List
        Collections.sort(newMasterAccounts, new Comparator<ArrayList<String>> () {
            @Override //overides Comparator function to compare a List of Strings and sort it properly
            public int compare(ArrayList<String> listA, ArrayList<String> listB) {
                return listA.get(0).compareTo(listB.get(0));
            }
        });
        //Second Write To File
        FileWriter writer = null;
        try {
            writer = new FileWriter(newMasterAccountsFilename);
            for (ArrayList<String> list : newMasterAccounts) {
                writer.write(list.get(0) + " " + list.get(1) + " " + list.get(2) +"\n");
            }
            writer.close();
        }catch(IOException e){
            fatalError(300);
        }
    }
    //writes the account numbers in new master accounts to valid accounts file
    public static void writeNewValidAccounts(){
        FileWriter writer = null;
        try {
            writer = new FileWriter(validAccountsFilename);
            for (ArrayList<String> list : newMasterAccounts) {
                writer.write(list.get(0) + "\n");
            }
            writer.close();
        }catch(IOException e){
            fatalError(301);
        }
    }

    //reads out an error message, than exits the program with a specified error code
    public static void fatalError(int code){
        System.out.println("fatal error");
        System.exit(code);
    }
    //takes in a string and prints to standard output as an error message
    public static void errorMessage(String message){
        System.out.println("error: " + message + ", transaction skipped");
    }
/*
    This commandline program takes in 4 files as arguements: transaction summary file, old master accounts
    list, new master accounts list, and valid accounts list. The transaction summary file and old master accounts
    are used by the program as input, while the output files are the new master accounts and the valid accounts.
    This program also outputs an error log to standard output. The intention of this program is to update the
    master accounts and the valid accounts lists using the daily transaction summaries.
*/
    public static void main(String[] args) {
        transactionSummaryFilename = args[0];
        oldMasterAccountsFilename = args[1];
        newMasterAccountsFilename = args[2];
        validAccountsFilename = args[3];
        oldMasterAccounts = new ArrayList<ArrayList<String>>();
        newMasterAccounts = new ArrayList<ArrayList<String>>();
        readOldMasterAccounts();
        readMergeSummaryFile();
        newMasterAccounts = oldMasterAccounts;
        writeNewMasterAccounts();
        writeNewValidAccounts();
    }
}
