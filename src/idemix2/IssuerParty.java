package idemix2;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

class cert {
	public Element  A,B,e,s;
	public Element[] attr;
}
class ipkkeypair {
	 Element   ISK;
	 Element   PK;
	 String[]  AttrName;
	 Element[] HATttr;
	 Element   HSK;
	 Element   Hrand;
	 Element   g1_hat;
	 Element   g2_hat;
	 Element[] attr;
 }
class sys {
     Pairing  pairing;
	 Element  g_1;
	 Element  g_2;
}
class tk {
	public Element  delta1;
	public Element  delta2;
}
class gpk {
	public Element  u;
	public Element  v;
	public Element  h;
}

public class IssuerParty {
	sys  SystemElement=new  sys();
	ipkkeypair IssuerInfo=new  ipkkeypair();
    tk  TK=new tk();
    gpk GPK= new  gpk();
	Element r;
	String[]  AttrName;
    Element[] attr = new Element[Utils.l];
	Element[] HAttr = new Element[Utils.l];   //有问题，如何动态改变数组长度.如何在函数内部定义全局变量。
	IssuerParty(String[] attrname) {
		SetUp("a.properties");
		AttrName=attrname;
		GenIssuerKeyPair();
		GenTK();
		GenGPK();
	}
	public void SetUp(String perperties) {
		   Pairing pairing=PairingFactory.getPairing(perperties);
		   Element g_1=pairing.getG1().newRandomElement().getImmutable();
		   Element g_2=pairing.getG2().newRandomElement().getImmutable();
		   SystemElement.g_1=g_1;
		   SystemElement.g_2=g_2;
		   SystemElement.pairing=pairing;
	}
	public void  GenIssuerKeyPair() {
		   Pairing  pairing= SystemElement.pairing;
		   r=pairing.getZr().newRandomElement().getImmutable();
		   IssuerInfo.ISK=r;
		   IssuerInfo.PK=SystemElement.g_2.powZn(r).getImmutable();
		   IssuerInfo.AttrName=AttrName;
	       for(int  x=0;x<AttrName.length;x++) {
	    	   Element r_hat=pairing.getZr().newRandomElement().getImmutable();
	    	   HAttr[x]=SystemElement.g_1.powZn(r_hat).getImmutable();
	    	   attr[x]=pairing.getZr().newRandomElement().getImmutable();
	       }
	       IssuerInfo.HATttr=HAttr;
	       IssuerInfo.attr=attr;
	       IssuerInfo.HSK=SystemElement.g_1.powZn(pairing.getZr().newRandomElement()).getImmutable();
	       IssuerInfo.Hrand=SystemElement.g_1.powZn(pairing.getZr().newRandomElement()).getImmutable();
	       IssuerInfo.g1_hat=SystemElement.g_1.powZn(pairing.getZr().newRandomElement()).getImmutable();
	       IssuerInfo.g2_hat=IssuerInfo.g1_hat.powZn(IssuerInfo.ISK);
	       if(VerifyIssuerKey()) {
	    	   System.out.println("Issuser秘钥对生成功并验证通过");
	       }else {
	    	   System.out.println("Issuser秘钥对生成功但验证失败");
	       }
	   }
   public  boolean VerifyIssuerKey() {
		   Pairing  pairing= SystemElement.pairing;
		   Element  r_mao=pairing.getZr().newRandomElement().getImmutable();
		   Element  t1_hat=SystemElement.g_2.powZn(r_mao);
		   Element  t2_hat=IssuerInfo.g1_hat.powZn(r_mao);
		   String   con=t1_hat.add(t2_hat).toString();
		   Element  c_r= pairing.getZr().newElement().setFromHash(con.getBytes(), 0,18).getImmutable();
		   Element  temp=c_r.mul(IssuerInfo.ISK).getImmutable();
		   Element  s_r=r_mao.add(temp).getImmutable();
		   Element  t1_hat_f=SystemElement.g_2.powZn(s_r).mul(IssuerInfo.PK.powZn(c_r.negate())).getImmutable();
		   Element  t2_hat_f=IssuerInfo.g1_hat.powZn(s_r).mul(IssuerInfo.g2_hat.powZn(c_r.negate())).getImmutable();
		   String   con_hat=t1_hat_f.add(t2_hat_f).toString();
		   Element  c_r_hat= pairing.getZr().newElement().setFromHash(con_hat.getBytes(), 0, 18).getImmutable();
		   if(c_r_hat.isEqual(c_r)) {
			   return   true;
		   }else {
			   return   false;
		   }
	 }
 //generte TK
   public void  GenTK() {
   	   Pairing pairing= SystemElement.pairing;
       TK.delta1=pairing.getZr().newRandomElement().getImmutable();
   	   TK.delta2=pairing.getZr().newRandomElement().getImmutable();
   	   System.out.println("生成TK成功");
   }
      
  //generate GPK
   public void  GenGPK() {
        GPK.h=SystemElement.g_1.powZn(TK.delta1.mul(TK.delta2)).getImmutable();
        GPK.u=SystemElement.g_1.powZn(TK.delta2).getImmutable();
        GPK.v=SystemElement.g_1.powZn(TK.delta1).getImmutable();
        System.out.println("生成GPK成功");
   } 
	
	public usersk AcceptRegester(String name) {
		   Pairing pairing=SystemElement.pairing;
		   usersk  UserSk=new  usersk();
		   Element x=pairing.getZr().newRandomElement().getImmutable();
		   Element zz=IssuerInfo.ISK.add(x).getImmutable();
		   UserSk.K=SystemElement.g_1.powZn(zz.invert()).getImmutable();
		   UserSk.x=x;
		   System.out.println("用户注册成功");
		   StoreUserInfo(name);
		   return   UserSk;
	}
	public void  StoreUserInfo(String name) {
		System.out.println("存储用户信息:"+name);
	}
	public Element  GenZrRandom(){
	  	  Pairing pairing= SystemElement.pairing;
	  	  Element ZrRandom=pairing.getZr().newRandomElement().getImmutable();
	  	  return   ZrRandom;
    }
	public cert  GenCert(Element nym) {
		   Pairing pairing= SystemElement.pairing;
		   Element s=pairing.getZr().newRandomElement().getImmutable();
		   Element e=pairing.getZr().newRandomElement().getImmutable();
		   Element temp1=SystemElement.g_1.mul(nym).getImmutable();
		   Element temp2=IssuerInfo.Hrand.powZn(s).getImmutable();
		   Element B_1=temp1.mul(temp2).getImmutable();
//		   System.out.println(IssuerInfo.HATttr);
		   for(int x=0;x<IssuerInfo.HATttr.length;x++) {
			   B_1=B_1.mul(IssuerInfo.HATttr[x].powZn(IssuerInfo.attr[x])).getImmutable();
		   }
		   cert Cert=new cert();
		   Cert.B=B_1;
		   Cert.A=Cert.B.powZn((e.add(IssuerInfo.ISK)).invert()).getImmutable();
		   Cert.e=e;
		   Cert.s=s;
		   Cert.attr=IssuerInfo.attr;
		   System.out.println("证书生成成功");
		   return Cert;
		}
	public   Element[]  SetRandArr (int l) {
		Element[] r_a=new Element[l];
		  for(int i=0;i<l;i++) {
			   r_a[i]=SystemElement.pairing.getZr().newRandomElement().getImmutable();
		   }
		 return   r_a;
	}
	public Element  trace(traceinfo  TraceInfo) {
		Element  k_f=TraceInfo.T3.div(TraceInfo.T1.powZn(TK.delta1).mul(TraceInfo.T2.powZn(TK.delta2)));
		return k_f;
	}
}
