package com.levaniphoenix.remoteturtle;

import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.AssertJUnit.*;

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