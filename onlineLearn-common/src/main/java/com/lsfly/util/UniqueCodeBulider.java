package com.lsfly.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class UniqueCodeBulider {
	
//	public static void main(String[] args){
//		System.out.println(getBillCode(BillType.sales));
//	}

	public static enum BillType{
		sales, //销售订单  门店、气站一样
		purchase, //采购
		transport, //配送
		filling,//充装
		deposit,//押金
		rent,//租金
		getMoney,//收款
		payMoney,//付款
		material,//库存
		debtMoney, //欠款
		retailCustomerCode,//零售客户
		wholesaleCustomerCode,//批发客户
		insure,//保险
		call, //来电
		other; //其他
	}
	
	public static enum BillCodeDateType{
		year("yy"),
		yearMonth("yyMM"),
		yearMonthDay("yyMMdd");
		
		private String val;
		private BillCodeDateType(String val){
			this.val = val;
		}
		public String toString(){
			return this.val;
		}
	}
	
	/**
	 * 生成BIllCode
	 * @return
	 */
	public final static String getBillCode(BillType billType){
		//前缀
		String prefix = "w";
		//长度
		int codeLength = 9;
		//日期格式  默认 年月
		BillCodeDateType dateType = BillCodeDateType.year;
		
		switch (billType) {
		case sales:
			prefix = "1";
			break;
		case purchase:
			prefix = "2";
			break;
		case transport:
			prefix = "3";
			break;
		case filling:
			prefix = "";
			dateType = BillCodeDateType.yearMonthDay;
			codeLength = 7;
			break;
		case deposit:
			prefix = "4";
			break;
		case rent:
			prefix = "5";
			break;
		case getMoney:
			prefix = "6";
			break;
		case payMoney:
			prefix = "7";
			break;
		case material:
			prefix = "8";
			break;
		case debtMoney:
			prefix = "9";
			break;	
		case retailCustomerCode:
			prefix = "";
			break;
		case wholesaleCustomerCode:
			prefix = "";
			break;
		case insure:
			prefix = "B";
			break;
		case call:
			prefix = "";
			dateType = BillCodeDateType.yearMonthDay;
			break;
		case other:
			prefix = "";
			dateType = BillCodeDateType.yearMonthDay;
			break;
		default:
			break;
		}
		
		String serialNo = SerialNo(dateType, codeLength);
		
		return prefix + serialNo;
	}
	
	private static String SerialNo(BillCodeDateType dateType, int codeLength){
		if(codeLength == 0) codeLength = 9;
		
		int uuid = Math.abs(UUID.randomUUID().toString().hashCode());
		String id = String.valueOf(uuid);
		if(id.length() > codeLength){
			//截取
			id = id.substring(0, codeLength);
		}else if(id.length() < codeLength){
			//补0
			int index = codeLength - id.length();
			for(int i=0;i<index;i++){
				id += "0";
			}
		}
   	 	DateFormat df = new SimpleDateFormat(dateType.toString()); //yyMMdd yyMM
		String dateTime = df.format(new Date());
		return dateTime+id;
	}
}
