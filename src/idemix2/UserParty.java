package idemix2;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;


class usersk {
	public Element  K;
	public Element  x;
}

class traceinfo{
	Element T1,T2,T3;
}
public class UserParty {
	usersk  UserSk=new  usersk();
	cert Cert=new cert();
	traceinfo TraceInfo=new traceinfo();
	Element sk,r_4,Nym;
    public  void  Regester(IssuerParty Issuer,String name) {
    	UserSk=Issuer.AcceptRegester(name);   //java如何传递形参实参
    }
    public  void CertRequest(IssuerParty Issuer) {
    	sk=Issuer.GenZrRandom();
    	r_4=Issuer.GenZrRandom();
    	
    	Element x_left=Issuer.IssuerInfo.HSK.powZn(sk).getImmutable();
        Element x_right=Issuer.IssuerInfo.Hrand.powZn(r_4).getImmutable();
    	Nym=x_left.mul(x_right).getImmutable();
    	System.out.println("产生NYM成功");
    }
    public void  ReceiveCert(IssuerParty Issuer) {
    	Cert=Issuer.GenCert(Nym);
    }
    public boolean Prove(IssuerParty Issuer,int[]  HiddenIndices,int[] Disclosure ) {
    	   Pairing pairing= Issuer.SystemElement.pairing;
//    	   Element nonce=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_n=pairing.getZr().newRandomElement().getImmutable();
    	   Element r1_hat=pairing.getZr().newRandomElement().getImmutable();
    	   Element r2_hat=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_e=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_l1=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_l2=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_sp=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_sk=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_nr=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_alpha=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_beta=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_x=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_delta1=pairing.getZr().newRandomElement().getImmutable();
    	   Element r_delta2=pairing.getZr().newRandomElement().getImmutable();
    	   Element alpha=pairing.getZr().newRandomElement().getImmutable();
    	   Element beta=pairing.getZr().newRandomElement().getImmutable();
    	   Element x_left=Issuer.IssuerInfo.HSK.powZn(sk).getImmutable();
    	   Element x_right=Issuer.IssuerInfo.Hrand.powZn(r_n).getImmutable();
    	   Element Nym_r=x_left.mul(x_right).getImmutable();
    	   Element A_hat= Cert.A.powZn(r1_hat);
    	   Element A_hat_f=(A_hat.powZn(Cert.e.negate())).mul(Cert.B.powZn(r1_hat));
    	   Element B_hat=Issuer.IssuerInfo.Hrand.powZn(r2_hat.negate()).mul(Cert.B.powZn(r1_hat)).getImmutable();
    	   Element s_p=Cert.s.add(r_4).sub(r2_hat.div(r1_hat)).getImmutable();
    	   Element delta_1= UserSk.x.mul(alpha).getImmutable();
    	   Element delta_2= UserSk.x.mul(beta).getImmutable();
    	   final Element t_1=A_hat.powZn(r_e).mul(Issuer.IssuerInfo.Hrand.powZn(r_l1));
    	   Element t_2_hat=(Issuer.IssuerInfo.Hrand.powZn(r_sp)).mul(B_hat.powZn(r_l2)).mul(Issuer.IssuerInfo.HSK.powZn(r_sk));
    	   Element t_2=t_2_hat;
    	   Element[] r_a=Issuer.SetRandArr(Issuer.AttrName.length) ;
    	   for(int i: HiddenIndices) {
    		   t_2=t_2.mul(Issuer.HAttr[i].powZn(r_a[i]));
    	   }
    	   Element t_3=Issuer.IssuerInfo.HSK.powZn(r_sk).mul(Issuer.IssuerInfo.Hrand.powZn(r_nr));
    	   Element T_1=Issuer.GPK.u.powZn(alpha);
    	   Element T_2=Issuer.GPK.v.powZn(beta);
    	   Element T_3= UserSk.K.mul(Issuer.GPK.h.powZn(alpha.add(beta)));
    	   TraceInfo.T1=T_1;
    	   TraceInfo.T2=T_2;
    	   TraceInfo.T3=T_3;
    	   Element R_1=Issuer.GPK.u.powZn(r_alpha);
    	   Element R_2=Issuer.GPK.v.powZn(r_beta);
    	   Element R_3=(pairing.pairing(T_3,Issuer.SystemElement.g_2).powZn(r_x)).mul(pairing.pairing(Issuer.GPK.h,Issuer.IssuerInfo.PK).powZn(r_alpha.negate().sub(r_beta))).mul(pairing.pairing(Issuer.GPK.h,Issuer.SystemElement.g_2).powZn(r_delta1.negate().sub(r_delta2)));
    	   Element R_4=T_1.powZn(r_x).mul(Issuer.GPK.u.powZn(r_delta1.negate()));
    	   Element R_5=T_2.powZn(r_x).mul(Issuer.GPK.v.powZn(r_delta2.negate()));
    	   Element  c=pairing.getZr().newRandomElement().getImmutable();
    	   Element  s_sk=r_sk.add(c.mul(sk));
    	   Element  s_e=r_e.sub(c.mul(Cert.e));
    	   Element  s_l1=r_l1.add(c.mul(r2_hat));
    	   Element  s_l2=r_l2.sub(c.mul(r1_hat.invert()));
    	   Element  s_sp=r_sp.add(c.mul(s_p));
    	   Element  s_nr=r_nr.add(c.mul(r_n));
    	   Element  s_alpha=r_alpha.add(c.mul(alpha));
    	   Element  s_beta=r_beta.add(c.mul(beta));
    	   Element  s_x=r_x.add(c.mul(UserSk.x));
    	   Element  s_delta1=r_delta1.add(c.mul(delta_1));
    	   Element  s_delta2=r_delta2.add(c.mul(delta_2));
    	   //签名格式检测
    	   Element e_left=pairing.pairing(Issuer.IssuerInfo.PK,A_hat);
    	   Element e_right=pairing.pairing(Issuer.SystemElement.g_2,A_hat_f);
    	   if(e_left.equals(e_right)) {
    		   System.out.println("匿名签名格式检测正确");
    		   //对签名进行零知识证明
    		   Element  t1_ff=A_hat.powZn(s_e).mul(Issuer.IssuerInfo.Hrand.powZn(s_l1));
    		   Element  ccc=A_hat_f.div(B_hat);
    		   final Element  t1_f=t1_ff.div(ccc.powZn(c));
    		   Element  t2_fff=Issuer.IssuerInfo.Hrand.powZn(s_sp).mul(B_hat.powZn(s_l2)).mul(Issuer.IssuerInfo.HSK.powZn(s_sk));
    		   Element  t2_ff=t2_fff;
    		   for(int i: HiddenIndices) {
    			   Element  s_ai=r_a[i].add(c.mul(Issuer.attr[i]));
    		       t2_ff=t2_ff.mul(Issuer.HAttr[i].powZn(s_ai));
    		   }
    		   Element  t2_f_right=Issuer.SystemElement.g_1;
    		   for(int i:Disclosure) {
    			   t2_f_right=t2_f_right.mul(Issuer.HAttr[i].powZn(Issuer.attr[i]));
    		   }
    		   final Element t2_f=t2_f_right.powZn(c).mul(t2_ff);
    		   Element t3_ff=Issuer.IssuerInfo.HSK.powZn(s_sk).mul(Issuer.IssuerInfo.Hrand.powZn(s_nr));
    		   Element t3_f=t3_ff.div(Nym_r.powZn(c));
    		   Element R1_f=Issuer.GPK.u.powZn(s_alpha).mul(T_1.powZn(c.negate()));
    		   Element R2_f=Issuer.GPK.v.powZn(s_beta).mul(T_2.powZn(c.negate()));
    		   Element R3_f_left=(pairing.pairing(T_3,Issuer.SystemElement.g_2).powZn(s_x)).mul(pairing.pairing(Issuer.GPK.h,Issuer.IssuerInfo.PK).powZn(s_alpha.negate().sub(s_beta))).mul(pairing.pairing(Issuer.GPK.h,Issuer.SystemElement.g_2).powZn(s_delta1.negate().sub(s_delta2)));
    		   Element R3_f=R3_f_left.mul((pairing.pairing(T_3,Issuer.IssuerInfo.PK).div(pairing.pairing(Issuer.SystemElement.g_1,Issuer.SystemElement.g_2))).powZn(c));
    		   Element R4_f=T_1.powZn(s_x).mul(Issuer.GPK.u.powZn(s_delta1.negate()));
    		   Element R5_f=T_2.powZn(s_x).mul(Issuer.GPK.v.powZn(s_delta2.negate()));
    		   if(t_1.equals(t1_f)&t_2.equals(t2_f)&t_3.equals(t3_f)&&R_1.equals(R1_f)&&R_2.equals(R2_f)&&R_3.equals(R3_f)&&R_4.equals(R4_f)&&R_5.equals(R5_f)) {
    			   System.out.println("证书有效");
    			   return   true;
    		   }else {
    			   System.out.println("证书无效");
    			   return   false;
    		   }	  	  
    	   }else {
    		   System.out.println("匿名签名格式检测错误");
    		   return   false;
    	   }
    	
    }
    
}







