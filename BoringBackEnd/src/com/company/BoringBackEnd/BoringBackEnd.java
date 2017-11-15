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
  
//takes two Strings: an Account Number and an Account Name. Adds a new account to the New Master Accounts List,

    public static void createAccount(){
        String accNum=summaryLine[1];
        String accName=summaryLine[3];
        String[] newAccount=[accNum, "0", accName];
        oldMasterAccounts.add(newAccount);
    }
  
//takes an array of strings. Deletes an account from the New Master Accounts List.
//Only Used BEFORE oldMasterAccounts has been transferred over to newMasterAccounts
    public static void deleteAccount(){
        String accNum=summaryLine[1];
        String accName=summaryLine[3];
        if(isBalanceZero()){
            if(masterAccountListContains()){
                String[]=account[accNum, "0", accName];
                for(i=0; i<oldMasterAccounts.size(); i++ ){
                    if(oldMasterAccounts[i]==account){
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

//takes an array of Strings and an int value and determines if the account balance will be zero or not
    public static boolean isBalanceZero(){
        String accNum=summaryLine[1]
        for(ArrayList<String> list: oldMasterAccounts) {
            if(list.get(0)==accNum){
                int accountBalance=Integer.parseInt(list.get(1));
                if(accountBalance==0){
                    return true;
                }//end if
                return false:
            }
        }//end for
        return false;
    }
//takes a string that holds an account number and returns true if that account number is in the old master accounts list
    public static boolean masterAccountListContains(){
        String accNum=summaryLine[1]
        for(ArrayList<String> list: oldMasterAccounts){ ;
            if(list.get(0)==accNum){
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
        Collections.sort(newMasterAccounts, new Comparator<List<String>> () {
            @Override //overides Comparator function to compare a List of Strings and sort it properly
            public int compare(List<String> stringA, List<String> stringB) {
                return stringA.get(1).compareTo(stringB.get(1));
            }
        });
        newMasterAccounts.add
        //Second Write To File
        sortNewMasterAccounts();//Sorts global List newMasterAccounts
        FileWriter writer = new FileWriter(newMasterAccountsFilename);
        for(String[] str: newMasterAccounts) {
            writer.write(str[0]+ " "+str[1]+" "+str[2]);
        }
        writer.close();
    }
    //john
    public static void writeNewValidAccounts(){
        FileWriter writer = new FileWriter(newMasterAccountsFilename);
        for(String[] str: newMasterAccounts) {
            writer.write(str[0]);
        }
        writer.close();
    }

    //reads out an error message, than exits the program with error code 1
    public static void fatalError(String errorMsg){
        errorMessage(errorMsg);
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
