package nz.ac.wgtn.swen301.a3.server;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * The type Logging event serializer.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class LoggingEventSerializer extends StdSerializer<LogEvent> {
    public LoggingEventSerializer() {
        this(null);
    }

    public LoggingEventSerializer(Class<LogEvent> t) {
        super(t);
    }

    @Override
    public void serialize(LogEvent event, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("id", String.valueOf(event.getId()));
        jgen.writeStringField("message", String.valueOf(event.getMessage()));
        jgen.writeStringField("timestamp", String.valueOf(event.getTimestamp()));
        jgen.writeStringField("thread", event.getThread());
        jgen.writeStringField("logger", event.getLogger());
        jgen.writeStringField("level", String.valueOf(event.getLevel()));
        jgen.writeStringField("errorDetails", event.getErrorDetails());
        jgen.writeEndObject();
    }
}