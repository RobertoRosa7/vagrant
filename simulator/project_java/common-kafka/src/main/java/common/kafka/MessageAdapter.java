package common.kafka;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MessageAdapter<T> implements JsonSerializer<Message<T>>, JsonDeserializer<Message<T>> {

  @Override
  public JsonElement serialize(Message<T> message, Type type, JsonSerializationContext context) {
    JsonObject obj = new JsonObject();
    obj.addProperty("type", message.getPayload().getClass().getName());
    obj.add("payload", context.serialize(message.getPayload()));
    obj.add("correlationId", context.serialize(message.getId()));
    return obj;
  }

  @Override
  public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
    var obj = jsonElement.getAsJsonObject();
    var payloadType = obj.get("type").getAsString();
    CorrelationId correlationId = context.deserialize(obj.get("correlationId"), CorrelationId.class);
    try {
      var payload = context.deserialize(obj.get("payload"), Class.forName(payloadType));
      return new Message<>(correlationId, payload);
    } catch (ClassNotFoundException e) {
      throw new JsonParseException(e);
    }
  }

}
