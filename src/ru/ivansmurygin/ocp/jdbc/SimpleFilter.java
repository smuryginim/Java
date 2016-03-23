package ru.ivansmurygin.ocp.jdbc;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SmuryginIM on 23.03.2016.
 */
public class SimpleFilter implements Predicate {
    Pattern pattern;

    public SimpleFilter(String namePattern) {
        if (namePattern != null && !namePattern.isEmpty()) {
            pattern = Pattern.compile(namePattern);
        }

    }

    @Override
    public boolean evaluate(RowSet rs) {
        try {
            if (!rs.isAfterLast()){
                String name = rs.getString(2);
                Matcher matcher = pattern.matcher(name);
                boolean matches = matcher.matches();
                if (matches){
                    System.out.printf("Current name = %s matches pattern\n", name);
                } else {
                    System.out.printf("Current name = %s doesn't match pattern\n", name);
                }
                return matches;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean evaluate(Object value, int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean evaluate(Object value, String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }

}
