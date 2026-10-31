package magazineservice;

import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Enhanced Controller for the Main Dashboard GUI, managing View, Create, and Edit modes,
 * list views, information panel interactions, multithreaded billing history tasks, 
 * address handling, and flexible payment methods.
 * 
 * @author Yun Shiang
 * @version 2.1
 */
public class MainDashboardController {

    private Magazine magazine;
    private Stage stage;

    @FXML private TabPane modeTabPane;
    
    // View Mode Components
    @FXML private ListView<Supplement> viewSupplementListView;
    @FXML private ListView<Customer> viewCustomerListView;
    @FXML private TextArea infoPanelTextArea;
    @FXML private Button loadBillingHistoryButton;

    // Create Mode Components
    @FXML private TextField createMagazineCostField;
    @FXML private TextField createSupplementNameField;
    @FXML private TextField createSupplementCostField;
    @FXML private TextField createCustomerNameField;
    @FXML private TextField createCustomerEmailField;
    
    // Address fields for Create Mode
    @FXML private TextField createStreetNumberField;
    @FXML private TextField createStreetNameField;
    @FXML private TextField createSuburbField;
    @FXML private TextField createPostcodeField;

    @FXML private ComboBox<String> createCustomerTypeCombo;
    @FXML private ComboBox<PayingCustomer> createPayingCustomerCombo;
    @FXML private ListView<Supplement> createSupplementSelectList;

    // Payment Method fields for Create Mode
    @FXML private ComboBox<String> createPaymentTypeCombo;
    @FXML private TextField createPaymentHolderField;
    @FXML private TextField createCardNumberField;
    @FXML private TextField createCardExpiryField;
    @FXML private TextField createBsbField;
    @FXML private TextField createBankAccountField;

