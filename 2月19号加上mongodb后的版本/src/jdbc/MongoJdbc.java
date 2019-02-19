package jdbc;
import org.bson.Document;
import java.util.List;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
public class MongoJdbc{
	public static void main( String args[] ){
	      try{ 
	    	  IssuerParty Issuer=new IssuerParty();
	    	  
	    	  
	         // 连接到 mongodb 服务
	         MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	         
	         // 连接到数据库
	         MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");  
	         System.out.println("Connect to database successfully");
	         //创建集合userinfo
	         mongoDatabase.createCollection("userinfo11");
	         //获取集合userinfo
	         MongoCollection<Document> collection = mongoDatabase.getCollection("userinfo11");
	         System.out.println("集合 userinfo 选择成功");
	       //插入文档  
	         /** 
	         * 1. 创建文档 org.bson.Document 参数为key-value的格式 
	         * 2. 创建文档集合List<Document> 
	         * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document) 
	         * */
	         List<Document> documents = new ArrayList<Document>();  
	         for(int i=0;i<100000;i++) {
		         Document document = new Document("title", i).  
		                 append("description", i);
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
	         FindIterable<Document> findIterable1 = collection.find(Filters.eq("likes", 500));  
	         MongoCursor<Document> mongoCursor1 = findIterable1.iterator();  
	         while(mongoCursor1.hasNext()){  
	            System.out.println(mongoCursor1.next());  
	         } 
	         mongoDatabase.drop();
	      }catch(Exception e){
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      }
	   }
}