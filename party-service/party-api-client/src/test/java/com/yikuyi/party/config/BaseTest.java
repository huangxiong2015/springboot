package com.yikuyi.party.config;

import org.junit.Before;

import com.yikuyi.party.PartyClientBuilder;

public class BaseTest {

	public PartyClientBuilder partyClient;

	@Before
	public void init() {
		partyClient = new PartyClientBuilder("http://localhost:27082");
		//partyClient = new PartyClientBuilder("http://192.168.1.110:27082");
	}
}