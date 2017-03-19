package generator.interf.list;

import gsonpath.internal.GsonPathElementList;

import java.lang.Override;
import java.lang.String;
import java.util.List;

public final class TestListInterface_GsonPathModel extends GsonPathElementList<String> implements TestListInterface {
    private final List<String> internalList;

    public TestListInterface_GsonPathModel(List<String> internalList) {
        this.internalList = internalList;
    }

    @Override
    protected List<String> getList() {
        return internalList;
    }
}
