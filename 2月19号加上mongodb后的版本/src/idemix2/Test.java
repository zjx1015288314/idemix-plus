package idemix2;

import it.unisa.dia.gas.jpbc.Element;

public class Test {
	public static void main(String[] args){
		String[]  a= {"name","tel","emails","home","age","sex","weight"};
		Utils.l=a.length;
		
		IssuerParty  Issuer=new  IssuerParty(a) ;
		VeriParty  Verifier=new  VeriParty();
		int[] hidden={1,3};
		int[] disclose={0,2,4,5,6};
		

		
		
		for(int i=0;i<1000;i++) {
			UserParty  User1=new  UserParty();
			User1.Regester(Issuer, "i",i,"i".concat("@buaa.edu.cn"));
			User1.CertRequest(Issuer);
			User1.ReceiveCert( Issuer);
		}
		UserParty  User=new  UserParty();
		User.Regester(Issuer, "Tom",18,"tom@buaa.edu.cn");
		User.CertRequest(Issuer);
		User.ReceiveCert( Issuer);
		for(int i=1001;i<2000;i++) {
			UserParty  User1=new  UserParty();
			User1.Regester(Issuer, "i",i,"i".concat("@buaa.edu.cn"));
			User1.CertRequest(Issuer);
			User1.ReceiveCert( Issuer);
		}

		
		
		Verifier.Verify( Issuer,User,hidden,disclose);

		Issuer.trace(User.TraceInfo);
//		Issuer.trace(User1.TraceInfo);
		
	}
}
