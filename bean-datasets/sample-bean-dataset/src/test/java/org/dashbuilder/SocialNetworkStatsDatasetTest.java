package org.dashbuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SocialNetworkStatsDatasetTest {

    @Test
    public void buildDataSetTest() throws Exception {
        var dataSet = new SocialNetworkDataGenerator().buildDataSet(null);
        System.out.println(dataSet.getColumns());
        assertEquals(27, dataSet.getRowCount());
    }

}