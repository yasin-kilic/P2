package main.java;

import main.java.DAO.ReizigerDAOPsql;
import main.java.Interfaces.ReizigerDAO;
import main.java.Model.Reiziger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main
{
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        getConnection();
        var reizerDAO = new ReizigerDAOPsql(connection);
        testReizigerDAO(reizerDAO);
        closeConnection();
    }

    private static void getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/ovchip?user=postgres&password=windows16";
        connection = DriverManager.getConnection(url);
    }

    private static void closeConnection() throws SQLException {
        connection.close();
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal alle reizigers op uit de database bij geboortedatum
        List<Reiziger> reizigers2 = rdao.findByGbdatum("1981-03-14");
        System.out.println("[Test] ReizigerDAO.findByGbdatum('1981-03-14') geeft de volgende reizigers:");
        for (Reiziger r : reizigers2) {
            System.out.println(r);
        }
        System.out.println();


        // Haal reiziger op uit de database bij id
        Reiziger reiziger = rdao.findById(3);
        System.out.println("[Test] ReizigerDAO.findById('3') geeft de volgende reiziger:");
        System.out.println(reiziger);
        System.out.println();

        // update de achternaam van een reiziger
        sietske.setAchternaam("achternaam voor update");
        rdao.update(sietske);
        Reiziger sietskeVoorUpdate = rdao.findById(sietske.getId());
        System.out.println("sietske voor update: " + sietskeVoorUpdate);

        sietske.setAchternaam("achternaam na update!!!");
        rdao.update(sietske);
        Reiziger dietskeNaUpdate = rdao.findById(sietske.getId());

        System.out.println("sietske na update: " + dietskeNaUpdate);
        System.out.println();

        // verwijderd Reiziger uit database.
        Reiziger sietskeVoorDelete = rdao.findById(sietske.getId());
        System.out.println("Dietske voor delete: ");
        System.out.println(sietskeVoorDelete != null ? "Dietske gevonden in database!" : "Dietske niet gevonden in database!");
        rdao.delete(sietske);
        Reiziger sietskeNaDelete = rdao.findById(sietske.getId());
        System.out.println("Dietske na delete: ");
        System.out.println(sietskeNaDelete != null ? "Dietske gevonden in database!" : "Dietske niet gevonden in database!");
    }
}
