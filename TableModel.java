package view;

import javax.swing.table.DefaultTableModel;

/**
 * I make the table no editable with this class
 *
 * @author Jessica Lopes and Thiago Teixeira
 */
public class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
