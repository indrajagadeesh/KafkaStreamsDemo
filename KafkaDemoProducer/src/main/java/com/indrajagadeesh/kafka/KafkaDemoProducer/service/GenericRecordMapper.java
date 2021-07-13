package com.indrajagadeesh.kafka.KafkaDemoProducer.service;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
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
        Assert.notNull(record, "record must not be null");
        Assert.notNull(object, "object must not be null");
        final Schema schema = ReflectData.get().getSchema(object.getClass());
        Assert.isTrue(schema.getFields().equals(record.getSchema().getFields()), "Schema fields didn't match");
        record.getSchema().getFields().forEach(d ->
                PropertyAccessorFactory.forDirectFieldAccess(object)
                        .setPropertyValue(d.name(), record.get(d.name()) == null ? record.get(d.name()) : record.get(d.name()).toString()));
        return object;
    }

}
