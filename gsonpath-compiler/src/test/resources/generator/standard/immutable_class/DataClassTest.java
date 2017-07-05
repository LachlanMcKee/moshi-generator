package generator.standard.immutable_class;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoGsonAdapter
public final class DataClassTest {
   @SerializedName("parent.child.")
   @NotNull
   private final String value1;
   private final boolean isBooleanTest1;
   @Nullable
   private final Boolean isBooleanTest2;

   @NotNull
   public final String getValue1() {
      return this.value1;
   }

   public final boolean isBooleanTest1() {
      return this.isBooleanTest1;
   }

   @Nullable
   public final Boolean isBooleanTest2() {
      return this.isBooleanTest2;
   }

   public DataClassTest(@NotNull String value1, boolean isBooleanTest1, @Nullable Boolean isBooleanTest2) {
      Intrinsics.checkParameterIsNotNull(value1, "value1");
      this.value1 = value1;
      this.isBooleanTest1 = isBooleanTest1;
      this.isBooleanTest2 = isBooleanTest2;
   }

   @NotNull
   public final String component1() {
      return this.value1;
   }

   public final boolean component2() {
      return this.isBooleanTest1;
   }

   @Nullable
   public final Boolean component3() {
      return this.isBooleanTest2;
   }
}
