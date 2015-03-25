package org.nextime.ion.frontoffice.bean;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import org.nextime.ion.common.ResourceServlet;

public class Resources {

    private static ResourceXmlBean bean;

    public static ResourceXmlBean getResourceXmlBean(
            HttpServlet servlet,
            String id)
            throws Exception {
        if (bean == null) {
            FileInputStream fis
                    = new FileInputStream(
                            new File(
                                    servlet.getServletContext().getRealPath(
                                            ResourceServlet.configPath)));
            bean = ResourceXmlBean.parse(fis);
            fis.close();
        }
        return bean.getResource(id);
    }

    public static Vector getResourceXmlBeans(HttpServlet servlet)
            throws Exception {
        if (bean == null) {
            FileInputStream fis
                    = new FileInputStream(
                            new File(
                                    servlet.getServletContext().getRealPath(
                                            ResourceServlet.configPath)));
            bean = ResourceXmlBean.parse(fis);
            fis.close();
        }
        return bean.getItems();
    }

}
