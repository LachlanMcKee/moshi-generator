package gsonpath.generated;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;
import gsonpath.internal.GsonPathElementList;

import java.util.List;

@AutoGsonAdapter
public class PersonModelList extends GsonPathElementList<PersonModelList.PersonModel> {
    List<PersonModel> people;

    @Override
    protected List<PersonModel> getList() {
        return people;
    }

    @AutoGsonAdapter(rootField = "person.names")
    public interface PersonModel {
        @SerializedName("first")
        String getFirstName();
    }

}
