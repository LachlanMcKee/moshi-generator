package gsonpath;

import com.squareup.moshi.Moshi;
import gsonpath.generated.PersonModelGenerated;
import gsonpath.vanilla.PeopleModelVanilla;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PeopleModelTest {

    private static final int PEOPLE_SIZE = 50000;
    private static final int testSize = 50;

    // Create very large json file in memory to help benchmark.
    private static final String JSON_TEST_STRING;

    static {
        StringBuilder sb = new StringBuilder("{\"people\": [");

        int max = PEOPLE_SIZE;
        for (int i = 0; i < max; i++) {
            sb.append("{ \"person\": { \"names\": { \"first\": \"Lachlan\", \"last\": \"McKee\",");
            sb.append("\"Unused1\": \"ABC\", \"Unused2\": \"ABC\", \"Unused3\": \"ABC\",");
            sb.append("\"Unused4\": \"ABC\", \"Unused5\": \"ABC\", \"Unused6\": \"ABC\",");
            sb.append("\"Unused7\": \"ABC\", \"Unused8\": \"ABC\", \"Unused9\": \"ABC\",");
            sb.append("\"Unused8\": \"ABC\", \"Unused9\": \"ABC\", \"Unused10\": \"ABC\",");
            sb.append("\"Unused11\": \"ABC\", \"Unused12\": \"ABC\", \"Unused13\": \"ABC\",");
            sb.append("\"Unused14\": \"ABC\", \"Unused15\": \"ABC\", \"Unused16\": \"ABC\"");
            sb.append("} } }");

            if (i < max - 1) {
                sb.append(",");
            }
        }
        sb.append("]}");

        JSON_TEST_STRING = sb.toString();
    }

    // Create the gson objects once.
    private Moshi vanillaMoshi;
    private Moshi generatedMoshi;

    @Test
    public void test() throws IOException {
        vanillaMoshi = new Moshi.Builder().build();

        Moshi.Builder generatedMoshiBuilder = new Moshi.Builder();
        generatedMoshiBuilder.add(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class));
        generatedMoshi = generatedMoshiBuilder.build();

        // Benchmark regular gson.
        long vanillaAverage = 0;
        for (int i = 0; i < testSize; i++) {
            vanillaAverage += testVanillaGson();
        }
        System.out.println("vanillaAverage: " + (vanillaAverage / testSize));

        // Benchmark gson with gson path.
        long pathAverage = 0;
        for (int i = 0; i < testSize; i++) {
            pathAverage += testGsonPath();
        }
        System.out.println("pathAverage: " + (pathAverage / testSize));
    }

    private long testVanillaGson() throws IOException {
        long start = System.nanoTime();
        PeopleModelVanilla vanillaModel = vanillaMoshi.adapter(PeopleModelVanilla.class).fromJson(JSON_TEST_STRING);

        long duration = ((System.nanoTime() - start) / 1000000);
        System.out.println("vanillaModel. Time taken: " + duration);

        Assert.assertEquals(PEOPLE_SIZE, vanillaModel.people.length);
        Assert.assertEquals("Lachlan", vanillaModel.people[0].person.names.first);

        return duration;
    }

    private long testGsonPath() throws IOException {
        long start = System.nanoTime();
        PersonModelGenerated personModelGenerated = generatedMoshi.adapter(PersonModelGenerated.class)
                .fromJson(JSON_TEST_STRING);

        long duration = ((System.nanoTime() - start) / 1000000);
        System.out.println("gsonPathModel. Time taken: " + duration);

        PersonModelGenerated.PersonModel[] people = personModelGenerated.getPeople();
        Assert.assertEquals(PEOPLE_SIZE, people.length);
        Assert.assertEquals("Lachlan", people[0].getFirstName());

        return duration;
    }

}
