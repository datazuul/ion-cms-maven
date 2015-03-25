package org.nextime.ion.admin.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.config.Config;

public class ConfigAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        File f = Config.getInstance().getDatabaseConfigurationFile();
        request.setAttribute("pathConfig", f.getAbsolutePath());

        // lit le contenu du fichier
        String content = "";
        String line = "";
        BufferedReader is = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        while (line != null) {
            content += line + "\r\n";
            line = is.readLine();
        }
        is.close();
        request.setAttribute("content", content);

        request.setAttribute("view", "config");
        return mapping.findForward("view");
    }

}
