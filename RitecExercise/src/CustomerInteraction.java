/*
You have been asked to design the code for an ATM machine. 
Another group will be creating the interface. Your task is 
only to create the code portion.

Requirements:
Assume a starting balance (make it up yourself)
Start with withdrawal functionality in denominations of 20’s
Do not allow transactions that will cause a negative balance.

Extra credit: (Not required)
Dispense other denominations
Allow account to go negative and charge a fee
Or whatever you can think up!
*/

import java.io.IOException;
import java.util.Scanner;

public class CustomerInteraction 
{
    public static void main(String[] args) throws IOException 
    {
        Scanner r = new Scanner(System.in);
        System.out.println( "Customer welcome information displayed here" );
        System.out.println( "Customer prompted to enter account number here" );

        boolean AccountAccessable = false;
        int account = 0;
        do
        {
            account = r.nextInt();
            
            //TODO: Implement account checking to detect if account actually
            //      exists, not being tested for this in the interview
            AccountAccessable = true;
        }
        while( !AccountAccessable );
        
        ATM atm = new ATM(account);
        
        System.out.println( "Customer account interface display" );
        
        //Customer account interface displays here
        //-1 = Exit code
        // 1 = Withdraw Checking
        // 2 = Withdraw Savings
        // 3 = Make a Deposit Checking
        // 4 = Make a Deposit Savings
        // 5 = Display Account info
        
        double Amount = 0;
        int    choice = 0;
        do
        {
            System.out.println( "Asking choice of customer" );
            choice = r.nextInt();
            
            switch(choice)
            {
                case 1:
                    System.out.println( "Prompt customer to enter amount to withdrawl from Checking" );
                    //TODO: For now we're assuming the customer put in a withdraw request of even
                    //      denominations of $20s, $10s, $5s, and $1s
                    Amount = r.nextDouble();
                    atm.WithdrawChecking(Amount);
                    break;
                case 2:
                    System.out.println( "Prompt customer to enter amount to withdrawl from Saving" );
                    //TODO: For now we're assuming the customer put in a withdraw request of even
                    //      denominations of $20s, $10s, $5s, and $1s
                    Amount = r.nextDouble();
                    atm.WithdrawSavings(Amount);
                    break;
                case 3:
                    System.out.println( "Prompt customer to enter amount as well as deposit" );
                    System.out.println( "cooresponding cash/check in ATM envelope into money slot" );
                    Amount = r.nextDouble();
                    atm.MakeDeposit(Amount, true);
                    break;
                case 4:
                    System.out.println( "Prompt customer to enter amount as well as deposit" );
                    System.out.println( "cooresponding cash/check  in ATM envelope into money slot" );
                    Amount = r.nextDouble();
                    atm.MakeDeposit(Amount, false);
                    break;
                case 5:
                    atm.DisplayCustomerAccountInfo();
                    break;
                default : 
                    //Default not needed
            }
        }
        while( choice != -1 );
        
        System.out.println( "Session terminated" );
        //End session with customer here
        r.close();
        
        //atm.MakeDeposit(55.81, true);
    }
    
    
}