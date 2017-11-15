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

//katherine
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
//katherine
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
//katherine
    public static void parseSummaryLine(String line){
        summaryLine = line.split(" ");
    }
//katherine
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
//takes two Strings: an Account Number and an Account Name. Adds a new account to the New Master Accounts List,
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
            list.add("0");
            list.add(accName);
            oldMasterAccounts.add(list);
        }
    }

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
//mike
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
//mike
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
//mike
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

//katherine
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
//katherine
    public static boolean accountNumMatchesName(String accNum, String accName){
        for (ArrayList<String> list: oldMasterAccounts){
            if (list.get(1).equals(accNum) && list.get(4).equals(accName)){
                return true;
            }
        }
        return false;
    }
//takes an array of Strings and an int value and determines if the account balance will be zero or not
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
//takes a string that holds an account number and returns true if that account number is in the old master accounts list
    public static boolean masterAccountListContains(String accNum){
        for(ArrayList<String> list: oldMasterAccounts){
            if(list.get(0).equals(accNum)){
                return true;
            }//end if
        }//end for
        return false;
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
    //john
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

    //reads out an error message, than exits the program with error code 1
    public static void fatalError(int code){
        System.out.println("fatal error");
        System.exit(code);
    }

    public static void errorMessage(String message){
        System.out.println("error: " + message + ", transaction skipped");
    }

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
