package org.linxiangyu.mmbaby.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sudongsheng on 14-3-20.
 */
public class Record {
    public int primary_key;
    public String title;
    public String content;
    public String time;
    public String field;
    public byte photo[][]=new byte[6][];
    public int rating;
}
