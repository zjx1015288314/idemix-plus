package idemix2;

public class VeriParty {
	public boolean Verify(IssuerParty Issuer,UserParty User,int[]  HiddenIndices,int[] Disclosure ) {
		return User.Prove( Issuer,HiddenIndices,Disclosure);
	}
}
