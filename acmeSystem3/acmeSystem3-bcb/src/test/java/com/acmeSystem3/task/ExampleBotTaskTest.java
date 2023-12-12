package com.acmeSystem3.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.WorkerJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;

import static org.assertj.core.api.Assertions.assertThat;

@WorkerJUnitConfig
class ExampleBotTaskTest {

    @Test
    @DisplayName("should run ExampleBotTask")
    void shouldRunExampleBotTask(BotTaskFactory botTaskFactory) {
        // when
        String actualResult = botTaskFactory.fromClass(ExampleBotTask.class)
                .buildAndRun()
                .getFirstRecord()
                .get("example_bot_task_output");

        // then
        assertThat(actualResult).isEqualTo("completed_successfully");
    }

}
