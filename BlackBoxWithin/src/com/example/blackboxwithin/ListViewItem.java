package com.example.blackboxwithin;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;
/*
 * @설치대기@ - ListViewItem
 */
public class ListViewItem {

	private String name;
	private String needs;
	private String reserve;
	private String addr;
	private String phone1;
	private String blackbox;
	private String channel;
	private String state;
	private String accept_num;
	private String accept_date;
	private String phone2;
	private String company_pay;
	private String field_pay;
	private String pic0, pic1, pic2;

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	private String car;

	private long n, d, r;
	private int nYear, nMonth, nDay;

	public String getAccept_num() {
		return accept_num;
	}

	public void setAccept_num(String accept_num) {
		this.accept_num = accept_num;
	}

	public String getAccept_date() {
		return accept_date;
	}

	public void setAccept_date(String accept_date) {
		this.accept_date = accept_date;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getCompany_pay() {
		return company_pay;
	}

	public void setCompany_pay(String company_pay) {
		this.company_pay = company_pay;
	}

	public String getField_pay() {
		return field_pay;
	}

	public void setField_pay(String field_pay) {
		this.field_pay = field_pay;
	}

	public String getPic0() {
		return pic0;
	}

	public void setPic0(String pic0) {
		this.pic0 = pic0;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public void setPic2(String pic2) {
		this.pic2 = pic2;
	}

	public ListViewItem() {

	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNeeds(String needs) {
		if (needs.equalsIgnoreCase("") == true || needs.equalsIgnoreCase("null") == true) {
			this.needs = "";
		} else {
			this.needs = needs;

		}
	}

	public void setReserve(String reserve) {
		this.state = reserve;
		if (reserve.equalsIgnoreCase("") == true || reserve.equalsIgnoreCase("null") == true) {
			this.reserve = "";
		} else
			this.reserve = reserve;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setBlackbox(String blackbox) {
		this.blackbox = blackbox;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public String getNeeds() {
		return needs;
	}

	public String getReserve() {
		return reserve;
	}

	public String getAddr() {
		return addr;
	}

	public String getPhone1() {
		return phone1;
	}

	public String getBlackbox() {
		return blackbox;
	}

	public String getChannel() {
		return channel;
	}

	public String getState() {
		if (reserve.equalsIgnoreCase("") == true || reserve.equalsIgnoreCase("null") == true
				|| reserve.equalsIgnoreCase("0") == true) {
			Calendar dCal = Calendar.getInstance();

			Calendar nCal = Calendar.getInstance();

			nYear = nCal.get(Calendar.YEAR);
			nMonth = nCal.get(Calendar.MONTH);
			nDay = nCal.get(Calendar.DAY_OF_MONTH);

			dCal.set(Integer.parseInt(accept_date.substring(0, 4)), Integer.parseInt(accept_date.substring(4, 6)),
					Integer.parseInt(accept_date.substring(6, 8)));
			nCal.set(nYear, nMonth + 1, nDay);

			d = dCal.getTimeInMillis() / (24L * 60L * 60L * 1000L);
			n = nCal.getTimeInMillis() / (24L * 60L * 60L * 1000L);

			r = (int) (n - d);

			if (r > 5 || r == 5)
				state = "dday";
			else if (r < 5 && r > 2 || r == 2)
				state = "hurry";
			else if (r < 2 && r > 0 || r == 0)
				state = "good";
		} else {
			state = "nope";
		}

		return state;
	}

}
