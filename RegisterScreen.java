package view;

import Controller.RegisterController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Screen for registration
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class RegisterScreen extends JFrame {

    private final RegisterController controller;
    private JPanel locationPanel;
    private JTextField locationField;
    private JRadioButton customer;
    private JRadioButton barber;
    private JTextField emailTextField;
    private JPasswordField passField;
    private JPasswordField confPassField;
    private JTextField fNameField;
    private JTextField sNameField;
    private JTextField phoneNumberField;

    public RegisterScreen(RegisterController controller) {

        this.controller = controller;

        // We encapsulated the building process of the window
        attributesSetter();
        components();
        validation();
    }

    private void attributesSetter() {

        this.setVisible(true);
        this.setSize(800, 600);
        this.setTitle("Barber 2Friends");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

    }

    private void components() {

        //main layout
        BorderLayout frameLayout = new BorderLayout();
        this.setLayout(frameLayout);

        // panel with grid layout 2 rows and 1 column
        JPanel topPanel = new JPanel();
        BoxLayout topLayout = new BoxLayout(topPanel, BoxLayout.Y_AXIS);
        topPanel.setLayout(topLayout);

        // Labels
        JPanel titlePanel = new JPanel();
        FlowLayout titleLayout = new FlowLayout();
        titleLayout.setAlignment(FlowLayout.CENTER);
        titlePanel.setLayout(titleLayout);
        JLabel titleLabel = new JLabel("");

        Font labelFont = titleLabel.getFont();

        titleLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 20));
        titlePanel.add(titleLabel);

        // Image
        JPanel imagePanel = new JPanel();
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/b2friends_50.jpeg")));
        imagePanel.add(imgLabel);

        topPanel.add(titlePanel);
        topPanel.add(imagePanel);

        // User register Panel
        // center panel
        JPanel centerPanel = new JPanel();
        BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(centerLayout);

        // ButtonGroup customer or group 
        JPanel bgPanel = new JPanel();
        FlowLayout bgLayout = new FlowLayout();
        bgLayout.setAlignment(FlowLayout.CENTER);
        ButtonGroup user = new ButtonGroup();
        customer = new JRadioButton();
        barber = new JRadioButton();
        customer.setText("CUSTOMER");
        customer.setSelected(true);
        barber.setText("BARBER");
        user.add(customer);
        user.add(barber);

        bgPanel.add(customer);
        bgPanel.add(barber);

        // customer is selected
        customer.addActionListener((ActionEvent e) -> {
            locationField.setText("");
            locationPanel.setVisible(false);
        });

        // customer is selected
        barber.addActionListener((ActionEvent e) -> {
            locationPanel.setVisible(true);
        });

        // Email     
        JPanel emailPanel = new JPanel();
        FlowLayout emailLayout = new FlowLayout();
        emailLayout.setAlignment(FlowLayout.CENTER);
        emailPanel.setLayout(emailLayout);

        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email:                          ");
        emailTextField = new JTextField(15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailTextField);

        // Password
        JPanel passPanel = new JPanel();
        FlowLayout passLayout = new FlowLayout();
        passLayout.setAlignment(FlowLayout.CENTER);
        passPanel.setLayout(passLayout);

        JLabel passLabel = new JLabel();
        passLabel.setText("Password:                 ");
        passField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Confirm Password
        JPanel confPassPanel = new JPanel();
        FlowLayout confPassLayout = new FlowLayout();
        confPassLayout.setAlignment(FlowLayout.CENTER);
        confPassPanel.setLayout(confPassLayout);

        JLabel confPassLabel = new JLabel();
        confPassLabel.setText("Confirm Password: ");
        confPassField = new JPasswordField(15);
        confPassPanel.add(confPassLabel);
        confPassPanel.add(confPassField);

        // First Name
        JPanel fNamePanel = new JPanel();
        FlowLayout fNameLayout = new FlowLayout();
        fNameLayout.setAlignment(FlowLayout.CENTER);
        fNamePanel.setLayout(fNameLayout);

        JLabel fNameLabel = new JLabel();
        fNameLabel.setText("First Name:                ");
        fNameField = new JTextField(15);
        fNamePanel.add(fNameLabel);
        fNamePanel.add(fNameField);

        // Second Name
        JPanel sNamePanel = new JPanel();
        FlowLayout sNameLayout = new FlowLayout();
        sNameLayout.setAlignment(FlowLayout.CENTER);
        sNamePanel.setLayout(sNameLayout);

        JLabel sNameLabel = new JLabel();
        sNameLabel.setText("Last Name:                ");
        sNameField = new JTextField(15);
        sNamePanel.add(sNameLabel);
        sNamePanel.add(sNameField);

        // Phone Number
        JPanel phoneNumberPanel = new JPanel();
        FlowLayout phoneNumberLayout = new FlowLayout();
        phoneNumberLayout.setAlignment(FlowLayout.CENTER);
        phoneNumberPanel.setLayout(phoneNumberLayout);

        JLabel phoneNumberLabel = new JLabel();
        phoneNumberLabel.setText("Phone Number:         ");
        phoneNumberField = new JTextField(15);
        phoneNumberPanel.add(phoneNumberLabel);
        phoneNumberPanel.add(phoneNumberField);

        // Location
        locationPanel = new JPanel();
        FlowLayout locationLayout = new FlowLayout();
        locationLayout.setAlignment(FlowLayout.CENTER);
        locationPanel.setLayout(locationLayout);

        JLabel locationLabel = new JLabel();
        locationLabel.setText("Location:                     ");
        locationField = new JTextField(15);
        locationPanel.add(locationLabel);
        locationPanel.add(locationField);

        // Button
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout();
        buttonLayout.setAlignment(FlowLayout.CENTER);
        JButton registerButton = new JButton("Register");
        registerButton.setActionCommand("register");
        registerButton.addActionListener(controller);

        // Set border for the panel
        centerPanel.setBorder(new EmptyBorder(new Insets(10, 200, 150, 200)));
        buttonPanel.add(registerButton);
        // center panel
        centerPanel.add(bgPanel);
        centerPanel.add(emailPanel);
        centerPanel.add(passPanel);
        centerPanel.add(confPassPanel);
        centerPanel.add(fNamePanel);
        centerPanel.add(sNamePanel);
        centerPanel.add(phoneNumberPanel);
        locationPanel.setVisible(false);
        centerPanel.add(locationPanel);
        // space between fields and button
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(buttonPanel);

        // register section
        JPanel loginPanel = new JPanel();
        FlowLayout loginLayout = new FlowLayout();
        loginLayout.setAlignment(FlowLayout.CENTER);
        JLabel hyperlink = new JLabel("If you are already registered, log in here!");
        hyperlink.addMouseListener(controller);
        loginPanel.add(hyperlink);

        this.add(topPanel, BorderLayout.PAGE_START);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(loginPanel, BorderLayout.PAGE_END);

    }

    private void validation() {
        this.validate();
        this.repaint();
    }

    // get location from screen
    public String getLocationText() {
        return locationField.getText();
    }

    // which radiobutton is checked
    public boolean isCustomer() {
        return customer.isSelected();
    }

    public boolean isBarber() {
        return barber.isSelected();
    }

    // get email from screen
    public String getEmail() {
        return emailTextField.getText();
    }

    public String getPassword() {
        return new String(passField.getPassword());
    }

    // get confirmatioon password from the screen
    public String getConfPassword() {
        return new String(confPassField.getPassword());
    }

    // get name from screen
    public String getfName() {
        return fNameField.getText();
    }

    public String getsName() {
        return sNameField.getText();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    // show a message on the screen
    public void showErrorMsg(String message) {

        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.ERROR_MESSAGE);

    }

    public void showSuccessMsg(String message) {

        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.PLAIN_MESSAGE);

    }

    public void showInfoMsg(String message) {

        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.INFORMATION_MESSAGE);

    }

}
