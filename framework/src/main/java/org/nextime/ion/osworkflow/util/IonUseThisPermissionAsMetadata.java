package org.nextime.ion.osworkflow.util;

import com.opensymphony.workflow.Condition;
import com.opensymphony.workflow.WorkflowContext;
import java.util.Map;

/**
 * @author gbort
 */
public class IonUseThisPermissionAsMetadata implements Condition {

    public boolean passesCondition(Map inputs, Map args, Map variables) {
        try {
            WorkflowContext context
                    = (WorkflowContext) variables.get("context");
            return "smartIonFramework".equals(context.getCaller());
        } catch (Exception e) {
            return false;
        }
    }
}
