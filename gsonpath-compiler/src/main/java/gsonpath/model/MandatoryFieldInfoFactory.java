package gsonpath.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class MandatoryFieldInfoFactory {

    public Map<String, MandatoryFieldInfo> createMandatoryFieldsFromGsonTree(GsonTree gsonTree) {
        Map<String, MandatoryFieldInfo> mandatoryInfoMap = new LinkedHashMap<>();
        createMandatoryFieldsFromGsonTree(gsonTree, mandatoryInfoMap);
        return mandatoryInfoMap;
    }

    /**
     * Add any mandatory field indexes as constants. This is done for code readability.
     * We will obtain the values using a depth-first recursion.
     */
    private void createMandatoryFieldsFromGsonTree(GsonTree gsonTree, Map<String, MandatoryFieldInfo> mandatoryInfoMap) {

        for (String branchKey : gsonTree.keySet()) {
            Object treeObject = gsonTree.get(branchKey);

            if (treeObject instanceof GsonField) {
                createConstantForField((GsonField) treeObject, mandatoryInfoMap);

            } else {
                // Recursive call, navigating further down the tree.
                createMandatoryFieldsFromGsonTree((GsonTree) treeObject, mandatoryInfoMap);
            }
        }
    }

    private void createConstantForField(GsonField info, Map<String, MandatoryFieldInfo> mandatoryInfoMap) {
        //
        // For all required fields we add an index field so we can easily check whether the
        // value has been assigned after the json has been parsed.
        //
        if (info.isRequired) {
            String fieldName = info.fieldInfo.getFieldName();
            String mandatoryFieldIndexName = "MANDATORY_INDEX_" + fieldName.toUpperCase();

            // Keep track of the information for later use. Since this is a linked list, we keep track of insert order.
            mandatoryInfoMap.put(fieldName, new MandatoryFieldInfo(mandatoryFieldIndexName, info));
        }
    }

}
