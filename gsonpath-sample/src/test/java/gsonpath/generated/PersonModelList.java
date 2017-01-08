package gsonpath.generated;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;

import java.util.List;

@AutoGsonAdapter(rootField = "people")
public interface PersonModelList extends List<PersonModelList.PersonModel> {

    @AutoGsonAdapter(rootField = "person.names")
    interface PersonModel {
        @SerializedName("first")
        String getFirstName();
    }

}
