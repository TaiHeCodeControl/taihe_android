package com.taihe.eggshell.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihe.eggshell.R;

public class IndexFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.chats_fragment, null) ;
		return v ;
	}
}