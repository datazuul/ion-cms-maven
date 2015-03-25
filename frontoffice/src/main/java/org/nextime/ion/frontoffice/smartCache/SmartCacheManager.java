package org.nextime.ion.frontoffice.smartCache;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.nextime.ion.framework.business.impl.PublicationImpl;
import org.nextime.ion.framework.business.impl.SectionImpl;
import org.nextime.ion.framework.event.WcmEvent;
import org.nextime.ion.framework.event.WcmListener;

public class SmartCacheManager extends HttpServlet implements WcmListener {

    static {
        PublicationImpl.addListener(new SmartCacheManager());
        SectionImpl.addListener(new SmartCacheManager());
    }

    private static ServletContext context;

    /**
     * @see org.nextime.ion.framework.event.WcmListener#objectCreated(WcmEvent)
     */
    public void objectCreated(WcmEvent event) {
        cleanCache();
    }

    /**
     * @see org.nextime.ion.framework.event.WcmListener#objectDeleted(WcmEvent)
     */
    public void objectDeleted(WcmEvent event) {
        cleanCache();
    }

    /**
     * @see org.nextime.ion.framework.event.WcmListener#objectModified(WcmEvent)
     */
    public void objectModified(WcmEvent event) {
        cleanCache();
    }

    protected void cleanCache() {
//		try {
//			Vector sections = Section.listAll();
//			for (int i = 0; i < sections.size(); i++) {
//				String id = ((Section) sections.get(i)).getId();
//				context.removeAttribute("org.apache.taglibs.cache.session.caches.section_"+id);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        context.setAttribute("ion_lastUpdate", System.currentTimeMillis() + "");
    }

    /**
     * @see javax.servlet.Servlet#init(ServletConfig)
     */
    public void init(ServletConfig arg0) throws ServletException {
        context = arg0.getServletContext();
    }

}
