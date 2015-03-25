package org.nextime.ion.osworkflow.util;

import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;
import java.util.Map;
import org.nextime.ion.framework.business.Publication;

/**
 * @author gbort
 */
public class IonOnlyAuthorCondition implements Condition {

    public boolean passesCondition(Map inputs, Map args, Map variables) {
        try {
            WorkflowContext context
                    = (WorkflowContext) variables.get("context");
            String id = variables.get("publicationId") + "";
            int version
                    = Integer.parseInt(variables.get("publicationVersion") + "");

            String author
                    = Publication
                    .getInstance(id)
                    .getVersion(version)
                    .getAuthor()
                    .getLogin();

            return author.equals(context.getCaller());
        } catch (Exception e) {
            return false;
        }
    }
}
