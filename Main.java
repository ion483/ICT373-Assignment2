/*
 * Title: Magazine Service Test Driver
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * File Name: Main.java
 * Purpose: Test driver to construct a Magazine service, register paying and associate
 *          customers with various payment methods, generate 4 weeks of customer emails,
 *          produce end-of-month billing statements, and demonstrate customer addition/removal.
 * Assumptions:
 *  - 1 month consists of exactly 4 weekly magazine issues.
 *  - Console input via Scanner is optional (users can select built-in defaults or enter custom details).
 *  - Essential core entities (such as Customer names and email addresses) cannot be null, empty, 
 *    or composed entirely of whitespace. 
 *    Attempting to instantiate an entity with blank data will immediately trigger a runtime exception (IllegalArgumentException)
 *    and program terminates
 */




import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args){
        displayStudentDetails();
        Scanner scanner = new Scanner(System.in);
        Magazine magazine = new Magazine(); // Assume the base magazine cost is $10.00/week

        System.out.println("\n==================================================");
        System.out.println("       MAGAZINE SERVICE INITIALIZATION");
        System.out.println("==================================================");
        System.out.println("Select Data Initialization Mode:");
        System.out.println("1. Use Pre-Populated Default Data (Fast Test)");
        System.out.println("2. Interactive Input via Scanner");
        System.out.print("Enter choice (1 or 2): ");

        int choice = 1;
        if(scanner.hasNextInt()){
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }

        if (choice == 2) {
            setupInteractive(magazine, scanner);
        } else {
            setupBuiltInDefaults(magazine);
        }

        // Step c: Print weekly emails for 4 weeks
        runStepC_WeeklyEmails(magazine);

        // Step d: Print monthly billing emails for paying customers
        runStepD_MonthlyInvoices(magazine);

        // Step e: Add a new customer to the service
        runStepE_AddCustomer(magazine, scanner);

        // Step f: Remove an existing customer from the service
        runStepF_RemoveCustomer(magazine, scanner);

        System.out.println("\n==================================================");
        System.out.println("       TEST SUITE COMPLETED SUCCESSFULLY");
        System.out.println("==================================================\n");

        scanner.close();

    }

    /**
     * Prints student identification metadata header to console output.
     */
    public static void displayStudentDetails() {
        System.out.println("==================================================");
        System.out.println("               STUDENT DETAILS");
        System.out.println("==================================================");
        System.out.println("Name:               Chang Yun-Shiang");
        System.out.println("Student ID:         35702674");
        System.out.println("Mode of Enrolment:  External");
        System.out.println("Tutor Name:         Poh Kok Loo");
        System.out.println("Tutorial Day/Time:  2026/10/2 8:00 AM");
        System.out.println("==================================================");
    }

    /**
     * Pre-populates the magazine instance with 4 default supplements and 6 default customers.
     *
     * @param magazine the target Magazine service instance to populate
     */
    private static void setupBuiltInDefaults(Magazine magazine){
        System.out.println("\n[INFO] Initializing default supplements and customer accounts...");

        // Create 4 Supplements
        Supplement tech = new Supplement("Tech & Gadgets", 4.50);
        Supplement health = new Supplement("Health & Fitness", 3.00);
        Supplement gaming = new Supplement("Gaming Monthly", 5.00);
        Supplement cooking = new Supplement("Gourmet Cooking", 2.50);

        magazine.addSupplement(tech);
        magazine.addSupplement(health);
        magazine.addSupplement(gaming);
        magazine.addSupplement(cooking);

        // Create Payment Methods
        PaymentMethod card1 = new CreditCard("Alice Smith", "4111222233334444", "12/28");
        PaymentMethod bank1 = new BankAccount("Charlie Brown", "123-456", "98765432");

        // Create Paying Customers
        PayingCustomer alice = new PayingCustomer("Alice Smith", "alice@example.com", card1);
        alice.addSupplement(tech);
        alice.addSupplement(health);

        PayingCustomer charlie = new PayingCustomer("Charlie Brown", "charlie@example.com", bank1);
        charlie.addSupplement(gaming);

        // Create Associate Customers
        AssociateCustomer bob = new AssociateCustomer("Bob Smith", "bob@example.com", alice);
        bob.addSupplement(gaming);
        alice.addAssociateCustomer(bob);

        AssociateCustomer david = new AssociateCustomer("David Brown", "david@example.com", charlie);
        david.addSupplement(cooking);
        david.addSupplement(health);
        charlie.addAssociateCustomer(david);

        AssociateCustomer eve = new AssociateCustomer("Eve Brown", "eve@example.com", charlie);
        eve.addSupplement(tech);
        charlie.addAssociateCustomer(eve);

        // Create Standalone Paying Customer without associates
        PaymentMethod card2 = new CreditCard("Frank Wright", "5500000000001111", "08/27");
        PayingCustomer frank = new PayingCustomer("Frank Wright", "frank@example.com", card2);
        frank.addSupplement(cooking);

        // Register all customers with the magazine
        magazine.addCustomer(alice);
        magazine.addCustomer(bob);
        magazine.addCustomer(charlie);
        magazine.addCustomer(david);
        magazine.addCustomer(eve);
        magazine.addCustomer(frank);

        System.out.println("[SUCCESS] Loaded " + magazine.getAvailableSupplements().size() 
                + " supplements and " + magazine.getCustomers().size() + " customers.");
    }

    /**
     * Interactively prompts user via Scanner to input supplements and customers.
     *
     * @param magazine the target Magazine service instance to populate
     * @param scanner  the active Scanner instance for reading console input
     */
    private static void setupInteractive(Magazine magazine, Scanner scanner) {
        System.out.println("\n--- Interactive Supplement Setup ---");
        System.out.print("How many supplements would you like to add? ");
        int suppCount = 3;
        try {
            suppCount = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[INFO] Invalid input. Defaulting to 3 supplements.");
        }

        // 1. Prompt for supplements
        for (int i = 1; i <= suppCount; i++) {
            System.out.println("\nSupplement #" + i + ":");
            System.out.print("  Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  Weekly Cost ($): ");
            double cost = 3.50;
            try {
                cost = Double.parseDouble(scanner.nextLine().trim());
                if (cost < 0) {
                    System.out.println("[INFO] Cost cannot be negative. Defaulting to $0.00.");
                    cost = 0.0; 
                }
            } catch (NumberFormatException e) {
                System.out.println("[INFO] Invalid cost. Defaulting to $3.50.");
            }
            boolean success = magazine.addSupplement(new Supplement(name, cost));
            if(success){
                System.out.println("\nSupplement (" + name + ") added");
            }else{
                System.out.println("\nSupplement (" + name + ") failed to be added(it might be already added)");
            }
        }

        // 2. Prompt for customers
        System.out.println("\n--- Interactive Customer Setup ---");
        System.out.print("How many customers would you like to add? ");
        int custCount = 1;
        try {
            custCount = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[INFO] Invalid input. Defaulting to 1 customer.");
        }

        for (int i = 1; i <= custCount; i++) {
            System.out.println("\nCustomer #" + i + ":");

            System.out.print("  Name: ");
            String custName = scanner.nextLine().trim();
            while (custName == null || custName.isEmpty()) {
                System.out.println("[INFO] Name cannot be empty. Please enter a valid name.");
                System.out.print("  Name: ");
                custName = scanner.nextLine().trim();
            }

            System.out.print("  Email: ");
            String custEmail = scanner.nextLine().trim();
            while (custEmail == null || custEmail.isEmpty()) {
                System.out.println("[INFO] Email cannot be empty. Please enter a valid email.");
                System.out.print("  Email: ");
                custEmail = scanner.nextLine().trim();
            }

            // Prompt for Customer Type
            System.out.println("  Customer Type:");
            System.out.println("    1. Paying Customer");
            System.out.println("    2. Associate Customer");
            System.out.print("  Select (1 or 2): ");

            int custType = 1;
            try {
                custType = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[INFO] Defaulting to Paying Customer.");
            }

            // Collect current paying customers in case an Associate Customer is selected
            List<PayingCustomer> existingPayingCustomers = new ArrayList<>();
            for (Customer c : magazine.getCustomers()) {
                if (c instanceof PayingCustomer) {
                    existingPayingCustomers.add((PayingCustomer) c);
                }
            }

            Customer newCust = null;

            // Associate Customer Creation
            if (custType == 2) {
                if (existingPayingCustomers.isEmpty()) {
                    System.out.println("[!] No Paying Customers exist to pay for this associate. Automatically switching to Paying Customer.");
                    custType = 1; // Fallback to Paying Customer setup
                } else {
                    System.out.println("  Select the Paying Customer responsible for this Associate:");
                    for (int p = 0; p < existingPayingCustomers.size(); p++) {
                        PayingCustomer payer = existingPayingCustomers.get(p);
                        System.out.println("    " + (p + 1) + ". " + payer.getName() + " (" + payer.getEmail() + ")");
                    }
                    System.out.print("  Select (1-" + existingPayingCustomers.size() + "): ");

                    int payerChoice = 1;
                    try {
                        payerChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (payerChoice < 1 || payerChoice > existingPayingCustomers.size()) {
                            payerChoice = 1;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[INFO] Defaulting to first Paying Customer.");
                    }

                    PayingCustomer selectedPayer = existingPayingCustomers.get(payerChoice - 1);
                    AssociateCustomer assocCust = new AssociateCustomer(custName, custEmail, selectedPayer);
                    selectedPayer.addAssociateCustomer(assocCust);
                    newCust = assocCust;
                }
            }

            // Paying Customer Creation
            if (custType == 1 || newCust == null) {
                System.out.println("  Payment Method Type:");
                System.out.println("    1. Credit Card");
                System.out.println("    2. Bank Account Direct Debit");
                System.out.print("  Select (1 or 2): ");

                int payType = 1;
                try {
                    payType = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("[INFO] Defaulting to Credit Card.");
                }

                PaymentMethod paymentMethod;
                if (payType == 2) {
                    System.out.print("  Enter Bank BSB (e.g., 123-456): ");
                    String bsb = scanner.nextLine().trim();
                    System.out.print("  Enter Bank Account Number: ");
                    String accNum = scanner.nextLine().trim();
                    paymentMethod = new BankAccount(custName, bsb, accNum);
                } else {
                    System.out.print("  Enter Credit Card Number: ");
                    String cardNum = scanner.nextLine().trim();
                    System.out.print("  Enter Card Expiry (MM/YY): ");
                    String expiry = scanner.nextLine().trim();
                    paymentMethod = new CreditCard(custName, cardNum, expiry);
                }

                newCust = new PayingCustomer(custName, custEmail, paymentMethod);
            }

            // Populate interestedSupplements interactively (Applies to both Paying and Associate Customers)
            List<Supplement> availableSupps = magazine.getAvailableSupplements();
            if (!availableSupps.isEmpty()) {
                System.out.println("\n  Select Interested Supplements for " + custName + ":");
                for (int s = 0; s < availableSupps.size(); s++) {
                    System.out.println("    " + (s + 1) + ". " + availableSupps.get(s).getName() 
                                        + " ($" + availableSupps.get(s).getWeeklyCost() + "/wk)");
                }

                boolean addingSupplements = true;
                while (addingSupplements) {
                    System.out.print("  Enter supplement number to add (or 0 when finished): ");
                    try {
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        if (choice > 0 && choice <= availableSupps.size()) {
                            Supplement selected = availableSupps.get(choice - 1);

                            boolean success = newCust.addSupplement(selected);
                            if(success){
                                System.out.println("    [+] Added " + selected.getName());
                            }else{
                                System.out.println("    Supplement already added");
                            }

                        } else if (choice == 0) {
                            addingSupplements = false;
                        } else {
                            System.out.println("    [!] Choice out of range.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("    [!] Please enter a valid number.");
                    }
                }
            }

            boolean success = magazine.addCustomer(newCust);
            if(success){
                System.out.println("\nCustomer (" + newCust.getName() + "(" + newCust.getEmail() + ")) added");
            }else{
                // If newCust was an Associate, unlink them from the PayingCustomer since registration failed
                if (newCust instanceof AssociateCustomer) {
                    AssociateCustomer assoc = (AssociateCustomer) newCust;
                    if (assoc.getPayingCustomer() != null) {
                        assoc.getPayingCustomer().removeAssociateCustomer(assoc);
                    }
                }
                System.out.println("\nCustomer (" + newCust.getName() + " (" + newCust.getEmail() + ")) failed to be added (the email might already exist)");
            }
        }
        System.out.println("\n[SUCCESS] Interactive setup complete.");
    }

    /**
     * Executes Requirement Step C: Iterates over 4 weeks to print weekly notifications for all customers.
     *
     * @param magazine the target Magazine service containing active customers
     */
    private static void runStepC_WeeklyEmails(Magazine magazine){
        System.out.println("\n==================================================");
        System.out.println(" STEP C: PRINTING WEEKLY EMAILS (4 WEEKS)");
        System.out.println("==================================================");

        for (int week = 1; week <= 4; week++) {
            System.out.println("\n==========================================");
            System.out.println("           WEEK " + week + " NOTIFICATIONS");
            System.out.println("==========================================");
            for (Customer c : magazine.getCustomers()) {
                System.out.print(c.generateWeeklyEmail());
            }
        }
    }

    /**
     * Executes Requirement Step D: Prints end-of-month itemized billing emails for paying customers.
     *
     * @param magazine the target Magazine service containing active customers
     */
    private static void runStepD_MonthlyInvoices(Magazine magazine){
        System.out.println("\n==================================================");
        System.out.println(" STEP D: END OF MONTH INVOICES (PAYING CUSTOMERS)");
        System.out.println("==================================================");

        for (Customer c : magazine.getCustomers()) {
            if (c instanceof PayingCustomer) {
                PayingCustomer pc = (PayingCustomer) c;
                System.out.print(pc.generateMonthlyBillingEmail());
            }
        }
    }

    /**
     * Executes Requirement Step E: Demonstrates adding a new customer via interactive Scanner input or hardcoded defaults.
     *
     * @param magazine the target Magazine service instance
     * @param scanner  the active Scanner instance for optional interactive input
     */
    private static void runStepE_AddCustomer(Magazine magazine, Scanner scanner){
        System.out.println("\n==================================================");
        System.out.println(" STEP E: ADD A NEW CUSTOMER TO SERVICE");
        System.out.println("==================================================");
        System.out.println("Choose mode for Step E:");
        System.out.println("  1. Hardcoded Default Customer (Grace Hopper)");
        System.out.println("  2. Interactive Input via Scanner");
        System.out.print("Enter choice (1 or 2): ");

        int mode = 1;
        try {
            mode = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[INFO] Defaulting to Hardcoded Customer Addition.");
        }

        Customer newCustomer = null;

        if (mode == 2) {
            // Interactive Scanner Mode
            System.out.print("\nEnter Customer Name: ");
            String name = scanner.nextLine().trim();
            while (name == null || name.isEmpty()) {
                System.out.println("[INFO] Name cannot be empty. Please enter a valid name.");
                System.out.print("  Name: ");
                name = scanner.nextLine().trim();
            }

            System.out.print("Enter Customer Email: ");
            String email = scanner.nextLine().trim();
            while (email == null || email.isEmpty()) {
                System.out.println("[INFO] Email cannot be empty. Please enter a valid email.");
                System.out.print("  Email: ");
                email = scanner.nextLine().trim();
            }

            System.out.println("Customer Payment Type:");
            System.out.println("  1. Credit Card");
            System.out.println("  2. Bank Account Direct Debit");
            System.out.print("Select choice (1 or 2): ");

            int type = 1;
            try {
                type = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[INFO] Defaulting to Credit Card.");
            }

            if (type == 2) {
                System.out.print("Enter Bank BSB (e.g. 123-456): ");
                String bsb = scanner.nextLine().trim();
                System.out.print("Enter Account Number: ");
                String accNum = scanner.nextLine().trim();

                PaymentMethod ba = new BankAccount(name, bsb, accNum);
                newCustomer = new PayingCustomer(name, email, ba);
            } else {
                System.out.print("Enter Credit Card Number: ");
                String cardNum = scanner.nextLine().trim();
                System.out.print("Enter Expiry Date (MM/YY): ");
                String expiry = scanner.nextLine().trim();

                PaymentMethod cc = new CreditCard(name, cardNum, expiry);
                newCustomer = new PayingCustomer(name, email, cc);
            }

            // Supplement selection
            List<Supplement> available = magazine.getAvailableSupplements();
            if (!available.isEmpty()) {
                System.out.println("\nAvailable Supplements:");
                for (int i = 0; i < available.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + available.get(i).toString());
                }

                boolean addingSupplements = true;
                while (addingSupplements) {
                    System.out.print("  Enter supplement number to add (or 0 when finished): ");
                    try {
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        if (choice > 0 && choice <= available.size()) {
                            Supplement selected = available.get(choice - 1);
                            newCustomer.addSupplement(selected); // Populates interestedSupplements
                            System.out.println("    [+] Added " + selected.getName());
                        } else if (choice == 0) {
                            addingSupplements = false;
                        } else {
                            System.out.println("    [!] Choice out of range.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("    [!] Please enter a valid number.");
                    }
                }

            }
        } else {
            // Hardcoded Mode
            System.out.println("\n[INFO] Adding hardcoded customer: Grace Hopper...");
            PaymentMethod graceCard = new CreditCard("Grace Hopper", "4916000011112222", "05/29");
            newCustomer = new PayingCustomer("Grace Hopper", "grace@example.com", graceCard);

            if (!magazine.getAvailableSupplements().isEmpty()) {
                newCustomer.addSupplement(magazine.getAvailableSupplements().get(0));
            }
        }

        boolean success = magazine.addCustomer(newCustomer);

        System.out.println("\n[VERIFICATION] Adding Status: " + (success ? "SUCCESS" : "FAILED"));
        System.out.println("[VERIFICATION] Total active customers: " + magazine.getCustomers().size());

        if (!success) {
            System.out.println("[ERROR] Failed to add customer (null or duplicate entry).");
            return;
        }

        System.out.println("\nGenerated Initial Weekly Email for New Customer:");
        System.out.print(newCustomer.generateWeeklyEmail());

        System.out.println("\nRemaining Active Customers:");
        List<Customer> remaining = magazine.getCustomers();
        for (int i = 0; i < remaining.size(); i++) {
            Customer c = remaining.get(i);
            String type = (c instanceof PayingCustomer) ? "Paying Customer" : "Associate Customer";
            System.out.println("  " + (i + 1) + ". " + c.getName() + " (" + c.getEmail() + ") - " + type);
        }
    }

    /**
     * Executes Requirement Step F: Demonstrates customer removal via interactive Scanner input or hardcoded selection.
     *
     * @param magazine the target Magazine service instance
     * @param scanner  the active Scanner instance for optional interactive input
     */
    private static void runStepF_RemoveCustomer(Magazine magazine, Scanner scanner){
        System.out.println("\n==================================================");
        System.out.println(" STEP F: REMOVE AN EXISTING CUSTOMER");
        System.out.println("==================================================");

        List<Customer> customerList = magazine.getCustomers();
        if (customerList.isEmpty()) {
            System.out.println("[INFO] No customers available to remove.");
            return;
        }

        System.out.println("Choose mode for Step F:");
        System.out.println("  1. Hardcoded Removal (Remove First Customer in List)");
        System.out.println("  2. Interactive Input via Scanner");
        System.out.print("Enter choice (1 or 2): ");

        int mode = 1;
        try {
            mode = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[INFO] Defaulting to Hardcoded Removal.");
        }

        Customer target = null;

        if (mode == 2) {
            // Interactive Mode
            System.out.println("\nCurrent Active Customers:");
            for (int i = 0; i < customerList.size(); i++) {
                Customer c = customerList.get(i);
                String typeStr = (c instanceof PayingCustomer) ? "Paying Customer" : "Associate Customer";
                System.out.println("  " + (i + 1) + ". " + c.getName() + " (" + c.getEmail() + ") - " + typeStr);
            }

            System.out.print("\nEnter customer number to remove (1-" + customerList.size() + "): ");
            int removeIndex = -1;
            try {
                removeIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid number format.");
                return;
            }

            if (removeIndex >= 0 && removeIndex < customerList.size()) {
                target = customerList.get(removeIndex);
            } else {
                System.out.println("[ERROR] Selected index is out of bounds. Removal aborted.");
                return;
            }
        } else {
            // Hardcoded Mode
            target = customerList.get(0);
        }

        System.out.println("\nRemoving Customer: " + target.getName() + " (" + target.getEmail() + ")...");
        boolean success = magazine.removeCustomer(target);

        System.out.println("[VERIFICATION] Removal Status: " + (success ? "SUCCESS" : "FAILED"));
        System.out.println("[VERIFICATION] Total remaining active customers: " + magazine.getCustomers().size());

        System.out.println("\nRemaining Active Customers:");
        List<Customer> remaining = magazine.getCustomers();
        for (int i = 0; i < remaining.size(); i++) {
            Customer c = remaining.get(i);
            String type = (c instanceof PayingCustomer) ? "Paying Customer" : "Associate Customer";
            System.out.println("  " + (i + 1) + ". " + c.getName() + " (" + c.getEmail() + ") - " + type);
        }
    }

}