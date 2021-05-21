package simulator.views;

import javax.swing.table.AbstractTableModel;

public class ForceLawParametersTable extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private String[] refactor = { "Key", "Value", "Description" };
    String[][] data;
    int rows = 3, columns = 3;

    public ForceLawParametersTable() {
        data = new String[rows][columns];
        clear();
    }

    public void clear() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                data[i][j] = "";
        fireTableStructureChanged();
    }

    @Override
    public String getColumnName(int column) {
        return refactor[column];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return refactor.length;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = o.toString();
    }

    // Method getData() returns a String corresponding to a JSON structure
    // with column 1 as keys and column 2 as values.

    // This method return the coIt is important to build it as a string, if
    // we create a corresponding JSONObject and use put(key,value), all values
    // will be added as string. This also means that if users want to add a
    // string value they should add the quotes as well as part of the
    // value (2nd column).
    //
    public String getData() {
        StringBuilder s = new StringBuilder();
        s.append('{');
        for (String[] datum : data) {
            if (!datum[0].isEmpty() && !datum[1].isEmpty()) {
                s.append('"');
                s.append(datum[0]);
                s.append('"');
                s.append(':');
                s.append(datum[1]);
                s.append(',');
            }
        }

        if (s.length() > 1)
            s.deleteCharAt(s.length() - 1);
        s.append('}');

        return s.toString();
    }

}
