package me.ahfun.mmbaby.database;

import me.ahfun.mmbaby.R;

/**
 * Created by Inner on 14-4-21.
 */
public interface BabyData {
    //品德方面的内容morality
    public final static String[] morality_goodsName=new String[]{"日照","泉水","大剪刀","铲子"};
    public final static int[] morality_goodsImage = new  int[]{R.drawable.sunlight_normal,R.drawable.water_normal,
            R.drawable.scissors_normal,R.drawable.shovel_normal};
    public final static int[] morality_buttonImage= new int[]{R.drawable.sunlight_button,R.drawable.water_button,
            R.drawable.scissors_button,R.drawable.shovel_button};
    public final static int[] morality_needMoney = new int[]{50,80,80,60};
    public final static int[] morality_up = new int[]{50,80,80,60};
    public final static int[] sunflower_image = new int[]{R.drawable.sunflower_image_0,
            R.drawable.sunflower_image_1,R.drawable.sunflower_image_2};
    public final static int[][] morality_animation = new int[][]{
            {R.anim.sunlight_anima0,R.anim.water_anima0,R.anim.scissor_anima0,R.anim.shovel_anima0},
            {R.anim.sunlight_anima1,R.anim.water_anima1,R.anim.scissor_anima1,R.anim.shovel_anima1},
            {R.anim.sunlight_anima2,R.anim.water_anima2,R.anim.scissor_anima2,R.anim.shovel_anima2}
    };


    //体育方面的内容physical
    public final static String[] physical_goodsName=new String[]{"精品粮","谷物","篮球","溜冰鞋"};
    public final static int[] physical_goodsImage = new  int[]{R.drawable.foodstuff_normal,R.drawable.grain_normal,
            R.drawable.basketball_normal,R.drawable.roller_skates_normal};
    public final static int[] physical_buttonImage= new int[]{R.drawable.foodstuff_button,R.drawable.grain_button,
            R.drawable.basketball_button,R.drawable.roller_skates_button};
    public final static int[] physical_needMoney = new int[]{60,80,40,75};
    public final static int[] physical_up = new int[]{60,80,40,75};
    public final static int[] chick_image = new int[]{R.drawable.chick_image_0,R.drawable.chick_image_1,
            R.drawable.chick_image_2};
    public final static int[][] physical_animation0 = new int[][]{
        {R.anim.chick_grain_anima0,R.anim.wheat_anima0,R.anim.basketball_anima0,R.anim.skate_anima0},
        {R.anim.chick_grain_anima1,R.anim.wheat_anima1,R.anim.basketball_anima1,R.anim.skate_anima1},
        {R.anim.chick_grain_anima2,R.anim.wheat_anima2,R.anim.basketball_anima2,R.anim.skate_anima2}};



    //智力方面的内容intelligence
    public final static String[] intelligence_goodsName=new String[]{"精品粮","骨头","智慧书","魔方"};
    public final static int[] intelligence_goodsImage = new  int[]{R.drawable.foodstuff_normal,R.drawable.bone_normal,
            R.drawable.book_normal,R.drawable.magic_square_normal};
    public final static int[] intelligence_buttonImage= new int[]{R.drawable.foodstuff_button,R.drawable.bone_button,
            R.drawable.book_button,R.drawable.magic_square_button};
    public final static int[] intelligence_needMoney = new int[]{50,85,65,70};
    public final static int[] intelligence_up = new int[]{50,85,65,70};
    public final static int[] dog_image = new int[]{R.drawable.dog_image_0,
            R.drawable.dog_image_1,R.drawable.dog_image_2};
    public final static int[][] intelligence_animation = new int[][]{
            {R.anim.food_anima0,R.anim.bone_anima0,R.anim.book_anima0,R.anim.cube_anima0},
            {R.anim.food_anima1,R.anim.bone_anima1,R.anim.book_anima1,R.anim.cube_anima1},
            {R.anim.food_anima2,R.anim.bone_anima2,R.anim.book_anima2,R.anim.cube_anima2}};
}
