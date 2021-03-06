package com.yl.dto;

import java.sql.Date;

public class Member_dto {
	private String mid;
	private String mpw;
	private String mname;
	private String mphone;
	private String maddress_basic;
	private String maddress_detail;
	private Date mbirth;
	private String memail;
	private String mgender;
	private int mpoint;
	private int mcumulative_buy;
	private Date mjoindate;
	private boolean mstatus;
	private String gname;
	private int cart_cnt;
	private int ad_email;
	private int ad_phone;
	private int ad_call;
	

	public Member_dto() {
		super();
	}

	public Member_dto(String mid, String mpw, String mname, String mphone, String maddress_basic,
			String maddress_detail, Date mbirth, String memail, String mgender, int mpoint, int mcumulative_buy,
			Date mjoindate, boolean mstatus, String gname, int cart_cnt, int ad_email, int ad_phone, int ad_call) {
		super();
		this.mid = mid;
		this.mpw = mpw;
		this.mname = mname;
		this.mphone = mphone;
		this.maddress_basic = maddress_basic;
		this.maddress_detail = maddress_detail;
		this.mbirth = mbirth;
		this.memail = memail;
		this.mgender = mgender;
		this.mpoint = mpoint;
		this.mcumulative_buy = mcumulative_buy;
		this.mjoindate = mjoindate;
		this.mstatus = mstatus;
		this.gname = gname;
		this.cart_cnt = cart_cnt;
		this.ad_email = ad_email;
		this.ad_phone = ad_phone;
		this.ad_call = ad_call;
	}

	public int getcart_cnt() {
		return cart_cnt;
	}

	public void setcart_cnt(int cart_cnt) {
		this.cart_cnt = cart_cnt;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getMpw() {
		return mpw;
	}

	public void setMpw(String mpw) {
		this.mpw = mpw;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getMaddress_basic() {
		return maddress_basic;
	}

	public void setMaddress_basic(String maddress_basic) {
		this.maddress_basic = maddress_basic;
	}

	public String getMaddress_detail() {
		return maddress_detail;
	}

	public void setMaddress_detail(String maddress_detail) {
		this.maddress_detail = maddress_detail;
	}

	public Date getMbirth() {
		return mbirth;
	}

	public void setMbirth(Date mbirth) {
		this.mbirth = mbirth;
	}

	public String getMemail() {
		return memail;
	}

	public void setMemail(String memail) {
		this.memail = memail;
	}

	public String getMgender() {
		return mgender;
	}

	public void setMgender(String mgender) {
		this.mgender = mgender;
	}

	public int getMpoint() {
		return mpoint;
	}

	public void setMpoint(int mpoint) {
		this.mpoint = mpoint;
	}

	public int getMcumulative_buy() {
		return mcumulative_buy;
	}

	public void setMcumulative_buy(int mcumulative_buy) {
		this.mcumulative_buy = mcumulative_buy;
	}

	public Date getMjoindate() {
		return mjoindate;
	}

	public void setMjoindate(Date mjoindate) {
		this.mjoindate = mjoindate;
	}

	public int getAd_email() {
		return ad_email;
	}

	public void setAd_email(int ad_email) {
		this.ad_email = ad_email;
	}

	public int getAd_phone() {
		return ad_phone;
	}

	public void setAd_phone(int ad_phone) {
		this.ad_phone = ad_phone;
	}

	public int getAd_call() {
		return ad_call;
	}

	public void setAd_call(int ad_call) {
		this.ad_call = ad_call;
	}

	public boolean isMstatus() {
		return mstatus;
	}

	public void setMstatus(boolean mstatus) {
		this.mstatus = mstatus;
	}

}
