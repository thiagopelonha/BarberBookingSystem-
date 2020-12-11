package view;

import Controller.BarberHomeScreenController;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

/**
 * Barbe home screen contains only the elements to show to the user (strings)
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class BarberHomeScreen extends JFrame {

    private BarberHomeScreenController controller;
    private final Object[] columnNamesSlot = {"Slot", "Available"};
    private final Object[] columnNamesApp = {"ID App", "Customer", "Phone Number", "Date - Slot", "status"};
    private JSpinner timeSpinner;
    private TableModel tableModelSlot;
    private JTable tableSlot;
    private JButton addSlot;
    private JButton delete;
    private Object[][] dataSlot;
    private JLabel timeSlot;
    private JTextField dateFromTextField;
    private JTextField dateToTextField;
    private TableModel tableModelApp;
    private JTable tableApp;
    private JComboBox status;

    public BarberHomeScreen(BarberHomeScreenController controller, Object[][] data) {

        this.controller = controller;
        this.dataSlot = data;
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
        imgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/b2friends_80.jpeg")));
        imagePanel.add(imgLabel);

        // this top panel will be inserted in the PAGE_START of the screen
        topPanel.add(titlePanel);
        topPanel.add(imagePanel);
        //----------------------------------------------------------------------
        // center panel
        JPanel centerPanel = new JPanel();
        BoxLayout centerLayout = new BoxLayout(centerPanel, BoxLayout.Y_AXIS);
        centerPanel.setLayout(centerLayout);

        // first Panel all the elements related with slots
        JPanel firstPanel = new JPanel();
        GridLayout firstLayout = new GridLayout(1, 3);
        firstPanel.setLayout(firstLayout);
        firstPanel.setBorder(BorderFactory.createTitledBorder("Slots"));

        // buttons to work with slots - LEFT PANEL 
        JPanel slotPanel = new JPanel();
        FlowLayout slotLayout = new FlowLayout();
        slotPanel.setLayout(slotLayout);

        timeSlot = new JLabel("Time slot");
        // start Spinner Time 
        timeSpinner = new JSpinner(new SpinnerDateModel());
        // set format to HH:mm
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        DateFormatter formatter = (DateFormatter) timeEditor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        timeSpinner.setEditor(timeEditor);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        // it will only show the current time
        timeSpinner.setValue(calendar.getTime());

        // end Spinner Time
        // button add slot
        addSlot = new JButton("Add slot");
        addSlot.setActionCommand("add");
        addSlot.addActionListener(controller);

        slotPanel.add(timeSlot);
        slotPanel.add(timeSpinner);
        slotPanel.add(addSlot);

        // list with slots and buttosn - CENTER PANEL 
        JPanel listPanel = new JPanel();
        BoxLayout listLayout = new BoxLayout(listPanel, BoxLayout.Y_AXIS);
        listPanel.setLayout(listLayout);

        // SLOTS TABLE
        tableModelSlot = new TableModel(dataSlot, columnNamesSlot) {

            // this method is overried to define a checkbox on the table
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Boolean.class;
                    default:
                        return String.class;

                }
            }

        };

        // table slot
        tableSlot = new JTable(tableModelSlot);
        JScrollPane scrollPanelSlot = new JScrollPane(tableSlot);
        tableSlot.setSelectionModel(new ForcedListSelectionModel());

        // button save
        JButton save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(controller);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);

        // add the center panel table slots and save button
        listPanel.add(scrollPanelSlot);
        listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        listPanel.add(save);

        // three buttons to work with the list - LAST PANEL ON THE RIGHT
        JPanel thirdColPanel = new JPanel();
        FlowLayout thirdLayout = new FlowLayout();
        thirdColPanel.setLayout(thirdLayout);

        JPanel buttonsPanel = new JPanel();
        BoxLayout buttonsLayout = new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS);
        buttonsPanel.setLayout(buttonsLayout);

        delete = new JButton("Delete slot");
        delete.setActionCommand("delete");
        delete.addActionListener(controller);
        JButton setAvailable = new JButton("Set slot as (not) available");
        setAvailable.setActionCommand("setAvailable");
        setAvailable.addActionListener(controller);

        buttonsPanel.add(delete);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        buttonsPanel.add(setAvailable);

        thirdColPanel.add(buttonsPanel);

        // gridLayout 3 columns
        firstPanel.add(slotPanel);
        firstPanel.add(listPanel);
        firstPanel.add(thirdColPanel);

        // PANEL BELOW WITH APPOINMENTS = SECOND LINE ON THE CENTER PANEL
        // first Panel 
        JPanel secondPanel = new JPanel();
        BoxLayout secondLayout = new BoxLayout(secondPanel, BoxLayout.Y_AXIS);
        secondPanel.setLayout(secondLayout);
        secondPanel.setBorder(BorderFactory.createTitledBorder("Appointments"));

        // FIRST LINE 
        JPanel filtersPanel = new JPanel();
        GridLayout filtersLayout = new GridLayout(1, 2);
        filtersPanel.setLayout(filtersLayout);

        // LEFT PANEL WITH DATES FROM AND TO AND SEARCH BUTTON
        JPanel leftPanel = new JPanel();
        BoxLayout leftLayout = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(leftLayout);

        // panel for date from 
        JPanel dateFromPanel = new JPanel();
        FlowLayout dateFromLayout = new FlowLayout();
        dateFromLayout.setAlignment(FlowLayout.LEFT);
        dateFromPanel.setLayout(dateFromLayout);
        JLabel dateFromLabel = new JLabel("Date from: ");
        dateFromTextField = new JTextField(6);
        // create button to open date picker frame for from date
        JButton pDateFromButton = new JButton("Pick a date");
        // catch event to set the date
        pDateFromButton.addActionListener((ActionEvent arg0) -> {
            //create frame new object  f
            final JFrame datePicker = new JFrame();
            //set text which is collected by date picker i.e. set date
            dateFromTextField.setText(new DatePicker(datePicker).setPickedDate());
        });
        // date from panel
        dateFromPanel.add(dateFromLabel);
        dateFromPanel.add(dateFromTextField);
        dateFromPanel.add(pDateFromButton);

        //----------------------------------------------------
        JPanel dateToPanel = new JPanel();
        FlowLayout dateToLayout = new FlowLayout();
        dateToLayout.setAlignment(FlowLayout.LEFT);
        dateToPanel.setLayout(dateToLayout);
        JLabel dateToLabel = new JLabel("Date to:      ");
        dateToTextField = new JTextField(6);
        // create button to open date picker frame for from date
        JButton pDateToButton = new JButton("Pick a date");
        pDateToButton.addActionListener((ActionEvent arg0) -> {
            //create frame new object  f
            final JFrame datePicker = new JFrame();
            //set text which is collected by date picker i.e. set date
            dateToTextField.setText(new DatePicker(datePicker).setPickedDate());
        });
        // search button
        JButton search = new JButton("Search");
        search.addActionListener(controller);
        search.setActionCommand("search");

        dateToPanel.add(dateToLabel);
        dateToPanel.add(dateToTextField);
        dateToPanel.add(pDateToButton);
        dateToPanel.add(search);

        // left panel with buttons and textfield and labels
        leftPanel.add(dateFromPanel);
        leftPanel.add(dateToPanel);

        filtersPanel.add(leftPanel);

        // RIGHT PANEL WITH STATUS BUTTON CHANGE STATUS AND REFRESH TABLE BUTTON
        JPanel rightPanel = new JPanel();
        BoxLayout rightLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(rightLayout);

        // TO ALING TO THE LEFT EVERYTHING
        JPanel rightPanel2 = new JPanel();
        FlowLayout rightLayout2 = new FlowLayout();
        rightLayout2.setAlignment(FlowLayout.LEFT);
        rightPanel2.setLayout(rightLayout2);

        // COMBOBOX
        String statusValues[] = {"cancelled", "confirmed", "completed"};
        status = new JComboBox<>(statusValues);

        JButton changeStatus = new JButton("Change status");
        changeStatus.addActionListener(controller);
        changeStatus.setActionCommand("change");

        // TO ALING TO THE LEFT THE BUTTON REFRESH AND CHECKBOX
        JPanel rightPanel3 = new JPanel();
        FlowLayout rightLayout3 = new FlowLayout();
        rightLayout3.setAlignment(FlowLayout.LEFT);
        rightPanel3.setLayout(rightLayout3);

        JButton refresh = new JButton("Refresh table");
        refresh.addActionListener(controller);
        refresh.setActionCommand("refresh");
        refresh.setVisible(false);

        JCheckBox upAppCheckBox = new JCheckBox("Upcomming appointments", true);
        upAppCheckBox.setSelected(false);
        // CHECKBOX EVENT
        // DISABLE BUTTONS TO SEARCH BY DATE - ONLY REFRESH AND CHANGE STATUS LEFT ON THE SCREEN
        upAppCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (e.getStateChange() == 1) { //checked

                    leftPanel.setVisible(false);
                    refresh.setVisible(true);
                    controller.searchUpcomingApps();

                } else { // unchecked

                    leftPanel.setVisible(true);
                    refresh.setVisible(false);
                    setDataApps(null);
                }
            }
        });

        rightPanel2.add(changeStatus);
        rightPanel2.add(status);

        rightPanel3.add(upAppCheckBox);
        rightPanel3.add(refresh);

        rightPanel.add(rightPanel2);
        rightPanel.add(rightPanel3);

        filtersPanel.add(rightPanel);

        // SECOND LINE IN THE SECOND PANEL
        // table appointments
        Object[][] dataApp = null;
        tableModelApp = new TableModel(dataApp, columnNamesApp);

        tableApp = new JTable(tableModelApp);
        JScrollPane scrollPanelApp = new JScrollPane(tableApp);
        tableApp.setSelectionModel(new ForcedListSelectionModel());

        secondPanel.add(filtersPanel);
        secondPanel.add(scrollPanelApp);

        centerPanel.add(firstPanel);
        centerPanel.add(secondPanel);

        this.add(topPanel, BorderLayout.PAGE_START);
        this.add(centerPanel, BorderLayout.CENTER);

    }

    private void validation() {
        this.validate();
        this.repaint();
    }

    // get slot from spinner
    public String getTime() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(timeSpinner.getValue());

    }

    // Show a message on the screen
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

    // add slot to the table on the screen
    public void addSlot(int index, Object[] row) {

        tableModelSlot.insertRow(index, row);

    }

    // get details from selected appoinment
    public String[] getSlotSelected() {

        if (tableSlot.getSelectedRow() >= 0) {

            String[] app = new String[2];
            app[0] = tableSlot.getValueAt(tableSlot.getSelectedRow(), 0).toString();
            app[1] = tableSlot.getValueAt(tableSlot.getSelectedRow(), 1).toString();
            return app;
        }
        return null;

    }

    // remove slot
    public void removeSlot() {

        tableModelSlot.removeRow(tableSlot.getSelectedRow());

    }

    // set fi a slot is available or not on the scren
    public void setAvailability(boolean available) {

        tableModelSlot.setValueAt(available, tableSlot.getSelectedRow(), 1);

    }

    // set visible fields on the screen when checkbox upcominf appointments is set
    public void disableAddandDeleteSlot() {

        addSlot.setVisible(false);
        timeSpinner.setVisible(false);
        delete.setVisible(false);
        timeSlot.setVisible(false);
    }

    // get date from
    public String getDateFrom() {

        return dateFromTextField.getText();

    }

    // get date to
    public String getDateTo() {

        return dateToTextField.getText();

    }

    // set appointments on the screen
    public void setDataApps(Object[][] data) {

        tableModelApp = new TableModel(data, columnNamesApp);
        tableApp.setModel(tableModelApp);

    }

    // get the appiontment selected
    public String[] getAppSelected() {

        if (tableApp.getSelectedRow() >= 0) {

            String[] app = new String[6];
            app[0] = tableApp.getValueAt(tableApp.getSelectedRow(), 0).toString();
            app[1] = tableApp.getValueAt(tableApp.getSelectedRow(), 1).toString();
            app[2] = tableApp.getValueAt(tableApp.getSelectedRow(), 2).toString();
            app[3] = tableApp.getValueAt(tableApp.getSelectedRow(), 3).toString();
            app[4] = tableApp.getValueAt(tableApp.getSelectedRow(), 4).toString();
            return app;
        }
        return null;
    }

    // get the status selected on the checkbox
    public String getStatus() {

        return (String) status.getSelectedItem();

    }

    // set status for a row
    public void setStatus(String status) {

        tableModelApp.setValueAt(status, tableApp.getSelectedRow(), 4);

    }

}
