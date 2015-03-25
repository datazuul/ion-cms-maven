package org.nextime.ion.admin.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.nextime.ion.framework.config.Config;
import org.nextime.ion.framework.mapping.Mapping;

public class ChangeDatabaseAction extends Action {

    public ActionForward perform(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        File f = Config.getInstance().getDatabaseConfigurationFile();

        // ecris le contenu du fichier
        PrintStream os = new PrintStream(new FileOutputStream(f));
        os.println(request.getParameter("content"));
        os.close();

        Mapping.getInstance().reset();

        request.setAttribute("view", "config");
        return mapping.findForward("success");
    }

}
