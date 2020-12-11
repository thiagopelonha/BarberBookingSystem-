
package view;

import Controller.LoginController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
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
 * Screen for login
 * @author Jessica Lopes and Thiago Teixeira
 */
public class LoginScreen extends JFrame {
    
    private final LoginController controller;
    private JTextField emailTextField;
    private JPasswordField passField;
    private JRadioButton customer;
    private JRadioButton barber; 
    
    public LoginScreen(LoginController controller) {
        
        this.controller = controller;
        
        // We encapsulated the building process of the window
        attributesSetter();
        components();
        validation();
    }
    
    private void attributesSetter() {

        this.setVisible(true);
        this.setSize(800,600);
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

        // checkbox customer or barber
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
        
        // Email     
        JPanel emailPanel = new JPanel();
        FlowLayout emailLayout = new FlowLayout();
        emailLayout.setAlignment(FlowLayout.CENTER);
        emailPanel.setLayout(emailLayout);
        
        JLabel emailLabel = new JLabel();
        emailLabel.setText("Email:          ");
        emailTextField = new JTextField(15);
        emailPanel.add(emailLabel);
        emailPanel.add(emailTextField);

        // Password
        JPanel passPanel = new JPanel();
        FlowLayout passLayout = new FlowLayout();
        passLayout.setAlignment(FlowLayout.CENTER);
        passPanel.setLayout(passLayout);
        
        JLabel passLabel = new JLabel();
        passLabel.setText("Password: ");
        passField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Button
        JPanel buttonPanel = new JPanel();
        FlowLayout buttonLayout = new FlowLayout();
        buttonLayout.setAlignment(FlowLayout.CENTER);
        JButton loginButton = new JButton("Log in");
        loginButton.setActionCommand("login");
        loginButton.addActionListener(controller);
        
        buttonPanel.add(loginButton);

        // Set border for the panel
        centerPanel.setBorder(new EmptyBorder(new Insets(100, 200, 150, 200)));
        centerPanel.add(bgPanel);
        centerPanel.add(emailPanel);
        centerPanel.add(passPanel);

        // space between fields and button
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(buttonPanel);
                
        // register section
        JPanel loginPanel = new JPanel();
        FlowLayout loginLayout = new FlowLayout();
        loginLayout.setAlignment(FlowLayout.CENTER);
        JLabel hyperlink = new JLabel("If you are not registered, sign up here");
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

    // get email from screen
    public String getEmail() {
        return emailTextField.getText();
    }

    // get password from screen
    public String getPassword() {
        return new String(passField.getPassword());
    }

    // which radiobutton is checked
    public boolean isCustomer() {
        return customer.isSelected();
    }

    public boolean isBarber() {
        return barber.isSelected();
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
