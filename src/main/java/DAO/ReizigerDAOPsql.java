package main.java.DAO;

import main.java.Interfaces.ReizigerDAO;
import main.java.Model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO
{
    private Connection conn;

    public ReizigerDAOPsql(Connection conn)
    {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) {

        System.out.println("save is being called");

        String SQL;
        try
        {
            SQL =   "INSERT INTO reiziger" +
                    "(reiziger_id,voorletters, tussenvoegsel, achternaam, geboortedatum) " +
                    "VALUES(?,?,?,?,?)";
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return false;
        }


        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("affectedRows = " + affectedRows);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        System.out.println("update is being called");

        String SQL;
        try
        {
            SQL =   "UPDATE reiziger " +
                    "SET reiziger_id = ?,voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? " +
                    "WHERE reiziger_id = " + reiziger.getId();
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return false;
        }

        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("affectedRows = " + affectedRows);

        } catch (SQLException ex) {
            System.out.println("Error tijdens uitvoeren van PreparedStatement");
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        System.out.println("delete is being called");

        String SQL;
        try
        {
            SQL =   "DELETE FROM reiziger " +
                    "WHERE reiziger_id = " + reiziger.getId();
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return false;
        }

        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println("affectedRows = " + affectedRows);

        } catch (SQLException ex) {
            System.out.println("Error tijdens uitvoeren van PreparedStatement");
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Reiziger findById(int id) {
        System.out.println("findById is being called");

        String SQL;
        try
        {
            SQL =   "SELECT * FROM reiziger " +
                    "WHERE reiziger_id = " + id;
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return null;
        }

        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");
        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            boolean result = preparedStatement.execute();
            System.out.println("PreparedStatement succesfull executed? : " + result);

            if (result) {
                try (ResultSet rs = preparedStatement.getResultSet()) {
                    if (rs.next()) {
                        return new Reiziger(
                                rs.getInt("reiziger_id"),
                                rs.getString("voorletters"),
                                rs.getString("tussenvoegsel"),
                                rs.getString("achternaam"),
                                rs.getDate("geboortedatum")
                                );
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error tijdens uitvoeren van PreparedStatement");
            System.out.println(ex.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        System.out.println("findByGbdatum is being called");

        String SQL;
        try
        {
            SQL =   "SELECT * " +
                    "FROM reiziger " +
                    "WHERE geboortedatum = '" + datum +"'";
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return null;
        }

        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            boolean result = preparedStatement.execute();
            System.out.println("PreparedStatement succesfull executed? : " + result);

            List<Reiziger> resultaten = new ArrayList<Reiziger>();
            if (result) {
                System.out.println("resultaat gevonden!");

                try (ResultSet rs = preparedStatement.getResultSet()) {
                    while (rs.next()) {
                        resultaten.add(new Reiziger(
                                rs.getInt("reiziger_id"),
                                rs.getString("voorletters"),
                                rs.getString("tussenvoegsel"),
                                rs.getString("achternaam"),
                                rs.getDate("geboortedatum")
                        ));
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return resultaten;

        } catch (SQLException ex) {
            System.out.println("Error tijdens uitvoeren van de PreparedStatement");
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public List<Reiziger> findAll() {
        System.out.println("findAll is being called");

        String SQL;
        try
        {
            SQL =   "SELECT * FROM reiziger";
        }
        catch(Exception exception)
        {
            System.out.println("Er is iets misgegaan tijdens aanmaken van de SQL query");
            return null;
        }

        System.out.println("The query to be executed: " + SQL);

        System.out.println("Executing PreparedStatement");

        try (PreparedStatement preparedStatement = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            boolean result = preparedStatement.execute();
            System.out.println("PreparedStatement succesfull executed? : " + result);

            List<Reiziger> resultaten = new ArrayList<Reiziger>();

            if (result) {
                System.out.println("resultaat gevonden!");

                try (ResultSet resultSet = preparedStatement.getResultSet()) {
                    while (resultSet.next()) {
                        resultaten.add(new Reiziger(
                                resultSet.getInt("reiziger_id"),
                                resultSet.getString("voorletters"),
                                resultSet.getString("tussenvoegsel"),
                                resultSet.getString("achternaam"),
                                resultSet.getDate("geboortedatum")
                        ));
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            return resultaten;

        } catch (SQLException ex)
        {
            System.out.println("Error tijdens uitvoeren van de PreparedStatement");
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
