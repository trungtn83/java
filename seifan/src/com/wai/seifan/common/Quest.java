package com.wai.seifan.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Response;
import com.wai.seifan.dto.QuestInfo;

public abstract class Quest implements Url,  Http {
	protected Logger logger;

	protected AsyncHttpClient _client;
	protected String _cookie;
	
	protected boolean isRunable = true;

	public Quest() {
		AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder().setFollowRedirects(true);
//		ProxyServer proxyServer = new ProxyServer("192.168.1.166", 8080);
//		builder.setProxyServer(proxyServer);

		_client = new AsyncHttpClient(builder.setExecutorService(
			Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("seifan-async-http-client-%s").build())).build());
		
		logger = Logger.getLogger(Quest.class);
	}

	public abstract void execute() throws Exception;
	
	public abstract void doQuest(String level) throws Exception;
	
	public abstract QuestInfo getQuestInfo(Response response) throws Exception;

	public boolean login(String username, String password) throws Exception {
		// Call to login page to get cookie value
		Response firstTimeResponse = _client
				.prepareGet(URL_LOGIN)
				.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0")
//				.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//				.setHeader("Host", "chada.seifan.shopgautho.com")
				.execute()
				.get();
		Document document = Jsoup.parse(firstTimeResponse.getResponseBody());
		String cid = document.getElementById("CID").val();
		String submit = document.select("input[name=submit]").val();
		_cookie = StringUtils.split(firstTimeResponse.getHeader("Set-Cookie"), ";")[0];

		// Do the action login
		FluentStringsMap params = new FluentStringsMap();
		params.add("CID", cid);
		params.add("data[email]", username);
		params.add("data[pass]", password);
		params.add("submit", submit);
		
		Response homeResponse = _client
			.preparePost(URL_LOGIN)
			.setMethod("POST")
			.setHeaders(this.getCookiePOSTHeaders(_cookie))
			.setParameters(params)
			.execute()
			.get();
//		System.out.println(homeResponse.getResponseBody());
		String homeURI = homeResponse.getUri().toString();
		
		// Check if login is successful or not
		if (StringUtils.contains(homeURI, "user/home/login_bonus_redirect")) {
			logger.info("HI " + username + ", WELCOME TO SEIFAN WORLD");
			return true;
		}
		
		logger.info("PLEASE CHECK YOUR USERNAME OR PASSWORD");
		return false;
	}
	
	public Response getResponse(String url) throws Exception {
		return _client
				.preparePost(url)
				.setMethod("GET")
				.setHeaders(this.getCookiePOSTHeaders(_cookie))
				.execute()
				.get();
	}
	
	public Response getResponse(String url, String body) throws Exception {
		return _client
				.preparePost(url)
				.setMethod("POST")
				.setHeaders(this.getCookiePOSTHeaders(_cookie))
				.setBody(body)
				.execute()
				.get();
	}
	
	public long getPotentialPoint() throws Exception {
		Response response = this.getResponse(URL_USER_HOME);		
		Elements notElements = Jsoup.parse(response.getResponseBody()).select("ul.verticalOverlayMenu > li > a");
		if (notElements.size() > 0) {
			for (Element notElement : notElements) {
				if (StringUtils.contains(notElement.attr("href"), "/user/param/")) {
					return Long.parseLong(StringUtils.split(StringUtils.split(notElement.text(), "+")[1], ")")[0]);
				}
			}
		}
		return 0L;
	}
	
	public void usePotentialPoint(double _mana, double _attack, double _defense) throws Exception {
		String ID_MANA = "energy_max";
		String ID_ATTACK = "attack_energy_max";
		String ID_DEFENSE = "difense_energy_max";
		// get total potential point
		long totalPotentialPoint = this.getPotentialPoint();
		
		long manaPoint = (long) (((_mana > 0) ? _mana : Const.RATIO_POTENTIAL_POINT[0])*totalPotentialPoint);
		long attackPoint = (long) (((_attack > 0) ? _attack : Const.RATIO_POTENTIAL_POINT[1])*totalPotentialPoint);
		long defensePoint = (long) (((_defense > 0) ? _defense : Const.RATIO_POTENTIAL_POINT[2])*totalPotentialPoint);

		
		Response userParamReponse = this.getResponse("http://chada.seifan.shopgautho.com/user/param");
		Document doc = Jsoup.parse(userParamReponse.getResponseBody());
		String upLinkMana = "http://chada.seifan.shopgautho.com/" + doc.getElementById(ID_MANA).parent().attr("action");
		String upLinkAttack = "http://chada.seifan.shopgautho.com/" + doc.getElementById(ID_ATTACK).parent().attr("action");
		String upLinkDefense = "http://chada.seifan.shopgautho.com/" + doc.getElementById(ID_DEFENSE).parent().attr("action");

		// 1. cong diem linh luc
		this.getResponse(upLinkMana, "data%5B"+ID_MANA+"%5D="+ manaPoint);
		// 2. cong diem tinh luc tan cong
		this.getResponse(upLinkAttack, "data%5B"+ID_ATTACK+"%5D="+ attackPoint);
		// 3. cong diem tinh luc phong thu
		
		this.println("USED POTENTIAL POINT " + manaPoint + "(mana);" + attackPoint + "(attack);" + defensePoint + "(defense)");
	}
	
	protected FluentCaseInsensitiveStringsMap getCookiePOSTHeaders(String cookie) {
		FluentCaseInsensitiveStringsMap headers = new FluentCaseInsensitiveStringsMap();
		headers.add("Cookie", cookie);
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		return headers;
	}
	
	protected FluentCaseInsensitiveStringsMap getCookieGETHeaders(String cookie) {
		FluentCaseInsensitiveStringsMap headers = new FluentCaseInsensitiveStringsMap();
		headers.add("Cookie", cookie);
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
		return headers;
	}
	
	protected void println(String str) {
		String currentDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date());
		System.out.println("[" + currentDate + "] " + str);
	}

	protected boolean isRunable() {
		return isRunable;
	}

	protected void setRunable(boolean isRunable) {
		this.isRunable = isRunable;
	}
}
