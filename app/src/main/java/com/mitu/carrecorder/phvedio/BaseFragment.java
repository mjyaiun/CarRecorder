package com.mitu.carrecorder.phvedio;


import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if(getView()!=null){
			getView().setVisibility(menuVisible?View.VISIBLE:View.GONE);
		}
	}
}
