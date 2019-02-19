package idemix2;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
public  class Db{
	 MongoCollection<Document> collection;
	 MongoDatabase mongoDatabase;
	 public void  StoreUserInfo(String Host,int   Port,String DataBase,String  Collection,Element K,userinfo  UserInfo) {
		try {
		// 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient(Host,Port );
        // 连接到数据库
        mongoDatabase = mongoClient.getDatabase(DataBase);  
        System.out.println("Connect to database successfully");
        //创建集合userinfo
//        mongoDatabase.createCollection("userinf11");
        //获取集合userinfo
        collection = mongoDatabase.getCollection(Collection);
//        System.out.println("集合 userinfo 选择成功");
        List<Document> documents = new ArrayList<Document>();    
	    Document document = new Document("name",UserInfo.name ). 
	    		     append("email",UserInfo.email).
	    		     append("age", UserInfo.age).
	                 append("K", K.toString());
//	    System.out.println("插入的k："+K);
	    documents.add(document);
        collection.insertMany(documents);  
//        System.out.println("文档插入成功");
    	}catch(Exception e){
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
}
	public void   Ask(Element K) {
		try {
		 //检索所有文档  
        /** 
        * 1. 获取迭代器FindIterable<Document> 
        * 2. 获取游标MongoCursor<Document> 
        * 3. 通过游标遍历检索出的文档集合 
        * */  
	   System.out.println("所有项目");
	   FindIterable<Document> findIterable = collection.find();  
	   MongoCursor<Document> mongoCursor = findIterable.iterator();  
	   while(mongoCursor.hasNext()){  
	       System.out.println(mongoCursor.next());  
	    }  			
        System.out.println("筛选项目");
        FindIterable<Document> findIterable1 = collection.find(Filters.eq("K",K.toString()));  
        MongoCursor<Document> mongoCursor1 = findIterable1.iterator();  
        while(mongoCursor1.hasNext()){  
           System.out.println(mongoCursor1.next());     
        } 
//        mongoDatabase.drop();
     }catch(Exception e){
        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
     }
	}
	
	
	
	
	
	public static void main( String args[] ){
	      try{ 
	    	  Pairing pairing=PairingFactory.getPairing("a.properties");
			   Element g_1=pairing.getG1().newRandomElement().getImmutable();
	           System.out.println("g_1_pre:"+g_1);
	           pairing.getZr().newElement().setFromHash(g_1.toBytes(), 0,18).getImmutable();
	           System.out.println("g_1    :"+g_1);
	         // 连接到 mongodb 服务
	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         
	         // 连接到数据库
	         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
	         System.out.println("Connect to database successfully");
	         //创建集合userinfo
//	         mongoDatabase.createCollection("userinf11");
	         //获取集合userinfo
	         MongoCollection<Document> collection = mongoDatabase.getCollection("userinf111111");
	         System.out.println("集合 userinfo 选择成功");
	       //插入文档  
	         /** 
	         * 1. 创建文档 org.bson.Document 参数为key-value的格式 
	         * 2. 创建文档集合List<Document> 
	         * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document) 
	         * */
	         List<Document> documents = new ArrayList<Document>(); 
	         Document documentx = new Document("title", 8).  
	                 append("description", g_1.toString());
	        documents.add(documentx);
	         for(int i=0;i<10;i++) {
		         Document document = new Document("title", i).  
		                 append("description", pairing.getG1().newRandomElement().getImmutable().toString());
		        documents.add(document);
	         }
	         collection.insertMany(documents);  
	         System.out.println("文档插入成功");
	         
	         //检索所有文档  
	         /** 
	         * 1. 获取迭代器FindIterable<Document> 
	         * 2. 获取游标MongoCursor<Document> 
	         * 3. 通过游标遍历检索出的文档集合 
	         * */  
	         System.out.println("所有项目");
	         FindIterable<Document> findIterable = collection.find();  
	         MongoCursor<Document> mongoCursor = findIterable.iterator();  
	         while(mongoCursor.hasNext()){  
	            System.out.println(mongoCursor.next());  
	         }  
	         System.out.println("筛选项目");
	         FindIterable<Document> findIterable1 = collection.find(Filters.eq("description",  g_1.toString()));  
	         MongoCursor<Document> mongoCursor1 = findIterable1.iterator();  
	         while(mongoCursor1.hasNext()){  
	            System.out.println(mongoCursor1.next());  
	         } 
//	         mongoDatabase.drop();
	      }catch(Exception e){
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      }
	   }
}