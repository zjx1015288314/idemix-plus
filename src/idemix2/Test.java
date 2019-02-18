package idemix2;

public class Test {
	public static void main(String[] args){
		String[]  a= {"name","tel","emails","home","age","sex","weight"};
		Utils.l=a.length;
		IssuerParty  Issuer=new  IssuerParty(a) ;
		VeriParty  Verifier=new  VeriParty();
		int[] hidden={1,3};
		int[] disclose={0,2,4,5,6};
		UserParty  User=new  UserParty();
		User.Regester(Issuer, "Tom");
		User.CertRequest(Issuer);
		User.ReceiveCert( Issuer);

		UserParty  User1=new  UserParty();
		User1.Regester(Issuer, "Alice");
		User1.CertRequest(Issuer);
		User1.ReceiveCert( Issuer);
	
		Verifier.Verify( Issuer,User1,hidden,disclose);
		Verifier.Verify( Issuer,User,hidden,disclose);
		
		Issuer.trace(User.TraceInfo);
		Issuer.trace(User1.TraceInfo);
		
	}
}
