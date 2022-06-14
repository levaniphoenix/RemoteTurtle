package com.levaniphoenix.remoteturtle;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class test {

    @Test
    void testBlockClass(){
        Block block = new Block(10f,10f,10f);
        block.setBlockName("minecraft:stone");
        assertEquals("minecraft:stone", block.getBlockName());
    }

    @Test
    void testDatabaseConnection() throws SQLException {
        assertFalse(Database.Instance.isClosed());
    }

}