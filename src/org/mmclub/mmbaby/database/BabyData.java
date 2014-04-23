package org.mmclub.mmbaby.database;

import org.mmclub.mmbaby.R;

/**
 * Created by Inner on 14-4-21.
 */
public interface BabyData {
    //品德方面的内容morality
    public final static String morality_goodsName[]=new String[]{"日照","泉水","大剪刀","铲子"};
    public final static int[] morality_ownedNumber = new int[]{0,0,0,0};
    public final static int[] morality_goodsImage = new  int[]{R.drawable.sunlight_normal,R.drawable.water_normal,
            R.drawable.scissors_normal,R.drawable.shovel_normal};
    public final static int morality_buttonImage[]= new int[]{R.drawable.sunlight_button,R.drawable.water_button,
            R.drawable.scissors_button,R.drawable.shovel_button};

    //体育方面的内容physical
    public final static String physical_goodsName[]=new String[]{"精品粮","谷物","篮球","溜冰鞋"};
    public final static int[] physical_ownedNumber = new int[]{0,0,0,0};
    public final static int[] physical_goodsImage = new  int[]{R.drawable.foodstuff_normal,R.drawable.grain_normal,
            R.drawable.basketball_normal,R.drawable.roller_skates_normal};
    public final static int physical_buttonImage[]= new int[]{R.drawable.foodstuff_button,R.drawable.grain_button,
            R.drawable.basketball_button,R.drawable.roller_skates_button};

    //智力方面的内容intelligence
    public final static String intelligence_goodsName[]=new String[]{"精品粮","骨头","智慧书","魔方"};
    public final static int[] intelligence_ownedNumber = new int[]{0,0,0,0};
    public final static int[] intelligence_goodsImage = new  int[]{R.drawable.foodstuff_normal,R.drawable.bone_normal,
            R.drawable.book_normal,R.drawable.magic_square_normal};
    public final static int intelligence_buttonImage[]= new int[]{R.drawable.foodstuff_button,R.drawable.bone_button,
            R.drawable.book_button,R.drawable.magic_square_button};
}
