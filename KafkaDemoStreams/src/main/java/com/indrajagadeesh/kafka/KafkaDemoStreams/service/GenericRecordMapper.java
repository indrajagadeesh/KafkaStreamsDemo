package com.indrajagadeesh.kafka.KafkaDemoStreams.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class GenericRecordMapper {

    public GenericRecord mapObjectToRecord(Object object) {
        Assert.notNull(object, "object must not be null");
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        final GenericData.Record record = new GenericData.Record(schema);
        schema.getFields().forEach(r -> record.put(r.name(),
                PropertyAccessorFactory.forDirectFieldAccess(object).getPropertyValue(r.name())));
        return record;
    }

    public <T> T mapRecordToObject(GenericRecord record, T object) {
        Class<?> clazz = object.getClass();
        Assert.notNull(record, "record must not be null");
        Assert.notNull(object, "object must not be null");
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        Assert.isTrue(schema.getFields().equals(record.getSchema().getFields()), "Schema fields didn't match");
        record.getSchema().getFields().forEach(d ->
                PropertyAccessorFactory.forDirectFieldAccess(object)
                        .setPropertyValue(d.name(), record.get(d.name()) != null ?  getType(record.get(d.name()))
                                : null));
        return object;
    }

    private Object getType(Object o){
        log.debug(" object ----- {}", o.getClass().getName());
        if("org.apache.avro.util.Utf8".equals(o.getClass().getName()))
            return o.toString();
        else
            return o;
    }

}
