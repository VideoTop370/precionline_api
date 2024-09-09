package com.workeache.precionline.api.demo.batch;

import com.workeache.precionline.api.demo.persistence.entities.DataApiReeEntity;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import org.slf4j.Logger;

import java.util.List;

public class LoggingItemWriter implements ItemWriter<List<DataApiReeEntity>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingItemWriter.class);

    @Override
    public void write(Chunk<? extends List<DataApiReeEntity>> chunk) throws Exception {
        LOGGER.info("Writting prices: {} ", chunk);
    }
}
