package view;

import Controller.CustomerHomeScreenController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

/**
 * Screen for customers
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class CustomerHomeScreen extends JFrame {

    private CustomerHomeScreenController controller;
    private final Object[] columnNamesBarber = {"Email", "Barber", "Phone Number", "Location"};
    private final Object[] columnNamesApp = {"ID App", "Barber", "Phone Number", "Date - Slot", "status", "location"};
    private JTextField nameText;
    private JTextField locationText;
    private JTextField dateTextField;
    private JComboBox slots;
    private JTable tableApp;
    private JTable tableBarbers;
    private String complaintText;
    private String previousDate;
    private TableModel tableModelApp;

    public CustomerHomeScreen(CustomerHomeScreenController controller, Object[][] data) {

        this.controller = controller;
        tableModelApp = new TableModel(data, columnNamesApp);
        tableApp = new JTable(tableModelApp);
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

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem itemLogout = new JMenuItem("Log out");
        itemLogout.setActionCommand("logout");
        itemLogout.addActionListener(controller);
        menu.add(itemLogout);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);

        // panel with grid layout 2 rows and 1 column - top panel to inserted at PAGE_START
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
        imgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/b2friends_80.jpeg")));
        imagePanel.add(imgLabel);

        topPanel.add(titlePanel);
        topPanel.add(imagePanel);
        //----------------------------------------------------------------------
        // center panel 
        JPanel centerPanel = new JPanel();
        BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(centerLayout);

        //-------------------------------------------------
        // first Panel - searching
        JPanel firstPanel = new JPanel();
        GridLayout firstLayout = new GridLayout(1, 2);
        firstPanel.setLayout(firstLayout);

        // buttons to search by name or location
        JPanel searchPanel = new JPanel();
        FlowLayout searchLayout = new FlowLayout();
        searchPanel.setLayout(searchLayout);

        JPanel searchPanel2 = new JPanel();
        BoxLayout searchLayout2 = new BoxLayout(searchPanel2, BoxLayout.Y_AXIS);
        searchPanel2.setLayout(searchLayout2);
        searchPanel.add(searchPanel2);

        // name panel
        JPanel namePanel = new JPanel();
        FlowLayout nameLayout = new FlowLayout();
        namePanel.setLayout(nameLayout);
        JLabel nameLabel = new JLabel("Name:      ");
        nameText = new JTextField(20);
        namePanel.add(nameLabel);
        namePanel.add(nameText);

        // location panel
        JPanel locationPanel = new JPanel();
        FlowLayout locationLayout = new FlowLayout();
        locationPanel.setLayout(locationLayout);
        JLabel locationLabel = new JLabel("Location: ");
        locationText = new JTextField(20);
        locationPanel.add(locationLabel);
        locationPanel.add(locationText);

        // Button
        JButton search = new JButton("Search");
        search.setActionCommand("search");
        search.addActionListener(controller);

        // Button        
        searchPanel2.add(namePanel);
        searchPanel2.add(locationPanel);
        searchPanel2.add(search);

        // Barbers Table
        Object[][] dataBarber = null;
        TableModel tableModelBarber = new TableModel(dataBarber, columnNamesBarber);

        tableBarbers = new JTable(tableModelBarber);
        JScrollPane scrollPanelBarber = new JScrollPane(tableBarbers);
        tableBarbers.setSelectionModel(new ForcedListSelectionModel());
        TableColumn colTodelete = tableBarbers.getColumnModel().getColumn(0);
        tableBarbers.removeColumn(colTodelete);

        // add panels to center panel
        firstPanel.add(searchPanel);
        firstPanel.add(scrollPanelBarber);
        firstPanel.setBorder(BorderFactory.createTitledBorder("Search Barbers"));

        //-------------------------------------------------------------
        // second panel
        JPanel secondPanel = new JPanel();
        BoxLayout secondLayout = new BoxLayout(secondPanel, BoxLayout.Y_AXIS);
        secondPanel.setLayout(secondLayout);

        // first line
        JPanel firstLinePanel = new JPanel();
        FlowLayout firstLineLayout = new FlowLayout();
        firstLineLayout.setAlignment(FlowLayout.CENTER);
        firstLinePanel.setLayout(firstLineLayout);
        dateTextField = new JTextField(6);
        dateTextField.setEditable(false);
        // create button to open date picker frame
        JButton pickerDateButton = new JButton("Pick a date");
        // event produced by the user when click on pick a date button
        pickerDateButton.addActionListener((ActionEvent arg0) -> {
            //create frame new object  f
            final JFrame datePicker = new JFrame();
            String dateAux = new DatePicker(datePicker).setPickedDate();

            if (dateAux.equals("")) {
                return;
            }

            if (getBarberSelected() == null) {

                showInfoMsg("A barber must be selected before choosing a date");
                return;
            }

            // format date
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("d-MM-yyyy");
            LocalDate date = LocalDate.parse(dateAux, formatterDate);

            if (date.compareTo(LocalDate.now()) < 0) { // compare if the date selected is before today

                showInfoMsg("The selected date is before today");
                slots.removeAllItems();
                return;

            }
            // the user select a new value, different from previous one
            if (!dateAux.equals(previousDate)) {
                //set text which is collected by date picker i.e. set date
                dateTextField.setText(dateAux);
                previousDate = getDate();
                slots.removeAllItems();
                // get slots from controller to show the new slots for the selected date 
                String[] freeSlots = controller.getAvailableSlots();
                for (String s : freeSlots) {

                    slots.addItem(s);

                }
            }

        });

        // list with the available slots 
        String slotValues[] = {};
        JLabel slotLabel = new JLabel("select a slot: ");
        slots = new JComboBox<>(slotValues);

        // button make appointment
        JButton makeAppButton = new JButton("Make an appointment");
        makeAppButton.setActionCommand("makeAppointment");
        makeAppButton.addActionListener(controller);

        firstLinePanel.add(dateTextField);
        firstLinePanel.add(pickerDateButton);
        firstLinePanel.add(slotLabel);
        firstLinePanel.add(slots);

        secondPanel.add(firstLinePanel);
        secondPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        secondPanel.add(makeAppButton);
        secondPanel.setBorder(BorderFactory.createTitledBorder("Make Appointment"));

        // third panel - table with my appointments
        JPanel thirdPanel = new JPanel();
        BoxLayout thirdLayout = new BoxLayout(thirdPanel, BoxLayout.Y_AXIS);
        thirdPanel.setLayout(thirdLayout);

        JScrollPane scrollPanelApp = new JScrollPane(tableApp);
        tableApp.setSelectionModel(new ForcedListSelectionModel());

        // line below table - buttons to send a complaint and cancel status
        JPanel functionsPanel = new JPanel();
        FlowLayout functionsLayout = new FlowLayout();
        functionsLayout.setAlignment(FlowLayout.CENTER);
        functionsPanel.setLayout(functionsLayout);
        // combox box status
        // list with the available slots 
        JButton refresh = new JButton("Refresh Table");
        refresh.setActionCommand("refresh");
        refresh.addActionListener(controller);
        // buttons change status and send complaint
        JButton changeStatus = new JButton("Cancelar App.");
        changeStatus.setActionCommand("cancelar");
        changeStatus.addActionListener(controller);
        JButton complaint = new JButton("Send complaint");
        complaint.setActionCommand("sendComplain");
        complaint.addActionListener(controller);

        secondPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        functionsPanel.add(refresh);
        functionsPanel.add(changeStatus);
        functionsPanel.add(complaint);

        thirdPanel.add(scrollPanelApp);
        thirdPanel.add(functionsPanel);
        thirdPanel.setBorder(BorderFactory.createTitledBorder("My Appointments"));

        // center panel
        centerPanel.add(firstPanel);
        centerPanel.add(secondPanel);
        centerPanel.add(thirdPanel);

        this.add(topPanel, BorderLayout.PAGE_START);
        this.add(centerPanel, BorderLayout.CENTER);

    }

    private void validation() {

        this.validate();
        this.repaint();

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

    // get name from text field to search by name
    public String getfName() {
        return nameText.getText();
    }

    // get location to search by location
    public String getLocationText() {
        return locationText.getText();
    }

    // get date selected
    public String getDate() {
        return dateTextField.getText();
    }

    // get slots selected or time
    public String getSlot() {
        if (this.slots.getSelectedItem() != null) {
            return this.slots.getSelectedItem().toString();
        } else {
            return "";
        }
    }

    // set barbers on the screen
    public void setDataBarbers(Object[][] data) {

        TableModel tableModelBarber = new TableModel(data, columnNamesBarber);
        tableBarbers.setModel(tableModelBarber);
        // hide first colum which has the email, we need it because is the key of every barber
        tableBarbers.getColumnModel().getColumn(0).setMinWidth(0);
        tableBarbers.getColumnModel().getColumn(0).setMaxWidth(0);

    }

    // set appointments
    public void setDataApps(Object[][] data) {

        tableModelApp = new TableModel(data, columnNamesApp);
        tableApp.setModel(tableModelApp);

    }

    // add one appointment to the table on the screen
    public void addAppRow(Object[] rowData) {

        tableModelApp.addRow(rowData);

    }

    // get size of the table with appointments
    public int getLengthRows() {

        return tableModelApp.getRowCount();

    }

    // get details from selected barber
    public String[] getBarberSelected() {

        if (tableBarbers.getSelectedRow() >= 0) {

            String[] barber = new String[4];
            barber[0] = tableBarbers.getValueAt(tableBarbers.getSelectedRow(), 0).toString();
            barber[1] = tableBarbers.getValueAt(tableBarbers.getSelectedRow(), 1).toString();
            barber[2] = tableBarbers.getValueAt(tableBarbers.getSelectedRow(), 2).toString();
            barber[3] = tableBarbers.getValueAt(tableBarbers.getSelectedRow(), 3).toString();
            return barber;
        }
        return null;

    }

    // get details from selected appoinment
    public String[] getAppSelected() {

        if (tableApp.getSelectedRow() >= 0) {

            String[] app = new String[6];
            app[0] = tableApp.getValueAt(tableApp.getSelectedRow(), 0).toString();
            app[1] = tableApp.getValueAt(tableApp.getSelectedRow(), 1).toString();
            app[2] = tableApp.getValueAt(tableApp.getSelectedRow(), 2).toString();
            app[3] = tableApp.getValueAt(tableApp.getSelectedRow(), 3).toString();
            app[4] = tableApp.getValueAt(tableApp.getSelectedRow(), 4).toString();
            app[5] = tableApp.getValueAt(tableApp.getSelectedRow(), 5).toString();
            return app;
        }
        return null;

    }

    // get complaint
    public String getComplaint() {

        return complaintText;

    }

    // screen to show a textarea to send complaints
    public void sendComplaintScreen() {

        JFrame complaint = new JFrame("Send Complaint");
        complaint.setVisible(true);
        complaint.setSize(350, 250);
        complaint.setResizable(false);
        complaint.setLocationRelativeTo(null);

        BorderLayout frameLayout = new BorderLayout();
        complaint.setLayout(frameLayout);

        // subtitle
        JPanel titlePanel = new JPanel();
        FlowLayout titleLayout = new FlowLayout();
        titleLayout.setAlignment(FlowLayout.CENTER);
        titlePanel.setLayout(titleLayout);
        JLabel subtitle = new JLabel("Write a complaint (no more than 250 characters)");
        titlePanel.add(subtitle);

        // add center
        // panel to enter compplaint
        JPanel complaintPanel = new JPanel();
        FlowLayout centerLayout = new FlowLayout();
        titleLayout.setAlignment(FlowLayout.CENTER);
        complaintPanel.setLayout(titleLayout);
        JTextArea textComplaint = new JTextArea();
        textComplaint.setColumns(20);
        textComplaint.setLineWrap(true);
        textComplaint.setRows(8);
        JScrollPane scrollpane = new JScrollPane(textComplaint);
        complaintPanel.add(scrollpane);

        // add Button send feeback
        JPanel buttonPanel = new JPanel();
        FlowLayout pageEndLayout = new FlowLayout();
        pageEndLayout.setAlignment(FlowLayout.CENTER);
        buttonPanel.setLayout(pageEndLayout);
        JButton feedback = new JButton("Send");
        feedback.addActionListener((ActionEvent e) -> {
            complaintText = textComplaint.getText();
            controller.sendComplaint();
            complaint.dispose();
        });
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener((ActionEvent e) -> {
            complaintText = textComplaint.getText();
            complaint.dispose();
        });
        buttonPanel.add(feedback);
        buttonPanel.add(cancelar);

        complaint.add(titlePanel, BorderLayout.PAGE_START);
        complaint.add(complaintPanel, BorderLayout.CENTER);
        complaint.add(buttonPanel, BorderLayout.PAGE_END);

    }

    // set status for a row
    public void setStatus(String status) {

        tableModelApp.setValueAt(status, tableApp.getSelectedRow(), 4);

    }

    // set status for a row
    public void setStatusByRow(int row, String status) {

        tableModelApp.setValueAt(status, row, 4);

    }

}
