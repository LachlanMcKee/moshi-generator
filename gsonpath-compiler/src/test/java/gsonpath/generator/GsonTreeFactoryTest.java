package gsonpath.generator;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.annotations.SerializedName;
import com.squareup.javapoet.TypeName;
import gsonpath.GsonFieldValidationType;
import gsonpath.PathSubstitution;
import gsonpath.ProcessingException;
import gsonpath.model.FieldInfo;
import gsonpath.model.GsonField;
import gsonpath.model.GsonTree;
import gsonpath.model.GsonTreeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GsonTreeFactoryTest {
    @Test
    public void createFieldTree_givenNoAnnotations_expectSingleTreeElement() throws ProcessingException {
        //given
        GsonTreeFactory gsonTreeFactory = new GsonTreeFactory();

        FieldInfo fieldInfo = mock(FieldInfo.class);
        when(fieldInfo.getTypeName()).thenReturn(TypeName.INT);
        when(fieldInfo.getAnnotationNames()).thenReturn(new String[0]);
        when(fieldInfo.getFieldName()).thenReturn("variableName");

        GsonTree expectedTree = new GsonTree();
        expectedTree.addField("variableName", new GsonField(0, fieldInfo, "variableName", false));

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        fieldInfoList.add(fieldInfo);

        // when
        GsonTree outputTree = gsonTreeFactory.createGsonTree(fieldInfoList,
                "", '.', FieldNamingPolicy.IDENTITY, GsonFieldValidationType.NO_VALIDATION,
                new PathSubstitution[0]);

        // then
        Assert.assertEquals(expectedTree, outputTree);
    }

    @Test
    public void createFieldTree_givenAnnotations_expectMultipleTreeElement() throws ProcessingException {
        //given
        GsonTreeFactory gsonTreeFactory = new GsonTreeFactory();

        SerializedName serializedName = mock(SerializedName.class);
        when(serializedName.value()).thenReturn("root.child");

        FieldInfo fieldInfo = mock(FieldInfo.class);
        when(fieldInfo.getTypeName()).thenReturn(TypeName.INT);
        when(fieldInfo.getAnnotationNames()).thenReturn(new String[0]);
        when(fieldInfo.getFieldName()).thenReturn("variableName");
        when(fieldInfo.getAnnotation(SerializedName.class)).thenReturn(serializedName);

        GsonTree expectedTree = new GsonTree();
        GsonTree rootTree = new GsonTree();
        rootTree.addField("child", new GsonField(0, fieldInfo, "root.child", false));
        expectedTree.addTreeBranch("root", rootTree);

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        fieldInfoList.add(fieldInfo);

        // when
        GsonTree outputTree = gsonTreeFactory.createGsonTree(fieldInfoList,
                "", '.', FieldNamingPolicy.IDENTITY, GsonFieldValidationType.NO_VALIDATION,
                new PathSubstitution[0]);

        // then
        Assert.assertEquals(expectedTree, outputTree);
    }
}
