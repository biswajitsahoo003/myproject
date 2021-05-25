package com.tcl.dias.l2oworkflowutils.service.v1;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TATServiceTest {

    @Autowired
    private TATService tatService;

    @Test
    public void shouldReturnDueDateBasedOnTatAndGroupName() {

        Timestamp taskCreateTime = Timestamp.valueOf(LocalDateTime.of(2019, 2, 11, 9, 0));
        Timestamp actual = tatService.calculateDueDate(1440, "Group1", taskCreateTime);
        Timestamp expected = Timestamp.valueOf(LocalDateTime.of(2019, 2, 13, 17, 0));

        assertEquals(expected, actual);
    }

    @Test
    public void shouldIgnoreHolidayOnDueDateCalculation() {

        Timestamp taskCreateTime = Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 11, 45));
        Timestamp actual = tatService.calculateDueDate(1440, "Group1", taskCreateTime);
        Timestamp expected = Timestamp.valueOf(LocalDateTime.of(2019, 2, 20, 11, 45));

        assertEquals(expected, actual);
    }

    @Test
    public void shouldHandleDueDateCalculationWhenTaskCreatedOnNonBusinessHour() {

        Timestamp taskCreateTime = Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 1, 45));
        Timestamp actual = tatService.calculateDueDate(180, "Group1", taskCreateTime);
        Timestamp expected = Timestamp.valueOf(LocalDateTime.of(2019, 2, 15, 12, 0));

        assertEquals(expected, actual);
    }

}