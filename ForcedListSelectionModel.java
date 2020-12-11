
package view;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * this class is used to force the user to select only one row.
 * this one used by JTable components
 * @author Fernando
 */
public class ForcedListSelectionModel extends DefaultListSelectionModel {

    public ForcedListSelectionModel () {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }

}