package org.nextime.ion.frontoffice.objectSelector;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nextime.ion.common.IsOnline;
import org.nextime.ion.framework.helper.SearchResult;
import org.nextime.ion.framework.helper.Searcher;
import org.nextime.ion.framework.helper.Viewer;
import org.nextime.ion.framework.logger.Logger;

public class SearchPublications implements ObjectSelector {

    public Collection selectObjects(
            Hashtable params,
            HttpServletRequest request,
            HttpServletResponse response)
            throws SelectException {

        try {
            String queryString = (String) params.get("queryString");
            String view = (String) params.get("view");
            String index = (String) params.get("index");

            String currentLocale
                    = request.getSession().getAttribute("currentLocale") + "";

            Vector returnResults = new Vector();
            if (queryString.equals("") || index.equals("")) {
                return returnResults;
            }
            Vector results = Searcher.search(queryString, index);

            int count = 0;
            for (int i = 0; i < results.size(); i++) {
                SearchResult result = (SearchResult) results.get(i);

                // est ce la version actuellement publi�e ???
                if (result.getVersion()
                        == IsOnline.getMostRecentVersion(result.getPublication())) {

                    String viewResult = "";
                    if (!view.equals("")) {
                        viewResult
                                = new String(
                                        Viewer.getView(
                                                result.getPublication(),
                                                result.getVersion(),
                                                view,
                                                currentLocale));
                    }

                    SearchPublicationResult spr = new SearchPublicationResult();
                    PublicationResult pr = new PublicationResult();
                    pr.setPublication(result.getPublication());
                    pr.setVersion(result.getVersion());
                    pr.setView(viewResult);
                    spr.setPublicationResult(pr);
                    spr.setSearchResult(result);

                    returnResults.add(spr);
                }
            }
            return returnResults;
        } catch (Exception e) {
            Logger.getInstance().error("Erreur du SelectObject", this, e);
            throw new SelectException(e.getMessage());
        }
    }

}
