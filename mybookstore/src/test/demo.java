package test;

import org.junit.Test;

public class demo {
	/*
	 * https://www.yeepay.com/app-merchant-proxy/node?p0_Cmd=Buy&p1_MerId=10001126856&p2_Order=123&p3_Amt=0.01&p4_Cur=CNY&p5_Pid&p6_Pcat&p7_Pdesc&p8_Url=http://localhost/mybookstore/OrderServlet?method=back&p9_SAF&pa_MP&pd_FrpId=ICBC-NET-B2C&pr_NeedResponse=1&hmac=0d8af0a8e28aa31bb7257a17d1b15e42
	 */
	@Test
	public void fun() {
	String s=	PaymentUtil.buildHmac("Buy", "10001126856", "123", "0.01", "CNY", "",
				"", "","http://localhost/mybookstore/OrderServlet?method=back",
				"", "", "ICBC-NET-B2C", "1", "69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
	System.out.println(s);
	}
}
