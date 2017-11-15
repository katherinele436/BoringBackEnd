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
                fatalError();
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
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0) == accNum){
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
            fatalError();
        }
        //ensures account number is on the master accounts list
        else if (!masterAccountListContains(accNum)){
            errorMessage("account not in master accounts list");
        }
        //ensures amount field is valid
        else if (!checkValidAmount(amount)){
            fatalError();
        }
        else{
            //find account number in master accounts, subtract amount from balance
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0) == accNum){
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
            fatalError();
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
            fatalError();
        }
        else{
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0) == toAccount){
                    newToAmount = Integer.parseInt(list.get(1)) + Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newToAmount));
                }
            }
            for (ArrayList<String> list: oldMasterAccounts){
                if(list.get(0) == fromAccount){
                    newFromAmount = Integer.parseInt(list.get(1)) + Integer.parseInt(amount);
                    list.remove(1);
                    list.add(1, Integer.toString(newFromAmount));
                }
            }
        }
    }

//katherine
    public static boolean isBalanceNegative(String accNum, String amount){
        int accountBalance= Integer.parseInt(amount);
        if (accountBalance < 0){
            errorMessage("Account balance is negative");
            return true;
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
    // takes a String as a parameter, outputs it to console and Exits the program with error code 1
    public static void fatalError(){

        System.exit(1);
    }

    public static void errorMessage(String message){
        System.out.println("error: " + message + ", transaction skipped");
    }

    public static void main(String[] args) {
        oldMasterAccounts = new ArrayList<ArrayList<String>>();
        newMasterAccounts = new ArrayList<ArrayList<String>>();
        readOldMasterAccounts();
        readAllSummaryFiles();
        newMasterAccounts = oldMasterAccounts;
        writeNewMasterAccounts();
        writeNewValidAccounts();
    }
}
