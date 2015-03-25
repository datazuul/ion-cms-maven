package org.nextime.ion.framework.mapping;

import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.jdo.Database;
import org.nextime.ion.framework.logger.Logger;

/**
 * libere la couche mapping des thread qui ne sont plus actifs
 *
 * @author gbort
 * @version 0.1
 */
public class MappingCleanerDaemon extends Thread {

    private Mapping _mapping;

    public MappingCleanerDaemon(Mapping mapping) {
        super("Mapping Cleaner");
        _mapping = mapping;
        setDaemon(true);
        start();
    }

    public void run() {
        for (;;) {
            try {
                this.sleep(1000 * 60 * 10);
            } catch (InterruptedException e) {
            }
            Logger.getInstance().log("Clean Mapping", this);
            Hashtable dbs = _mapping.databases;
            Enumeration threads = dbs.keys();
            while (threads.hasMoreElements()) {
                Thread thread = (Thread) threads.nextElement();
                if (!thread.isAlive()) {
                    Logger.getInstance().log(
                            "Le thread "
                            + thread
                            + " est mort. Destruction de son instance de Database",
                            this);
                    Database db = (Database) dbs.get(thread);
                    try {
                        db.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dbs.remove(thread);
                    thread.destroy();
                }
            }
        }
    }

}