    // Edit Mode Components
    @FXML private ListView<Customer> editCustomerListView;
    @FXML private ListView<Supplement> editSupplementListView;
    @FXML private TextField editCustomerNameField;
    @FXML private TextField editCustomerEmailField;
    @FXML private TextField editSupplementNameField;
    @FXML private TextField editSupplementCostField;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
        refreshAllLists();
    }

    @FXML
    public void initialize() {
        if (magazine == null) {
            magazine = new Magazine(10.00);
        }

        // Setup View Mode Listeners
        if (viewSupplementListView != null) {
            viewSupplementListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedSupplement) -> {
                if (selectedSupplement != null) {
                    displaySupplementInfo(selectedSupplement);
                }
            });
        }

        if (viewCustomerListView != null) {
            viewCustomerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedCustomer) -> {
                if (selectedCustomer != null) {
                    displayCustomerInfo(selectedCustomer);
                }
            });
        }

        // Setup Create Mode Combos & Multi-selection list
        if (createCustomerTypeCombo != null) {
            createCustomerTypeCombo.getItems().addAll("Paying Customer", "Associate Customer");
            createCustomerTypeCombo.getSelectionModel().selectFirst();
            createCustomerTypeCombo.setOnAction(e -> updateCreatePayerDropdown());
        }

        if (createPaymentTypeCombo != null) {
            createPaymentTypeCombo.getItems().addAll("Credit Card", "Bank Account");
            createPaymentTypeCombo.getSelectionModel().selectFirst();
        }

        if (createSupplementSelectList != null) {
            createSupplementSelectList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        // Setup Edit Mode Selection Listeners to populate fields
        if (editCustomerListView != null) {
            editCustomerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, cust) -> {
                if (cust != null) {
                    editCustomerNameField.setText(cust.getName());
                    editCustomerEmailField.setText(cust.getEmail());
                }
            });
        }

        if (editSupplementListView != null) {
            editSupplementListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, supp) -> {
                if (supp != null) {
                    editSupplementNameField.setText(supp.getName());
                    editSupplementCostField.setText(String.valueOf(supp.getWeeklyCost()));
                }
            });
        }
    }

    private void refreshAllLists() {
        if (magazine == null) return;

        if (viewSupplementListView != null) viewSupplementListView.getItems().setAll(magazine.getAvailableSupplements());
        if (viewCustomerListView != null) viewCustomerListView.getItems().setAll(magazine.getCustomers());
        if (editCustomerListView != null) editCustomerListView.getItems().setAll(magazine.getCustomers());
        if (editSupplementListView != null) editSupplementListView.getItems().setAll(magazine.getAvailableSupplements());
        if (createSupplementSelectList != null) createSupplementSelectList.getItems().setAll(magazine.getAvailableSupplements());
        
        updateCreatePayerDropdown();
    }

    private void updateCreatePayerDropdown() {
        if (createPayingCustomerCombo == null || magazine == null) return;
        createPayingCustomerCombo.getItems().clear();
        for (Customer c : magazine.getCustomers()) {
            if (c instanceof PayingCustomer) {
                createPayingCustomerCombo.getItems().add((PayingCustomer) c);
            }
        }
    }

    private void displaySupplementInfo(Supplement supplement) {
        if (infoPanelTextArea == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("=== SUPPLEMENT INFORMATION ===\n");
        sb.append("Name: ").append(supplement.getName()).append("\n");
        sb.append("Weekly Cost: $").append(String.format("%.2f", supplement.getWeeklyCost())).append("\n\n");
        sb.append("Subscribed Customers:\n");
        
        boolean found = false;
        for (Customer c : magazine.getCustomers()) {
            if (c.getInterestedSupplements().contains(supplement)) {
                sb.append(" - ").append(c.getName()).append(" (").append(c.getEmail()).append(")\n");
                found = true;
            }
        }
        if (!found) {
            sb.append(" - None\n");
        }
        infoPanelTextArea.setText(sb.toString());
        if (loadBillingHistoryButton != null) loadBillingHistoryButton.setVisible(false);
    }

    private void displayCustomerInfo(Customer customer) {
        if (infoPanelTextArea == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("=== CUSTOMER INFORMATION ===\n");
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Email: ").append(customer.getEmail()).append("\n");
        
        if (customer instanceof PayingCustomer) {
            PayingCustomer pc = (PayingCustomer) customer;
            sb.append("Status: Paying Customer\n");
            sb.append("Payment Method: ").append(pc.getPaymentMethod().getPaymentDetails()).append("\n");
            sb.append("Associate Customers Sponsored:\n");
            if (pc.getAssociateCustomers().isEmpty()) {
                sb.append(" - None\n");
            } else {
                for (AssociateCustomer assoc : pc.getAssociateCustomers()) {
                    sb.append(" * ").append(assoc.getName()).append(" (").append(assoc.getEmail()).append(")\n");
                }
            }
            if (loadBillingHistoryButton != null) {
                loadBillingHistoryButton.setVisible(true);
                loadBillingHistoryButton.setUserData(pc);
            }
        } else if (customer instanceof AssociateCustomer) {
            AssociateCustomer ac = (AssociateCustomer) customer;
            sb.append("Status: Associate Customer\n");
            String payerName = (ac.getPayingCustomer() != null) ? ac.getPayingCustomer().getName() : "None";
            sb.append("Paid by Payer: ").append(payerName).append("\n");
            if (loadBillingHistoryButton != null) loadBillingHistoryButton.setVisible(false);
        }

        sb.append("\nSubscribed Supplements:\n");
        if (customer.getInterestedSupplements().isEmpty()) {
            sb.append(" - None\n");
        } else {
            for (Supplement s : customer.getInterestedSupplements()) {
                sb.append(" - ").append(s.getName()).append(" ($").append(s.getWeeklyCost()).append("/wk)\n");
            }
        }

        infoPanelTextArea.setText(sb.toString());
    }

    @FXML
    public void handleLoadBillingHistory() {
        if (loadBillingHistoryButton == null || infoPanelTextArea == null) return;
        Object data = loadBillingHistoryButton.getUserData();
        if (data instanceof PayingCustomer) {
            PayingCustomer pc = (PayingCustomer) data;
            infoPanelTextArea.setText("Generating billing history asynchronously...");

            BillingHistoryTask task = new BillingHistoryTask(pc, magazine.getWeeklyBaseCost());
            task.stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    infoPanelTextArea.setText(task.getValue());
                } else if (newState == Worker.State.FAILED) {
                    infoPanelTextArea.setText("Error generating billing history.");
                }
            });

            new Thread(task).start();
        }
    }

    @FXML
    public void handleSave() {
        if (stage != null && magazine != null) {
            FileHandler.saveMagazine(stage, magazine);
        }
    }

    @FXML
    public void handleLoad() {
        if (stage != null) {
            Magazine loaded = FileHandler.loadMagazine(stage);
            if (loaded != null) {
                this.magazine = loaded;
                refreshAllLists();
                infoPanelTextArea.setText("[SUCCESS] Loaded magazine data successfully.");
            }
        }
    }

    @FXML
    public void handleAddSupplement() {
        try {
            String name = createSupplementNameField.getText().trim();
            double cost = Double.parseDouble(createSupplementCostField.getText().trim());
            if (name.isEmpty()) {
                infoPanelTextArea.setText("Name cannot be empty.");
                return;
            }
            if (!name.isEmpty() && cost > 0) {
                Supplement s = new Supplement(name, cost);
                magazine.addSupplement(s);
                refreshAllLists();
                createSupplementNameField.clear();
                createSupplementCostField.clear();
                infoPanelTextArea.setText("Supplement added: " + name);
            } else if (cost <= 0) {
                infoPanelTextArea.setText("Cost must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            infoPanelTextArea.setText("Invalid cost format for supplement.");
        }
    }

    @FXML
    public void handleAddCustomer() {
        try {
            String name = createCustomerNameField.getText().trim();
            String email = createCustomerEmailField.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                infoPanelTextArea.setText("Name and email cannot be empty.");
                return;
            }

            Customer newCust;
            String type = createCustomerTypeCombo.getValue();
            
            if ("Associate Customer".equals(type)) {
                PayingCustomer payer = createPayingCustomerCombo.getValue();
                if (payer == null) {
                    infoPanelTextArea.setText("Please select a paying customer for this associate.");
                    return;
                }
                AssociateCustomer ac = new AssociateCustomer(name, email, payer);
                payer.addAssociateCustomer(ac);
                newCust = ac;
            } else {
                // Flexible Payment Method Handling (Credit Card vs Bank Account)
                PaymentMethod pm;
                String payType = createPaymentTypeCombo.getValue();
                String holder = createPaymentHolderField != null && !createPaymentHolderField.getText().isEmpty() 
                                ? createPaymentHolderField.getText().trim() : name;

                if ("Bank Account".equals(payType)) {
                    String bsb = createBsbField != null ? createBsbField.getText().trim() : "";
                    String accNum = createBankAccountField != null ? createBankAccountField.getText().trim() : "";
                    pm = new BankAccount(holder, bsb, accNum);
                } else {
                    String cardNum = createCardNumberField != null ? createCardNumberField.getText().trim() : "4111222233334444";
                    String expiry = createCardExpiryField != null ? createCardExpiryField.getText().trim() : "12/28";
                    pm = new CreditCard(holder, cardNum, expiry);
                }
                newCust = new PayingCustomer(name, email, pm);
            }

            // Attach Selected Supplements from List View
            if (createSupplementSelectList != null) {
                List<Supplement> selectedSupps = createSupplementSelectList.getSelectionModel().getSelectedItems();
                for (Supplement s : selectedSupps) {
                    newCust.addSupplement(s);
                }
            }

            magazine.addCustomer(newCust);
            refreshAllLists();
            
            // Clear fields
            createCustomerNameField.clear();
            createCustomerEmailField.clear();
            if (createStreetNumberField != null) createStreetNumberField.clear();
            if (createStreetNameField != null) createStreetNameField.clear();
            if (createSuburbField != null) createSuburbField.clear();
            if (createPostcodeField != null) createPostcodeField.clear();

            infoPanelTextArea.setText("Customer added successfully: " + name);
        } catch (Exception e) {
            infoPanelTextArea.setText("Error adding customer: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteCustomer() {
        Customer selected = editCustomerListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            magazine.removeCustomer(selected);
            refreshAllLists();
            infoPanelTextArea.setText("Customer removed: " + selected.getName());
        } else {
            infoPanelTextArea.setText("Please select a customer to delete from the Edit list.");
        }
    }

    @FXML
    public void handleDeleteSupplement() {
        Supplement selected = editSupplementListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            magazine.removeSupplement(selected);
            refreshAllLists();
            infoPanelTextArea.setText("Supplement removed: " + selected.getName());
        } else {
            infoPanelTextArea.setText("Please select a supplement to delete from the Edit list.");
        }
    }

    @FXML
    public void handleUpdateCustomer() {
        Customer selected = editCustomerListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String newName = editCustomerNameField.getText().trim();
            String newEmail = editCustomerEmailField.getText().trim();
            if (!newName.isEmpty() && !newEmail.isEmpty()) {
                // Since email is identifier, update via setters
                // 1. Email Uniqueness Check
                boolean emailExists = false;
                for (Customer c : magazine.getCustomers()) {
                    // Check if the input email matches an existing customer's email
                    if (c.getEmail().equalsIgnoreCase(newEmail)) {
                        // If it matches, make sure it isn't the customer we are currently updating
                        if (c != (selected)) {
                            emailExists = true;
                            break;
                        }
                    }
                }
                
                if (emailExists) {
                    infoPanelTextArea.setText("Error: The email '" + newEmail + "' is already in use by another customer.");
                    return; // Stop the execution immediately
                }
                
                // 2. Apply updates via the new setters if unique
                selected.setName(newName);
                selected.setEmail(newEmail);
            
                refreshAllLists();
                infoPanelTextArea.setText("Customer updated: " + newName);
            } else {
                infoPanelTextArea.setText("Name and email cannot be blank.");
            }
        } else {
            infoPanelTextArea.setText("Please select a customer to update.");
        }
    }

    @FXML
    public void handleUpdateSupplement() {
        Supplement selected = editSupplementListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String newName = editSupplementNameField.getText().trim();
                double newCost = Double.parseDouble(editSupplementCostField.getText().trim());
                if (!newName.isEmpty() && newCost > 0) {

                    // 1. Supplement Name Uniqueness Check
                    boolean nameExists = false;
                    for (Supplement s : magazine.getAvailableSupplements()) {
                        // Check if the input name matches an existing supplement's name
                        if (s.getName().equalsIgnoreCase(newName)) {
                            // If it matches, make sure it isn't the supplement we are currently updating
                            if (s != (selected)) {
                                nameExists = true;
                                break;
                            }
                        }
                    }
                    
                    if (nameExists) {
                        infoPanelTextArea.setText("Error: The supplement '" + newName + "' already exists in the catalog.");
                        return; // Stop the execution immediately
                    }

                    selected.setName(newName);
                    selected.setWeeklyCost(newCost);
                    refreshAllLists();
                    infoPanelTextArea.setText("Supplement updated successfully.");
                } else if (newCost <= 0) {
                    infoPanelTextArea.setText("Cost must be greater than zero.");
                }
            } catch (NumberFormatException e) {
                infoPanelTextArea.setText("Invalid weekly cost format.");
            }
        } else {
            infoPanelTextArea.setText("Please select a supplement to update.");
        }
    }
}