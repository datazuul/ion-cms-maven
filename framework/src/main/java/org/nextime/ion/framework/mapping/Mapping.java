package org.nextime.ion.framework.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDO;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.logger.Logger;

public class Mapping {

    private JDO jdo;
    private static Mapping instance;
    private File databaseConfigurationFile;
    protected Hashtable databases;

    private Mapping() {
        try {
            File f = Config.getInstance().getDatabaseConfigurationFile();
            if (f == null) {
                throw new MappingException("Le fichier database.xml est necessaire au demarrage de la couche mapping");
            }
            jdo = new JDO(Config.getInstance().getDatabaseName());
            databases = new Hashtable();
            if (!Config.getInstance().getDisableLog()) {
                getJdo().setLogWriter(
                        new org.exolab.castor.util.Logger(System.out).setPrefix(
                                "WCM mapping"));
            }

            String content = "";
            BufferedReader reader
                    = new BufferedReader(
                            new InputStreamReader(new FileInputStream(f)));
            String line = "";
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    content += line;
                }
            }
            while (content.indexOf("%conf%") != -1) {
                content
                        = doReplace(
                                content,
                                "%conf%",
                                Config.getInstance().getConfigDirectoryPath());
            }
            File temp = File.createTempFile("ion", "database.xml");
            FileOutputStream fos = new FileOutputStream(temp);
            fos.write(content.getBytes());
            fos.close();

            jdo.setConfiguration(temp.getAbsolutePath());
            new MappingCleanerDaemon(this);
            Logger.getInstance().log("La couche mapping est prxte.", this);
        } catch (Exception e) {
            Logger.getInstance().fatal(
                    "Erreur lors de la creation de la couche mapping.",
                    this,
                    e);
            System.exit(0);
        }
    }

    public void reset() {
        instance = null;
    }

    public static Mapping getInstance() {
        if (instance == null) {
            instance = new Mapping();
        }
        return instance;
    }

    public static void begin() throws MappingException {
        try {
            getInstance().getDb().begin();
        } catch (org.exolab.castor.jdo.PersistenceException e) {
            Logger.getInstance().error(
                    "Impossible de dxmarrer une transaction.",
                    getInstance(),
                    e);
            throw new MappingException(e.getMessage());
        }
    }

    public static void commit() throws MappingException {
        try {
            getInstance().getDb().commit();
        } catch (org.exolab.castor.jdo.PersistenceException e) {
            Logger.getInstance().error(
                    "Impossible de valider la transaction.",
                    getInstance(),
                    e);
            throw new MappingException(e.getMessage());
        }
    }

    /**
     * Annule la transaction.
     */
    public static void rollback() {
        try {
            getInstance().getDb().rollback();
        } catch (org.exolab.castor.jdo.PersistenceException e) {
            Logger.getInstance().error(
                    "Impossible d'annuler la transaction.",
                    getInstance(),
                    e);
        }
    }

    public Database getDb() throws MappingException {
        try {
            Thread t = Thread.currentThread();
            Database db = (Database) databases.get(t);
            if (db != null) {
                return db;
            }
            db = jdo.getDatabase();
            databases.put(t, db);
            return db;
        } catch (Exception e) {
            throw new MappingException(e.getMessage());
        }
    }

    /**
     * @return true/false selon qu'une transaction est en cours dans le thread courant
     */
    public boolean isTransactionActive() throws MappingException {
        try {
            Thread t = Thread.currentThread();
            Database db = (Database) databases.get(t);
            if (db == null) {
                return false;
            }
            return db.isActive();
        } catch (Exception e) {
            throw new MappingException(e.getMessage());
        }
    }

    public JDO getJdo() {
        return jdo;
    }

    private static String doReplace(String source, String from, String to) {
        String ret = "";
        int i = source.indexOf(from);
        if (i == -1) {
            return source;
        }
        ret = source.substring(0, i);
        ret = ret + to;
        ret = ret + source.substring(i + from.length());
        return ret;
    }

}
