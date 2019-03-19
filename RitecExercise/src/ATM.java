import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


//Assumptions made:
//  -The account number when passed has already been checked if
//   the account exists and at this point the account exists
//  -All account files are first line customer name, second
//   line customer checking account amount, and third line
//   customer savings account amount

public class ATM 
{
    private String       CustomerName;
    private double       AmountChecking;
    private double       AmountSaving;
    private int          AccountNumber;
    private List<String> Deposits;
    private double       OverdraftCharge;
    
    public ATM(int AccountNum) throws IOException
    {
        OverdraftCharge = .10; //Current bank overdraft fee of the amount requested
        Deposits        = new ArrayList<String>();
        AccountNumber   = AccountNum;
        File    f       = new File(AccountNumber + ".acc"); 
        Scanner s       = new Scanner(f); 
        
        //Set up information for customer interaction
        CustomerName   = s.nextLine();
        AmountChecking = Double.parseDouble(s.nextLine());
        AmountSaving   = Double.parseDouble(s.nextLine());
        
        //Grab any deposits still not reported to the bank
        while( s.hasNext() )
        {
            Deposits.add(s.nextLine());
        }
        s.close();
    }
    
    private void UpdateCustomerAccount() throws IOException
    {
        String AccountContent = CustomerName + "\n" + AmountChecking + "\n" + AmountSaving + "\n";
        BufferedWriter writer = new BufferedWriter(new FileWriter(AccountNumber + ".acc"));
        writer.write(AccountContent);
        for (int i = 0; i < Deposits.size(); i++) 
        {
            writer.write(Deposits.get(i) + "\n");
        }
        writer.close();
    }
    
    /**
     * Cash/check deposits need to be verified by the bank which 
     * usually takes 1-2 business days to do so.  Therefore, the 
     * information of a deposit is stored and not added to the 
     * running account total until verified by the bank.
     */
    public void MakeDeposit(double deposit, boolean checking) throws IOException
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        
        String DepositDirection;
        if(checking)
        {
            DepositDirection = "Checking Deposit: ";
        }
        else
        {
            DepositDirection = "Savings Deposit:  ";
        }
        
        Deposits.add( DepositDirection + deposit + " " + dateFormat.format(date) );
        //TODO: Add ATM locations to the deposit notes
        
        UpdateCustomerAccount();
    }
    
    public void DisplayCustomerAccountInfo()
    {
        System.out.println(CustomerName);
        System.out.println("Checking: " + AmountChecking);
        System.out.println("Savings : " + AmountSaving  );
        for (int i = 0; i < Deposits.size(); i++) 
        {
            System.out.println(Deposits.get(i));
        }
    }
    
    /** 
     * 3 Scenarios for a withdraw
     * Scenario 1: Customer has enough in corresponding checking
     * Scenario 2: Customer doesn't have enough in corresponding checking
     *             and will be charged an overdraft fee on their savings
     *             as well as their savings will compensate for the deficit
     *             the checking was unable to fulfill of the customer's
     *             request
     * Scenario 3: Customer has requested more money than their checking
     *             and savings combined cannot fulfill, withdraw action
     *             aborted
     */
    public void WithdrawChecking(double withdrawl) throws IOException
    {
        if( AmountChecking - withdrawl > -.01 )
        {
            //Scenario 1
            AmountChecking -= withdrawl;
            Denominations(withdrawl);
        }
        else
        {
            double Remaining = AmountChecking - withdrawl;
            double Surcharge = withdrawl * OverdraftCharge;
            if( Remaining + AmountSaving - Surcharge >= -.01 )
            {
                //Scenario 2
                AmountChecking = 0.0;
                AmountSaving  += Remaining; //Because Remaining is a neg number
                AmountSaving  -= Surcharge; //Because Surcharge is a pos number
                Denominations(withdrawl);
                
                //TODO: Place function to send Surcharge to the bank here
            }
            else
            {
                //Scenario 3
                System.out.println("Prompt customer amount requested exceeds available");
                System.out.println("funds in account and withdrawl action has been aborted");
            }
        }
    }
    
    /** 
     * 3 Scenarios for a withdraw
     * Scenario 1: Customer has enough in corresponding savings
     * Scenario 2: Customer doesn't have enough in corresponding savings
     *             and will be charged an overdraft fee on their checking
     *             as well as their checking will compensate for the deficit
     *             the savings was unable to fulfill of the customer's
     *             request
     * Scenario 3: Customer has requested more money than their checking
     *             and savings combined cannot fulfill, withdraw action
     *             aborted
     */
    public void WithdrawSavings(double withdrawl) throws IOException
    {
        if( AmountSaving - withdrawl > -.01 )
        {
            //Scenario 1
            AmountSaving -= withdrawl;
            Denominations(withdrawl);
        }
        else
        {
            double Remaining = AmountSaving - withdrawl;
            double Surcharge = withdrawl * OverdraftCharge;
            if( Remaining + AmountChecking - Surcharge > -.01 )
            {
                //Scenario 2
                AmountSaving    = 0.0;
                AmountChecking += Remaining; //Because Remaining is a neg number
                AmountChecking -= Surcharge; //Because Surcharge is a pos number
                Denominations(withdrawl);
                
                //TODO: Place function to send Surcharge to the bank here
            }
            else
            {
                //Scenario 3
                System.out.println("Prompt customer amount requested exceeds available");
                System.out.println("funds in account and withdrawl action has been aborted");
            }
        }
    }
    //WithdrawSavings and WithdrawChecking could've been cleaner by combining into
    //one function and just use logic to take funds from one to the other.  Figured
    //I'd just finish this up under interview test time constraints.
    
    //Note: Weird things happen in the realm of doubles and floats, especially when
    //      dealing with money.  This is why Checking and Savings when overdrawn
    //      gets set to 0.0 rather than a simple Checking minus Checking or
    //      Savings minus Savings to get to zero.  This is to ensure no ghost $.001
    //      cents are leftover for the customer to steal from the bank.
    
    private void Denominations(double withdrawl) throws IOException
    {
        //Amount of each bill to give to customer
        //TODO: For now we're assuming the ATM has infinite enough of 
        //      these to give out
        int C20 = 0;
        int C10 = 0;
        int C05 = 0;
        int C01 = 0;
        
        //Twenties
        while( withdrawl >= 20.0 )
        {
            withdrawl -= 20.0;
            C20++;
        }
        
        //Tens
        while( withdrawl >= 10.0 )
        {
            withdrawl -= 10.0;
            C10++;
        }
        
        //Fives
        while( withdrawl >= 5.0 )
        {
            withdrawl -= 5.0;
            C05++;
        }
        
        //Ones
        while( withdrawl >= 1.0 )
        {
            withdrawl -= 1.0;
            C01++;
        }
        
        System.out.println( "Customer recieves" );
        System.out.println( "20 Bills: " + C20 );
        System.out.println( "10 Bills: " + C10 );
        System.out.println( " 5 Bills: " + C05 );
        System.out.println( " 1 Bills: " + C01 );
        
        UpdateCustomerAccount();
    }
}