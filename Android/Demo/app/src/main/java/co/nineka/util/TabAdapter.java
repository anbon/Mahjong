package co.nineka.util;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class TabAdapter extends PagerAdapter
{

    //�s��View��
    private ArrayList<View> Views;
    //�s��Title  
    private ArrayList<String> Titles;


    public TabAdapter(ArrayList<View> Views,ArrayList<String> Titles)
    {
        this.Views=Views;
        this.Titles=Titles;
    }

    //PageTitle  
    @Override
    public CharSequence getPageTitle(int position)
    {
        return Titles.get(position);
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        ((ViewPager)container).removeView(Views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position)
    {
        ((ViewPager)container).addView(Views.get(position),0);
        return Views.get(position);
    }

    @Override
    public int getCount()
    {
        if(Views!=null)
        {
            return Views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view==(View)object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}  