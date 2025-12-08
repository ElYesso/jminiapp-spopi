package com.jminiapp.examples.musicwrapped;

import com.jminiapp.core.adapters.JSONAdapter;

public class MusicWrappedJSONAdapter implements JSONAdapter<MusicWrappedState> {

	@Override
	public Class<MusicWrappedState> getstateClass() {
		return MusicWrappedState.class;
	}
}
