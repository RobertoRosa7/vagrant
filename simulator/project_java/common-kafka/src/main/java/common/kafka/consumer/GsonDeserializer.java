package common.kafka.consumer;

import common.kafka.Message;
import common.kafka.MessageAdapter;
import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonDeserializer<T> implements Deserializer<Message<T>> {
  /*
   * Deprecate Method
   */
  // public static final String TYPE_CONFIG = "com.example.type_config";
  // private final Gson gson = new GsonBuilder().create();
  // private Class<T> type;
  // @Override
  // public void configure(Map<String, ?> configs, boolean isKey) {
  // String typeName = String.valueOf(configs.get(TYPE_CONFIG));
  // try {
  // this.type = (Class<T>) Class.forName(typeName);
  // } catch (ClassNotFoundException e) {
  // throw new RuntimeException("Type for deserialize does not exist");
  // }
  // }

  private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter<>()).create();

  @Override
  public Message<T> deserialize(String s, byte[] bytes) {
    return this.gson.fromJson(new String(bytes), Message.class);
  }
}
